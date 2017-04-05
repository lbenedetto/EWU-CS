import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class GUI implements ActionListener, ItemListener {
    public JButton buttonNewMaze;
    public JButton buttonSolveMaze;
    public JCheckBox checkBoxAutoSolve;
    public JPanel panelMenu;
    public JPanel panelMaze;
    public JFrame frameMain;
    public TextForm formX;
    public TextForm formY;
    public int[][] maze;
    public boolean autoSolve;

    public GUI(int[][] newMaze) {
        maze = newMaze;
        //Create a panel and paint the maze onto it
        paintMaze(maze);
        //Create some buttons for the menu
        createMenuButtons();
        //Create some forms for the menu
        formX = new TextForm("Cols: ", 4, "Width");
        formY = new TextForm("Rows: ", 4, "Height");
        //Put the buttons and forms in a menu
        assembleMenu();
        //Assemble the final product
        assembleGUI();
    }

    public void createMenuButtons() {
        buttonNewMaze = new JButton(" New Maze ");
        buttonNewMaze.setPreferredSize(new Dimension(125, 25));
        buttonNewMaze.addActionListener(this);
        buttonNewMaze.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonSolveMaze = new JButton("Solve Maze");
        buttonSolveMaze.setPreferredSize(new Dimension(125, 25));
        buttonSolveMaze.addActionListener(this);
        buttonSolveMaze.setAlignmentX(Component.CENTER_ALIGNMENT);

        checkBoxAutoSolve = new JCheckBox("Auto-Solve");
        checkBoxAutoSolve.setPreferredSize(new Dimension(125, 25));
        checkBoxAutoSolve.addItemListener(this);
        checkBoxAutoSolve.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void assembleMenu() {
        panelMenu = new JPanel();
        panelMenu.setPreferredSize(new Dimension(125, 150));
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setMaximumSize(new Dimension(125, 150));
        panelMenu.add(formX);
        panelMenu.add(formY);
        panelMenu.add(buttonNewMaze);
        panelMenu.add(buttonSolveMaze);
        panelMenu.add(checkBoxAutoSolve);
    }

    public void assembleGUI() {
        try {
            frameMain = new JFrame("Maze " + maze.length + "x" + maze[0].length);
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            frameMain = new JFrame("Maze 0x0");
        }
        frameMain.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameMain.setLayout(new BoxLayout(frameMain.getContentPane(), BoxLayout.X_AXIS));
        frameMain.add(panelMaze);
        frameMain.add(panelMenu);
        frameMain.pack();
        frameMain.setVisible(true);
    }

    public void paintMaze(int[][] maze) {
        panelMaze = new JPanel();
        //Create a maze (GridLayout)
        try {
            panelMaze.setLayout(new GridLayout(maze.length, maze[0].length));
            //Fill the maze with colored buttons
            for (int[] row : maze) {
                for (int col : row) {
                    JButton button = new JButton();
                    switch (col) {
                        case 0:
                            button.setBackground(Color.BLACK);
                            break;
                        case 1:
                            button.setBackground(Color.YELLOW);
                            break;
                        case 3:
                            button.setBackground(Color.RED);
                            break;
                        case 7:
                            button.setBackground(Color.GREEN);
                            break;
                    }
                    button.setPreferredSize(new Dimension(20, 20));
                    panelMaze.add(button);
                }
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            panelMaze.setLayout(new GridLayout(1, 0));
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(buttonNewMaze)) {
            System.out.println("Generating new maze");
            int x;
            int y;
            try {
                x = Integer.parseInt(formX.getText());
                if (x < 1) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                x = 13;
            }
            try {
                y = Integer.parseInt(formY.getText());
                if (y < 1) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                y = 8;
            }
            maze = MazeTester.generateSolvableMaze(y, x);
            if (autoSolve) maze = MazeTester.solveMaze(maze);
            refreshGUI();
            frameMain.setTitle("Maze " + x + "x" + y);
            System.out.println("New maze generated");
        } else if (source.equals(buttonSolveMaze)) {
            System.out.println("Attempting to solve maze");
            maze = MazeTester.solveMaze(maze);
            refreshGUI();
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.DESELECTED) {
            System.out.println("Mazes will no longer be automatically solved");
            autoSolve = false;
        }
        if (e.getStateChange() == ItemEvent.SELECTED) {
            System.out.println("Mazes will now be automatically solved upon generation");
            autoSolve = true;
        }
    }

    public void refreshGUI() {
        paintMaze(maze);
        frameMain.getContentPane().removeAll();
        frameMain.getContentPane().add(panelMaze);
        frameMain.getContentPane().add(panelMenu);
        frameMain.revalidate();
        frameMain.pack();
    }
}
