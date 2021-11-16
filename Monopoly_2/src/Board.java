/**
 * Class Board creates an array of properties which acts as the board
 */


public class Board
{
    private int COMMUNITY_POT;
    private Property[] THE_BOARD;

    /**
     * constructor for class Board
     * @param players is the array containing all of the players in the game
     */
    public Board(Player players[])
    {
        COMMUNITY_POT = 0;
        THE_BOARD = new Property[40];
        initAllSquares(players);
    }

    /**
     * getSquare returns a property in the board
     * @param squareNum is the position of the square on the board
     * @return a property in THE_BOARD
     */
    public Property getSquare(int squareNum)
    {
        return THE_BOARD[squareNum];
    }

    /**
     * cashPotIn adds a sum of money to the total amount of money on "Free Parking"
     * @param currentPlayer is the player who is currently rolling
     * @param tax is the amount that the player has to pay to the pot
     */
    public void cashPotIn(Player currentPlayer, int tax)
    {
        currentPlayer.finePlayer(tax);
        COMMUNITY_POT += tax;
    }

    /**
     * cashPotOut pays a player the sum in the pot if they land on "Free Parking"
     * @param currentPlayer is the player who is currently rolling
     */
    public void cashPotOut(Player currentPlayer)
    {
        currentPlayer.payPlayer(COMMUNITY_POT);
        COMMUNITY_POT = 0;
    }

    /**
     * getCommunityPot returns the sum of money in the pot
     * @return COMMUNITY_POT
     */
    public int getCommunityPot()
    {
        return COMMUNITY_POT;
    }

    /**
     * initAllSquares initializes each square on the board
     * @param players is the array containing all players in the game
     */
    private void initAllSquares(Player players[])
    {
        THE_BOARD[0] = new GoSquare(players, 608, 610);
        THE_BOARD[1] = new PrimaryProperty(1, 60, 2, 50, "CM", 553, 622);
        THE_BOARD[2] = new SpecialCardSquare(2, "Community Service", 495, 622);
        THE_BOARD[3] = new PrimaryProperty(3, 60, 4, 50, "Madonna", 437, 622);
        THE_BOARD[4] = new TaxSquare(4, 200, "Tuition", 379, 622);
        THE_BOARD[5] = new RestaurantProperty(5, "Duffs Bistro", 321, 622);
        THE_BOARD[6] = new PrimaryProperty(6, 100, 6, 50, "Welch", 263, 622);
        THE_BOARD[7] = new SpecialCardSquare(7, "Chance", 205, 622);
        THE_BOARD[8] = new PrimaryProperty(8, 100, 6, 50, "Chardan", 147, 622);
        THE_BOARD[9] = new PrimaryProperty(9, 120, 8, 50, "Desmet", 89, 622);
        THE_BOARD[10] = new CampoSquare(3, 647);
        THE_BOARD[11] = new PrimaryProperty(11, 140, 10, 100, "Dillon", 30, 551);
        THE_BOARD[12] = new UtilityProperty(12, "Plant Services", 30, 497);
        THE_BOARD[13] = new PrimaryProperty(13, 140, 10, 100, "Twohy", 30, 443);
        THE_BOARD[14] = new PrimaryProperty(14, 160, 12, 100, "Coughlin", 30, 389);
        THE_BOARD[15] = new RestaurantProperty(15, "1887", 30, 335);
        THE_BOARD[16] = new PrimaryProperty(16, 180, 14, 100, "Crosby", 30, 281);
        THE_BOARD[17] = new SpecialCardSquare(17, "Community Service", 30, 227);
        THE_BOARD[18] = new PrimaryProperty(18, 180, 14, 100, "Saint Aloysius", 30, 173);
        THE_BOARD[19] = new PrimaryProperty(19, 200, 16, 100, "Zag Shop", 30, 119);
        THE_BOARD[20] = new Property(20, 0, 0, 30, 30);
        THE_BOARD[21] = new PrimaryProperty(21, 220, 18, 150, "Hughes", 107, 33);
        THE_BOARD[22] = new SpecialCardSquare(22, "Chance", 163, 33);
        THE_BOARD[23] = new PrimaryProperty(23, 220, 18, 150, "Herak", 219, 33);
        THE_BOARD[24] = new PrimaryProperty(24, 240, 20, 150, "College Hall", 275, 33);
        THE_BOARD[25] = new RestaurantProperty(25, "Starbucks", 331, 33);
        THE_BOARD[26] = new PrimaryProperty(26, 260, 22, 150, "Paccar", 387, 33);
        THE_BOARD[27] = new PrimaryProperty(27, 260, 22, 150, "Jepson", 443, 33);
        THE_BOARD[28] = new UtilityProperty(28, "IT Services", 499, 33);
        THE_BOARD[29] = new PrimaryProperty(29, 280, 22, 150, "Tilford", 555, 33);
        THE_BOARD[30] = new GoToCampoSquare(611, 33);
        THE_BOARD[31] = new PrimaryProperty(31, 300, 26, 200, "Dussault", 637, 108);
        THE_BOARD[32] = new PrimaryProperty(32, 300, 26, 200, "Kennedy", 637, 165);
        THE_BOARD[33] = new SpecialCardSquare(33, "Community Service", 637, 222);
        THE_BOARD[34] = new PrimaryProperty(34, 320, 28, 200, "Hemmingson", 637, 279);
        THE_BOARD[35] = new RestaurantProperty(35, "The COG", 637, 336);
        THE_BOARD[36] = new SpecialCardSquare(36, "Chance", 637, 393);
        THE_BOARD[37] = new PrimaryProperty(37, 350, 35, 200, "McCarthey Athletic Center", 637, 450);
        THE_BOARD[38] = new TaxSquare(38, 75, "Mandatory Donation", 637, 507);
        THE_BOARD[39] = new PrimaryProperty(39, 400, 50, 200, "Volker Athletic Center", 637, 563);
    }
}