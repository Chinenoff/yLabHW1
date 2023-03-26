package org.example.homework3.org.structure;

import java.io.File;
import java.io.IOException;

public class OrgStructureParserTest {

  public static void main(String[] args) throws IOException {
    File csvFile = new File("src/main/resources/employeeCSV.csv");
    OrgStructureParser orgStructureParser = new OrgStructureParserImpl();
    Employee boss = orgStructureParser.parseStructure(csvFile);
    System.out.println(boss.getPosition() + " --> " + boss.getName());

    for (Employee s : boss.getSubordinate()
    ) {
      System.out.println("Boss Subordinate: " + s.getPosition());
    }
  }
}
