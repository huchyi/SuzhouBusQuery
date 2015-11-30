package com.hcy.suzhoubusquery.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 不好用的BaseBean
 *
 * @author WHO,
 * @info 为了避免不能序列化，应该尽量不忘BaseBean里加入MAP,LIST类型等不可序列化元素
 *
 */
public class BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, Object> dataMap;

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public boolean isHasData() {
        boolean b = true;
        if (null == dataMap || dataMap.size() == 0) {
            b = false;
        }
        return b;
    }

    /**
     * 初始化数据
     *
     * @param jsonObject
     */
    public void initData(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        dataMap = parseJSONObject(jsonObject);
    }

    /**
     * 将json对象转化为map
     * @param jsonObject
     * @return
     */
    private Map<String, Object> parseJSONObject(JSONObject jsonObject) {
        Map<String, Object> map = null;
        Iterator<String> iterator = jsonObject.keys();
        if (iterator.hasNext()) {
            map = new HashMap<>();
        }
        while (iterator.hasNext()) {
            String tempKey = iterator.next();
            if (!jsonObject.isNull(tempKey) && null != jsonObject.optJSONObject(tempKey)) {
                map.put(tempKey, new BaseBean(jsonObject.optJSONObject(tempKey)));
            } else if (!jsonObject.isNull(tempKey) && null != jsonObject.optJSONArray(tempKey)) {
                map.put(tempKey, parseJSONArray(jsonObject.optJSONArray(tempKey)));
            } else if (!jsonObject.isNull(tempKey)){
                map.put(tempKey, jsonObject.opt(tempKey));
            } else {
                map.put(tempKey, null);
            }
        }
        return map;
    }

    private ArrayList<Object> parseJSONArray(JSONArray jsonArray) {
        ArrayList<Object> list = null;
        if (jsonArray.length() > 0) {
            list = new ArrayList<Object>();
            int len = jsonArray.length();
            for (int i = 0; i < len; i++) {
                if (null != jsonArray.optJSONObject(i)) { // 对象 BaseBean
                    list.add(new BaseBean(jsonArray.optJSONObject(i)));
                } else if (null != jsonArray.optJSONArray(i)) { // 集合
                    list.add(parseJSONArray(jsonArray.optJSONArray(i)));
                } else {
                    list.add(jsonArray.opt(i));
                }
            }
        }
        return list;
    }

    public BaseBean() {
        dataMap = new HashMap<String, Object>();
    }

    public BaseBean(JSONObject jsonObject) {
        initData(jsonObject);
    }

    public Object get(String key) {
        if (null == dataMap || dataMap.get(key) == null) {
            return null;
        }
        try {
            return dataMap.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    public String getStr(String key) {
        // return null == dataMap.get(key) ? "" :
        // "null".equals(dataMap.get(key).toString())?"":dataMap.get(key).toString();
        return null == dataMap.get(key) ? "" : dataMap.get(key).toString();
    }

    public int getInt(String key) {
        int n = 0;
        String str = getStr(key);
        try {
            String tmp = StringUtils.isEmpty(str) ? "0" : str;
            n = Integer.parseInt(StringUtils.isNumeric(tmp) ? tmp : "0");
        } catch (NumberFormatException e) {
            throw new RuntimeException("parse int error [" + str + "]");
        }
        return n;
    }

    public long getLong(String key) {
        long n = 0;
        String str = getStr(key);
        try {
            String tmp = StringUtils.isEmpty(str) ? "0" : str;
            n = Long.parseLong(StringUtils.isNumeric(tmp) ? tmp : "0");
        } catch (NumberFormatException e) {
            throw new RuntimeException("parse int error [" + str + "]");
        }
        return n;
    }

    public float getFloat(String key) {
        float n = 0;
        String str = getStr(key);
        try {
            String tmp = StringUtils.isEmpty(str) ? "0" : str;
            n = Float.parseFloat(tmp);
        } catch (NumberFormatException e) {
            throw new RuntimeException("parse float error [" + str + "]");
        }
        return n;
    }

    public void set(String key, Object value) {
        if (null == dataMap) {
            dataMap = new HashMap<String, Object>();
        }
        dataMap.put(key, value);
    }


    public boolean containKey(String key) {
        if (null != dataMap) {
            return dataMap.containsKey(key);
        }
        return false;
    }

    /*
     * public String debugerString() { StringBuffer buffer = new StringBuffer(); Set<String> set = dataMap.keySet(); for (String string : set) { Object object = dataMap.get(string); if (null == object) { buffer.append(string).append("="); continue; } if (object instanceof HashMap) { buffer.append(string).append("=").append(((HashMap<String, Object>)object).toString()).append(","); continue; }else if(object instanceof ArrayList){ ArrayList<Object> obj = (ArrayList<Object>)object; buffer.append(string).append("="); for (Object object2 : obj) { if (object2 instanceof BaseBean) { buffer.append((((BaseBean)object2).getDataMap()).toString()).append(","); }else{ buffer.append(object2.toString()).append(","); } } }else{ buffer.append(string).append("=").append(object.toString()).append(","); } }
     * return buffer.toString(); }
     */

}
