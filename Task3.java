import java.util.Random;

public class Task3 {
  public static String COLUMNS_FORMAT = "%-25s%-9s%-7b%-7b%n";
  public static int RANDOM_NUMBER_LIMIT = 10001;
  public static int LENGTH = 50_000_000;

  public static void main(String[] args) {
    // Time and sort each list using single pivot quicksort
    System.out.format("%-25s%-9s%-7s%-7s%n", "TYPE OF ARRAY:", "TIME:", "SUM:", "SORTED:");
    System.out.println("------------------------------------------------------------");
    timeSortMethods(getRandomArray(LENGTH), "Random Array");
    timeSortMethods(getHalfDuplicateArray(LENGTH), "Half Duplicate");
    timeSortMethods(getSortedArray(LENGTH), "Sorted");
    timeSortMethods(getReverseSortedArray(LENGTH), "Reverse Sorted");
  }

  public static int[] getRandomArray(int length) {
    int[] list = new int[length];
    Random random = new Random();
    for (int i = 0; i < list.length; i++) {
      list[i] = random.nextInt(RANDOM_NUMBER_LIMIT);
    }
    return list;
  }

  public static int[] getHalfDuplicateArray(int length) {
    int[] list = new int[length];
    Random random = new Random();
    int firstValue = random.nextInt(RANDOM_NUMBER_LIMIT);
    int secondValue = random.nextInt(RANDOM_NUMBER_LIMIT);

    for (int i = 0; i < length; i++) {
      if (i % 2 == 0) {
        list[i] = firstValue;
      } else {
        list[i] = secondValue;
      }
    }

    return list;
  }

  public static int[] getSortedArray(int length) {
    int[] list = new int[length];
    for (int i = 0; i < list.length; i++) {
      list[i] = i;
    }
    return list;
  }

  public static int[] getReverseSortedArray(int length) {
    int[] list = new int[length];
    for (int i = 0; i < list.length; i++) {
      list[i] = list.length - i;
    }
    return list;
  }

  public static long checkSum(int[] list) {
    long sum = 0;
    for (int j : list) {
      sum += j;
    }
    return sum;
  }

  public static boolean checkOrder(int[] list) {
    for (int i = 0; i < list.length - 1; i++) {
      if (list[i] > list[i + 1]) {
        return false;
      }
    }
    return true;
  }

  public static void timeSortMethods(int[] list, String title) {
    int[] singlePivotList = list.clone();
    int[] dualPivotList = list.clone();

    //first time singlepivot
    long sumBefore = checkSum(singlePivotList);
    long start = System.nanoTime();
    quickSortSinglePivot(singlePivotList, 0, singlePivotList.length - 1);
    long end = System.nanoTime();
    long sumAfter = checkSum(singlePivotList);
    boolean orderAfter = checkOrder(singlePivotList);
    System.out.format(COLUMNS_FORMAT, title + " (single)", (end - start) / 1_000_000 + " ms", (sumBefore == sumAfter), orderAfter);

    //second time dualpivot
    sumBefore = checkSum(dualPivotList);
    start = System.nanoTime();
    dualPivotQuickSort(dualPivotList, 0, dualPivotList.length - 1);
    end = System.nanoTime();
    sumAfter = checkSum(dualPivotList);
    orderAfter = checkOrder(dualPivotList);
    System.out.format(COLUMNS_FORMAT, title + " (dual)", (end - start) / 1_000_000 + " ms", (sumBefore == sumAfter), orderAfter);
    System.out.println("------------------------------------------------------------");
  }

  // utility function to swap two elements in an array
  public static void swap(int[] list, int i, int j) {
    int temp = list[i];
    list[i] = list[j];
    list[j] = temp;
  }

  // single pivot quicksort
  // int low = starting index
  // int high = ending index
  public static void quickSortSinglePivot(int[] list, int low, int high) {
    if (high - low > 2) {
      int pivot = partitionSinglePivot(list, low, high); // pivot is where the array is partitioned
      quickSortSinglePivot(list, low, pivot - 1); // recursively sort the left side of the pivot
      quickSortSinglePivot(list, pivot + 1, high); // recursively sort the right side of the pivot
    } else {
      median3sort(list, low, high);
    }
  }

  // partition the array using a single pivot,
  // placing all elements smaller than the pivot to the left of the pivot and,
  // placing all elements larger to the right of the pivot
  public static int partitionSinglePivot(int[] list, int low, int high) {
    int iv, ih;
    int mid = median3sort(list, low, high);
    int dv = list[mid];
    swap(list, mid, high - 1);
    for (iv = low, ih = high - 1; ; ) {
      while (list[++iv] < dv) ;
      while (list[--ih] > dv) ;
      if (iv >= ih) {
        break;
      }
      swap(list, iv, ih);
    }
    swap(list, iv, high - 1);
    return iv;
  }

  public static int median3sort(int[] list, int low, int high) {
    int mid = (low + high) / 2;
    if (list[low] > list[mid]) {
      swap(list, low, mid);
    }
    if (list[mid] > list[high]) {
      swap(list, mid, high);
      if (list[low] > list[mid]) {
        swap(list, low, mid);
      }
    }
    return mid;
  }

  static void dualPivotQuickSort(int[] arr, int low, int high) {
    if (low < high) {
      int[] piv = partition(arr, low, high);

      dualPivotQuickSort(arr, low, piv[0] - 1);
      if (arr[piv[0]] != arr[piv[1]]) {
        dualPivotQuickSort(arr, piv[0] + 1, piv[1] - 1);
      }
      dualPivotQuickSort(arr, piv[1] + 1, high);
    }
  }

  static int[] partition(int[] arr, int low, int high) {
    swap(arr, low, low + (high - low) / 3);
    swap(arr, high, high - (high - low) / 3);

    if (arr[low] > arr[high])
      swap(arr, low, high);

    // p is the left pivot, and q
    // is the right pivot.
    int j = low + 1;
    int g = high - 1, k = low + 1,
            p = arr[low], q = arr[high];

    while (k <= g)
    {

      // If elements are less than the left pivot
      if (arr[k] < p)
      {
        swap(arr, k, j);
        j++;
      }

      // If elements are greater than or equal
      // to the right pivot
      else if (arr[k] >= q)
      {
        while (arr[g] > q && k < g)
          g--;

        swap(arr, k, g);
        g--;

        if (arr[k] < p)
        {
          swap(arr, k, j);
          j++;
        }
      }
      k++;
    }
    j--;
    g++;

    // Bring pivots to their appropriate positions.
    swap(arr, low, j);
    swap(arr, high, g);

    // Returning the indices of the pivots
    // because we cannot return two elements
    // from a function, we do that using an array.
    return new int[] { j, g };
  }
}
