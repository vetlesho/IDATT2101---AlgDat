package compression;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZW {

  /**
   * This method creates an initial dictionary containing all single-byte ASCII characters (0-255).
   * As it processes each character in the input string,
   * it searches for the longest sequence of characters (substring) that matches an entry in the dictionary.
   * When a substring that is not in the dictionary is found,
   * the method stores the dictionary code of the last matched substring,
   * and adds the new substring to the dictionary with a unique code.
   */
  public static byte[] compress(byte[] data) {
    int dictSize = 256;
    Map<String, Integer> dictionary = new HashMap<>();
    for (int i = 0; i < dictSize; i++) {
      dictionary.put(String.valueOf((char) i), i);
    }

    String foundChars = "";
    List<Integer> compressedData = new ArrayList<>();

    for (byte b : data) {
      // concatenates the current sequence of found characters
      String charsToAdd = foundChars + (char) (b & 0xFF);
      if (dictionary.containsKey(charsToAdd)) {
        // If the dictionary has the code value of the character(s), add it to the sequence and continue.
        foundChars = charsToAdd;
      } else {
        // If the chars are not in the dictionary, add the current sequence of code value(s) to the result.
        compressedData.add(dictionary.get(foundChars));
        // Then add the new entry to the dictionary, and reset to the current character being looped over.
        dictionary.put(charsToAdd, dictSize++);
        foundChars = String.valueOf((char) (b & 0xFF));
      }
    }

    // One last check after exiting the loop:
    if (!foundChars.isEmpty()) {
      // If the current foundChars isn't empty, it didn't get the chance to add it to the result, so add it here.
      compressedData.add(dictionary.get(foundChars));
    }
    System.out.println(dictionary.size());

    // Convert List<Integer> to byte array
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    for (int code : compressedData) {
      byteArrayOutputStream.write((code >> 16) & 0xFF); // 16
      byteArrayOutputStream.write((code >> 8) & 0xFF); // high byte
      byteArrayOutputStream.write(code & 0xFF);         // low byte
    }

    return byteArrayOutputStream.toByteArray();
  }

  /**
   * For decompressing, the logic is similar and almost reversed.
   * <p>
   * For each code number we have, we are going to check if this code is present in the dictionary.
   * If it is, we hold on to it, check the one following it, and append their corresponding characters
   * along with the new code to the dictionary.
   * <p>
   * This allows decoding without the need to provide a pre-saved dictionary to the decoder.
   */
  public static byte[] decompress(byte[] compressedData) {
    int dictSize = 256;
    Map<Integer, String> dictionary = new HashMap<>();
    for (int i = 0; i < dictSize; i++) {
      dictionary.put(i, String.valueOf((char) i));
    }

    // Convert compressed byte data to list of integers
    List<Integer> encodedText = new ArrayList<>();
    for (int i = 0; i < compressedData.length; i += 3) {
      int code =
              ((compressedData[i] & 0xFF) << 16) |
              ((compressedData[i + 1] & 0xFF) << 8) |
              (compressedData[i + 2] & 0xFF);
      encodedText.add(code);
    }

    // The first integer of the compressed list will always be a character in the dictionary
    String characters = String.valueOf((char) encodedText.removeFirst().intValue());
    StringBuilder result = new StringBuilder(characters);

    for (int code : encodedText) {
      // If the code is present in the dictionary, add it, otherwise use the last sequence
      String entry = dictionary.containsKey(code)
              ? dictionary.get(code)
              : characters + characters.charAt(0);
      result.append(entry);

      // Add new sequence to the dictionary
      dictionary.put(dictSize++, characters + entry.charAt(0));
      characters = entry;
    }

    // Convert the result StringBuilder to byte array
    byte[] decompressedBytes = new byte[result.length()];
    for (int i = 0; i < result.length(); i++) {
      decompressedBytes[i] = (byte) result.charAt(i);
    }
    return decompressedBytes;
  }
}
