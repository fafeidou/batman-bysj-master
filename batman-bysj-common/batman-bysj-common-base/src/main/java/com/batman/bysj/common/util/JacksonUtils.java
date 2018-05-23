package com.batman.bysj.common.util;

import com.batman.bysj.common.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JacksonUtils {

    private final static ObjectMapper DEFAULT_OBJECT_MAPPER;
    private final static ObjectMapper NOT_NULL_OBJECT_MAPPER;
    private final static ObjectMapper OBJECT_MAPPER_WITH_DATE_FORMAT;

    static {
        DEFAULT_OBJECT_MAPPER = new ObjectMapper();
        DEFAULT_OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        NOT_NULL_OBJECT_MAPPER = new ObjectMapper();
        NOT_NULL_OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        OBJECT_MAPPER_WITH_DATE_FORMAT = new ObjectMapper();
        OBJECT_MAPPER_WITH_DATE_FORMAT.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OBJECT_MAPPER_WITH_DATE_FORMAT.setDateFormat(format);
    }

    public static String toJson(Object obj) {
        return bean2Json(obj);
    }

    public static <T> T fromJson(String json, Class<T> tClass) {
        return json2Bean(json, tClass);
    }

    /**
     * 根据json字符串返回对应java类型
     *
     * @param obj Object
     * @return String
     */
    public static String bean2Json(Object obj) {
        try {
            return DEFAULT_OBJECT_MAPPER.writeValueAsString(obj);
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    public static String bean2JsonNotNull(Object obj) {
        try {
            return NOT_NULL_OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 根据json字符串返回对应java类型
     *
     * @param jsonStr String
     * @param cls     Class<T>
     * @return T
     */
    public static <T> T json2Bean(String jsonStr, Class<T> cls) {
        try {
            return DEFAULT_OBJECT_MAPPER.readValue(jsonStr, cls);
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 根据json字符串返回对应java类型
     *
     * @param jsonStr String
     * @param cls     Class<T>
     * @return T
     */
    public static <T> T json2BeanWithDateCovert(String jsonStr, Class<T> cls) {
        try {
            return OBJECT_MAPPER_WITH_DATE_FORMAT.readValue(jsonStr, cls);
        } catch (IOException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * 根据json字符串返回对应Map类型
     *
     * @param jsonString String
     * @return Map result
     */
    public static Map<String, Object> jsonToMap(String jsonString) {
        try {
            return DEFAULT_OBJECT_MAPPER.readValue(jsonString, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据json字符串返回对应java类型List
     *
     * @param jsonString String
     * @param cls        Class
     * @return List
     */
    public static <T> List<T> jsonToBeanList(String jsonString, Class<T> cls) {
        JavaType javaType = DEFAULT_OBJECT_MAPPER.getTypeFactory().constructParametrizedType(ArrayList.class, List.class, cls);
        try {
            return DEFAULT_OBJECT_MAPPER.readValue(jsonString, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据json字符串返回对应Map类型List
     *
     * @param jsonString String
     * @return List
     */
    public static List<Map<String, Object>> jsonToMapList(String jsonString) {
        JavaType javaType = DEFAULT_OBJECT_MAPPER.getTypeFactory().constructParametrizedType(ArrayList.class, List.class, Map.class);
        try {
            return DEFAULT_OBJECT_MAPPER.readValue(jsonString, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * bean2Map
     */
    public static Map<String, Object> bean2Map(Object obj) {
        //Date to long
        //mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return DEFAULT_OBJECT_MAPPER.convertValue(obj, new TypeReference<Map<String, Object>>() {
        });
    }

    public static JsonNode readTree(Reader reader) throws IOException {
        return DEFAULT_OBJECT_MAPPER.readTree(reader);
    }

    /**
     * 直接生成 json 到响应
     */
//    public static void writeValue(ServletResponse response, Object value) throws IOException {
//        try (PrintWriter writer = response.getWriter()) {
//            DEFAULT_OBJECT_MAPPER.writeValue(writer, value);
//        }
//    }
    public static MapPutHelper put(String key, Object obj) {
        return new MapPutHelper().put(key, obj);
    }

    public static class MapPutHelper extends HashMap<String, Object> {
        public MapPutHelper put(String key, Object obj) {
            super.put(key, obj);
            return this;
        }
    }
}