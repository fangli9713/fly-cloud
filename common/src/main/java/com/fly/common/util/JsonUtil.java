package com.fly.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Slf4j
public class JsonUtil {


    /**
     * 对象转JSONString
     *
     * @param object
     * @return
     */
    public static String beanToJson(Object object) {
        if (object != null) {
            return JSON.toJSONString(object);
        } else {
            return null;
        }
    }

    /**
     * 将json字符串转换成对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Object jsonToBean(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json) || clazz == null) {
            return null;
        }
        return JSONObject.toJavaObject(JSONObject.parseObject(json), clazz);
    }

    /**
     * json字符串转map
     *
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> jsonToMap(String json) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return JSON.parseObject(json, Map.class);
    }

    /**
     * json字符串转换成list<Map>
     *
     * @param jsonString
     * @return
     */
    public static List<Map<String, Object>> stringToListMap(String jsonString) {
        return JSON.parseObject(jsonString, new TypeReference<List<Map<String, Object>>>() {
        });
    }

    /**
     * json字符串转成为List
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> stringToList(String jsonStr, Class<T> clazz) {
        return JSON.parseArray(jsonStr, clazz);
    }

}
