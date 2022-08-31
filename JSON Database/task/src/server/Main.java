package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final Storage storage = new Storage(1000);
    private static boolean run = true;


    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(Constants.PORT)) {
            System.out.println("Server started!");
            while (run) {
                acceptConnectionAndInput(serverSocket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void acceptConnectionAndInput(ServerSocket serverSocket) {
        try (
                Socket socket = serverSocket.accept();
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
        ) {
            String input = inputStream.readUTF();
            System.out.println("Received: " + input);
            processMessage(outputStream, input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processMessage(DataOutputStream outputStream, String input) {
        String[] args = input.split(" ");
        CommandService commandService = new CommandService();
        Command command = commandService.convert(args[0]);
        int index = -1;
        if (args.length > 1) {
            try {
                index = Integer.parseInt(args[1]);
            } catch (Exception e) {
                sendMessage(outputStream, ResponseType.ERROR.name());
                return;
            }
        }
        String content = "";
        if (args.length > 2) {
            StringBuilder sb = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            content = sb.toString();
        }

        Controller controller = new Controller();
        ICommand action;

        switch (command) {
            case SET -> action = new SetContentCommand(storage, index, content);
            case GET -> action = new GetContentCommand(storage, index);
            case DELETE -> action = new DeleteContentCommand(storage, index);
            case EXIT -> {
                run = false;
                sendMessage(outputStream, ResponseType.OK.name());
                return;
            }
            default -> {
                return;
            }
        }

        controller.setCommand(action);
        controller.executeCommand();
        sendMessage(outputStream, controller.getStatus());
    }

    private static void sendMessage(DataOutputStream outputStream, String message) {
        try {
            outputStream.writeUTF(message);
            System.out.println("Sent: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}