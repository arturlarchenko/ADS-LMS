package by.it.group451051.larchenko.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m.
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private long startTime = System.currentTimeMillis();

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        long n = 10;
        int m = 2;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    long fasterC(long n, int m) {
        // Находим период Пизано для модуля m
        long period = getPisanoPeriod(m);

        // Находим эквивалентный индекс в пределах периода
        long equivalentIndex = n % period;

        // Вычисляем число Фибоначчи по модулю m только для этого индекса
        return fibonacciModM(equivalentIndex, m);
    }

    private long getPisanoPeriod(int m) {
        // Период Пизано всегда начинается с 0, 1
        long prev = 0;
        long curr = 1;
        long period = 0;

        // Ищем период: последовательность (0, 1) должна повториться
        for (int i = 0; i <= m * m; i++) {
            long temp = curr;
            curr = (prev + curr) % m;
            prev = temp;
            period++;

            // Если мы нашли начало периода (0, 1)
            if (prev == 0 && curr == 1) {
                return period;
            }
        }

        return period;
    }

    private long fibonacciModM(long n, int m) {
        if (n == 0) return 0;
        if (n == 1) return 1;

        long prevPrev = 0;
        long prev = 1;
        long current = 0;

        for (long i = 2; i <= n; i++) {
            current = (prev + prevPrev) % m;
            prevPrev = prev;
            prev = current;
        }

        return current;
    }
}