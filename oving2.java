import java.util.function.BiFunction;

public class oving2 {
  //O(n)
  public static double recursiveExponent1(double x, int n) {
    if (n == 1) {
      return  x;
    } else {
      return x * recursiveExponent1(x, n - 1);
    }
  }

  //O(log(n)
  public static double recursiveExponent2(double x, int n) {
    if (n == 1) {
      return x;
    } else if ((n & 1) != 1) { //n is even
      return recursiveExponent2(x * x, n / 2);
    } else { //n is odd
      return x * recursiveExponent2(x * x, (n - 1) / 2);
    }
  }

  public static double powFunc(double x, int n) {
    return Math.pow(x, n);
  }

  public static void checkCorrectness(double x, int n, double correctAnswer) {
    double svar = recursiveExponent1(x, n);
    if (svar != correctAnswer) {
      System.out.println("Feil i metode 1: " + svar + " != " + correctAnswer);
    }

    svar = recursiveExponent2(x, n);
    if (svar != correctAnswer) {
      System.out.println("Feil i metode 2: " + svar + " != " + correctAnswer);
    }

    svar = powFunc(x, n);
    if (svar != correctAnswer) {
      System.out.println("Feil i Math.pow: " + svar + " != " + correctAnswer);
    }
  }

  public static void testTime(BiFunction<Double, Integer, Double> func, double x, double n) {
    int count = 0;
    long start = System.nanoTime();
    double result = 0;
    while (System.nanoTime() - start < 1000000000) { //1 second in 1 000 000 000 ns
      result = func.apply(x, (int) n);
      count++;
    }
    System.out.println("n-verdi: " + n + "."
            + " Tid: " + Math.round(1000000000.0 / count) + " ns."
            + " Resultat: " + result);
  }

  public static void runTests(String methodName, BiFunction<Double, Integer, Double> method) {
    double x = 1.002;
    System.out.println("\n------ " + methodName + " ------");
    testTime(method, x, 128);
    testTime(method, x, 256);
    testTime(method, x, 512);
    testTime(method, x, 1024);
    testTime(method, x, 2048);
    testTime(method, x, 4096);
    testTime(method, x, 8192);
    testTime(method, x, 16384);
  }

  public static void main(String[] args) {
    checkCorrectness(5, 11, 48828125);
    checkCorrectness(2, 10, 1024);

    runTests("Metode 1, O(n)", oving2::recursiveExponent1);
    runTests("Metode 2, O(log(n))", oving2::recursiveExponent2);
    runTests("Math.pow", oving2::powFunc);
  }
}
