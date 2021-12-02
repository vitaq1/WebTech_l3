package by.bsuir.wt2.server.manager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerManager extends Thread {
    public static final int PORT = 25565;
    private static final int BACKLOG = 50;

    private ServerSocket serverSocket;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORT, BACKLOG, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Server is running");

        while (true) {
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
                System.out.println("Client connected");
                ServerClientManager client = new ServerClientManager(clientSocket, this);
                client.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect(ServerClientManager client) {
        try {
            client.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Client disconnected");
    }
}
