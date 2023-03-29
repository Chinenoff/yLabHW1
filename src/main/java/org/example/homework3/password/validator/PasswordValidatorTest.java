package org.example.homework3.password.validator;

public class PasswordValidatorTest {

  public static void main(String[] args) {

    boolean authorization = PasswordValidator.checkAuthorizationInputParameters("123asdZXC_", "docker-compose.yml", "docker-compose.yml");

    System.out.println((authorization) ? "Authorization successful!!!" : "Access is denied!!!");

  }

}
