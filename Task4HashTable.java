import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Task4HashTable {
  // HashNode class to create linked list nodes
  private static class HashNode {
    String value;
    HashNode next;

    public HashNode(String value, HashNode next) {
      this.value = value;
      this.next = next;
    }
  }

  private final HashNode[] nodes;
  private final int length;
  private int collisions;

  public Task4HashTable(int length) {
    this.nodes = new HashNode[length];
    this.length = length;
    this.collisions = 0;
  }

  // Hash function to calculate the hash value of a given string
  private int hashFunction(String value) {
    int number = 1;
    int hash = 0;

    for (char n : value.toCharArray()) {
      hash += n * number;
      number++;
    }
    return hash % length;
  }

  // Insert a new string into the hash table
  public void insert(String s) {
    int h = hashFunction(s);
    if (nodes[h] == null) {
      nodes[h] = new HashNode(s, null);
    } else {
      nodes[h] = new HashNode(s, nodes[h]);
      collisions++;
    }
  }

  // Search for a string in the hash table
  public HashNode search(String s) {
    int h = hashFunction(s);
    HashNode temp = nodes[h];
    while (temp != null) {
      if (temp.value.equals(s)) {
        return temp;
      }
      temp = temp.next;
    }
    return null;
  }

  // Print the hash table and some statistics
  public void print() {
    HashNode temp;
    int people = 0;
    int i = 0;
    for (HashNode n : nodes) {
      i++;
      if (n != null) {
        temp = n;
        people++;
        System.out.print(i + ": " + temp.value);
        while (temp.next != null) {
          temp = temp.next;
          people++;
          System.out.print(" -> " + temp.value);
        }
        System.out.println();
      } else {
        System.out.println(i + ": ");
      }
    }

    System.out.println("Nr of collisions " + collisions);
    System.out.println("Load factor " + (double) (people - collisions) / length);
    System.out.println("Average collisions pr person " + ((double) collisions / (double) people));
  }

  public static void main(String[] args) {
    Task4HashTable table = new Task4HashTable(124);
    try {
      BufferedReader b = new BufferedReader(new FileReader(("navn.txt")));
      String text;
      while ((text = b.readLine()) != null) {
        table.insert(text);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    table.print();
    System.out.println(table.search("Vetle Solheim Hodne").value);
  }
}
