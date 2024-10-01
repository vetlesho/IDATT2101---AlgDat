import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Task4_part2 {
  private static int[] randomArray(int length, int testNumber) {
    int range = 10 * length;
    int[] list = new int[length];
    list[0] = testNumber;
    for (int i = 1; i < length; i++) {
      list[i] = (int) (Math.random() * range) + 1;
    }
    return list;
  }

  private static void printTests(int dataAmount, int collisions, int runs, boolean containsTestNumber, boolean containsEntireDataset, List<Integer> notFound, long fillTime, long findTime, long fillTimeJava, long findTimeJava, HashTable hashTable) {
    int length = hashTable.getLength();
    System.out.println("------------------ Part 2 ------------------");
    System.out.println("Amount of data: " + dataAmount);
    System.out.println("Length of Hashtable: " + length);
    System.out.print("Our hashtable contains entire dataset: ");

    if (containsEntireDataset) {
      System.out.println(" true");
    } else {
      for (Integer i : notFound) {
        System.out.println(" false, did not contain: " + i);
      }
    }
    System.out.println("Our hashtable contains test number: " + containsTestNumber);
    System.out.println("\n-- Averages after '" + runs + "' runs: --\n");

    System.out.println("Collisions: " + collisions / runs);
    System.out.println("Load factor: " + (double) (length - (length - dataAmount)) / length);
    System.out.println("Average collision per data: " + (double) collisions / (dataAmount * runs));


    System.out.println("Time to fill our table: " + fillTime / runs + "ms");

    System.out.println("Time to fill Java's HashMap: " + fillTimeJava / runs + "ms \n");


    System.out.println("Time to find all elements in our table: " + findTime / runs + "ms");

    System.out.println("Time to find all elements in Java's HashMap: " + findTimeJava / runs + "ms");


    System.out.println("--------------------------------------------\n");

  }

  public static void main(String[] args) {

    int runs = 10;
    int fillTime = 0;
    int findTime = 0;
    int fillTimeJava = 0;
    int findTimeJava = 0;
    boolean containsTestNumber = true;
    boolean containsEntireDataset = true;
    List<Integer> notFound = new ArrayList<>();
    int testNumber = 5035;
    int LIST_SIZE = 10_000_000;
    int collisions = 0;

    for (int j = 1; j <= runs; j++) {
      System.out.println("Run: " + (j));

      int[] list = randomArray(LIST_SIZE, testNumber);
      HashTable hashTabel = new HashTable(list.length);
      long fillStart = System.nanoTime();
      for (int i = 0; i < list.length; i++) {
        hashTabel.put(list[i]);
      }
      fillTime += ((int) (System.nanoTime() - fillStart) / 1000000);

      HashMap<Integer, Integer> javaHash = new HashMap<>();
      long fillStartJava = System.nanoTime();
      for (int i = 0; i < list.length; i++) {
        javaHash.put(list[i], i);
      }
      fillTimeJava += (int) ((System.nanoTime() - fillStartJava) / 1000000);

      long findStart = System.nanoTime();
      for (int i = 0; i < list.length; i++) {
        hashTabel.get(list[i]);
      }
      findTime += (int) ((System.nanoTime() - findStart) / 1000000);

      long findStartJava = System.nanoTime();
      for (int i = 0; i < list.length; i++) {
        javaHash.get(list[i]);
      }
      findTimeJava += (int) ((System.nanoTime() - findStartJava) / 1000000);

      int notFoundNumber = hashTabel.checkContent(list);
      if (notFoundNumber != -1) {
        containsEntireDataset = false;
        notFound.add(notFoundNumber);
      }
      if (containsTestNumber) {
        containsTestNumber = hashTabel.get(testNumber) == testNumber;
      }
      collisions += hashTabel.getCollisions();

      printTests(LIST_SIZE, collisions, j, containsTestNumber, containsEntireDataset, notFound, fillTime, findTime, fillTimeJava, findTimeJava, hashTabel);
    }
  }
}
