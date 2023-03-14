package org.example.homework2.statsAccumulator;

public class StatsAccumulatorImpl implements StatsAccumulator {

  private int currentNumber;
  private int minimumNumber;
  private int maximumNumber;
  private int numberCounter;
  private double arithmeticMeanOfNumbers;

  @Override
  public void add(int value) {
    currentNumber = value;
    numberCounter++;
    if (currentNumber < minimumNumber || numberCounter == 1) {
      minimumNumber = currentNumber;
    }
    if (currentNumber > maximumNumber) {
      maximumNumber = currentNumber;
    }
    arithmeticMeanOfNumbers =
        (arithmeticMeanOfNumbers * (numberCounter - 1) + currentNumber) / numberCounter;
  }

  @Override
  public int getMin() {
    return minimumNumber;
  }

  @Override
  public int getMax() {
    return maximumNumber;
  }

  @Override
  public int getCount() {
    return numberCounter;
  }

  @Override
  public Double getAvg() {
    return arithmeticMeanOfNumbers;
  }
}
