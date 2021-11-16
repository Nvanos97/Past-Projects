/**
 * Class ZagopolyDice creates the game dice for Zagopoly
 */

public class ZagopolyDice
{
    private Die Dice;
    private int[] rolls;
    private int total;

    /**
     * Constructor for class ZagopolyDice
     *
     * @param dNumber is the number of dice (should be 2 in standard Monopoly)
     */
    ZagopolyDice(int dNumber)
    {
        Dice = new Die();
        rolls = new int[dNumber];
        total = 0;
        //numberOfDice = dNumber;
    }

    /**
     * rollDice rolls the Zagopoly game dice
     */
    public void rollDice()
    {
        for (int d = 0; d < rolls.length; d++)
        {
            rolls[d] = Dice.rollDie();
            total += rolls[d];
        }
    }

    /**
     * resetDice resets the total roll (total) back to 0
     */
    public void resetDice()
    {
        for(int r = 0; r < rolls.length; r++)
            rolls[r] = 0;
        total = 0;
    }

    /**
     * displayRoll displays the player's most recent roll
     * @param TextWindow is the GUI that displays the game messages
     */
    public void displayRoll(ZagopolyTextWindow TextWindow)
    {
        if (noRolls())
            TextWindow.printMessage("The player has not yet rolled anything.");
        else {
            if(doubles())
                TextWindow.printMessage("YOU ROLLED A DOUBLES!! ");
            TextWindow.printMessage("Player's roll was: ");
            for(int r = 0; r < rolls.length; r++)
            {
                TextWindow.printMessage(rolls[r] + ", ");
            }
            TextWindow.printMessage("The player will move " + total + " squares unless they are in Campo.");
        }
    }

    /**
     * getTotalRoll returns the sum of all the rolls
     * @return total, the sum of all the rolls
     */
    public int getTotalRoll()
    {
        return total;
    }

    /**
     * doubles checks to see if the player rolled a doubles
     * @return true if both dice have the same value
     */
    public boolean doubles()
    {
        if(rolls[0] == rolls[1] && rolls[0] != 0)
            return true;
        return false;
    }

    /**
     * noRolls checks to see if there are no rolled dice
     *
     * @return true if the count is 0
     */
    public boolean noRolls()
    {
        if (total == 0)
            return true;
        return false;
    }
}
