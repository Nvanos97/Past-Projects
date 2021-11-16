// Programmer: Nathan Vanos
// Programming Assignment 3, CSCD 501
// Date: 10/24/2021
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.Integer;
import java.io.IOException;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.List;

// class PatternMatching matches strings using both the Karp-Rabin and Automata algorithms
class PatternMatching{
    // main method
    public static void main(String[] args)
    {
        // only proceed if the command line arguments are given
        if(argsExist(args))
        {
            // exception handling for buffered reader
            try{
                // need the alphabet, the text, and the pattern for 
                // both algorithms
                final int[] ALPHABET = buildAlphabet();
                final String textString = readInput(args[0]);
                final String patternString = readInput(args[1]);
                checkForNullInput(textString, patternString);
                checkForLongerPattern(textString, patternString);

                // perform automata based pattern matching
                System.out.println("");
                performAutomata(textString, patternString, ALPHABET);
                performKarpRabin(textString, patternString, ALPHABET);
                System.out.println("");
            }
            catch(IOException exception)
            {
                System.out.println(exception);
                endProgramWithError("Program run ended.");
            }
            
        }
        else
        {
            endProgramWithError("Must enter names of text and pattern files.");
        }
    }

    // argsExist checks to see if the command line args were given by the user
    // params (input): args, the command line arguments
    // returns (output): true if the length of the args is 2
    public static boolean argsExist(String[] args)
    {
        return args.length == 2;
    }

    // automataMatching() matches patterns in a text with
    // the previously constructed delta table
    // params (inputs):
        // text, the text that is being searched
        // deltaTable, the table of states for the automata
        // finalState, the acceptance state of the deltaTable
    // returns (output): none, just prints the starting indices of the matches
    public static void automataMatching(final String text, int[][] deltaTable, int finalState)
    {
        // state starts at 0
        // matchesExist is just for printing
        int currentState = 0;
        boolean matchesExist = false;

        System.out.print("The automata-based pattern matching result: ");

        // feed the text into the delta table (automata)
        // if currentState reaches the final state
        // then a match has been found
        for(int i = 1; i <= text.length(); i++)
        {
            // 0 based indexing
            int column = (int)text.charAt(i - 1);
            currentState = deltaTable[currentState][column];
            if(currentState == finalState)
            {
                printMatch(i - finalState, matchesExist);
                matchesExist = true;
                /*currentState = 0;
                if(text.charAt(i - 1) == text.charAt(i - finalState) && finalState > 1)
                    currentState = 1;*/
            }
        }

        if(matchesExist)
            System.out.println();
        else
            System.out.println("null");
        
    }

    // buildAlphabet constructs the alphabet of ascii characters
    // represented as their int ascii values
    // params (inputs): none
    // returns (output): alphabet, the array of ascii chars as their int values
    public static int[] buildAlphabet()
    {
        // just make an array of size 256 and make each element
        // the same as its index
        int[] alphabet = new int[256];

        for(int i = 0; i < alphabet.length; i++)
        {
            alphabet[i] = i;
        }

        return alphabet;
    }

    // calculateInitialHash() calculates the modulo of the 
    // hashcode of a substring, where the substring is the length
    // of the given pattern
    // params (inputs): 
        // text, the text that contains the substring
        // patternLength, the length of the pattern that is being matched
        // alphabetLength, the length of the alphabet
        // p, the modulo value
    // returns (output): the modded hashcode of the substring
    public static int calculateInitialHash(final String text, int patternLength, int alphabetLength, int p)
    {
        // recursive relationship in original equation
        // base case is when i == m - 1, so
        // m (pattern length) should be 1
        // in this case the alphabetLength multiplier will disappear
        // because it will be 256^(i - i), which is 1
        if(patternLength == 1)
        {
            return (int)text.charAt(patternLength - 1) % p;
        } 
        // recursive case is when you must multiply by sigma (alphabetLength) and then
        // add the int of the next value in the text
        else
        {
            return (
                alphabetLength * calculateInitialHash(text, patternLength - 1, alphabetLength, p) 
                + (int)text.charAt(patternLength - 1)
            ) % p;
        }
    }

