/**
 * Class UtilityProperty is a child class of class Property that creates a utility square
 */

public class UtilityProperty extends Property
{
    /**
     * constructor for class UtilityProperty
     * @param propertyNum is the property's position on the board
     * @param name is the name of the property
     * @param XCoord  is the utility's X coordinate on the game board
     * @param YCoord  is the utility's Y coordinate on the game board
     */
    public UtilityProperty(int propertyNum, String name, int XCoord, int YCoord)
    {
        super(propertyNum, 150, 1, XCoord, YCoord);
        this.IS_FOR_SALE = true;
        this.NAME = name;
    }

    /**
     * updateRent overrides updateRent of Property by updating the rent based on the number of Utilities that the
     * player owns
     * @param theDice is the game dice
     */
    public void updateRent(ZagopolyDice theDice)
    {
        if(OWNER.getNumberOfUtilities() == 1)
        {
            RENT = 4 * theDice.getTotalRoll();
        }
        if(OWNER.getNumberOfUtilities() == 2)
        {
            RENT = 10 * theDice.getTotalRoll();
        }
    }

    /**
     * displayPropertyInfo overrides displayPropertyInfo of class Property
     * @param TextWindow is the GUI that displays the game messages
     */
    public void displayPropertyInfo(ZagopolyTextWindow TextWindow)
    {
        TextWindow.printMessage("Property Name: " + this.NAME);
        TextWindow.printMessage("Property Price: " + this.PRICE);
        TextWindow.printMessage("Property Rent: determined by dice.");
        TextWindow.printMessage("Property Set: UTILITY");
        if(isOwned())
            TextWindow.printMessage("Property Owner: Player " + (this.OWNER.getPlayerNum() + 1));
        else
            TextWindow.printMessage("Property is unowned.");
        if(isMortgaged())
            TextWindow.printMessage("This property is currently mortgaged, so it's owner " +
                    "cannot receive rent.");
    }

    /**
     * isUtility checks if the property is a utility
     * @return true
     */
    public boolean isUtility()
    {
        return true;
    }
}
