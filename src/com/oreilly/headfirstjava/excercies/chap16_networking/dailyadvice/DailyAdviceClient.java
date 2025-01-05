package com.oreilly.headfirstjava.excercies.chap16_networking.dailyadvice;

import java.io.BufferedReader;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class DailyAdviceClient implements Runnable{
    public static void main(String[] args) {
        new DailyAdviceClient().go();
    }

    private void go() {
        SocketAddress socketAddress = new InetSocketAddress("localhost" , 5000);
        try(SocketChannel channel = SocketChannel.open(socketAddress)) {

            Reader channelReader = Channels.newReader(channel, StandardCharsets.UTF_8);
            BufferedReader bReader = new BufferedReader(channelReader);

            System.out.println( Thread.currentThread().getName() + "Today you should : " + bReader.readLine());
            bReader.close();
            Thread.sleep(30000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        new DailyAdviceClient().go();
    }
}
