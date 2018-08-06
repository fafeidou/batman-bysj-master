package com.bysj.common.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
*
* @author victor.qin
* @date 2018/8/6 17:11
*/
public abstract class BusinessReflectUtils {
    public static String[] nullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 将 srouce 属性设置到 target 对应的 属性名 + suffix 里
     * demo: suffix = "Suf" 效果即 target.aSuf = source.a
     */
    public static void copyPropertiesWithSuffix(
            String suffix, Object source, Object target, String... ignoreProperties) {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();

        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            String targetPdName = targetPd.getName();
            if (null != suffix) {
                if (!targetPdName.endsWith(suffix)) continue;
                targetPdName = targetPdName.substring(0, targetPdName.lastIndexOf(suffix));
            }
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPdName))) {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPdName);
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null &&
                            ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        } catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPdName + "' from source to target", ex);
                        }
                    }
                }
            }
        }

    }

    /**
     * 计算除法用
     * 会将 paramSuffixes 结尾的属性作为参数 使用 formulas 计算后 resultSuffix 结尾的属性作为结果
     */
    @SuppressWarnings("unchecked")
    public static <T, U> void calculateWithSuffix(
            Object target,
            Map<String, String> paramSuffixes,
            String resultSuffix,
            Function<Map<String, T>, U> formula) throws InvocationTargetException, IllegalAccessException {

        Map<String, T> params = new HashMap<>();
        Map<String, Map<String, Method>> paramPropertyNames = new HashMap<>();
        Map<String, Method> resultSetter = new HashMap<>();
        for (PropertyDescriptor propertyDescriptor : BeanUtils.getPropertyDescriptors(target.getClass())) {
            for (Map.Entry<String, String> entry : paramSuffixes.entrySet()) {
                if (propertyDescriptor.getName().endsWith(entry.getValue())) {
                    // 按照属性名分组
                    String propertyName = propertyDescriptor.getName()
                            .substring(0, propertyDescriptor.getName().lastIndexOf(entry.getValue()));
                    if (propertyName.length() == 1) {
                        propertyName = propertyName.toUpperCase();
                    }
                    Map<String, Method> properties = Optional.ofNullable(paramPropertyNames.get(propertyName))
                            .orElse(new HashMap<>());
                    properties.put(entry.getValue(), propertyDescriptor.getReadMethod());
                    paramPropertyNames.put(propertyName, properties);
                }
            }
            if (propertyDescriptor.getName().endsWith(resultSuffix)) {
                String propertyName = propertyDescriptor.getName()
                        .substring(0, propertyDescriptor.getName().lastIndexOf(resultSuffix));
                if (propertyName.length() == 1) {
                    propertyName = propertyName.toUpperCase();
                }
                resultSetter.put(propertyName, propertyDescriptor.getWriteMethod());
            }
        }
        for (Map.Entry<String, Map<String, Method>> entry : paramPropertyNames.entrySet()) {
            if (entry.getValue().size() == paramSuffixes.size() && resultSetter.get(entry.getKey()) != null) {
                Map<String, T> paramMap = new HashMap<>();
                for (Map.Entry<String, Method> stringMethodEntry : entry.getValue().entrySet()) {
                    String paramSuffix = stringMethodEntry.getKey();
                    Method fieldGetter = stringMethodEntry.getValue();
                    paramMap.put(paramSuffix, (T) fieldGetter.invoke(target));
                }
                U result = formula.apply(paramMap);
                resultSetter.get(entry.getKey()).invoke(target, result);
            }
        }

    }

    public static <T, U> void calculateWithSuffix(
            Object target,
            List<String> paramSuffixes,
            String resultSuffix,
            Function<Map<String, T>, U> formula) throws InvocationTargetException, IllegalAccessException {
        Map<String, String> paramMap = paramSuffixes.stream()
                .collect(Collectors.toMap(Function.identity(), Function.identity()));
        calculateWithSuffix(target, paramMap, resultSuffix, formula);
    }
}
