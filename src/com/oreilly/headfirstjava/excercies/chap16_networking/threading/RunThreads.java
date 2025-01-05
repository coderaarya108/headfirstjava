package com.oreilly.headfirstjava.excercies.chap16_networking.threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunThreads {

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        threadPool.execute(() -> runJob("job 1"));
        threadPool.execute(() -> runJob("Job 2"));

        threadPool.shutdown();
    }

    public static void runJob(String jobName){
        for (int i = 0; i < 25 ; i++) {
            System.out.println(jobName + " is getting run by - " + Thread.currentThread().getName());
        }
    }

}
