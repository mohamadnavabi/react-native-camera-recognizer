package com.reactnativecamerarecognizer;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;


@ReactModule(name = CameraRecognizerModule.NAME)
public class CameraRecognizerModule extends ReactContextBaseJavaModule {
  public static final String NAME = "CameraRecognizer";
  ReactApplicationContext context = getReactApplicationContext();

  public CameraRecognizerModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void paymentCard(boolean fullDetection, Promise promise) {
    Intent intent = new Intent(context, LaunchActivity.class);
    if (intent.resolveActivity(context.getPackageManager()) != null){
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
    }

    promise.resolve("test");
  }

  public static native int nativeMultiply(int a, int b);
}
