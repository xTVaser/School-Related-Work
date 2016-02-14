import java.util.Arrays;
import java.util.Random;

/**
 * Author - Tyler Wilding
 * Description - Empirical Analysis of several sorting functions.
 * TODO -
 *          Calculate Execution Times
 *          Implement Swap and Comparison Counting
 *          Hoare Quicksort
 *          Radix Sort
 */
public class Sort {

    public static long swaps = 0;
    public static long compares = 0;
    public static long accesses = 0;
    public static long recursions = 0;
    public static double time = 0;

    public static void main(String[] args) {

        int[] array = generateArray("",100000);
        //int[] array = {7,8,4,5,7,8,1,2};

        array = insertionSort(array, true);

        System.out.println(verifySorted(array));

        testAlgorithms("", 100, 10);


    }

    public static void testAlgorithms(String arrayType, int arraySize, int iterations) {

        long swapAvg = 0;
        long compareAvg = 0;
        long accessAvg = 0;
        long recursionAvg = 0;
        double timeAvg = 0;

        System.out.println("Test - Array Type: "+arrayType+" Array Size: "+arraySize+" Sample Size: "+iterations);

        for(int i = 0; i < iterations; i++) {

            int[] array = generateArray(arrayType, arraySize);
            array = heapSort(array, true);

            if(verifySorted(array)) {

                swapAvg += swaps;
                compareAvg += compares;
                accessAvg += accesses;
                recursionAvg += recursions;
            }
            else {

                System.out.println("Heap Sort Failed!");
            }
        }

        System.out.println("Test - ");;


    }

    /**
     * Mai heap sort function.
     * @param uArr Unsorted array that is a heap during sorting.
     * @param count Whether or not to count comparisons and swaps.
     * @return The sorted array.
     */
    public static int[] heapSort(int[] uArr, boolean count) {

        if(count) {
            long startTime = System.nanoTime();

            int numElements = uArr.length - 1;

            for(int i = numElements/2; i >= 0; i--)
                heapify(uArr, i, numElements, count);

            for(int i = numElements; i > 0; i--) {

                int temp = uArr[0];
                uArr[0] = uArr[i];
                uArr[i] = temp;
                accesses+=3;
                swaps++;

                numElements--;
                heapify(uArr, 0, numElements, count);
            }
            time = (double)(System.nanoTime()-startTime)/1000000;
            System.out.printf("Heap Sort completed in: %.3f ms\n",time);
            System.out.println("Heap Sort swaps: "+swaps);
            System.out.println("Heap Sort comparisons: "+compares);
            System.out.println("Heap Sort array accesses: "+accesses);
            System.out.println("Heap Sort recursive calls: "+recursions);

            return uArr;
        }

        else {
            long startTime = System.nanoTime();

            int numElements = uArr.length - 1;

            for(int i = numElements/2; i >= 0; i--)
                heapify(uArr, i, numElements, count);

            for(int i = numElements; i > 0; i--) {

                int temp = uArr[0];
                uArr[0] = uArr[i];
                uArr[i] = temp;

                numElements--;
                heapify(uArr, 0, numElements, count);
            }
            System.out.println("Heap Sort completed in: "+(System.nanoTime()-startTime)/1000000+"ms");

            return uArr;
        }
    }

    /**
     * Rebuilds the array back into a max heap.
     * @param uArr Unsorted Array, now a heap.
     * @param i Current root node we are looking at
     * @param numElements Total elements that are not sorted in the array
     * @return Heap as an integer array.
     */
    public static int[] heapify(int[] uArr, int i, int numElements, boolean count) {

        if(count) {

            int leftChild = i * 2;
            int rightChild = i * 2 + 1;
            int root = i;

            if(leftChild <= numElements) {

                if(uArr[leftChild] > uArr[root])
                    root = leftChild;
                accesses+=2;
                compares++;
            }

            if(rightChild <= numElements) {

                if(uArr[rightChild] > uArr[root])
                    root = rightChild;
                accesses+=2;
                compares++;
            }

            if(root != i) {
                int temp = uArr[i];
                uArr[i] = uArr[root];
                uArr[root] = temp;
                accesses+=4;
                swaps++;
                recursions++;
                heapify(uArr, root, numElements, count);
            }

            return uArr;
        }

        else {

            int leftChild = i * 2;
            int rightChild = i * 2 + 1;
            int root = i;

            if(leftChild <= numElements && uArr[leftChild] > uArr[root])
                root = leftChild;
            if(rightChild <= numElements && uArr[rightChild] > uArr[root])
                root = rightChild;
            if(root != i) {
                int temp = uArr[i];
                uArr[i] = uArr[root];
                uArr[root] = temp;
                heapify(uArr, root, numElements, count);
            }

            return uArr;
        }

    }

