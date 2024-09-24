import java.io.*;

public class Task4HashTable {

  // HashNode class to create linked list nodes
  private static class HashNode {
    String key;
    HashNode next;

    public HashNode(String key) {
      this.key = key;
      this.next = null;
    }
  }

  private HashNode[] nodes;
  private int length;
  private int collisions;

  public Task4HashTable(int length) {
    this.nodes = new HashNode[length];
    this.length = length;
    this.collisions = 0;
  }

  private int hashFunction(String key) {
    int number = 1;
    int hash = 0;

    for (char n : key.toCharArray()) {
      hash += n*31*number;
      number++;
    }
    return hash % length;
  }

  public void insert(String key) {
    int index = hashFunction(key);
    HashNode newNode = new HashNode(key);

    if (nodes[index] == null) {
      nodes[index] = newNode;
    } else {
      collisions++;
      System.out.println("Collision: " + nodes[index].key + " and " + key);
      HashNode temp = nodes[index];
      while (temp.next != null) {
        temp = temp.next;
      }
      temp.next = newNode;
    }
  }

  public HashNode search(String key) {
    StringBuilder output = new StringBuilder();
    int index = hashFunction(key);
    HashNode temp = nodes[index];
    output.append(temp.key);
    while (!temp.key.equals(key)) {
      temp = temp.next;
      output.append(" -> ").append(temp.key);
    }
    System.out.println(output);
    return temp;
  }

  public void print() {
    HashNode temp;
    int people = 0;
    for(HashNode n : nodes){
      if(n != null){
        temp = n;
        people++;
        while(temp.next != null) {
          temp = temp.next;
          people++;
        }

      }
    }
    System.out.println("nr of collisions " + collisions);
    System.out.println("Load factor " + (double) (people/length) );
    System.out.println("avarage collisions pr person " + ((double)collisions/(double)people));
  }

  public static void main(String[] args) {
    Task4HashTable table = new Task4HashTable(86);
    try {
      BufferedReader b = new BufferedReader(new FileReader(new File("navn.txt")));
      String text;
      while((text = b.readLine() )!= null){
        table.insert(text);
      }

    }catch (IOException e){
      e.printStackTrace();
    }
    //print all the names
    int i = 0;
    for (HashNode n : table.nodes) {
      i++;
      if (n != null) {
        HashNode temp = n;
        System.out.print(i + ": " + temp.key);
        while (temp.next != null) {
          temp = temp.next;
          System.out.print(" -> " + temp.key);
        }
        System.out.println();
      }
    }


    //System.out.println(table.search("Vetle Solheim Hodne").key);
  }
}
