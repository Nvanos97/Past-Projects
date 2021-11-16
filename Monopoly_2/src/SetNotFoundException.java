/**
 * This class is a child class of Exception that implements a SetNotFoundException
 * CPSC 224, Spring 2018
 * Group Project
 * @authors Connor Cooley, Jackson Paris, Nathan Vanos
 * @version 1.0, 04/24/2018
 */


public class SetNotFoundException extends Exception
{
    /**
     * constructor for class SetNotFoundException
     * @param message is the message that will be printed if this exception is caught
     */
    public SetNotFoundException(String message)
    {
        super(message);
    }
}
