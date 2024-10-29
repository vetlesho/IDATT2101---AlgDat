package compression;

import java.io.*;
import java.util.Scanner;

public class Program {
  private static final String inputFile = "compression/resources/diverse.lyx";
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
      FileInputStream fileInputStream = new FileInputStream(inputFile);
      byte[] data = fileInputStream.readAllBytes();
      fileInputStream.close();

      byte[] compressedData = LZW.compress(data);

      FileOutputStream fileOutputStream = new FileOutputStream(compressedFile);
      fileOutputStream.write(compressedData);
      fileOutputStream.close();

      System.out.println("Compression completed successfully.");
      System.out.println("Original size: " + data.length + " bytes.");
      System.out.println("Compressed size: " + compressedData.length + " bytes.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void decompress() {
    try {
      FileInputStream fileInputStream = new FileInputStream(compressedFile);
      byte[] compressedData = fileInputStream.readAllBytes();
      fileInputStream.close();

      byte[] decompressedData = LZW.decompress(compressedData);

      FileOutputStream fileOutputStream = new FileOutputStream(deCompressedFile);
      fileOutputStream.write(decompressedData);
      fileOutputStream.close();

      System.out.println("Decompression completed successfully.");
      System.out.println("Decompressed size: " + decompressedData.length + " bytes.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
