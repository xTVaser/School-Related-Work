import java.util.Random;
import java.util.Scanner;

/**
 * Created by Tyler Wilding on 10/03/16.
 * Assignment 4 - Analysis of Algorithms
 * Description - Finds the maximum amount of money that can be picked up without picking up adjacent coins.
 */
public class CoinRow {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.println("Stress Test: (y/n): ");
        if(in.nextLine().equalsIgnoreCase("y"))
            stressTest(1000000);

        else {

            System.out.println("Please enter the values of the coins, seperated by spaces: ");
            String[] coinRow =  in.nextLine().split("\\s"); //{"5", "1", "2", "10", "6", "2"};

            int[] coins = new int[coinRow.length+1];
            int[] maxMoney = new int[coinRow.length+1];

            populateCoinArray(coinRow, coins);

            System.out.println(coinRowAlgorithm(coins, maxMoney));
        }
    }

    public static void populateCoinArray(String[] coins, int[] array) {

        for(int i = 0; i < array.length-1; i++)
            array[i+1] = Integer.parseInt(coins[i]);
    }

    public static int coinRowAlgorithm(int[] coins, int[] money) {

        money[0] = 0;
        money[1] = coins[1];

        for(int i = 2; i < coins.length; i++) {
            money[i] = Math.max((coins[i] + money[i-2]), money[i-1]);
            System.out.printf("F[%4d] = max(%3d + %3d, %3d) = %3d\n", i, coins[i], money[i-2], money[i-1], money[i]);
        }

        return money[money.length-1];
    }

    public static void stressTest(int iterations) {

        int[] coins = new int[iterations+1];

        Random rand = new Random();

        for(int i = 0; i < coins.length; i++)
            coins[i] = rand.nextInt(50);

        int[] money = new int[iterations+1];

        System.out.println(coinRowAlgorithm(coins, money));
    }
}
