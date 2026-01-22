package by.it.group451051.larchenko.lesson05;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Первая строка содержит число 1<=n<=10000, вторая - n натуральных чисел, не превышающих 10.
Выведите упорядоченную по неубыванию последовательность этих чисел.

При сортировке реализуйте метод со сложностью O(n)

Пример: https://karussell.wordpress.com/2010/03/01/fast-integer-sorting-algorithm-on/
Вольный перевод: http://programador.ru/sorting-positive-int-linear-time/
*/

import java.io.*;
import java.util.*;

public class B_CountSort {

    int[] countSort(InputStream stream) {
        Scanner scanner = new Scanner(stream);

        // Размер массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        // Читаем числа
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // Так как числа не превышают 10, создаем массив счетчиков размером 11
        // (индексы от 0 до 10)
        int[] count = new int[11];

        // 1. Подсчитываем количество вхождений каждого числа
        for (int i = 0; i < n; i++) {
            // Проверяем, что число действительно в диапазоне 0-10
            if (points[i] < 0 || points[i] > 10) {
                throw new IllegalArgumentException("Число должно быть в диапазоне от 0 до 10");
            }
            count[points[i]]++;
        }

        // 2. Преобразуем массив count так, чтобы каждый элемент содержал
        // количество чисел, меньших или равных данному индексу
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        // 3. Создаем результирующий массив
        int[] result = new int[n];

        // 4. Заполняем результирующий массив, начиная с конца
        // для сохранения стабильности сортировки
        for (int i = n - 1; i >= 0; i--) {
            int value = points[i];
            // Уменьшаем счетчик для данного значения
            count[value]--;
            // Помещаем элемент на правильную позицию
            result[count[value]] = value;
        }

        scanner.close();
        return result;
    }

    // Альтернативная версия - более простая, но менее эффективная по памяти
    // (требует создания нового массива)
    int[] countSortSimple(InputStream stream) {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] points = new int[n];

        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }

        // Массив для подсчета (числа от 0 до 10)
        int[] count = new int[11];

        // Подсчитываем каждое число
        for (int num : points) {
            if (num < 0 || num > 10) {
                throw new IllegalArgumentException("Число должно быть в диапазоне от 0 до 10");
            }
            count[num]++;
        }

        // Заполняем исходный массив отсортированными числами
        int index = 0;
        for (int i = 0; i < count.length; i++) {
            // Записываем число i столько раз, сколько оно встретилось
            for (int j = 0; j < count[i]; j++) {
                points[index] = i;
                index++;
            }
        }

        scanner.close();
        return points;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String root = System.getProperty("user.dir") + "/src/";
        InputStream stream = new FileInputStream(root + "by/it/a_khmelev/lesson05/dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }
}