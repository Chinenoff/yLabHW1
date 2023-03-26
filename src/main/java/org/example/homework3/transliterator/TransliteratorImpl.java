package org.example.homework3.transliterator;

public class TransliteratorImpl implements Transliterator {

  @Override
  public String transliterate(String source) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < source.length(); i++) {
      sb.append(transliterateChar(charAtPos(source, i)));
    }
    return sb.toString();
  }

  private String transliterateChar(String c) {
    return CharMapCyrToLat.cyrToLat.getOrDefault(c, c);
  }

  private String charAtPos(String string, int position) {
    return position >= string.length() ? null : string.substring(position, position + 1);
  }
}
