package com.batman.bysj.common.redis.cache;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

/**
 * 基于本地缓存实现的 ValueOperations
 * Created by jonas on 2017/1/13.
 */
public class LocalValueOperations<K, V> implements ValueOperations<K, V> {
    @Override
    public void set(K key, V value) {
        LocalCacheContainer.getInstance().put(key, value);
    }

    @Override
    public void set(K key, V value, long timeout, TimeUnit unit) {
        set(key, value);
    }

    @Override
    public Boolean setIfAbsent(K key, V value) {
        V e = get(key);
        if (e == null) {
            LocalCacheContainer.getInstance().put(key, value);
            return true;
        }
        return false;
    }

    @Override
    public void multiSet(Map<? extends K, ? extends V> m) {
        m.forEach(this::set);
    }

    @Override
    public Boolean multiSetIfAbsent(Map<? extends K, ? extends V> m) {
        boolean result = false;
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            boolean _r = setIfAbsent(e.getKey(), e.getValue());
            if (_r) result = true;
        }
        return result;
    }

    @Override
    public V get(Object key) {
        return LocalCacheContainer.getInstance().getValueCache(key);
    }

    @Override
    public V getAndSet(K key, V value) {
        V e = get(key);
        set(key, value);
        return e;
    }

    @Override
    public List<V> multiGet(Collection<K> keys) {
        return keys.stream().map(this::get).collect(Collectors.toList());
    }

    @Override
    public Long increment(K key, long delta) {
        return increment(key, (double) delta).longValue();
    }

    @Override
    public Double increment(K key, double delta) {
        V e = get(key);
        Double d;
        if (e == null) {
            d = 0D;
        } else {
            String str = e.toString();
            if (NumberUtils.isCreatable(str)) {
                d = Double.valueOf(str);
            } else {
                throw new InvalidDataAccessApiUsageException("ERR value is not an integer or out of range");
            }
        }
        d += delta;
        LocalCacheContainer.getInstance().put(key, new DecimalFormat("#.##").format(d));
        return d;
    }

    @Override
    public Integer append(K key, String value) {
        String e = LocalCacheContainer.getInstance().getValueCache(key);
        if (e == null) {
            e = value;
        } else {
            e += value;
        }
        LocalCacheContainer.getInstance().put(key, e);
        return e.length();
    }

    @Override
    public String get(K key, long start, long end) {
        String e = LocalCacheContainer.getInstance().getValueCache(key);
        if (e == null) {
            return "";
        }
        long[] validRange = IndexConverter.convertRange(e.length(), start, end);
        if (validRange == null)
            return "";

        start = validRange[0];
        end = validRange[1];

        return e.substring((int) start, (int) end);
    }

    @Override
    public void set(K key, V value, long offset) {
        if (offset < 0) {
            throw new InvalidDataAccessApiUsageException("ERR offset is out of range");
        }
        String e = LocalCacheContainer.getInstance().getValueCache(key);
        if (e == null) {
            LocalCacheContainer.getInstance().put(key, value.toString());
            return;
        }

        if (offset > e.length()) {
            e += IntStream.range(0, (int) (offset - e.length())).mapToObj(it -> " ").collect(joining());
        }

        String _v = value.toString();

        Pattern regex = Pattern.compile(String.format("^(.{0,%s})(.{0,%s})", offset, _v.length()));

        e = regex.matcher(e).replaceFirst(_v);

        LocalCacheContainer.getInstance().put(key, e);
    }

    @Override
    public Long size(K key) {
        return (long) get(key).toString().length();
    }

    @Override
    public RedisOperations<K, V> getOperations() {
        return null;
    }

    @Override
    public Boolean setBit(K key, long offset, boolean value) {
        return null;
    }

    @Override
    public Boolean getBit(K key, long offset) {
        return null;
    }
}
