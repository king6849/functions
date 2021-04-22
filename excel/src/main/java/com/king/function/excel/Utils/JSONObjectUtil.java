package com.king.function.excel.Utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Component
public class JSONObjectUtil {
    /**
     * 将字符串转化成为json格式对象
     *
     * @param object 转换对象
     * @return JSON对象
     */
    public JSONObject parseStringToJSONObject(HashMap<String, Object> object) {
        String jsonStr = JSONObject.toJSONString(object);
        if (ObjectUtils.isNullStringObj(jsonStr)) {
            return new JSONObject();
        }
        return JSONObject.parseObject(jsonStr);
    }

    public String[] JSONArrayToStrings(JSONArray jsonArray) {
        int size = jsonArray.size();
        if (size == 0) {
            return null;
        }
        String[] strings = new String[size];
        for (int i = 0; i < size; i++) {
            strings[i] =
                    jsonArray.getString(i);
        }
        return strings;
    }

    public HashMap<String, Object> jsonToHashMap(JSONObject jsonObject) {
        HashMap<String, Object> map = new HashMap<>();
        for (String key : jsonObject.keySet()) {
            map.put(key, jsonObject.get(key));
        }
        return map;
    }

    public LinkedHashMap<String, Object> jsonToLinkedHashMap(JSONObject jsonObject) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        for (String key : jsonObject.keySet()) {
            map.put(key, jsonObject.get(key));
        }
        return map;
    }

}
