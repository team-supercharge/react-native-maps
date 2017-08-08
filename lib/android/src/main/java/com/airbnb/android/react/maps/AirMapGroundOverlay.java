package com.airbnb.android.react.maps;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import javax.annotation.Nullable;

public class AirMapGroundOverlay extends AirMapFeature {

  private static final String LONGITUDE = "longitude";
  private static final String LATITUDE = "latitude";
  private static final String DRAWABLE_RESOURCE_TYPE = "drawable";

  private GroundOverlay groundOverlay;

  private LatLng northeastCoordinate;
  private LatLng southwestCoordinate;
  private int width;
  private int height;

  private float transparency;

  private float zIndex;

  private BitmapDescriptor iconBitmapDescriptor;

  private DataSource<CloseableReference<CloseableImage>> dataSource;

  public AirMapGroundOverlay(Context context) {
    super(context);
  }

  @Override
  public void addToMap(GoogleMap map) {
    groundOverlay = map.addGroundOverlay(createGroundOverlayOptions());
  }

  @Override
  public void removeFromMap(GoogleMap map) {
    groundOverlay.remove();
  }

  @Override
  public Object getFeature() {
    return groundOverlay;
  }

  public void setnortheastCoordinates(ReadableMap coordinate) {
    northeastCoordinate = map(coordinate);
  }

  public void setsouthwestCoordinates(ReadableMap coordinate) {
    southwestCoordinate = map(coordinate);
  }

  public void setImage(String uri) {
    if (uri.startsWith("http://") || uri.startsWith("https://") ||
        uri.startsWith("file://")) {
      ImageRequest imageRequest = ImageRequestBuilder
          .newBuilderWithSource(Uri.parse(uri))
          .build();

      ImagePipeline imagePipeline = Fresco.getImagePipeline();
      dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);

      dataSource.subscribe(new BaseBitmapDataSubscriber() {
        @Override protected void onNewResultImpl(@Nullable Bitmap bitmap) {
          iconBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        }

        @Override
        protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
        }
      }, UiThreadImmediateExecutorService.getInstance());
    } else {
      iconBitmapDescriptor = getBitmapDescriptorByName(uri);
    }
  }

  public void setTransparency(float transparency) {
    this.transparency = transparency;
  }

  public void setZIndex(float zIndex) {
    this.zIndex = zIndex;
  }

  private LatLng map(ReadableMap coordinate) {
    return new LatLng(coordinate.getDouble(LATITUDE), coordinate.getDouble(LONGITUDE));
  }

  private GroundOverlayOptions createGroundOverlayOptions() {
    GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions();
    groundOverlayOptions
        .positionFromBounds(new LatLngBounds(southwestCoordinate, northeastCoordinate));

    groundOverlayOptions.image(iconBitmapDescriptor);
    groundOverlayOptions.zIndex(zIndex);
    groundOverlayOptions.transparency(transparency);

    return groundOverlayOptions;
  }

  private int getDrawableResourceByName(String name) {
    return getResources().getIdentifier(
        name,
        DRAWABLE_RESOURCE_TYPE,
        getContext().getPackageName());
  }

  private BitmapDescriptor getBitmapDescriptorByName(String name) {
    return BitmapDescriptorFactory.fromResource(getDrawableResourceByName(name));
  }
}
