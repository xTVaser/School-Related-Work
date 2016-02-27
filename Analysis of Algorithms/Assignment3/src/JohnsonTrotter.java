import java.util.ArrayList;

/**
 * Created by tyler on 26/02/16.
 */
public class JohnsonTrotter {

    //Add all elements, with a direction to the left
    //Always move the larger mobile element, an element is mobile if in its direction there is a smaller item.
        //Not mobile if the above condition doesnt hold true, or if it is facing left on the left side, or facing right on the right side.
    //When a swap occurs, all elements greater must have their direction inverted, this can be skipped if it is the larger element.
    //Stop when no mobile elements exist.

    static ArrayList<String> permutations = new ArrayList<String>();

    public static void main(String[] args) {

        Element[] elements = new Element[4];

        elements[0] = new Element(1);
        elements[1] = new Element(2);
        elements[2] = new Element(3);
        elements[3] = new Element(4);

        johnsonTrotter(elements);

        System.out.println("Number of Permutations: "+permutations.size()+"\n");
        for(int i = 0; i < permutations.size(); i++) {
            System.out.print(i+") ");
            System.out.println(permutations.get(i));
        }
    }

    public static void johnsonTrotter(Element[] elements) {

        updateMobility(elements);

        int max = findMax(elements);
        int i = findBiggestMobile(elements);

        addPermutation(elements);

        while(i != -1) {

            //We found the biggest mobile elment, swap it in the direction its facing.
                //Update the direction, if not the largest element in the array

            int lastMoved = elements[i].item;

            swap(elements, i, i+elements[i].direction);

            if(lastMoved != max)
                updateDirection(elements, lastMoved);

            updateMobility(elements);
            addPermutation(elements);

            i = findBiggestMobile(elements);
        }
    }

    public static void addPermutation(Element[] elements) {

        String permutation = "";

        for(int i = 0; i < elements.length; i++) {

            permutation += elements[i].item + " ";
        }

        permutations.add(permutation);
    }

    public static void swap(Element[] elements, int i, int j) {

        Element temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
    }

    public static int findMax(Element[] array) {

        int max = array[0].item;
        for(int i = 0; i < array.length; i++) {

            if(array[i].item > max)
                max = array[i].item;
        }
        return max;
    }

    public static void updateMobility(Element[] array) {

        for(int i = 0; i < array.length; i++) {

            if(i == 0 && array[i].direction == -1)
                array[i].mobile = false;

            else if(i == array.length-1 && array[i].direction == 1)
                array[i].mobile = false;

            else if(array[i+array[i].direction].item > array[i].item)
                array[i].mobile = false;

            else
                array[i].mobile = true;
        }
    }

    public static int findBiggestMobile(Element[] array) {

        int biggestItem = Integer.MIN_VALUE;
        int biggestIndex = -1;
        for(int i = 0; i < array.length; i++) {

            if(array[i].item > biggestItem && array[i].mobile) {
                biggestIndex = i;
                biggestItem = array[i].item;
            }
        }

        return biggestIndex;
    }

    public static void updateDirection(Element[] array, int lastMoved) {

        for(int i = 0; i < array.length; i++) {

            if(array[i].item > lastMoved)
                array[i].direction *= -1;
        }
    }
}
