package com.oreilly.headfirstjava.excercies.FinalBeatBoxApp;

import com.oreilly.headfirstjava.excercies.chap16_networking.chatapp.v1.ClientHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Channels;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MusicServer {

    List<ObjectOutputStream> clientOutputStreams = new ArrayList<>();

    public static void main(String[] args) {
        new MusicServer().go();
    }

    public void go() {
        try{
            ServerSocket serverSocket = new ServerSocket(4545);
            ExecutorService threadPool = Executors.newCachedThreadPool();

            while (!serverSocket.isClosed()){
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                clientOutputStreams.add(oos);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                threadPool.execute(clientHandler);
                System.out.println("Got a connection");

            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    class ClientHandler implements Runnable{

        private ObjectInputStream ois;

        public ClientHandler(Socket socket){
            try{
                ois = new ObjectInputStream(socket.getInputStream());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            Object userName ;
            Object beatSequence;

            try{
                while( (userName = ois.readObject()) != null){
                    beatSequence = ois.readObject();

                    System.out.println("read two objects");
                    tellEveryOne(userName ,  beatSequence);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void tellEveryOne(Object userName, Object beatSequence) {
        for( ObjectOutputStream oos : clientOutputStreams){
            try{
                oos.writeObject(userName);
                oos.writeObject(beatSequence);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}