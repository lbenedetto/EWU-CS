
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

/**
 * This is a custom JDialog box that builds a linkedList which can then be retrieved after it is closed
 * The structure is a JPanel which contains 3 JPanels.
 * panelMenu contains the buttons to select which method to test
 * panelInput contains the JTextFields where the user types their input
 * panelOutput is where the current linked list is shown
 */
class ListBuilderGUI extends JDialog implements ActionListener, ChangeListener {
    //<editor-fold desc="Variables">
    //Buttons
    private JButton buttonDone;
    private JButton buttonNewList;
    private JButton buttonPrintList;
    private JButton buttonClearList;
    private JRadioButton buttonAddValue;
    private JRadioButton buttonAddValueIndex;
    private JButton buttonAddAll;
    private JRadioButton buttonAddAllIndex;
    private JRadioButton buttonContains;
    private JButton buttonContainsAll;
    private JRadioButton buttonGetAtIndex;
    private JButton buttonHashCode;
    private JRadioButton buttonIndexOf;
    private JRadioButton buttonLastIndexOf;
    private JButton buttonIsEmpty;
    private JRadioButton buttonRemoveIndex;
    private JRadioButton buttonRemoveObject;
    private JButton buttonRemoveAll;
    private JButton buttonRetainAll;
    private JRadioButton buttonSetIndexTo;
    private JButton buttonSize;
    private JRadioButton buttonSubList;
    private JButton buttonEquals;
    private ButtonGroup buttonGroup;
    private JButton buttonRun;
    private JButton buttonBack;
    //Structure
    private JPanel panelMenu;
    private JPanel panelInput;
    private JPanel panelOutput;
    //Forms
    private TextForm formString;
    private TextForm formInt1;
    private TextForm formInt2;
    private JTextArea textAreaOutput;
    //Other vars
    private LinkedList<String> linkedList;
    // </editor-fold>

    public ListBuilderGUI(JFrame owner, String title) {
        this(owner, title, null);
    }

    public ListBuilderGUI(JFrame owner, String title, LinkedList<String> list) {
        super(owner, true);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setLocationRelativeTo(owner);
        setTitle(title);
        //Create output field
        createOutputField();
        //Create the components of the menu
        createMenuComponents();
        //Assemble input panel
        assembleInputField();
        //Assemble the final product
        assembleGUI();
        linkedList = list;
        if (linkedList != null) showList();
        setVisible(true);
    }

    private void createMenuComponents() {
        panelMenu = new JPanel();
        panelMenu.setPreferredSize(new Dimension(350, 700));
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setMaximumSize(new Dimension(300, 700));
        panelMenu.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonNewList = newButton(" New List ");
        buttonPrintList = newButton("Print List");
        buttonClearList = newButton("Clear List");
        buttonGroup = new ButtonGroup();
        buttonAddValue = newRadioButton("Add Value");
        buttonAddValueIndex = newRadioButton("Add Value at Index");
        buttonAddAll = newButton("Add all from Collection");
        buttonAddAllIndex = newRadioButton("Add all from Collection at index");
        buttonContains = newRadioButton("Contains");
        buttonContainsAll = newButton("Contains all in Collection");
        buttonGetAtIndex = newRadioButton("Get Value at Index");
        buttonHashCode = newButton("Print Hash Code");
        buttonIndexOf = newRadioButton("Index Of");
        buttonLastIndexOf = newRadioButton("Last Index Of");
        buttonIsEmpty = newButton("Is List Empty");
        buttonRemoveIndex = newRadioButton("Remove at Index");
        buttonRemoveObject = newRadioButton("Remove Value");
        buttonRemoveAll = newButton("Remove all from Collection");
        buttonRetainAll = newButton("Retain all in Collection");
        buttonSetIndexTo = newRadioButton("Set value at index");
        buttonSize = newButton("Print List Size");
        buttonSubList = newRadioButton("Sub list");
        buttonEquals = newButton("Equals");
        buttonDone = newButton("Done");
    }

