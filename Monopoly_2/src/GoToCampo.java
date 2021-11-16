/**
 * This class is a child class of class property that creates the "Go to Campo" square
 * CPSC 224, Spring 2018
 * Group Project
 * @authors Connor Cooley, Jackson Paris, Nathan Vanos
 * @version 1.0, 04/15/2018
 */

public class GoToCampo extends Property
{
    /**
     * constructor of class GoToCampo
     * @param XCoord is the X coordinate of the "Go To Campo" square on the board
     * @param YCoord is the Y coordinate of the "Go to Campo" square on the board
     */
    public GoToCampo(int XCoord, int YCoord)
    {
        super(30, 0, 0, XCoord, YCoord);
        NAME = "Go To Campo";
    }

    /**
     * isGoToCampoSquare checks to see if this is the "Go to Campo" square
     * @return true
     */
    public boolean isGoToCampoSquare()
    {
        return true;
    }
}
