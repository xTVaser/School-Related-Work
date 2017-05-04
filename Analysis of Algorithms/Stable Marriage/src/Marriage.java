import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

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

        //Initialize the Men and the Women
        Man[] men = new Man[numberOfPeople];
        Woman[] women = new Woman[numberOfPeople];
        for(int i = 0; i < numberOfPeople; i++) {
            women[i] = new Woman();
            men[i] = new Man();
        }

        //Initialze their preferences by shuffling the array of men and women.
        for(int i = 0; i < men.length; i++)
            men[i].addPreferences((Woman[])shuffleArray(women));

        for(int i = 0; i < women.length; i++)
            women[i].addPreferences((Man[])shuffleArray(men));

        /* Testing Case
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

        //Output
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

    /**
     * Method that will print out the marriage connects after the algorithm has run.
     * @param men Array that holds all of the men, these have links to their spouses.
     */
    public static void printMatching(Man[] men) {

        System.out.println("-------=======Marriage Matching=======-------");

        for(int i = 0; i < men.length; i++)
            System.out.printf("%4d: %15s     <-> %15s\n",i+1,men[i].name,men[i].spouse.name);
    }

    /**
     * Method will print out all the mens and womens preferences if desired, before the algorithm gets called.
     * @param men Array of all of the men
     * @param women Array of all of the women
     */
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

    /**
     * Gale-Shapley algorithm for solving the stable marriage problem.
     * @param men List of men
     * @param women List of women
     */
    public static void galeShapley(Man[] men, Woman[] women) {

        //Start off by holding all of the unmarried men.
        ArrayList<Man> unmarriedMen = new ArrayList<Man>();
        //Originally all men are unmarried.
        for(int i = 0; i < men.length; i++)
            unmarriedMen.add(men[i]);

        //We loop until we no longer have unmarried men.
        while(unmarriedMen.isEmpty() == false) {

            //For each man
            for(int i = 0; i < unmarriedMen.size(); i++) {
                //Propose to their best highest choice.
                Woman wifeMaterial = unmarriedMen.get(i).preferences.get(0);
                //And add the men to the woman's list of suitors
                wifeMaterial.suitors.add(unmarriedMen.get(i));
                //Remove the woman from the man's preference list as this is his ONLY CHANCE
                unmarriedMen.get(i).preferences.remove(0);
                //The man is now "married" to the woman unless she rejects him now or later on.
                unmarriedMen.get(i).spouse = wifeMaterial;
            }

            //After all men have made their proposals, the women pick the best one and reject the rest.
            for(int i = 0; i < women.length; i++) {
                unmarriedMen = women[i].selectSpouse(unmarriedMen);
            }
        }
    }

    /**
     * Fisher Yates shuffle, will randomly pull an element out of a "hat" to shuffle the array until all elements have been picked.
     * @param array The array we want shuffled, generic.
     * @return The array.
     */
    public static Object[] shuffleArray(Object[] array) {

        Random rand = new Random();
        for (int i = 0; i < array.length; i++) {

            int index = rand.nextInt(array.length-i);

            //Swap
            Object item = array[index];
            array[index] = array[i];
            array[i] = item;
        }
        return array;
    }
}
