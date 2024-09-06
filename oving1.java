import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class oving1 {
  public static int[] rate;

  public static void main(String[] args) {
    init();
    randomRates();
    optimalRate(rate);
  }

  public static void init() {
    rate = new int[10];
  }

  public static void ratesFromTextbook() {
    rate = new int[]{-1, 3, -9, 2, 2, -1, 2, -1, -5};
  }

  public static void randomRates() {
    for (int i = 0; i < rate.length; i++) {
      rate[i] = (int) (Math.random() * 10);
    }
    System.out.println(Arrays.toString(rate));
  }

  public static void optimalRate(int[] rate) {
    int optimalRate = 0;
    int sumOfRates = 0;
    int buyDay= 0;
    int sellDay = 0;
    int dayCount = 1;

    for (int i = 1; i < rate.length; i++) {
      sumOfRates += rate[i];
      if (sumOfRates > optimalRate) {
        optimalRate = sumOfRates;
        buyDay = dayCount;
        sellDay = i + 1;
      }
      if (sumOfRates < 0) {
        sumOfRates = 0;
        dayCount = i + 1;
      }
    }

    System.out.println("Max profit rate: " + optimalRate);
    System.out.println("Buy day: " + buyDay);
    System.out.println("Sell day: " + sellDay);
  }
}
