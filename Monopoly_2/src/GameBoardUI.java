/**
 * This class creates the game board GUI
 */

import javafx.geometry.Pos;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class GameBoardUI extends JFrame implements ActionListener
{
    private JPanel DiceButtonPanel = new JPanel();
    private JPanel PostRollButtonsPanel = new JPanel();
    private JPanel PostTurnButtonsPanel = new JPanel();
    private JPanel JailButtonsPanel = new JPanel();
    private JPanel SelectSetPanel = new JPanel();
    private JPanel SelectPropertyPanel = new JPanel();
    private JPanel HotelButtonPanel = new JPanel();
    private JPanel MortgagePanel = new JPanel();
    private JPanel StatsButtonPanel = new JPanel();
    private JPanel ForfeitButtonPanel = new JPanel();
    private JPanel QuitButtonPanel = new JPanel();
    private ImagePanel background;
    private Image houseImage;
    private Image hotelImage;
    private Image backgroundImage;
    private ImagePanel[] HousePanel;
    private ImagePanel[] HotelPanel;
    private ImagePanel[] playerIcons;
    private JButton diceButton = new JButton("Roll");
    private JButton buyButton = new JButton("Buy Property");
    private JButton passButton = new JButton("Pass Property");
    private JButton addHouseButton = new JButton("Add House");
    private JButton endTurnButton = new JButton("End Turn");
    private JButton stayButton = new JButton("Stay");
    private JButton bailoutButton = new JButton("Pay 50");
    private JButton getOutOfJailFreeButton = new JButton("Play Card");
    private JButton addHotelButton = new JButton("Add Hotel");
    private JButton declineHotelButton = new JButton("No Thanks");
    private JButton mortgageButton = new JButton("Mortgage Property");
    private JButton tradeButton = new JButton("Trade Property");
    private JButton statsButton = new JButton("Player Stats");
    private JButton payDebtButton = new JButton("Pay Debt");
    private JButton forfeitButton = new JButton("Forfeit");
    private JButton quitButton = new JButton("Quit Game");
    private JButton[] selectPropertyButton = new JButton[3];
    private JButton[] selectSetButton = new JButton[8];
    private PlayerIcon[] gamePieces;

    private ZagopolyTextWindow MESSAGE_BOX = new ZagopolyTextWindow();
    private ZagopolyDice DICE = new ZagopolyDice(2);

    private int DEFAULT_WIDTH = 680;
    private int DEFAULT_HEIGHT = 780;
    private int CURRENT_PLAYER = 0;
    private int ROLL;
    private int DOUBLES_COUNT = 0;

    private Player[] PLAYERS;
    private Board THE_BOARD;
    private The_Sets GAME_SETS;
    private Set CURRENT_SET;
    private Property CURRENT_PROPERTY;


    /**
     * constructor for class GameBoardUI
     * @param numberOfPlayers is the number of players in the game
     */
    public GameBoardUI(int numberOfPlayers)
    {
        // startup procedures
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setTitle("Zagopoly");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add background (board)
        try {
            backgroundImage = ImageIO.read(new File("res/board.png"));
            houseImage = ImageIO.read(new File("res/House.png"));
            hotelImage = ImageIO.read(new File("res/Hotel.png"));
        } catch(IOException e){
            e.printStackTrace();
        }
        background = new ImagePanel(backgroundImage);
        addIcons(numberOfPlayers);
        addHouseImages();
        addHotelImages();
        addButtons();
        add(background);
        initPlayers();
        startGame(numberOfPlayers);

        THE_BOARD = new Board(PLAYERS);
        GAME_SETS = new The_Sets(THE_BOARD);
        ROLL = 0;

        this.setVisible(true);
    }

    /**
     * doDrawing draws a figure on the game board
     * @param g is the object that draws the images on the board
     */
    public void doDrawing(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        for(int i = 0; i < gamePieces.length; i++)
        {
            g2d.drawImage(gamePieces[i].getImage(i), gamePieces[i].getX(), gamePieces[i].getY(), this);
        }
    }

    /**
     * overrides actionPerformed of ActionListener
     * @param e is the event in question
     */
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();

        if(source == quitButton)
        {
            System.exit(0);
        }
        else if(source == diceButton) // player has rolled the dice
        {
            DICE.rollDice();
            ROLL = DICE.getTotalRoll();

            if(!PLAYERS[CURRENT_PLAYER].isFree(this)) // player is in jail this turn
            {
                JailButtonsPanel.setVisible(false);
                if(DICE.doubles()) // player rolled a doubles
                {
                    MESSAGE_BOX.printMessage("You got lucky. :) You are now free to move. " +
                            "Roll the dice baby!!");
                    PLAYERS[CURRENT_PLAYER].freePlayer();
                    DICE.resetDice();
                } else{ // player did not roll doubles
                    MESSAGE_BOX.printMessage("Better luck next time!");
                    PLAYERS[CURRENT_PLAYER].incrementTurnsInJail();
                    resetTurn();
                }
            } else{ // player is not currently in jail
                if(DICE.doubles())
                    DOUBLES_COUNT++;
                if(DOUBLES_COUNT == 3 && PLAYERS[CURRENT_PLAYER].getDebt() == 0)
                    resetTurn();
                else{
                    MESSAGE_BOX.printMessage("Player " + (CURRENT_PLAYER + 1) + " has rolled " + ROLL + ", " +
                            "this player will move " + ROLL + " squares.");
                    PLAYERS[CURRENT_PLAYER].movePlayer(ROLL);
                    step(THE_BOARD, PLAYERS[CURRENT_PLAYER], CURRENT_PLAYER);
                    if(PLAYERS[CURRENT_PLAYER].passedGo()) // player passed go
                    {
                        MESSAGE_BOX.printMessage("Collect 200 bulldog bucks!");
                        PLAYERS[CURRENT_PLAYER].payPlayer(200);
                    }

                    Property currentSquare = THE_BOARD.getSquare(PLAYERS[CURRENT_PLAYER].currentSquare());
                    if(currentSquare.isForSale()) // property is actual property and not just a square
                    {
                        if(!currentSquare.isOwned()) // unowned property
                        {
                            DiceButtonPanel.setVisible(false);
                            PostRollButtonsPanel.setVisible(true);
                            MESSAGE_BOX.printMessage("What would you like to do?");
                        } else{ // owned property
                            if(CURRENT_PLAYER == currentSquare.getOwnerNum()) // current player owns the property
                            {
                                MESSAGE_BOX.printMessage("You have landed on one of your own properties. Isn't life" +
                                        " sweet when you're rich? :)");
                                resetTurn();
                            } else{ // other player owns the property
                                if(currentSquare.isUtility() && !currentSquare.isMortgaged()) // utility property
                                {
                                    currentSquare.updateRent(DICE);
                                }

                                if(currentSquare.isMortgaged()) // mortgaged property
                                {
                                    MESSAGE_BOX.printMessage("It's your lucky day! You don't have to pay the rent " +
                                            "for this property because the owner has mortgaged it.");
                                } else{ // active property
                                    MESSAGE_BOX.printMessage("This property is owned by player " +
                                            (currentSquare.getOwnerNum() + 1)
                                            + ": you owe this player " + currentSquare.getRent() + " bulldog bucks.");
                                    currentSquare.payOwnerRent();
                                }

                                if(PLAYERS[CURRENT_PLAYER].getBalance() >= currentSquare.getRent()) // can pay rent
                                {
                                    PLAYERS[CURRENT_PLAYER].finePlayer(currentSquare.getRent());
                                    MESSAGE_BOX.printMessage("You have paid the owner of " + currentSquare.getName() + " "
                                            + currentSquare.getRent() + " bulldog bucks.");
                                    resetTurn();
                                }
                                else{ // cannot pay rent
                                    MESSAGE_BOX.printMessage("You do not have enough money to pay the owner. " +
                                            "Mortgage property to pay or you will be eliminated from the game.");
                                    int remainder = currentSquare.getRent() - PLAYERS[CURRENT_PLAYER].getBalance();
                                    revealEliminationButtons(remainder);
                                }
                            }
                        }
                    } else{ // property cannot be bought
                        if(currentSquare.isCardSquare()) // card square
                        {
                            if(currentSquare.getNumber() == 2 || currentSquare.getNumber() == 17 ||
                                    currentSquare.getNumber() == 33) // community square
                            {
                                MESSAGE_BOX.printMessage("You have landed on Community Service!" +
                                        " You will now receive a Community Service Card.");
                                currentSquare.getServiceCard(PLAYERS[CURRENT_PLAYER], THE_BOARD, this, CURRENT_PLAYER, DICE, GAME_SETS, MESSAGE_BOX);
                            } else{ // chance square
                                MESSAGE_BOX.printMessage("You have landed on Chance! You will now receive a Chance Card.");
                                currentSquare.getChanceCard(PLAYERS[CURRENT_PLAYER], THE_BOARD, this, CURRENT_PLAYER, DICE, GAME_SETS, MESSAGE_BOX);
                            }
                        }
                        else if(currentSquare.isTaxSquare()) // tax square
                        {
                            int tax = 0;
                            if(currentSquare.getNumber() == 4) // tuition
                            {
                                MESSAGE_BOX.printMessage("Time for that tuition bill! Pay 200 bulldog bucks.");
                                tax = 200;
                            }
                            else{ // donation
                                MESSAGE_BOX.printMessage("Mandatory donation. Pay 75 bulldog bucks.");
                                tax = 75;
                            }
                            // now determine if player can pay tax
                            if(PLAYERS[CURRENT_PLAYER].getBalance() >= tax) // player can pay tax
                            {
                                MESSAGE_BOX.printMessage("You have successfully paid the fine.");
                                THE_BOARD.cashPotIn(PLAYERS[CURRENT_PLAYER], tax);
                                resetTurn();
                            } else{ // player can't pay tax
                                MESSAGE_BOX.printMessage("You do not have enough money in your account to pay" +
                                        " the fine. Please mortgage property to pay or you will be " +
                                        "eliminated.");
                                int remainder = tax - PLAYERS[CURRENT_PLAYER].getBalance();
                                revealEliminationButtons(remainder);
                            }
                        }
                        else if(currentSquare.isCampoSquare()) // just visiting
                        {
                            MESSAGE_BOX.printMessage("You are just visiting Campo, so you will not be imprisoned. :)");
                            resetTurn();
                        }
                        else if(currentSquare.isGoToCampoSquare()) // go to campo
                        {
                            MESSAGE_BOX.printMessage("Go to Campo. Do not pass Go, do not collect 200 bulldog bucks.");
                            PLAYERS[CURRENT_PLAYER].imprisonPlayer();
                            PLAYERS[CURRENT_PLAYER].relocatePlayer(10);
                            step(THE_BOARD, PLAYERS[CURRENT_PLAYER], CURRENT_PLAYER);
                            DICE.resetDice();
                            resetTurn();
                        }
                        else if(currentSquare.getNumber() == 20) // free parking
                        {
                            MESSAGE_BOX.printMessage("You have landed on Free Parking!! " +
                                    "You will be paid a sum of " + THE_BOARD.getCommunityPot() +
                                    " bulldog bucks!!!");
                            THE_BOARD.cashPotOut(PLAYERS[CURRENT_PLAYER]);
                            resetTurn();
                        }
                        else{ // Go
                            MESSAGE_BOX.printMessage("You have gotten all the way back to Go!!!");
                            resetTurn();
                        }
                    }
                }
            }
        }
        else if(source == statsButton) // display stats
        {
            PLAYERS[CURRENT_PLAYER].displayStats(MESSAGE_BOX, this);
        }
        else if(source == buyButton) // player chose to buy the property
        {
            Property currentSquare = THE_BOARD.getSquare(PLAYERS[CURRENT_PLAYER].currentSquare());
            //PLAYERS[CURRENT_PLAYER].displayStats(MESSAGE_BOX);
            if(PLAYERS[CURRENT_PLAYER].getBalance() < currentSquare.getPrice())
            {
                MESSAGE_BOX.printMessage("Would you like to mortgage property in order" +
                        " to raise funds for this property? If not, click the 'pass property'" +
                        " button. If so, click on mortgage.");
            } else{
                currentSquare.addOwner(PLAYERS[CURRENT_PLAYER], DICE, GAME_SETS, MESSAGE_BOX);
                resetTurn();
            }
        }
        else if(source == passButton) // player skipped the property
        {
            MESSAGE_BOX.printMessage("You have chosen to pass this property. It will remain unowned for now.");
            resetTurn();
        }

        else if(source == stayButton) // player chooses to remain in jail
        {
            MESSAGE_BOX.printMessage("You have chosen to remain in Campo for this turn.");
            PLAYERS[CURRENT_PLAYER].incrementTurnsInJail();
            JailButtonsPanel.setVisible(false);
            resetTurn();
        }

        else if(source == bailoutButton) // player tries to pay their way out of jail
        {
            if(PLAYERS[CURRENT_PLAYER].getBalance() >= 50) // player has enough money
            {
                MESSAGE_BOX.printMessage("You have paid 50 bulldog bucks to get out of Campo. " +
                        "You can now move again. Roll the dice!");
                THE_BOARD.cashPotIn(PLAYERS[CURRENT_PLAYER], 50);
                PLAYERS[CURRENT_PLAYER].freePlayer();
                JailButtonsPanel.setVisible(false);
            } else{ // player doesn't have enough money
                MESSAGE_BOX.printMessage("You do not have enough money in your account to bail yourself" +
                        "out. Please choose another option.");
            }
        }

        else if(source == getOutOfJailFreeButton) // play get out of campo free card
        {
            if(PLAYERS[CURRENT_PLAYER].hasGetOutOfCampoFree()) // player has a get out of campo free card
            {
                MESSAGE_BOX.printMessage("You have played 'Get out of Campo Free', you are now free to move again!" +
                        "Roll the dice!");
                PLAYERS[CURRENT_PLAYER].getOutOfCampoFree();
                JailButtonsPanel.setVisible(false);
            } else{
                MESSAGE_BOX.printMessage("You do not have a 'Get out of Campo Free' card. " +
                        "Please choose another option");
            }
        }

        else if(source == addHouseButton) // add house
        {
            if(PLAYERS[CURRENT_PLAYER].ownsNoSets()) // player does not own a set yet
            {
                MESSAGE_BOX.printMessage("You do not yet own any sets. Come back later to add a house.");
            } else{
                MESSAGE_BOX.printMessage("Select the set you wish to build a house in.");
                PostTurnButtonsPanel.setVisible(false);
                SelectSetPanel.setVisible(true);
            }
        }

        else if(source == selectSetButton[0]) // player selects purple set
        {
            CURRENT_SET = GAME_SETS.getSet(0);
            if(PLAYERS[CURRENT_PLAYER].hasSet(CURRENT_SET)) // player owns the set they selected
            {
                SelectSetPanel.setVisible(false);
                SelectPropertyPanel.setVisible(true);
                MESSAGE_BOX.printMessage("You have chosen to add a house to a property in the" +
                        " PURPLE set. Select which property you want to build a house on.");
            } else{ // player does not own this particular set
                MESSAGE_BOX.printMessage("You do not own this set. Please select another set that you" +
                        "do own.");
            }
        }

        else if(source == selectSetButton[1]) // player selects light blue set
        {
            CURRENT_SET = GAME_SETS.getSet(1);
            if(PLAYERS[CURRENT_PLAYER].hasSet(CURRENT_SET)) // player owns the set they selected
            {
                SelectSetPanel.setVisible(false);
                SelectPropertyPanel.setVisible(true);
                MESSAGE_BOX.printMessage("You have chosen to add a house to a property in the" +
                        " LIGHT BLUE set. Select which property you want to build a house on.");
            } else{ // player does not own this particular set
                MESSAGE_BOX.printMessage("You do not own this set. Please select another set that you" +
                        "do own.");
            }
        }

        else if(source == selectSetButton[2]) // player selects pink sets
        {
            CURRENT_SET = GAME_SETS.getSet(2);
            if(PLAYERS[CURRENT_PLAYER].hasSet(CURRENT_SET)) // player owns the set they selected
            {
                SelectSetPanel.setVisible(false);
                SelectPropertyPanel.setVisible(true);
                MESSAGE_BOX.printMessage("You have chosen to add a house to a property in the" +
                        " PINK set. Select which property you want to build a house on.");
            } else{ // player does not own this particular set
                MESSAGE_BOX.printMessage("You do not own this set. Please select another set that you" +
                        "do own.");
            }
        }

        else if(source == selectSetButton[3]) // player selects orange set
        {
            CURRENT_SET = GAME_SETS.getSet(3);
            if(PLAYERS[CURRENT_PLAYER].hasSet(CURRENT_SET)) // player owns the set they selected
            {
                SelectSetPanel.setVisible(false);
                SelectPropertyPanel.setVisible(true);
                MESSAGE_BOX.printMessage("You have chosen to add a house to a property in the" +
                        " ORANGE set. Select which property you want to build a house on.");
            } else{ // player does not own this particular set
                MESSAGE_BOX.printMessage("You do not own this set. Please select another set that you" +
                        "do own.");
            }
        }

        else if(source == selectSetButton[4]) // player selects red set
        {
            CURRENT_SET = GAME_SETS.getSet(4);
            if(PLAYERS[CURRENT_PLAYER].hasSet(CURRENT_SET)) // player owns the set they selected
            {
                SelectSetPanel.setVisible(false);
                SelectPropertyPanel.setVisible(true);
                MESSAGE_BOX.printMessage("You have chosen to add a house to a property in the" +
                        " RED set. Select which property you want to build a house on.");
            } else{ // player does not own this particular set
                MESSAGE_BOX.printMessage("You do not own this set. Please select another set that you" +
                        "do own.");
            }
        }

        else if(source == selectSetButton[5]) // player selects yellow set
        {
            CURRENT_SET = GAME_SETS.getSet(5);
            if(PLAYERS[CURRENT_PLAYER].hasSet(CURRENT_SET)) // player owns the set they selected
            {
                SelectSetPanel.setVisible(false);
                SelectPropertyPanel.setVisible(true);
                MESSAGE_BOX.printMessage("You have chosen to add a house to a property in the" +
                        " YELLOW set. Select which property you want to build a house on.");
            } else{ // player does not own this particular set
                MESSAGE_BOX.printMessage("You do not own this set. Please select another set that you" +
                        "do own.");
            }
        }

        else if(source == selectSetButton[6]) // player selects green set
        {
            CURRENT_SET = GAME_SETS.getSet(6);
            if(PLAYERS[CURRENT_PLAYER].hasSet(CURRENT_SET)) // player owns the set they selected
            {
                SelectSetPanel.setVisible(false);
                SelectPropertyPanel.setVisible(true);
                MESSAGE_BOX.printMessage("You have chosen to add a house to a property in the" +
                        " GREEN set. Select which property you want to build a house on.");
            } else{ // player does not own this particular set
                MESSAGE_BOX.printMessage("You do not own this set. Please select another set that you" +
                        "do own.");
            }
        }

        else if(source == selectSetButton[7]) // player selects dark blue set
        {
            CURRENT_SET = GAME_SETS.getSet(7);
            if(PLAYERS[CURRENT_PLAYER].hasSet(CURRENT_SET)) // player owns the set they selected
            {
                SelectSetPanel.setVisible(false);
                SelectPropertyPanel.setVisible(true);
                MESSAGE_BOX.printMessage("You have chosen to add a house to a property in the" +
                        " DARK BLUE set. Select which property you want to build a house on.");
            } else{ // player does not own this particular set
                MESSAGE_BOX.printMessage("You do not own this set. Please select another set that you" +
                        "do own.");
            }
        }

        else if(source == selectPropertyButton[0] || source == selectPropertyButton[1]) // property 1 or 2
        {
            if(source == selectPropertyButton[0]) // first property
            {
                CURRENT_PROPERTY = CURRENT_SET.getProperty(0);
            } else{ // second property
                CURRENT_PROPERTY = CURRENT_SET.getProperty(1);
            }

            MESSAGE_BOX.printMessage("You have chosen to add a house on the property " + CURRENT_PROPERTY.getName());

            if(PLAYERS[CURRENT_PLAYER].getBalance() >= CURRENT_PROPERTY.getHousePrice()) // player has money for a house
            {
                doAddHouse();
            } else{ // player doesn't have money for a house
                MESSAGE_BOX.printMessage("You do not have sufficient funds for a house here. " +
                        "Please select another property or end your turn.");
                SelectPropertyPanel.setVisible(false);
                resetToPostTurn();
            }
        }

        else if(source == selectPropertyButton[2]) // player selects third property in a set
        {
            if(CURRENT_SET.isTwoSet()) // current set does not have a third property
            {
                MESSAGE_BOX.printMessage("This set only has two properties. Please select Property 1 or " +
                        "Property 2.");
            } else{ // set has a third property
                CURRENT_PROPERTY = CURRENT_SET.getProperty(2);
                MESSAGE_BOX.printMessage("You have chosen to add a house to " + CURRENT_PROPERTY.getName());

                if(PLAYERS[CURRENT_PLAYER].getBalance() > CURRENT_PROPERTY.getHousePrice()) // player has money for a house
                {
                    doAddHouse();
                } else{ // player doesn't have money for a house
                    MESSAGE_BOX.printMessage("You do not have sufficient funds for a house here. " +
                            "Please select another property or end your turn.");
                    SelectPropertyPanel.setVisible(false);
                    resetToPostTurn();
                }
            }
        }

        else if(source == addHotelButton) // add hotel
        {
            PLAYERS[CURRENT_PLAYER].finePlayer(CURRENT_PROPERTY.getHousePrice());
            revealHotel();
            CURRENT_PROPERTY.addHotel(MESSAGE_BOX);
            CURRENT_PROPERTY.updateRent(DICE);
            PLAYERS[CURRENT_PLAYER].buyHotel();
            MESSAGE_BOX.printMessage("You have successfully built a hotel on " + CURRENT_PROPERTY.getName() +
                    "!");

            HotelButtonPanel.setVisible(false);
            resetToPostTurn();
        }

        else if(source == declineHotelButton) // decline hotel
        {
            MESSAGE_BOX.printMessage("You have chosen to not add a hotel on this property for now." +
                    "It will remain without one for now.");
            HotelButtonPanel.setVisible(false);
            resetToPostTurn();
        }

        else if(source == mortgageButton) // mortgage a property
        {
            MESSAGE_BOX.printMessage("Select the property that you wish to mortgage" +
                        " or un-mortgage." +
                    " Each mortgage is worth half of that property's original price.");
            MortgageGUI MortgageMenu = new MortgageGUI(PLAYERS[CURRENT_PLAYER], MESSAGE_BOX, this, GAME_SETS, DICE);
            setVisible(false);
        }

        else if(source == tradeButton)
        {
            MESSAGE_BOX.printMessage("You have decided to trade property with another player. Choose which player you " +
                    "want to trade with first.");
            TradeGUI TradeMenu = new TradeGUI(PLAYERS, CURRENT_PLAYER, MESSAGE_BOX, this, GAME_SETS, DICE);
            setVisible(false);
        }

        else if(source == endTurnButton) // end turn
        {
            do{
                if(CURRENT_PLAYER == (PLAYERS.length - 1)) // wrap around to player 1
                {
                    CURRENT_PLAYER = 0;
                } else{ // increment to next player
                    CURRENT_PLAYER++;
                }
            } while(PLAYERS[CURRENT_PLAYER].isEliminated()); // if the current player is eliminated keep cycling through


            MESSAGE_BOX.printMessage("It is now player " + (CURRENT_PLAYER + 1) + "'s turn.");

            if(!PLAYERS[CURRENT_PLAYER].isFree(this)) // next player is in jail
            {
                MESSAGE_BOX.printMessage("You are in Campo. Roll doubles, pay 50 bulldog bucks, remain in Campo " +
                        "for this turn, or play a Get Out of Campo Free card.");
                JailButtonsPanel.setVisible(true);
            }
            PostTurnButtonsPanel.setVisible(false);
            DiceButtonPanel.setVisible(true);
        }

        else if(source == payDebtButton) // player pays debt
        {
            PLAYERS[CURRENT_PLAYER].reduceDebt();

            if(PLAYERS[CURRENT_PLAYER].hasDebt()) // player has not fully paid off debt
            {
                MESSAGE_BOX.printMessage("If you still have more property to mortgage " +
                        "then continue to mortgage to pay off your debt. Otherwise, click " +
                        "forfeit, because you have lost.");
            } else{ // player has successfully paid off debt
                MESSAGE_BOX.printMessage("You have survived...for now.");
                ForfeitButtonPanel.setVisible(false);
                resetTurn();
            }
        }

        else if(source == forfeitButton) // player is eliminated
        {
            MESSAGE_BOX.printMessage("You have resigned.");
            playerIcons[CURRENT_PLAYER].setVisible(false);      // remove player icon from the board
            PLAYERS[CURRENT_PLAYER].eliminatePlayer(this);          // set all properties and sets to be unowned & set eliminated flag

            if(CURRENT_PLAYER == (PLAYERS.length - 1))
            {
                CURRENT_PLAYER = 0;
            } else{
                CURRENT_PLAYER++;
            }

            // check to see if the game is over
            if(checkForWinner())
            {
                int indexOfWinner = 0;
                while(PLAYERS[indexOfWinner].isEliminated())
                {
                    indexOfWinner++;
                }
                WinScreen PlayerWinsScreen = new WinScreen(indexOfWinner); // roll credits

                MESSAGE_BOX.setVisible(false);
                setVisible(false);
            } else{
                DICE.resetDice();
                MESSAGE_BOX.printMessage("It is now player " + (PLAYERS[CURRENT_PLAYER].getPlayerNum() + 1) + "'s turn.");

                ForfeitButtonPanel.setVisible(false);
                PostTurnButtonsPanel.setVisible(false);
                DiceButtonPanel.setVisible(true);
            }
        }
    }

    /**
     * step relocates the player icon on the board
     * @param gameBoard is the virtual game board containing all properties and squares
     * @param currentPlayer is the player who is rolling
     * @param currentPiece is the number of the player's piece
     */
    public void step(Board gameBoard, Player currentPlayer, int currentPiece)
    {
        gamePieces[currentPiece].move(gameBoard, currentPlayer);
        playerIcons[currentPiece].setLocation(gamePieces[currentPiece].getX(), gamePieces[currentPiece].getY());
        if(!currentPlayer.isFree(this))
        {
            playerIcons[currentPiece].setLocation(gamePieces[currentPiece].getJailX(), gamePieces[currentPiece].getJailY());
        }
    }

    /**
     * revealEliminationButtons reveals the ForfeitButtonPanel when the player cannot pay back
     * a certain debt
     */
    public void revealEliminationButtons(int remainder)
    {
        PLAYERS[CURRENT_PLAYER].finePlayer(PLAYERS[CURRENT_PLAYER].getBalance());
        PLAYERS[CURRENT_PLAYER].setDebt(remainder);
        DiceButtonPanel.setVisible(false);
        ForfeitButtonPanel.setVisible(true);
    }

    /**
     * resetTurn resets the dice and switches the current player if the current player didn't roll doubles
     */
    public void resetTurn()
    {
        DiceButtonPanel.setVisible(false);
        PostRollButtonsPanel.setVisible(false);
        if(DICE.doubles() && PLAYERS[CURRENT_PLAYER].isFree(this)) // player rolled doubles AND is free
        {
            if(DOUBLES_COUNT < 3) // player has not yet rolled doubles 3 times in a row
            {
                MESSAGE_BOX.printMessage("You rolled a doubles, so you will roll again!");
                //DOUBLES_COUNT++;
                DiceButtonPanel.setVisible(true);
            } else{ // player has rolled 3 doubles in a row
                MESSAGE_BOX.printMessage("You are getting too lucky. Go to Campo.");
                PLAYERS[CURRENT_PLAYER].imprisonPlayer();
                PLAYERS[CURRENT_PLAYER].relocatePlayer(10);
                step(THE_BOARD, PLAYERS[CURRENT_PLAYER], CURRENT_PLAYER);
                PostTurnButtonsPanel.setVisible(true);
                DOUBLES_COUNT = 0;
            }
        } else{ // player did not roll doubles or is in jail
            MESSAGE_BOX.printMessage("You are done rolling for this turn, what would you like to do?");
            PostTurnButtonsPanel.setVisible(true);
            DOUBLES_COUNT = 0;
        }
        DICE.resetDice();
    }

    /**
     * revealPostRollButtons brings up the post roll buttons and hides the roll button
     */
    public void revealPostRollButtons()
    {
        DiceButtonPanel.setVisible(false);
        PostRollButtonsPanel.setVisible(true);
    }

    /**
     * hideHouse hides a house that is revealed on the board
     * @param currentProperty is the property where the house is being removed from
     */
    public void hideHouse(Property currentProperty)
    {
        try{
            int houseNumber = currentProperty.getSetNumber(GAME_SETS); // retrieve set
            if(houseNumber == 0 || houseNumber == 7) // two set
            {
                if(currentProperty.getNumber() == 37 || currentProperty.getNumber() == 39) // dark blue
                    houseNumber = 80;
                // otherwise purple

                houseNumber += (currentProperty.getNumHouses() - 1);
                if(currentProperty.getNumber() == 3 || currentProperty.getNumber() == 39)
                {
                    houseNumber += 4;
                }
            }
            else if(houseNumber > 0 && houseNumber < 7) // three set
            {
                if(houseNumber == 1) // light blue
                    houseNumber = 8;
                else if(houseNumber == 2) // pink
                    houseNumber = 20;
                else if(houseNumber == 3) // orange
                    houseNumber = 32;
                else if(houseNumber == 4) // red
                    houseNumber = 44;
                else if(houseNumber == 5) // yellow
                    houseNumber = 56;
                else{ // green
                    houseNumber = 68;
                }

                houseNumber += (currentProperty.getNumHouses() - 1); // adjust to correct house

                // adjust to correct property
                if(currentProperty.getNumber() == 8 || currentProperty.getNumber() == 13
                        || currentProperty.getNumber() == 18 || currentProperty.getNumber() == 23
                        || currentProperty.getNumber() == 27 || currentProperty.getNumber() == 32)
                {
                    houseNumber += 4;
                }
                else if(currentProperty.getNumber() == 9 || currentProperty.getNumber() == 14
                        || currentProperty.getNumber() == 19 || currentProperty.getNumber() == 24
                        || currentProperty.getNumber() == 29 || currentProperty.getNumber() == 34)
                {
                    houseNumber += 8;
                }
            }
            HousePanel[houseNumber].setVisible(false);
        }catch(SetNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * hideHotel makes a hotel image disappear
     * @param currentProperty is the property where the hotel is being removed
     */
    public void hideHotel(Property currentProperty)
    {
        int hotelNumber = currentProperty.getNumber();

        // locate correct property with switch statement
        switch(hotelNumber)
        {
            case 1:
                hotelNumber = 0;
                break;
            case 3:
                hotelNumber = 1;
                break;
            case 6:
                hotelNumber = 2;
                break;
            case 8:
                hotelNumber = 3;
                break;
            case 9:
                hotelNumber = 4;
                break;
            case 11:
                hotelNumber = 5;
                break;
            case 13:
                hotelNumber = 6;
                break;
            case 14:
                hotelNumber = 7;
                break;
            case 16:
                hotelNumber = 8;
                break;
            case 18:
                hotelNumber = 9;
                break;
            case 19:
                hotelNumber = 10;
                break;
            case 21:
                hotelNumber = 11;
                break;
            case 23:
                hotelNumber = 12;
                break;
            case 24:
                hotelNumber = 13;
                break;
            case 26:
                hotelNumber = 14;
                break;
            case 27:
                hotelNumber = 15;
                break;
            case 29:
                hotelNumber = 16;
                break;
            case 31:
                hotelNumber = 17;
                break;
            case 32:
                hotelNumber = 18;
                break;
            case 34:
                hotelNumber = 19;
                break;
            case 37:
                hotelNumber = 20;
                break;
            case 39:
                hotelNumber = 21;
                break;
        }

        HotelPanel[hotelNumber].setVisible(false);
    }

    /**
     * resetToPostTurn brings back the PostTurnButtonsPanel
     */
    private void resetToPostTurn()
    {
        PostTurnButtonsPanel.setVisible(true);
        MESSAGE_BOX.printMessage("What would you like to do?");
    }

    /**
     * doAddHouse performs the act of adding a house to a property
     */
    private void doAddHouse()
    {
        if(CURRENT_PROPERTY.isMortgaged()) // don't add house if the property is mortgaged
        {
            MESSAGE_BOX.printMessage("This property is mortgaged and therefore houses cannot be built on it.");
            SelectPropertyPanel.setVisible(false);
            resetToPostTurn();
        } else{ // if not mortgaged, go ahead
            if(CURRENT_PROPERTY.getNumHouses() == 4) // houses are maxed on this property
            {
                if(CURRENT_PROPERTY.hasHotel()) // property has a hotel
                {
                    MESSAGE_BOX.printMessage("This property already has a hotel, and is therefore" +
                            " at maximum rent. Choose another property or end your turn.");
                    SelectPropertyPanel.setVisible(false);
                    resetToPostTurn();
                } else{ // property doesn't have a hotel
                    MESSAGE_BOX.printMessage("This property already has the maximum number of houses.");
                    MESSAGE_BOX.printMessage("Would you like to add a hotel?");
                    SelectPropertyPanel.setVisible(false);
                    HotelButtonPanel.setVisible(true);
                }
            } else{ // houses aren't yet maxed on this property
                PLAYERS[CURRENT_PLAYER].finePlayer(CURRENT_PROPERTY.getHousePrice());
                revealHouse();
                CURRENT_PROPERTY.addHouse(MESSAGE_BOX);
                PLAYERS[CURRENT_PLAYER].buyHouse();
                CURRENT_PROPERTY.updateRent(DICE);

                MESSAGE_BOX.printMessage("You have successfully built a new home on " + CURRENT_PROPERTY.getName());
                SelectPropertyPanel.setVisible(false);
                resetToPostTurn();
            }
        }

    }

    /**
     * revealHouse reveals a house that was bought on the board
     */
    private void revealHouse()
    {
        try{
            int houseNumber = CURRENT_PROPERTY.getSetNumber(GAME_SETS); // retrieve set
            if(houseNumber == 0 || houseNumber == 7)
            {
                if(CURRENT_PROPERTY.getNumber() == 37 || CURRENT_PROPERTY.getNumber() == 39) // dark blue
                    houseNumber = 80;
                // otherwise purple

                houseNumber += CURRENT_PROPERTY.getNumHouses();
                if(CURRENT_PROPERTY.getNumber() == 3 || CURRENT_PROPERTY.getNumber() == 39)
                {
                    houseNumber += 4;
                }
            }
            else if(houseNumber > 0 && houseNumber < 7) // three set
            {
                if(houseNumber == 1) // light blue
                    houseNumber = 8;
                else if(houseNumber == 2) // pink
                    houseNumber = 20;
                else if(houseNumber == 3) // orange
                    houseNumber = 32;
                else if(houseNumber == 4) // red
                    houseNumber = 44;
                else if(houseNumber == 5) // yellow
                    houseNumber = 56;
                else{ // green
                    houseNumber = 68;
                }

                houseNumber += CURRENT_PROPERTY.getNumHouses(); // adjust to correct house

                // adjust to correct property
                if(CURRENT_PROPERTY.getNumber() == 8 || CURRENT_PROPERTY.getNumber() == 13
                        || CURRENT_PROPERTY.getNumber() == 18 || CURRENT_PROPERTY.getNumber() == 23
                        || CURRENT_PROPERTY.getNumber() == 27 || CURRENT_PROPERTY.getNumber() == 32)
                {
                    houseNumber += 4;
                }
                else if(CURRENT_PROPERTY.getNumber() == 9 || CURRENT_PROPERTY.getNumber() == 14
                        || CURRENT_PROPERTY.getNumber() == 19 || CURRENT_PROPERTY.getNumber() == 24
                        || CURRENT_PROPERTY.getNumber() == 29 || CURRENT_PROPERTY.getNumber() == 34)
                {
                    houseNumber += 8;
                }
            }
            HousePanel[houseNumber].setVisible(true);
        }catch(SetNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * revealHotel reveals a hotel image on the board
     */
    private void revealHotel()
    {
        int hotelNumber = CURRENT_PROPERTY.getNumber();

        // locate correct property with switch statement
        switch(hotelNumber)
        {
            case 1:
                hotelNumber = 0;
                break;
            case 3:
                hotelNumber = 1;
                break;
            case 6:
                hotelNumber = 2;
                break;
            case 8:
                hotelNumber = 3;
                break;
            case 9:
                hotelNumber = 4;
                break;
            case 11:
                hotelNumber = 5;
                break;
            case 13:
                hotelNumber = 6;
                break;
            case 14:
                hotelNumber = 7;
                break;
            case 16:
                hotelNumber = 8;
                break;
            case 18:
                hotelNumber = 9;
                break;
            case 19:
                hotelNumber = 10;
                break;
            case 21:
                hotelNumber = 11;
                break;
            case 23:
                hotelNumber = 12;
                break;
            case 24:
                hotelNumber = 13;
                break;
            case 26:
                hotelNumber = 14;
                break;
            case 27:
                hotelNumber = 15;
                break;
            case 29:
                hotelNumber = 16;
                break;
            case 31:
                hotelNumber = 17;
                break;
            case 32:
                hotelNumber = 18;
                break;
            case 34:
                hotelNumber = 19;
                break;
            case 37:
                hotelNumber = 20;
                break;
            case 39:
                hotelNumber = 21;
                break;
        }

        HotelPanel[hotelNumber].setVisible(true);
    }

    /**
     * initPlayers initializes the players in the game
     */
    private void initPlayers()
    {
        PLAYERS = new Player[gamePieces.length];
        for(int i = 0; i < PLAYERS.length; i++)
        {
            PLAYERS[i] = new Player(i, MESSAGE_BOX);
        }
    }

    /**
     * addIcons adds the player icons to the board
     * @param numberOfPlayers
     */
    private void addIcons(int numberOfPlayers)
    {
        gamePieces = new PlayerIcon[numberOfPlayers];
        playerIcons = new ImagePanel[numberOfPlayers];
        for(int i = 0; i < numberOfPlayers; i++)
        {
            gamePieces[i] = new PlayerIcon(i);
            playerIcons[i] = new ImagePanel(gamePieces[i].getImage(i));
            playerIcons[i].setLocation(gamePieces[i].getX(), gamePieces[i].getY());
            playerIcons[i].setSize(21, 25);
            background.add(playerIcons[i]);
        }
    }

    /**
     * addHouseImages adds the house images to the background
     */
    private void addHouseImages()
    {
        HousePanel = new ImagePanel[88];
        int xShift = 0;
        int yShift = 0;

        for(int h = 0; h < HousePanel.length; h++)
        {
            HousePanel[h] = new ImagePanel(houseImage);
            HousePanel[h].setSize(12, 12);
            HousePanel[h].setLocation((538 + xShift), (598 + yShift));
            HousePanel[h].setVisible(false);
            background.add(HousePanel[h]);

            if(h < 19) // bottom row
            {
                if(h == 3 || h == 11)
                {
                    xShift -= 164;
                }
                if(h == 7)
                {
                    xShift -= 219;
                }
                if(h == 15)
                {
                    xShift -= 108;
                }
                xShift += 13;
            }
            if(h == 19) // move to left column
            {
                xShift -= 60;
                yShift -= 62;
            }
            if(h > 19 && h < 43) // left column
            {
                if(h == 23 || h == 31 || h == 35)
                {
                    yShift -= 164;
                }
                if(h == 27 || h == 39)
                {
                    yShift -= 108;
                }
                yShift += 13;
            }
            if(h == 43) // move to top row
            {
                xShift += 22;
                yShift -= 55;
            }
            if(h > 43 && h < 67) // top row
            {
                if(h == 47 || h == 55 || h == 63)
                {
                    xShift += 60;
                }
                if(h == 51 || h == 59)
                {
                    xShift += 4;
                }
                xShift += 13;
            }
            if(h == 67) // move to right column
            {
                xShift += 21;
                yShift += 16;
            }
            if(h > 67)
            {
                if(h == 71)
                {
                    yShift += 3;
                }
                if(h == 75 || h == 83)
                {
                    yShift += 61;
                }
                if(h == 79)
                {
                    yShift += 115;
                }
                yShift += 13;
            }
        }
    }

    /**
     * addHotelImages adds the hotel images to the background
     */
    private void addHotelImages()
    {
        HotelPanel = new ImagePanel[22];
        int xShift = 0;
        int yShift = 0;

        for(int h = 0; h < HotelPanel.length; h++) //  loop through and place each hotel
        {
            HotelPanel[h] = new ImagePanel(hotelImage);
            HotelPanel[h].setSize(50, 18);
            HotelPanel[h].setLocation((539 + xShift), (579 + yShift));
            HotelPanel[h].setVisible(false);
            background.add(HotelPanel[h]);

            if(h == 0 || h == 2)
            {
                xShift -= 111;
            }
            if(h == 1)
            {
                xShift -= 168;
            }
            if(h == 3)
            {
                xShift -= 57;
            }
            if(h == 4 || h == 10)
            {
                yShift -= 30;
            }
            if(h == 6 || h == 9)
            {
                yShift -= 50;
            }
            if(h == 5 || h == 7 || h == 8)
            {
                yShift -= 112;
            }
            if(h == 11 || h == 13 || h == 15)
            {
                xShift += 111;
            }
            if(h == 12 || h == 14)
            {
                xShift += 58;
            }
            if(h == 16)
            {
                yShift += 30;
            }
            if(h == 17)
            {
                yShift += 50;
            }
            if(h == 18 || h == 20)
            {
                yShift += 111;
            }
            if(h == 19)
            {
                yShift += 164;
            }
        }
    }

    /**
     * addButtons adds the buttons to the game board for the player to use
     */
    private void addButtons()
    {
        DiceButtonPanel.setSize(170, 80);
        diceButton.setLocation(265,  500);
        diceButton.setPreferredSize(new Dimension(70, 50));
        DiceButtonPanel.setBackground(Color.black);
        DiceButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        DiceButtonPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        DiceButtonPanel.setLayout(new BoxLayout(DiceButtonPanel, BoxLayout.PAGE_AXIS));
        DiceButtonPanel.add(diceButton);
        DiceButtonPanel.add(Box.createVerticalGlue());
        DiceButtonPanel.setLocation(0, 679);
        diceButton.addActionListener(this);

        PostRollButtonsPanel.setSize(170, 80);
        passButton.setLocation(265,  500);
        passButton.setPreferredSize(new Dimension(70, 50));
        buyButton.setPreferredSize(new Dimension(70, 50));
        PostRollButtonsPanel.setBackground(Color.black);
        PostRollButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        PostRollButtonsPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        PostRollButtonsPanel.setLayout(new BoxLayout(PostRollButtonsPanel, BoxLayout.PAGE_AXIS));
        PostRollButtonsPanel.add(buyButton);
        PostRollButtonsPanel.add(passButton);
        PostRollButtonsPanel.add(Box.createVerticalGlue());
        PostRollButtonsPanel.setLocation(0, 679);
        buyButton.addActionListener(this);
        passButton.addActionListener(this);
        PostRollButtonsPanel.setVisible(false);

        PostTurnButtonsPanel.setSize(170, 80);
        endTurnButton.setLocation(265,  500);
        endTurnButton.setPreferredSize(new Dimension(70, 50));
        addHouseButton.setPreferredSize(new Dimension(70, 50));
        PostTurnButtonsPanel.setBackground(Color.black);
        PostTurnButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        PostTurnButtonsPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        PostTurnButtonsPanel.setLayout(new BoxLayout(PostTurnButtonsPanel, BoxLayout.PAGE_AXIS));
        PostTurnButtonsPanel.add(addHouseButton);
        PostTurnButtonsPanel.add(endTurnButton);
        PostTurnButtonsPanel.add(Box.createVerticalGlue());
        PostTurnButtonsPanel.setLocation(0, 679);
        addHouseButton.addActionListener(this);
        endTurnButton.addActionListener(this);
        PostTurnButtonsPanel.setVisible(false);

        JailButtonsPanel.setSize(100, 90);
        stayButton.setLocation(265,  500);
        stayButton.setPreferredSize(new Dimension(700, 30));
        bailoutButton.setPreferredSize(new Dimension(70, 30));
        getOutOfJailFreeButton.setPreferredSize(new Dimension(70, 30));
        JailButtonsPanel.setBackground(Color.RED);
        JailButtonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JailButtonsPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        JailButtonsPanel.setLayout(new BoxLayout(JailButtonsPanel, BoxLayout.PAGE_AXIS));
        JailButtonsPanel.add(stayButton);
        JailButtonsPanel.add(bailoutButton);
        JailButtonsPanel.add(getOutOfJailFreeButton);
        JailButtonsPanel.add(Box.createVerticalGlue());
        JailButtonsPanel.setLocation(220, 180);
        stayButton.addActionListener(this);
        bailoutButton.addActionListener(this);
        getOutOfJailFreeButton.addActionListener(this);
        JailButtonsPanel.setVisible(false);

        SelectSetPanel.setSize(40, 160);
        SelectSetPanel.setBackground(Color.black);
        SelectSetPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        SelectSetPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        SelectSetPanel.setLayout(new BoxLayout(SelectSetPanel, BoxLayout.PAGE_AXIS));
        for(int s = 0; s < selectSetButton.length; s++) // initialize every button
        {
            selectSetButton[s] = new JButton("Set " + (s + 1));
            selectSetButton[s].setPreferredSize(new Dimension(20, 20));
            SelectSetPanel.add(selectSetButton[s]);
            selectSetButton[s].addActionListener(this);
        }
        SelectSetPanel.add(Box.createVerticalGlue());
        SelectSetPanel.setLocation(550, 300);
        SelectSetPanel.setVisible(false);

        SelectPropertyPanel.setSize(80, 160);
        SelectPropertyPanel.setBackground(Color.BLUE);
        SelectPropertyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        SelectPropertyPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        SelectPropertyPanel.setLayout(new BoxLayout(SelectPropertyPanel, BoxLayout.PAGE_AXIS));
        for(int s = 0; s < selectPropertyButton.length; s++) // initialize every button
        {
            selectPropertyButton[s] = new JButton("Property " + (s + 1));
            selectPropertyButton[s].setPreferredSize(new Dimension(80, 60));
            SelectPropertyPanel.add(selectPropertyButton[s]);
            selectPropertyButton[s].addActionListener(this);
        }
        SelectPropertyPanel.add(Box.createVerticalGlue());
        SelectPropertyPanel.setLocation(510, 88);
        SelectPropertyPanel.setVisible(false);

        HotelButtonPanel.setSize(100, 100);
        addHotelButton.setLocation(265,  500);
        addHotelButton.setPreferredSize(new Dimension(70, 50));
        declineHotelButton.setPreferredSize(new Dimension(70, 50));
        HotelButtonPanel.setBackground(Color.RED);
        HotelButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        HotelButtonPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        HotelButtonPanel.setLayout(new BoxLayout(HotelButtonPanel, BoxLayout.PAGE_AXIS));
        HotelButtonPanel.add(addHotelButton);
        HotelButtonPanel.add(declineHotelButton);
        HotelButtonPanel.add(Box.createVerticalGlue());
        HotelButtonPanel.setLocation(300, 180);
        addHotelButton.addActionListener(this);
        declineHotelButton.addActionListener(this);
        HotelButtonPanel.setVisible(false);

        ForfeitButtonPanel.setSize(170, 80);
        forfeitButton.setLocation(265,  500);
        forfeitButton.setPreferredSize(new Dimension(90, 50));
        payDebtButton.setPreferredSize(new Dimension(90, 50));
        ForfeitButtonPanel.setBackground(Color.RED);
        ForfeitButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ForfeitButtonPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        ForfeitButtonPanel.setLayout(new BoxLayout(ForfeitButtonPanel, BoxLayout.PAGE_AXIS));
        ForfeitButtonPanel.add(forfeitButton);
        ForfeitButtonPanel.add(payDebtButton);
        ForfeitButtonPanel.add(Box.createVerticalGlue());
        ForfeitButtonPanel.setLocation(0, 679);
        forfeitButton.addActionListener(this);
        payDebtButton.addActionListener(this);
        ForfeitButtonPanel.setVisible(false);

        // buttons below here should be visible at all times

        MortgagePanel.setSize(170, 80);
        mortgageButton.setLocation(265,  500);
        mortgageButton.setPreferredSize(new Dimension(90, 50));
        tradeButton.setPreferredSize(new Dimension(90, 50));
        MortgagePanel.setBackground(Color.yellow);
        MortgagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        MortgagePanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        MortgagePanel.setLayout(new BoxLayout(MortgagePanel, BoxLayout.PAGE_AXIS));
        MortgagePanel.add(mortgageButton);
        MortgagePanel.add(tradeButton);
        MortgagePanel.add(Box.createVerticalGlue());
        MortgagePanel.setLocation(170, 679);
        mortgageButton.addActionListener(this);
        tradeButton.addActionListener(this);

        StatsButtonPanel.setSize(170, 80);
        statsButton.setLocation(265,  500);
        statsButton.setPreferredSize(new Dimension(90, 50));
        StatsButtonPanel.setBackground(Color.BLUE);
        StatsButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        StatsButtonPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        StatsButtonPanel.setLayout(new BoxLayout(StatsButtonPanel, BoxLayout.PAGE_AXIS));
        StatsButtonPanel.add(statsButton);
        StatsButtonPanel.add(Box.createVerticalGlue());
        StatsButtonPanel.setLocation(340, 679);
        statsButton.addActionListener(this);

        QuitButtonPanel.setSize(170, 80);
        quitButton.setLocation(265,  500);
        quitButton.setPreferredSize(new Dimension(70, 50));
        QuitButtonPanel.setBackground(Color.RED);
        QuitButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        QuitButtonPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        QuitButtonPanel.setLayout(new BoxLayout(QuitButtonPanel, BoxLayout.PAGE_AXIS));
        QuitButtonPanel.add(quitButton);
        QuitButtonPanel.add(Box.createVerticalGlue());
        QuitButtonPanel.setLocation(510, 679);
        quitButton.addActionListener(this);

        background.add(DiceButtonPanel);
        background.add(PostRollButtonsPanel);
        background.add(PostTurnButtonsPanel);
        background.add(JailButtonsPanel);
        background.add(SelectSetPanel);
        background.add(SelectPropertyPanel);
        background.add(HotelButtonPanel);
        background.add(MortgagePanel);
        background.add(StatsButtonPanel);
        background.add(ForfeitButtonPanel);
        background.add(QuitButtonPanel);
    }

    /**
     * startGame creates the game's message box
     * @param numberOfPlayers is the number of players in the game
     */
    private void startGame(int numberOfPlayers)
    {
        MESSAGE_BOX.printMessage("Welcome to Zagopoly!!!");
        MESSAGE_BOX.printMessage("Rolling the dice to see who goes first...");
        setFirstPlayer(numberOfPlayers);
        MESSAGE_BOX.printMessage("The player who will go first is player " + (CURRENT_PLAYER + 1) + ".");
        MESSAGE_BOX.printMessage("Player " + (CURRENT_PLAYER + 1) + ", please roll the dice by clicking the 'roll' button" +
                " to begin the game.");
    }

    /**
     * setFirstPlayer randomly decides which player will go first using the dice
     * @param numberOfPlayers is the number of players in the game
     */
    private void setFirstPlayer(int numberOfPlayers)
    {
        int[] initialRolls = new int[numberOfPlayers];

        for(int i = 0; i < numberOfPlayers; i++)
        {
            DICE.rollDice();
            initialRolls[i] = DICE.getTotalRoll();
            DICE.resetDice();
        }
        // now find the index of the largest roll
        int indexOfLargest = 0;
        for(int i = 0; i < initialRolls.length; i++)
        {
            if(initialRolls[i] > initialRolls[indexOfLargest])
                indexOfLargest = i;
        }
        // set the player
        CURRENT_PLAYER = indexOfLargest;
    }

    /**
     * checkForWinner checks to see if someone has won the game
     * @return true if someone has won
     */
    private boolean checkForWinner()
    {
        int inCount = PLAYERS.length;
        for(int p = 0; p < PLAYERS.length; p++)
        {
            if(PLAYERS[p].isEliminated()) // check for eliminated player
            {
                inCount--; // if eliminated, reduce the count of players who are in the game
            }
        }

        // now check to see if there is only one player remaining
        if(inCount == 1)
        {
            return true;
        } else{
            return false;
        }
    }
}
