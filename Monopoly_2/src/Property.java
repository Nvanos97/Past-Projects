/**
 * Class Property creates a property square
 */

import java.util.Scanner;

public class Property
{
    protected int PROPERTY_NUMBER;
    protected int PRICE;
    protected int RENT;
    protected int START_RENT;
    protected int XPosition;
    protected int YPosition;
    protected boolean IS_MORTGAGED;
    protected boolean IS_FOR_SALE;
    protected String NAME;
    protected Player OWNER;

    /**
     * constructor for class Property
     * @param propertyNum is the property's position on the board
     * @param propertyPrice is the price of purchasing the property
     * @param propertyRent is the rent that must be paid on that property to the property's owner
     *                     by other players who land there
     * @param XCoord is the property's X coordinate on the game board
     * @param YCoord is the property's Y coordinate on the game board
     */
    public Property(int propertyNum, int propertyPrice, int propertyRent, int XCoord, int YCoord)
    {
        this.PROPERTY_NUMBER = propertyNum;
        this.PRICE = propertyPrice;
        this.RENT = propertyRent;
        this.START_RENT = propertyRent;
        this.XPosition = XCoord;
        this.YPosition = YCoord;
        this.IS_MORTGAGED = false;
        this.IS_FOR_SALE = false;
        this.NAME = "Free Parking";
    }

    /**
     * updateRent changes the property's rent
     * @param theDice is the game dice
     */
    public void updateRent(ZagopolyDice theDice)
    {
        System.out.println("Can't update the rent of Free Parking because there is no rent here.");
    }

    /**
     * addOwner makes a player the owner of this property
     * @param newOwner is the new owner of the property
     * @param theDice is the game dice
     * @param gameSets are the sets in the game
     * @param TextWindow is the GUI that displays the game messages
     */
    public void addOwner(Player newOwner, ZagopolyDice theDice, The_Sets gameSets, ZagopolyTextWindow TextWindow)
    {
        if(newOwner.getBalance() < PRICE)
        { // case that the player does not have the money for this property
            TextWindow.printMessage("You have insufficient funds to buy this property.");
        } else{ // case that the player does have the money for this property
            newOwner.finePlayer(PRICE);
            this.OWNER = newOwner;
            newOwner.takeOwnershipOfProperty(this);
            if(isUtility()) // case that the current property is a utility
            {
                newOwner.addUtility();
            }
            else if(isRestaurant()) // case that the property is a restaurant
            {
                newOwner.addRestaurant();
                newOwner.updateRestaurantRents(theDice);
            } else{ // case that the property is a primary property (and is therefore in a set)
                try
                {
                    int currentSetNumber = getSetNumber(gameSets);
                    Set currentSet = gameSets.getSet(currentSetNumber);
                    currentSet.addOwner(newOwner, TextWindow);
                    newOwner.takeOwnerShipOfSet(currentSet);
                    currentSet.updateRents(theDice);
                }catch(SetNotFoundException S)
                {
                    S.printStackTrace();
                }
            }
            TextWindow.printMessage("You now own this property! Other players who land here " +
                    "will now have to pay you rent.");
        }
    }

    /**
     * addOwnerFree gives this property to a player for free
     * @param newOwner is the new owner
     */
    public void addOwnerFree(Player newOwner)
    {
        OWNER = newOwner;
    }

    /**
     * removeOwner takes the property away from the owner's ownership
     */
    public void removeOwner()
    {
        this.OWNER = null;
    }

    /**
     * addHouse prints the message that tells the player they cannot put a house on this property
     * @param TextWindow is the GUI that displays the game messages
     */
    public void addHouse(ZagopolyTextWindow TextWindow)
    {
        System.out.println("You cannot add a house to a property that does not belong to a set.");
    }

    /**
     * addHotel prints a message that tells the player they cannot put a hotel on this property
     * @param TextWindow is the GUI that displays the game messages
     */
    public void addHotel(ZagopolyTextWindow TextWindow)
    {
        System.out.println("You cannot add a hotel to a property that does not belong to a set.");
    }

    /**
     * removeHouse prints a message that tells the player they can't remove a house from this property
     * @param TextWindow is the GUI that displays the game messages
     */
    public void removeHouse(ZagopolyTextWindow TextWindow)
    {
        TextWindow.printMessage("You cannot remove a house from a property that doesn't belong to a set.");
    }

    /**
     * removeHotel prints a message that tells the player they can't remove a hotel from this property
     * @param TextWindow is the GUI that displays the game messages
     */
    public void removeHotel(ZagopolyTextWindow TextWindow)
    {
        TextWindow.printMessage("You cannot remove a hotel from a property that doesn't belong to a set.");
    }

    /**
     * payOwnerRent pays the owner of the property the amount that the rent is worth
     */
    public void payOwnerRent()
    {
        OWNER.payPlayer(RENT);
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
        System.out.println("Player will not receive a service card because this is not a service square.");
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
        System.out.println("Player will not receive a chance card because this is not a chance square.");
    }

    /**
     * resetRent resets the property rent to it's starting rent
     */
    public void resetRent()
    {
        RENT = START_RENT;
    }

    /**
     * reduceRent reduces the property's rent
     */
    public void reduceRent()
    {
        if(RENT != START_RENT)
            RENT = RENT/2;
    }

    /**
     * mortgage mortgages the property
     * @param TextWindow is the GUI that displays the game messages
     */
    public void mortgage(ZagopolyTextWindow TextWindow)
    {
       RENT = 0;
       IS_MORTGAGED = true;
       OWNER.payPlayer((PRICE/2));
       TextWindow.printMessage(NAME + " is now mortgaged. Players who land here will no longer have to pay" +
               " rent until the mortgage is paid off.");
    }

