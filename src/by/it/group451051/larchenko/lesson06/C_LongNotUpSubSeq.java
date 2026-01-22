package by.it.group451051.larchenko.lesson06;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая невозростающая подпоследовательность

Дано:
    целое число 1<=n<=1E5 ( ОБРАТИТЕ ВНИМАНИЕ НА РАЗМЕРНОСТЬ! )
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    для которой каждый элемент A[i[k]] не больше любого предыдущего
    т.е. для всех 1<=j<k, A[i[j]]>=A[i[j+1]].

    В первой строке выведите её длину k,
    во второй - её индексы i[1]<i[2]<…<i[k]
    соблюдая A[i[1]]>=A[i[2]]>= ... >=A[i[n]].

    (индекс начинается с 1)

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ

    Sample Input:
    5
    5 3 4 4 2

    Sample Output:
    4
    1 3 4 5
*/


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class C_LongNotUpSubSeq {

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];
        //читаем всю последовательность
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // Для поиска длины LIS (Longest Non-increasing Subsequence)
        // Используем массив tail для хранения последних элементов подпоследовательностей
        List<Integer> tail = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        int[] prev = new int[n];  // Используем массив вместо List
        Arrays.fill(prev, -1);

        for (int i = 0; i < n; i++) {
            // Для невозрастающей последовательности используем >=
            // Бинарный поиск первого элемента меньшего чем m[i] (так как последовательность невозрастающая)
            int pos = Collections.binarySearch(tail, m[i], (a, b) -> Integer.compare(b, a));

            if (pos < 0) {
                pos = -(pos + 1);
            }

            if (pos == tail.size()) {
                tail.add(m[i]);
                indices.add(i);
            } else {
                tail.set(pos, m[i]);
                indices.set(pos, i);
            }

            // Запоминаем предыдущий элемент для восстановления
            if (pos > 0) {
                prev[i] = indices.get(pos - 1);
            }
        }

        // Восстановление последовательности
        List<Integer> resultIndices = new ArrayList<>();
        int current = indices.get(indices.size() - 1);
        while (current != -1) {
            resultIndices.add(current + 1); // +1 т.к. индексы с 1
            current = prev[current];
        }
        Collections.reverse(resultIndices);

        // Вывод результата
        System.out.println(tail.size());
        for (int i = 0; i < resultIndices.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(resultIndices.get(i));
        }
        System.out.println();

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return tail.size();
    }

    public static void main(String[] args) throws FileNotFoundException {
        String root = System.getProperty("user.dir") + "/src/";
        InputStream stream = new FileInputStream(root + "by/it/a_khmelev/lesson06/dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
    }
}