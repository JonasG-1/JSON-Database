package server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
            HashMap<JsonElement, JsonElement> map = new HashMap<>();
            jsonData = gson.toJson(map);
            saveJson();
        } else {
            jsonData = json;
        }
        ReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    public JsonElement getContent(JsonElement key) {
        readLock.lock();
        Gson gson = new Gson();
        HashMap<String, JsonElement> map = getJsonElementMap(gson);
        String trueKey;
        JsonElement result;
        if (key.isJsonArray()) {
            JsonArray jsonArray = key.getAsJsonArray();
            trueKey = jsonArray.get(0).getAsString();
            if (jsonArray.size() > 1) {
                JsonElement element = map.get(trueKey);
                if (element == null) {
                    return null;
                }
                result = getJsonDataByArray(element, jsonArray);
            } else {
                result = map.get(trueKey);
            }
        } else {
            trueKey = key.getAsString();
            result = map.get(trueKey);
        }
        readLock.unlock();
        return result;
    }

    private JsonElement getJsonDataByArray(JsonElement element, JsonArray array) {
        return getJsonDataByArray(element, array, 1);
    }

    private JsonElement getJsonDataByArray(JsonElement element, JsonArray array, int arrayIndex) {
        if (array.size() == arrayIndex) {
            return element;
        } else {
            String currentItem = array.get(arrayIndex).getAsString();
            if (!element.isJsonObject() || !element.getAsJsonObject().has(currentItem)) {
                return null;
            }
            element = element.getAsJsonObject().get(currentItem);
            return getJsonDataByArray(element, array, arrayIndex + 1);
        }
    }

    public boolean setContent(JsonElement key, JsonElement value) {
        writeLock.lock();
        try {
            Gson gson = new Gson();
            HashMap<String, JsonElement> map = getJsonElementMap(gson);
            String trueKey;
            if (key.isJsonArray()) {
                JsonArray jsonArray = key.getAsJsonArray();
                trueKey = jsonArray.get(0).getAsString();
                if (jsonArray.size() > 1) {
                    JsonElement element = map.get(trueKey);
                    if (element == null) {
                        element = new JsonObject();
                    }
                    value = rebuildJsonElementByArray(element, jsonArray, value);
                }
            } else {
                trueKey = key.getAsString();
            }
            map.put(trueKey, value);
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

    private HashMap<String, JsonElement> getJsonElementMap(Gson gson) {
        Type mapType = new TypeToken<HashMap<String, JsonElement>>() {}.getType();
        return gson.fromJson(jsonData, mapType);
    }

    public boolean deleteContent(JsonElement key) {
        return setContent(key, null);
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

    private JsonElement rebuildJsonElementByArray(JsonElement element, JsonArray array, JsonElement value) {
        return rebuildJsonElementByArray(element, array, value, 1);
    }

    private JsonElement rebuildJsonElementByArray(JsonElement element, JsonArray array, JsonElement value, int arrayIndex) {
        if (array.size() - 1 == arrayIndex) {
            return appendValue(element.getAsJsonObject(), array.get(arrayIndex).getAsString(), value);
        } else {
            String currentItem = array.get(arrayIndex).getAsString();
            JsonObject jsonObject = element.getAsJsonObject();
            if (!jsonObject.has(currentItem)) {
                jsonObject.add(currentItem, buildMissingObjects(array, value, arrayIndex));
                return jsonObject;
            }
            rebuildJsonElementByArray(jsonObject.get(currentItem), array, value, arrayIndex + 1);
            return jsonObject;
        }
    }

    private static JsonObject appendValue(JsonObject object, String arrayContent, JsonElement value) {
        object.add(arrayContent, value);
        return object;
    }

    private JsonObject buildMissingObjects(JsonArray array, JsonElement value, int arrayIndex) {
        JsonObject jsonObject = new JsonObject();
        String lastElement = array.get(array.size() - 1).getAsString();
        jsonObject.add(lastElement, value);
        jsonObject = addRootObjects(array, arrayIndex, jsonObject);
        return jsonObject;
    }

    private static JsonObject addRootObjects(JsonArray array, int arrayIndex, JsonObject jsonObject) {
        for (int i = array.size() - 2; i > arrayIndex; i--) {
            JsonObject tempObject = new JsonObject();
            tempObject.add(array.get(i).getAsString(), jsonObject);
            jsonObject = tempObject;
        }
        return jsonObject;
    }
}
