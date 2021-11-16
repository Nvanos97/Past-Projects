/**
 * Class Die class creates a virtual die
 */

public class Die
{
    /**
     * rollDie simulates the rolling of a single die
     * @return the value of the resulting simulated roll
     */
    public int rollDie()
    {
        int roll = (int)(Math.random() * 6 + 1);
        return roll;
    }
}