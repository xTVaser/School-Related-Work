import java.util.Scanner;

/**
 * Created by Tyler Wilding on 11/03/16.
 * Assignment 4 - COSC
 * Description - Finds the minimum amount of coins needed to make change given a list of denominations
 */
public class ChangeMaking {

    public static void main(String[] args) {

        //Get the User input
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter the denominations of coins separated by spaces: ");
        String[] denominationsString =  in.nextLine().split("\\s");
        System.out.println("Please enter the amount you need to make change for: ");
        int amount = Integer.parseInt(in.nextLine());

        //Make the two arrays
        int[] denominations = new int[denominationsString.length];
        int[] minCoins = new int[amount+1];

        //Put the user values into the array
        populateCoinArray(denominationsString, denominations);

        //Call the algorithm and output the answer
        System.out.println("Making change for "+amount+" cents, requires "+changeAlgorithm(denominations, minCoins)+" coins.");
    }

    /**
     * Will place numbers seperated by spaces into an integer array
     * @param coins String array of numbers
     * @param array Int array of those numbers
     */
    public static void populateCoinArray(String[] coins, int[] array) {

        for(int i = 0; i < array.length; i++)
            array[i] = Integer.parseInt(coins[i]);
    }

    /**
     * Will pass through all numbers from 1...n where n is the amount needed for change.
     * @param coins array of denomination values.
     * @param minCoins array of the minimum number of coins needed for each amount along the way
     * @return An integer containing the minimum number of coins needed for the requested amount.
     */
    public static int changeAlgorithm(int[] coins, int[] minCoins) {

        //First value is 0
        minCoins[0] = 0;

        //Loop through the array to fill every spot
        for(int i = 1; i < minCoins.length; i++)
            //Each time we call the method to determine the best coin to choose for each spot.
            minChange(minCoins, coins, i);

        //The answer is in the last position of the array
        return minCoins[minCoins.length-1];
    }

    /**
     * Will determine the minimum number of coins needed based on previous calculations.
     * @param minCoins Array that holds all previous calculations on the minum number of coins
     * @param coins Denomination array
     * @param value The value we are attempting to solve for at this point.
     */
    public static void minChange(int[] minCoins, int[] coins, int value) {

        int smallestAmount = Integer.MAX_VALUE;

        String output = "F["+value+"] = ";

        //Loop through all denominations
        for(int i = 0; i < coins.length; i++) {

            //If the denomination is greater than what we are making change for, skip it.
            if(coins[i] > value)
                continue;

            //Use the previous calculation to determine this amounts requirement.
            int delta = minCoins[value - coins[i]];

            if(i != 0)
                output += " + ";
            output += "min(F["+value+" - "+coins[i]+"])";

            //Else, if its the largest denomination that will work, set it.
            if(delta < smallestAmount)
                smallestAmount = delta;
        }

        //The algorithm in the book has a flaw where if you do not give it a denomination to work in all cases, for example 1,
        //Then things will break, this solves some problems but not all of them.
        if(smallestAmount == Integer.MAX_VALUE)
            output += " = Impossible given the denominations";

        else {

            //The answer is the previous calculations total coins, plus 1 because we are adding a coin right now.
            minCoins[value] = smallestAmount + 1;

            output += " = " + minCoins[value];
        }

        System.out.println(output);
    }
}