import javafx.scene.control.TextFormatter;

import java.util.Random;
import java.util.Scanner;

/**
 * Created by Tyler Wilding on 11/03/16.
 * Assignment 4 - COSC
 */
public class ChangeMaking {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
            System.out.println("Please enter the denominations of coins separated by spaces: ");
            String[] denominationsString =  in.nextLine().split("\\s"); //{"5", "1", "2", "10", "6", "2"};
            System.out.println("Please enter the amount you need to make change for: ");
            int amount = Integer.parseInt(in.nextLine());

            int[] denominations = new int[denominationsString.length];
            int[] minCoins = new int[amount+1];

            populateCoinArray(denominationsString, denominations);

            System.out.println("Making change for "+amount+" cents, requires "+changeAlgorithm(denominations, minCoins)+" coins.");

    }

    public static void populateCoinArray(String[] coins, int[] array) {

        for(int i = 0; i < array.length; i++)
            array[i] = Integer.parseInt(coins[i]);
    }

    public static int changeAlgorithm(int[] coins, int[] minCoins) {

        minCoins[0] = 0;

        for(int i = 1; i < minCoins.length; i++) {
            minChange(minCoins, coins, i);


        }
        
        return minCoins[minCoins.length-1];
    }

    public static void minChange(int[] minCoins, int[] coins, int value) {

        int smallestAmount = Integer.MAX_VALUE;

        String output = "F["+value+"] = ";

        for(int i = 0; i < coins.length; i++) {

            if(coins[i] > value)
                continue;

            int delta = minCoins[value - coins[i]];

            if(i != 0)
                output += " + ";

            output += "min(F["+value+" - "+coins[i]+"])";



            if(delta < smallestAmount)
                smallestAmount = delta;
        }

        minCoins[value] = smallestAmount+1;

        output += " = "+minCoins[value];

        System.out.println(output);
    }
}
