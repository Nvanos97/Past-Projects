/**
 * Class Three_Set creates a Set of three primary properties
 */

public class Three_Set extends Set
{
    /**
     * constructor for class Three_Set
     * @param P1 is the primary property in the set that comes first on the board
     * @param P2 is the primary property in the set that is in the middle on the board
     * @param P3 is the primary property in the set that comes last on the board
     */
    public Three_Set(Property P1, Property P2, Property P3)
    {
        super(3);
        SET[0] = P1;
        SET[1] = P2;
        SET[2] = P3;

        if(P1.getNumber() == 6)
            NAME = "Light Blue Set";
        else if(P1.getNumber() == 11)
            NAME = "Pink Set";
        else if(P1.getNumber() == 16)
            NAME = "Orange Set";
        else if(P1.getNumber() == 21)
            NAME = "Red Set";
        else if(P1.getNumber() == 26)
            NAME = "Yellow Set";
        else
            NAME = "Green Set";
    }
}
