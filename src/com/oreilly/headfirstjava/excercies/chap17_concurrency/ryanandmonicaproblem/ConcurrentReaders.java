package com.oreilly.headfirstjava.excercies.chap17_concurrency.ryanandmonicaproblem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentReaders {
    public static void main(String[] args) {
        // For use cases where there are multiple thread reading our data structure while few threads are
        // writing it as well at the same time , we mostly get -- ConcurrentModificationExecption
        // so the normal ArrayList<> DS cant be used for this use case

        //List<Chat> chatHistory = new ArrayList<>();

        //We will need to use appropriate DS from java.util.concurrent package
        //for this purpose , we will use CopyOnWriteArrayList

        List<Chat> chatHistory = new CopyOnWriteArrayList<>();


        ExecutorService threadpool = Executors.newFixedThreadPool(3);

        for(int i=0 ; i < 5 ; i++){
            threadpool.execute(() -> chatHistory.add(new Chat("Hi there")));
            threadpool.execute(() -> System.out.println(chatHistory));
            threadpool.execute(() -> System.out.println(chatHistory));
        }

        threadpool.shutdown();
    }
}

final class Chat{
    final private String message;
    final private LocalDateTime timeStamp;

    public Chat(String message){
        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        String time = timeStamp.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM));
        return time + "  " + message;

    }
}
