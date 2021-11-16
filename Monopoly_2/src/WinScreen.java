/**
 * Class WinScreen creates the congratulatory screen for the winner when the game is over
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class WinScreen extends JFrame implements ActionListener
{
    private JPanel QuitButtonPanel = new JPanel();
    private ImagePanel background;
    private ImagePanel winnerIconPanel;
    private Image backgroundImage;
    private Image winnerIcon;
    private JButton quitButton = new JButton("Done");

    private int WINNER_NUM;
    private int DEFAULT_WIDTH = 700;
    private int DEFAULT_HEIGHT = 700;

    /**
     * constructor for class WinScreen
     * @param numberOfWinner is the number of the player that won the game
     */
    public WinScreen(int numberOfWinner)
    {
        WINNER_NUM = numberOfWinner;
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setTitle("Game Over");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add background
        try{
            backgroundImage = ImageIO.read(new File("res/Ending_Background.png"));
            setWinnerIcon();
        } catch(IOException e)
        {
            e.printStackTrace();
        }
        winnerIconPanel = new ImagePanel(winnerIcon);
        winnerIconPanel.setSize(210, 210);
        winnerIconPanel.setLocation(250, 310);

        background = new ImagePanel(backgroundImage);
        background.add(winnerIconPanel);
        addButton();

        add(background);
        setVisible(true);
    }

    /**
     * actionPerformed overrides actionPerformed of ActionListener
     * @param e is the event in question
     */
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();

        if(source == quitButton) // winner is finished gloating
        {
            System.exit(0);
        }
    }

    /**
     * setWinnerIcon sets the icon that will be displayed on the winner screen
     * @throws IOException if the file can't be read
     */
    private void setWinnerIcon() throws IOException
    {
        if(WINNER_NUM == 0)
            winnerIcon = ImageIO.read(new File("res/playerIcon1Enlarged.png"));
        else if(WINNER_NUM == 1)
            winnerIcon = ImageIO.read(new File("res/playerIcon2Enlarged.png"));
        else if(WINNER_NUM == 2)
            winnerIcon = ImageIO.read(new File("res/playerIcon3Enlarged.png"));
        else
            winnerIcon = ImageIO.read(new File("res/playerIcon4Enlarged.png"));
    }

    /**
     * addButton adds the button to the game
     */
    private void addButton()
    {
        QuitButtonPanel.setSize(75, 50);
        quitButton.setLocation(265,  500);
        quitButton.setPreferredSize(new Dimension(70, 50));
        QuitButtonPanel.setBackground(Color.RED);
        QuitButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        QuitButtonPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        QuitButtonPanel.setLayout(new BoxLayout(QuitButtonPanel, BoxLayout.PAGE_AXIS));
        QuitButtonPanel.add(quitButton);
        QuitButtonPanel.add(Box.createVerticalGlue());
        QuitButtonPanel.setLocation(310, 522);
        quitButton.addActionListener(this);

        background.add(QuitButtonPanel);
    }
}
