package compression;

import java.io.*;

public class Compression {
  public static void main(String[] args) {
    String inputFile = "compression/resources/Twenty_thousand_leagues_under_the_sea.txt";
    String outputFile = "compression/output/compressed.bin";

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
      FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
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
}
