package by.it.group451051.larchenko.lesson02;

import java.util.ArrayList;
import java.util.List;
/*
даны интервальные события events
реализуйте метод calcStartTimes, так, чтобы число принятых к выполнению
непересекающихся событий было максимально.
Алгоритм жадный. Для реализации обдумайте надежный шаг.
*/
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class B_Sheduler {
    //событие у аудитории(два поля: начало и конец)
    static class Event {
        int start;
        int stop;

        Event(int start, int stop) {
            this.start = start;
            this.stop = stop;
        }

        @Override
        public String toString() {
            return "("+ start +":" + stop + ")";
        }
    }

    public static void main(String[] args) {
        B_Sheduler instance = new B_Sheduler();
        Event[] events = {  new Event(0, 3),  new Event(0, 1), new Event(1, 2), new Event(3, 5),
                new Event(1, 3),  new Event(1, 3), new Event(1, 3), new Event(3, 6),
                new Event(2, 7),  new Event(2, 3), new Event(2, 7), new Event(7, 9),
                new Event(3, 5),  new Event(2, 4), new Event(2, 3), new Event(3, 7),
                new Event(4, 5),  new Event(6, 7), new Event(6, 9), new Event(7, 9),
                new Event(8, 9),  new Event(4, 6), new Event(8, 10), new Event(7, 10)
        };

        List<Event> starts = instance.calcStartTimes(events,0,10);  //рассчитаем оптимальное заполнение аудитории
        System.out.println(starts);                                 //покажем рассчитанный график занятий
    }

    List<Event> calcStartTimes(Event[] events, int from, int to) {
        List<Event> result = new ArrayList<>();

        // 1. Сортируем события по времени окончания (в порядке возрастания)
        Arrays.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                // Сначала сравниваем по времени окончания
                if (e1.stop != e2.stop) {
                    return Integer.compare(e1.stop, e2.stop);
                }
                // Если окончания совпадают, сортируем по началу
                return Integer.compare(e1.start, e2.start);
            }
        });

        // 2. Выбираем первое событие (с самым ранним окончанием)
        if (events.length > 0 && events[0].start >= from && events[0].stop <= to) {
            result.add(events[0]);
            int lastEndTime = events[0].stop; // время окончания последнего выбранного события

            // 3. Проходим по остальным событиям
            for (int i = 1; i < events.length; i++) {
                Event currentEvent = events[i];

                // Проверяем, что событие попадает в заданный интервал [from, to]
                if (currentEvent.start < from || currentEvent.stop > to) {
                    continue;
                }

                // 4. Если начало текущего события >= времени окончания последнего выбранного,
                // то это событие можно добавить (не пересекается)
                if (currentEvent.start >= lastEndTime) {
                    result.add(currentEvent);
                    lastEndTime = currentEvent.stop; // обновляем время окончания
                }
            }
        }

        return result;
    }
}