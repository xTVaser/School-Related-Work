import java.util.Random;

/**
 * Author - Tyler Wilding
 * Description - Empirical Analysis of several sorting functions.
 * TODO -   Hoare Quicksort, Radix Sort
 */
public class Sort {

    //Global variables for keeping track of swaps, comparisons, etc.
    public static long swaps = 0;
    public static long compares = 0;
    public static long accesses = 0;
    public static long recursions = 0;
    public static double time = 0;

    public static void main(String[] args) {

        //Testing purposes
        //int[] array = generateArray("Random",10000);
        //int[] array = {7,8,4,5,2};
        //array = insertionSort(array);
        //System.out.println(verifySorted(array));

        long startTime = System.nanoTime();
        //Actual rigorous testing routine, does 100 sorts with each test case, on each sorting algorithm from size 100 to 12800.
        for(int i = 0; i <= 7; i++) {
            testAlgorithms("Ascending",(int)Math.pow(2,i)*100,100);
            testAlgorithms("Descending",(int)Math.pow(2,i)*100,100);
            testAlgorithms("Random",(int)Math.pow(2,i)*100,100);
        }

        //Testing at the requested 5000 array size.
        testAlgorithms("Ascending",5000,100);
        testAlgorithms("Descending",5000,100);
        testAlgorithms("Random",5000,100);
        System.out.println("Testing Completed In: "+(System.nanoTime()-startTime)/1000000000+"s");
    }

    /**
     * Quick method to reset the global variables for each test.
     */
    public static void resetCounters() {

        swaps = 0;
        compares = 0;
        accesses = 0;
        recursions = 0;
        time = 0;
    }

    /**
     * Testing method to automate testing and averaging results on each sorting algorithm.
     * @param arrayType Whether or not the array should be ascending, descending, or random order.
     * @param arraySize Number of elements in the array.
     * @param iterations Number of tests to run per sorting algorithm to calculate the average.
     */
    public static void testAlgorithms(String arrayType, int arraySize, int iterations) {

        //HEAP SORT//
        //Each test we set the time back to 0.
        double timeAvg = 0;

        System.out.printf("%25s Array Type: %15s | Array Size: %10d | Sample Size: %5d\n","Sorting Algorithm Test - ",arrayType,arraySize,iterations);
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");

        for(int i = 0; i < iterations; i++) {

            //Generate an array at the required type and size and then call the sort.
            int[] array = generateArray(arrayType, arraySize);
            array = heapSort(array);

            if(i == 0) //For whatever reason, heapsort's first run will take the expected time, but successive heap sorts on different arrays will be extremely quick (0.02ms) or less, so we just care about the first.
                timeAvg = time;

            if(!verifySorted(array)) //Check to make sure the array was successfully sorted.
                System.out.println("Heap Sort Failed!");
        }
        //Print out the averages of the results.
        System.out.printf(  "%24s %10d | Comparisons: %10d | Array Accesses: %10d | Recursive Calls: %10d | Time: %10.3fms\n"
                            ,"Heap Sort - Swaps: ",swaps/iterations,compares/iterations,accesses/iterations,recursions/iterations,timeAvg);
        //Reset the counters for the next test.
        resetCounters();
        timeAvg = 0;

        //QUICK SORT//
        for(int i = 0; i < iterations; i++) {

            int[] array = generateArray(arrayType, arraySize);
            array = quickSortHelper(array);

            timeAvg += time;

            if(!verifySorted(array))
                System.out.println("Quick Sort Failed!");
        }
        System.out.printf(  "%24s %10d | Comparisons: %10d | Array Accesses: %10d | Recursive Calls: %10d | Time: %10.3fms\n"
                ,"Quick Sort - Swaps: ",swaps/iterations,compares/iterations,accesses/iterations,recursions/iterations,timeAvg/iterations);

        resetCounters();
        timeAvg = 0;

        //MERGE SORT//
        for(int i = 0; i < iterations; i++) {

            int[] array = generateArray(arrayType, arraySize);
            array = mergeSortHelper(array);

            timeAvg += time;


            if(!verifySorted(array))
                System.out.println("Merge Sort Failed!");
        }
        System.out.printf(  "%24s %10d | Comparisons: %10d | Array Accesses: %10d | Recursive Calls: %10d | Time: %10.3fms\n"
                ,"Merge Sort - Swaps: ",swaps/iterations,compares/iterations,accesses/iterations,recursions/iterations,timeAvg/iterations);

        resetCounters();
        timeAvg = 0;

        //SELECTION SORT//
        for(int i = 0; i < iterations; i++) {

            int[] array = generateArray(arrayType, arraySize);
            array = selectionSort(array);

            timeAvg += time;


            if(!verifySorted(array))
                System.out.println("Selecton Sort Failed!");
        }
        System.out.printf(  "%24s %10d | Comparisons: %10d | Array Accesses: %10d | Recursive Calls: %10d | Time: %10.3fms\n"
                ,"Selection Sort - Swaps: ",swaps/iterations,compares/iterations,accesses/iterations,recursions/iterations,timeAvg/iterations);

        resetCounters();
        timeAvg = 0;

        //INSERTION SORT//
        for(int i = 0; i < iterations; i++) {

            int[] array = generateArray(arrayType, arraySize);
            array = insertionSort(array);

            timeAvg += time;

            if(!verifySorted(array))
                System.out.println("Insertion Sort Failed!");
        }
        System.out.printf(  "%24s %10d | Comparisons: %10d | Array Accesses: %10d | Recursive Calls: %10d | Time: %10.3fms\n"
                            ,"Insertion Sort - Swaps: ",swaps/iterations,compares/iterations,accesses/iterations,recursions/iterations,timeAvg/iterations);

        resetCounters();
        System.out.println();
        System.out.println();
    }

