package com.oreilly.headfirstjava.excercies.chap11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Songv2 implements Comparable<Songv2> {

    private String title ;
    private String artist;
    private int bpm;

    public Songv2( String title , String artist , int bpm){
        this.title = title;
        this.artist = artist;
        this.bpm = bpm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public static List<Songv2> getSongsList(){
            List<Songv2>  songsList = new ArrayList<>();
            songsList.add(new Songv2("We dont talk anymore" , "Charlie Puth" , 56));
            songsList.add(new Songv2("Royals" , "Lorde" , 75));
            songsList.add(new Songv2("Everything at once" , "Lenka" , 64));
            songsList.add(new Songv2("Breaking my habit" , "Linkin Park" , 128));
            songsList.add(new Songv2("America Idiot" , "Green Day" , 256));

            return songsList;
        }

    @Override
    public int compareTo(Songv2 songv2) {
        return  songv2.bpm - this.bpm ;
    }

    @Override
    public String toString() {
        return this.artist + " , " +  this.title + " , " +  this.bpm;
    }
}
