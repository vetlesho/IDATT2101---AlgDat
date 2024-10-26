package compression;

import java.io.*;
import java.util.Arrays;

public class LZ {
  private static final int MAX_LOOKBACK = 32 * 1024; // 32 KB
  static byte[] buffer; // bytearray buffer
  static int position = 0;
  static byte[] bytes; // bytearray with (soon) source data
  static int endPos = 0;

  public static byte[] compress(byte[] data) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(outputStream);
    bytes = data;
    buffer = new byte[MAX_LOOKBACK];
    position = 0;
    endPos = 0;
    int last = 0;

    for (int i = 0; i < bytes.length; i++) { // iterate through the input data, byte by byte
      int posBuff = checkInBuffer(bytes[i], endPos); // check if the current byte is in the buffer
      if (posBuff == -1) { // if the byte is not in the buffer, it adds it to the buffer and increments the position
        addToBuffer(bytes[i]);
        position++;
      } else { // if it is in the buffer, tries to find the longest match
        int max = buildWord(posBuff, i);
        int maxIndex = posBuff;
        while (true) { // tries to find a longer match
          posBuff = checkInBuffer(bytes[i], posBuff - 1);
          if (posBuff == -1) break;
          if (buildWord(posBuff, i) > max) {
            max = buildWord(posBuff, i);
            maxIndex = posBuff;
          }
        }
        if (max > 6) {
          out.writeShort((short) (position - last));
          byte[] temp = new byte[position - last];
          int count = 0;
          for (int j = last; j < position; j++) temp[count++] = bytes[j];
          System.out.println(Arrays.toString(temp));
          out.write(temp);
          out.writeShort((short) (-(i - maxIndex)));
          out.writeShort((short) max);

          for (int j = 0; j < max; j++) {
            addToBuffer(bytes[i++]);
            position++;
          }
          last = i;
          i--;
        } else {
          addToBuffer(bytes[i]);
          position++;
        }
      }
    }
    out.writeShort((short) (position - last)); // writes the length of the unmatched bytes
    for (int i = last; i < position; i++) out.writeByte(bytes[i]); // writes the unmatched bytes
    out.close();

    // Print all bytes in the ByteArrayOutputStream
    byte[] compressedData = outputStream.toByteArray();
    System.out.println(Arrays.toString(compressedData));

    // Print all bytes in the ByteArrayOutputStream as 0s and 1s
    byte[] compressedData2 = outputStream.toByteArray();
    StringBuilder binaryString = new StringBuilder();
    for (byte b : compressedData2) {
      binaryString.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
    }
    System.out.println(binaryString.toString());

    return outputStream.toByteArray();
  }

  public static byte[] decompress(byte[] data) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
    DataOutputStream out = new DataOutputStream(outputStream);
    position = 0;
    endPos = 0;
    buffer = new byte[MAX_LOOKBACK];
    short start = in.readShort();
    bytes = new byte[start];
    in.readFully(bytes);
    out.write(bytes);
    for (int j = 0; j < start; j++) {
      addToBuffer(bytes[j]);
      position++;
    }
    while (in.available() > 0) {
      short back = in.readShort();
      short copyAmount = in.readShort();
      int end = endPos;
      bytes = new byte[copyAmount];
      int i = 0;
      for (int tempIndex = end + back; tempIndex < (end + back) + copyAmount; tempIndex++) {
        byte index = get(tempIndex);
        bytes[i++] = index;
        addToBuffer(index);
        position++;
      }
      out.write(bytes);
      start = in.readShort();
      bytes = new byte[start];
      in.readFully(bytes);
      for (int j = 0; j < start; j++) {
        addToBuffer(bytes[j]);
        position++;
      }
      out.write(bytes);
    }

    in.close();
    out.close();

    return outputStream.toByteArray();
  }

  // Adds a byte to the buffer, and updates end position
  private static void addToBuffer(byte b) {
    if (position >= buffer.length) {
      endPos = 0;
    }
    buffer[endPos] = b;
    endPos = (position + 1) % buffer.length;
  }

  // Retrieves a byte from the buffer
  private static byte get(int index) {
    if (index >= buffer.length) {
      int i = index % buffer.length;
      return buffer[i];
    } else if (index < 0) {
      int i = buffer.length + (index % buffer.length);
      if (i == buffer.length) i = 0;
      return buffer[i];
    }
    return buffer[index];
  }

  // Checks if byte is in buffer, from a given position
  private static int checkInBuffer(byte b, int pos) {
    for (int i = pos; i >= 0; i--) { // goes backwards from end-position
      if (buffer[i] == b) return i;
    }
    return -1;
  }

  // Finds the longest match of bytes, starting from a given position
  private static int buildWord(int posBuff, int posByte) {
    byte byte1 = bytes[posByte];
    byte buff1 = get(posBuff);
    int count = 0;
    while (buff1 == byte1 && posByte != bytes.length - 1) {
      count++;
      byte1 = bytes[++posByte];
      buff1 = get(++posBuff);
    }
    return count;
  }
}