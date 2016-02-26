package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        List<UserMealWithExceed> mealListExceeded = getFilteredMealsWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        mealListExceeded.stream().forEach(System.out::println);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredMealsWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with correctly exceeded field

        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();

        //считаем количество калорий по каждому дню
        mealList.stream()
                .forEach(userMeal -> {
                    caloriesPerDayMap.computeIfPresent(userMeal.getDateTime().toLocalDate(), (k, v) -> v + userMeal.getCalories());
                    caloriesPerDayMap.putIfAbsent((userMeal.getDateTime().toLocalDate()), userMeal.getCalories());
                });

        //отбираем данные между датами с превышенными калориями
        List<UserMealWithExceed> result = mealList
                .stream()
                .filter(userMeal -> {
                    if (caloriesPerDayMap.get(userMeal.getDateTime().toLocalDate()) <= caloriesPerDay) return false;
                    LocalTime mealTime = userMeal.getDateTime().toLocalTime();
                    return TimeUtil.isBetween(mealTime, startTime, endTime);
                })
                .map(userMeal -> {
                    return new UserMealWithExceed(userMeal, true);
                })
                .collect(Collectors.toList());

        return result;
    }
}
