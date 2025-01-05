package com.oreilly.headfirstjava.excercies.chap5.realstartupgame;


import java.util.ArrayList;

public class StartUpBust {

    private GameHelper helper = new GameHelper();
    private Startup startup = new Startup();
    private int noOfUserGuesses = 0;

    private ArrayList<Startup> startups = new ArrayList<>();

    public void setUpGame(){
       Startup ola = new Startup();
       ola.setName("OLA");

       Startup uber = new Startup();
       uber.setName("UBER");

       Startup rapido = new Startup();
       rapido.setName("Rapido");

       startups.add(ola);
       startups.add(uber);
       startups.add(rapido);

       ola.setStartupLocCells(helper.getStartupLoc());
       uber.setStartupLocCells(helper.getStartupLoc());
       rapido.setStartupLocCells(helper.getStartupLoc());


    }

    public void startPlaying(){

        noOfUserGuesses++;
        while(! startups.isEmpty()){
            String userInput = helper.getUserInput();

            for( Startup stup : startups){
                if(stup.checkYourself(userInput).equals("kill")){
                    startups.remove(stup);
                }
            }

        }
    }


}
