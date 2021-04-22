package com.king.function.excel.Reflection;

import javassist.*;

import java.lang.reflect.Field;
import java.util.*;

public class DynamicCreateObject {

    private static final HashMap<Object, Object> objectCache = new HashMap<>();

    public DynamicCreateObject() {
    }


    protected int hash(String key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public Object getDynamicObject(String dynamicName, Map<String, String> map) throws Exception {
        int hash = hash(dynamicName);
        Object dynamicObj = null;
        if ((dynamicObj = objectCache.get(hash)) == null) {
            //dynamicObj
            dynamicObj = addField(dynamicName, map);
            objectCache.put(hash, dynamicObj);
        }
        return dynamicObj;
    }


    /**
     * 添加属性
     *
     * @param className 类名
     * @param fieldMap  字段
     */
    public Object addField(String className, Map<String, String> fieldMap) throws Exception {
        // 获取javassist类池
        ClassPool pool = ClassPool.getDefault();
        // 创建javassist类
        CtClass ctClass = pool.makeClass(className, pool.get(Object.class.getName()));
        // 为创建的类ctClass添加属性
        Iterator<Map.Entry<String, String>> iterator = fieldMap.entrySet().iterator();
        // 遍历所有的属性
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue();
            // 增加属性，这里仅仅是增加属性字段
            String fieldType = fieldValue.getClass().getName();
            CtField ctField = new CtField(pool.get(fieldType), fieldName, ctClass);
            ctField.setModifiers(Modifier.PUBLIC);
            ctClass.addField(ctField);
        }
        // 为创建的javassist类转换为java类
        Class<?> c = ctClass.toClass();
        // 为创建java对象
        Object newObject = c.newInstance();
        iterator = fieldMap.entrySet().iterator();
        // 遍历所有的属性
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
            String fieldName = (String) entry.getKey();
            String fieldValue = (String) entry.getValue();
            // 为属性赋值
            setFieldValue(newObject, fieldName, fieldValue);
        }
        return newObject;
    }

    /***
     * @description: 获取属性值
     */
    public Object getFieldValue(Object object, String fieldName) throws Exception {
        Object result = null;
        // 获取对象的属性域
        Field fu = object.getClass().getDeclaredField(fieldName);
        // 设置对象属性域的访问属性
        fu.setAccessible(true);
        // 获取对象属性域的属性值
        result = fu.get(object);
        return result;
    }

    /***
     * @description: 属性赋值
     */
    private Object setFieldValue(Object object, String fieldName, String val) throws Exception {
        Object result = null;
        // 获取对象的属性域
        Field fu = object.getClass().getDeclaredField(fieldName);
        // 设置对象属性域的访问属性
        fu.setAccessible(true);
        // 设置对象属性域的属性值
        fu.set(object, val);
        // 获取对象属性域的属性值
        result = fu.get(object);
        return result;
    }

    public static HashMap<Object, Object> getObjectCache() {
        return objectCache;
    }
}
