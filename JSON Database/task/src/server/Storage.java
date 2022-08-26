package server;

import java.util.Arrays;

public class Storage {

    private final String[] storage;

    public Storage(int size) {
        storage = new String[size];
        Arrays.fill(storage, null);
    }

    public String getContent(int index) {
        return storage[index];
    }

    public boolean setContent(String content, int index) {
        if (index > storage.length || index < 0) {
            return false;
        }
        storage[index] = content;
        return true;
    }

    public boolean deleteContent(int index) {
        return setContent(null, index);
    }
}
