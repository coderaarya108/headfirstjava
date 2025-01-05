package com.oreilly.headfirstjava.excercies.chap16_networking.threading;

import org.w3c.dom.ls.LSOutput;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTestWithLatchCountDown {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CountDownLatch latch = new CountDownLatch(1);

        executorService.execute(() -> testDriveLatchCountDown(latch));
        System.out.println(Thread.currentThread().getName() + " thread here");
        latch.countDown();

        executorService.shutdown();
    }

    public static void testDriveLatchCountDown(CountDownLatch latch){
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() + "  top o stack");
    }
}
