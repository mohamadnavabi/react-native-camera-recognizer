import UIKit

@objc
class ViewController:  UIViewController {
    var fullDetection: Bool
    var resolve: RCTPromiseResolveBlock? = nil
    
    init(fullDetection: Bool, resolve: RCTPromiseResolveBlock?) {
        self.fullDetection = fullDetection
        self.resolve = resolve

        super.init(nibName: nil, bundle: nil)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) is not supported")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        presentCamera(for: [paymentCardRecognizer()])
    }
    
    private var cameraViewController: RecognizerCameraViewController?
        
    func presentCamera(for recognizers: [Recognizer]) {
        let extractionViewController = RecognizerCameraViewController(recognizers: recognizers)
        
        let vc = DemoBoxOverlayViewController()
        let nvc = DemoScannerNavigationController(rootViewController: extractionViewController)

        extractionViewController.addOverlayViewController(vc)
        extractionViewController.modalPresentationStyle = .fullScreen
        extractionViewController.navigationItem.rightBarButtonItem = nvc.closeButton()
        
        cameraViewController = extractionViewController
        self.present(nvc, animated: false, completion: nil)
    }

    func dismissCamera() {
        cameraViewController?.captureState = .stopped
        dismiss(animated: false, completion: nil)
    }
}

extension ViewController {
    fileprivate func paymentCardRecognizer() -> PaymentCardRecognizer {
        let fullDetection = fullDetection
        let config = PaymentCardRecognizer.Configuration(
            detectingElements: fullDetection ? [.pan, .expDate, .cvv2, .iban] : .pan,
            resultsHandler: cardResultsHandler
        )
        return PaymentCardRecognizer(configuration: config)
    }
    
    fileprivate func cardResultsHandler(_ image: Image, _ cardInfo: CardInfo) {
        var data: NSDictionary

        if fullDetection {
            data = ["PAN": cardInfo.pan, "CVV2": cardInfo.cvv2, "EXP": cardInfo.exp, "IBAN": cardInfo.iban]
        } else {
            data = ["PAN": cardInfo.pan]
        }
        
        if resolve != nil {
            resolve!(data)
            self.dismissCamera()
        }
    }
}
