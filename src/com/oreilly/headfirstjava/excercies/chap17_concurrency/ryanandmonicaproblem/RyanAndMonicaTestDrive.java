package com.oreilly.headfirstjava.excercies.chap17_concurrency.ryanandmonicaproblem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RyanAndMonicaTestDrive {


    public static void main(String[] args) {
        BankAccount bankAccount = new BankAccount();

        RyanAndMonicaJob ryan = new RyanAndMonicaJob("Ryan", 50 , bankAccount);
        RyanAndMonicaJob monica = new RyanAndMonicaJob("Monica", 100 , bankAccount);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(ryan);
        executorService.execute(monica);

        executorService.shutdown();

    }
}


class RyanAndMonicaJob implements Runnable{

    final private String name;
    final private int amount;
    final private BankAccount bankAccount;

     RyanAndMonicaJob(String name , int amount , BankAccount bankAccount){
        this.name = name;
        this.amount = amount;
        this.bankAccount = bankAccount;
    }


    @Override
    public void run() {
        goShopping(amount);
    }

    private void goShopping(int amount){
        //synchronized (bankAccount){
        //if( amount <= bankAccount.getBalance()){
            System.out.println(name + " is about to spend. Balance right now : " + bankAccount.getBalance());
            bankAccount.spend(name, amount);
            System.out.println(name + " finished spending. Balance right now : " + bankAccount.getBalance());
       // } else {

        //}
        //}
    }

}

class BankAccount{
    private int balance = 100;

     synchronized int getBalance(){
        return this.balance;
    }

     synchronized void spend(String name , int amount){
        if(balance >= amount){
            balance = balance - amount;
            if(balance < 0){
                System.out.println("Overdrawn");
            }
        } else {
            System.out.println("Short of money, cant proceed for " + name + "!");
        }
    }
}



