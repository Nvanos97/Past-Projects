/**
 * Class GoSquare is a child class of class property that creates the game's "Go" Square
 */

public class GoSquare extends Property
{
    /**
     * constructor for class GoSquare
     * @param players is the array containing all of the players in the game
     */
    public GoSquare(Player players[], int XCoord, int YCoord)
    {
        super(0, 0, 0, XCoord, YCoord);
        NAME = "Go";
    }
}
