/**
 * Class The_Sets creates an array of all the Sets in the game
 */

public class The_Sets
{
    private Set[] GAME_SETS;

    /**
     * constructor for class The_Sets
     * @param theBoard is the game board
     */
    public The_Sets(Board theBoard)
    {
        GAME_SETS = new Set[8];

        GAME_SETS[0] = new Two_Set(theBoard.getSquare(1), theBoard.getSquare(3));
        GAME_SETS[1] = new Three_Set(theBoard.getSquare(6), theBoard.getSquare(8), theBoard.getSquare(9));
        GAME_SETS[2] = new Three_Set(theBoard.getSquare(11), theBoard.getSquare(13), theBoard.getSquare(14));
        GAME_SETS[3] = new Three_Set(theBoard.getSquare(16), theBoard.getSquare(18), theBoard.getSquare(19));
        GAME_SETS[4] = new Three_Set(theBoard.getSquare(21), theBoard.getSquare(23), theBoard.getSquare(24));
        GAME_SETS[5] = new Three_Set(theBoard.getSquare(26), theBoard.getSquare(27), theBoard.getSquare(29));
        GAME_SETS[6] = new Three_Set(theBoard.getSquare(31), theBoard.getSquare(32), theBoard.getSquare(34));
        GAME_SETS[7] = new Two_Set(theBoard.getSquare(37), theBoard.getSquare(39));
    }

    /**
     * getNumberOfSets returns the number of sets in the game
     * @return GAME_SETS.length
     */
    public int getNumberOfSets()
    {
        return GAME_SETS.length;
    }

    /**
     * thisSet returns the set at the desired index of GAME_SETS
     * @param s is the desired index
     * @return GAME_SETS[s]
     */
    public Set getSet(int s)
    {
        return GAME_SETS[s];
    }
}
