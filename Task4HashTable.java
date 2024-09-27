import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Hash table implementation using linked lists for collisions
public class Task4HashTable {
  // HashNode class to create linked list nodes
  private static class HashNode {
    String value;
    HashNode next; // Pointer to the next node

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
    String output = "";
    int key = hashFunction(s);
    if (nodes[key] == null) { // If the list is empty, create a new node
      nodes[key] = new HashNode(s, null);
    } else {
      output += nodes[key].value + " --> ";
      nodes[key] = new HashNode(s, nodes[key]); // If the list is not empty, add the new node to the beginning
      output += nodes[key].value;
      collisions++;
    }
    if (!output.isEmpty()) {
      System.out.println(output);
    } else {
      System.out.print(output);
    }
  }

  // Search for a string in the hash table
  public HashNode search(String s) {
    int key = hashFunction(s);
    HashNode temp = nodes[key];
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
    int elements = 0;
    int people = 0;
    for (HashNode n : nodes) { // Iterate through all the nodes
      if (n != null) {
        elements++;
        people++;
        while (n.next != null) { // Iterate through the linked list
          n = n.next;
          people++;
        }
      }
    }

    System.out.println("--------------------------");
    System.out.println("Nr of collisions " + collisions);
    System.out.println("Load factor " + (double) (elements) / length);
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
    String findName = "Vetle Solheim Hodne";
    HashNode find = table.search(findName);
    if (find != null) {
      System.out.println(find.value + ", er funnet!");
    } else {
      System.out.println(findName + ", ikke funnet");
    }
  }
}
