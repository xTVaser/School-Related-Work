import java.util.ArrayList;
import java.util.Scanner;

/**
 * Author - Tyler Wilding
 * Description - Finds the permutations of an n digit number using the Johnson-Trotter Algorithm
 *
 * Basic Algorithm:
 * Add all elements, set the intial directions to left.
 * Always move the largest mobile element, an element is mobile if it is facing a smaller item.
 *  It is not mobile if the above condition doesnt hold true, or if it is facing left at the beginning of the array, or similar for the right.
 * When a swap occurs, all elements greater must have their direction inverted, this can be skipped if we swap the largest element.
 * Stop when no mobile elements exist.
 */
public class JohnsonTrotter {

    static ArrayList<String> permutations = new ArrayList<String>();

    public static void main(String[] args) {

        //Get user input as a string so we can easily get individual digits.
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter an integer: ");
        String number = Integer.toString(scanner.nextInt());

        Element[] elements = new Element[number.length()];

        //Add said digits to the array
        for(int i = 0; i < number.length(); i++) {

            elements[i] = new Element(Integer.parseInt(number.charAt(i)+""));
        }

        //Call the algorithm function
        johnsonTrotter(elements);

        //Print out the permutations
        System.out.println("Number of Permutations: "+permutations.size()+"\n");
        for(int i = 0; i < permutations.size(); i++) {
            System.out.print(i+1+") ");
            System.out.println(permutations.get(i));
        }
    }

    /**
     * Main algorithm function to calculate the permutations.
     * @param elements Array with all of the digits, holds the current permutation.
     */
    public static void johnsonTrotter(Element[] elements) {

        //First we have to update the item's mobility since they were just added
        updateMobility(elements);

        int max = findMax(elements);                //Small optimization to save updateDirection calls.
        int i = findBiggestMobile(elements);        //Index of the item we need to swap with, initial call.

        //Add the first permutation, the items unmoved.
        addPermutation(elements);

        while(i != -1) { //While we have a mobile item

            //Store the item we are about to move
            int lastMoved = elements[i].item;

            //Swap the item itself, and the item that it is facing
            swap(elements, i, i+elements[i].direction);

            //If the item we moved was not the largest, then we need to update the directions of all elements larger
            if(lastMoved != max)
                updateDirection(elements, lastMoved);

            //Update the mobility after the swap and add the permutation
            updateMobility(elements);
            addPermutation(elements);

            //Find the new biggest mobile element.
            i = findBiggestMobile(elements);
        }
    }

    /**
     * Simple method to take all elements in the array and convert it to a string
     * @param elements The array with all of the items
     */
    public static void addPermutation(Element[] elements) {

        String permutation = "";

        for(int i = 0; i < elements.length; i++)
            permutation += elements[i].item + " ";

        permutations.add(permutation);
    }

    /**
     * Simple swap method
     * @param elements Item array
     * @param i First element's index
     * @param j Second element's index
     */
    public static void swap(Element[] elements, int i, int j) {

        Element temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
    }

    /**
     * Simple method that goes through the array to find the biggest element, used once.
     * @param array Element array
     * @return The largest digit as an int.
     */
    public static int findMax(Element[] array) {

        int max = array[0].item;
        for(int i = 0; i < array.length; i++) {

            if(array[i].item > max)
                max = array[i].item;
        }
        return max;
    }

    /**
     * Important method for the algorithm, will go through the array and determine if an element is mobile or not.
     * @param array
     */
    public static void updateMobility(Element[] array) {

        for(int i = 0; i < array.length; i++) {

            if(i == 0 && array[i].direction == -1) //If we are on the far left, and we are facing the left
                array[i].mobile = false;

            else if(i == array.length-1 && array[i].direction == 1) //If we are on the far right and facing the right
                array[i].mobile = false;

            else if(array[i+array[i].direction].item > array[i].item) //If we are in the middle but facing a larger item
                array[i].mobile = false;

            else //Else it is mobile
                array[i].mobile = true;
        }
    }

    /**
     * Important method for the algorithm, passes through the array and finds the index of the largest mobile item
     * @param array Item array
     * @return The index of the largest mobile item, returns -1 if nothing found.
     */
    public static int findBiggestMobile(Element[] array) {

        int biggestItem = Integer.MIN_VALUE;
        int biggestIndex = -1;
        for(int i = 0; i < array.length; i++) {

            //If we find an element that is larger, and it is also mobile
            if(array[i].item > biggestItem && array[i].mobile) {
                biggestIndex = i;
                biggestItem = array[i].item;
            }
        }

        return biggestIndex;
    }

    /**
     * Simple method that inverts all of the directions greater than the last moved item
     * @param array Element array
     * @param lastMoved Integer of the last moved digit
     */
    public static void updateDirection(Element[] array, int lastMoved) {

        for(int i = 0; i < array.length; i++) {

            //If we find an element bigger than the last moved item, invert it's direction
            if(array[i].item > lastMoved)
                array[i].direction *= -1;
        }
    }
}
