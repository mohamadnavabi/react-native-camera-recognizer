#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(CameraRecognizer, NSObject)

RCT_EXTERN_METHOD(paymentCard: (BOOL)fullDetection withResolver:(RCTPromiseResolveBlock)resolve withRejecter:(RCTPromiseRejectBlock)reject)

@end
