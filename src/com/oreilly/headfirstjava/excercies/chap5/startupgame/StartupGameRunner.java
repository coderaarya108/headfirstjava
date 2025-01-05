package com.oreilly.headfirstjava.excercies.chap5.startupgame;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class StartupGameRunner {

    public static void main(String[] args) {

        Startup startup = new Startup();

        int noOfGuesses = 0;

        boolean gameIsOn = true;

        int startLoc = new Random(5).nextInt(5);

        ArrayList<Integer> locationCells = new ArrayList<>();
        locationCells.add(startLoc);
        locationCells.add(startLoc+1);
        locationCells.add(startLoc+2);

        startup.setLocationCells(locationCells);


        while( gameIsOn){
            noOfGuesses++;
            System.out.println(startup.getLocationCells());
            String result = startup.startUpGame(new Scanner(System.in).nextInt());
            if (result.equals("kill")) {
                gameIsOn = false;
                System.out.println(result);
            } else {
                System.out.println(result);
            }
        }

        System.out.println("You took " + noOfGuesses + " guesses");
    }

}
