package compression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LZ {
  private static final int MAX_LOOKBACK = 32 * 1024; // 32 KB

  public static byte[] compress(byte[] data) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    int i = 0;

    while (i < data.length) {
      int matchLength = 0;
      int matchDistance = 0;

      // Find the longest match
      for (int j = Math.max(0, i - MAX_LOOKBACK); j < i; j++) {
        int k = 0;
        while (k < 255 && i + k < data.length && data[j + k] == data[i + k]) {
          k++;
        }
        if (k > matchLength) {
          matchLength = k;
          matchDistance = i - j;
        }
      }

      if (matchLength > 2) {
        // Write a compressed block
        outputStream.write(matchLength);
        outputStream.write((matchDistance >> 8) & 0xFF);
        outputStream.write(matchDistance & 0xFF);
        i += matchLength;
      } else {
        // Write an uncompressed block
        int start = i;
        while (i < data.length && (i - start) < 127 && matchLength <= 2) {
          i++;
          if (i < data.length) {
            matchLength = 0;
            for (int j = Math.max(0, i - MAX_LOOKBACK); j < i; j++) {
              int k = 0;
              while (k < 255 && i + k < data.length && data[j + k] == data[i + k]) {
                k++;
              }
              if (k > matchLength) {
                matchLength = k;
              }
            }
          }
        }
        outputStream.write(-(i - start));
        outputStream.write(data, start, i - start);
      }
    }

    return outputStream.toByteArray();
  }

  public static byte[] decompress(byte[] data) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    int i = 0;

    while (i < data.length) {
      int length = data[i++];
      if (length < 0) {
        length = -length;
        outputStream.write(data, i, length);
        i += length;
      } else {
        int distance = ((data[i++] & 0xFF) << 8) | (data[i++] & 0xFF);
        int start = outputStream.size() - distance;
        for (int j = 0; j < length; j++) {
          outputStream.write(outputStream.toByteArray()[start + j]);
        }
      }
    }

    return outputStream.toByteArray();
  }
}