package com.nilknow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Hello world!
 */
public class Server {
    private final Map<String, String> stringValues = new HashMap<>();
    private final Map<String, List<String>> lists = new HashMap<>();
    private final Map<String, Set<String>> sets = new HashMap<>();
    private final BlockingQueue<Command> commandQueue = new LinkedBlockingQueue<>();

    public void start(int port){
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Toy Redis server started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(6379);
    }
}
