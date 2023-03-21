package org.example.homework3.org.structure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrgStructureParserImpl implements OrgStructureParser {

  public Map<Long, Employee> employeeMap = new HashMap<>();

  @Override
  public Employee parseStructure(File csvFile) {
    try {
      employeeMap = parseCsvFileEmployee(csvFile);
      fillingEmptyFields();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return findBoss();
  }

  private Employee findBoss() {
    for (Long key : employeeMap.keySet()
    ) {
      if (employeeMap.get(key).getBossId() == null) {
        return employeeMap.get(key);
      }
    }
    return null;
  }

  private Map<Long, Employee> parseCsvFileEmployee(File csvFile) throws IOException {

    String cvsSplitBy = ";";

    Map<Long, Employee> result = new HashMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
      String line = br.readLine();
      if (line == null) {
        throw new IllegalArgumentException("File is empty");
      }
      if (!line.equals("id;boss_id;name;position")) {
        throw new IllegalArgumentException("File has wrong columns: " + line);
      }
      while ((line = br.readLine()) != null) {
        String[] items = line.split(cvsSplitBy);
        try {
          if (items.length > 4) {
            throw new ArrayIndexOutOfBoundsException();
          }
          Employee employee = new Employee();
          employee.setId(Long.parseLong(items[0]));
          employee.setBossId(convertStringToLong(items[1]));
          employee.setName(items[2]);
          employee.setPosition(items[3]);
          result.put(employee.getId(), employee);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException | NullPointerException e) {
          System.out.println("Invalid line: " + line);
        }
      }
      return result;
    }
  }

  private Long convertStringToLong(String str) {
    String lineSplitBy = "";
    if (str.equals(lineSplitBy)) {
      return null;
    }
    return Long.parseLong(str);
  }

  private void fillingEmptyFields() {
    for (Long key : employeeMap.keySet()
    ) {
      Employee value = employeeMap.get(key);
      if (value.getBossId() != null) {
        value.setBoss(employeeMap.get(value.getBossId()));
        List<Employee> employeeList = employeeMap.get(value.getBossId()).getSubordinate();
        employeeList.add(value);
      }
    }
  }
}
