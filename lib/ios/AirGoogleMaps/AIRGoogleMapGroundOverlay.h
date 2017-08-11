//
//  AIRGoogleMapGroundOverlay.h
//  AirMaps
//
//  Created by Gergo Nemeth on 7/7/17.
//

#import <GoogleMaps/GoogleMaps.h>
#import <React/RCTBridge.h>
#import "AIRGMSMarker.h"
#import "AIRGoogleMap.h"
#import "AIRGoogleMapCallout.h"

@interface AIRGoogleMapGroundOverlay : UIView

@property (nonatomic, weak) RCTBridge *bridge;
@property (nonatomic, strong) AIRGoogleMap* map;

@property (nonatomic, strong) NSString *identifier;
@property (nonatomic, assign) CLLocationCoordinate2D northeastCoordinate;
@property (nonatomic, assign) CLLocationCoordinate2D southwestCoordinate;
@property (nonatomic, copy) NSString *imageSrc;
@property (nonatomic, assign) double transparency;
@property (nonatomic, assign) NSInteger zIndex;

@property (nonatomic, assign) BOOL numberShown;
@property (nonatomic, copy) NSString *contentText;
@property (nonatomic, assign) double textSize;
@property (nonatomic, copy) NSString *textColor;

@property (nonatomic, copy) RCTBubblingEventBlock onPress;

@end
