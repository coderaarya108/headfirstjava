package com.oreilly.headfirstjava.excercies.chap5.startupgame;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Startup {

        private ArrayList<Integer> locationCells = new ArrayList<>();

        public ArrayList<Integer> getLocationCells(){
            return this.locationCells;
        }

    public void setLocationCells(ArrayList<Integer> locationCells) {
        this.locationCells = locationCells;
    }

    public String startUpGame( int userGuess){

        String result = "miss";

        if( locationCells.contains(userGuess)){
            result = "hit";

            locationCells.remove(locationCells.indexOf(userGuess));
        }

        if( locationCells.isEmpty()){
            result = "kill";
        }

        return result;
    }
}
