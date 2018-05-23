package com.batman.bysj.common.redis.cache;

import static com.batman.bysj.common.redis.cache.LocalCacheContainer.getInstance;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;

import java.util.*;
import java.util.stream.Collectors;
/**
 * 基于同步 SortedSet 实现的本地 Set 缓存
 * Created by jonas on 2017/1/13.
 */
public class LocalZSetOperations<K, V> implements ZSetOperations<K, V> {

    private final Set<V> EMPTY = Collections.unmodifiableSortedSet(new TreeSet<V>());
    private final Set<TypedTuple<V>> EMPTY_WITH_SCORE = Collections.unmodifiableSortedSet(new TreeSet<TypedTuple<V>>());

    private Set<TypedTuple<V>> getSetOrCreate(K key) {
        Set<TypedTuple<V>> set = LocalCacheContainer.getInstance().getZSetCahce(key);
        if (set == null) {
            LocalCacheContainer.getInstance().put(key, set = new TreeSet<>());
        }
        return set;
    }

    private TypedTuple<V> getByValue(Set<TypedTuple<V>> set, TypedTuple<V> typedTuple) {
        for (TypedTuple<V> typedTuple1 : set) {
            if (Objects.equals(typedTuple.getValue(), typedTuple1.getValue()))
                return typedTuple1;
        }
        return null;
    }

    private Set<V> collectValue(Collection<TypedTuple<V>> collection, double min, double max, long offset, long count) {
        Set<V> resultSet = new LinkedHashSet<>();

        for (TypedTuple<V> typedTuple : collection) {

            double score = typedTuple.getScore();
            if (score >= min && score <= max) {
                if (offset > 0) {
                    offset--;
                    continue;
                }
                resultSet.add(typedTuple.getValue());
                if (count > 0 && resultSet.size() == count) {
                    break;
                }
            }
        }
        return resultSet;
    }

    private Set<TypedTuple<V>> collectValueWithScore(Collection<TypedTuple<V>> collection, double min, double max, long offset, long count) {
        Set<TypedTuple<V>> resultSet = new LinkedHashSet<>();

        for (TypedTuple<V> typedTuple : collection) {

            double score = typedTuple.getScore();
            if (score >= min && score <= max) {
                if (offset > 0) {
                    offset--;
                    continue;
                }
                resultSet.add(typedTuple);
                if (count > 0 && resultSet.size() == count) {
                    break;
                }
            }
        }
        return resultSet;
    }

    class LocalTypedTuple extends DefaultTypedTuple<V> {
        LocalTypedTuple(V value, Double score) {
            super(value, score);
        }

        @SuppressWarnings("unchecked")
        @Override
        public int compareTo(TypedTuple<V> o) {
            int byScore = super.compareTo(o);
            if (byScore != 0) {
                return byScore;
            }

            Object value = getValue();

            if (!(value instanceof Comparable)) {
                return byScore;
            }

            return ((Comparable) value).compareTo(o.getValue());
        }
    }

    @Override
    public Long intersectAndStore(K key, K otherKey, K destKey) {

        Set<TypedTuple<V>> set1 = getInstance().getZSetCahce(key);
        Set<TypedTuple<V>> set2 = getInstance().getZSetCahce(otherKey);
        Set<TypedTuple<V>> set3 = new TreeSet<>();

        if (set1 == null || set2 == null || set1.isEmpty() || set2.isEmpty()) {
            getInstance().put(destKey, set3);
            return 0L;
        }

        for (TypedTuple<V> typedTuple : set1) {
            TypedTuple<V> byValue = getByValue(set2, typedTuple);
            if (byValue != null) {
                set3.add(new LocalTypedTuple(byValue.getValue(), byValue.getScore() + typedTuple.getScore()));
            }
        }

        getInstance().put(destKey, set3);

        return (long) set3.size();
    }

