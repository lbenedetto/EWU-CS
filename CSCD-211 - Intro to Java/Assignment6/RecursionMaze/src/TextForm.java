import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextForm extends JPanel {
    public JPanel fieldPanel;
    public JTextField field;
    public JLabel lab;

    // Create a form with the specified labels, tooltips, and sizes.
    public TextForm(String label, int width, String tip) {
        //Create a JPanel with a 1x2 GridLayout
        fieldPanel = new JPanel(new GridLayout(1, 2));
        add(fieldPanel);
        //Create a text field with specified parameters
        field = new JTextField(JTextField.RIGHT);
        field.setToolTipText(tip);
        field.setColumns(width);
        field.setAlignmentX(Component.RIGHT_ALIGNMENT);
        //Create a label for the field
        lab = new JLabel(label, JLabel.LEFT);
        lab.setLabelFor(field);
        lab.setAlignmentX(Component.LEFT_ALIGNMENT);
        //Add the label and field to the panel
        fieldPanel.add(lab);
        fieldPanel.add(field);
    }

    public String getText() {
        return field.getText();
    }
}