package com.oreilly.headfirstjava.excercies.chap16_networking.chatapp.v1;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class SimpleChatClientA {

    private JTextField textField;
    private PrintWriter printWriter;

    public static void main(String[] args) {
        new SimpleChatClientA().buildChatGui();
    }

    public void buildChatGui(){
        setUpNetworking();

        textField = new JTextField( 15);

        JButton sendMsgButton = new JButton("Send");
        sendMsgButton.addActionListener(e -> sendMsgToServer());

        JPanel mainPanel = new JPanel();
        mainPanel.add(textField);
        mainPanel.add(sendMsgButton);

        JFrame chatFrame = new JFrame("Ludicrously Simple Chat Client");
        chatFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        chatFrame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        chatFrame.setSize(400, 400);
        chatFrame.setVisible(true);
    }

    private void sendMsgToServer() {
        printWriter.println(textField.getText());
        printWriter.flush();
        textField.setText("");
        textField.requestFocus();
    }

    private void setUpNetworking() {
        SocketAddress serverAddr = new InetSocketAddress("localhost", 5000);

        try{
            SocketChannel serverChannel = SocketChannel.open(serverAddr);
            printWriter = new PrintWriter(Channels.newWriter(serverChannel, StandardCharsets.UTF_8));
            System.out.println("Networking established. Client running at : " + serverChannel.getLocalAddress());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
