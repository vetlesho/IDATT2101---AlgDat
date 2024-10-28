package compression;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

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
      // Read inputfile
      FileInputStream fileInputStream = new FileInputStream(inputFile);
      DataInputStream in = new DataInputStream(fileInputStream);
      byte[] data = new byte[in.available()];
      in.readFully(data);
      in.close();

      // Compress data
      /**
      byte[] compressedData = LZ2.compress(data);
      // Write compressed data to output file
      FileOutputStream fileOutputStream = new FileOutputStream(compressedFile);
      DataOutputStream out = new DataOutputStream(fileOutputStream);
      out.write(compressedData);
      out.close();
       */

      // LWZ
      String inputString = new String(data);
      List<Integer> compressedData = LZW.encodeNew(inputString);

      // Write compressed data to output file
      FileOutputStream fileOutputStream = new FileOutputStream(compressedFile);
      DataOutputStream out = new DataOutputStream(fileOutputStream);
      System.out.println(compressedData);
      for (Integer code : compressedData) {
        out.writeInt(code);
      }
      out.close();

      System.out.println("Compression completed successfully.");
      System.out.println("Original size: " + data.length + " bytes.");
      System.out.println("Compressed size: " + compressedData.size() * 4 + " bytes.");
    } catch (IOException  | IllegalStateException e) {
      e.printStackTrace();
    }
  }

  public static void decompress() {
    try {
      // Read compressed file
      FileInputStream fileInputStream = new FileInputStream(compressedFile);
      DataInputStream compressedIn = new DataInputStream(fileInputStream);
      /**
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
       */

      // LZW
      List<Integer> compressedData = new ArrayList<>();
      while (compressedIn.available() > 0) {
        compressedData.add(compressedIn.readInt());
      }
      compressedIn.close();
      String decompressedString = LZW.decode(compressedData);

      FileOutputStream fileOutputStream = new FileOutputStream(deCompressedFile);
      DataOutputStream decompressedOut = new DataOutputStream(fileOutputStream);
      decompressedOut.write(decompressedString.getBytes());
      decompressedOut.close();

      System.out.println("Decompression completed successfully.");
      System.out.println("Decompressed size: " + decompressedString.length() + " bytes");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
