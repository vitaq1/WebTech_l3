package by.bsuir.wt2.server.manager;

import by.bsuir.wt2.server.command.Command;
import by.bsuir.wt2.server.command.CommandProvider;
import by.bsuir.wt2.server.command.CommandException;
import by.bsuir.wt2.server.command.implementation.DisconnectCommand;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class ServerClientManager extends Thread {
    private final Socket socket;
    private final ServerManager serverManager;

    private BufferedReader reader;
    private PrintWriter writer;

    public ServerClientManager(Socket socket, ServerManager serverManager) {
        this.socket = socket;
        this.serverManager = serverManager;
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendMessage("Commands:\n AUTH USER OR ADMIN\n DISC\n VIEW\n CREATE <firstName> <lastName>\n EDIT <id> <firstName> <lastName>");

        boolean running = true;
        do {
            try {
                String request = readMessage();
                if (request == null) {
                    break;
                }

                Command command = CommandProvider.getInstance().getCommand(request);
                String response = command.execute(this, request);
                sendMessage(response);

                if (command instanceof DisconnectCommand) {
                    running = false;
                }
            } catch (CommandException e) {
                e.printStackTrace();
                sendMessage(e.getMessage());
            }
        } while (running);

        serverManager.disconnect(this);
    }

    private String readMessage() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerClientManager that = (ServerClientManager) o;
        return socket.equals(that.socket) && serverManager.equals(that.serverManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, serverManager);
    }
}
