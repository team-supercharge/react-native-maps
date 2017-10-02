package com.airbnb.android.react.maps;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;

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
  private static final String EMPTY_STRING = "";

  private Bitmap groundOverlayBitmap;
  private GroundOverlay groundOverlay;
  private TextPaint textPaint;
  private BitmapDescriptor iconBitmapDescriptor;

  private LatLng northeastCoordinate;
  private LatLng southwestCoordinate;

  private String contentText;

  private float transparency;
  private float zIndex;

  private boolean isTextDisplayed;

  public AirMapGroundOverlay(Context context) {
    super(context);

    this.textPaint = new TextPaint();
  }

  @Override
  public void addToMap(GoogleMap map) {
    groundOverlay = map.addGroundOverlay(createGroundOverlayOptions());
    groundOverlay.setClickable(true);
  }

  @Override
  public void removeFromMap(GoogleMap map) {
    groundOverlay.remove();
  }

  @Override
  public Object getFeature() {
    return groundOverlay;
  }

  public void setTextVisibility(boolean textVisibility) {
    isTextDisplayed = textVisibility;

    updateGroundOverlay();
  }

  public LatLngBounds getLatLngBounds() {
    return new LatLngBounds(southwestCoordinate, northeastCoordinate);
  }

  public void setNortheastCoordinates(ReadableMap coordinate) {
    northeastCoordinate = map(coordinate);
      if (groundOverlay != null) {
      groundOverlay.setPositionFromBounds(new LatLngBounds(southwestCoordinate, northeastCoordinate));
    }
  }

  public void setSouthwestCoordinates(ReadableMap coordinate) {
    southwestCoordinate = map(coordinate);
    if (groundOverlay != null) {
      groundOverlay.setPositionFromBounds(new LatLngBounds(southwestCoordinate, northeastCoordinate));
    }
  }

  public void setTextSize(int textSize) {
    textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize,
        getResources().getDisplayMetrics()));

    updateGroundOverlay();
  }

  public void setTextColor(String textColor) {
    int color;
    try {
      color = Color.parseColor(textColor);
    } catch (IllegalArgumentException exception) {
      color = Color.WHITE;
    }

    textPaint.setColor(color);

    updateGroundOverlay();
  }

  public void setContentText(String contentText) {
    this.contentText = contentText;

    updateGroundOverlay();
  }

  public void setTransparency(float transparency) {
    this.transparency = transparency;

    if (groundOverlay != null) {
      groundOverlay.setTransparency(transparency);
    }
  }

  public void setZIndex(float zIndex) {
    this.zIndex = zIndex;

    if (groundOverlay != null) {
      groundOverlay.setZIndex(zIndex);
    }
  }

  public void setImage(String uri) {
    if (uri.startsWith("http://") || uri.startsWith("https://") ||
        uri.startsWith("file://")) {
      ImageRequest imageRequest = ImageRequestBuilder
          .newBuilderWithSource(Uri.parse(uri))
          .build();

      ImagePipeline imagePipeline = Fresco.getImagePipeline();
      DataSource<CloseableReference<CloseableImage>> dataSource =
          imagePipeline.fetchDecodedImage(imageRequest, this);

      dataSource.subscribe(new BaseBitmapDataSubscriber() {
        @Override protected void onNewResultImpl(@Nullable Bitmap bitmap) {
          if (groundOverlayBitmap != null) {
            groundOverlayBitmap.recycle();
          }
          groundOverlayBitmap = Bitmap.createBitmap(bitmap);

          updateGroundOverlay();
        }

        @Override
        protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
        }
      }, UiThreadImmediateExecutorService.getInstance());
    } else {
      loadBitmap(uri);
    }
  }

  private LatLng map(ReadableMap coordinate) {
    return new LatLng(coordinate.getDouble(LATITUDE), coordinate.getDouble(LONGITUDE));
  }

  private GroundOverlayOptions createGroundOverlayOptions() {
    GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions();
    groundOverlayOptions
        .positionFromBounds(new LatLngBounds(southwestCoordinate, northeastCoordinate));

    drawTextOnBitmap();
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

  private void loadBitmap(String name) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inMutable = true;

    if (groundOverlayBitmap != null) {
      groundOverlayBitmap.recycle();
    }
    groundOverlayBitmap =
        BitmapFactory.decodeResource(getResources(), getDrawableResourceByName(name), options);

    updateGroundOverlay();
  }

  private void updateGroundOverlay() {
    if (groundOverlay != null && groundOverlayBitmap != null) {
      drawTextOnBitmap();

      groundOverlay.setImage(iconBitmapDescriptor);
    }
  }

  private void drawTextOnBitmap() {
    Bitmap mutable = getOriginalGroundOverlayBitmap();
    if (isTextDisplayed) {
      final String textToDisplay = TextUtils.isEmpty(contentText) ? EMPTY_STRING : contentText;
      final Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
      final float width = textPaint.measureText(textToDisplay);
      final float height = Math.abs(fontMetrics.ascent);

      Canvas canvas = new Canvas(mutable);
      canvas.drawText(textToDisplay, canvas.getWidth() / 2 - width / 2,
          canvas.getHeight() / 2 + height / 2,
          textPaint);
    }

    iconBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(mutable);
  }

  private Bitmap getOriginalGroundOverlayBitmap() {
    return groundOverlayBitmap.copy(Bitmap.Config.ARGB_8888, true);
  }
}