    /**
     * unMortgage pays off the mortgage owed to the property
     * @param TextWindow is the GUI that displays the game messages
     */
    public void unMortgage(ZagopolyTextWindow TextWindow)
    {
        if(OWNER.getBalance() >= (PRICE/2)) // the owner can pay off this mortgage
        {
            RENT = START_RENT;
            IS_MORTGAGED = false;
            OWNER.finePlayer(PRICE/2);
            TextWindow.printMessage(NAME + " is no longer mortgaged. Players who land here will now be " +
                    "required to pay rent again.");
        } else{ // the owner cannot pay off this mortgage
            TextWindow.printMessage("You do not have the money to pay off this mortgage.");
        }
    }

    /**
     * unMortgageFree unmortgages a property without charge
     * @param TextWindow is the GUI that displays game info to the user
     */
    public void unMortgageFree(ZagopolyTextWindow TextWindow)
    {
        RENT = START_RENT;
        IS_MORTGAGED = false;
    }

    /**
     * displayPropertyInfo displays all of the property's key information
     * @param TextWindow is the GUI that displays the game messages
     */
    public void displayPropertyInfo(ZagopolyTextWindow TextWindow)
    {
        TextWindow.printMessage("Property Name: " + this.NAME);
        TextWindow.printMessage("Property Price: " + this.PRICE);
        TextWindow.printMessage("Property Rent: " + this.RENT);
        if(isOwned()) // owned
            TextWindow.printMessage("Property Owner: Player " + (this.OWNER.getPlayerNum() + 1));
        else // unowned
            TextWindow.printMessage("Property is unowned.");
        if(isMortgaged()) // mortgaged
            TextWindow.printMessage("This property is currently mortgaged, so it's owner" +
                    " cannot receive rent.");
    }

    /**
     * getNumber returns the position of the property on the board
     * @return PROPERTY_NUMBER
     */
    public int getNumber()
    {
        return this.PROPERTY_NUMBER;
    }

    /**
     * getPrice() returns the price for buying the property
     * @return PRICE
     */
    public int getPrice()
    {
        return this.PRICE;
    }

    /**
     * getRent returns the property's current rent
     * @return RENT
     */
    public int getRent()
    {
        return this.RENT;
    }

    /**
     * getHousePrice returns no value
     * @return 0
     */
    public int getHousePrice()
    {
        return 0;
    }

    /**
     * getSetNumber finds out which set this property is in
     * @return
     */
    public int getSetNumber(The_Sets Sets) throws SetNotFoundException
    {
        throw new SetNotFoundException("This is not the right type of property to be in a set.");
    }

    /**
     * getX returns the property's x coordinate on the GUI
     * @return XPosition
     */
    public int getX()
    {
        return XPosition;
    }

    /**
     * getY returns the property's y coordinate on the GUI
     * @return YPosition
     */
    public int getY()
    {
        return YPosition;
    }

    /**
     * getNumHouses doesn't return anything because this property can't have houses
     * @return 0
     */
    public int getNumHouses()
    {
        return 0;
    }

    /**
     * getOwnerNum gets the player number of the owner
     * @return OWNER.getPlayerNum
     */
    public int getOwnerNum()
    {
        return OWNER.getPlayerNum();
    }


    /**
     * isOwned checks if this property is owned
     * @return true if OWNER is not null
     */
    public boolean isOwned()
    {
        if(this.OWNER == null)
            return false;
        return true;
    }

    /**
     * rentIsDoubled checks to see if the current rent is doubled
     * @return true if the RENT = 2*START_RENT
     */
    public boolean rentIsNotDoubled()
    {
        if(RENT == 2*START_RENT)
        {
            return false;
        }
        return true;
    }

    /**
     * isMortgaged checks to see if the property is mortgaged
     * @return true if the property is mortgaged
     */
    public boolean isMortgaged()
    {
        return IS_MORTGAGED;
    }

    /**
     * isForSale determines if the property can be bought
     * @return true if the property can be bought
     */
    public boolean isForSale()
    {
        if(IS_FOR_SALE)
            return true;
        return false;
    }

    /**
     * isPrimary checks to see if the current sqaure is a primary property
     * @return
     */
    public boolean isPrimary()
    {
        return false;
    }

    /**
     * isTaxSquare determines if the current property is one of two tax squares
     * @return
     */
    public boolean isTaxSquare()
    {
        return false;
    }

    /**
     * isCardSquare determines if the current square is a card sqaure
     * @return false
     */
    public boolean isCardSquare()
    {
        return false;
    }

    /**
     * isGoToCampoSquare checks to see if the current property is the Go To Campo square
     * @return false
     */
    public boolean isGoToCampoSquare()
    {
        return false;
    }

    /**
     * isCampoSquare checks to see if the current square is the campo square
     * @return false
     */
    public boolean isCampoSquare()
    {
        return false;
    }

    /**
     * isUtility checks if the current property is a utility
     * @return false
     */
    public boolean isUtility()
    {
        return false;
    }

    /**
     * isRestaurant checks to see if the current property is a restaurant
     * @return false
     */
    public boolean isRestaurant()
    {
        return false;
    }

    /**
     * hasHotel checks to see if the current property has a hotel
     * @return false
     */
    public boolean hasHotel()
    {
        return false;
    }

    /**
     * getName gets the name of the property
     * @return NAME, the property name
     */
    public String getName()
    {
        return NAME;
    }

    /**
     * getOwner returns the owner of this property
     * @return OWNER
     */
    public Player getOwner()
    {
        return OWNER;
    }
}
