package org.example.homework2.complexNumbers;

public class ComplexTest {

  public static void main(String[] args) {
    ComplexNumbers a = new ComplexNumbers(4, -5);
    System.out.println("Complex number a: " + a);
    ComplexNumbers b = new ComplexNumbers(2, 6);
    System.out.println("Complex number b: " + b);

    System.out.println(a + " + " + b + " = " + a.sum(b));
    System.out.println(a + " - " + b + " = " + a.subtract(b));
    System.out.println(a + " . " + b + " = " + a.multiply(b));
    System.out.println("abs=" + a.abs());
  }
}