    // calculateMultiplier() efficiently computes the large number (sigma^m) that is used in the
    // calculation of the next modulo hashcode value
    // params (inputs):
        // alphabetLength, the length of the list of all possible characters
        // patternLength, the length of the pattern that is being searched for
        // p, the modulo value
    // returns (output): the multiplier for the equation
    public static int calculateMultiplier(int alphabetLength, int patternLength, int p)
    {
        // recursive relationship in the original equation
        // base case is when the exponent is m - (m - 1), or 1
        // in that case just return sigma ^ 1 (alphabetLength ^ 1)
        if(patternLength == 1)
        {   
            return alphabetLength % p;
        } 
        // in the recursive case just multiply the previous result by sigma again
        // and then modulo again
        else
        {
            return (
                alphabetLength * calculateMultiplier(alphabetLength, patternLength - 1, p)
            ) % p;
        }
    }

    // calculateNextHash() efficiently computes the modded hashcode of
    // the next substring in the text
    // params (inputs):
        // text, the text that it being searched
        // patternLength, or "m", the length of the pattern
        // alphabetLength, or sigma, the number of possible characters
        // r, the starting index of the next substring
        // p, the modulo value
        // prevHash, the hash of the current substring
    // returns (output): the hash of the next substring
    public static int calculateNextHash(
        final String text, 
        int patternLength, 
        int alphabetLength, 
        int r, 
        int p, 
        int previousHash,
        int multiplier
    )
    {
        // equation: sigma * Hp(Tr-1) - (sigma ^ m % p) * T[r - 1] + T[r + m - 1]
        // add the terms together and modulo the resulting value
        return (
            (alphabetLength * previousHash)
            - (multiplier * (int)text.charAt(r - 1))
            + (int)text.charAt(r + patternLength - 1)
        ) % p;
    }


    // checkForNullInput() checks if the text or pattern is null
    // params (inputs):
        // text, the given text to be searched
        // pattern, the given pattern to be searched for
    // returns (outputs): none, just prints that the matches were null and
    // ends the program
    public static void checkForNullInput(final String text, final String pattern)
    {
        if(text == null || pattern == null)
        {
            System.out.print("The automata-based pattern matching result: null");
            System.out.print("The Karp-Rabin pattern matching result: null");
            System.exit(0);
        }
    }

    // checkForLongerPattern() checks if the text is shorter than the pattern
    // params (inputs):
        // text, the given text to be searched
        // pattern, the given pattern to be searched for
    // returns (outputs): none, just prints that the matches were null and
    // ends the program
    public static void checkForLongerPattern(final String text, final String pattern)
    {
        if(pattern.length() > text.length())
        {
            System.out.print("The automata-based pattern matching result: null");
            System.out.print("The Karp-Rabin pattern matching result: null");
            System.exit(0);
        }
    }

    // constructAutomata() builds the delta table for the automata algorithm
    // params (inputs):
        // pattern, the pattern that is being searched for
        // alphabet, the list of all possible characters
    // returns (outputs): the delta table
    public static int[][] constructAutomata(final String pattern, final int[] alphabet)
    {
        // initialize the new delta table as a 2d array of integers
        int patternLength = pattern.length();
        int[][] deltaTable = new int[patternLength][alphabet.length];

        // fill each row of the new table
        for(int state = 0; state < patternLength; state++)
        {
            // fill each column of the new table
            for(int currentChar = 0; currentChar < alphabet.length; currentChar++)
            {
                // the index of the current prefix is the minimum between these two values
                int currentPrefixIndex = Math.min(patternLength + 1, state + 2);

                // decrement the ending index of the current prefix
                // until it is also a suffix of the current 
                // suffix in the pattern
                do
                {
                    currentPrefixIndex--;
                }
                while(!isSuffix(pattern, currentPrefixIndex, state, (char)currentChar));

                // use the currentPrefixIndex as a state in the delta table
                deltaTable[state][currentChar] = currentPrefixIndex;
            }
        }

        return deltaTable;
    }

    // endProgramWithError() terminates the process running this program and notifies the user of an error
    // params (inputs): message, the error message that will be shown to the user
    // returns (outputs): status code to system
    public static void endProgramWithError(String message)
    {
        System.out.println(message);
        System.exit(0);
    }

