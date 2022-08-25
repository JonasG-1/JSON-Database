package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Main {

    public static String ADDRESS = "127.0.0.1";
    public static int PORT = 23456;
    public static void main(String[] args) {
        //Client client = new Client(ADDRESS, PORT);
        //client.start();
        try (
                Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
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