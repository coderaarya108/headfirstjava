package com.oreilly.headfirstjava.excercies.chap16_networking.threading;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ThreadTestDrive implements Runnable{
    @Override
    public void run() {
        doThis();
    }

    private void doThis() {
        doMore();
    }

    private void doMore() {
        try {
            System.out.println("Time before sleep : " + LocalDateTime.now());
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() + ": top o' the stack . Time after sleep : " + LocalDateTime.now());
       // Thread.dumpStack();
    }
}
