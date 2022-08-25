package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Session extends Thread {

    private final Socket socket;

    public Session(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
        ) {
            String out = "A record # 12 was sent!";
            String in = inputStream.readUTF();
            System.out.println("Received: " + in);
            System.out.println("Sent: " + out);
            outputStream.writeUTF(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
