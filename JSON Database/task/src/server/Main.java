package server;

import com.google.gson.*;
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
                    try {
                        socket.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processMessage(DataOutputStream outputStream, String jsonData) {
        JsonElement jsonElement = JsonParser.parseString(jsonData);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        CommandService commandService = new CommandService();
        Command command = commandService.convert(jsonObject.get("type").getAsString());
        JsonElement keyElement = jsonObject.get("key");
        JsonElement value = jsonObject.get("value");
        JsonResponse jsonResponse = new JsonResponse();
        Controller controller = new Controller();
        ICommand iCommand;

        switch (command) {
            case SET -> iCommand = new SetContentCommand(storage, keyElement, value);
            case GET -> iCommand = new GetContentCommand(storage, keyElement);
            case DELETE -> iCommand = new DeleteContentCommand(storage, keyElement);
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
        jsonResponse = controller.getResponse();

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