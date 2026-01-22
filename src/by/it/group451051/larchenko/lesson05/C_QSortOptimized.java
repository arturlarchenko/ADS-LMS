package by.it.group451051.larchenko.lesson05;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.

        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии,
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения

        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)

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

public class C_QSortOptimized {

    //отрезок
    private class Segment implements Comparable<Segment> {
        int start;
        int stop;

        Segment(int start, int stop) {
            this.start = Math.min(start, stop);
            this.stop = Math.max(start, stop);
        }

        @Override
        public int compareTo(Segment o) {
            // Сортируем по началу отрезка
            if (this.start != o.start) {
                return Integer.compare(this.start, o.start);
            }
            // Если начала равны, сортируем по концу
            return Integer.compare(this.stop, o.stop);
        }
    }

    // Трехчастное разбиение для быстрой сортировки (Dutch National Flag algorithm)
    private int[] partition3(Segment[] arr, int left, int right) {
        Segment pivot = arr[left];
        int less = left;
        int equal = left;
        int greater = right;

        while (equal <= greater) {
            int cmp = arr[equal].compareTo(pivot);
            if (cmp < 0) {
                // Элемент меньше опорного
                swap(arr, less, equal);
                less++;
                equal++;
            } else if (cmp > 0) {
                // Элемент больше опорного
                swap(arr, equal, greater);
                greater--;
            } else {
                // Элемент равен опорному
                equal++;
            }
        }

        return new int[]{less, greater};
    }

    private void swap(Segment[] arr, int i, int j) {
        Segment temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Быстрая сортировка с трехчастным разбиением и элиминацией хвостовой рекурсии
    private void quickSort3Way(Segment[] arr, int left, int right) {
        while (left < right) {
            int[] bounds = partition3(arr, left, right);
            int less = bounds[0];
            int greater = bounds[1];

            // Рекурсивно сортируем меньшую часть, итеративно обрабатываем большую
            if (less - left < right - greater) {
                quickSort3Way(arr, left, less - 1);
                left = greater + 1;
            } else {
                quickSort3Way(arr, greater + 1, right);
                right = less - 1;
            }
        }
    }

    // Бинарный поиск первого отрезка, который начинается после точки
    private int binarySearchFirstAfter(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = segments.length;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (segments[mid].start > point) {
                // Нашли отрезок, начинающийся после точки
                result = mid;
                right = mid - 1;
            } else {
                // Ищем дальше
                left = mid + 1;
            }
        }

        return result;
    }

    // Бинарный поиск последнего отрезка, который начинается до или в точке
    private int binarySearchLastBefore(Segment[] segments, int point) {
        int left = 0;
        int right = segments.length - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (segments[mid].start <= point) {
                // Нашли отрезок, начинающийся до или в точке
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    int[] getAccessory2(InputStream stream) {
        Scanner scanner = new Scanner(stream);

        // Число отрезков
        int n = scanner.nextInt();
        // Число точек
        int m = scanner.nextInt();

        Segment[] segments = new Segment[n];
        int[] points = new int[m];
        int[] result = new int[m];

        // Читаем отрезки
        for (int i = 0; i < n; i++) {
            int start = scanner.nextInt();
            int end = scanner.nextInt();
            segments[i] = new Segment(start, end);
        }

        // Читаем точки
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки с использованием трехчастного разбиения
        quickSort3Way(segments, 0, n - 1);

        // Создаем массивы для бинарного поиска
        int[] starts = new int[n];
        int[] ends = new int[n];

        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }

        // Сортируем концы отрезков для бинарного поиска
        Arrays.sort(ends);

        // Обрабатываем каждую точку
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Используем бинарный поиск для оптимизации
            // Находим индекс первого отрезка, который начинается после точки
            int firstAfter = binarySearchFirstAfter(segments, point);

            // Все отрезки, начинающиеся до или в точке, потенциально могут покрывать точку
            int count = 0;

            // Проверяем отрезки, которые начинаются до или в точке
            // Мы можем начать с последнего отрезка, начинающегося до точки
            int lastBefore = binarySearchLastBefore(segments, point);

            if (lastBefore >= 0) {
                // Проверяем отрезки в обратном порядке до тех пор, пока они могут покрывать точку
                for (int j = lastBefore; j >= 0; j--) {
                    if (segments[j].stop >= point) {
                        count++;
                    } else {
                        // Так как отрезки отсортированы по началу,
                        // если текущий отрезок уже не покрывает точку,
                        // то более ранние тоже не будут (т.к. их концы <= текущему)
                        break;
                    }
                }
            }

            result[i] = count;
        }

        scanner.close();
        return result;
    }

    // Альтернативная оптимизированная версия с использованием двух бинарных поисков
    int[] getAccessory2Optimized(InputStream stream) {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int m = scanner.nextInt();

        Segment[] segments = new Segment[n];
        int[] points = new int[m];
        int[] result = new int[m];

        for (int i = 0; i < n; i++) {
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        }

        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }

        // Сортируем отрезки
        quickSort3Way(segments, 0, n - 1);

        // Создаем отдельные массивы для начал и концов
        int[] starts = new int[n];
        int[] ends = new int[n];

        for (int i = 0; i < n; i++) {
            starts[i] = segments[i].start;
            ends[i] = segments[i].stop;
        }

        // Сортируем концы
        Arrays.sort(ends);

        // Для каждой точки
        for (int i = 0; i < m; i++) {
            int point = points[i];

            // Количество отрезков, начинающихся до или в точке
            int countStarts = binarySearchLastBefore(segments, point) + 1;

            if (countStarts > 0) {
                // Количество отрезков, заканчивающихся до точки
                int countEndsBefore = 0;
                int left = 0, right = n - 1;
                while (left <= right) {
                    int mid = left + (right - left) / 2;
                    if (ends[mid] < point) {
                        countEndsBefore = mid + 1;
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }

                // Отрезки, покрывающие точку = начинающиеся до/в точке - заканчивающиеся до точки
                result[i] = countStarts - countEndsBefore;
            } else {
                result[i] = 0;
            }
        }

        scanner.close();
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String root = System.getProperty("user.dir") + "/src/";
        InputStream stream = new FileInputStream(root + "by/it/a_khmelev/lesson05/dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result = instance.getAccessory2Optimized(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }
}
