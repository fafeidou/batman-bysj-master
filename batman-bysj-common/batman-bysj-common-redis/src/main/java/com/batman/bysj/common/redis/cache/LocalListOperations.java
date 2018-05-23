package com.batman.bysj.common.redis.cache;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * 基于本地缓存实现的 ListOperations
 * Created by jonas on 2017/1/13.
 */
public class LocalListOperations<K, V> implements ListOperations<K, V> {

    private final List<V> EMPTY = Collections.unmodifiableList(new ArrayList<V>(0));

    private List<V> getListOrCreate(K key) {
        List<V> list = LocalCacheContainer.getInstance().getListCache(key);
        if (list == null) {
            LocalCacheContainer.getInstance().put(key, list = new ArrayList<>());
        }
        return list;
    }

    /**
     * 取缓存集合内下标 start 到 end 的所有元素，包含 end
     * <ul>
     * <li><code>0 -> 1, 从 0 到 1 的所有元素</code></li>
     * <li><code>0 -> -3，从 0 到倒数第 3 个的所有元素</code></li>
     * <li><code>-1 -> -3，空，因为不是正序</code></li>
     * <li><code>-3 -> -1, 从倒数第 3 个到倒数第 1 个的所有元素</code></li>
     * </ul>
     * 对应如下, 设有集合 => [1, 3, 6]
     * <ul>
     * <li><code>0 -> 1, [1, 3]</code></li>
     * <li><code>0 -> -3，[1]</code></li>
     * <li><code>-1 -> -3，[]</code></li>
     * <li><code>-3 -> -1, [1, 3, 6]</code></li>
     * </ul>
     * 仍设有上述集合，超出索引范围时
     * <ul>
     * <li><code>0 -> 100, [1, 3, 6]</code></li>
     * <li><code>0 -> -300，[]</code></li>
     * <li><code>-3 -> -100, []</code></li>
     * <li><code>-100 -> -3, [1]</code></li>
     * </ul>
     */
    @Override
    public List<V> range(K k, long start, long end) {
        List<V> list = LocalCacheContainer.getInstance().getListCache(k);
        if (list == null)
            return EMPTY;
        long[] ascIndex = IndexConverter.convertRange(list.size(), start, end);
        if (ascIndex == null)
            return EMPTY;
        start = ascIndex[0];
        end = ascIndex[1];
        return LongStream.range(start, end).mapToObj(i -> list.get((int) i)).collect(Collectors.toList());
    }

    /**
     * 保留从 l 到 l1 的所有元素，其他全部删除，顺序计算参考 range 方法
     */
    @Override
    public void trim(K k, long l, long l1) {
        List<V> list = LocalCacheContainer.getInstance().getListCache(k);
        if (list == null)
            return;
        long[] ascIndex = IndexConverter.convertRange(list.size(), l, l1);
        if (ascIndex == null)
            return;
        l = ascIndex[0];
        l1 = ascIndex[1];
        List<V> trimmedList = LongStream.range(l, l1).mapToObj(i -> list.get((int) i)).collect(Collectors.toList());
        LocalCacheContainer.getInstance().put(k, trimmedList);
    }

    @Override
    public Long size(K key) {
        List list = LocalCacheContainer.getInstance().getListCache(key);
        return (long) (list == null ? 0 : list.size());
    }

    @Override
    public Long leftPush(K key, V value) {
        List<V> list = getListOrCreate(key);
        list.add(0, value);
        return (long) list.size();
    }

    @SafeVarargs
    @Override
    public final Long leftPushAll(K key, V... values) {
        List<V> list = getListOrCreate(key);
        for (V e : values) {
            list.add(0, e);
        }
        return (long) list.size();
    }

    @Override
    public Long leftPushAll(K key, Collection<V> values) {
        List<V> list = getListOrCreate(key);
        for (V e : values) {
            list.add(0, e);
        }
        return (long) list.size();
    }

    @Override
    public Long leftPushIfPresent(K key, V value) {
        List<V> list = LocalCacheContainer.getInstance().getListCache(key);
        if (list == null)
            return 0L;
        list.add(0, value);
        return (long) list.size();
    }

