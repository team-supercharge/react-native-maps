//
//  AIRGoogleMapGroundOverlayManager.m
//  AirMaps
//
//  Created by Gergo Nemeth on 7/7/17.
//

#import "AIRGoogleMapGroundOverlayManager.h"
#import "AIRGoogleMapMarker.h"
#import <MapKit/MapKit.h>
#import <React/RCTUIManager.h>
#import "AIRGoogleMapGroundOverlay.h"
#import "RCTConvert+AirMap.h"

@implementation AIRGoogleMapGroundOverlayManager

RCT_EXPORT_MODULE()

- (UIView *)view
{
  AIRGoogleMapGroundOverlay *overlay = [AIRGoogleMapGroundOverlay new];
  overlay.bridge = self.bridge;
  return overlay;
}

RCT_EXPORT_VIEW_PROPERTY(identifier, NSString)
RCT_EXPORT_VIEW_PROPERTY(northeastCoordinate, CLLocationCoordinate2D)
RCT_EXPORT_VIEW_PROPERTY(southwestCoordinate, CLLocationCoordinate2D)
RCT_REMAP_VIEW_PROPERTY(image, imageSrc, NSString)
RCT_EXPORT_VIEW_PROPERTY(contentText, NSString)
RCT_REMAP_VIEW_PROPERTY(isNumberShown, numberShown, BOOL)
RCT_EXPORT_VIEW_PROPERTY(textSize, double)
RCT_EXPORT_VIEW_PROPERTY(textColor, NSString)
RCT_EXPORT_VIEW_PROPERTY(zIndex, NSInteger)
RCT_EXPORT_VIEW_PROPERTY(transparency, double)

RCT_EXPORT_VIEW_PROPERTY(onPress, RCTBubblingEventBlock)

@end
