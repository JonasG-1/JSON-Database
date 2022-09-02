package client;

import com.google.gson.Gson;

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
        Client client = new Client(ADDRESS, PORT);
        String jsonData = gson.toJson(jsonAction);
        client.sendMessage(jsonData);
    }
}