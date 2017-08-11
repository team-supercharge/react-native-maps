package com.airbnb.android.react.maps;


import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

import javax.annotation.Nullable;

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

  @Override
  @Nullable
  public Map getExportedCustomDirectEventTypeConstants() {
    return MapBuilder.of(
        "onPress", MapBuilder.of("registrationName", "onPress"));
  }

  @ReactProp(name = "northeastCoordinate")
  public void setNortheastCoordinate(AirMapGroundOverlay view, ReadableMap coordinates) {
    view.setNortheastCoordinates(coordinates);
  }

  @ReactProp(name = "southwestCoordinate")
  public void setSouthwestCoordinate(AirMapGroundOverlay view, ReadableMap coordinates) {
    view.setSouthwestCoordinates(coordinates);
  }

  @ReactProp(name = "image")
  public void setImage(AirMapGroundOverlay view, String source) {
    view.setImage(source);
  }

  @ReactProp(name = "zIndex", defaultFloat = 1.0f)
  public void setZIndex(AirMapGroundOverlay view, float zIndex) {
    view.setZIndex(zIndex);
  }

  @ReactProp(name = "transparency")
  public void setTransparency(AirMapGroundOverlay view, float transparency) {
    view.setTransparency(transparency);
  }

  @ReactProp(name = "contentText")
  public void setTextToDisplay(AirMapGroundOverlay view, String contentText) {
    view.setContentText(contentText);
  }

  @ReactProp(name = "textSize", defaultInt = 20)
  public void setTextSize(AirMapGroundOverlay view, int textSize) {
    view.setTextSize(textSize);
  }

  @ReactProp(name = "textColor")
  public void setTextColor(AirMapGroundOverlay view, String textColor) {
    view.setTextColor(textColor);
  }

  @ReactProp(name = "isNumberShown")
  public void setIsNumberShown(AirMapGroundOverlay view, boolean isNumberShown) {
    view.setTextVisibility(isNumberShown);
  }
}
