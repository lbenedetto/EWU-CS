import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

class GUI implements ActionListener, ItemListener {
    //Buttons
    private JButton buttonNewList;
    private JButton buttonSortList;
    private JButton buttonPrintList;
    private JButton buttonRemoveOdds;
    private JButton buttonDeleteList;
    private JButton buttonDeleteValue;
    //Check Boxes
    private JCheckBox checkBoxReverseOrder;
    private JCheckBox checkBoxNthTerm;
    //Structure
    private JPanel panelMenu;
    private JPanel panelOutput;
    //Forms
    private TextForm formSize;
    private TextForm formNthTerm;
    private TextForm formValueToDelete;
    private JTextArea textAreaOutput;
    //Other vars
    private boolean optionReverseOrder;
    private boolean optionPrintNthTerm;
    private LinkedList linkedList;

    public GUI() {
        //Create output field
        createOutputField();
        //Create the components of the menu
        createMenuComponents();
        //Assemble the components of the menu
        assembleMenu();
        //Assemble the final product
        assembleGUI();
    }

    private void createOutputField() {
        textAreaOutput = new JTextArea();
        textAreaOutput.setEditable(false);
        textAreaOutput.setText("LinkedList will be" + "\n" + "Printed here");
        textAreaOutput.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPaneOutput = new JScrollPane(textAreaOutput);
        scrollPaneOutput.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneOutput.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panelOutput = new JPanel();
        panelOutput.setLayout(new GridLayout(1, 1));
        panelOutput.add(scrollPaneOutput);
        panelOutput.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelOutput.setPreferredSize(new Dimension(175, 300));
    }

    private void createMenuComponents() {
        buttonNewList = newButton(" New List ");
        buttonSortList = newButton("Sort List");
        buttonPrintList = newButton("Print List");
        buttonRemoveOdds = newButton("Remove Odds");
        buttonDeleteValue = newButton("Delete Value");
        buttonDeleteList = newButton("Delete List");
        checkBoxReverseOrder = newCheckBox("Reverse Order");
        checkBoxNthTerm = newCheckBox("Nth Term");
        formSize = new TextForm("Size: ", ListTester.getRandomNumber());
        formNthTerm = new TextForm("Nth Term: ", ListTester.getRandomNumber());
        formValueToDelete = new TextForm("Value: ", ListTester.getRandomNumber());
    }

    private void assembleMenu() {
        panelMenu = new JPanel();
        panelMenu.setPreferredSize(new Dimension(150, 350));
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setMaximumSize(new Dimension(300, 500));
        panelMenu.add(formSize);
        panelMenu.add(buttonNewList);
        panelMenu.add(buttonSortList);
        panelMenu.add(buttonPrintList);
        panelMenu.add(checkBoxReverseOrder);
        panelMenu.add(checkBoxNthTerm);
        panelMenu.add(formNthTerm);
        panelMenu.add(buttonRemoveOdds);
        panelMenu.add(buttonDeleteValue);
        panelMenu.add(formValueToDelete);
        panelMenu.add(buttonDeleteList);
        panelMenu.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private void assembleGUI() {
        JFrame frameMain = new JFrame("LinkedList");
        frameMain.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frameMain.setLayout(new BoxLayout(frameMain.getContentPane(), BoxLayout.X_AXIS));
        frameMain.add(panelMenu);
        frameMain.add(panelOutput);
        frameMain.pack();
        frameMain.setVisible(true);
    }


    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        try {
            if (source.equals(buttonNewList)) {
                Integer size = Integer.parseInt(formSize.getText());
                if (size < 0) throw new NumberFormatException();
                linkedList = new LinkedList();
                for (int i = 0; i < size; i++) {
                    linkedList.add(ListTester.randomInteger());
                }
            }
            if (linkedList == null)
                textAreaOutput.setText("Must create list first");
            else if (source.equals(buttonPrintList)) {
                showList(linkedList);
            } else if (source.equals(buttonSortList)) {
                linkedList.sort();
                showList(linkedList);
            } else if (source.equals(buttonRemoveOdds)) {
                showList(linkedList.evenNumbersOnly());
            } else if (source.equals(buttonDeleteValue)) {
                if(linkedList.remove(Integer.parseInt(formValueToDelete.getText())))
                    showList(linkedList);
                else
                    textAreaOutput.setText("Value is not in list");
            } else if (source.equals(buttonDeleteList)) {
                linkedList = null;
                textAreaOutput.setText("LinkedList deleted");
            }
        } catch (NumberFormatException ex) {
            textAreaOutput.setText("Invalid input");
        }
    }

    private void showList(LinkedList linkedList) {
        int term = 1;
        String out;
        if (optionPrintNthTerm)
            term = Integer.parseInt(formNthTerm.getText());
        if (optionReverseOrder)
            out = linkedList.toStringReverse(term);
        else
            out = linkedList.toString(term);
        textAreaOutput.setText(out);
    }

    public void itemStateChanged(ItemEvent e) {
        Object source = e.getSource();
        if (e.getStateChange() == ItemEvent.DESELECTED) {
            if (source.equals(checkBoxReverseOrder)) {
                optionReverseOrder = false;
            }
            if (source.equals(checkBoxNthTerm)) {
                optionPrintNthTerm = false;
            }
        }
        if (e.getStateChange() == ItemEvent.SELECTED) {
            if (source.equals(checkBoxReverseOrder)) {
                optionReverseOrder = true;
            }
            if (source.equals(checkBoxNthTerm)) {
                optionPrintNthTerm = true;
            }
        }
    }

    private JButton newButton(String s) {
        JButton newButton = new JButton(s);
        newButton.setPreferredSize(new Dimension(125, 25));
        newButton.addActionListener(this);
        newButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        return newButton;
    }

    private JCheckBox newCheckBox(String s) {
        JCheckBox newCheckBox = new JCheckBox(s);
        newCheckBox.setPreferredSize(new Dimension(125, 25));
        newCheckBox.addItemListener(this);
        newCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        return newCheckBox;
    }

    class TextForm extends JPanel {
        public final JPanel fieldPanel;
        public final JTextField field;
        public final JLabel lab;

        // Create a form with the specified labels, tooltips, and sizes.
        public TextForm(String label, int width) {
            //Create a JPanel with a 1x2 GridLayout
            fieldPanel = new JPanel(new GridLayout(1, 2));
            add(fieldPanel);
            //Create a text field with specified parameters
            field = new JTextField(JTextField.RIGHT);
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
}
