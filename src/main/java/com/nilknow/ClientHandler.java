package com.nilknow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nilknow.Storage.*;

class ClientHandler extends Thread {
    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String line;
            while ((line = in.readLine()) != null) {
                Object response = processCommand(line);
                out.println(response != null ? response : "");
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    private Object processCommand(String line) {
        String[] parts = line.split(" ");
        String type = parts[0];
        String key = parts[1];
        String value = parts.length > 2 ? parts[2] : null;

        switch (type) {
            case "SET":
                stringValues.put(key, value);
                return "OK";
            case "GET":
                return stringValues.getOrDefault(key, null);
            case "EXISTS":
                return stringValues.containsKey(key) ? "1" : "0";
            case "DEL":
                stringValues.remove(key);
                lists.remove(key);
                sets.remove(key);
                return "OK";
            case "LPUSH":
                lists.computeIfAbsent(key, k -> new ArrayList<>()).add(0, value);
                return "OK";
            case "SADD":
                sets.computeIfAbsent(key, k -> new HashSet<>()).add(value);
                return "OK";
            case "SMEMBERS":
                Set<String> set = sets.get(key);
                if (set != null) {
                    return set;
                }
                return null;
            default:
                return "ERR unknown command";
        }
    }
}