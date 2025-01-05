package com.oreilly.headfirstjava.excercies.chap17_concurrency.ryanandmonicaproblem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class RyanAndMonicaTestDriveWithAtomic {

    public static void main(String[] args) {
        BankAccountWithAtomic bankAccountWithAtomic = new BankAccountWithAtomic();

        RyanAndMonicaJobWithAtomic ryan = new RyanAndMonicaJobWithAtomic("Ryan", 50 , bankAccountWithAtomic);
        RyanAndMonicaJobWithAtomic monica = new RyanAndMonicaJobWithAtomic("Monica", 100 , bankAccountWithAtomic);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(ryan);
        executorService.execute(monica);

        executorService.shutdown();
    }
}

class RyanAndMonicaJobWithAtomic implements Runnable{
    final private String name;
    final private int amount;
    final private BankAccountWithAtomic bankAccountWithAtomic;

    public RyanAndMonicaJobWithAtomic(String name , int amount , BankAccountWithAtomic bankAccountWithAtomic){
        this.name = name;
        this.amount = amount;
        this.bankAccountWithAtomic = bankAccountWithAtomic;
    }

    @Override
    public void run() {
        goShopping(amount);
    }

    private void goShopping(int amount){
            System.out.println(name + " is about to spend. Balance right now : " + bankAccountWithAtomic.getBalance());
            bankAccountWithAtomic.spend(name, amount);
            System.out.println(name + " finished spending. Balance right now : " + bankAccountWithAtomic.getBalance());
    }
}

class BankAccountWithAtomic{
    private final AtomicInteger balance = new AtomicInteger(100);

    public int getBalance(){
        return balance.get();
    }

    public void spend(String name , int amount){
        int initialBalance = balance.get();

        if(initialBalance >= amount){
            boolean success = balance.compareAndSet(initialBalance , initialBalance - amount);
            if(!success){
                System.out.println(name + " , sorry something went wrong , you might want to retry! ");
            }
        } else {
            System.out.println("Balance got low for : " + name);
        }
    }
}



