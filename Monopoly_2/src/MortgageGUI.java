/**
 * Class MortgageGUI creates the user interface with which a player can mortgage
 * properties of his/her choice
 */

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MortgageGUI extends JFrame implements ActionListener
{
    private ImagePanel background;
    private Image backgroundImage;
    private JPanel[] MortgagePanels;
    private JPanel UnMortgagePanel = new JPanel();
    private JPanel StatsButtonPanel = new JPanel();
    private JPanel DonePanel = new JPanel();
    private JButton[] mortgageButtons;
    private JButton unMortgageButton = new JButton("Un-Mortgage");
    private JButton declineUnMortgage = new JButton("No Thanks");
    private JButton statsButton = new JButton("Player Stats");
    private JButton doneButton = new JButton("Done");

    private Property[] PLAYER_HAND;
    private Player CURRENT_PLAYER;
    private Property CURRENT_PROPERTY;
    private ZagopolyTextWindow MESSAGE_BOX;
    private The_Sets GAME_SETS;
    private Set CURRENT_SET;
    private GameBoardUI GUI;
    private ZagopolyDice DICE;
    private int DEFAULT_WIDTH = 800;
    private int DEFAULT_HEIGHT = 500;

    /**
     * constructor for class MortgageGUI
     * @param currentPlayer is the player who is trying to mortgage their property(ies)
     */
    public MortgageGUI(Player currentPlayer, ZagopolyTextWindow TextWindow, GameBoardUI gui, The_Sets gameSets, ZagopolyDice theDice)
    {
        // get player info so correct properties can be determined
        PLAYER_HAND = currentPlayer.getHand();
        CURRENT_PLAYER = currentPlayer;
        MESSAGE_BOX = TextWindow;
        GUI = gui;
        GAME_SETS = gameSets;
        DICE = theDice;
        MortgagePanels = new JPanel[PLAYER_HAND.length];
        mortgageButtons = new JButton[PLAYER_HAND.length];

        // routine ops
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setTitle("Mortgage Menu");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try{
            backgroundImage = ImageIO.read(new File("res/MortgageBackground.png"));
        } catch(IOException e){
            e.printStackTrace();
        }

        background = new ImagePanel(backgroundImage);
        addButtons();

        setVisible(true);
        add(background);
    }

    /**
     * overrides actionPerformed of ActionListener
     * @param e is the event in question
     */
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();

        if(source == doneButton) // player doesn't want to mortgage anything else
        {
            MESSAGE_BOX.printMessage("You are done mortgaging for the time being.");
            GUI.setVisible(true);
            this.setVisible(false);
        }
        else if(source == statsButton) // display stats
        {
            CURRENT_PLAYER.displayStats(MESSAGE_BOX, GUI);
        }
        else if(source == unMortgageButton) // unmortgage
        {
            MESSAGE_BOX.printMessage("You have decided to pay off the mortgage for this property.");
            CURRENT_PROPERTY.unMortgage(MESSAGE_BOX);
            if(CURRENT_PROPERTY.isPrimary())
            {
                try{
                    CURRENT_SET = GAME_SETS.getSet(CURRENT_PROPERTY.getSetNumber(GAME_SETS));
                    CURRENT_SET.updateRents(DICE);
                } catch(SetNotFoundException se){
                    se.printStackTrace();
                }
            }
            UnMortgagePanel.setVisible(false);

            for(int m = 0; m < MortgagePanels.length; m++)
            {
                MortgagePanels[m].setVisible(true);
            }
        }
        else if(source == declineUnMortgage) // don't unmortgage
        {
            MESSAGE_BOX.printMessage("You have decided not to pay off this mortgage yet.");
            UnMortgagePanel.setVisible(false);

            for(int m = 0; m < MortgagePanels.length; m++)
            {
                MortgagePanels[m].setVisible(true);
            }
        }else{ // player selects one of many mortgaging buttons
            // Note: it's technically possible for the player to have every single property. So, there
            // is a button for every single property. But the likelihood that this situation will arise
            // is so highly improbable that this situation will never arise. This was more of a routine measure
            // than anything to protect the game from breaking.
            boolean unMortgageOption = false;
            if(source == mortgageButtons[0])
            {
                if(PLAYER_HAND[0].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[0];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[0]);
                }
            }
            else if(source ==  mortgageButtons[1])
            {
                if(PLAYER_HAND[1].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[1];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[1]);
                }
            }
            else if(source ==  mortgageButtons[2])
            {
                if(PLAYER_HAND[2].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[2];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[2]);
                }
            }
            else if(source ==  mortgageButtons[3])
            {
                if(PLAYER_HAND[3].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[3];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[3]);
                }
            }
            else if(source ==  mortgageButtons[4])
            {
                if(PLAYER_HAND[4].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[4];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[4]);
                }
            }
            else if(source ==  mortgageButtons[5])
            {
                if(PLAYER_HAND[5].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[5];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[5]);
                }
            }
            else if(source ==  mortgageButtons[6])
            {
                if(PLAYER_HAND[6].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[6];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[6]);
                }
            }
            else if(source ==  mortgageButtons[7])
            {
                if(PLAYER_HAND[7].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[7];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[7]);
                }
            }
            else if(source ==  mortgageButtons[8])
            {
                if(PLAYER_HAND[8].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[8];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[8]);
                }
            }
            else if(source ==  mortgageButtons[9])
            {
                if(PLAYER_HAND[9].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[9];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[9]);
                }
            }
            else if(source ==  mortgageButtons[10])
            {
                if(PLAYER_HAND[10].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[10];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[10]);
                }
            }
            else if(source ==  mortgageButtons[11])
            {
                if(PLAYER_HAND[11].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[11];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[11]);
                }
            }
            else if(source ==  mortgageButtons[12])
            {
                if(PLAYER_HAND[12].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[12];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[12]);
                }
            }
            else if(source ==  mortgageButtons[13])
            {
                if(PLAYER_HAND[13].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[13];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[13]);
                }
            }
            else if(source ==  mortgageButtons[14])
            {
                if(PLAYER_HAND[14].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[14];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[14]);
                }
            }
            else if(source ==  mortgageButtons[15])
            {
                if(PLAYER_HAND[15].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[15];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[15]);
                }
            }
            else if(source ==  mortgageButtons[16])
            {
                if(PLAYER_HAND[16].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[16];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[16]);
                }
            }
            else if(source ==  mortgageButtons[17])
            {
                if(PLAYER_HAND[17].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[17];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[17]);
                }
            }
            else if(source ==  mortgageButtons[18])
            {
                if(PLAYER_HAND[18].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[18];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[18]);
                }
            }
            else if(source ==  mortgageButtons[19])
            {
                if(PLAYER_HAND[19].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[19];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[19]);
                }
            }
            else if(source ==  mortgageButtons[20])
            {
                if(PLAYER_HAND[20].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[20];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[20]);
                }
            }
            else if(source ==  mortgageButtons[21])
            {
                if(PLAYER_HAND[21].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[21];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[21]);
                }
            }
            else if(source ==  mortgageButtons[22])
            {
                if(PLAYER_HAND[22].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[22];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[22]);
                }
            }
            else if(source ==  mortgageButtons[23])
            {
                if(PLAYER_HAND[23].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[23];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[23]);
                }
            }
            else if(source ==  mortgageButtons[24])
            {
                if(PLAYER_HAND[24].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[24];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[24]);
                }
            }
            else if(source ==  mortgageButtons[25])
            {
                if(PLAYER_HAND[25].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[25];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[25]);
                }
            }
            else if(source ==  mortgageButtons[26])
            {
                if(PLAYER_HAND[26].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[26];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[26]);
                }
            }
            else if(source ==  mortgageButtons[27])
            {
                if(PLAYER_HAND[27].isMortgaged())
                {
                    MESSAGE_BOX.printMessage("You have already mortgaged this property. " +
                            "Would you like to pay back this mortgage (un-mortgage)?");
                    for(int m = 0; m < MortgagePanels.length; m++) // hide all mortgaging buttons
                    {
                        MortgagePanels[m].setVisible(false);
                    }
                    // get correct property for potential un-mortgaging and give player options
                    CURRENT_PROPERTY = PLAYER_HAND[27];
                    UnMortgagePanel.setVisible(true);
                } else{
                    mortgageProperty(PLAYER_HAND[27]);
                }
            }
        }
    }

    private void mortgageProperty(Property currentProperty)
    {
        if(currentProperty.isPrimary()) // if primary must reset the rent of the whole set
        {
            int payment = currentProperty.getHousePrice()/2;
            if(currentProperty.getNumHouses() == 0) // mortgage property and set
            {
                try{
                    CURRENT_SET = GAME_SETS.getSet(currentProperty.getSetNumber(GAME_SETS));
                    if(CURRENT_SET.hasHouses()) // if there are any houses left in the set do not mortgage this property
                    {
                        MESSAGE_BOX.printMessage("This property cannot be mortgaged yet because there are other " +
                                "properties in this set that still have houses. Remove houses from other properties" +
                                " before mortgaging this one.");
                    }else{ // mortgage this property
                        currentProperty.mortgage(MESSAGE_BOX);
                        if(CURRENT_PLAYER.hasSet(CURRENT_SET)) // don't mortgage set unless player owns set
                            CURRENT_SET.mortgageSet(MESSAGE_BOX);
                    }
                } catch(SetNotFoundException set_e){
                    set_e.printStackTrace();
                }

            }
            else if(currentProperty.hasHotel()) // remove hotel and adjust rent accordingly
            {
                MESSAGE_BOX.printMessage("You have removed your hotel on " + currentProperty.getName());
                GUI.hideHotel(currentProperty);
                currentProperty.removeHotel(MESSAGE_BOX);
                CURRENT_PLAYER.payPlayer(payment);
            }
            else{ // remove house and adjust rent accordingly
                MESSAGE_BOX.printMessage("You have removed a house on " + currentProperty.getName());
                GUI.hideHouse(currentProperty);
                currentProperty.removeHouse(MESSAGE_BOX);
                CURRENT_PLAYER.payPlayer(payment);
            }
        } else{ // restaurant or utility
            currentProperty.mortgage(MESSAGE_BOX);
            currentProperty.updateRent(DICE);
        }

    }

    /**
     * addButtons adds the mortgage buttons to the background according to what properties are in
     * the player's hand
     */
    private void addButtons()
    {
        int xShift = 1;
        int yShift = 200;
        for(int m = 0; m < PLAYER_HAND.length; m++)
        {
            mortgageButtons[m] = new JButton(PLAYER_HAND[m].getName());
            MortgagePanels[m] = new JPanel();
            MortgagePanels[m].setSize(90, 50);
            mortgageButtons[m].setLocation(265,  500);
            mortgageButtons[m].setPreferredSize(new Dimension(90, 50));
            MortgagePanels[m].setBackground(Color.BLUE);
            MortgagePanels[m].setAlignmentX(Component.CENTER_ALIGNMENT);
            MortgagePanels[m].setAlignmentY(Component.BOTTOM_ALIGNMENT);
            MortgagePanels[m].setLayout(new BoxLayout(MortgagePanels[m], BoxLayout.PAGE_AXIS));
            MortgagePanels[m].add(mortgageButtons[m]);
            MortgagePanels[m].add(Box.createVerticalGlue());
            MortgagePanels[m].setLocation(xShift,yShift);
            mortgageButtons[m].addActionListener(this);

            xShift += 100;
            if(m == 7)
            {
                xShift -= 800;
                yShift += 80;
            }

            background.add(MortgagePanels[m]);
        }

        UnMortgagePanel.setSize(100, 100);
        unMortgageButton.setLocation(265, 500);
        unMortgageButton.setPreferredSize(new Dimension(100, 50));
        declineUnMortgage.setPreferredSize(new Dimension(100, 50));
        UnMortgagePanel.setBackground(Color.GREEN);
        UnMortgagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        UnMortgagePanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        UnMortgagePanel.setLayout(new BoxLayout(UnMortgagePanel, BoxLayout.PAGE_AXIS));
        UnMortgagePanel.add(unMortgageButton);
        UnMortgagePanel.add(declineUnMortgage);
        UnMortgagePanel.add(Box.createVerticalGlue());
        UnMortgagePanel.setLocation(350, 1);
        unMortgageButton.addActionListener(this);
        declineUnMortgage.addActionListener(this);
        UnMortgagePanel.setVisible(false);

        // buttons below here should always be visible

        StatsButtonPanel.setSize(100, 50);
        statsButton.setLocation(265,  500);
        statsButton.setPreferredSize(new Dimension(90, 50));
        StatsButtonPanel.setBackground(Color.BLUE);
        StatsButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        StatsButtonPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        StatsButtonPanel.setLayout(new BoxLayout(StatsButtonPanel, BoxLayout.PAGE_AXIS));
        StatsButtonPanel.add(statsButton);
        StatsButtonPanel.add(Box.createVerticalGlue());
        StatsButtonPanel.setLocation(699, 427);
        statsButton.addActionListener(this);

        DonePanel.setSize(78, 50);
        doneButton.setLocation(265,  500);
        doneButton.setPreferredSize(new Dimension(78, 50));
        DonePanel.setBackground(Color.RED);
        DonePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        DonePanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        DonePanel.setLayout(new BoxLayout(DonePanel, BoxLayout.PAGE_AXIS));
        DonePanel.add(doneButton);
        DonePanel.add(Box.createVerticalGlue());
        DonePanel.setLocation(1, 427);
        doneButton.addActionListener(this);

        background.add(UnMortgagePanel);
        background.add(StatsButtonPanel);
        background.add(DonePanel);
    }
}
