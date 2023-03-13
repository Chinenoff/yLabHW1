package org.example.homework2.snilsValidator;

public class SnilsValidatorImpl implements SnilsValidator {
    @Override
    public boolean validate(String strSnils) {
        int controlSumm = Integer.parseInt(strSnils.substring(strSnils.length() - 2));
        String num = strSnils.substring(0, strSnils.length() - 2).replaceAll("\\D", "");

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
}