package org.example.homework3.dated.map;

public class DatedMapTest {

  public static void main(String[] args) {
    DatedMap testDateMap = new DatedMapImpl();

    testDateMap.put("qwe", "qwerty");
    testDateMap.put("asd", "asdfg");
    testDateMap.put("zxc", "zxcvb");
    testDateMap.put("docker-compose.yml", "docker-compose.yml 45");

    System.out.println(testDateMap.get("asd")); //asdfg
    System.out.println(testDateMap.get("asd123")); //null

    System.out.println(testDateMap.containsKey("asd")); //true
    System.out.println(testDateMap.containsKey("asd123")); //false

    System.out.println(testDateMap.keySet()); //[docker-compose.yml, asd, zxc, qwe]

    System.out.println(testDateMap.getKeyLastInsertionDate("asd")); //Date
    System.out.println(testDateMap.getKeyLastInsertionDate("asd123")); //null
  }
}
