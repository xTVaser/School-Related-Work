import java.util.Arrays;
import java.util.Random;

/**
 * Author - Tyler Wilding
 * Description - Empirical Analysis of several sorting functions.
 * TODO -   Heap Sort
 *          Calculate Execution Times
 *          Implement Swap and Comparison Counting
 *          Hoare Quicksort
 *          Radix Sort
 */
public class Sort {

    public static void main(String[] args) {

        //int[] array = generateArray("",5000);
        int[] array = {12,11,10,9,7,6};

        System.out.println(Arrays.toString(array));

        array = quickSort(array, 0, array.length-1);

        System.out.println(Arrays.toString(array));

        System.out.println(verifySorted(array));


    }

    /**
     * Quicksort Method
     * @param uArr Full unsorted array
     * @param lowBound Beginning of the partion (assuming 0 based)
     * @param highBound End of the partiton (for example length-1)
     * @return The sorted array.
     */
    public static int[] quickSort(int[] uArr, int lowBound, int highBound) {

        if(lowBound < highBound) {

            int pivot = partitionArray(uArr, lowBound, highBound);

            quickSort(uArr, lowBound, pivot-1);
            quickSort(uArr, pivot+1, highBound);
        }

        return uArr;
    }

    /**
     * Quicksort partition scheme using Lomento's Pivot Selection
     * @param arr Full unsorted array
     * @param lowBound Beginning of the partition (assuming 0 based)
     * @param highBound End of the partition (for example length-1)
     * @return The new pivot's position
     */
    public static int partitionArray(int[] arr, int lowBound, int highBound) {

        int pivotElement = arr[highBound];

        int i = lowBound-1;

        for(int j = lowBound; j < highBound; j++) {

            if(arr[j] <= pivotElement) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        i++;
        int temp = arr[i];
        arr[i] = arr[highBound];
        arr[highBound] = temp;

        return i;
    }



    /**
     * Performs an merge sort on a given array, will also calculate the execution time, number of swaps, comparisons and array accesses
     * @param mArr The unsorted array
     * @param count pass true if number of swaps, comparisons and array accesses are desired, else pass false.
     * @return The sorted array
     */
    public static int[] mergeSort(int[] mArr, boolean count) {

        if(mArr.length <= 1)
            return mArr;

        int[] aArr = new int[mArr.length/2];
        int[] bArr = new int[(int)Math.ceil(mArr.length/2.0)];

        for(int i = 0; i < mArr.length/2; i++)
            aArr[i] = mArr[i];

        for(int i = mArr.length/2, j = 0; i < mArr.length; i++, j++)
            bArr[j] = mArr[i];

        aArr = mergeSort(aArr, count);
        bArr = mergeSort(bArr, count);

        return merge(aArr, bArr, count);
    }

    /**
     * Merges 2 unsorted integers arrays into a combined sorted array
     * @param aArr First array
     * @param bArr Second array
     * @param count If swaps/comparisons should be calculated
     * @return Combined sorted array.
     */
    public static int[] merge(int[] aArr, int[] bArr, boolean count) {

        if(aArr.length == 2 && aArr[0] > aArr[1]) {
            int temp = aArr[1];
            aArr[1] = aArr[0];
            aArr[0] = temp;
        }

        if(bArr.length == 2 && bArr[0] > bArr[1]) {
            int temp = bArr[1];
            bArr[1] = bArr[0];
            bArr[0] = temp;
        }

        int[] cArr = new int[aArr.length+bArr.length];

        int aIndex = 0;
        int bIndex = 0;
        int cIndex = 0;

        while(aIndex < aArr.length && bIndex < bArr.length) {

            if(aArr[aIndex] < bArr[bIndex])
                cArr[cIndex++] = aArr[aIndex++];
            else
                cArr[cIndex++] = bArr[bIndex++];
        }

        while(aIndex < aArr.length)
            cArr[cIndex++] = aArr[aIndex++];

        while(bIndex < bArr.length)
            cArr[cIndex++] = bArr[bIndex++];

        return cArr;
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

    /**
     * Quick method to verify that the array is sorted, useful for testing.
     * @param sArray Potentially sorted integer array
     * @return True or false if sorted.
     */
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