    @Override
    public Long intersectAndStore(K key, Collection<K> otherKeys, K destKey) {
        Set<TypedTuple<V>> set1 = getInstance().getZSetCahce(key);
        Set<TypedTuple<V>> set2;

        if (set1 == null || set1.isEmpty()) {
            return 0L;
        }

        Map<V, TypedTuple<V>> map = new HashMap<>();

        for (K otherKey : otherKeys) {
            set2 = getInstance().getZSetCahce(otherKey);
            if (set2 == null || set2.isEmpty()) {
                continue;
            }
            for (TypedTuple<V> typedTuple : set1) {
                TypedTuple<V> byValue = getByValue(set2, typedTuple);
                if (byValue != null) {
                    V value = byValue.getValue();
                    TypedTuple<V> byValueFromSet3 = map.get(value);
                    double score = byValue.getScore();
                    if (byValueFromSet3 != null) {
                        score += byValueFromSet3.getScore();
                    } else {
                        score += typedTuple.getScore();
                    }
                    map.put(value, new LocalTypedTuple(value, score));
                }
            }
        }

        getInstance().put(destKey, new TreeSet<>(map.values()));

        return (long) map.size();
    }

    @Override
    public Long unionAndStore(K key, K otherKey, K destKey) {
        Set<TypedTuple<V>> set1 = getInstance().getZSetCahce(key);
        Set<TypedTuple<V>> set2 = getInstance().getZSetCahce(otherKey);
        Set<TypedTuple<V>> set3 = new TreeSet<>();

        int set1Size = 0;

        if (set1 != null && !set1.isEmpty()) {
            for (TypedTuple<V> typedTuple : set1) {
                set3.add(new LocalTypedTuple(typedTuple.getValue(), typedTuple.getScore()));
            }
            set1Size = set1.size();
        }

        if (set2 != null && !set2.isEmpty()) {
            for (TypedTuple<V> typedTuple : set2) {
                double score = typedTuple.getScore();
                if (set1Size > 0) {
                    TypedTuple<V> byValue = getByValue(set3, typedTuple);
                    if (byValue != null) {
                        set1Size--;
                        score += byValue.getScore();
                        set3.remove(byValue);
                    }
                }
                set3.add(new LocalTypedTuple(typedTuple.getValue(), score));
            }
        }

        getInstance().put(destKey, set3);

        return (long) set3.size();
    }

    @Override
    public Long unionAndStore(K key, Collection<K> otherKeys, K destKey) {
        Set<TypedTuple<V>> set1 = getInstance().getZSetCahce(key);
        Set<TypedTuple<V>> set2;
        Map<V, TypedTuple<V>> map3 = new HashMap<>();

        if (set1 != null && !set1.isEmpty()) {
            for (TypedTuple<V> typedTuple : set1) {
                map3.put(typedTuple.getValue(), new LocalTypedTuple(typedTuple.getValue(), typedTuple.getScore()));
            }
        }

        for (K otherKey : otherKeys) {
            set2 = getInstance().getZSetCahce(otherKey);
            if (set2 == null || set2.isEmpty()) {
                continue;
            }
            for (TypedTuple<V> typedTuple : set2) {
                double score = typedTuple.getScore();
                V value = typedTuple.getValue();

                TypedTuple<V> byValue = map3.get(value);
                if (byValue != null) {
                    score += byValue.getScore();
                }

                map3.put(value, new LocalTypedTuple(typedTuple.getValue(), score));
            }
        }

        getInstance().put(destKey, new TreeSet<>(map3.values()));

        return (long) map3.size();
    }

    @Override
    public Set<V> range(K key, long start, long end) {
        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);
        if (set == null || set.isEmpty()) {
            return EMPTY;
        }

        long[] ascIndex = IndexConverter.convertRange(set.size(), start, end);
        if (ascIndex == null) {
            return EMPTY;
        }

        start = ascIndex[0];
        end = ascIndex[1];

        Set<V> resultSet = new TreeSet<>();
        int index = 0;
        for (TypedTuple<V> typedTuple : set) {
            if (index >= start && index < end) {
                resultSet.add(typedTuple.getValue());
            }
            index++;
        }

