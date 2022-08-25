package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {

    private final String address;
    private final int port;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {
        try (
                Socket socket = new Socket(InetAddress.getByName(address), port);
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
        ) {
            System.out.println("Client started!");
            String out = "Give me a record # 12";
            outputStream.writeUTF(out);
            System.out.println("Sent: " + out);
            String read = inputStream.readUTF();
            System.out.println("Received: " + read);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
