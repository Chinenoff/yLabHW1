package org.example.homework2.snilsValidator;

public class SnilsValidatorImpl implements SnilsValidator {

  @Override
  public boolean validate(String strSnils) {
    String validateString = strSnils.trim().replaceAll(" ", "");
    if (verifyStrSnils(validateString)) {
      int controlSumm = Integer.parseInt(validateString.substring(validateString.length() - 2));
      String num = validateString.substring(0, validateString.length() - 2).replaceAll("\\D", "");
      int checkNum = 0;
      for (int i = 0, j = 9; i < num.length(); i++, j--) {
        checkNum += ((num.charAt(i) - '0') * j);
      }
      if (checkNum > 100) {
        checkNum %= 101;
      }
      if (checkNum == 100) {
        checkNum = 0;
      }
      return checkNum == controlSumm;
    }
    return false;
  }

  private boolean verifyStrSnils(String strVal) {
      if (strVal == null || strVal.equals("") || strVal.length() != 11) {
          return false;
      }
      if (!strVal.matches("[0-9]*")) {
          return false;
      }
    return true;
  }
}