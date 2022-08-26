package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private final String address;
    private final int port;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void sendMessage(String message) {
        try (
                Socket socket = new Socket(InetAddress.getByName(address), port);
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                DataInputStream inputStream = new DataInputStream(socket.getInputStream())
        ) {
            outputStream.writeUTF(message);
            System.out.println("Sent: " + message);
            String in = inputStream.readUTF();
            System.out.println("Received: " + in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
