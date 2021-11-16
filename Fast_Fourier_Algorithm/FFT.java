// Programmer: Nathan Vanos
// Programming Assignment 4, CSCD 501
// Date: 11/11/2021

// imports
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

// class FFT computes the FFT of a polynomial and its inverse
class FFT{

    // class ComplexNumber is needed for handling
    // operations with complex numbers since such operations cannot 
    // be handled by traditional java operators
    static class ComplexNumber{
        // fields:
        // real represents the real portion of a complex number
        // imaginary represents the imaginary portion of a complex number
        private double real;
        private double imaginary;

        // constructor() creates a new complex number object
        // params (inputs): 
            // @param: realPart, the initialization for the real number portion
            // @param: imaginaryPart, the initialization for the imaginary portion
        // returns (output): a new complex number object
        public ComplexNumber(final double realPart, final double imaginaryPart)
        {
            // initialize real and imaginary numbers to 0
            this.real = realPart;
            this.imaginary = imaginaryPart;
        }

        // getReal() returns the real portion of the complex number
        // params (inputs): void
        // returns (output): the real portion of the complex number
        public double getReal()
        {
            return this.real;
        }

        // getImaginary() returns the imaginary portion of the complex number
        // params (inputs): void
        // returns (output): the imaginary portion of the complex number
        public double getImaginary()
        {
            return this.imaginary;
        }

        // toString() creates a representation of a complex number
        // @Overrides toString()
        // params (inputs): void
        // returns (output): a string representing the complex number
        @Override
        public String toString()
        {
            // sign is a + if imaginary is positive, - otherwise
            String sign = (this.imaginary >= 0) ? (sign = "+") : (sign = "-");

            return String.format("%.1f%s%.1fi", this.real, sign, Math.abs(this.imaginary));
        }

        // add() adds this complex number to another complex number
        // params (inputs): another, the number being added to this one
        // returns (output): the resulting complex number after the addition
        public ComplexNumber add(final ComplexNumber another)
        {
            // add the real to the real then
            // add the imaginary to the imaginary
            double newReal = this.real + another.getReal();
            double newImaginary = this.imaginary + another.getImaginary();

            return new ComplexNumber(newReal, newImaginary);
        }

        // subtract() subtracts another complex number from this one
        // params (inputs): another, the number being subtracted from this one
        // returns (output): the resulting complex number after the subtraction
        public ComplexNumber subtract(final ComplexNumber another)
        {
            // subtract the real from the real
            // subtract the imaginary from the imaginary
            double newReal = this.real - another.getReal();
            double newImaginary = this.imaginary - another.getImaginary();

            return new ComplexNumber(newReal, newImaginary);
        }

        // multiply() multiplies this complex number by another complex number
        // params (inputs): another, the number being multiplied with this one
        // returns (output): the resulting complex number after the multiplication
        public ComplexNumber multiply(final ComplexNumber another)
        {
            // pemdas
            // 1. multiply the real numbers together
            // 2. multiply first real with second imaginary and 
            // first imaginary with second real
            // 3. multiply the imaginary numbers together
            //      note: the result of this last one will always be multiplied by -1
            //      since i^2 is -1
            double productOfReals = this.real * another.getReal();

            double newImaginary = (this.real * another.getImaginary()) + 
                                    (this.imaginary * another.getReal());

            double productOfImaginaries = (this.imaginary * another.getImaginary()) * (-1);

            return new ComplexNumber(productOfReals + productOfImaginaries, newImaginary);
        }
    }

    // main method
    public static void main(String[] args)
    {
        // only proceed if the user has entered the command line arguments
        if(argsExist(args))
        {
            // retrieve array of coefficients
            ComplexNumber[] coefficients = readInput(args[0]);

            // compute the FFT
            System.out.println("");
            ComplexNumber[] pointValues = recursiveFFT(coefficients, false, true);
            printResult(pointValues, false);
            System.out.println("");

            // compute the inverse of the FFT
            printResult(recursiveFFT(pointValues, true, true), true);
            System.out.println("");
        }
        else
        {
            endProgramWithError("Must enter the name of the coefficients file.");
        }   
    }

    // argsExist() checks to see if the command line args were given by the user
    // params (input): args, the command line arguments
    // returns (output): true if the length of the args is 1
    public static boolean argsExist(String[] args)
    {
        return args.length == 1;
    }

    // computeHalvedArray() creates a new array with half the elements of the original,
    // in which each element is either all of the even indicies of 
    // the original or all of the odd indices
    // params (inputs):
        // A, the original array
        // initialStep, the starting position of the halved array
    // returns (output): halvedArray, the halved version of the original array
    public static ComplexNumber[] computeHalvedArray(ComplexNumber[] A, int initialStep)
    {
        // initialize a new array
        ComplexNumber[] halvedArray = new ComplexNumber[ (A.length / 2) ];

        // store every other element from A in halvedArray,
        // starting from initialStep
        for(int i = 0; i < halvedArray.length; i++)
        {
            halvedArray[i] = A[initialStep];
            initialStep += 2;
        }

        return halvedArray;
    }

