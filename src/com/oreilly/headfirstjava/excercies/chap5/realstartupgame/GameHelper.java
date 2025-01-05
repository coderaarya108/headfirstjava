package com.oreilly.headfirstjava.excercies.chap5.realstartupgame;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameHelper {


    private ArrayList<String> startupLoc = new ArrayList<>(3);

    private int userInput;


    public ArrayList<String> getStartupLoc(){

        boolean isRowSet = new Random().nextBoolean();
        if( isRowSet){
            int startRowCell = new Random().nextInt(3);
            int colCell = new Random().nextInt(7);
            this.startupLoc.add(startRowCell+ "" +  colCell);
            this.startupLoc.add(startRowCell + 1 + "" +  colCell);
            this.startupLoc.add(startRowCell + 2 + "" +  colCell);
        } else {
            int rowCell = new Random().nextInt(7);
            int startRColCell = new Random().nextInt(3);
            this.startupLoc.add(rowCell + "" + ( startRColCell)) ;
            this.startupLoc.add(rowCell + "" + ( startRColCell + 1 )) ;
            this.startupLoc.add(rowCell + "" + ( startRColCell + 2 )) ;
        }

        return this.startupLoc;
    }

    public String getUserInput(){

        return new Scanner(System.in).next();
    }
}


/*
    public static void main(String[] args) {

        boolean testFlag = new Random().nextBoolean();
        System.out.println(testFlag);
    }
        public static void main(String[] args) {

        ArrayList<String> locCells ;
        locCells = new GameHelper().getStartupLoc();

        System.out.println(locCells);
    }
 */