    // isSuffix() checks if the substring in pattern from 0 to state 
    // is equal to the substring at pattern from 0 to largestPrefixIndex
    // params (inputs):
        // pattern, the pattern that contains the suffix
        // largestPrefixIndex, the index that marks the end of the prefix
        // state, the index that marks the penultimate index of the suffix
        // currentChar, the last character in the suffix
    // returns (output): true if the largest prefix of the current substring is also a 
    // suffix of the current substring
    public static boolean isSuffix(final String pattern, int currentPrefixIndex, int state, char currentChar)
    {
        // if currentPrefix has reached 0 then this character of the alphabet 
        // is not in the pattern
        if(currentPrefixIndex == 0)
            return true;

        return pattern.substring(0, currentPrefixIndex).equals(pattern.substring(0, state) + currentChar);
    }

    // openFile(): opens a file
    // params(inputs): filename, the name of the file to be opened
    // returns(output): a new file object
    public static File openFile(String fileName)
    {
        return new File(fileName);
    }

    // performAutomata() runs the automata-based pattern matching system on
    // the text and the given pattern
    // params (inputs):
        // text, the text to be searched
        // pattern, the pattern that is being searched for
        // alphabet, the list of all possible characters
    // returns (output): just prints the array of starting indices
    public static void performAutomata(final String text, final String pattern, final int[] alphabet)
    {
        // first, construct the delta table
        final int[][] deltaTable = constructAutomata(pattern, alphabet);
        // then use the delta table to find the substrings
        automataMatching(text, deltaTable, pattern.length());
    }

    // performKarpRabin() runs the Karp-Rabin pattern matching system
    // on the text and the given pattern
    // params (inputs):
        // text, the text to be searched
        // pattern, the pattern that is being searched for
        // alphabet, the list of all possible characters
    // returns (output): just prints the array of starting indices
    public static void performKarpRabin(final String text, final String pattern, final int[] alphabet)
    {
        // hard code p as 524287, as the assignment requires
        int p = 524287;
        boolean matchesExist = false;

        // step 1: calculate the hash of text at 0 (Hp(T0)),
        // the pattern's hash (Hp(P)), and the multiplier (sigma ^ m % p)
        int currentTextHash = calculateInitialHash(text, pattern.length(), alphabet.length, p);
        int patternHash = calculateInitialHash(pattern, pattern.length(), alphabet.length, p);
        int multiplier = calculateMultiplier(alphabet.length, pattern.length(), p);

        System.out.print("The Karp-Rabin pattern matching result: ");

        // now that the modulo of H(P) and H(T0) are obtained, 
        // scan the text for matches
        for(int r = 0; r < (text.length() - pattern.length() + 1); r++)
        {
            // if the pattern's modded hash code == the current substring's
            // modded hashcode, this is a match
            if(patternHash == currentTextHash)
            {
                printMatch(r, matchesExist);
                matchesExist = true;
            }
            // get the hash of the next substring as long as r + m is not greater than 
            // the last index of the text
            if(r < (text.length() - pattern.length()))
            {
                currentTextHash = calculateNextHash(text, pattern.length(), alphabet.length, r + 1, p, currentTextHash, multiplier);
            }
        }

        if(matchesExist) 
            System.out.println("");
        else
            System.out.println("null");

    }

    // printMatch() prints an index where a pattern match is found
    // params (inputs):
        // matchIndex, the starting index of the match
        // notFirstMatch, a boolean that states whether this is the first match
    public static void printMatch(int matchIndex, boolean notFirstMatch)
    {
        if(notFirstMatch == false)
            System.out.print(matchIndex);
        else
            System.out.print(", " + matchIndex);
    }

    // readInput() reads input from a file and stores it in a single string
    // params (inputs): fileName, the name of the file to be read
    // returns (outputs): an array of numbers
    public static String readInput(String fileName) throws IOException
    {
        File inputFile = openFile(fileName);

        // must handle file not found exception with scanner
        try
        {
            //Scanner inputScanner = new Scanner(inputFile);
            FileReader inputReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(inputReader);
            StringBuilder inputBuilder = new StringBuilder();
            int currentValue;

            while((currentValue = bufferedReader.read()) != -1)
            {
                inputBuilder.append((char) currentValue);
            }

            bufferedReader.close();
            return inputBuilder.toString();
        } 
        catch(FileNotFoundException exception)
        {
            System.out.println(exception);
            endProgramWithError("Stopping program");
            return "";
        }
    }
}