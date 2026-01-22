package by.it.group451051.larchenko.lesson03;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Lesson 3. C_Heap.
// Задача: построить max-кучу = пирамиду = бинарное сбалансированное дерево на массиве.
// ВАЖНО! НЕЛЬЗЯ ИСПОЛЬЗОВАТЬ НИКАКИЕ КОЛЛЕКЦИИ, КРОМЕ ARRAYLIST (его можно, но только для массива)

//      Проверка проводится по данным файла
//      Первая строка входа содержит число операций 1 ≤ n ≤ 100000.
//      Каждая из последующих nn строк задают операцию одного из следующих двух типов:

//      Insert x, где 0 ≤ x ≤ 1000000000 — целое число;
//      ExtractMax.

//      Первая операция добавляет число x в очередь с приоритетами,
//      вторая — извлекает максимальное число и выводит его.

//      Sample Input:
//      6
//      Insert 200
//      Insert 10
//      ExtractMax
//      Insert 5
//      Insert 500
//      ExtractMax
//
//      Sample Output:
//      200
//      500


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class C_HeapMax {

    private class MaxHeap {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        private List<Long> heap = new ArrayList<>();

        // Просеивание вниз (для восстановления кучи после удаления корня)
        int siftDown(int i) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int largest = i;

            // Проверяем, не является ли левый ребенок больше текущего узла
            if (left < heap.size() && heap.get(left) > heap.get(largest)) {
                largest = left;
            }

            // Проверяем, не является ли правый ребенок больше текущего наибольшего узла
            if (right < heap.size() && heap.get(right) > heap.get(largest)) {
                largest = right;
            }

            // Если наибольший элемент не текущий узел, меняем их местами и продолжаем
            if (largest != i) {
                swap(i, largest);
                return siftDown(largest);
            }

            return i;
        }

        // Просеивание вверх (для восстановления кучи после вставки)
        int siftUp(int i) {
            // Пока не дошли до корня и текущий узел больше родителя
            while (i > 0 && heap.get(i) > heap.get((i - 1) / 2)) {
                swap(i, (i - 1) / 2);
                i = (i - 1) / 2;
            }
            return i;
        }

        // Меняем местами два элемента в куче
        private void swap(int i, int j) {
            Long temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }

        // Вставка элемента в кучу
        void insert(Long value) {
            heap.add(value);  // Добавляем элемент в конец
            siftUp(heap.size() - 1);  // Восстанавливаем свойства кучи
        }

        // Извлечение и удаление максимального элемента (корня)
        Long extractMax() {
            if (heap.isEmpty()) {
                return null;
            }

            Long result = heap.get(0);  // Сохраняем максимальный элемент (корень)

            // Перемещаем последний элемент в корень
            heap.set(0, heap.get(heap.size() - 1));
            heap.remove(heap.size() - 1);

            // Восстанавливаем свойства кучи
            if (!heap.isEmpty()) {
                siftDown(0);
            }

            return result;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }

    // Эта процедура читает данные из файла, ее можно не менять.
    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;
        MaxHeap heap = new MaxHeap();
        Scanner scanner = new Scanner(stream);
        Integer count = scanner.nextInt();
        scanner.nextLine(); // Переходим на следующую строку после числа операций

        for (int i = 0; i < count; ) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null && res > maxValue) {
                    maxValue = res;
                }
                if (res != null) {
                    System.out.println(res); // Выводим извлеченный максимум
                }
                i++;
            } else if (s.startsWith("insert ") || s.startsWith("Insert ")) {
                String[] p = s.split(" ");
                if (p[0].equalsIgnoreCase("insert") && p.length > 1) {
                    heap.insert(Long.parseLong(p[1]));
                    i++;
                }
            }
        }
        scanner.close();
        return maxValue;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String root = System.getProperty("user.dir") + "/src/";
        InputStream stream = new FileInputStream(root + "by/it/a_khmelev/lesson03/heapData.txt");
        C_HeapMax instance = new C_HeapMax();
        System.out.println("MAX=" + instance.findMaxValue(stream));
    }
}