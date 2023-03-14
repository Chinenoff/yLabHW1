package org.example.homework2.rateLimitedPrinter;

public class RateLimitedPrinter {

  private final int interval;
  private long lastTripTime;

  public RateLimitedPrinter(int interval) {
    this.interval = interval;
    this.lastTripTime = 0;
  }

  public void print(String message) {
    long currentTime = System.currentTimeMillis();
    if (currentTime - lastTripTime > interval) {
      System.out.println(message);
      lastTripTime = currentTime;
    }
  }
}

