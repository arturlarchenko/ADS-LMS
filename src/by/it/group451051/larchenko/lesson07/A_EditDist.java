package by.it.group451051.larchenko.lesson07;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Рекурсивно вычислить расстояние редактирования двух данных непустых строк

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    0

    Sample Input 2:
    short
    ports
    Sample Output 2:
    3

    Sample Input 3:
    distance
    editing
    Sample Output 3:
    5

*/

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    private int[][] dp;
    private String one;
    private String two;

    // ИЗМЕНЕНИЕ: добавить public перед методом
    public int getDistanceEdinting(String one, String two) {
        this.one = one;
        this.two = two;
        int n = one.length();
        int m = two.length();
        dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                dp[i][j] = -1;
            }
        }

        return editDist(n, m);
    }

    private int editDist(int i, int j) {
        if (dp[i][j] != -1) return dp[i][j];

        if (i == 0) {
            dp[i][j] = j;
            return j;
        }
        if (j == 0) {
            dp[i][j] = i;
            return i;
        }

        int insert = editDist(i, j - 1) + 1;
        int delete = editDist(i - 1, j) + 1;
        int replace = editDist(i - 1, j - 1) +
                (one.charAt(i - 1) == two.charAt(j - 1) ? 0 : 1);

        dp[i][j] = Math.min(Math.min(insert, delete), replace);
        return dp[i][j];
    }

    public static void main(String[] args) throws FileNotFoundException {
        String root = System.getProperty("user.dir") + "/src/";
        InputStream stream = new FileInputStream(root + "by/it/a_khmelev/lesson07/dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);

        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}