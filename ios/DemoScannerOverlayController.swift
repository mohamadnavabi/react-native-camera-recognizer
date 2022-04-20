//

import UIKit
import AVFoundation

class DemoScannerNavigationController: UINavigationController {
	
	override func viewDidLoad() {
		super.viewDidLoad()
		
		modalPresentationStyle = .fullScreen
		
		setupNavigationBar()
	}
	
	private func setupNavigationBar() {
		let appearance = UINavigationBarAppearance()
		appearance.configureWithTransparentBackground()
		
		navigationBar.standardAppearance = appearance
        navigationBar.barStyle = .black
		navigationBar.isTranslucent = true
		navigationBar.tintColor = .white
	}
    
    func closeButton() -> UIBarButtonItem {
        .init(barButtonSystemItem: .close, target: self, action: #selector(closeTapped))
    }
    
    @objc func closeTapped() {
        DispatchQueue.main.async {
         let topController = UIApplication.topMostViewController()
         topController?.dismiss(animated: true, completion: nil)
        }
    }
}