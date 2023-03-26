package org.example.homework3.transliterator;

import java.util.HashMap;
import java.util.Map;

public class CharMapCyrToLat {
  public static Map<String, String> cyrToLat = new HashMap<>();
  private static void addPair(String cyr, String lat) {
    cyrToLat.put(cyr, lat);
  }
  static {
    addPair("А", "A");
    addPair("Б", "B");
    addPair("В", "V");
    addPair("Г", "G");
    addPair("Д", "D");
    addPair("Е", "E");
    addPair("Ё", "E");
    addPair("Ж", "ZH");
    addPair("З", "Z");
    addPair("И", "I");
    addPair("Й", "I");
    addPair("К", "K");
    addPair("Л", "L");
    addPair("М", "M");
    addPair("Н", "N");
    addPair("О", "O");
    addPair("П", "P");
    addPair("Р", "R");
    addPair("С", "S");
    addPair("Т", "T");
    addPair("У", "U");
    addPair("Ф", "F");
    addPair("Х", "KH");
    addPair("Ц", "TS");
    addPair("Ч", "CH");
    addPair("Ш", "SH");
    addPair("Щ", "SHCH");
    addPair("Ы", "Y");
    addPair("Ь", "-");
    addPair("Ъ", "IE");
    addPair("Э", "E");
    addPair("Ю", "IU");
    addPair("Я", "IA");
  }
}
