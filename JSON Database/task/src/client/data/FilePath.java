package client.data;

import java.io.File;

public class FilePath {

    public static String getFilePath() {
        String normalPath = new File("").getAbsolutePath() + "/JSON Database/task/src/client/data/";
        try {
            File file = new File(normalPath);
            file.createNewFile();
        } catch (Exception e) {
            return new File("").getAbsolutePath() + "/src/client/data/";
        }
        return normalPath;
    }
}
