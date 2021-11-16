/**
 * Class SpecialCardSquare is a child class of class Property that creates a special card square
 */

public class SpecialCardSquare extends Property
{
    /**
     * constructor for class SpecialCardSquare
     * @param squareNum is the position of the square on the board
     * @param name is the name of the special card square
     * @param XCoord  is the card square's X coordinate on the game board
     * @param YCoord  is the card square's Y coordinate on the game board
     */
    public SpecialCardSquare(int squareNum, String name, int XCoord, int YCoord)
    {
        super(squareNum, 0, 0, XCoord, YCoord);
        this.NAME = name;
    }

    /**
     * getServiceCard gives the current player a service card
     * @param currentPlayer is the player who landed on the square
     * @param gameBoard is the game board
     * @param gui is the user interface that displays the board
     * @param currentPiece is the piece of the current player
     * @param theDice is the game dice
     * @param gameSets are the sets in the game
     * @param TextWindow is the GUI that displays the game messages
     */
    public void getServiceCard(Player currentPlayer, Board gameBoard, GameBoardUI gui, int currentPiece, ZagopolyDice theDice, The_Sets gameSets, ZagopolyTextWindow TextWindow)
    {
        int CardNumber = (int) (Math.random() * 15 + 1);

        switch (CardNumber) {
            case 1:
                //CardNumber = 1;
                TextWindow.printMessage("Buy girl scout cookies in Hemmingson, pay 15 bulldog bucks.");
                if(currentPlayer.getBalance() < 15)
                {
                    TextWindow.printMessage("You do not have the money to pay this fine." +
                            "Mortgage property to pay or you will be eliminated.");
                    int remainder = 15 - currentPlayer.getBalance();
                    gui.revealEliminationButtons(remainder);
                } else{
                    gameBoard.cashPotIn(currentPlayer, 15);
                    gui.resetTurn();
                }
                break;

            case 2:
                //CardNumber = 2;
                TextWindow.printMessage("Buy your tickets for the spring formal, pay 50 bulldog bucks.");
                if(currentPlayer.getBalance() < 50)
                {
                    TextWindow.printMessage("You do not have the money to pay this fine." +
                            "Mortgage property to pay or you will be eliminated.");
                    int remainder = 50 - currentPlayer.getBalance();
                    gui.revealEliminationButtons(remainder);
                } else{
                    gameBoard.cashPotIn(currentPlayer, 50);
                    gui.resetTurn();
                }
                break;

            case 3:
                //CardNumber = 3;
                TextWindow.printMessage("Sell some extra clothes on ebay, earn 100 bulldog bucks.");
                currentPlayer.payPlayer(100);
                gui.resetTurn();
                break;

            case 4:
                //CardNumber = 4;
                TextWindow.printMessage("Advance straight to Go!");
                currentPlayer.relocatePlayer(0);
                currentPlayer.payPlayer(200);
                gui.step(gameBoard, currentPlayer, currentPiece);
                gui.resetTurn();
                break;

            case 5:
                //CardNumber = 5;
                TextWindow.printMessage("Get caught speeding, pay 100 bulldog bucks.");
                if(currentPlayer.getBalance() < 100)
                {
                    TextWindow.printMessage("You do not have the money to pay this fine." +
                            "Mortgage property to pay or you will be eliminated.");
                    int remainder = 100 - currentPlayer.getBalance();
                    gui.revealEliminationButtons(remainder);
                } else{
                    gameBoard.cashPotIn(currentPlayer, 100);
                    gui.resetTurn();
                }
                break;

            case 6:
                //CardNumber = 6;
                TextWindow.printMessage("Get written up for a noise violation, go straight to Campus Security.");
                currentPlayer.imprisonPlayer();
                currentPlayer.relocatePlayer(10);
                gui.step(gameBoard, currentPlayer, currentPiece);
                gui.resetTurn();
                break;

            case 7:
                //CardNumber = 7;
                TextWindow.printMessage("Find a 50 bulldog buck bill on the ground outside of your dorm, earn 50 bulldog bucks.");
                currentPlayer.payPlayer(50);
                gui.resetTurn();
                break;

            case 8:
                //CardNumber = 8;
                TextWindow.printMessage("Go out to a sushi dinner date, pay 60 bulldog bucks.");
                if(currentPlayer.getBalance() < 60)
                {
                    TextWindow.printMessage("You do not have the money to pay this fine." +
                            "Mortgage property to pay or you will be eliminated.");
                    int remainder = 60 - currentPlayer.getBalance();
                    gui.revealEliminationButtons(remainder);
                } else{
                    gameBoard.cashPotIn(currentPlayer, 60);
                    gui.resetTurn();
                }
                break;

            case 9:
                //CardNumber = 9;
                TextWindow.printMessage("Hamburgers at Late Night, go straight to Hemmingson.");
                currentPlayer.relocatePlayer(34);
                gui.step(gameBoard, currentPlayer, currentPiece);
                if(gameBoard.getSquare(34).isOwned())
                {
                    payRelocationRent(gameBoard, currentPlayer, 34, TextWindow, gui);
                } else{
                    TextWindow.printMessage("Property is currently unowned. Would you like to buy it?");
                    gui.revealPostRollButtons();
                }
                break;

            case 10:
                //CardNumber = 10;
                TextWindow.printMessage("Friend invited you over to their apartment, go straight to Dussault.");
                currentPlayer.relocatePlayer(31);
                gui.step(gameBoard, currentPlayer, currentPiece);
                if(gameBoard.getSquare(31).isOwned())
                {
                    payRelocationRent(gameBoard, currentPlayer, 31, TextWindow, gui);
                } else{
                    TextWindow.printMessage("Property is currently unowned. Would you like to buy it?");
                    gui.revealPostRollButtons();
                }
                break;

            case 11:
                //CardNumber = 11;
                TextWindow.printMessage("Lose your room key, pay 50 bulldog bucks.");
                if(currentPlayer.getBalance() < 50)
                {
                    TextWindow.printMessage("You do not have the money to pay this fine." +
                            "Mortgage property to pay or you will be eliminated.");
                    int remainder = 50 - currentPlayer.getBalance();
                    gui.revealEliminationButtons(remainder);
                } else{
                    gameBoard.cashPotIn(currentPlayer, 50);
                    gui.resetTurn();
                }
                break;

            case 12:
                //CardNumber = 12;
                TextWindow.printMessage("Property Taxes are due, Pay 50 bulldog bucks for each house owned " +
                        "and 75 for each hotel");
                int houseCost = (currentPlayer.getNumberOfHouses() * 50);
                int hotelCost = (currentPlayer.getNumberOfHotels() * 75);
                if(currentPlayer.getBalance() < (hotelCost + houseCost))
                {
                    TextWindow.printMessage("You do not have the money to pay this fine." +
                            "Mortgage property to pay or you will be eliminated.");
                    int remainder = (hotelCost + houseCost) - currentPlayer.getBalance();
                    gui.revealEliminationButtons(remainder);
                } else{
                    gameBoard.cashPotIn(currentPlayer, (hotelCost + houseCost));
                    gui.resetTurn();
                }
                break;

            case 13:
                //CardNumber = 13;
                TextWindow.printMessage("Family member sends you money for getting good grades, earn 25 bulldog bucks");
                currentPlayer.payPlayer(25);
                gui.resetTurn();
                break;

            case 14:
                //CardNumber = 14;
                TextWindow.printMessage("'Get out of Campo' Free card!!! If you play this card on any given turn while " +
                        "in Campo, you will automatically be freed. This card will be returned to the deck when you " +
                        "play it.");
                currentPlayer.giveGetOutOfCampoFreeCard();
                gui.resetTurn();
                break;

            case 15:
                //CardNumber = 15;
                TextWindow.printMessage("Go on a tour of the new athletic building, go straight to Volkar.");
                currentPlayer.relocatePlayer(39);
                gui.step(gameBoard, currentPlayer, currentPiece);
                if(gameBoard.getSquare(39).isOwned())
                {
                    payRelocationRent(gameBoard, currentPlayer, 39, TextWindow, gui);
                } else{
                    TextWindow.printMessage("Property is currently unowned. Would you like to buy it?");
                    gui.revealPostRollButtons();
                }
                break;
        }
    }

