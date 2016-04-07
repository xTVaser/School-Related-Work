import java.util.Random;
import java.util.Scanner;

/**
 * Created by Tyler Wilding on 10/03/16.
 * Assignment 4 - Analysis of Algorithms
 * Description - Finds the maximum amount of money that can be picked up without picking up adjacent coins.
 */
public class CoinRow {

    public static void main(String[] args) {

        //User Input
        Scanner in = new Scanner(System.in);

        System.out.println("Stress Test: (y/n): ");
        if(in.nextLine().equalsIgnoreCase("y"))
            stressTest(1000000);

        //Split the strings and turn them into arrays,call the algorithm.
        else {

            System.out.println("Please enter the values of the coins, seperated by spaces: ");
            String[] coinRow =  in.nextLine().split("\\s"); //{"5", "1", "2", "10", "6", "2"};

            int[] coins = new int[coinRow.length+1];
            int[] maxMoney = new int[coinRow.length+1];

            populateCoinArray(coinRow, coins);

            System.out.println(coinRowAlgorithm(coins, maxMoney));
        }
    }


    /**
     * Method that will take in a string array and convert it to an integer array
     * @param coins String array of coin values
     * @param array Int array to hold the coin values
     */
    public static void populateCoinArray(String[] coins, int[] array) {

        for(int i = 0; i < array.length-1; i++)
            array[i+1] = Integer.parseInt(coins[i]);
    }

    /**
     * Main algorithm procedure, will find the most money that can be picked up without picking up adjacent coins.
     * @param coins Array of coins to pick up
     * @param money Array that will keep track of the money we have accumulated
     * @return The maximum we can pick up.
     */
    public static int coinRowAlgorithm(int[] coins, int[] money) {

        //First two numbers are static.
        money[0] = 0;
        money[1] = coins[1];

        //Loop through both arrays
        for(int i = 2; i < coins.length; i++) {
            //Determine whether or not it is better to either:
            //1. Pick up the current coin, and drop the previous one or.
            //2. Keep the last 2 coin combinations and skip this one.
            money[i] = Math.max((coins[i] + money[i-2]), money[i-1]);
            System.out.printf("F[%4d] = max(%3d + %3d, %3d) = %3d\n", i, coins[i], money[i-2], money[i-1], money[i]);
        }

        return money[money.length-1];
    }

    /**
     * Will generate random coins.
     * @param iterations Number of coins in the coin row.
     */
    public static void stressTest(int iterations) {

        int[] coins = new int[iterations+1];

        Random rand = new Random();

        for(int i = 0; i < coins.length; i++)
            coins[i] = rand.nextInt(50)+1;

        int[] money = new int[iterations+1];

        System.out.println(coinRowAlgorithm(coins, money));
    }
}
