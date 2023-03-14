package org.example.homework2.complexNumbers;

public class ComplexNumbers {

  private final double real;
  private final double imaginary;

  public ComplexNumbers(double r) {
    real = r;
    imaginary = 0;
  }

  public ComplexNumbers(double r, double im) {
    real = r;
    imaginary = im;
  }

  public ComplexNumbers sum(ComplexNumbers b) {
    return new ComplexNumbers(this.real + b.real, this.imaginary + b.imaginary);
  }

  public ComplexNumbers subtract(ComplexNumbers b) {
    return new ComplexNumbers(this.real - b.real, this.imaginary - b.imaginary);
  }

  public ComplexNumbers multiply(ComplexNumbers b) {
    return new ComplexNumbers(this.real * b.real - this.imaginary * b.imaginary,
        this.real * b.imaginary + this.imaginary * b.real);
  }

  public double abs() {
    return Math.hypot(this.real, this.imaginary);
  }

  @Override
  public String toString() {
    return "" + real + ((imaginary < 0) ? ("-i" + (-imaginary)) : ("+i" + imaginary));
  }
}
