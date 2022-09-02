package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class Storage {

    private String jsonData;

    public Storage() {
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<>();
        jsonData = gson.toJson(map);
    }

    public String getContent(String key) {
        Gson gson = new Gson();
        Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();
        HashMap<String, String> map = gson.fromJson(jsonData, mapType);
        return map.get(key);
    }

    public boolean setContent(String key, String value) {
        try {
            Gson gson = new Gson();
            Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();
            HashMap<String, String> map = gson.fromJson(jsonData, mapType);
            map.put(key, value);
            jsonData = gson.toJson(map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteContent(String key) {
        try {
            Gson gson = new Gson();
            Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();
            HashMap<String, String> map = gson.fromJson(jsonData, mapType);
            String ret = map.remove(key);
            if (ret == null) {
                return false;
            }
            jsonData = gson.toJson(map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
