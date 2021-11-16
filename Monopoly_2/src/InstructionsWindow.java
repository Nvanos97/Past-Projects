import javax.swing.*;

/**
 * Class InstructionsWindow
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class InstructionsWindow extends JFrame implements ActionListener
{
    private JPanel INSTRUCTIONS_PANEL = new JPanel();
    private JPanel BackButtonPanel = new JPanel();
    private JTextArea INSTRUCTIONS_BOX;
    private JButton backButton = new JButton("Back");
    private static final int DEFAULT_WIDTH = 900;
    private static final int DEFAULT_HEIGHT = 820;
    private String I_TEXT;
    private final static String newLine = "\n";

    private Start_Menu MAIN_MENU;

    /**
     * constructor for class InstructionsWindow
     */
    public InstructionsWindow(Start_Menu theMenu)
    {
        MAIN_MENU = theMenu;

        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        add(INSTRUCTIONS_PANEL);
        setTitle("Zagopoly Instructions");
        setLocationRelativeTo(null);
        INSTRUCTIONS_PANEL.setBackground(Color.RED);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        INSTRUCTIONS_BOX = new JTextArea(45, 65);
        INSTRUCTIONS_BOX.setEditable(false);
        INSTRUCTIONS_BOX.setBackground(Color.green);
        JScrollPane scrollPane = new JScrollPane(INSTRUCTIONS_BOX);

        GridBagConstraints Constraints = new GridBagConstraints();
        Constraints.gridwidth = GridBagConstraints.REMAINDER;
        Constraints.fill = GridBagConstraints.HORIZONTAL;
        Constraints.fill = GridBagConstraints.BOTH;
        Constraints.weightx = 1.0;
        Constraints.weighty = 1.0;
        INSTRUCTIONS_PANEL.add(scrollPane, Constraints);

        try{
            getInstructions();
        } catch(IOException e){
            e.printStackTrace();
        }

        addButton();
        
        setVisible(true);
    }

    /**
     * overrides actionPerformed of ActionListener
     * @param e is the event in question
     */
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if(source == backButton)
        {
            setVisible(false);
            MAIN_MENU.setVisible(true);
        }
    }

    /**
     * adds the back button to the GUI
     */
    private void addButton()
    {
        BackButtonPanel.setSize(70, 50);
        backButton.setLocation(50, 50);
        backButton.setPreferredSize(new Dimension(70, 50));
        BackButtonPanel.setBackground(Color.BLUE);
        BackButtonPanel.setAlignmentX(Component.BOTTOM_ALIGNMENT);
        BackButtonPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        BackButtonPanel.setLayout(new BoxLayout(BackButtonPanel, BoxLayout.PAGE_AXIS));
        BackButtonPanel.add(backButton);
        BackButtonPanel.add(Box.createVerticalGlue());
        BackButtonPanel.setLocation(50, 150);
        backButton.addActionListener(this);

        INSTRUCTIONS_PANEL.add(BackButtonPanel);
    }

    /**
     * getInstructions retrieves the game instructions from a text file
     */
    private void getInstructions() throws IOException
    {
        File instructionsFile = new File("res/Instructions.txt");
        Scanner instructionsInput = new Scanner(instructionsFile);

        // get entire text from file
        while(instructionsInput.hasNextLine())
        {
            I_TEXT = instructionsInput.nextLine();
            INSTRUCTIONS_BOX.append(I_TEXT + newLine);
            INSTRUCTIONS_BOX.setCaretPosition(INSTRUCTIONS_BOX.getDocument().getLength());
        }
    }
}
