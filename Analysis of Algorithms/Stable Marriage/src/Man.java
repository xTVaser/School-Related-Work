import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Tyler Wilding on 31/03/16.
 * Stable Marriage - COSC
 */
public class Man {

    public Woman spouse;
    public ArrayList<Woman> preferences = new ArrayList<Woman>();
    public String name;

    /**
     * Initializes the Man with a random name.
     */
    public Man() {

        String inputString = null;
        try {
            inputString = new String(Files.readAllBytes(Paths.get("male.txt")));
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
     * Converts a Woman array into the ArrayList of woman we use now.
     * @param array Array of Women
     */
    public void addPreferences(Woman[] array) {

        for(int i = 0; i < array.length; i++)
            preferences.add(array[i]);
    }
}

