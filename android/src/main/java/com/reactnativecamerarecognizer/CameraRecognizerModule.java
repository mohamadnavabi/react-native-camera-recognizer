package com.reactnativecamerarecognizer;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.module.annotations.ReactModule;

import java.util.HashMap;
import java.util.Map;

import ir.mohammadnavabi.paymentcardscanner.DebitCard;
import ir.mohammadnavabi.paymentcardscanner.ScanActivity;


@ReactModule(name = CameraRecognizerModule.NAME)
public class CameraRecognizerModule extends ReactContextBaseJavaModule implements ActivityEventListener  {
  public static final String NAME = "CameraRecognizer";
  ReactApplicationContext context = getReactApplicationContext();
  Promise mPromise = null;

  public CameraRecognizerModule(ReactApplicationContext reactContext) {
    super(reactContext);

    this.context.addActivityEventListener(this);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void paymentCard(boolean fullDetection, Promise promise) {
    mPromise = promise;
    Activity currentActivity  = getCurrentActivity();

    if (currentActivity == null) {
      promise.reject("E_ACTIVITY_DOES_NOT_EXIST", "Activity doesn't exist");
      return;
    }

    try {
    ScanActivity.start(currentActivity);
  } catch (Exception e) {
      promise.reject("E_FAILED_TO_SHOW_PICKER", e);
      mPromise = null;
  }
  }


//  @Override
//  public void onActivityResult(int requestCode, int resultCode, Intent data) {
////		super.onActivityResult(requestCode, resultCode, data);
//
//    if (ScanActivity.isScanResult(requestCode)) {
//      if (resultCode == Activity.RESULT_OK && data != null) {
//        DebitCard scanResult = ScanActivity.debitCardFromResult(data);
//
//        Log.d("cardNumber", scanResult.number);
//        Log.d("cardExpiryMonth", String.valueOf(scanResult.expiryMonth));
//        Log.d("cardExpiryYear", String.valueOf(scanResult.expiryYear));
//      } else if (resultCode == ScanActivity.RESULT_CANCELED) {
//        boolean fatalError = data.getBooleanExtra(ScanActivity.RESULT_FATAL_ERROR, false);
//        if (fatalError) {
//          Log.d("TAG", "fatal error");
//        } else {
//          Log.d("TAG", "The user pressed the back button");
//        }
//      }
//    }
//  }

  @ReactMethod
  public WritableMap dataExtractor(Intent data) {
    DebitCard scanResult = ScanActivity.debitCardFromResult(data);

    HashMap<String, String> hm = new HashMap<>();
    hm.put("PAN", scanResult.number);
    hm.put("CVV", "");
    hm.put("EXP", String.valueOf(scanResult.expiryMonth) + "/" + String.valueOf(scanResult.expiryYear));
    WritableMap map = new WritableNativeMap();
    for (Map.Entry<String, String> entry : hm.entrySet()) {
      map.putString(entry.getKey(), entry.getValue());
    }

    return map;
  }

  @Override
  public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
    if (ScanActivity.isScanResult(requestCode)) {
      if (resultCode == Activity.RESULT_OK && data != null) {
        mPromise.resolve(dataExtractor(data));
      } else if (resultCode == ScanActivity.RESULT_CANCELED) {
        boolean fatalError = data.getBooleanExtra(ScanActivity.RESULT_FATAL_ERROR, false);
        if (fatalError) {
          mPromise.reject("fatal error");
        } else {
          mPromise.reject("The user pressed the back button");
        }
      }
    }

    mPromise = null;
  }

  @Override
  public void onNewIntent(Intent intent) { }
}
