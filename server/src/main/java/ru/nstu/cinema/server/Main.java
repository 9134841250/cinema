package ru.nstu.cinema.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Main class starting server
 */
public class Main {
    public static void main(String[] ar) {

        ServerSocket serverSocket = null;

        boolean listeningSocket = true;
        try {
            serverSocket = new ServerSocket(6666);
            System.out.println("Сервер доступен и ждет подключений");
        } catch (IOException e) {
            System.err.println("Could not listen on port: 6666");
        }

        while (listeningSocket) {
            Socket clientSocket;
            try {
                if (serverSocket == null) throw new IOException();
                clientSocket = serverSocket.accept();
                ServerHelper mini = new ServerHelper(clientSocket);
                mini.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}