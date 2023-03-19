package org.example.homework3.dated.map;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DatedMapImpl implements DatedMap {

  public Map<String,String> datedMap = new HashMap<>();
  // "EEE MMM dd HH:mm:ss zzz yyyy" - toString Data (28 number)

  @Override
  public void put(String key, String value) {
    Date date = new Date(System.currentTimeMillis());
    String datedValue = date + value;
    datedMap.put(key, datedValue);

  }

  @Override
  public String get(String key) {
    if (datedMap.containsKey(key)){
      return datedMap.get(key).substring(28);
    } else {
      return null;
    }
  }

  @Override
  public boolean containsKey(String key) {
    return datedMap.containsKey(key);
  }

  @Override
  public void remove(String key) {
    datedMap.remove(key);
  }

  @Override
  public Set<String> keySet() {
    return datedMap.keySet();
  }

  @Override
  public Date getKeyLastInsertionDate(String key) {
    return new Date(datedMap.get(key).substring(0, 28));
  }
}
