package org.example.homework3.password.validator;

import org.example.homework3.password.validator.exception.WrongLoginException;
import org.example.homework3.password.validator.exception.WrongPasswordException;

public class PasswordValidator {

  private static final String SYMBOL_PATTERN = ".*[\\w]";
  private static final String LENGHT_PATTERN = "(.{0,20})";


  public static boolean checkAuthorizationInputParameters(String login, String password,
      String confirmPassword) {

    try {
      boolean b = login.equals("");
      if (!login.matches(SYMBOL_PATTERN) & !login.equals("")) {
        throw new WrongLoginException("Логин содержит недопустимые символы ");
      } else if (!login.matches(LENGHT_PATTERN)) {
        throw new WrongLoginException("Логин слишком длинный ");
      }
      if (password.equals(confirmPassword)) {
        if (!password.matches(SYMBOL_PATTERN) & !password.equals("")) {
          throw new WrongPasswordException("Пароль содержит недопустимые символы ");
        } else if (!password.matches(LENGHT_PATTERN)) {
          throw new WrongPasswordException("Пароль слишком длинный");
        }
      } else {
        throw new WrongPasswordException("Пароль и подтверждение не совпадают");
      }
    } catch (WrongLoginException | WrongPasswordException e) {
      System.out.println("Caught the exception");
      System.out.println(e);
      return false;
    }
    return true;
  }

}
