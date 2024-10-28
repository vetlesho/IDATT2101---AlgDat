package compression;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZW {
  public static List<Integer> encode(String text) {
    int dictSize = 256;
    Map<String, Integer> dictionary = new HashMap<>();
    for (int i = 0; i < dictSize; i++) {
      dictionary.put(String.valueOf((char) i), i);
    }

    String foundChars = "";
    List<Integer> result = new ArrayList<>();

    for (char character : text.toCharArray()) {
      String charsToAdd = foundChars + character;
      if (dictionary.containsKey(charsToAdd)) {
        foundChars = charsToAdd;
      } else {
        Integer value = dictionary.get(foundChars);
        if (value != null) {
          result.add(value);
        } else {
          System.err.println("Warning: null value for foundChars = " + foundChars);
        }
        dictionary.put(charsToAdd, dictSize++);
        foundChars = String.valueOf(character);
      }
    }
    if (!foundChars.isEmpty()) {
      result.add(dictionary.get(foundChars));
    }

    return result;
  }

  public static List<Integer> encodeNew(String text) {
    int dictSize = 256;
    Map<String, Integer> dictionary = new HashMap<>();
    for (int i = 0; i < dictSize; i++) {
      dictionary.put(String.valueOf((char) i), i);
    }

    // Convert text to UTF-8 bytes
    byte[] utf8Bytes = text.getBytes(StandardCharsets.UTF_8);
    String foundChars = "";
    List<Integer> result = new ArrayList<>();

    for (byte b : utf8Bytes) {
      String charsToAdd = foundChars + (char) (b & 0xFF);
      if (dictionary.containsKey(charsToAdd)) {
        foundChars = charsToAdd;
      } else {
        result.add(dictionary.get(foundChars));
        dictionary.put(charsToAdd, dictSize++);
        foundChars = String.valueOf((char) (b & 0xFF));
      }
    }
    if (!foundChars.isEmpty()) {
      result.add(dictionary.get(foundChars));
    }

    return result;
  }

  public static String decode(List<Integer> encodedText) {
    int dictSize = 256;
    Map<Integer, String> dictionary = new HashMap<>();
    for (int i = 0; i < dictSize; i++) {
      dictionary.put(i, String.valueOf((char) i));
    }

    String characters = String.valueOf((char) encodedText.remove(0).intValue());
    StringBuilder result = new StringBuilder(characters);
    for (int code : encodedText) {
      String entry = dictionary.containsKey(code)
              ? dictionary.get(code)
              : characters + characters.charAt(0);
      result.append(entry);
      dictionary.put(dictSize++, characters + entry.charAt(0));
      characters = entry;
    }

    // Convert the result to a UTF-8 string
    return new String(result.toString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
  }
}
