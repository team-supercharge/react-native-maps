//
//  AIRGMSGroundOverlay.h
//  AirMaps
//
//  Created by Gergo Nemeth on 7/7/17.
//

#import <GoogleMaps/GoogleMaps.h>
#import <React/UIView+React.h>

@class GMSGroundOverlay;

@interface AIRGMSGroundOverlay : GMSGroundOverlay

@property (nonatomic, strong) NSString *identifier;
@property (nonatomic, copy) RCTBubblingEventBlock onPress;

@end
