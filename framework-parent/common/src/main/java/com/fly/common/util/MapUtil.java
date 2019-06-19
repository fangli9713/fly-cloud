package com.fly.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

public class MapUtil {


    /**
     * 将对象装换为map
     *
     * @param bean
     * @param <T>
     * @return
     */

    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            beanMap.keySet().forEach(key -> map.put(key.toString(), beanMap.get(key)));

        }
        return map;
    }


    public static <T> Map<String, String> beanToStringMap(T bean) {
        Map<String, String> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            beanMap.keySet().forEach(key -> {
                Object value=beanMap.get(key);
                if(value!=null){
                    map.put(key.toString(), beanMap.get(key) == null ? null : beanMap.get(key).toString());
                }
            });

        }
        return map;
    }


    /**
     * 将map装换为javabean对象
     *
     * @param map
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }


    /**
     * 将List<T>转换为List<Map<String, Object>>
     *
     * @param objList
     * @param <T>
     * @return
     */

    public static <T> List<Map<String, Object>> objectsToMaps(List<T> objList) {
        List<Map<String, Object>> list = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(objList)) {
            objList.forEach(t -> list.add(beanToMap(t)));
        }
        return list;


    }


    /**
     * 将List<Map<String,Object>>转换为List<T>
     *
     * @param maps
     * @param clazz
     * @param <T>
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> List<T> mapsToObjects(List<Map<String, Object>> maps, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        List<T> list = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(maps)) {
            for (Map<String, Object> map : maps) {
                T bean = clazz.newInstance();
                mapToBean(map, bean);
                list.add(bean);
            }
        }

        return list;


    }
}