    /**
     * Main heapsort function.
     * @param uArr Unsorted array that is a heap during sorting.
     * @return The sorted array.
     */
    public static int[] heapSort(int[] uArr) {

        long startTime = System.nanoTime();

        //Find the index of the end of the heap
        int endOfHeap = uArr.length - 1;

        //Heapify the array, we only have to heapify the first half of the array to generate a complete heap.
        for(int i = endOfHeap/2; i >= 0; i--)
            heapify(uArr, i, endOfHeap);

        //Once the heap is built, since we are using a max heap we add to the end of our array.
        for(int i = endOfHeap; i > 0; i--) {

            //Inplace sorting, so we just swap the first element with the current max element near the latter half of the array
            int temp = uArr[0];
            uArr[0] = uArr[i];
            uArr[i] = temp;
            accesses+=3;
            swaps++;

            endOfHeap--;
            //Heapify the new sorted region.
            heapify(uArr, 0, endOfHeap);
        }
        time = (double)(System.nanoTime()-startTime)/1000000;
        return uArr;
    }

    /**
     * Rebuilds the array back into a max heap.
     * @param uArr Unsorted Array, now a heap.
     * @param i Current root node we are looking at
     * @param endOfHeap Total elements that are not sorted in the array
     * @return Heap as an integer array.
     */
    public static int[] heapify(int[] uArr, int i, int endOfHeap) {

        //Calculate the left, right and root indexes.
        int leftChild = i * 2;
        int rightChild = i * 2 + 1;
        int root = i;

        //If we are in the range of the heap.
        if(leftChild <= endOfHeap) {

            //And the left child is greater than the root, this invalidates the heap, so change the index of root.
            if(uArr[leftChild] > uArr[root])
                root = leftChild;
            accesses+=2;
            compares++;
        }

        //If we are in the range of the heap
        if(rightChild <= endOfHeap) {

            //And the right child is greater than the root, invalidates the heap, change the index of root.
            if(uArr[rightChild] > uArr[root])
                root = rightChild;
            accesses+=2;
            compares++;
        }

        //If we changed the root index.
        if(root != i) {
            //Swap the original root, with the new root.
            int temp = uArr[i];
            uArr[i] = uArr[root];
            uArr[root] = temp;
            accesses+=4;
            swaps++;
            recursions++;
            //Recall heapify with the new root to check the rest of the heap.
            heapify(uArr, root, endOfHeap);
        }

        return uArr;
    }

