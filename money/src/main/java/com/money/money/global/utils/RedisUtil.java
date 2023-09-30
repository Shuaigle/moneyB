package com.money.money.global.utils;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@AllArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    // Example method to set a key-value pair in Redis
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // Example method to get a value by key from Redis
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Example method to delete a key from Redis
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    // Example method to add an element to a Redis list
    public Long pushToList(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    // Example method to retrieve a range of elements from a Redis list
    public List<Object> getListRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    // Example method to add a member to a Redis set
    public Long addToSet(String key, Object value) {
        return redisTemplate.opsForSet().add(key, value);
    }

    // Example method to retrieve all members of a Redis set
    public Set<Object> getSetMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    // Example method to add fields and values to a Redis hash
    public void addToHash(String key, Map<String, String> fieldValues) {
        redisTemplate.opsForHash().putAll(key, fieldValues);
    }

    // Example method to retrieve a field from a Redis hash
    public String getFromHash(String key, String field) {
        return (String) redisTemplate.opsForHash().get(key, field);
    }

    // Add more fine-grained Redis operations as needed

    // Example method to delete a field from a Redis hash
    public void deleteFromHash(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }
}

