import java.util.Arrays;
import java.util.Random;

/**
 * Author - Tyler Wilding
 * Description - Empirical Analysis of several sorting functions.
 */
public class Sort {

    public static void main(String[] args) {

        int[] testArray = generateArray("",5000);

        System.out.println(Arrays.toString(testArray));

        System.out.println(Arrays.toString(selectionSort(testArray, true)));

        System.out.println(verifySorted(testArray));

        int[] array = {1,2,3,4,5,6,7,8,9,10};
        split(array);


    }

    /**
     * Performs an merge sort on a given array, will also calculate the execution time, number of swaps, comparisons and array accesses
     * @param uArray The unsorted array
     * @param count pass true if number of swaps, comparisons and array accesses are desired, else pass false.
     * @return The sorted array
     */
    public static int[] mergeSort(int[] uArray, boolean count) {

        return uArray;//wrong
    }

    public static int[] split(int[] mArr) {

        //Optimization Here
        if(mArr.length == 1)
            return mArr;

        int lengthArr = mArr.length/2;

        int[] aArr = new int[lengthArr];
        for(int i = 0; i < aArr.length; i++)
            aArr[i] = mArr[i];

        int[] bArr = new int[lengthArr];
        for(int i = 0; i < bArr.length; i++)
            bArr[i] = mArr[lengthArr+i];

        return merge(aArr, bArr);
    }

    public static int[] merge(int[] aArr, int[] bArr) {

        return null;
    }


    /**
     * Performs an insertion sort on a given array, will also calculate the execution time, number of swaps, comparisons and array accesses
     * @param uArray The unsorted int array
     * @param count pass true if number of swaps, comparisons and array accesses are desired, else pass false.
     * @return The sorted array
     */
    public static int[] selectionSort(int[] uArray, boolean count) {

        for(int i = 0; i < uArray.length; i++) {

            int smallestElement = uArray[i];
            int swapIndex = 0;

            for(int j = i; j < uArray.length; j++) {

                if(uArray[j] <= smallestElement) {
                    smallestElement = uArray[j];
                    swapIndex = j;
                }
            }

            uArray[swapIndex] = uArray[i];
            uArray[i] = smallestElement;

        }

        return uArray;
    }

    /**
     * Performs an insertion sort on a given array, will also calculate the execution time, number of swaps, comparisons and array accesses
     * @param uArray The unsorted int array
     * @param count pass true if numbere of swaps, comparisons and array accesses are desired, else pass false.
     * @return The sorted array.
     */
    public static int[] insertionSort(int[] uArray, boolean count) {

        for(int i = 1,j=i-1; i < uArray.length; i++,j=i-1) {

            int temp = uArray[i];

            while(j >= 0 && uArray[j] > temp) {

                uArray[j+1] = uArray[j];
                j--;
            }

            uArray[j+1] = temp;
        }

        return uArray;
    }

    public static boolean verifySorted(int[] sArray) {

        for(int i = 0; i < sArray.length-1; i++) {

            if(sArray[i] > sArray[i+1])
                return false;
        }
        return true;
    }

    /**
     * Generates and returns an int array filled with a specified amount of elements in a particle ordering scheme
     * @param args "sorted" for a sorted array in ascending order 1,2,3,4..."decreasing" for descending order, and anything else for random order.
     * @param size Number of elements in the array.
     * @return The generated array as an int[]
     */
    public static int[] generateArray(String args, int size) {

        int lowBound = size/2*-1;
        int highBound = size/2;

        if (args.equalsIgnoreCase("sorted") || args.equalsIgnoreCase("ascending")) {

            int[] array = new int[size];
            for(int i = 0; i < array.length; i++)
                array[i] = lowBound+i;
            return array;
        }
        else if(args.equalsIgnoreCase("decreasing") || args.equalsIgnoreCase("descending")){

            int[] array = new int[size];
            for(int i = 0; i < array.length; i++)
                array[i] = highBound-i;
            return array;
        }

        int[] array = new int[size];
        Random rand = new Random();
        for(int i = 0; i < array.length; i++)
            array[i] = rand.nextInt(size)-highBound;
        return array;
    }
}
