package org.example.homework2.complex.numbers;

public class ComplexTest {

  public static void main(String[] args) {
    ComplexNumbers numberA = new ComplexNumbers(4, -5);
    System.out.println("Complex number a: " + numberA);
    ComplexNumbers numberB = new ComplexNumbers(2, 6);
    System.out.println("Complex number b: " + numberB);

    System.out.println(numberA + " + " + numberB + " = " + numberA.sum(numberB));
    System.out.println(numberA + " - " + numberB + " = " + numberA.subtract(numberB));
    System.out.println(numberA + " . " + numberB + " = " + numberA.multiply(numberB));
    System.out.println("abs=" + numberA.abs());
  }
}
