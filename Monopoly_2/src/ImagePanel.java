/**
 * class ImagePanel
 */

import javax.swing.*;
import java.awt.*;

/**
 * inner class for setting background image
 */
public class ImagePanel extends JComponent
{
    private Image backgroundImage;
    private PlayerIcon[] gamePieces;

    /**
     * constructor for ImagePanel
     * @param image
     */
    public ImagePanel(Image image)
    {
        this.backgroundImage = image;
    }

    /**
     * setImages assigns game icons to the image panel
     * @param icons
     */
    /*public void setImages(PlayerIcon[] icons)
    {
        gamePieces = new PlayerIcon[icons.length];
        for(int i = 0; i < icons.length; i++)
        {
            gamePieces[i] = icons[i];
        }
    }*/

    /**
     * doDrawing draws a figure on the game board
     * @param g is the object that draws the images on the board
     */
    public void doDrawing(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        for(int i = 0; i < gamePieces.length; i++)
        {
            g2d.drawImage(gamePieces[i].getImage(i), gamePieces[i].getX(), gamePieces[i].getY(), this);
        }
    }

    /**
     * overridden paintComponent
     */
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
    }
}