    /**
     * Quick method to easily call quicksort and collect empirical analysis on it.
     * @param uArr Unsorted Array
     * @return Sorted integer array.
     */
    public static int[] quickSortHelper(int[] uArr) {

        long startTime = System.nanoTime();

        //Call quicksort with the array, the start index, and end index.
        uArr = quickSort(uArr, 0, uArr.length-1);

        time = (double)(System.nanoTime()-startTime)/1000000;
        return uArr;
    }

    /**
     * Quicksort Method
     * @param uArr Full unsorted array
     * @param lowBound Beginning of the partion (assuming 0 based)
     * @param highBound End of the partiton (for example length-1)
     * @return The sorted array.
     */
    public static int[] quickSort(int[] uArr, int lowBound, int highBound) {

        //If we are examining a valid index
        if(lowBound < highBound) {

            //Calculate the pivot using Lomento's scheme.
            int pivot = partitionArray(uArr, lowBound, highBound);
            recursions+=2;
            //Recursively call quickSort with the two partitions around the pivot.
            quickSort(uArr, lowBound, pivot-1);
            quickSort(uArr, pivot+1, highBound);
        }

        //Else return the array, we are finished.
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

        //Our key is the end of the partition
        int key = arr[highBound];
        accesses++;

        //Start another index at one position behind the loops
        int i = lowBound-1;
        for(int j = lowBound; j < highBound; j++) { //Loop until the end of the partition

            //If we encounter an element less than the key.
            if(arr[j] <= key) {
                //Increment our other pointer, then swap both elements.
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

        //At the end, we must swap the key element and the determined pivot.
        i++;
        int temp = arr[i];
        arr[i] = arr[highBound];
        arr[highBound] = temp;
        swaps++;
        accesses+=4;

        //Return the index of the pivot to be used in later calculations.
        return i;
    }

    /**
     * Quick method to easily call mergesort and collect empirical analysis on it.
     * @param uArr Unsorted Array
     * @return Sorted integer array.
     */
    public static int[] mergeSortHelper(int[] uArr) {

        long startTime = System.nanoTime();

        uArr = mergeSort(uArr);

        time = (double)(System.nanoTime()-startTime)/1000000;
        return uArr;
    }

    /**
     * Performs an merge sort on a given array, will also calculate the execution time, number of swaps, comparisons and array accesses
     * @param mArr The unsorted array
     * @return The sorted array
     */
    public static int[] mergeSort(int[] mArr) {

        //If the length of the array is 2 elements or less, we can stop doing recursive calls.
        if(mArr.length <= 2)
            return mArr;

        //Create two arrays, one for the first half and one for the second half.
        int[] aArr = new int[mArr.length/2];
        int[] bArr = new int[(int)Math.ceil(mArr.length/2.0)];

        //Place the first half from the original array, into array A.
        for(int i = 0; i < mArr.length/2; i++) {
            aArr[i] = mArr[i];
            accesses++;
        }

        //Place the second half from the original array, into array B.
        for(int i = mArr.length/2, j = 0; i < mArr.length; i++, j++) {
            bArr[j] = mArr[i];
            accesses++;
        }

        //Call mergeSort for both arrays so we can recursively split them up.
        recursions+=2;
        aArr = mergeSort(aArr);
        bArr = mergeSort(bArr);

        //We will then return both of the arrays merged in sorted order.
        return merge(aArr, bArr);
    }

    /**
     * Merges 2 unsorted integers arrays into a combined sorted array
     * @param aArr First array
     * @param bArr Second array
     * @return Combined sorted array.
     */
    public static int[] merge(int[] aArr, int[] bArr) {

        //If the length of array A is 2
        if(aArr.length == 2) {

            //And the elements are not sorted, swap them.
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

        //If the length of array B is 2
        if(bArr.length == 2) {

            //And the elements are not sorted, swap them.
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

        //Create a new array that can hold both array's elements.
        int[] cArr = new int[aArr.length+bArr.length];

        //Make some indexes for each array.
        int aIndex = 0;
        int bIndex = 0;
        int cIndex = 0;

        //While both arrays have elements
        while(aIndex < aArr.length && bIndex < bArr.length) {

            //Add the smaller of the two elements to the merged array
            if(aArr[aIndex] < bArr[bIndex])
                cArr[cIndex++] = aArr[aIndex++];
            else
                cArr[cIndex++] = bArr[bIndex++];
            accesses+=4;
            swaps++;
            compares++;
        }

        //If only array A has elements, add it's elements to the merged array
        while(aIndex < aArr.length) {
            cArr[cIndex++] = aArr[aIndex++];
            accesses += 2;
            swaps++;
        }

        //If only array B has elements, add it's elements to the merged array
        while(bIndex < bArr.length) {
            cArr[cIndex++] = bArr[bIndex++];
            accesses += 2;
            swaps++;
        }

        return cArr;
    }

    /**
     * Performs an insertion sort on a given array, will also calculate the execution time, number of swaps, comparisons and array accesses
     * @param uArray The unsorted int array
     * @return The sorted array
     */
    public static int[] selectionSort(int[] uArray) {

        long startTime = System.nanoTime();

        //Loop through the array
        for(int i = 0; i < uArray.length; i++) {

            //We need to find the smallest element and place it in the correct spot, originally the first elements can be the smallest.
            int smallestElement = uArray[i];
            //Index for where we took the element from, so we can swap at the end.
            int swapIndex = 0;
            accesses++;

            //Loop through from the beginning of the sorted region to the end of the array.
            for(int j = i; j < uArray.length; j++) {

                //If we find a new smallest element, update smallest element and save the index where it was taken from.
                if(uArray[j] <= smallestElement) {
                    smallestElement = uArray[j];
                    swapIndex = j;
                    accesses++;
                }
                accesses++;
                compares++;
            }

            //At the end, swap the smallest element with the last element in the sorted region, sorted region now increases by 1.
            uArray[swapIndex] = uArray[i];
            uArray[i] = smallestElement;
            swaps+=2;
            accesses+=3;
        }

        time = (double)(System.nanoTime()-startTime)/1000000;
        return uArray;
    }

    /**
     * Performs an insertion sort on a given array, will also calculate the execution time, number of swaps, comparisons and array accesses
     * @param uArray The unsorted int array
     * @return The sorted array.
     */
    public static int[] insertionSort(int[] uArray) {

        long startTime = System.nanoTime();

        //Loop through the entire array with 2 pointers, j is 1 position behind i.
        for(int i = 1,j=i-1; i < uArray.length; i++,j=i-1) {

            //Temporarily store the element at i's location
            int temp = uArray[i];
            accesses++;

            //While j is within the bounds of the array, and the element it is pointing at is greater than i's
            while(j >= 0 && uArray[j] > temp) {

                accesses++;
                compares++;
                //Push the elements forward in the array, then decrement j.
                uArray[j+1] = uArray[j];
                j--;
                accesses+=2;
                swaps++;
            }
            accesses++;
            compares++;

            //At the end insert i's value into it's correct position.
            uArray[j+1] = temp;
            swaps++;
            accesses++;
        }

        time = (double)(System.nanoTime()-startTime)/1000000;
        return uArray;
    }

    /**
     * Quick method to verify that the array is sorted, useful for testing.
     * @param sArray Potentially sorted integer array
     * @return True or false if sorted.
     */
    public static boolean verifySorted(int[] sArray) {

        //Loop through the array
        for(int i = 0; i < sArray.length-1; i++) {

            //if the next element is less than the current element, the array isn't in ascending order
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

        //So we can generate negative numbers, size 5000 would give you -2500->2499
        int lowBound = size/2*-1;
        int highBound = size/2;

        //Ascending Order
        if (args.equalsIgnoreCase("sorted") || args.equalsIgnoreCase("ascending")) {

            int[] array = new int[size];
            for(int i = 0; i < array.length; i++)
                array[i] = lowBound+i;
            return array;
        }

        //Descending Order
        else if(args.equalsIgnoreCase("decreasing") || args.equalsIgnoreCase("descending")){

            int[] array = new int[size];
            for(int i = 0; i < array.length; i++)
                array[i] = highBound-i;
            return array;
        }

        //Else, do random order.
        int[] array = new int[size];
        Random rand = new Random();
        for(int i = 0; i < array.length; i++)
            array[i] = rand.nextInt(size)-highBound;
        return array;
    }
}
