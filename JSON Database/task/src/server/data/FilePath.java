package server.data;

import java.io.File;

public class FilePath {

    public static String getFilePath() {
        String normalPath = new File("").getAbsolutePath() + "/JSON Database/task/src/server/data/";
        try {
            File file = new File(normalPath);
            file.createNewFile();
        } catch (Exception e) {
            return new File("").getAbsolutePath() + "/src/server/data/";
        }
        return normalPath;
    }
}
