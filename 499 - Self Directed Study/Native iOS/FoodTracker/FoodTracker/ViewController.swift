import UIKit

class ViewController: UIViewController, UITextFieldDelegate {
    //MARK: Properties
    
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var labelMealName: UILabel!
    @IBOutlet weak var textFieldMealName: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        textFieldMealName.delegate = self
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    //MARK: UITextFieldDelegate
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        //Hide keyboard
        textFieldMealName.resignFirstResponder()
        return true
    }
    func textFieldDidEndEditing(_ textField: UITextField) {
        labelMealName.text = textFieldMealName.text
    }
    //MARK: Actions
    @IBAction func buttonSetText(_ sender: UIButton) {
        labelMealName.text = "Default Text"
    }
}

