package com.fly.common.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.*;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存工具类
 *
 * @param <T>
 */
@Slf4j
public class RedisUtil<T> {

    private RedisUtil() {
    }

    public RedisUtil(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        init();
        log.info("默认tenant-common.RedisUtil工具类已加载...");
    }

    private RedisTemplate redisTemplate;

    protected HashOperations hashOperations;
    protected SetOperations setOperations;
    protected ValueOperations valueOperations;
    protected ZSetOperations zSetOperations;

    // 默认失效时间
    long redisExpire = 2592000L;

    private void init() {
        valueOperations = redisTemplate.opsForValue();
        hashOperations = redisTemplate.opsForHash();
        setOperations = redisTemplate.opsForSet();
        zSetOperations = redisTemplate.opsForZSet();
    }

    public RedisTemplate<Object, Object> getRedisTemplate() {
        return this.redisTemplate;
    }

    /*value*/
    public Boolean valueSetObject(String hkey, Object object) {
        return valueSetObject(hkey, object, false);
    }

    public Boolean valueSetObject(String hkey, Object object, long expire) {
        return valueSetObject(hkey, object, expire, true);
    }


    private Boolean valueSetObject(String hkey, Object object, boolean isExpire) {
        return valueSetObject(hkey, object, redisExpire, isExpire);
    }

    private Boolean valueSetObject(String hkey, Object object, long expire, boolean isExpire) {
        redisTemplate.opsForValue().set(hkey, object);
        boolean result = true;
        if (isExpire) {
            result = redisTemplate.expire(hkey, expire, TimeUnit.SECONDS);
        }
        return result;
    }

    public <T> T valueGetObject(String hkey) {
        return (T) redisTemplate.opsForValue().get(hkey);
    }


    public void valueSetString(String key, String value) {
        valueSetString(key, value, redisExpire);
    }

    public void valueSetString(String key, String value, long expire) {
        if (StringUtils.isEmpty(value)) {
            return;
        }
        valueOperations.set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * hash
     *
     * @param hkey     redis-key
     * @param field    map-key
     * @param value    map-value
     * @param expire   过期时间
     * @param isExpire 是否设置过期时间
     */
    private Boolean hashSetField(String hkey, String field, Object value, long expire, boolean isExpire) {
        if (null == value) {
            value = "";
        }
        boolean result = true;
        hashOperations.put(hkey, field, value);  // hkey指向一个hashMap{field: value}
        if (isExpire) {
            result = redisTemplate.expire(hkey, expire, TimeUnit.SECONDS);
        }
        return result;
    }

    /**
     * @param hkey   redis-key
     * @param field  map-key
     * @param value  map-value
     * @param expire 过期时间
     * @return
     */
    private Boolean hashSetField(String hkey, String field, Object value, long expire) {
        return hashSetField(hkey, field, value, expire, true);
    }

    /**
     * hash
     *
     * @param hkey  redis-key
     * @param field map-key
     * @param value map-value
     */
    public Boolean hashSetField(String hkey, String field, Object value) {
        return hashSetField(hkey, field, value, -1, false);
    }

    //存储map集合
    public void hashPutAll(String hkey, Map map) {
        if (StringUtils.isBlank(hkey)) {
            return;
        }
        hashOperations.putAll(hkey, map);
    }

    //返回map集合
    public <T> Map<String, T> hashEntries(String hkey) {
        return hashOperations.entries(hkey);
    }

    /*set*/
    public void putSet(String skey, Collection<T> value, Long expire) {
        if (null == value) {
            return;
        }
        delete(skey);
        for (T elem : value) {
            setOperations.add(skey, elem);
        }
        expire(skey, expire);
    }

    public void putSet(String skey, Collection<T> value) {
        if (null == value) {
            return;
        }
        putSet(skey, value, redisExpire);
    }

    public <T> Set<T> getCollectionSet(String lkey) {
        return setOperations.members(lkey);
    }

    /*zSet*/
    public Long addZSet(String key, Set<ZSetOperations.TypedTuple> set){
        return zSetOperations.add(key, set);
    }
    public <T> Set<T> rangeByScore(String key, double min, double max, long offset, long count){
        return zSetOperations.rangeByScore(key, min, max, offset, count);
    }

    /*template*/
    //通过key删除缓存
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    //批量删除缓存
    public void delete(Collection<String> key) {
        redisTemplate.delete(key);
    }

    public void deleteHashField(String hkey, String... field) {
        hashOperations.delete(hkey, field);
    }

    //key是否存在
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    //查询条件pattern，查询key集合
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    //key剩余时间
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    //设置key时间
    public Boolean expire(String key, Long num) {
        return redisTemplate.expire(key, num, TimeUnit.SECONDS);
    }

    //判断key是否存在,不存在则插入,返回true
    public Boolean setNX(String key, Object value) {
        return valueOperations.setIfAbsent(key, value);
    }
    //set值并返回set之前的值
    public Object getSet(String key, Object value) {
        return valueOperations.getAndSet(key, value);
    }
}