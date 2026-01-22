package by.it.group451051.larchenko.lesson08;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Даны число 1<=n<=100 ступенек лестницы и
целые числа −10000<=a[1],…,a[n]<=10000, которыми помечены ступеньки.
Найдите максимальную сумму, которую можно получить, идя по лестнице
снизу вверх (от нулевой до n-й ступеньки), каждый раз поднимаясь на
одну или на две ступеньки.

Sample Input 1:
2
1 2
Sample Output 1:
3

Sample Input 2:
2
2 -1
Sample Output 2:
1

Sample Input 3:
3
-1 2 1
Sample Output 3:
3

*/

import java.io.*;
import java.util.Scanner;

public class C_Stairs {

    int getMaxSum(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int stairs[] = new int[n];
        for (int i = 0; i < n; i++) {
            stairs[i] = scanner.nextInt();
        }

        if (n == 0) return 0;
        if (n == 1) return Math.max(0, stairs[0]); // если ступенька одна, можно на неё не наступать?
        // По условию нужно дойти от 0 до n-й ступеньки, значит нужно наступить на все n ступенек?
        // Нет, нужно наступить на последнюю n-ю ступеньку, а промежуточные можно пропускать.
        // Но по заданию: "идя по лестнице снизу вверх (от нулевой до n-й ступеньки)" —
        // значит нулевая ступенька — это земля (вес 0), первая ступенька — это stairs[0].
        // На n-ю ступеньку нужно обязательно наступить.

        int[] dp = new int[n + 1];
        dp[0] = 0; // перед первой ступенькой
        dp[1] = stairs[0]; // первая ступенька

        for (int i = 2; i <= n; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2]) + stairs[i - 1];
        }

        int result = dp[n];
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String root = System.getProperty("user.dir") + "/src/";
        InputStream stream = new FileInputStream(root + "by/it/a_khmelev/lesson08/dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res = instance.getMaxSum(stream);
        System.out.println(res);
    }
}