    /**
     * getChanceCard gives the current player a chance
     * @param currentPlayer is the player who landed on the square
     * @param gameBoard is the game board
     * @param gui is the user interface that displays the board
     * @param currentPiece is the piece of the current player
     * @param theDice is the game dice
     * @param gameSets are the sets in the game
     * @param TextWindow is the GUI that displays the game messages
     */
    public void getChanceCard(Player currentPlayer, Board gameBoard, GameBoardUI gui, int currentPiece, ZagopolyDice theDice, The_Sets gameSets, ZagopolyTextWindow TextWindow)
    {
        int CardNumber = (int)(Math.random() * 15 + 1);

        switch(CardNumber) {
            case 1: //CardNumber = 1;
                TextWindow.printMessage("Advance Straight to Go!");
                currentPlayer.relocatePlayer(0);
                currentPlayer.payPlayer(200);
                gui.step(gameBoard, currentPlayer, currentPiece);
                gui.resetTurn();
                break;

            case 2: //CardNumber = 2;
                TextWindow.printMessage("Pulling an all nighter, take a trip to Starbucks.");
                currentPlayer.relocatePlayer(25);
                gui.step(gameBoard, currentPlayer, currentPiece);
                if(gameBoard.getSquare(25).isOwned())
                {
                    payRelocationRent(gameBoard, currentPlayer, 25, TextWindow, gui);
                } else{
                    TextWindow.printMessage("Property is currently unowned. Would you like to buy it?");
                    gui.revealPostRollButtons();
                }
                break;

            case 3: //CardNumber = 3;
                TextWindow.printMessage("Time for a Zags Game, Advance to McCarthy.");
                currentPlayer.relocatePlayer(37);
                gui.step(gameBoard, currentPlayer, currentPiece);
                if(gameBoard.getSquare(37).isOwned())
                {
                    payRelocationRent(gameBoard, currentPlayer, 37, TextWindow, gui);
                } else{
                    TextWindow.printMessage("Property is currently unowned. Would you like to buy it?");
                    gui.revealPostRollButtons();
                }
                break;

            case 4: //CardNumber = 4;
                TextWindow.printMessage("Room searched by RAs, Go straight to Campus Security.");
                currentPlayer.imprisonPlayer();
                currentPlayer.relocatePlayer(10);
                gui.step(gameBoard, currentPlayer, currentPiece);
                gui.resetTurn();
                break;

            case 5: //CardNumber = 5;
                TextWindow.printMessage("Parking Violation, pay 50 bulldog bucks.");
                if(currentPlayer.getBalance() < 50)
                {
                    TextWindow.printMessage("You do not have the money to pay this fine." +
                            "Mortgage property to pay or you will be eliminated.");
                    int remainder = 50 - currentPlayer.getBalance();
                    gui.revealEliminationButtons(remainder);
                } else{
                    gameBoard.cashPotIn(currentPlayer, 50);
                    gui.resetTurn();
                }
                break;

            case 6: //CardNumber = 6;
                TextWindow.printMessage("Win the biggest Zags Fan Competition, earn 75 bulldog bucks.");
                currentPlayer.payPlayer(75);
                gui.resetTurn();
                break;

            case 7: //CardNumber = 7;
                TextWindow.printMessage("Time for Biology, Go straight to Hughes.");
                currentPlayer.relocatePlayer(21);
                gui.step(gameBoard, currentPlayer, currentPiece);
                if(gameBoard.getSquare(21).isOwned())
                {
                    payRelocationRent(gameBoard, currentPlayer, 21, TextWindow, gui);
                } else{
                    TextWindow.printMessage("Property is currently unowned. Would you like to buy it?");
                    gui.revealPostRollButtons();
                }
                break;

            case 8: //CardNumber = 8;
                TextWindow.printMessage("Property Improvement Time, Pay 25 bulldog bucks " +
                        " for each house owned and 40 for each hotel.");
                int houseCost = (currentPlayer.getNumberOfHouses() * 25);
                int hotelCost = (currentPlayer.getNumberOfHotels() * 40);
                if(currentPlayer.getBalance() < (houseCost + hotelCost))
                {
                    TextWindow.printMessage("You do not have the money to pay this fine." +
                            "Mortgage property to pay or you will be eliminated.");
                    int remainder = (hotelCost + houseCost) - currentPlayer.getBalance();
                    gui.revealEliminationButtons(remainder);
                } else{
                    gameBoard.cashPotIn(currentPlayer, (houseCost + hotelCost));
                    gui.resetTurn();
                }
                break;

            case 9: //CardNumber = 9;
                TextWindow.printMessage("Zags are in the final four, pay 30 bulldog bucks to get some new gear.");
                if(currentPlayer.getBalance() < 30)
                {
                    TextWindow.printMessage("You do not have the money to pay this fine." +
                            "Mortgage property to pay or you will be eliminated.");
                    int remainder = 30 - currentPlayer.getBalance();
                    gui.revealEliminationButtons(remainder);
                } else{
                    gameBoard.cashPotIn(currentPlayer, 30);
                    gui.resetTurn();
                }
                break;

            case 10: //CardNumber = 10;
                TextWindow.printMessage("Win a karaoke competition at late night, earn 25 bulldog bucks.");
                currentPlayer.payPlayer(25);
                gui.resetTurn();
                break;

            case 11: //CardNumber = 11;
                TextWindow.printMessage("Time for philosophy, go straight to College Hall.");
                currentPlayer.relocatePlayer(24);
                gui.step(gameBoard, currentPlayer, currentPiece);
                if(gameBoard.getSquare(24).isOwned())
                {
                    payRelocationRent(gameBoard, currentPlayer, 24, TextWindow, gui);
                } else{
                    TextWindow.printMessage("Property is currently unowned. Would you like to buy it?");
                    gui.revealPostRollButtons();
                }
                break;

            case 12: //CardNumber = 12;
                TextWindow.printMessage("'Get out of Campo' Free card!!! If you play this card on any given turn while " +
                        "in Campo, you will automatically be freed. This card will be returned to the deck when you " +
                        "play it.");
                currentPlayer.giveGetOutOfCampoFreeCard();
                gui.resetTurn();
                break;

            case 13: //CardNumber = 13;
                TextWindow.printMessage("Found a nice parking spot, go to Free Parking.");
                if(gameBoard.getCommunityPot() > 0)
                {
                    TextWindow.printMessage("You will receive " + gameBoard.getCommunityPot() + " bulldog bucks!!");
                }
                currentPlayer.relocatePlayer(20);
                gameBoard.cashPotOut(currentPlayer);
                gui.step(gameBoard, currentPlayer, currentPiece);
                gui.resetTurn();
                break;

            case 14: //CardNumber = 14;
                TextWindow.printMessage("Get some pre basketball game 'supplies' for friends, pay 25 bulldog bucks.");
                if(currentPlayer.getBalance() < 25)
                {
                    TextWindow.printMessage("You do not have the money to pay this fine." +
                            "Mortgage property to pay or you will be eliminated.");
                    int remainder = 25 - currentPlayer.getBalance();
                    gui.revealEliminationButtons(remainder);
                } else{
                    gameBoard.cashPotIn(currentPlayer, 25);
                    gui.resetTurn();
                }
                break;

            case 15: //CardNumber = 15;
                TextWindow.printMessage("You lost your zag card, go straight to Hemmingson to check lost and found.");
                currentPlayer.relocatePlayer(34);
                gui.step(gameBoard, currentPlayer, currentPiece);
                if(gameBoard.getSquare(34).isOwned())
                {
                    payRelocationRent(gameBoard, currentPlayer, 34, TextWindow, gui);
                } else{
                    TextWindow.printMessage("Property is currently unowned. Would you like to buy it?");
                    gui.revealPostRollButtons();
                }
                break;
        }
    }