        return resultSet;
    }

    @Override
    public Set<V> reverseRange(K key, long start, long end) {
        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);
        if (set == null || set.isEmpty()) {
            return EMPTY;
        }

        long[] ascIndex = IndexConverter.convertRange(set.size(), start, end);
        if (ascIndex == null) {
            return EMPTY;
        }

        start = ascIndex[0];
        end = ascIndex[1];

        List<V> list = new ArrayList<>();
        for (TypedTuple<V> typedTuple : set) {
            list.add(0, typedTuple.getValue());
        }
        return new LinkedHashSet<>(list.subList((int) start, (int) end));
    }

    @Override
    public Set<TypedTuple<V>> rangeWithScores(K key, long start, long end) {
        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);
        if (set == null || set.isEmpty()) {
            return EMPTY_WITH_SCORE;
        }

        long[] ascIndex = IndexConverter.convertRange(set.size(), start, end);
        if (ascIndex == null) {
            return EMPTY_WITH_SCORE;
        }

        start = ascIndex[0];
        end = ascIndex[1];

        Set<TypedTuple<V>> resultSet = new LinkedHashSet<>();
        int index = 0;
        for (TypedTuple<V> typedTuple : set) {
            if (index >= start && index < end) {
                resultSet.add(typedTuple);
            }
            index++;
        }

        return resultSet;
    }

    @Override
    public Set<TypedTuple<V>> reverseRangeWithScores(K key, long start, long end) {
        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);
        if (set == null || set.isEmpty()) {
            return EMPTY_WITH_SCORE;
        }

        long[] ascIndex = IndexConverter.convertRange(set.size(), start, end);
        if (ascIndex == null) {
            return EMPTY_WITH_SCORE;
        }

        start = ascIndex[0];
        end = ascIndex[1];

        List<TypedTuple<V>> list = new ArrayList<>();
        for (TypedTuple<V> typedTuple : set) {
            list.add(0, typedTuple);
        }
        return new LinkedHashSet<>(list.subList((int) start, (int) end));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<V> rangeByLex(K key, RedisZSetCommands.Range range) {
        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);

        if (set == null || set.isEmpty()) {
            return EMPTY;
        }

        RedisZSetCommands.Range.Boundary max = range.getMax();
        RedisZSetCommands.Range.Boundary min = range.getMin();

        if (max == null && min == null) {
            return set.stream().map(TypedTuple::getValue).collect(Collectors.toSet());
        }

        Set<V> resultSet = new LinkedHashSet<>();

        for (TypedTuple<V> typedTuple : set) {
            V value = typedTuple.getValue();
            if (!(value instanceof Comparable)) {
                throw new UnsupportedClassVersionError("暂时不支持对非 Comparable 类型的此类操作。但这仅限本地模拟缓存");
            }
            Comparable comparable = ((Comparable) value);
            boolean pass = true;
            int compare;
            if (max != null) {
                compare = comparable.compareTo(max.getValue());
                pass = max.isIncluding() ? compare <= 0 : compare < 0;
            }

            if (!pass) {
                continue;
            }

            if (min != null) {
                compare = comparable.compareTo(min.getValue());
                pass = min.isIncluding() ? compare >= 0 : compare > 0;
            }

            if (pass) {
                resultSet.add(typedTuple.getValue());
            }
        }

        return resultSet;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<V> rangeByLex(K key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        int offset = limit.getOffset();
        int count = limit.getCount();

        if (offset < 0 || count == 0) {
            return EMPTY;
        }

        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);

        if (set == null || set.isEmpty()) {
            return EMPTY;
        }

        RedisZSetCommands.Range.Boundary max = range.getMax();
        RedisZSetCommands.Range.Boundary min = range.getMin();

        if (max == null && min == null) {
            return set.stream().map(TypedTuple::getValue).collect(Collectors.toSet());
        }

        Set<V> resultSet = new LinkedHashSet<>();

        for (TypedTuple<V> typedTuple : set) {

            V value = typedTuple.getValue();
            if (!(value instanceof Comparable)) {
                throw new UnsupportedClassVersionError("暂时不支持对非 Comparable 类型的此类操作。但这仅限本地模拟缓存");
            }

            Comparable comparable = ((Comparable) value);
            boolean pass = true;
            int compare;
            if (max != null) {
                compare = comparable.compareTo(max.getValue());
                pass = max.isIncluding() ? compare <= 0 : compare < 0;
            }

            if (!pass) {
                continue;
            }

            if (min != null) {
                compare = comparable.compareTo(min.getValue());
                pass = min.isIncluding() ? compare >= 0 : compare > 0;
            }

            if (pass) {
                if (offset > 0) {
                    offset--;
                    continue;
                }
                resultSet.add(typedTuple.getValue());
                if (count > 0 && resultSet.size() == count) {
                    break;
                }
            }
        }

        return resultSet;
    }

    @Override
    public Set<V> rangeByScore(K key, double min, double max) {
        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);

        if (set == null || set.isEmpty()) {
            return EMPTY;
        }

        Set<V> resultSet = new LinkedHashSet<>();
        for (TypedTuple<V> typedTuple : set) {
            double score = typedTuple.getScore();
            if (score >= min && score <= max) {
                resultSet.add(typedTuple.getValue());
            }
        }

        return resultSet;
    }

    @Override
    public Set<V> rangeByScore(K key, double min, double max, long offset, long count) {
        if (offset < 0 || count == 0) {
            return EMPTY;
        }

        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);

        if (set == null || set.isEmpty()) {
            return EMPTY;
        }

        return collectValue(set, min, max, offset, count);
    }

    @Override
    public Set<V> reverseRangeByScore(K key, double min, double max) {
        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);

        if (set == null || set.isEmpty()) {
            return EMPTY;
        }

        List<TypedTuple<V>> reverseList = new ArrayList<>();

        for (TypedTuple<V> typedTuple : set) {
            reverseList.add(0, typedTuple);
        }

        Set<V> resultSet = new LinkedHashSet<>();
        for (TypedTuple<V> typedTuple : reverseList) {
            double score = typedTuple.getScore();
            if (score >= min && score <= max) {
                resultSet.add(typedTuple.getValue());
            }
        }

        return resultSet;
    }

    @Override
    public Set<V> reverseRangeByScore(K key, double min, double max, long offset, long count) {
        if (offset < 0 || count == 0) {
            return EMPTY;
        }

        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);

        if (set == null || set.isEmpty()) {
            return EMPTY;
        }

        List<TypedTuple<V>> reverseList = new ArrayList<>();

        for (TypedTuple<V> typedTuple : set) {
            reverseList.add(0, typedTuple);
        }

        return collectValue(reverseList, min, max, offset, count);
    }

    @Override
    public Set<TypedTuple<V>> rangeByScoreWithScores(K key, double min, double max) {
        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);

        if (set == null || set.isEmpty()) {
            return EMPTY_WITH_SCORE;
        }

        Set<TypedTuple<V>> resultSet = new LinkedHashSet<>();
        for (TypedTuple<V> typedTuple : set) {
            double score = typedTuple.getScore();
            if (score >= min && score <= max) {
                resultSet.add(typedTuple);
            }
        }

        return resultSet;
    }

    @Override
    public Set<TypedTuple<V>> rangeByScoreWithScores(K key, double min, double max, long offset, long count) {
        if (offset < 0 || count == 0) {
            return EMPTY_WITH_SCORE;
        }

        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);

        if (set == null || set.isEmpty()) {
            return EMPTY_WITH_SCORE;
        }

        return collectValueWithScore(set, min, max, offset, count);
    }

    @Override
    public Set<TypedTuple<V>> reverseRangeByScoreWithScores(K key, double min, double max) {
        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);

        if (set == null || set.isEmpty()) {
            return EMPTY_WITH_SCORE;
        }

        List<TypedTuple<V>> reverseList = new ArrayList<>();

        for (TypedTuple<V> typedTuple : set) {
            reverseList.add(0, typedTuple);
        }

        Set<TypedTuple<V>> resultSet = new LinkedHashSet<>();
        for (TypedTuple<V> typedTuple : reverseList) {
            double score = typedTuple.getScore();
            if (score >= min && score <= max) {
                resultSet.add(typedTuple);
            }
        }

        return resultSet;
    }

    @Override
    public Set<TypedTuple<V>> reverseRangeByScoreWithScores(K key, double min, double max, long offset, long count) {
        if (offset < 0 || count == 0) {
            return EMPTY_WITH_SCORE;
        }

        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);

        if (set == null || set.isEmpty()) {
            return EMPTY_WITH_SCORE;
        }

        List<TypedTuple<V>> reverseList = new ArrayList<>();

        for (TypedTuple<V> typedTuple : set) {
            reverseList.add(0, typedTuple);
        }

        return collectValueWithScore(reverseList, min, max, offset, count);
    }

    @Override
    public Boolean add(K key, V value, double score) {
        TypedTuple<V> typedTuple = new LocalTypedTuple(value, score);
        Set<TypedTuple<V>> set = getSetOrCreate(key);

        TypedTuple<V> byValue = getByValue(set, typedTuple);

        boolean updated = byValue != null;
        if (updated) {
            set.remove(byValue);
        }

        set.add(typedTuple);

        return !updated;
    }

    @Override
    public Long add(K key, Set<TypedTuple<V>> typedTuples) {
        return typedTuples.stream()
                .filter(it -> add(key, it.getValue(), it.getScore()))
                .count();
    }

    @Override
    public Double incrementScore(K key, V value, double delta) {
        Set<TypedTuple<V>> set = getSetOrCreate(key);

        if (!set.isEmpty()) {
            TypedTuple<V> byValue = getByValue(set, new LocalTypedTuple(value, 0D));
            if (byValue != null) {
                delta += byValue.getScore();
                set.remove(byValue);
            }
        }

        set.add(new LocalTypedTuple(value, delta));

        return delta;
    }

    @Override
    public Long rank(K key, Object o) {
        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);

        if (set == null || set.isEmpty()) {
            return null;
        }

        boolean equals = false;
        int index = 0;
        for (TypedTuple<V> typedTuple : set) {
            if (typedTuple.getValue().equals(o)) {
                equals = true;
                break;
            }
            index++;
        }

        return equals ? (long) index : null;
    }

    @Override
    public Long reverseRank(K key, Object o) {
        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);

        if (set == null || set.isEmpty()) {
            return null;
        }

        boolean equals = false;
        int index = 0;
        for (TypedTuple<V> typedTuple : set) {
            if (typedTuple.getValue().equals(o)) {
                equals = true;
                break;
            }
            index++;
        }

        return equals ? (long) (set.size() - 1 - index) : null;
    }

    @Override
    public Double score(K key, Object o) {
        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);

        if (set == null || set.isEmpty()) {
            return null;
        }

        for (TypedTuple<V> typedTuple : set) {
            if (typedTuple.getValue().equals(o)) {
                return typedTuple.getScore();
            }
        }

        return null;
    }

    @Override
    public Long remove(K key, Object... values) {
        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);

        long count = 0;
        for (Object value : values) {
            if (set.removeIf(it -> it.getValue().equals(value))) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Long removeRange(K key, long start, long end) {
        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);

        if (set == null || set.isEmpty()) {
            return 0L;
        }

        long[] ascIndex = IndexConverter.convertRange(set.size(), start, end);
        if (ascIndex == null) {
            return 0L;
        }

        start = ascIndex[0];
        end = ascIndex[1];

        int index = 0;
        List<TypedTuple<V>> removing = new ArrayList<>();
        for (TypedTuple<V> typedTuple : set) {
            if (index >= start && index < end) {
                removing.add(typedTuple);
            }
            index++;
        }

        set.removeAll(removing);

        return (long) removing.size();
    }

    @Override
    public Long removeRangeByScore(K key, double min, double max) {
        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);

        if (set == null || set.isEmpty()) {
            return 0L;
        }

        List<TypedTuple<V>> removing = new ArrayList<>();
        for (TypedTuple<V> typedTuple : set) {
            double score = typedTuple.getScore();
            if (score >= min && score <= max) {
                removing.add(typedTuple);
            }
        }

        set.removeAll(removing);

        return (long) removing.size();
    }

    @Override
    public Long count(K key, double min, double max) {
        Set<TypedTuple<V>> set = getInstance().getZSetCahce(key);

        if (set == null || set.isEmpty()) {
            return 0L;
        }

        long count = 0;
        for (TypedTuple<V> typedTuple : set) {
            double score = typedTuple.getScore();
            if (score >= min && score <= max) {
                count++;
            }
        }

        return count;
    }

    @Override
    public Long size(K key) {
        Set set = getInstance().getZSetCahce(key);
        return (long) set.size();
    }

    @Override
    public Long zCard(K key) {
        return size(key);
    }

    @Override
    public RedisOperations<K, V> getOperations() {
        return null;
    }

    @Override
    public Cursor<TypedTuple<V>> scan(K key, ScanOptions options) {
        return null;
    }
}
