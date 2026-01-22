package by.it.group451051.larchenko.lesson02;
/*
Даны
1) объем рюкзака 4
2) число возможных предметов 60
3) сам набор предметов
    100 50
    120 30
    100 50
Все это указано в файле (by/it/a_khmelev/lesson02/greedyKnapsack.txt)

Необходимо собрать наиболее дорогой вариант рюкзака для этого объема
Предметы можно резать на кусочки (т.е. алгоритм будет жадным)
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class C_GreedyKnapsack {
    private static class Item implements Comparable<Item> {
        int cost;
        int weight;
        double costPerWeight; // стоимость за единицу веса

        Item(int cost, int weight) {
            this.cost = cost;
            this.weight = weight;
            this.costPerWeight = (double) cost / weight;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "cost=" + cost +
                    ", weight=" + weight +
                    ", costPerWeight=" + String.format("%.3f", costPerWeight) +
                    '}';
        }

        @Override
        public int compareTo(Item o) {
            // Сортируем по убыванию стоимости за единицу веса
            return Double.compare(o.costPerWeight, this.costPerWeight);
        }
    }

    double calc(File source) throws FileNotFoundException {
        Scanner input = new Scanner(source);
        int n = input.nextInt();      //сколько предметов в файле
        int W = input.nextInt();      //какой вес у рюкзака
        Item[] items = new Item[n];   //получим список предметов
        for (int i = 0; i < n; i++) { //создавая каждый конструктором
            items[i] = new Item(input.nextInt(), input.nextInt());
        }
        //покажем предметы
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        // 1. Сортируем предметы по убыванию стоимости за единицу веса
        Arrays.sort(items);

        // 2. Жадный алгоритм: берем предметы с наибольшей удельной стоимостью
        double result = 0;
        int remainingWeight = W; // оставшаяся вместимость рюкзака

        for (Item item : items) {
            if (remainingWeight == 0) {
                break; // рюкзак заполнен
            }

            if (item.weight <= remainingWeight) {
                // Берем предмет целиком
                result += item.cost;
                remainingWeight -= item.weight;
                System.out.printf("Взяли предмет целиком: %s. Осталось места: %d кг\n",
                        item, remainingWeight);
            } else {
                // Берем часть предмета
                double fraction = (double) remainingWeight / item.weight;
                result += item.cost * fraction;
                System.out.printf("Взяли %.2f часть предмета: %s\n", fraction, item);
                remainingWeight = 0;
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        String root = System.getProperty("user.dir") + "/src/";
        File f = new File(root + "by/it/a_khmelev/lesson02/greedyKnapsack.txt");
        double costFinal = new C_GreedyKnapsack().calc(f);
        long finishTime = System.currentTimeMillis();
        System.out.printf("Общая стоимость %f (время %d)\n", costFinal, finishTime - startTime);
    }
}