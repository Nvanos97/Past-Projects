/**
 * Class TradeGUI creates the menu with which players can trade their properties
 */

import javax.imageio.ImageIO;
import javax.print.attribute.standard.JobMediaSheetsSupported;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TradeGUI extends JFrame implements ActionListener
{
    private ImagePanel background;
    private Image backgroundImage;
    private JPanel PlayerButtonPanel = new JPanel();
    private JPanel StatsPanel = new JPanel();
    private JPanel RequestMoneyPanel = new JPanel();
    private JPanel OfferMoneyPanel = new JPanel();
    private JPanel NextPanel = new JPanel();
    private JPanel AcceptPanel = new JPanel();
    private JPanel DonePanel = new JPanel();
    private JPanel[] OptionsPanel;
    private JPanel[] CurrentPlayerPropertyPanel;
    private JButton requestTwentyButton = new JButton("Request $20");
    private JButton requestOneHundredButton = new JButton("Request $100");
    private JButton offerTwentyButton = new JButton("Offer $20");
    private JButton offerOneHundredButton = new JButton("Offer $100");
    private JButton doneButton = new JButton("Done");
    private JButton nextButton = new JButton("Next");
    private JButton acceptButton = new JButton("Accept");
    private JButton declineButton = new JButton("Decline");
    private JButton[] playerButtons;
    private JButton[] playerStatButtons;
    private JButton[] optionsButtons;
    private JButton[] currentPlayerPropertyButtons;

    private Player[] PLAYERS;
    private Player CURRENT_PLAYER;
    private Player OTHER_PLAYER;
    private ZagopolyTextWindow MESSAGE_BOX;
    private GameBoardUI GUI;
    private The_Sets GAME_SETS;
    private ZagopolyDice DICE;
    private Property[] OTHER_PLAYER_PROPERTIES;
    private Property[] CURRENT_PLAYER_PROPERTIES;
    private ArrayList<Property> RequestedProperties = new ArrayList<>();
    private ArrayList<Property> OfferedProperties = new ArrayList<>();
    private int REQUESTED_MONEY = 0;
    private int OFFERED_MONEY = 0;
    private int DEFAULT_WIDTH = 800;
    private int DEFAULT_HEIGHT = 450;

    /**
     * constructor for class TradeGUI
     * @param players are the players in the game
     */
    public TradeGUI(Player players[], int currentPlayer, ZagopolyTextWindow TextWindow, GameBoardUI gui, The_Sets gameSets, ZagopolyDice theDice)
    {
        PLAYERS = players;
        CURRENT_PLAYER = PLAYERS[currentPlayer];
        MESSAGE_BOX = TextWindow;
        GUI = gui;
        GAME_SETS = gameSets;
        DICE = theDice;
        playerButtons = new JButton[PLAYERS.length];
        playerStatButtons = new JButton[PLAYERS.length];

        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setTitle("Trade Menu");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try{
            backgroundImage = ImageIO.read(new File("res/tradeBackground.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
        background = new ImagePanel(backgroundImage);
        addButtons();

        setVisible(true);
        add(background);
    }

    /**
     * actionPerformed overrides actionPerformed of ActionListener
     * @param e is the event in question
     */
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();

        if(source == playerButtons[0]) // player 1
        {
            if(PLAYERS[0] != CURRENT_PLAYER)
            {
                MESSAGE_BOX.printMessage("You have chosen to trade with player 1.");
                OTHER_PLAYER = PLAYERS[0];
                initTrade();
            } else{ // player tries to trade with himself
                MESSAGE_BOX.printMessage("You cannot trade with yourself. Please " +
                        "select a different player.");
            }
        }
        else if(source == playerButtons[1]) // player 2
        {
            if(PLAYERS[1] != CURRENT_PLAYER)
            {
                MESSAGE_BOX.printMessage("You have chosen to trade with player 2.");
                OTHER_PLAYER = PLAYERS[1];
                initTrade();
            } else{ // player tries to trade with himself
                MESSAGE_BOX.printMessage("You cannot trade with yourself. Please " +
                        "select a different player.");
            }
        }
        else if(playerButtons.length >= 3 && source == playerButtons[2]) // player 3
        {
            if(PLAYERS[2] != CURRENT_PLAYER)
            {
                MESSAGE_BOX.printMessage("You have chosen to trade with player 3.");
                OTHER_PLAYER = PLAYERS[2];
                initTrade();
            } else{ // player tries to trade with himself
                MESSAGE_BOX.printMessage("You cannot trade with yourself. Please " +
                        "select a different player.");
            }
        }
        else if(playerButtons.length == 4 && source ==  playerButtons[3]) // player 4
        {
            if(PLAYERS[3] != CURRENT_PLAYER)
            {
                MESSAGE_BOX.printMessage("You have chosen to trade with player 4.");
                OTHER_PLAYER = PLAYERS[3];
                initTrade();
            } else{ // player tries to trade with himself
                MESSAGE_BOX.printMessage("You cannot trade with yourself. Please " +
                        "select a different player.");
            }
        }

        else if(source == doneButton) // done trading
        {
            MESSAGE_BOX.printMessage("You are done trading for now.");
            GUI.setVisible(true);
            setVisible(false);
        }

        else if(source == nextButton) // player is done with a step
        {
            if(RequestMoneyPanel.isVisible()) // player is requesting
            {
                if(RequestedProperties.isEmpty() && REQUESTED_MONEY == 0) // player hasn't requested anything
                {
                    MESSAGE_BOX.printMessage("You have not selected any properties or requested any money. " +
                            "Please select a property or ask for money before moving on.");
                } else{
                    MESSAGE_BOX.printMessage("Now choose what you want to trade away.");
                    for(int o = 0; o < OptionsPanel.length; o++)
                    {
                        OptionsPanel[o].setVisible(false);
                    }
                    midTrade();
                }
            } else{ // player is offering
                if(OfferedProperties.isEmpty() && OFFERED_MONEY == 0)
                {
                    MESSAGE_BOX.printMessage("You haven't offered a property in return or any money in return. " +
                            "Please select a property or offer a sum of money.");
                } else{
                    NextPanel.setVisible(false);
                    for(int p = 0; p < CurrentPlayerPropertyPanel.length; p++)
                    {
                        CurrentPlayerPropertyPanel[p].setVisible(false);
                    }
                    showTrade();
                }
            }
        }

        // stats
        else if(source == playerStatButtons[0]) // player 1
        {
            PLAYERS[0].displayStats(MESSAGE_BOX, GUI);
        }
        else if(source == playerStatButtons[1]) // player 2
        {
            PLAYERS[1].displayStats(MESSAGE_BOX, GUI);
        }
        else if(playerStatButtons.length >= 3 && source == playerStatButtons[2]) // player 3
        {
            PLAYERS[2].displayStats(MESSAGE_BOX, GUI);
        }
        else if(playerStatButtons.length == 4 && source == playerStatButtons[3]) // player 4
        {
            PLAYERS[3].displayStats(MESSAGE_BOX, GUI);
        }

        else if(source == requestTwentyButton) // player asks for money on this trade
        {
            if((REQUESTED_MONEY + 20) > OTHER_PLAYER.getBalance())
            {
                MESSAGE_BOX.printMessage("You cannot request more money from a player than they have in their account.");
            } else{
                REQUESTED_MONEY += 20;
            }
        }
        else if(source == requestOneHundredButton) // player asks for money on this trade
        {
            if((REQUESTED_MONEY + 100) > OTHER_PLAYER.getBalance())
            {
                MESSAGE_BOX.printMessage("You cannot request more money from a player than they have in their account.");
            } else{
                REQUESTED_MONEY += 100;
            }
        }

        // theoretically possible for 1 player to have every property, but highly improbable
        else if(optionsButtons.length >= 1 && source ==  optionsButtons[0]) // other player's first property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[0]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[0]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[0].getName());
            }
        }
        else if(optionsButtons.length >= 2 && source ==  optionsButtons[1]) // other player's second property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[1]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[1]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[1].getName());
            }
        }
        else if(optionsButtons.length >= 3 && source ==  optionsButtons[2]) // other player's third property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[2]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[2]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[2].getName());
            }
        }
        else if(optionsButtons.length >= 4 && source ==  optionsButtons[3]) // other player's fourth property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[3]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[3]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[3].getName());
            }
        }
        else if(optionsButtons.length >= 5 && source ==  optionsButtons[4]) // other player's fifth property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[4]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[4]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[4].getName());
            }
        }
        else if(optionsButtons.length >= 6 && source ==  optionsButtons[5]) // other player's sixth property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[5]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[5]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[5].getName());
            }
        }
        else if(optionsButtons.length >= 7 && source ==  optionsButtons[6]) // other player's seventh property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[6]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[6]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[6].getName());
            }
        }
        else if(optionsButtons.length >= 8 && source ==  optionsButtons[7]) // other player's eighth property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[7]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[7]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[7].getName());
            }
        }
        else if(optionsButtons.length >= 9 && source ==  optionsButtons[8]) // other player's ninth property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[8]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[8]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[8].getName());
            }
        }
        else if(optionsButtons.length >= 10 && source ==  optionsButtons[9]) // other player's tenth property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[9]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[9]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[9].getName());
            }
        }
        else if(optionsButtons.length >= 11 && source ==  optionsButtons[10]) // other player's eleventh property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[10]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[10]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[10].getName());
            }
        }
        else if(optionsButtons.length >= 12 && source ==  optionsButtons[11]) // other player's twelfth property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[11]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[11]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[11].getName());
            }
        }
        else if(optionsButtons.length >= 13 && source ==  optionsButtons[12]) // other player's thirteenth property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[12]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[12]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[12].getName());
            }
        }
        else if(optionsButtons.length >= 14 && source ==  optionsButtons[13]) // other player's fourteenth property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[13]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[13]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[13].getName());
            }
        }
        else if(optionsButtons.length >= 15 && source ==  optionsButtons[14]) // other player's fifteenth property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[14]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[14]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[14].getName());
            }
        }
        else if(optionsButtons.length >= 16 && source ==  optionsButtons[15]) // other player's sixteenth property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[15]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[15]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[15].getName());
            }
        }
        else if(optionsButtons.length >= 17 && source ==  optionsButtons[16]) // other player's seventeenth property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[16]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[16]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[16].getName());
            }
        }
        else if(optionsButtons.length >= 18 && source ==  optionsButtons[17]) // other player's eighteenth property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[17]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[17]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[17].getName());
            }
        }
        else if(optionsButtons.length >= 19 && source ==  optionsButtons[18]) // other player's nineteenth property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[18]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[18]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[18].getName());
            }
        }
        else if(optionsButtons.length >= 20 && source ==  optionsButtons[19]) // other player's twentieth property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[19]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[19]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[19].getName());
            }
        }
        else if(optionsButtons.length >= 21 && source ==  optionsButtons[20]) // other player's twenty-first property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[20]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[20]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[20].getName());
            }
        }
        else if(optionsButtons.length >= 22 && source ==  optionsButtons[21]) // other player's twenty-second property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[21]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[21]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[21].getName());
            }
        }
        else if(optionsButtons.length >= 23 && source ==  optionsButtons[22]) // other player's twenty-third property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[22]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[22]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[22].getName());
            }
        }
        else if(optionsButtons.length >= 24 && source ==  optionsButtons[23]) // other player's twenty-fourth property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[23]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[23]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[23].getName());
            }
        }
        else if(optionsButtons.length >= 25 && source ==  optionsButtons[24]) // other player's twenty-fifth property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[24]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[24]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[24].getName());
            }
        }
        else if(optionsButtons.length >= 26 && source ==  optionsButtons[25]) // other player's twenty-sixth property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[25]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[25]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[25].getName());
            }
        }
        else if(optionsButtons.length >= 27 && source ==  optionsButtons[26]) // other player's twenty-seventh property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[26]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[26]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[26].getName());
            }
        }
        else if(optionsButtons.length == 28 && source ==  optionsButtons[27]) // other player's twenty-eighth property
        {
            if(RequestedProperties.contains(OTHER_PLAYER_PROPERTIES[27]))
            {
                MESSAGE_BOX.printMessage("You have already requested this property. " +
                        "Please select more or click 'next'.");
            } else{
                RequestedProperties.add(OTHER_PLAYER_PROPERTIES[27]);
                MESSAGE_BOX.printMessage("You have requested the property " +
                        OTHER_PLAYER_PROPERTIES[27].getName());
            }
        }

        // now repeat this same process again with current player's property buttons :(
        else if(source == offerTwentyButton)
        {
            if((OFFERED_MONEY + 20) > CURRENT_PLAYER.getBalance())
            {
                MESSAGE_BOX.printMessage("You cannot give a player more money than is in your account.");
            } else{
                OFFERED_MONEY += 20;
            }
        }
        else if(source == offerOneHundredButton)
        {
            if((OFFERED_MONEY + 100) > CURRENT_PLAYER.getBalance())
            {
                MESSAGE_BOX.printMessage("You cannot give a player more money than is in your account.");
            } else{
                OFFERED_MONEY += 100;
            }
        }

        else if(currentPlayerPropertyButtons.length >= 1 && source == currentPlayerPropertyButtons[0])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[0]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[0]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[0].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 2 && source == currentPlayerPropertyButtons[1])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[1]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[1]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[1].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 3 && source == currentPlayerPropertyButtons[2])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[2]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[2]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[2].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 4 && source == currentPlayerPropertyButtons[3])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[3]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[3]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[3].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 5 && source == currentPlayerPropertyButtons[4])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[4]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[4]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[4].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 6 && source == currentPlayerPropertyButtons[5])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[5]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[5]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[5].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 7 && source == currentPlayerPropertyButtons[6])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[6]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[6]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[6].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 8 && source == currentPlayerPropertyButtons[7])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[7]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[7]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[7].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 9 && source == currentPlayerPropertyButtons[8])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[8]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[8]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[8].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 10 && source == currentPlayerPropertyButtons[9])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[9]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[9]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[9].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 11 && source == currentPlayerPropertyButtons[10])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[10]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[10]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[10].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 12 && source == currentPlayerPropertyButtons[11])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[11]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[11]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[11].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 13 && source == currentPlayerPropertyButtons[12])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[12]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[12]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[12].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 14 && source == currentPlayerPropertyButtons[13])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[13]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[13]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[13].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 15 && source == currentPlayerPropertyButtons[14])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[14]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[14]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[14].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 16 && source == currentPlayerPropertyButtons[15])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[15]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[15]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[15].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 17 && source == currentPlayerPropertyButtons[16])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[16]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[16]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[16].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 18 && source == currentPlayerPropertyButtons[17])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[17]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[17]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[17].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 19 && source == currentPlayerPropertyButtons[18])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[18]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[18]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[18].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 20 && source == currentPlayerPropertyButtons[19])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[19]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[19]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[19].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 21 && source == currentPlayerPropertyButtons[20])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[20]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[20]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[20].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 22 && source == currentPlayerPropertyButtons[21])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[21]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[21]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[21].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 23 && source == currentPlayerPropertyButtons[22])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[22]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[22]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[22].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 24 && source == currentPlayerPropertyButtons[23])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[23]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[23]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[23].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 25 && source == currentPlayerPropertyButtons[24])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[24]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[24]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[24].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 26 && source == currentPlayerPropertyButtons[25])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[25]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[25]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[25].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 27 && source == currentPlayerPropertyButtons[26])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[26]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[26]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[26].getName());
            }
        }
        else if(currentPlayerPropertyButtons.length >= 28 && source == currentPlayerPropertyButtons[27])
        {
            if(OfferedProperties.contains(CURRENT_PLAYER_PROPERTIES[27]))
            {
                MESSAGE_BOX.printMessage("You have already offered this property. Please " +
                        "select more or click 'next'.");
            } else{
                OfferedProperties.add(CURRENT_PLAYER_PROPERTIES[27]);
                MESSAGE_BOX.printMessage("You have offered the property " +
                        CURRENT_PLAYER_PROPERTIES[27].getName());
            }
        }

        else if(source == acceptButton)
        {
            MESSAGE_BOX.printMessage("Player " + (OTHER_PLAYER.getPlayerNum() + 1) + " " +
                    "has accepted your offer!");
            AcceptPanel.setVisible(false);

            doTrade();
            PlayerButtonPanel.setVisible(true);
            MESSAGE_BOX.printMessage("Would you like to make another trade?");
        }

        else if(source == declineButton) // other player doesn't want to trade
        {
            MESSAGE_BOX.printMessage("Player " + (OTHER_PLAYER.getPlayerNum() + 1) + "" +
                    " has refused your offer. Would you like to make a different trade?");

            RequestedProperties.clear();
            OfferedProperties.clear();
            REQUESTED_MONEY = 0;
            OFFERED_MONEY = 0;

            AcceptPanel.setVisible(false);
            PlayerButtonPanel.setVisible(true);
        }
    }

    /**
     * initTrade starts a trade between two players
     */
    private void initTrade()
    {
        PlayerButtonPanel.setVisible(false);
        MESSAGE_BOX.printMessage("Select what you want to trade for.");

        OTHER_PLAYER_PROPERTIES = OTHER_PLAYER.getHand();
        OptionsPanel = new JPanel[OTHER_PLAYER_PROPERTIES.length];
        optionsButtons = new JButton[OTHER_PLAYER_PROPERTIES.length];

        int xShift = 0;
        int yShift = 0;
        for(int o = 0; o < OTHER_PLAYER_PROPERTIES.length; o++)
        {
            optionsButtons[o] = new JButton(OTHER_PLAYER_PROPERTIES[o].getName());
            OptionsPanel[o] = new JPanel();
            OptionsPanel[o].setSize(90, 50);
            optionsButtons[o].setLocation(265,  500);
            optionsButtons[o].setPreferredSize(new Dimension(90, 50));
            OptionsPanel[o].setBackground(Color.BLUE);
            OptionsPanel[o].setAlignmentX(Component.CENTER_ALIGNMENT);
            OptionsPanel[o].setAlignmentY(Component.BOTTOM_ALIGNMENT);
            OptionsPanel[o].setLayout(new BoxLayout(OptionsPanel[o], BoxLayout.PAGE_AXIS));
            OptionsPanel[o].add(optionsButtons[o]);
            OptionsPanel[o].add(Box.createVerticalGlue());
            OptionsPanel[o].setLocation(xShift,yShift);
            optionsButtons[o].addActionListener(this);

            xShift += 100;
            if(o == 7)
            {
                xShift -= 800;
                yShift += 60;
            }

            background.add(OptionsPanel[o]);
        }
        RequestMoneyPanel.setVisible(true);
        NextPanel.setVisible(true);
    }

    /**
     * midTrade gets what the current player is offering to trade away
     */
    private void midTrade()
    {
        CURRENT_PLAYER_PROPERTIES = CURRENT_PLAYER.getHand();
        CurrentPlayerPropertyPanel = new JPanel[CURRENT_PLAYER_PROPERTIES.length];
        currentPlayerPropertyButtons = new JButton[CURRENT_PLAYER_PROPERTIES.length];

        int xShift = 0;
        int yShift = 0;
        for(int p = 0; p < CURRENT_PLAYER_PROPERTIES.length; p++)
        {
            currentPlayerPropertyButtons[p] = new JButton(CURRENT_PLAYER_PROPERTIES[p].getName());
            CurrentPlayerPropertyPanel[p] = new JPanel();
            CurrentPlayerPropertyPanel[p].setSize(90, 50);
            currentPlayerPropertyButtons[p].setLocation(265,  500);
            currentPlayerPropertyButtons[p].setPreferredSize(new Dimension(90, 50));
            CurrentPlayerPropertyPanel[p].setBackground(Color.BLUE);
            CurrentPlayerPropertyPanel[p].setAlignmentX(Component.CENTER_ALIGNMENT);
            CurrentPlayerPropertyPanel[p].setAlignmentY(Component.BOTTOM_ALIGNMENT);
            CurrentPlayerPropertyPanel[p].setLayout(new BoxLayout(CurrentPlayerPropertyPanel[p], BoxLayout.PAGE_AXIS));
            CurrentPlayerPropertyPanel[p].add(currentPlayerPropertyButtons[p]);
            CurrentPlayerPropertyPanel[p].add(Box.createVerticalGlue());
            CurrentPlayerPropertyPanel[p].setLocation(xShift,yShift);
            currentPlayerPropertyButtons[p].addActionListener(this);

            xShift += 100;
            if(p == 7)
            {
                xShift -= 800;
                yShift += 60;
            }

            background.add(CurrentPlayerPropertyPanel[p]);
        }
        RequestMoneyPanel.setVisible(false);
        OfferMoneyPanel.setVisible(true);
    }

    /**
     * showTrade shows the final trade before a decision is made
     */
    private void showTrade()
    {
        OfferMoneyPanel.setVisible(false);
        Property[] RequestedPropertiesArray = new Property[RequestedProperties.size()];
        Property[] OfferedPropertiesArray = new Property[OfferedProperties.size()];
        RequestedPropertiesArray = RequestedProperties.toArray(RequestedPropertiesArray);
        OfferedPropertiesArray = OfferedProperties.toArray(OfferedPropertiesArray);

        MESSAGE_BOX.printMessage("Items requested:");
        for(int p = 0; p < RequestedPropertiesArray.length; p++)
        {
            MESSAGE_BOX.printMessage(RequestedPropertiesArray[p].getName());
        }
        if(REQUESTED_MONEY > 0)
        {
            MESSAGE_BOX.printMessage("Money: " + REQUESTED_MONEY + " bulldog bucks.");
        }

        MESSAGE_BOX.printMessage("Items offered in return:");
        for(int p = 0; p < OfferedPropertiesArray.length; p++)
        {
            MESSAGE_BOX.printMessage(OfferedPropertiesArray[p].getName());
        }
        if(OFFERED_MONEY > 0)
        {
            MESSAGE_BOX.printMessage("Money: " + OFFERED_MONEY + " bulldog bucks.");
        }

        MESSAGE_BOX.printMessage("Player " + (OTHER_PLAYER.getPlayerNum() + 1) + ", " +
                "do you accept this trade?");

        AcceptPanel.setVisible(true);
    }

    /**
     * doTrade performs a trade between two players
     */
    private void doTrade()
    {
        CURRENT_PLAYER.finePlayer(OFFERED_MONEY);
        OTHER_PLAYER.payPlayer(OFFERED_MONEY);
        CURRENT_PLAYER.payPlayer(REQUESTED_MONEY);
        OTHER_PLAYER.finePlayer(REQUESTED_MONEY);

        Property[] RequestedPropertiesArray = new Property[RequestedProperties.size()];
        Property[] OfferedPropertiesArray = new Property[OfferedProperties.size()];
        RequestedPropertiesArray = RequestedProperties.toArray(RequestedPropertiesArray);
        OfferedPropertiesArray = OfferedProperties.toArray(OfferedPropertiesArray);

        for(int p = 0; p < OfferedPropertiesArray.length; p++) // remove offered properties from original owner's ownership
        {
            CURRENT_PLAYER.removeProperty(OfferedPropertiesArray[p]);
            OTHER_PLAYER.takeOwnershipOfProperty(OfferedPropertiesArray[p]);
            OfferedPropertiesArray[p].addOwnerFree(OTHER_PLAYER);
            if(OfferedPropertiesArray[p].isRestaurant() || OfferedPropertiesArray[p].isUtility())
            {
                OfferedPropertiesArray[p].updateRent(DICE);
            }

            if(OfferedPropertiesArray[p].isPrimary()) // remove ownership of set from current player
            {
                try{
                    Set currentSet = GAME_SETS.getSet(OfferedPropertiesArray[p].getSetNumber(GAME_SETS));
                    if(CURRENT_PLAYER.hasSet(currentSet)) // current player just lost this set
                    {
                        CURRENT_PLAYER.removeSet(currentSet);
                        currentSet.removeOwner();
                    }
                    currentSet.addOwner(OTHER_PLAYER, MESSAGE_BOX);
                    if(currentSet.isOwned()) // other player gained this set
                    {
                        OTHER_PLAYER.takeOwnerShipOfSet(currentSet);
                        if(OfferedPropertiesArray[p].rentIsNotDoubled())
                            currentSet.updateRents(DICE);
                    }
                } catch(SetNotFoundException e)
                {
                    e.printStackTrace();
                }
            }

        }
        for(int p = 0; p < RequestedPropertiesArray.length; p++) // remove requested properties from original owner's ownership
        {
            OTHER_PLAYER.removeProperty(RequestedPropertiesArray[p]);
            CURRENT_PLAYER.takeOwnershipOfProperty(RequestedPropertiesArray[p]);
            RequestedPropertiesArray[p].addOwnerFree(CURRENT_PLAYER);
            if(RequestedPropertiesArray[p].isRestaurant() || RequestedPropertiesArray[p].isUtility())
            {
                RequestedPropertiesArray[p].updateRent(DICE);
            }

            if(RequestedPropertiesArray[p].isPrimary()) // remove ownership of set from other player
            {
                try{
                    Set currentSet = GAME_SETS.getSet(RequestedPropertiesArray[p].getSetNumber(GAME_SETS));
                    if(OTHER_PLAYER.hasSet(currentSet)) // other player just lost this set
                    {
                        OTHER_PLAYER.removeSet(currentSet);
                        currentSet.removeOwner();
                    }
                    currentSet.addOwner(CURRENT_PLAYER, MESSAGE_BOX);
                    if(currentSet.isOwned()) // current player gained this set
                    {
                        CURRENT_PLAYER.takeOwnerShipOfSet(currentSet);
                        if(RequestedPropertiesArray[p].rentIsNotDoubled())
                            currentSet.updateRents(DICE);
                    }
                }catch(SetNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        }
        RequestedProperties.clear();
        OfferedProperties.clear();
        REQUESTED_MONEY = 0;
        OFFERED_MONEY = 0;
    }

    /**
     * addButtons adds the buttons to the GUI
     */
    private void addButtons()
    {
        PlayerButtonPanel.setSize(90, 160);
        PlayerButtonPanel.setBackground(Color.BLUE);
        PlayerButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        PlayerButtonPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        PlayerButtonPanel.setLayout(new BoxLayout(PlayerButtonPanel, BoxLayout.PAGE_AXIS));
        for(int p = 0; p < playerButtons.length; p++) // initialize every button
        {
            playerButtons[p] = new JButton("Player " + (p + 1));
            playerButtons[p].setPreferredSize(new Dimension(80, 60));
            PlayerButtonPanel.add(playerButtons[p]);
            playerButtons[p].addActionListener(this);
        }
        PlayerButtonPanel.add(Box.createVerticalGlue());
        PlayerButtonPanel.setLocation(510, 120);

        StatsPanel.setSize(90, 160);
        StatsPanel.setBackground(Color.GREEN);
        StatsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        StatsPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        StatsPanel.setLayout(new BoxLayout(StatsPanel, BoxLayout.PAGE_AXIS));
        for(int p = 0; p < playerStatButtons.length; p++) // initialize every button
        {
            playerStatButtons[p] = new JButton("Player " + (p + 1) + " Stats");
            playerStatButtons[p].setPreferredSize(new Dimension(80, 60));
            StatsPanel.add(playerStatButtons[p]);
            playerStatButtons[p].addActionListener(this);
        }
        StatsPanel.add(Box.createVerticalGlue());
        StatsPanel.setLocation(210, 120);

        RequestMoneyPanel.setSize(125, 60);
        requestTwentyButton.setLocation(265,  500);
        requestTwentyButton.setPreferredSize(new Dimension(78, 50));
        requestOneHundredButton.setPreferredSize(new Dimension(78, 50));
        RequestMoneyPanel.setBackground(Color.BLACK);
        RequestMoneyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        RequestMoneyPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        RequestMoneyPanel.setLayout(new BoxLayout(RequestMoneyPanel, BoxLayout.PAGE_AXIS));
        RequestMoneyPanel.add(requestTwentyButton);
        RequestMoneyPanel.add(requestOneHundredButton);
        RequestMoneyPanel.add(Box.createVerticalGlue());
        RequestMoneyPanel.setLocation(335, 370);
        requestTwentyButton.addActionListener(this);
        requestOneHundredButton.addActionListener(this);
        RequestMoneyPanel.setVisible(false);

        OfferMoneyPanel.setSize(120, 50);
        offerTwentyButton.setLocation(265,  500);
        offerTwentyButton.setPreferredSize(new Dimension(78, 50));
        offerOneHundredButton.setPreferredSize(new Dimension(78, 50));
        OfferMoneyPanel.setBackground(Color.GREEN);
        OfferMoneyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        OfferMoneyPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        OfferMoneyPanel.setLayout(new BoxLayout(OfferMoneyPanel, BoxLayout.PAGE_AXIS));
        OfferMoneyPanel.add(offerTwentyButton);
        OfferMoneyPanel.add(offerOneHundredButton);
        OfferMoneyPanel.add(Box.createVerticalGlue());
        OfferMoneyPanel.setLocation(335, 377);
        offerTwentyButton.addActionListener(this);
        offerOneHundredButton.addActionListener(this);
        OfferMoneyPanel.setVisible(false);

        AcceptPanel.setSize(80, 100);
        acceptButton.setLocation(265,  500);
        acceptButton.setPreferredSize(new Dimension(78, 50));
        declineButton.setPreferredSize(new Dimension(78, 50));
        AcceptPanel.setBackground(Color.BLACK);
        AcceptPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        AcceptPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        AcceptPanel.setLayout(new BoxLayout(AcceptPanel, BoxLayout.PAGE_AXIS));
        AcceptPanel.add(acceptButton);
        AcceptPanel.add(declineButton);
        AcceptPanel.add(Box.createVerticalGlue());
        AcceptPanel.setLocation(360, 1);
        acceptButton.addActionListener(this);
        declineButton.addActionListener(this);
        AcceptPanel.setVisible(false);

        NextPanel.setSize(77, 50);
        nextButton.setLocation(265,  500);
        nextButton.setPreferredSize(new Dimension(78, 50));
        NextPanel.setBackground(Color.CYAN);
        NextPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        NextPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        NextPanel.setLayout(new BoxLayout(NextPanel, BoxLayout.PAGE_AXIS));
        NextPanel.add(nextButton);
        NextPanel.add(Box.createVerticalGlue());
        NextPanel.setLocation(721, 377);
        nextButton.addActionListener(this);
        NextPanel.setVisible(false);

        DonePanel.setSize(78, 50);
        doneButton.setLocation(265,  500);
        doneButton.setPreferredSize(new Dimension(78, 50));
        DonePanel.setBackground(Color.RED);
        DonePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        DonePanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        DonePanel.setLayout(new BoxLayout(DonePanel, BoxLayout.PAGE_AXIS));
        DonePanel.add(doneButton);
        DonePanel.add(Box.createVerticalGlue());
        DonePanel.setLocation(1, 377);
        doneButton.addActionListener(this);

        background.add(PlayerButtonPanel);
        background.add(StatsPanel);
        background.add(RequestMoneyPanel);
        background.add(OfferMoneyPanel);
        background.add(NextPanel);
        background.add(AcceptPanel);
        background.add(DonePanel);
    }
}