    /**
     * Quick method to easily call quicksort and collect empirical analysis on it.
     * @param uArr Unsorted Array
     * @param count Whether or not to count comparisons/swaps/accesses
     * @return Sorted integer array.
     */
    public static int[] quickSortHelper(int[] uArr, boolean count) {

        long startTime = System.nanoTime();

        quickSort(uArr, 0, uArr.length-1, count);

        time = (double)(System.nanoTime()-startTime)/1000000;

        System.out.printf("Quicksort completed in: %.3f ms\n",time);
        System.out.println("Quicksort swaps: "+swaps);
        System.out.println("Quicksort comparisons: "+compares);
        System.out.println("Quicksort array accesses: "+accesses);
        System.out.println("Quicksort recursive calls: "+recursions);

        return uArr;
    }

    /**
     * Quicksort Method
     * @param uArr Full unsorted array
     * @param lowBound Beginning of the partion (assuming 0 based)
     * @param highBound End of the partiton (for example length-1)
     * @return The sorted array.
     */
    public static int[] quickSort(int[] uArr, int lowBound, int highBound, boolean count) {

        if(count) {

            if(lowBound < highBound) {

                int pivot = partitionArray(uArr, lowBound, highBound, count);
                recursions+=2;
                quickSort(uArr, lowBound, pivot-1, count);
                quickSort(uArr, pivot+1, highBound, count);
            }

            return uArr;
        }

        else {

            if(lowBound < highBound) {

                int pivot = partitionArray(uArr, lowBound, highBound, count);

                quickSort(uArr, lowBound, pivot-1, count);
                quickSort(uArr, pivot+1, highBound, count);
            }

            return uArr;
        }
    }

