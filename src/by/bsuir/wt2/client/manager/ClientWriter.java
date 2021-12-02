package by.bsuir.wt2.client.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientWriter extends Thread {
    private final ClientManager client;

    public ClientWriter(ClientManager client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (client.isRunning()) {
                String response = reader.readLine();
                client.sendMessage(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
