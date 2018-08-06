package com.bysj.common.util;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author victor.qin
 * @date 2018/8/6 17:11
 */
public class BeanCollectionFactory<K, B> {
    protected Map<K, B> beans;

    public BeanCollectionFactory() {
        this.beans = new HashMap<>();
    }

    /**
     * 根据数据列表初始化数据
     *
     * @param models    数据源
     * @param generator 生成新实例并设置非数据属性
     * @param keyGetter 获取相同主键的值(判断是否为同一条数据用)
     * @param setters   自定义属性设置(为空则自动进行非 null 属性拷贝)
     * @param <U>       model
     */
    public <U> BeanCollectionFactory<K, B> union(
            Collection<U> models,
            Function<U, B> generator,
            Function<? super U, K> keyGetter,
            List<BiConsumer<? super U, ? super B>> setters) {

        for (U model : models) {
            K key = keyGetter.apply(model);
            B bean = beans.get(key);
            if (bean == null) {
                bean = generator.apply(model);
            }
            setProperties(model, bean, setters);
            beans.put(key, bean);
        }
        return this;
    }

    public <U> BeanCollectionFactory<K, B> union(
            Collection<U> models,
            Function<U, B> generator,
            Function<? super U, K> keyGetter,
            BiConsumer<? super U, ? super B> setter) {
        return union(models, generator, keyGetter, Collections.singletonList(setter));
    }

    /**
     * @see BeanCollectionFactory#union(Collection, Function, Function, BiConsumer)
     * @see BeanCollectionFactory#union(Collection, Function, Function, List)
     */
    @Deprecated
    public <U> BeanCollectionFactory<K, B> union(
            Collection<U> models,
            Function<U, B> generator,
            Function<? super U, K> keyGetter) {
        return union(models, generator, keyGetter, Collections.emptyList());
    }


    /**
     * 合并其他数据(取交集)
     */
    public <U> BeanCollectionFactory<K, B> intersect(
            Collection<U> models,
            Function<? super U, K> keyGetter,
            BiConsumer<? super U, ? super B> setters) {
        return intersect(models, keyGetter, Collections.singletonList(setters));
    }

    /**
     * 合并其他数据(取交集)
     */
    public <U> BeanCollectionFactory<K, B> intersect(
            Collection<U> models,
            Function<? super U, K> keyGetter,
            List<BiConsumer<? super U, ? super B>> setters) {
        List<K> keys = new ArrayList<>();
        for (U model : models) {
            K key = keyGetter.apply(model);
            B bean = beans.get(key);
            // 未找到的部分忽略掉
            if (null == bean) {
                continue;
            }

            // 保留找到的部分
            keys.add(key);
            // 设置属性
            setProperties(model, bean, setters);
            beans.put(key, bean);
        }

        // 删除原数据中多余的部分
        Collection<K> pendingRemoveKey = beans.keySet().stream()
                .filter(key -> !keys.contains(key))
                .collect(Collectors.toList());

        pendingRemoveKey.forEach(key -> beans.remove(key));
        return this;
    }

    public <U> BeanCollectionFactory<K, B> intersect(
            Collection<U> models,
            Function<? super U, K> keyGetter) {
        return intersect(models, keyGetter, (List<BiConsumer<? super U, ? super B>>) null);
    }

    /**
     * 合并其他数据(取左连接)
     *
     * @param models    数据源
     * @param keyGetter 获取相同主键的值(判断是否为同一条数据用)
     * @param <U>       销售类型 model
     */
    public <U> BeanCollectionFactory<K, B> leftJoin(
            Collection<U> models,
            Function<? super U, K> keyGetter,
            List<BiConsumer<? super U, ? super B>> setters) {
        for (U model : models) {
            K key = keyGetter.apply(model);
            B bean = beans.get(key);
            if (null == bean) {
                continue;
            }
            setProperties(model, bean, setters);
            beans.put(key, bean);
        }
        return this;
    }

    public <U> BeanCollectionFactory<K, B> leftJoin(
            Collection<U> models,
            Function<? super U, K> keyGetter,
            BiConsumer<? super U, ? super B> setter) {
        return leftJoin(models, keyGetter, Collections.singletonList(setter));
    }

    public <U> BeanCollectionFactory<K, B> leftJoin(
            Collection<U> models,
            Function<? super U, K> keyGetter) {
        return leftJoin(models, keyGetter, Collections.emptyList());
    }

    public Map<K, B> beanMap() {
        return beans;
    }

    public Collection<B> beanList() {
        return beans.values();
    }

    private <U> void setProperties(U source, B target, List<BiConsumer<? super U, ? super B>> setters) {
        if (!CollectionUtils.isEmpty(setters)) {
            // 如果有设置公式 则运行
            for (BiConsumer<? super U, ? super B> setter : setters) {
                setter.accept(source, target);
            }
        } else {
            // 否则直接复制
            BeanUtils.copyProperties(source, target, BusinessReflectUtils.nullPropertyNames(source));
        }
    }

}
