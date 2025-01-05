package com.oreilly.headfirstjava.excercies.chap16_networking.threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTester {
    public static void main(String[] args) {

        // This is the old way to start thread and one of the way

        /*
        Thread thread = new Thread(new ThreadTestDrive());
        thread.start();
        */

        //New way
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> new ThreadTestDrive().run());

        System.out.println(Thread.currentThread().getName() + " : back in main");
        //Thread.dumpStack();

        executorService.shutdown();
    }
}
