package by.it.group451051.larchenko.lesson05;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь.
На площади установлена одна или несколько камер.
Известны данные о том, когда каждая из них включалась и выключалась (отрезки работы)
Известен список событий на площади (время начала каждого события).
Вам необходимо определить для каждого события сколько камер его записали.

В первой строке задано два целых числа:
    число включений камер (отрезки) 1<=n<=50000
    число событий (точки) 1<=m<=50000.

Следующие n строк содержат по два целых числа ai и bi (ai<=bi) -
координаты концов отрезков (время работы одной какой-то камеры).
Последняя строка содержит m целых чисел - координаты точек.
Все координаты не превышают 10E8 по модулю (!).

Точка считается принадлежащей отрезку, если она находится внутри него или на границе.

Для каждой точки в порядке их появления во вводе выведите,
скольким отрезкам она принадлежит.
    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0

*/

import java.io.*;
import java.util.*;

public class A_QSort {

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            // Убедимся, что start всегда меньше или равен stop
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            // Сортируем сначала по началу отрезка, затем по концу
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            return Integer.compare(this.stop, o.stop);
        }
    }

    // Вспомогательный класс для сортировки точек с сохранением их исходного порядка
    private class PointWithIndex implements Comparable<PointWithIndex> {
        int value;
        int index;

        PointWithIndex(int value, int index) {
            this.value = value;
            this.index = index;
        }

        @Override
        public int compareTo(PointWithIndex o) {
            return Integer.compare(this.value, o.value);
        }
    }

    int[] getAccessory(InputStream stream) {
        Scanner scanner = new Scanner(stream);

        // Число отрезков
        int n = scanner.nextInt();
        // Число точек
        int m = scanner.nextInt();

        Segment[] segments = new Segment[n];
        int[] result = new int[m];

        // Читаем отрезки
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int end = scanner.nextInt();
            segments[i] = new Segment(start, end);
        }

        // Читаем точки с сохранением индексов
        PointWithIndex[] points = new PointWithIndex[m];
        for (int i = 0; i < m; i++) {
            points[i] = new PointWithIndex(scanner.nextInt(), i);
        }

        // Сортируем отрезки по началу
        quickSortSegments(segments, 0, n - 1);

        // Сортируем точки по значению
        quickSortPoints(points, 0, m - 1);

        // Приоритетная очередь (минимальная куча) для концов отрезков
        PriorityQueue<Integer> endQueue = new PriorityQueue<>();

        int segIndex = 0;

        // Обрабатываем точки в отсортированном порядке
        for (int i = 0; i < m; i++) {
            int point = points[i].value;
            int originalIndex = points[i].index;

            // Добавляем в очередь все отрезки, которые начинаются до или в момент точки
            while (segIndex < n && segments[segIndex].start <= point) {
                endQueue.add(segments[segIndex].stop);
                segIndex++;
            }

            // Удаляем из очереди все отрезки, которые уже закончились до точки
            while (!endQueue.isEmpty() && endQueue.peek() < point) {
                endQueue.poll();
            }

            // Размер очереди - количество отрезков, покрывающих текущую точку
            result[originalIndex] = endQueue.size();
        }

        scanner.close();
        return result;
    }

    // Быстрая сортировка для отрезков
    private void quickSortSegments(Segment[] arr, int low, int high) {
        if (low < high) {
            int pi = partitionSegments(arr, low, high);
            quickSortSegments(arr, low, pi - 1);
            quickSortSegments(arr, pi + 1, high);
        }
    }

    private int partitionSegments(Segment[] arr, int low, int high) {
        Segment pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j].compareTo(pivot) <= 0) {
                i++;
                Segment temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        Segment temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    // Быстрая сортировка для точек с индексами
    private void quickSortPoints(PointWithIndex[] arr, int low, int high) {
        if (low < high) {
            int pi = partitionPoints(arr, low, high);
            quickSortPoints(arr, low, pi - 1);
            quickSortPoints(arr, pi + 1, high);
        }
    }

    private int partitionPoints(PointWithIndex[] arr, int low, int high) {
        PointWithIndex pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j].compareTo(pivot) <= 0) {
                i++;
                PointWithIndex temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        PointWithIndex temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String root = System.getProperty("user.dir") + "/src/";
        InputStream stream = new FileInputStream(root + "by/it/a_khmelev/lesson05/dataA.txt");
        A_QSort instance = new A_QSort();
        int[] result = instance.getAccessory(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }
}