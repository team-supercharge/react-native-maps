package com.airbnb.android.react.maps;


import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

public class AirMapGroundOverlayManager
    extends ViewGroupManager<AirMapGroundOverlay> {

  private static final String REACT_CLASS = "AIRMapGroundOverlay";

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  protected AirMapGroundOverlay createViewInstance(ThemedReactContext reactContext) {
    return new AirMapGroundOverlay(reactContext);
  }

  @ReactProp(name = "northeastCoordinate")
  public void setNortheastCoordinate(AirMapGroundOverlay view, ReadableMap coordinates) {
    view.setnortheastCoordinates(coordinates);
  }

  @ReactProp(name = "southwestCoordinate")
  public void setSouthwestCoordinate(AirMapGroundOverlay view, ReadableMap coordinates) {
    view.setsouthwestCoordinates(coordinates);
  }

  @ReactProp(name = "image")
  public void setImage(AirMapGroundOverlay view, String source) {
    view.setImage(source);
  }

  @ReactProp(name = "zIndex", defaultFloat = 1.0f)
  public void setZIndex(AirMapGroundOverlay view, float zIndex) {
    view.setZIndex(zIndex);
  }

  @ReactProp(name = "transparency", defaultFloat = 0.0f)
  public void setTransparency(AirMapGroundOverlay view, float transparency) {
    view.setTransparency(transparency);
  }
}
