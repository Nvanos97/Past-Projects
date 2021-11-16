/**
 * Class PlayerIcon creates a player icon
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlayerIcon
{
    private int PLAYER_NUM;
    private int x;
    private int y;
    private int JAIL_X;
    private int JAIL_Y;
    private BufferedImage ICON;

    /**
     * constructor for class PlayerIcon
     * @param playerNum is the number of players in the game
     */
    public PlayerIcon(int playerNum)
    {
        PLAYER_NUM = playerNum;
        initImages();
        x = getStartX();
        y = getStartY();
        JAIL_X = setJailX();
        JAIL_Y = setJailY();
    }

    /**
     * getImage returns the image of the player icon
     * @param imageNum is the number of the icon in question
     * @return image1, image2, image3, or image4
     */
    public BufferedImage getImage(int imageNum)
    {
        return ICON;
    }

    /**
     * getStartX gets the starting position of an icon on the X axis
     * @return x position of icon on board
     */
    private int getStartX()
    {
        if (PLAYER_NUM == 0){
            return 608;
        }
        else if(PLAYER_NUM == 1){
            return 650;
        }
        else if(PLAYER_NUM == 2){
            return 608;
        }
        else{
            return 645;
        }
    }

    /**
     * getStartY gets the starting position of an icon on the Y axis
     * @return y position of icon on the board
     */
    private int getStartY()
    {
        if (PLAYER_NUM == 0){
            return 610;
        }
        else if(PLAYER_NUM == 1){
            return 610;
        }
        else if(PLAYER_NUM == 2){
            return 640;
        }
        else{
            return 636;
        }
    }

    /**
 * setJailX gets the jail location for a particular piece
 * @return
 */
private int setJailX()
{
    if (PLAYER_NUM == 0){
        return 50;
    }
    else if(PLAYER_NUM == 1){
        return 45;
    }
    else if(PLAYER_NUM == 2){
        return 55;
    }
    else{
        return 60;
    }
}

    /**
     * setJailY gets the jail location for a particular piece
     * @return
     */
    private int setJailY()
    {
        if (PLAYER_NUM == 0){
            return 625;
        }
        else if(PLAYER_NUM == 1){
            return 630;
        }
        else if(PLAYER_NUM == 2){
            return 620;
        }
        else{
            return 615;
        }
    }

    /**
     * initImages initializes the player icons that are to be put on the board
     */
    private void initImages()
    {
        try {
            if(PLAYER_NUM == 0)
                ICON = ImageIO.read(new File("res/playerIcon1.png"));
            else if(PLAYER_NUM == 1)
                ICON = ImageIO.read(new File("res/playerIcon2t.png"));
            else if(PLAYER_NUM == 2)
                ICON = ImageIO.read(new File("res/playerIcon3t.png"));
            else
                ICON = ImageIO.read(new File("res/playerIcon4t.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * move moves the player icon on the board
     *
     * @param theBoard      is the game board
     * @param currentPlayer is the player in question
     */
    public void move(Board theBoard, Player currentPlayer)
    {
        Property tempProperty = theBoard.getSquare(currentPlayer.currentSquare());
        int moveToX = tempProperty.getX();
        int moveToY = tempProperty.getY();
        x = moveToX;
        y = moveToY;
    }

    /**
     * getX gets the x coordinate of the player icon
     *
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * getY returns the y coordinate of the player icon
     *
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * getJailX gets the x jail position for an icon
     * @return JAIL_X
     */
    public int getJailX()
    {
        return JAIL_X;
    }

    /**
     * getJailY gets the y jail position for an icon
     * @return JAIL_Y
     */
    public int getJailY()
    {
        return JAIL_Y;
    }
}
