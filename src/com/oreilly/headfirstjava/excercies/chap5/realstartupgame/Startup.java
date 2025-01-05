package com.oreilly.headfirstjava.excercies.chap5.realstartupgame;

import java.util.ArrayList;

public class Startup {

    private int userGuess ;
    private String name ;
    private ArrayList<String> startupLocCells = new ArrayList<>();

    public int getUserGuess(){
        return this.userGuess;
    }

    public void setUserGuess(int userGuess){
        this.userGuess = userGuess;
    }

    public void setStartupLocCells(ArrayList<String> startupLocCells) {
        this.startupLocCells = startupLocCells;
    }

    public ArrayList<String> getStartupLocCells(){
        return this.startupLocCells;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String checkYourself( String userGuess){

        String result = "miss";

        if(this.startupLocCells.contains(userGuess)){
            result = "hit";
            this.startupLocCells.remove(userGuess);
        }

        if( this.startupLocCells.isEmpty()){
            result = "kill";
        }

        return result;

    }
}
