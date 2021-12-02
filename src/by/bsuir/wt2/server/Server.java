package by.bsuir.wt2.server;

import by.bsuir.wt2.server.manager.ServerManager;

public class Server {
    public static void main(String[] args) {
        ServerManager server = new ServerManager();
        server.start();
    }
}
