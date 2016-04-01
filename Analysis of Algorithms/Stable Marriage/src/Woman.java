import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Tyler Wilding on 31/03/16.
 * Stable Marriage - COSC
 */
public class Woman {

    public Man spouse;
    public ArrayList<Man> suitors = new ArrayList<Man>();
    public ArrayList<Man> preferences = new ArrayList<Man>();
    public String name;

    /**
     * Initialize the woman, give her a random name.
     */
    public Woman() {

        String inputString = null;
        try {
            inputString = new String(Files.readAllBytes(Paths.get("female.txt")));
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }

        String[] inputArray = inputString.split("\n");

        Random rand = new Random();
        String[] line = inputArray[rand.nextInt(inputArray.length)].split("\\s");
        name = line[0];

        name = name.substring(0,1)+name.substring(1).toLowerCase();
    }

    /**
     * Used to convert an array of Men into the ArrayList we have associated to the object.
     * @param array Array of Men.
     */
    public void addPreferences(Man[] array) {

        for(int i = 0; i < array.length; i++)
            preferences.add(array[i]);
    }

    /**
     * Will go through the suitor ArrayList and pick the best one, potentially replacing current spouse.
     * @param unmarriedMen ArrayList of men that have yet to be married.
     * @return The updated ArrayList of married men.
     */
    public ArrayList<Man> selectSpouse(ArrayList<Man> unmarriedMen) {

        //If we have no suitors, we have nothing to change.
        if(suitors.size() == 0) {
            return unmarriedMen;
        }

        int bestIndex = Integer.MAX_VALUE;
        for(int i = 0; i < suitors.size(); i++) {

            //We want to find the suitor that is closest to '0' in the woman's preferences array.
            if(preferences.indexOf(suitors.get(0)) < bestIndex)
                bestIndex = preferences.indexOf(suitors.get(0));
        }

        //If we have no current spouse, then just make him our spouse and remove him from the unmarried array.
        if(spouse == null) {

            spouse = preferences.get(bestIndex);
            spouse.spouse = this;
            unmarriedMen.remove(spouse);
        }

        //Otherwise, we need to see if this is a better husband.
        else {
            //Is the suitor better than the current husband
            int newHusband = Math.min(preferences.indexOf(spouse), bestIndex);

            if(newHusband == bestIndex) { //Found new husband

                //The old husband gets re-added back to the unmarried array, and we marry the new one.
                spouse.spouse = null;
                unmarriedMen.add(spouse);
                spouse = preferences.get(newHusband);
                unmarriedMen.remove(spouse);
                spouse.spouse = this;
            }
        }

        //Wipe the suitors array, and exit.
        suitors = new ArrayList<Man>();
        return unmarriedMen;
    }
}
