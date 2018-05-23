package com.batman.bysj.common.util;

import java.lang.reflect.ParameterizedType;

/**
 * Created by dell on 2016/12/21.
 */
public final class GenericSuperclassUtils {

    public static <T> Class<T> getGenericActualTypeClass(Object bean) {
        return getGenericActualTypeClass(bean, 0);
    }

    @SuppressWarnings({"unchecked", "WeakerAccess"})
    public static <T> Class<T> getGenericActualTypeClass(Object bean, int index) {
        return (Class<T>) ((ParameterizedType) bean.getClass().getGenericSuperclass()).getActualTypeArguments()[index];
    }
}
