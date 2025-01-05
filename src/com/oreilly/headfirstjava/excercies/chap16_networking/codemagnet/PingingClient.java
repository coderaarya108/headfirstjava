package com.oreilly.headfirstjava.excercies.chap16_networking.codemagnet;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.time.format.FormatStyle;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofLocalizedTime;

public class PingingClient {

public static void main(String[] args){

    InetSocketAddress server = new InetSocketAddress("127.0.0.1" , 5000);
    try(SocketChannel channel = SocketChannel.open(server)){
        System.out.println("Networking established");
        PrintWriter writer = new PrintWriter(Channels.newWriter(channel, UTF_8));

        for( int i = 0 ; i < 10 ; i++){
            String message = "ping " + i;
            String currentTime = now().format(ofLocalizedTime(FormatStyle.MEDIUM));
            System.out.println(currentTime + " Sent " + message);
            writer.println(message);
            writer.flush();
            TimeUnit.SECONDS.sleep(1);
        }
    } catch(IOException | InterruptedException e) {
        e.printStackTrace();
    }

}
}
