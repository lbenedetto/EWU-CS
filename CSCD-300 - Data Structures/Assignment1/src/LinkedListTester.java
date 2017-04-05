import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author lars
 */
class LinkedListTester {
    public static JTextArea output;
    public static JTextArea description;
    public static JFrame logger;

    public static void main(String[] args) {
        //Frame
        logger = new JFrame();
        logger.setTitle("LinkedListTester Logger");
        logger.setSize(new Dimension(500, 500));
        logger.getContentPane().setLayout(new BoxLayout(logger.getContentPane(), BoxLayout.PAGE_AXIS));
        //Button description stuff
        description = new JTextArea();
        description.setEditable(false);
        description.setText("Mouse over a button for a description of what it does");
        description.setBorder(new EmptyBorder(10, 10, 10, 10));
        description.setSize(new Dimension(480, 200));
        description.setMaximumSize(new Dimension(480, 200));
        JPanel panelDescription = new JPanel();
        panelDescription.setLayout(new GridLayout(1, 1));
        panelDescription.add(description);
        panelDescription.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelDescription.setPreferredSize(new Dimension(480, 200));
        //Output stuff
        output = new JTextArea();
        output.setEditable(false);
        output.setText("Program output will appear here");
        output.setBorder(new EmptyBorder(10, 10, 10, 10));
        output.setSize(new Dimension(480, 200));
        output.setMaximumSize(new Dimension(480, 200));
        JPanel panelOutput = new JPanel();
        panelOutput.setLayout(new GridLayout(1, 1));
        panelOutput.add(output);
        panelOutput.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelOutput.setPreferredSize(new Dimension(480, 200));
        //Assembly
        logger.add(panelDescription);
        logger.add(panelOutput);
        logger.setVisible(true);
        //Create tester linked list
        LinkedList<String> debugList = new LinkedList<>();
        for (int i = 0; i <= 10; i++) {
            debugList.add(String.valueOf(i));
        }
        //Create builder dialog
        ListBuilderGUI listBuilder = new ListBuilderGUI(logger, "LinkedList Builder", debugList);
        System.out.println(listBuilder.getList());
        System.exit(0);
    }
}
