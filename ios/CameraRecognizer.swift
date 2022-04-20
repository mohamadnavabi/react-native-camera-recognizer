@objc(CameraRecognizer)
class CameraRecognizer: NSObject {

    @objc(paymentCard:withResolver:withRejecter:)
    func paymentCard(fullDetection: CBool, resolve: @escaping RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock) -> Void {
        let modelVC: UIViewController
        modelVC = ViewController(fullDetection: fullDetection, resolve: resolve)

        DispatchQueue.main.async {
          let navController = UINavigationController(rootViewController: modelVC)
          navController.modalPresentationStyle = .fullScreen
          let topController = UIApplication.topMostViewController()
          topController?.present(navController, animated: true, completion: nil)
        }
    }
}

extension UIApplication {
    class func topMostViewController(controller: UIViewController? = UIApplication.shared.keyWindow?.rootViewController) -> UIViewController? {

            if let navigationController = controller as? UINavigationController {
              return topMostViewController(controller: navigationController.visibleViewController)
            }

            if let tabController = controller as? UITabBarController {
              if let selected = tabController.selectedViewController {
                return topMostViewController(controller: selected)
              }
            }

            if let presented = controller?.presentedViewController {
              return topMostViewController(controller: presented)
            }

        return controller
    }
}
