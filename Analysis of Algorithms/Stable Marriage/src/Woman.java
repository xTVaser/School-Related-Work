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
    public void addPreferences(Man[] array) {

        for(int i = 0; i < array.length; i++)
            preferences.add(array[i]);
    }

    public ArrayList<Man> selectSpouse(ArrayList<Man> unmarriedMen) {

        if(suitors.size() == 0) {
            return unmarriedMen;
        }

        int bestIndex = Integer.MAX_VALUE;

        for(int i = 0; i < suitors.size(); i++) {

            if(preferences.indexOf(suitors.get(0)) < bestIndex)
                bestIndex = preferences.indexOf(suitors.get(0));
        }

        if(spouse == null) {

            spouse = preferences.get(bestIndex);
            spouse.spouse = this;
            unmarriedMen.remove(spouse);
        }

        else {
            //Is the suitor better than the current husband
            int newHusband = Math.min(preferences.indexOf(spouse), bestIndex);

            if(newHusband == bestIndex) { //Found new husband

                spouse.spouse = null;
                unmarriedMen.add(spouse);
                spouse = preferences.get(newHusband);
                unmarriedMen.remove(spouse);
                spouse.spouse = this;
            }
        }

        suitors = new ArrayList<Man>();
        return unmarriedMen;
    }
}
