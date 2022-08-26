package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final String GET = "get";
    private static final String SET = "set";
    private static final String DELETE = "delete";
    private static final String EXIT = "exit";
    private static final String OK = "OK";
    private static final String ERR = "ERROR";
    private static final Storage storage = new Storage(1000);
    private static final int PORT = 23456;
    private static boolean run = true;


    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started!");
            while (run) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processMessage(DataOutputStream outputStream, String input) {
        String[] args = input.split(" ");
        String command = args[0];
        int index = -1;
        if (args.length > 1) {
            try {
                index = Integer.parseInt(args[1]);
            } catch (Exception e) {
                sendMessage(outputStream, ERR);
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
        Command action;

        switch (command) {
            case SET -> action = new SetContentCommand(storage, index, content);
            case GET -> action = new GetContentCommand(storage, index);
            case DELETE -> action = new DeleteContentCommand(storage, index);
            case EXIT -> {
                run = false;
                sendMessage(outputStream, OK);
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