    /**
     * isCardSquare determines if the current square is a card square
     * @return true
     */
    public boolean isCardSquare()
    {
        return true;
    }

    /**
     * payRelocationRent has the current player pay rent at the square he is moved to
     * @param gameBoard is the game board
     * @param currentPlayer is the current player
     * @param squareNum is the number of the square that the player is moved to
     * @param TextWindow is the GUI that displays the game messages
     */
    private void payRelocationRent(Board gameBoard, Player currentPlayer, int squareNum, ZagopolyTextWindow TextWindow, GameBoardUI gui)
    {
        if(currentPlayer == gameBoard.getSquare(squareNum).getOwner()) // this is one of your properties
        {
            TextWindow.printMessage("You have landed on one of your own properties. Isn't life sweet " +
                    "when you're rich? :)");
            gui.resetTurn();
        } else{ // this is owned by another player
            gameBoard.getSquare(squareNum).payOwnerRent();
            if (currentPlayer.getBalance() < gameBoard.getSquare(squareNum).getRent()) // player cannot pay the rent
            {
                TextWindow.printMessage("You do not have enough money in your account to pay " +
                        "the owner of this property the desired rent. Mortgage property or " +
                        "you will be eliminated.");
                int remainder = gameBoard.getSquare(squareNum).getRent() - currentPlayer.getBalance();
                gui.revealEliminationButtons(remainder);
                // drains the current player's account
                //currentPlayer.eliminatePlayer();
            } else {  // case that the player can pay the rent
                currentPlayer.finePlayer(gameBoard.getSquare(squareNum).getRent());
                TextWindow.printMessage("You have paid Player " + (gameBoard.getSquare(squareNum).getOwnerNum() + 1) +
                        " " + gameBoard.getSquare(squareNum).getRent() + " bulldog bucks.");
                gui.resetTurn();
            }
        }
    }
}