    /**
     * Quicksort partition scheme using Lomento's Pivot Selection
     * @param arr Full unsorted array
     * @param lowBound Beginning of the partition (assuming 0 based)
     * @param highBound End of the partition (for example length-1)
     * @return The new pivot's position
     */
    public static int partitionArray(int[] arr, int lowBound, int highBound, boolean count) {

        if(count) {
            int pivotElement = arr[highBound];
            accesses++;

            int i = lowBound-1;

            for(int j = lowBound; j < highBound; j++) {

                if(arr[j] <= pivotElement) {
                    i++;
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                    swaps++;
                    accesses+=4;
                }
                accesses++;
                compares++;
            }

            i++;
            int temp = arr[i];
            arr[i] = arr[highBound];
            arr[highBound] = temp;
            swaps++;
            accesses+=4;

            return i;
        }

        else {

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

    }

    /**
     * Quick method to easily call mergesort and collect empirical analysis on it.
     * @param uArr Unsorted Array
     * @param count Whether or not to count comparisons/swaps/accesses
     * @return Sorted integer array.
     */
    public static int[] mergeSortHelper(int[] uArr, boolean count) {

        long startTime = System.nanoTime();

        mergeSort(uArr, count);

        time = (double)(System.nanoTime()-startTime)/1000000;

        System.out.printf("Merge Sort completed in: %.3f ms\n",time);
        System.out.println("Merge Sort swaps: "+swaps);
        System.out.println("Merge Sort comparisons: "+compares);
        System.out.println("Merge Sort array accesses: "+accesses);
        System.out.println("Merge Sort recursive calls: "+recursions);

        return uArr;
    }

    /**
     * Performs an merge sort on a given array, will also calculate the execution time, number of swaps, comparisons and array accesses
     * @param mArr The unsorted array
     * @param count pass true if number of swaps, comparisons and array accesses are desired, else pass false.
     * @return The sorted array
     */
    public static int[] mergeSort(int[] mArr, boolean count) {

        if(count) {

            if(mArr.length <= 1)
                return mArr;

            int[] aArr = new int[mArr.length/2];
            int[] bArr = new int[(int)Math.ceil(mArr.length/2.0)];

            for(int i = 0; i < mArr.length/2; i++) {
                aArr[i] = mArr[i];
                accesses++;
            }

            for(int i = mArr.length/2, j = 0; i < mArr.length; i++, j++) {
                bArr[j] = mArr[i];
                accesses++;
            }

            recursions+=2;
            aArr = mergeSort(aArr, count);
            bArr = mergeSort(bArr, count);

            return merge(aArr, bArr, count);
        }

        else {

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
    }

    /**
     * Merges 2 unsorted integers arrays into a combined sorted array
     * @param aArr First array
     * @param bArr Second array
     * @param count If swaps/comparisons should be calculated
     * @return Combined sorted array.
     */
    public static int[] merge(int[] aArr, int[] bArr, boolean count) {

        if(count) {

            if(aArr.length == 2) {

                if(aArr[0] > aArr[1]) {

                    int temp = aArr[1];
                    aArr[1] = aArr[0];
                    aArr[0] = temp;
                    swaps++;
                    accesses+=4;
                }
                compares++;
                accesses+=2;
            }

            if(bArr.length == 2) {

                if(bArr[0] > bArr[1]) {

                    int temp = bArr[1];
                    bArr[1] = bArr[0];
                    bArr[0] = temp;
                    swaps++;
                    accesses+=4;
                }
                compares++;
                accesses+=2;
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
                accesses+=4;
                swaps++;
                compares++;
            }

            while(aIndex < aArr.length) {
                cArr[cIndex++] = aArr[aIndex++];
                accesses += 2;
                swaps++;
            }

            while(bIndex < bArr.length) {
                cArr[cIndex++] = bArr[bIndex++];
                accesses += 2;
                swaps++;
            }

            return cArr;
        }

        else {

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
    }

    /**
     * Performs an insertion sort on a given array, will also calculate the execution time, number of swaps, comparisons and array accesses
     * @param uArray The unsorted int array
     * @param count pass true if number of swaps, comparisons and array accesses are desired, else pass false.
     * @return The sorted array
     */
    public static int[] selectionSort(int[] uArray, boolean count) {

        long startTime = System.nanoTime();

        if(count) {

            for(int i = 0; i < uArray.length; i++) {

                int smallestElement = uArray[i];
                int swapIndex = 0;

                accesses++;

                for(int j = i; j < uArray.length; j++) {

                    if(uArray[j] <= smallestElement) {
                        smallestElement = uArray[j];
                        swapIndex = j;
                        accesses++;
                    }
                    accesses++;
                    compares++;
                }

                uArray[swapIndex] = uArray[i];
                uArray[i] = smallestElement;
                swaps+=2;
                accesses+=3;
            }
        }

        else {

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
        }

        time = (double)(System.nanoTime()-startTime)/1000000;

        System.out.printf("Selection Sort completed in: %.3f ms\n",time);
        System.out.println("Selection Sort swaps: "+swaps);
        System.out.println("Selection Sort comparisons: "+compares);
        System.out.println("Selection Sort array accesses: "+accesses);
        System.out.println("Selection Sort recursive calls: "+recursions);

        return uArray;
    }

    /**
     * Performs an insertion sort on a given array, will also calculate the execution time, number of swaps, comparisons and array accesses
     * @param uArray The unsorted int array
     * @param count pass true if numbere of swaps, comparisons and array accesses are desired, else pass false.
     * @return The sorted array.
     */
    public static int[] insertionSort(int[] uArray, boolean count) {

        long startTime = System.nanoTime();

        if(count) {

            for(int i = 1,j=i-1; i < uArray.length; i++,j=i-1) {

                int temp = uArray[i];

                accesses++;

                while(j >= 0 && uArray[j] > temp) {

                    accesses++;
                    compares++;
                    uArray[j+1] = uArray[j];
                    j--;
                    accesses+=2;
                    swaps++;
                }
                accesses++;
                compares++;

                uArray[j+1] = temp;
                swaps++;
                accesses++;
            }
        }

        else {

            for(int i = 1,j=i-1; i < uArray.length; i++,j=i-1) {

                int temp = uArray[i];

                while(j >= 0 && uArray[j] > temp) {

                    uArray[j+1] = uArray[j];
                    j--;
                }

                uArray[j+1] = temp;
            }
        }

        time = (double)(System.nanoTime()-startTime)/1000000;

        System.out.printf("Insertion Sort completed in: %.3f ms\n",time);
        System.out.println("Insertion Sort swaps: "+swaps);
        System.out.println("Insertion Sort comparisons: "+compares);
        System.out.println("Insertion Sort array accesses: "+accesses);
        System.out.println("Insertion Sort recursive calls: "+recursions);

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
