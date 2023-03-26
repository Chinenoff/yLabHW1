package org.example.homework3.password.validator;

public class PasswordValidatorTest {

  public static void main(String[] args) {

    boolean authorization = PasswordValidator.checkAuthorizationInputParameters("123asdZXC_", "123", "123");

    System.out.println((authorization) ? "Authorization successful!!!" : "Access is denied!!!");

  }

}
