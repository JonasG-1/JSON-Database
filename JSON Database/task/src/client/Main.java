package client;

public class Main {

    public static String ADDRESS = "127.0.0.1";
    public static int PORT = 23456;
    public static void main(String[] args) {
        String mode = "none";
        int index = -1;
        String content = "";
        for (int i = 0; i < args.length - 1; i += 2) {
            switch (args[i]) {
                case "-t" -> mode = args[i + 1];
                case "-i" -> index = Integer.parseInt(args[i + 1]);
                case "-m" -> {
                    if ("set".equals(mode)) {
                        content = getContent(i + 1, args);
                    }
                }
            }
        }

        Controller controller = new Controller();
        Client client = new Client(ADDRESS, PORT);
        Command command;

        switch (mode) {
            case "set" -> command = new SetStorageCommand(client, index, content);
            case "get" -> command = new GetStorageCommand(client, index);
            case "delete" -> command = new DeleteStorageCommand(client, index);
            case "exit" -> command = new ExitStorageCommand(client);
            default -> {
                return;
            }
        }

        controller.setCommand(command);
        controller.executeCommand();
    }

    private static String getContent(int beginningIndex, String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = beginningIndex; i < args.length; i++) {
            stringBuilder.append(args[i]).append(" ");
        }
        return stringBuilder.toString();
    }
}