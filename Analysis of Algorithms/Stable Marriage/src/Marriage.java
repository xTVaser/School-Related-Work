import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Tyler Wilding on 30/03/16.
 * Stable Marriage - COSC
 *
 * Description - Will perform the Gale-Shapley algorithm to find a stable set of marriage partners based on their preferences.
 * Stable marriage means that after the algorithm is completed there is no way that someone would prefer someone else over their existing partner.
 */
public class Marriage {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.println("How many Men and Women do you Want Created? (More than 0): ");
        int numberOfPeople = in.nextInt();

        //10 Men and 10 Women and their preferences.
        Man[] men = new Man[numberOfPeople];
        Woman[] women = new Woman[numberOfPeople];

        for(int i = 0; i < numberOfPeople; i++) {
            women[i] = new Woman();
            men[i] = new Man();
        }

        for(int i = 0; i < men.length; i++)
            men[i].addPreferences((Woman[])shuffleArray(women));

        for(int i = 0; i < women.length; i++)
            women[i].addPreferences((Man[])shuffleArray(men));

        /* Testing Cases
        Woman[] pref1 = {women[2], women[1], women[0]};
        Woman[] pref2 = {women[2], women[1], women[0]};
        Woman[] pref3 = {women[1], women[2], women[0]};
        men[0].addPreferences(pref1);
        men[1].addPreferences(pref2);
        men[2].addPreferences(pref3);

        Man[] preff1 = {men[2], men[0], men[1]};
        Man[] preff2 = {men[1], men[0], men[2]};
        Man[] preff3 = {men[0], men[2], men[1]};
        women[0].addPreferences(preff1);
        women[1].addPreferences(preff2);
        women[2].addPreferences(preff3);
        */

        System.out.println("People Generated!");
        System.out.println("Would you like to view their preferences? (y/n): ");
        String prompt = in.next();

        if(prompt.equalsIgnoreCase("y"))
            printPreferences(men, women);

        System.out.println();
        System.out.println("Running algorithm...");
        galeShapley(men, women);
        System.out.println("Algorithm Complete!");
        System.out.println();

        System.out.println("Would you like to view the marriages? (y/n): ");
        prompt = in.next();

        if(prompt.equalsIgnoreCase("y"))
            printMatching(men);
    }

    public static void printMatching(Man[] men) {

        System.out.println("-------=======Marriage Matching=======-------");

        for(int i = 0; i < men.length; i++)
            System.out.printf("%4d: %15s     <-> %15s\n",i+1,men[i].name,men[i].spouse.name);

    }

    public static void printPreferences(Man[] men, Woman[] women) {

        System.out.println("-------=======Men's Preferences=======-------");

        for(int i = 0; i < men.length; i++) {

            System.out.printf("%4d: %10s - ",i+1,men[i].name);
            for(int j = 0; j < men[i].preferences.size(); j++) {

                System.out.printf("%10s > ",men[i].preferences.get(j).name);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("-------=======Women's Preferences======------");

        for(int i = 0; i < women.length; i++) {

            System.out.printf("%4d: %10s - ",i+1,women[i].name);
            for(int j = 0; j < women[i].preferences.size(); j++) {

                System.out.printf("%10s > ",women[i].preferences.get(j).name);
            }
            System.out.println();
        }
    }
    public static void galeShapley(Man[] men, Woman[] women) {

        ArrayList<Man> unmarriedMen = new ArrayList<Man>();

        //Originally all men are unmarried.
        for(int i = 0; i < men.length; i++)
            unmarriedMen.add(men[i]);

        while(unmarriedMen.isEmpty() == false) {

            for(int i = 0; i < unmarriedMen.size(); i++) {
                Woman wifeMaterial = unmarriedMen.get(i).preferences.get(0);
                wifeMaterial.suitors.add(unmarriedMen.get(i));
                unmarriedMen.get(i).preferences.remove(0);
                unmarriedMen.get(i).spouse = wifeMaterial;
            }

            for(int i = 0; i < women.length; i++) {
                unmarriedMen = women[i].selectSpouse(unmarriedMen);
            }
        }
    }

    // Implementing Fisher–Yates shuffle
    static Object[] shuffleArray(Object[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Object a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        return ar;
    }
}
