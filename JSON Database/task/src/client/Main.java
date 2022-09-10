package client;

import com.google.gson.Gson;
import client.data.FilePath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Main {

    public static String ADDRESS = "127.0.0.1";
    public static int PORT = 23456;
    public static void main(String[] args) {
        String mode = "none";
        String key = null;
        String value = "";
        for (int i = 0; i < args.length - 1; i += 2) {
            switch (args[i]) {
                case "-t" -> mode = args[i + 1];
                case "-k" -> key = args[i + 1];
                case "-v" -> {
                    if ("set".equals(mode)) {
                        value = args[i + 1];
                    }
                }
                case "-in" -> {
                    sendJson(FilePath.getFilePath() + args[i + 1]);
                    return;
                }
            }
        }


        JsonAction jsonAction = new JsonAction();
        Controller controller = new Controller();
        ICommand command;

        switch (mode) {
            case "set" -> command = new SetStorageCommand(jsonAction, key, value);
            case "get" -> command = new GetStorageCommand(jsonAction, key);
            case "delete" -> command = new DeleteStorageCommand(jsonAction, key);
            case "exit" -> command = new ExitStorageCommand(jsonAction);
            default -> {
                return;
            }
        }

        controller.setCommand(command);
        controller.executeCommand();

        Gson gson = new Gson();
        String jsonData = gson.toJson(jsonAction);
        send(jsonData);
    }

    private static void sendJson(String filePath) {
        try {
            File file = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String current = reader.readLine();
            while (current != null) {
                builder.append(current);
                current = reader.readLine();
            }
            send(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void send(String json) {
        Client client = new Client(ADDRESS, PORT);
        client.sendMessage(json);
    }
}