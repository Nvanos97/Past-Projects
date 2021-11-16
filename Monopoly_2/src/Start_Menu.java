/**
 * Start_Menu creates the startup menu of the monopoly game
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Start_Menu extends JFrame implements ActionListener
{
    private JPanel StartButtonPanel = new JPanel();
    private JPanel ExitButtonPanel = new JPanel();
    private JPanel InstructionsPanel = new JPanel();
    private ImagePanel background;
    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 485;
    private JButton startButton = new JButton("Start");
    private JButton instructionsButton = new JButton("Instructions");
    private JButton exitButton = new JButton("Exit");
    private BufferedImage backgroundImage;

    private InstructionsWindow GAME_INSTRUCTIONS_WINDOW;
    //private GameBoardUI GUI;

    /**
     * constructor for class MainMenu
     */
    public Start_Menu(String fileName)
    {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setTitle("Zagopoly Menu");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // get background image
        try{
            backgroundImage = ImageIO.read(new File("res/Start_Menu_Background.png"));
        } catch(IOException e){
            e.printStackTrace();
        }

        background = new ImagePanel(backgroundImage);
        add(background);
        addButtons();

        this.setVisible(true);
    }

    /**
     * initGUI initializes the GUI of the game board
     */
    /*public void initUI()
    {
        add(GUI);
        setTitle("Zagopoly");
        setSize(697, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }*/

    /**
     * actionPerformed is a function inherited from the interface ActionListener that handles an event
     * @param e is the event in question
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if (source == exitButton) // case user clicks on exit
        {
            System.exit(0);
        }
        else if(source == startButton) // case user clicks on start
        {
            PlayerNumberUI PlayerGetter = new PlayerNumberUI(this);
            this.setVisible(false);
        }
        else if (source == instructionsButton) // case user clicks on instructions
        {
            openInstructions();
        }
    }

    /**
     * openInstructions opens the Instructions text file
     * @throws IOException
     */
    private void openInstructions()
    {
        setVisible(false);
        GAME_INSTRUCTIONS_WINDOW = new InstructionsWindow(this);
    }

    /**
     * addButtons adds the buttons to the main menu
     */
    private void addButtons()
    {
        // create the start button
        StartButtonPanel.setSize(70, 50);
        startButton.setLocation(100, 100);
        startButton.setPreferredSize(new Dimension(70, 50));
        StartButtonPanel.setBackground(Color.BLUE);
        StartButtonPanel.setAlignmentX(Component.BOTTOM_ALIGNMENT);
        StartButtonPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        StartButtonPanel.setLayout(new BoxLayout(StartButtonPanel, BoxLayout.PAGE_AXIS));
        StartButtonPanel.add(startButton);
        StartButtonPanel.add(Box.createVerticalGlue());
        StartButtonPanel.setLocation(265, 90);
        startButton.addActionListener(this);

        // create the exit button
        ExitButtonPanel.setSize(70, 50);
        exitButton.setLocation(100, 100);
        exitButton.setPreferredSize(new Dimension(70, 50));
        ExitButtonPanel.setBackground(Color.RED);
        ExitButtonPanel.setAlignmentX(Component.BOTTOM_ALIGNMENT);
        ExitButtonPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        ExitButtonPanel.setLayout(new BoxLayout(ExitButtonPanel, BoxLayout.PAGE_AXIS));
        ExitButtonPanel.add(exitButton);
        ExitButtonPanel.add(Box.createVerticalGlue());
        ExitButtonPanel.setLocation(265, 250);
        exitButton.addActionListener(this);

        // create the instructions button
        InstructionsPanel.setSize(90, 50);
        instructionsButton.setLocation(100, 100);
        instructionsButton.setPreferredSize(new Dimension(90, 50));
        InstructionsPanel.setBackground(Color.GREEN);
        InstructionsPanel.setAlignmentX(Component.BOTTOM_ALIGNMENT);
        InstructionsPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        InstructionsPanel.setLayout(new BoxLayout(InstructionsPanel, BoxLayout.PAGE_AXIS));
        InstructionsPanel.add(instructionsButton);
        InstructionsPanel.add(Box.createVerticalGlue());
        InstructionsPanel.setLocation(265, 170);
        instructionsButton.addActionListener(this);

        // add all three buttons
        background.add(StartButtonPanel);
        background.add(ExitButtonPanel);
        background.add(InstructionsPanel);
    }
}
