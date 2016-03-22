package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;
import ru.javawebinar.topjava.util.UserMealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * GKislin
 * 15.09.2015.
 */
@Repository
public class InMemoryUserMealRepositoryImpl implements UserMealRepository {
    private Map<Integer, UserMeal> repository = new ConcurrentHashMap<>();
    private Map<Integer, List<UserMeal>> repositoryByUserId = new ConcurrentHashMap<>();

    private AtomicInteger counter = new AtomicInteger(0);
    private Lock lock = new ReentrantLock();


    {
        UserMealsUtil.MEAL_LIST.forEach(this::save);
    }

    @Override
    public UserMeal save(UserMeal userMeal) {
        if (userMeal.isNew()) {
            userMeal.setId(counter.incrementAndGet());
        }
        lock.lock();
        repository.put(userMeal.getId(), userMeal);

        repositoryByUserId.putIfAbsent(userMeal.getUserId(), new ArrayList<>());
        repositoryByUserId.get(userMeal.getUserId()).add(userMeal);

        lock.unlock();
        return userMeal;
    }

    @Override
    public void delete(int id) {
        lock.lock();
        repositoryByUserId.remove(repository.get(id).getUserId());
        repository.remove(id);
        lock.unlock();
    }

    @Override
    public UserMeal get(int id) {
        return repository.get(id);
    }

    @Override
    public Collection<UserMeal> getAll() {
        return repository.values();
    }

    @Override
    public Collection<UserMeal> getAllUsersMeal(int userId) {
        List<UserMeal> usersMeals = repositoryByUserId.get(userId);
        Collections.sort(usersMeals);
        return usersMeals;
    }
}

