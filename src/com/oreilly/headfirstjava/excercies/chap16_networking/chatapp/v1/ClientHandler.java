package com.oreilly.headfirstjava.excercies.chap16_networking.chatapp.v1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ClientHandler implements Runnable {
    BufferedReader reader;
    SocketChannel socket;

    List<PrintWriter> clientWriters;

    public ClientHandler(SocketChannel clientSocket, List<PrintWriter> clientWriters) {
        socket = clientSocket;
        reader = new BufferedReader(Channels.newReader(socket, UTF_8));
        this.clientWriters = clientWriters;
    }
    public void run() {
        String message;
        try {
            while ((message = reader.readLine()) != null) {
                System.out.println("read " + message);
                tellEveryone(message);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void tellEveryone(String message) {
        for (PrintWriter writer : clientWriters) {
            writer.println(message);
            writer.flush();
        }
    }
}
