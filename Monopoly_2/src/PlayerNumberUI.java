import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Class PlayerNumberUI gets the number of players from the user(s)
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class PlayerNumberUI extends JFrame implements ActionListener
{
    private JPanel TwoPlayerPanel = new JPanel();
    private JPanel ThreePlayerPanel = new JPanel();
    private JPanel FourPlayerPanel = new JPanel();
    private JPanel BackButtonPanel = new JPanel();
    private ImagePanel background;
    private Image backgroundImage;
    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 485;
    private JButton TwoPlayerButton = new JButton("2 Players");
    private JButton ThreePlayerButton = new JButton("3 Players");
    private JButton FourPlayerButton = new JButton("4 Players");
    private JButton backButton = new JButton("Back");

    private Start_Menu MAIN_MENU;

    /**
     * constructor for class PlayerNumberUI
     */
    public PlayerNumberUI(Start_Menu theMenu)
    {
        MAIN_MENU = theMenu;

        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setTitle("Player Number Select");
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
     * overrides actionPerformed of ActionListener
     * @param e
     */
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        if(source == backButton)
        {
            MAIN_MENU.setVisible(true);
        }
        else if(source == TwoPlayerButton)
        {
            TwoPlayerPanel.setVisible(false);
            GameBoardUI gameBoard = new GameBoardUI(2);
        }
        else if(source == ThreePlayerButton)
        {
            ThreePlayerButton.setVisible(false);
            GameBoardUI gameBoard = new GameBoardUI(3);
        }
        else if(source == FourPlayerButton)
        {
            FourPlayerButton.setVisible(false);
            GameBoardUI gameBoard = new GameBoardUI(4);
        }
        this.setVisible(false);
    }

    /**
     * addButtons adds the buttons to the menu
     */
    private void addButtons()
    {
        // create the two player button
        TwoPlayerPanel.setSize(70, 50);
        TwoPlayerButton.setLocation(100, 100);
        TwoPlayerButton.setPreferredSize(new Dimension(70, 50));
        TwoPlayerPanel.setBackground(Color.BLUE);
        TwoPlayerPanel.setAlignmentX(Component.BOTTOM_ALIGNMENT);
        TwoPlayerPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        TwoPlayerPanel.setLayout(new BoxLayout(TwoPlayerPanel, BoxLayout.PAGE_AXIS));
        TwoPlayerPanel.add(TwoPlayerButton);
        TwoPlayerPanel.add(Box.createVerticalGlue());
        TwoPlayerPanel.setLocation(50, 120);
        TwoPlayerButton.addActionListener(this);

        // create the three player button
        ThreePlayerPanel.setSize(70, 50);
        ThreePlayerButton.setLocation(100, 100);
        ThreePlayerButton.setPreferredSize(new Dimension(70, 50));
        ThreePlayerPanel.setBackground(Color.RED);
        ThreePlayerPanel.setAlignmentX(Component.BOTTOM_ALIGNMENT);
        ThreePlayerPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        ThreePlayerPanel.setLayout(new BoxLayout(ThreePlayerPanel, BoxLayout.PAGE_AXIS));
        ThreePlayerPanel.add(ThreePlayerButton);
        ThreePlayerPanel.add(Box.createVerticalGlue());
        ThreePlayerPanel.setLocation(50, 200);
        ThreePlayerButton.addActionListener(this);

        // create the instructions button
        FourPlayerPanel.setSize(70, 50);
        FourPlayerButton.setLocation(100, 100);
        FourPlayerButton.setPreferredSize(new Dimension(70, 50));
        FourPlayerPanel.setBackground(Color.GREEN);
        FourPlayerPanel.setAlignmentX(Component.BOTTOM_ALIGNMENT);
        FourPlayerPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        FourPlayerPanel.setLayout(new BoxLayout(FourPlayerPanel, BoxLayout.PAGE_AXIS));
        FourPlayerPanel.add(FourPlayerButton);
        FourPlayerPanel.add(Box.createVerticalGlue());
        FourPlayerPanel.setLocation(50, 280);
        FourPlayerButton.addActionListener(this);

        BackButtonPanel.setSize(70, 50);
        backButton.setLocation(100, 100);
        backButton.setPreferredSize(new Dimension(70, 50));
        BackButtonPanel.setBackground(Color.ORANGE);
        BackButtonPanel.setAlignmentX(Component.BOTTOM_ALIGNMENT);
        BackButtonPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        BackButtonPanel.setLayout(new BoxLayout(BackButtonPanel, BoxLayout.PAGE_AXIS));
        BackButtonPanel.add(backButton);
        BackButtonPanel.add(Box.createVerticalGlue());
        BackButtonPanel.setLocation(485, 280);
        backButton.addActionListener(this);

        // add all three buttons
        background.add(TwoPlayerPanel);
        background.add(ThreePlayerPanel);
        background.add(FourPlayerPanel);
        background.add(BackButtonPanel);
    }
}
