package com.oreilly.headfirstjava.excercies.chap17_concurrency.ryanandmonicaproblem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LostUpdate {

    public static void main(String[] args) throws InterruptedException {
        Balance balance = new Balance();
        ExecutorService threadPool = Executors.newFixedThreadPool(6);

        for(int i = 0; i < 1000 ; i++){
            threadPool.execute(balance::increment);
        }
        threadPool.shutdown();

        if(threadPool.awaitTermination(1 , TimeUnit.MINUTES)){
            System.out.println("Balance is : " + balance.balance);
        }
    }
}

class Balance{
    AtomicInteger balance = new AtomicInteger(0);

    public void increment(){
        balance.incrementAndGet();
    }
}