    private void assembleInputField() {
        panelInput = new JPanel();
        panelInput.setPreferredSize(new Dimension(300, 350));
        panelInput.setLayout(new BoxLayout(panelInput, BoxLayout.Y_AXIS));
        panelInput.setMaximumSize(new Dimension(300, 500));
        panelInput.setBorder(new EmptyBorder(10, 10, 10, 10));
        formString = new TextForm("Value: ", 6);
        formInt1 = new TextForm("Index: ", 4);
        formInt2 = new TextForm("To Index: ", 4);
        buttonRun = newButton("Run");
        buttonBack = newButton("<- Back");
        panelInput.add(buttonRun);
        panelInput.add(formString);
        panelInput.add(formInt1);
        panelInput.add(formInt2);
        formString.setVisible(false);
        formInt1.setVisible(false);
        formInt2.setVisible(false);
        panelInput.add(buttonBack);
        panelInput.setVisible(false);
    }

    private void createOutputField() {
        textAreaOutput = new JTextArea();
        textAreaOutput.setEditable(false);
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

    private void assembleGUI() {
        setSize(new Dimension(700, 700));
        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        add(panelMenu);
        add(panelInput);
        add(panelOutput);
    }

    //Mouseover logic
    @Override
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        ButtonModel model = (ButtonModel) source;
        if (model.isRollover()) {
            if (source.equals(buttonNewList.getModel())) {
                describe("Creates a new list\n" +
                        "From an outside perspective, this is the same as clearing the list");
            } else if (source.equals(buttonPrintList.getModel())) {
                describe("Prints the linked list\n" +
                        "However, the LinkedList is also printed every time it is modified,\n" +
                        "making this button useless");
            } else if (source.equals(buttonClearList.getModel())) {
                describe("Clears all nodes from the list\n" +
                        "From an outside perspective, this is the same as creating a new list");
            } else if (source.equals(buttonAddValue.getModel())) {
                describe("Appends the specified element to the end of this list");
            } else if (source.equals(buttonAddValueIndex.getModel())) {
                describe("Inserts the specified element at the specified position in this list\n" +
                        "Shifts the element currently at that position\n" +
                        "and any subsequent elements to the right");
            } else if (source.equals(buttonAddAll.getModel())) {
                describe("Opens a new LinkedList builder in a new window\n" +
                        "When done, all elements in the new list will be appended to the end of this list");
            } else if (source.equals(buttonAddAllIndex.getModel())) {
                describe("Opens a new LinkedList builder in a new window\n" +
                        "When done, all elements in the new list will be inserted into this list\n" +
                        "at the specified index. Shifts the element currently at that position\n" +
                        "and any subsequent elements to the right");
            } else if (source.equals(buttonContains.getModel())) {
                describe("Checks if the list contains the specified value");
            } else if (source.equals(buttonContainsAll.getModel())) {
                describe("Opens a new LinkedList builder in a new window\n" +
                        "When done, will check if this list contains all elements in the new list");
            } else if (source.equals(buttonGetAtIndex.getModel())) {
                describe("Shows the element at the specified position in this list");
            } else if (source.equals(buttonHashCode.getModel())) {
                describe("Shows the hash code value for this list");
            } else if (source.equals(buttonIndexOf.getModel())) {
                describe("Finds the index of the first occurrence of the specified element\n" +
                        "in this list.");
            } else if (source.equals(buttonLastIndexOf.getModel())) {
                describe("Finds the index of the last occurrence of the specified element\n" +
                        "in this list");
            } else if (source.equals(buttonIsEmpty.getModel())) {
                describe("Checks if the list contains no elements");
            } else if (source.equals(buttonRemoveIndex.getModel())) {
                describe("Removes the element at the specified position in this list\n" +
                        "Shows the element that was removed from the list");
            } else if (source.equals(buttonRemoveObject.getModel())) {
                describe("Removes the first occurrence of the specified element from this list\n");
            } else if (source.equals(buttonRemoveAll.getModel())) {
                describe("Opens a new LinkedList builder in a new windows\n" +
                        "When done, removes from this list all elements which\n" +
                        "are contained in the new list\n");
            } else if (source.equals(buttonRetainAll.getModel())) {
                describe("Opens a new LinkedList builder in a new windows\n" +
                        "When done, removes from this list all elements\n" +
                        "which are not contained in the new list");
            } else if (source.equals(buttonSetIndexTo.getModel())) {
                describe("Replaces the element at the specified position\n" +
                        "in this list with the specified element");
            } else if (source.equals(buttonSize.getModel())) {
                describe("Shows the size of the list");
            } else if (source.equals(buttonSubList.getModel())) {
                describe("Opens a new LinkedList builder in a new window\n" +
                        "containing a sub-list of all elements from index (inclusive) to index (exclusive)");
            } else if (source.equals(buttonEquals.getModel())) {
                describe("Opens a new LinkedList builder in a new window\n" +
                        "When done, checks if the new list is the same as this list");
            } else if (source.equals(buttonDone.getModel())) {
                describe("Done editing the current list");
            } else if (source.equals(buttonRun.getModel())) {
                describe("Hitting enter while in the text field is the same as clicking Run");
            }
        } else {
            describe("Mouse over a button for a description of what it does");
        }
    }

