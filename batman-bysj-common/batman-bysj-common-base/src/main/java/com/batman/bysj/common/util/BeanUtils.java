package com.batman.bysj.common.util;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ContextClassLoaderLocal;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.springframework.cglib.beans.BeanCopier;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean 帮助类, 提供实例属性复制, 实例克隆等帮助方法
 *
 * @author liangchuanyu
 * @author jonas
 * @version 2.4.0
 * @since 2.0.0
 */
public final class BeanUtils {

    /**
     * 将 source 的属性值复制到 target 上
     *
     * @param source 被复制的实例
     * @param target 复制到该实例
     */
    public static void copy(Object source, Object target) {
        BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), false);
        copier.copy(source, target, null);
    }

    /**
     * toModel
     */
    public static <T> T toModel(Map<String, Object> map, Class<T> classInfo) {
        T model;
        try {
            model = classInfo.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        copyProperties(map, model);
        return model;
    }

    @SuppressWarnings("unchecked")
    private static final ContextClassLoaderLocal BEANS_BY_CLASSLOADER = new ContextClassLoaderLocal() {
        // Creates the default instance used when the context classloader is unavailable
        protected Object initialValue() {
            return new BeanUtilsBean(
                    new ConvertUtilsBean() {
                        @Override
                        public Object convert(String value, Class clazz) {
                            if (clazz.isEnum()) {
                                return Enum.valueOf(clazz, value);
                            } else {
                                return super.convert(value, clazz);
                            }
                        }
                    }
            ) {
                @Override
                public Object convert(Object value, Class type) {
                    Converter converter = getConvertUtils().lookup(type);
                    if (converter != null) {
                        return converter.convert(type, value);
                    } else {
                        // convert custom class
                        if (Map.class.isInstance(value) && !type.isAssignableFrom(Map.class)) {
                            return toModel((Map) value, type);
                        }
                        return value;
                    }
                }
            };
        }
    };

    /**
     * copyProperties
     */
    public static void copyProperties(Map<String, Object> source, Object target) {
        try {
            BeanUtilsBean beanUtilsBean = (BeanUtilsBean) BEANS_BY_CLASSLOADER.get();

            // Loop through the property name/value pairs to be set
            for (Map.Entry<String, Object> entry : source.entrySet()) {
                if (entry.getKey() == null || entry.getValue() == null) {
                    continue;
                }
                // Perform the assignment for this property
                beanUtilsBean.setProperty(target, entry.getKey(), entry.getValue());
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * toMapList
     */
    public static <T> List<Map<String, Object>> toMapList(List<T> modelList) {
        if (modelList.isEmpty()) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> listMap = new ArrayList<>();

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(modelList.get(0).getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (T entity : modelList) {
                Map<String, Object> map = new HashMap<>();
                copyProperties(entity, map, propertyDescriptors);
                listMap.add(map);
            }
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }

        return listMap;
    }

    /**
     * toMap
     */
    public static <T> Map<String, Object> toMap(T entity) {
        Map<String, Object> map = new HashMap<>();
        copyProperties(entity, map);
        return map;
    }

    /**
     * copyProperties
     */
    private static void copyProperties(Object source, Map<String, Object> target) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(source.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            copyProperties(source, target, propertyDescriptors);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * copyProperties
     */
    private static void copyProperties(Object source, Map<String, Object> target, PropertyDescriptor[] propertyDescriptors) {
        try {
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                if (!"class".equals(key)) {
                    Method getter = property.getReadMethod();
                    if (getter != null) {
                        Object value = getter.invoke(source);
                        target.put(key, value);
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
