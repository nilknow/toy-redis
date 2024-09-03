package com.nilknow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 6379);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("Connected to Nilknow Redis server. Enter commands (type 'quit' to exit):");

            while (true) {
                System.out.print("> ");
                String command = scanner.nextLine();

                if ("quit".equalsIgnoreCase(command) || "exit".equalsIgnoreCase(command)) {
                    System.out.println("Exiting...");
                    break;
                }

                out.println(command);
                String response = in.readLine();
                System.out.println(response);
            }
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }
}