    private void run() {
        String value = formString.getText();
        int int1 = -1;
        int int2 = -1;
        try {
            int1 = Integer.parseInt(formInt1.getText());
            int2 = Integer.parseInt(formInt2.getText());
        } catch (NumberFormatException e) {
            System.out.println("This should happen almost every time someone clicks Run");
            System.out.println("Its easier to have this than duplicate form.getText into every else statement");
            e.printStackTrace();
        }
        try {
            if (buttonRemoveObject.isSelected()) {
                if (linkedList.remove(value)) {
                    log("\"" + value + "\" was removed from the list");
                    showList();
                } else
                    log("\"" + value + "\" is not in list");
            } else if (buttonAddValue.isSelected()) {
                linkedList.add(value);
                log("\"" + value + "\" added to end of list");
                showList();
            } else if (buttonAddValueIndex.isSelected()) {
                linkedList.add(int1, value);
                log("\"" + value + "\" added to list at index " + int1);
                showList();
            } else if (buttonAddAllIndex.isSelected()) {
                log("A new window was created\n" +
                        "Use the window to create a new LinkedList\n" +
                        "The contents of the new LinkedList will be added to the current LinkedList\n" +
                        "at the specified index when you close the window");
                ListBuilderGUI listBuilder = new ListBuilderGUI(LinkedListTester.logger, "LinkedList Builder for addAll from Collection at Index");
                try {
                    linkedList.addAll(int1, listBuilder.getList());
                    log("Collection added to list successfully");
                    showList();
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                    log("You did not add any elements to the collection\n" +
                            "LinkedList was not modified");
                }
            } else if (buttonContains.isSelected()) {
                if (linkedList.contains(value))
                    log("List contains " + "\"" + value + "\"");
                else
                    log("List does not contain " + "\"" + value + "\"");
            } else if (buttonGetAtIndex.isSelected()) {
                log("Value at index of " + int1 + " is: \"" + linkedList.get(int1) + "\"");
            } else if (buttonIndexOf.isSelected()) {
                int indexOf = linkedList.indexOf(value);
                if (indexOf == -1) log("\"" + value + "\" is not in list");
                else log("Index of \"" + value + "\" is: " + indexOf);
            } else if (buttonLastIndexOf.isSelected()) {
                int indexOf = linkedList.lastIndexOf(value);
                if (indexOf == -1) log("\"" + value + "\" is not in list");
                else log("Last index of \"" + value + "\" is: " + indexOf);
            } else if (buttonRemoveIndex.isSelected()) {
                String s = linkedList.remove(int1);
                log("Element \"" + s + "\" was removed");
                showList();
            } else if (buttonRemoveObject.isSelected()) {
                if (linkedList.remove(value)) {
                    log("\"" + value + "\" removed from list");
                    showList();
                } else log("\"" + value + "\" was not removed from list");
            } else if (buttonSetIndexTo.isSelected()) {
                String oldValue = linkedList.set(int1, value);
                log(oldValue + " was replaced with " + value);
                showList();
            } else if (buttonSubList.isSelected()) {
                LinkedList<String> newList = (LinkedList<String>) linkedList.subList(int1, int2);
                new ListBuilderGUI(LinkedListTester.logger, "SubList of LinkedList Editor", newList);
            }
        } catch (NumberFormatException e) {
            log("Invalid Input");
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            log("Index out of bounds");
            e.printStackTrace();
        }
        formString.clearText();
        formInt1.clearText();
        formInt2.clearText();
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(buttonBack)) {
            formString.setVisible(false);
            formInt1.setVisible(false);
            formInt2.setVisible(false);
            panelInput.setVisible(false);
            panelMenu.setVisible(true);
            return;
        } else if (source.equals(buttonDone)) {
            dispose();
            return;
        } else if (source.equals(buttonRun)) {
            run();
            return;
        } else if (source.equals(buttonNewList)) {
            linkedList = new LinkedList<>();
            log("New LinkedList created");
            showList();
            return;
        }
        //The rest cannot be performed if there is no linked list
        if (linkedList == null) log("Must create list first");
        else if (source.equals(buttonPrintList)) {
            showList();
        } else if (source.equals(buttonRemoveObject)) {
            formString.setVisible(true);
            switchToInput();
        } else if (source.equals(buttonAddValueIndex)) {
            formString.setVisible(true);
            formInt1.setVisible(true);
            switchToInput();
        } else if (source.equals(buttonAddValue)) {
            formString.setVisible(true);
            switchToInput();
        } else if (source.equals(buttonClearList)) {
            linkedList.clear();
            log("LinkedList cleared");
            showList();
        } else if (source.equals(buttonAddAll)) {
            log("A new window was created\n" +
                    "Use the window to create a new LinkedList\n" +
                    "The contents of the new LinkedList will be added to the current\n" +
                    "LinkedList when you click \"Done\"");
            ListBuilderGUI listBuilder = new ListBuilderGUI(LinkedListTester.logger, "LinkedList Builder for addAll from Collection");
            try {
                linkedList.addAll(listBuilder.getList());
                log("Added successfully");
            } catch (NullPointerException ex) {
                ex.printStackTrace();
                log("You did not add any elements to the collection\n" +
                        "LinkedList was not modified");
            }
            showList();
        } else if (source.equals(buttonAddAllIndex)) {
            formInt1.setVisible(true);
            switchToInput();
        } else if (source.equals(buttonContains)) {
            formString.setVisible(true);
            switchToInput();
        } else if (source.equals(buttonContainsAll)) {
            log("A new window was created\n" +
                    "Use the window to create a new LinkedList\n" +
                    "The current LinkedList will then be check to see if it\n" + "" +
                    "contains all the values in the new LinkedList when you click \"Done\"");
            ListBuilderGUI listBuilder = new ListBuilderGUI(LinkedListTester.logger, "LinkedList Builder for containsAll from Collection");
            if (linkedList.containsAll(listBuilder.getList()))
                log("List contains all values entered");
            else
                log("List does not contain all values entered");
        } else if (source.equals(buttonGetAtIndex)) {
            formInt1.setVisible(true);
            switchToInput();
        } else if (source.equals(buttonHashCode)) {
            log(String.valueOf(linkedList.hashCode()));
        } else if (source.equals(buttonIndexOf)) {
            formString.setVisible(true);
            switchToInput();
        } else if (source.equals(buttonLastIndexOf)) {
            formString.setVisible(true);
            switchToInput();
        } else if (source.equals(buttonIsEmpty)) {
            if (linkedList.isEmpty())
                log("LinkedList is empty");
            else
                log("LinkedList is not empty");
        } else if (source.equals(buttonRemoveIndex)) {
            formInt1.setVisible(true);
            switchToInput();
        } else if (source.equals(buttonRemoveObject)) {
            formString.setVisible(true);
            switchToInput();
        } else if (source.equals(buttonRemoveAll)) {
            log("A new window was created\n" +
                    "Use the window to create a new LinkedList\n" +
                    "Everything in the new LinkedList will be removed\n" +
                    "from the current LinkedList when you click Done");
            ListBuilderGUI listBuilder = new ListBuilderGUI(LinkedListTester.logger, "LinkedList Builder for removeAll from Collection");
            if (linkedList.removeAll(listBuilder.getList())) {
                log("List changed");
                showList();
            } else
                log("List not changed");
        } else if (source.equals(buttonRetainAll)) {
            log("A new window was created\n" +
                    "Use the window to create a new LinkedList\n" +
                    "Everything not in the new LinkedList will be removed\n" +
                    "from the current LinkedList when you click Done");
            ListBuilderGUI listBuilder = new ListBuilderGUI(LinkedListTester.logger, "LinkedList Builder for retainAll from Collection");
            if (linkedList.retainAll(listBuilder.getList())) {
                log("List changed");
                showList();
            } else
                log("List not changed");
        } else if (source.equals(buttonSetIndexTo)) {
            formInt1.setVisible(true);
            formString.setVisible(true);
            switchToInput();
        } else if (source.equals(buttonSize)) {
            log("The size of the list is " + String.valueOf(linkedList.size()));
        } else if (source.equals(buttonSubList)) {
            formInt1.setVisible(true);
            formInt2.setVisible(true);
            switchToInput();
        } else if (source.equals(buttonEquals)) {
            log("A new window was created\n" +
                    "Use the window to create a new LinkedList\n" +
                    "The logger will show if the new list is the same as\n" +
                    "the current list when you click Done");
            ListBuilderGUI listBuilder = new ListBuilderGUI(LinkedListTester.logger, "LinkedList Builder for Equals");
            if (linkedList.equals(listBuilder.getList())) log("Lists are the same");
            else log("Lists are not the same");
        }
    }

    private void switchToInput() {
        panelMenu.setVisible(false);
        panelInput.setVisible(true);
    }

    private void showList() {
        textAreaOutput.setText(linkedList.toString());
    }

    private JButton newButton(String s) {
        JButton newButton = new JButton(s);
        newButton.setPreferredSize(new Dimension(125, 25));
        newButton.addActionListener(this);
        newButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMenu.add(newButton);
        newButton.getModel().addChangeListener(this);
        return newButton;
    }

    private JRadioButton newRadioButton(String s) {
        JRadioButton newButton = new JRadioButton(s);
        newButton.addActionListener(this);
        newButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonGroup.add(newButton);
        panelMenu.add(newButton);
        newButton.getModel().addChangeListener(this);
        return newButton;
    }

    /**
     * Custom text form comprised of a JPanel containing a JLabel and a JPanel
     * Also implements ActionListener so that pressing enter is the same as hitting run
     */
    class TextForm extends JPanel implements ActionListener {
        public final JPanel fieldPanel;
        public final JTextField field;
        public final JLabel lable;

        // Create a form with the specified labels, tooltips, and sizes.
        public TextForm(String label, int width) {
            //Create a JPanel with a 1x2 GridLayout
            fieldPanel = new JPanel(new GridLayout(1, 2));
            add(fieldPanel);
            //Create a text field with specified parameters
            field = new JTextField(JTextField.RIGHT);
            field.setColumns(width);
            field.setAlignmentX(Component.RIGHT_ALIGNMENT);
            field.addActionListener(this);
            //Create a label for the field
            lable = new JLabel(label, JLabel.LEFT);
            lable.setLabelFor(field);
            lable.setAlignmentX(Component.LEFT_ALIGNMENT);
            //Add the label and field to the panel
            fieldPanel.add(lable);
            fieldPanel.add(field);
        }

        public void actionPerformed(ActionEvent e) {
            buttonRun.doClick();
        }

        public String getText() {
            return field.getText();
        }

        public void clearText() {
            field.setText("");
        }
    }

    public LinkedList<String> getList() {
        return linkedList;
    }

    private static void log(String s) {
        LinkedListTester.output.setText(s);
    }

    private static void describe(String s) {
        LinkedListTester.description.setText(s);
    }
}