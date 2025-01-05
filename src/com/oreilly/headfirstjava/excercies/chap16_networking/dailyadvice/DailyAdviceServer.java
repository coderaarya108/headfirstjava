package com.oreilly.headfirstjava.excercies.chap16_networking.dailyadvice;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DailyAdviceServer {

    final private String[] adviceList = {
            "Take smaller bites",
            "Go for the skinny fit , they make you look uber sexy",
            "One word : inappropriate",
            "Just for today , be honest. Tell your boss what you really think"
    } ;

    private final Random random = new Random();

    public void go(){
        try(ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress(5000));

            while( serverSocketChannel.isOpen()){
                SocketChannel clientChannel = serverSocketChannel.accept();
                PrintWriter writer = new PrintWriter(Channels.newWriter(clientChannel, StandardCharsets.UTF_8));
                String advice = getAdvice();
                writer.println(advice);
                writer.close();
                System.out.println(advice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getAdvice() {
        int nextAdvice = random.nextInt(adviceList.length);
        return adviceList[nextAdvice];
    }

    public static void main(String[] args) {
        new DailyAdviceServer().go();
    }
}
