package org.example.homework2.sequences;

public class SequenceGeneratorTest {

  public static void main(String[] args) {
    final int amountOfElements = 6;

    SequenceGenerator sequenceGenerator = new SequenceGeneratorImpl();

    //запуск последовательности А
    System.out.println("последовательность А");
    sequenceGenerator.a(amountOfElements);

    //запуск последовательности B
    System.out.println("последовательность B");
    sequenceGenerator.b(amountOfElements);

    //запуск последовательности C
    System.out.println("последовательность C");
    sequenceGenerator.c(amountOfElements);

    //запуск последовательности D
    System.out.println("последовательность D");
    sequenceGenerator.d(amountOfElements);

    //запуск последовательности E
    System.out.println("последовательность E");
    sequenceGenerator.e(amountOfElements);

    //запуск последовательности F
    System.out.println("последовательность F");
    sequenceGenerator.f(amountOfElements);

    //запуск последовательности G
    System.out.println("последовательность G");
    sequenceGenerator.g(amountOfElements);

    //запуск последовательности H
    System.out.println("последовательность H");
    sequenceGenerator.h(amountOfElements);

    //запуск последовательности I
    System.out.println("последовательность I");
    sequenceGenerator.i(amountOfElements);

    //запуск последовательности J
    System.out.println("последовательность J");
    sequenceGenerator.j(amountOfElements);
  }
}
