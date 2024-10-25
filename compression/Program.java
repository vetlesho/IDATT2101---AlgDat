package compression;

import java.io.*;
import java.util.Scanner;

public class Program {
  private static final String inputFile = "compression/resources/test.txt";
  private static final String compressedFile = "compression/output/compressed.txt";
  private static final String deCompressedFile = "compression/output/decompressed.txt";

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter 'c' to compress or 'd' to decompress:");
    String choice = scanner.nextLine();

    if (choice.equalsIgnoreCase("c")) {
      compress();
    } else if (choice.equalsIgnoreCase("d")) {
      decompress();
    } else {
      System.out.println("Invalid choice. Please enter 'c' or 'd'.");
    }
    scanner.close();
  }

  public static void compress() {
    try {
      // Read inputfile
      FileInputStream fileInputStream = new FileInputStream(inputFile);
      DataInputStream in = new DataInputStream(fileInputStream);
      byte[] data = new byte[in.available()];
      in.readFully(data);
      in.close();

      // Compress data
      byte[] compressedData = LZ.compress(data);

      // Write compressed data to output file
      FileOutputStream fileOutputStream = new FileOutputStream(compressedFile);
      DataOutputStream out = new DataOutputStream(fileOutputStream);
      out.write(compressedData);
      out.close();

      System.out.println("Compression completed successfully.");
      System.out.println("Original size: " + data.length + " bytes.");
      System.out.println("Compressed size: " + compressedData.length + " bytes.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void decompress() {
    try {
      // Read compressed file
      FileInputStream fileInputStream = new FileInputStream(compressedFile);
      DataInputStream compressedIn = new DataInputStream(fileInputStream);
      byte[] compressedBytes = new byte[compressedIn.available()];
      compressedIn.readFully(compressedBytes);
      compressedIn.close();

      byte[] decompressedData = LZ.decompress(compressedBytes);

      // Write decompressed data to output file
      FileOutputStream fileOutputStream = new FileOutputStream(deCompressedFile);
      DataOutputStream decompressedOut = new DataOutputStream(fileOutputStream);
      decompressedOut.write(decompressedData);
      decompressedOut.close();

      System.out.println("Decompression completed successfully.");
      System.out.println("Decompressed size: " + decompressedData.length + " bytes");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
