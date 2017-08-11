//
//  AIRGoogleMapGroundOverlay.m
//  AirMaps
//
//  Created by Gergo Nemeth on 7/7/17.
//

#import "AIRGoogleMapGroundOverlay.h"
#import <GoogleMaps/GoogleMaps.h>
#import <React/RCTImageLoader.h>
#import <React/RCTUtils.h>
#import "AIRGMSMarker.h"
#import "AIRGoogleMapCallout.h"
#import "DummyView.h"
#import "AIRGMSGroundOverlay.h"

@interface AIRGoogleMapGroundOverlay ()

@property (nonatomic, strong) UIImage *image;
@property (nonatomic, strong) UIImage *imageWithText;
@property (nonatomic, strong) AIRGMSGroundOverlay *realOverlay;

@end

@implementation AIRGoogleMapGroundOverlay
{
	NSString *_identifier;
    RCTImageLoaderCancellationBlock _reloadImageCancellationBlock;
	RCTBubblingEventBlock _onPress;
}

#pragma mark - Setters

- (void)setIdentifier:(NSString *)identifier {
	_identifier = identifier;
	_realOverlay.identifier = identifier;
}

- (NSString *)identifier {
	return _realOverlay.identifier;
}

- (void)setMap:(AIRGoogleMap *)map {
    _map = map;

    [self showGroundOverlayIfPossible];
}

- (void)setNortheastCoordinate:(CLLocationCoordinate2D)northeastCoordinate
{
    _northeastCoordinate = northeastCoordinate;

    [self showGroundOverlayIfPossible];
}

- (void)setSouthwestCoordinate:(CLLocationCoordinate2D)southwestCoordinate
{
    _southwestCoordinate = southwestCoordinate;

    [self showGroundOverlayIfPossible];
}

- (void)setImageSrc:(NSString *)imageSrc
{
    _imageSrc = imageSrc;

    if (_reloadImageCancellationBlock)
    {
        _reloadImageCancellationBlock();
        _reloadImageCancellationBlock = nil;
    }

    _reloadImageCancellationBlock =
    [_bridge.imageLoader loadImageWithURLRequest:[RCTConvert NSURLRequest:_imageSrc]
                                            size:self.bounds.size
                                           scale:RCTScreenScale()
                                         clipped:YES
                                      resizeMode:RCTResizeModeCenter
                                   progressBlock:nil
                                partialLoadBlock:nil
                                 completionBlock:^(NSError *error, UIImage *image)
     {
         if (error)
         {
             NSLog(@"%@", error);
             return;
         }

         dispatch_async(dispatch_get_main_queue(), ^{
             self.image = image;
             [self generateImageWithTextIfPossible];
             [self showGroundOverlayIfPossible];
         });
     }];
}

- (void)setTransparency:(double)transparency
{
    _transparency = transparency;
    _realOverlay.opacity = 1.0 - transparency;
}

- (void)setZIndex:(NSInteger)zIndex
{
    _zIndex = zIndex;
    _realOverlay.zIndex = (float)zIndex;
}

- (void)setNumberShown:(BOOL)numberShown
{
    _numberShown = numberShown;

    if (numberShown)
    {
        [self showText];
    }
    else
    {
        [self hideText];
    }
}

- (void)setContentText:(NSString *)contentText
{
    _contentText = contentText;

    [self generateImageWithTextIfPossible];
}

#pragma mark - Private methods

- (void)generateImageWithTextIfPossible
{
    if (!_image || !_contentText)
    {
        return;
    }

    UIView *containerView = [UIView new];
    containerView.backgroundColor = [UIColor clearColor];

    UIImageView *imageView = [[UIImageView alloc] initWithImage:_image];
    containerView.frame = imageView.bounds;

    [containerView addSubview:imageView];

    UILabel *label = [[UILabel alloc] initWithFrame:containerView.bounds];
    label.textColor = [[self class] colorFromHexString:_textColor];
    label.textAlignment = NSTextAlignmentCenter;
    label.text = _contentText;
    label.font = [UIFont systemFontOfSize:(_textSize ?: 20.0)];
    [containerView addSubview:label];

    // generate image
    UIGraphicsBeginImageContextWithOptions(containerView.bounds.size, NO, 0.0);
    [containerView.layer renderInContext:UIGraphicsGetCurrentContext()];
    UIImage *resultImage = UIGraphicsGetImageFromCurrentImageContext();
    _imageWithText = resultImage;
    UIGraphicsEndImageContext();
}

- (void)showGroundOverlayIfPossible
{
    if (_realOverlay)
    {
        _realOverlay.map = nil;
        _realOverlay = nil;
    }

    if (_map && _northeastCoordinate.latitude && _southwestCoordinate.latitude)
    {
        GMSCoordinateBounds *overlayBounds = [[GMSCoordinateBounds alloc] initWithCoordinate:_southwestCoordinate
                                                                                  coordinate:_northeastCoordinate];
		_realOverlay = [AIRGMSGroundOverlay groundOverlayWithBounds:overlayBounds icon:(_numberShown ? _imageWithText : _image)];
		_realOverlay.tappable = YES;
		_realOverlay.onPress = _onPress;
		_realOverlay.identifier = _identifier;
        _realOverlay.map = _map;
    }
}

#pragma mark - Toggle Overlay Text

- (void)showText
{
    _realOverlay.icon = _imageWithText;
}

- (void)hideText
{
    _realOverlay.icon = _image;
}

#pragma mark - Events

- (void)setOnPress:(RCTBubblingEventBlock)onPress
{
	_onPress = onPress;
	_realOverlay.onPress = onPress;
}

- (RCTBubblingEventBlock)onPress
{
	return _realOverlay.onPress;
}

#pragma mark - Helpers

+ (UIColor *)colorFromHexString:(NSString *)hexString
{
    if (!hexString)
    {
        return [UIColor whiteColor];
    }

    unsigned rgbValue = 0;
    NSScanner *scanner = [NSScanner scannerWithString:hexString];
    [scanner setScanLocation:1]; // bypass '#' character
    [scanner scanHexInt:&rgbValue];
    return [UIColor colorWithRed:((rgbValue & 0xFF0000) >> 16)/255.0 green:((rgbValue & 0xFF00) >> 8)/255.0 blue:(rgbValue & 0xFF)/255.0 alpha:1.0];
}

@end
