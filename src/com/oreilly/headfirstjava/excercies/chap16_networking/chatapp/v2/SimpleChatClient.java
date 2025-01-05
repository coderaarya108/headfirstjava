package com.oreilly.headfirstjava.excercies.chap16_networking.chatapp.v2;

import com.oreilly.headfirstjava.excercies.chap17_concurrency.ryanandmonicaproblem.TwoThreadsWriting;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleChatClient {

    private TwoThreadsWriting test;
    private JTextField outgoingMsg;
    private PrintWriter clientWriter;
    private BufferedReader clientReader;
    private JTextArea messageDisplayBoard;

    public void go(){

        //Set up the networking - channels etc
        setUpNetworking();

        // Building GUI
        JFrame chatWindow = new JFrame("Simple Chat Client");
        chatWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();

        //Setting TextField -> outgoing to enter the message
        outgoingMsg = new JTextField(25);


        //Creating Send button and adding action listener
        JButton sendBtn = new JButton("Send");
        sendBtn.addActionListener(e -> sendMsg(outgoingMsg.getText()));



        mainPanel.add(outgoingMsg);
        mainPanel.add(sendBtn);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new IncomingReader());

        chatWindow.add(BorderLayout.CENTER, mainPanel);
        chatWindow.add(BorderLayout.NORTH, getScroller());
        chatWindow.setSize(400, 400);
        chatWindow.setVisible(true);

    }

    public void setUpNetworking(){
        SocketAddress serverAddress = new InetSocketAddress("localhost" , 5000);
        try {
            SocketChannel serverChannel = SocketChannel.open(serverAddress);
            clientWriter = new PrintWriter(Channels.newWriter(serverChannel, StandardCharsets.UTF_8));
            clientReader = new BufferedReader(Channels.newReader(serverChannel, StandardCharsets.UTF_8));

            System.out.println("Networking established");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMsg(String message){
        clientWriter.println(message);
        clientWriter.flush();
        outgoingMsg.setText("");
        outgoingMsg.requestFocus();
    }

    private JScrollPane getScroller(){
        //Creating TextArea to show incoming messages from server
        messageDisplayBoard = new JTextArea(10, 15);
        messageDisplayBoard.setLineWrap(true);
        messageDisplayBoard.setWrapStyleWord(false);
        messageDisplayBoard.setEditable(false);

        //Creating scroll pane to give scrolling feature to text area
        JScrollPane scroller = new JScrollPane(messageDisplayBoard);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        return scroller;
    }

    public static void main(String[] args) {
        new SimpleChatClient().go();

    }

    public class IncomingReader implements Runnable{
        @Override
        public void run() {

            String message;

            try{
                while( (message = clientReader.readLine()) != null){
                    System.out.println(" Read : " + message);
                    messageDisplayBoard.append(message + "\n");
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
