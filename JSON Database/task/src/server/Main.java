package server;

import com.google.gson.Gson;
import server.data.FilePath;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final String JSON_DB = "db.json";
    private static final String filePath = FilePath.getFilePath().concat(JSON_DB);
    private static final Storage storage = new Storage(filePath);
    private static boolean run = true;
    private static ExecutorService executor;


    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(Constants.PORT)) {
            executor = Executors.newFixedThreadPool(4);
            System.out.println("Server started!");
            while (run) {
                acceptConnectionAndInput(serverSocket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    private static void acceptConnectionAndInput(ServerSocket serverSocket) {
        try {
            Socket socket = serverSocket.accept();
            executor.submit(() -> {
                try (
                        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
                ) {
                    String input = inputStream.readUTF();
                    System.out.println("Received: " + input);
                    processMessage(outputStream, input);
                    socket.close();
                    if (!run) {
                        serverSocket.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processMessage(DataOutputStream outputStream, String jsonData) {
        Gson gson = new Gson();
        JsonAction jsonAction = gson.fromJson(jsonData, JsonAction.class);
        CommandService commandService = new CommandService();
        Command command = commandService.convert(jsonAction.getType());
        String key = jsonAction.getKey();
        String value = jsonAction.getValue();

        JsonResponse jsonResponse = new JsonResponse();
        Controller controller = new Controller();
        ICommand iCommand;

        switch (command) {
            case SET -> iCommand = new SetContentCommand(storage, key, value);
            case GET -> iCommand = new GetContentCommand(storage, key);
            case DELETE -> iCommand = new DeleteContentCommand(storage, key);
            case EXIT -> {
                Main.run = false;
                jsonResponse.setResponse(true);
                sendMessage(outputStream, jsonResponse);
                return;
            }
            default -> {
                jsonResponse.setResponse(false);
                sendMessage(outputStream, jsonResponse);
                return;
            }
        }

        controller.setCommand(iCommand);
        controller.executeCommand();
        boolean response = controller.getResponse();
        jsonResponse.setResponse(response);
        if (response) {
            jsonResponse.setValue(controller.getOutput());
        } else {
            jsonResponse.setReason(controller.getOutput());
        }
        sendMessage(outputStream, jsonResponse);
    }

    private static void sendMessage(DataOutputStream outputStream, JsonResponse jsonResponse) {
        try {
            Gson gson = new Gson();
            String out = gson.toJson(jsonResponse);
            outputStream.writeUTF(out);
            System.out.println("Sent: " + out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}