package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Storage {

    private String jsonData;
    private final String jsonPath;
    private final Lock readLock;
    private final Lock writeLock;

    public Storage(String jsonPath) {
        this.jsonPath = jsonPath;
        Gson gson = new Gson();
        String json = getJson();
        if (json == null) {
            HashMap<String, String> map = new HashMap<>();
            jsonData = gson.toJson(map);
            saveJson();
        } else {
            jsonData = json;
        }
        ReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    public String getContent(String key) {
        readLock.lock();
        Gson gson = new Gson();
        Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();
        HashMap<String, String> map = gson.fromJson(jsonData, mapType);
        readLock.unlock();
        return map.get(key);
    }

    public boolean setContent(String key, String value) {
        writeLock.lock();
        try {
            Gson gson = new Gson();
            Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();
            HashMap<String, String> map = gson.fromJson(jsonData, mapType);
            map.put(key, value);
            jsonData = gson.toJson(map);
            saveJson();
            writeLock.unlock();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeLock.unlock();
        return false;
    }

    public boolean deleteContent(String key) {
        writeLock.lock();
        try {
            Gson gson = new Gson();
            Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();
            HashMap<String, String> map = gson.fromJson(jsonData, mapType);
            String ret = map.remove(key);
            if (ret == null) {
                writeLock.unlock();
                return false;
            }
            jsonData = gson.toJson(map);
            saveJson();
            writeLock.unlock();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeLock.unlock();
        return false;
    }

    private synchronized void saveJson() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(jsonPath));
            writer.write(jsonData);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getJson() {
        try {
            File file = new File(jsonPath);
            if (file.createNewFile()) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String current = reader.readLine();
            while (current != null) {
                builder.append(current);
                current = reader.readLine();
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
