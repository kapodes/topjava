package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * GKislin
 * 15.06.2015.
 */
@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private Map<Integer, User> userDBId = new ConcurrentHashMap<>();
    private Map<String, User> userDBEmail = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    private Lock lock = new ReentrantLock();

    @Override
    public boolean delete(int id) {
        LOG.info("delete " + id);
        lock.lock();
        User delUser = userDBId.get(id);
        userDBEmail.remove(delUser.getEmail());
        lock.unlock();
        return userDBId.remove(id) != null;
    }

    @Override
    public User save(User user) {
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
        } else {
            lock.lock();
            userDBId.put(user.getId(), user);
            userDBEmail.put(user.getEmail(), user);
            lock.unlock();
        }

        LOG.info("save " + user);
        return user;
    }

    @Override
    public User get(int id) {
        LOG.info("get " + id);
        return userDBId.get(id);
    }

    @Override
    public List<User> getAll() {
        LOG.info("getAll");
        List<User> allUsers = new ArrayList<>(userDBId.values());
        Collections.sort(allUsers);
        return allUsers;
    }

    @Override
    public User getByEmail(String email) {
        LOG.info("getByEmail " + email);
        return userDBEmail.get(email);
    }
}
