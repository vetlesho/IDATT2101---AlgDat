package compression;

import java.io.*;

public class Decompression {
  public static void main(String[] args) {
    String compressedFile = "compression/output/compressed.bin";
    String decompressedFile = "compression/output/decompressed.txt";

    try {
      // Read compressed file
      FileInputStream fileInputStream = new FileInputStream(compressedFile);
      DataInputStream compressedIn = new DataInputStream(fileInputStream);
      byte[] compressedBytes = new byte[compressedIn.available()];
      compressedIn.readFully(compressedBytes);
      compressedIn.close();

      byte[] decompressedData = LZ.decompress(compressedBytes);

      // Write decompressed data to output file
      FileOutputStream fileOutputStream = new FileOutputStream(decompressedFile);
      DataOutputStream decompressedOut = new DataOutputStream(fileOutputStream);
      decompressedOut.write(decompressedData);
      decompressedOut.close();

      System.out.println("\nDecompression completed successfully.");
      System.out.println("Decompressed size: " + decompressedData.length + " bytes");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