    /**
     * 在 pivot 元素的左侧，增加 value 元素
     */
    @Override
    public Long leftPush(K key, V pivot, V value) {
        List<V> list = getListOrCreate(key);
        int pivotIndex = list.indexOf(pivot);
        if (pivotIndex < 0)
            return (long) list.size();
        list.add(pivotIndex, value);
        return (long) list.size();
    }

    @Override
    public Long rightPush(K key, V value) {
        List<V> list = getListOrCreate(key);
        list.add(value);
        return (long) list.size();
    }

    @SafeVarargs
    @Override
    public final Long rightPushAll(K key, V... values) {
        List<V> list = getListOrCreate(key);
        Collections.addAll(list, values);
        return (long) list.size();
    }

    @Override
    public Long rightPushAll(K key, Collection<V> values) {
        List<V> list = getListOrCreate(key);
        list.addAll(values);
        return (long) list.size();
    }

    @Override
    public Long rightPushIfPresent(K key, V value) {
        List<V> list = LocalCacheContainer.getInstance().getListCache(key);
        if (list == null)
            return 0L;
        list.add(value);
        return (long) list.size();
    }

    @Override
    public Long rightPush(K key, V pivot, V value) {
        List<V> list = getListOrCreate(key);
        int pivotIndex = list.indexOf(pivot);
        if (pivotIndex < 0) {
            return (long) list.size();
        }
        list.add(pivotIndex + 1, value);
        return (long) list.size();
    }

    @Override
    public void set(K key, long index, V value) {
        List<V> list = LocalCacheContainer.getInstance().getListCache(key);
        list.set((int) index, value);
    }

    /**
     * 从集合中删除元素
     * <ul>
     * <li>如果 i 为正数，则正序查找 i 个值为 value 的元素并删除</li>
     * <li>如果 i 为负数，则倒叙查找 -i 个值为 value 的元素并删除</li>
     * <li>如果 i 为 0，则正序查找所有值为 value 的元素并删除</li>
     * </ul>
     */
    @Override
    public Long remove(K key, long i, Object value) {
        List<V> list = LocalCacheContainer.getInstance().getListCache(key);
        if (list == null || list.isEmpty())
            return 0L;

        List<Integer> removedIndex = new ArrayList<>();

        int size = list.size();
        if (i < 0) {
            for (--size; i != 0 && 0 < size; size--) {
                if (list.get(size).equals(value)) {
                    removedIndex.add(size);
                    i++;
                }
            }
        } else if (i > 0) {
            for (int x = 0; i != 0 && x < size; x++) {
                if (list.get(x).equals(value)) {
                    removedIndex.add(x);
                    i--;
                }
            }
        } else {
            for (int x = 0; x < size; x++) {
                if (list.get(x).equals(value)) {
                    removedIndex.add(x);
                }
            }
        }

        for (Integer x : removedIndex) {
            list.remove(x.intValue());
        }

        return (long) removedIndex.size();
    }

    @Override
    public V index(K key, long index) {
        List<V> list = LocalCacheContainer.getInstance().getListCache(key);
        if (index < 0) {
            index += list.size();
        }
        if (list == null || list.isEmpty() || index >= list.size() || index < 0)
            return null;
        return list.get((int) index);
    }

    @Override
    public V leftPop(K key) {
        List<V> list = LocalCacheContainer.getInstance().getListCache(key);
        if (list == null || list.isEmpty())
            return null;
        V e = list.get(0);
        list.remove(0);
        return e;
    }

    @Override
    public V leftPop(K key, long timeout, TimeUnit unit) {
        return leftPop(key);
    }

    @Override
    public V rightPop(K key) {
        List<V> list = LocalCacheContainer.getInstance().getListCache(key);
        if (list == null || list.isEmpty())
            return null;
        int lastIndex = list.size() - 1;
        V e = list.get(lastIndex);
        list.remove(lastIndex);
        return e;
    }

    @Override
    public V rightPop(K key, long timeout, TimeUnit unit) {
        return rightPop(key);
    }

    @Override
    public V rightPopAndLeftPush(K sourceKey, K destinationKey) {
        V e = rightPop(sourceKey);
        leftPush(destinationKey, e);
        return e;
    }

    @Override
    public V rightPopAndLeftPush(K sourceKey, K destinationKey, long timeout, TimeUnit unit) {
        return rightPopAndLeftPush(sourceKey, destinationKey);
    }

    @Override
    public RedisOperations<K, V> getOperations() {
        return null;
    }
}
