/**
 * Class Set creates a small array of primary properties called a "Set"
 */

public class Set
{
    protected String NAME;
    protected Property SET[];
    protected Player OWNER;

    /**
     * constructor for class Set
     * @param setSize is the size of the property set
     */
    public Set(int setSize)
    {
        SET = new Property[setSize];
        NAME = "Undefined Set";
    }

    /**
     * addOwner makes a player the owner of this set of properties if he owns all three
     * @param newOwner is the player that is potentially going to be the new owner of the set
     * @param TextWindow is the GUI that displays the game messages
     */
    public void addOwner(Player newOwner, ZagopolyTextWindow TextWindow)
    {
        boolean ownerIsTheSame = true;
        for(int i = 0; i < SET.length; i++)
        {
            if(SET[i].getOwner() != newOwner)
            {
                ownerIsTheSame = false;
            }
        }

        if(ownerIsTheSame)
            OWNER = newOwner;
    }

    /**
     * removeOwned sets the owner of the set to null
     */
    public void removeOwner()
    {
        OWNER = null;
    }

    /**
     * displaySetInfo displays the names of the properties in the set
     * @param TextWindow is the GUI that displays the game messages
     */
    public void displaySetInfo(ZagopolyTextWindow TextWindow)
    {
        TextWindow.printMessage("This is the " + NAME + ", it contains: ");
        for(int i = 0; i < SET.length; i++)
        {
            TextWindow.printMessage(SET[i].getName());
        }
        TextWindow.printMessage("");
    }

    /**
     * updateRents updates all the rents of the properties in the set
     * @param theDice is the game dice
     */
    public void updateRents(ZagopolyDice theDice)
    {
        if(isOwned())
        {
            for(int p = 0; p < SET.length; p++)
            {
                if(SET[p].rentIsNotDoubled())
                    SET[p].updateRent(theDice);
            }
        }
    }

    /**
     * mortgageSet resets the property rents to their original rents if one of the properties is
     * mortgaged
     */
    public void mortgageSet(ZagopolyTextWindow TextWindow)
    {
        if(isMortgaged())
        {
            for(int p = 0; p < SET.length; p++)
            {
                SET[p].reduceRent();
            }
        } else{
            TextWindow.printMessage("This case should probably never happen, but we'll know" +
                    " if it does now.");
        }
    }

    /**
     * setSize returns the size of the set
     * @return size
     */
    public int setSize()
    {
        int size = SET.length;
        return size;
    }

    /**
     * isOwned checks to see if the current set is owned
     * @return false if OWNER is null
     */
    public boolean isOwned()
    {
        if(OWNER == null)
            return false;
        return true;
    }

    /**
     * containsProperty checks to see if the set contains a particular property
     * @param P is the property in question
     * @return true if the set contains this property
     */
    public boolean containsProperty(Property P)
    {
        boolean contains = false;
        int p = 0;
        while(!contains && p < SET.length)
        {
            if(SET[p].equals(P))
            {
                contains = true;
            }
            p++;
        }

        if(contains)
        {
            return true;
        }
        return false;
    }

    /**
     * isTwoSet checks to see if the current property is a two set
     * @return false
     */
    public boolean isTwoSet()
    {
        return false;
    }

    /**
     * isMortgaged checks to see if any of the properties in the set are mortgaged
     * @return true if any of the properties in the set are mortgaged
     */
    public boolean isMortgaged()
    {
        for(int p = 0; p < SET.length; p++)
        {
            if(SET[p].isMortgaged())
                return true;
        }
        return false;
    }

    /**
     * hasHouses checks to see if there are any houses left in the set
     * @return true if any property's number of houses is greater than 0
     */
    public boolean hasHouses()
    {
        for(int p = 0; p < SET.length; p++)
        {
            if(SET[p].getNumHouses() > 0)
                return true;
        }
        return false;
    }

    /**
     * getSetName returns the name of the set
     * @return NAME
     */
    public String getSetName()
    {
        return NAME;
    }

    /**
     * getProperty returns the property at the desired position in the set
     * p is the location of the property in the set
     * @return SET[p]
     */
    public Property getProperty(int p)
    {
        return SET[p];
    }
}
