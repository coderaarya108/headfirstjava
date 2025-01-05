package com.oreilly.headfirstjava.excercies.chap16_networking.dailyadvice;

public class ThreadApp {

    public static void main(String[] args) {
        Thread thread1 = new Thread(new DailyAdviceClient());
        thread1.start();

        Thread thread2 = new Thread(new DailyAdviceClient());
        thread2.start();

        Thread thread3 = new Thread(new DailyAdviceClient());
        thread3.start();
    }
}
