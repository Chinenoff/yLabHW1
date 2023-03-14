package org.example.homework2.complex.numbers;

public class ComplexNumbers {

  private final double real;
  private final double imaginary;

  public ComplexNumbers(double real) {
    this.real = real;
    this.imaginary = 0;
  }

  public ComplexNumbers(double real, double imagine) {
    this.real = real;
    this.imaginary = imagine;
  }

  public ComplexNumbers sum(ComplexNumbers number) {
    return new ComplexNumbers(this.real + number.real, this.imaginary + number.imaginary);
  }

  public ComplexNumbers subtract(ComplexNumbers number) {
    return new ComplexNumbers(this.real - number.real, this.imaginary - number.imaginary);
  }

  public ComplexNumbers multiply(ComplexNumbers number) {
    return new ComplexNumbers(this.real * number.real - this.imaginary * number.imaginary,
        this.real * number.imaginary + this.imaginary * number.real);
  }

  public double abs() {
    return Math.hypot(this.real, this.imaginary);
  }

  @Override
  public String toString() {
    return "" + real + ((imaginary < 0) ? ("-i" + (-imaginary)) : ("+i" + imaginary));
  }
}
