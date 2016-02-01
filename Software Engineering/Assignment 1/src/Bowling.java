import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author - Tyler Wilding
 * Program - Bowling Score
 * Description - Keeps track of the players score while playing 10 pin bowling.
 */
public class Bowling {

    //Variables
    public static int[] frameScore;
    public static int[] bonusCount;
    public static int[] pins;
    public static int frameNumber;
    public static int throwCounter;
    public static boolean finalFrame;

    /**
     * Bowling Construction, resets the variables for the bowling object.
     */
    public Bowling() {
        frameScore = new int[10];
        bonusCount = new int[10];
        pins = new int[21];
        throwCounter = 0;
        finalFrame = false;
        frameNumber = 0;
    }

    /**
     * Main method that runs through 10 frame inputs with a scanner.
     * @param args Command line arguments not used.
     */
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        while(frameNumber < 9) { //Loop 9 times for 9 frames, 10th time is outside the loop as it is slightly different.

            frame(scan.next(), scan.next(), null);
        }
        frame(scan.next(), scan.next(), scan.next());

        System.out.println(totalScore());
    }

    /**
     * Accepts one input for the frame, would be used for a strike for example.
     * @param pinsOne First throw's details.
     */
    public static void bowl(String pinsOne) {
        frame(pinsOne, "0", null);
    }

    /**
     * Accepts two inputs for the frame, can be used for normal hits or a spare.
     * @param pinsOne First throw's details
     * @param pinsTwo Second throw's details
     */
    public static void bowl(String pinsOne, String pinsTwo) {
        frame(pinsOne, pinsTwo, null);
    }

    /**
     * Accepts three inputs for the frame, used for the final frame, where a bonus throw is possible.
     * @param pinsOne First throw's details
     * @param pinsTwo Second throw's details
     * @param pinsThree Third throw's details
     */
    public static void bowl(String pinsOne, String pinsTwo, String pinsThree) {
        frame(pinsOne, pinsTwo, pinsThree);
    }

    /**
     * Processes the frame's input to enter into the datastructures
     * @param pinsOne First throw's details
     * @param pinsTwo Second throw's details
     * @param bonusMove Bonus throw's details
     */
    public static void frame(String pinsOne, String pinsTwo, String bonusMove) {

        //Variable is used to keep track of the previous shot's pin count, calculate for spare.
        int previousShot = 0;

        if(!checkUserInput(pinsOne)) //Verify user input
            return;

        int testKnocked = Integer.parseInt(pinsOne);
        if(!updateScore(testKnocked, 0, throwCounter)) //Update score, will also verify if it was a valid input.
            return;

        //Update array, print score as of so far
        pins[throwCounter++] = testKnocked;
        previousShot = testKnocked;
        printScore();

        if(frameNumber == 9) //If we are on the last frame, use the marker variable.
            finalFrame = true;


        if(testKnocked == 10 && !finalFrame) { //If we hit a strike not on the final frame, skip the second input.
            frameNumber++;
            throwCounter++;
            return;
        }

        //Repeat for the second throw
        if(!checkUserInput(pinsTwo))
            return;

        testKnocked = Integer.parseInt(pinsTwo);
        if(!updateScore(testKnocked, previousShot, throwCounter))
            return;

        pins[throwCounter++] = testKnocked;
        previousShot = testKnocked;
        printScore();

        if(finalFrame) { //If we are on the final frame, it is a special case

            if(pins[18] + pins[19] == 10) //If we hit a spare, mark it so.
                bonusCount[9] = 1;

            if(bonusCount[9] == 1 || bonusCount[9] == 2) { //If we hit a spare or a strike, we get an extra throw.
                if(!checkUserInput(bonusMove))
                    return;

                testKnocked = Integer.parseInt(bonusMove);
                if(!updateScore(testKnocked, 0, throwCounter))
                    return;

                pins[throwCounter++] = testKnocked;
                printScore();
            }
        }

        //Move to the next frame.
        frameNumber++;
    }

    /**
     * Computes the total score from all of the frames.
     * @return The total score as of so far.
     */
    public static int totalScore() {

        int totalScore = 0;
        for(int i = 0; i < frameScore.length; i++) {
            totalScore += frameScore[i];
        }
        return totalScore;
    }

    /**
     * Computes the runningTotal up to the current frame to be output at that frame.
     * @param length How many frames to add up.
     * @return Total up to that frame
     */
    public static int runningTotal(int length) {

        int runningTotal = 0;
        for(int i = 0; i <= length; i++)
            runningTotal += frameScore[i];
        return runningTotal;
    }

    /**
     * Update score method that checks if the throw input is valid, and deals with spares and strikes.
     * @param knocked The amount of pins knocked down.
     * @param previousShot The amount of pins knocked down in the previous shot.
     * @param throwCounter What throw out of 21 is it.
     * @return Will return -1 if error.
     */
    public static boolean updateScore(int knocked, int previousShot, int throwCounter) {

        if(frameNumber != 9 && previousShot + knocked > 10) { //If we are not on the final frame, and we hit more than 10, wrong.
            System.out.println("Cant hit more than 10 points in a frame");
            return false;
        }

        if(!finalFrame && knocked == 10) //Strike
            bonusCount[frameNumber] = 2;

        else if(!finalFrame && previousShot + knocked == 10) //Spare
            bonusCount[frameNumber] = 1;

        for(int i = 0; i < frameNumber; i++) { //Iterate through bonus array, consecutive throws will add to the previous frames.

            if(bonusCount[i] > 0) {
                bonusCount[i]--;
                frameScore[i] += knocked;
            }
        }

        //Add the pins knocked down to the score card.
        frameScore[frameNumber] += knocked;
        return true;
    }

    /**
     * Print scorecard method, loops through and prints.
     */
    public static void printScore() {
        System.out.println();
        int throwCounter = 0;
        for(int i = 0; i < frameNumber; i++){
            System.out.print("| "+runningTotal(i)+" |"+pins[throwCounter++]+"|"+pins[throwCounter++]+"|");
        }
        System.out.print("| "+runningTotal(9)+" |"+pins[throwCounter++]+"|"+pins[throwCounter++]+"|"+pins[throwCounter++]+"||");
        System.out.println();
    }

    /**
     * Check user input to see if it is not a letter or more than 10.
     * @param input Users input as a string.
     * @return True or False
     */
    public static boolean checkUserInput(String input) {
        //Define pattern, everything that is not 0-9
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(input);
        if(m.find()) { //If the input contains anything besides 0-9, false.
            System.out.println("Only enter numeric values!");
            return false;
        }

        //If the input is a number but less than 0 or greater than 10, remove.
        if(Integer.parseInt(input) > 10 || Integer.parseInt(input) < 0) {
            System.out.println("Only accepts values between 0 and 10, inclusive!");
            return false;
        }

        return true;
    }
}