    // convertToArray() converts a List of Integers to an array of ComplexNumbers
    // params (input): coefficientsList, a List of Integers
    // returns (output): an array of ComplexNumbers
    public static ComplexNumber[] convertToArray(List<Integer> coefficientsList)
    {
        // declare a new array of equal length to the arraylist
        // then store every value from the arraylist in here
        // casted as a complex number
        int size = (coefficientsList.size() % 2 == 0) ? (coefficientsList.size()) : (coefficientsList.size() + 1);
        ComplexNumber[] coefficients = new ComplexNumber[size];
        Iterator<Integer> iterator = coefficientsList.iterator();

        // iterate through the list and store each of its elements
        // in the new array
        for(int i = 0; i < coefficientsList.size(); i++)
        {
            double realNumber = (double)iterator.next().intValue();
            coefficients[i] = new ComplexNumber(realNumber, 0);
        }

        // if the array has an odd number of values, there needs to be a 0 at the end
        if(coefficientsList.size() % 2 != 0)
            coefficients[size - 1] = new ComplexNumber(0, 0);

        return coefficients;
    }

    // endProgramWithError() terminates the process running this program and notifies the user of an error
    // params (inputs): message, the error message that will be shown to the user
    // returns (outputs): status code to system
    public static void endProgramWithError(String message)
    {
        System.out.println(message);
        System.exit(0);
    }

    // normalize() normalizes the final result of an inverse FFT
    // params (inputs): Y, the inverse FFT
    // returns (output): a normalized version of Y
    public static void normalize(ComplexNumber[] Y)
    {
        // just divide each value by n
        for(int i = 0; i < Y.length; i++)
        {
            Y[i] = new ComplexNumber(Y[i].getReal() / Y.length, Y[i].getImaginary());
        }
    }

    // openFile() opens a file
    // params(inputs): filename, the name of the file to be opened
    // returns(output): a new file object
    public static File openFile(String fileName)
    {
        return new File(fileName);
    }

    // printResult() prints the FFT or its inverse
    // params (inputs): FFT, the FFT or its inverse
    // returns (output): none, just prints the FFT or its inverse
    public static void printResult(ComplexNumber[] FFT, boolean inverse)
    {
        String start = inverse ? "Inverse DFT: " : "DFT:";
        System.out.print(start);

        // print each element
        // use string.format() for the 
        // coefficient representation
        for(int i = 0; i < FFT.length; i++)
        {
            if(inverse)
                System.out.print(String.format("%.1f,", FFT[i].getReal()));
            else
                System.out.print(FFT[i] + ", ");
        }

        System.out.println("");
    }

    // recursiveFFT() computes the FFT or inverse FFT of a list of 
    // coefficients or point values
    // params (inputs):
        // @param A, the list of coefficients or points
        // @param inverse, if this is true then FFT should compute the inverse
        // @param isTopCall, indicates that this is the first level of recursion
    // returns (output): the FFT of the original input or the inverse FFT
    public static ComplexNumber[] recursiveFFT(ComplexNumber[] A, boolean inverse, boolean isTopCall)
    {
        // retrieve the length to make this easier to read
        int length = A.length;

        // if the length is only one, stop recursion
        if(length == 1) return A;
        
        // divide the original array into two halves, A_ZERO and A_ONE
        ComplexNumber[] A_ZERO = computeHalvedArray(A, 0);
        ComplexNumber[] A_ONE = computeHalvedArray(A, 1);

        // recursively call this function on the two halves of the original
        // array to get n*log(n) time
        ComplexNumber[] Y_ZERO = recursiveFFT(A_ZERO, inverse, false);
        ComplexNumber[] Y_ONE = recursiveFFT(A_ONE, inverse, false);

        // initialize a new array to store FFT
        // omega is a complex number initialized to one
        ComplexNumber[] Y = new ComplexNumber[length];
        ComplexNumber omega = new ComplexNumber(1, 0);

        // assign the nth root of unity (omega_n)
        // if this is the regular FFT, just leave j as 1 
        // if this is the inverse calculation, j will be n-1 
        double j = inverse ? (length - 1) : 1;
        ComplexNumber omega_n = new ComplexNumber(
            Math.cos((2*j*Math.PI) / length),
            Math.sin((2*j*Math.PI) / length)
        );

        // scan through Y_ZERO and Y_ONE
        // perform complex number operations on each of the elements
        // store the result in the FFT array
        for(int k = 0; k < (length / 2); k++)
        {
            Y[k] = Y_ZERO[k].add(omega.multiply(Y_ONE[k]));
            Y[k + (length/2)] = Y_ZERO[k].subtract(omega.multiply(Y_ONE[k]));
            omega = omega.multiply(omega_n);
        }

        // if the inverse is being computed then the results must be normalized
        if(inverse && isTopCall)
            normalize(Y);
        
        return Y;
    }

    // readInput() reads input from 
    // params (inputs): filename, the name of the coefficient file
    // returns (output): an array of complex numbers
    public static ComplexNumber[] readInput(String fileName)
    {
        // open the file
        File inputFile = openFile(fileName);

        // handle file not found exceptions
        try
        {
            Scanner inputScanner = new Scanner(inputFile);
            List<Integer> input = new ArrayList<Integer>();
            // use arraylist to build actual array
            while(inputScanner.hasNextLine())
            {
                input.add(Integer.parseInt(inputScanner.nextLine()));
            }
            inputScanner.close();
            ComplexNumber[] convertedArray = convertToArray(input);
            return convertedArray;
        }
        catch(FileNotFoundException exception)
        {
            System.out.println(exception);
            endProgramWithError("Stopping program");
            return null;
        }
    }
}