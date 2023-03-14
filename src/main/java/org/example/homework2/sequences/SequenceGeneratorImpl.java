package org.example.homework2.sequences;

public class SequenceGeneratorImpl implements SequenceGenerator {

  @Override
  public void a(int n) {
    long num = 2;
    for (int i = 0; i < n; i++) {
      System.out.println(num);
      num += 2;
    }
  }

  @Override
  public void b(int n) {
    long num = 1;
    for (int i = 0; i < n; i++) {
      System.out.println(num);
      num += 2;
    }
  }

  @Override
  public void c(int n) {
    for (int i = 1; i <= n; i++) {
      System.out.println(i * i);
    }
  }

  @Override
  public void d(int n) {
    for (int i = 1; i <= n; i++) {
      System.out.println(i * i * i);
    }
  }

  @Override
  public void e(int n) {
    for (int i = 1; i <= n; i++) {
      int num = (i % 2) == 0 ? -1 : 1;
      System.out.println(num);
    }
  }

  @Override
  public void f(int n) {
    for (int i = 1; i <= n; i++) {
      int num = (i % 2) == 0 ? i * -1 : i;
      System.out.println(num);
    }

  }

  @Override
  public void g(int n) {
    for (int i = 1; i <= n; i++) {
      int num = (i % 2) == 0 ? i * -i : i * i;
      System.out.println(num);
    }
  }

  @Override
  public void h(int n) {
    for (int i = 1; i <= n; i++) {
      System.out.println(i);
      System.out.println(0);
    }
  }

  @Override
  public void i(int n) {
    int num = 1;
    for (int i = 1; i <= n; i++) {
      num = num * i;
      System.out.println(num);
    }
  }

  @Override
  public void j(int n) {
    int num;
    int prevNum1 = 0;
    int prevNum2 = 0;
    for (int i = 1; i <= n; i++) {
      num = (prevNum1 > 0 || prevNum2 > 0) ? prevNum1 + prevNum2 : 1;
      prevNum2 = prevNum1;
      prevNum1 = num;
      System.out.println(num);
    }
  }
}
