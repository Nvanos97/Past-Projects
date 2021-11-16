/**
 * Class RestaurantProperty is a child class of class property that creates a Restaurant Square
 */

public class RestaurantProperty extends Property
{
    /**
     * constructor for class RestaurantSquare
     * @param propertyNumber is the position of the restaurant square on the board
     * @param name is the name of the property
     * @param XCoord is the restaurant property's X coordinate on the game board
     * @param YCoord is the restaurant property's Y coordinate on the game board
     */
    public RestaurantProperty(int propertyNumber, String name, int XCoord, int YCoord)
    {
        super(propertyNumber, 200, 50, XCoord, YCoord);
        this.IS_FOR_SALE = true;
        this.NAME = name;
    }

    /**
     * updateRent overrides updateRent of Property and changes the property's rent
     * @param theDice is the game dice
     */
    public void updateRent(ZagopolyDice theDice)
    {
        if(OWNER.getNumberOfRestaurants() == 2)
        {
            RENT = 50;
        }
        else if(OWNER.getNumberOfRestaurants() == 3)
        {
            RENT = 100;
        }
        else if(OWNER.getNumberOfRestaurants() == 4)
        {
            RENT = 200;
        } else{
            RENT = 25;
        }
    }

    /**
     * displayPropertyInfo overrides displayPropertyInfo of class property
     * @param TextWindow is the GUI that displays the game messages
     */
    public void displayPropertyInfo(ZagopolyTextWindow TextWindow)
    {
        TextWindow.printMessage("Property Name: " + this.NAME);
        TextWindow.printMessage("Property Price: " + this.PRICE);
        TextWindow.printMessage("Property Rent: " + this.RENT);
        TextWindow.printMessage("Property Set: RESTAURANTS");
        if(isOwned())
            TextWindow.printMessage("Property Owner: Player " + (this.OWNER.getPlayerNum() + 1));
        else
            TextWindow.printMessage("Property is unowned.");
        if(isMortgaged())
            TextWindow.printMessage("This property is currently mortgaged, so it's owner " +
                    "cannot receive rent.");
    }

    /**
     * isRestaurant checks to see if the current square is a restaurant square
     * @return true
     */
    public boolean isRestaurant()
    {
        return true;
    }
}
