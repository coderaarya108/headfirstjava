package com.oreilly.headfirstjava.excercies.chap11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SongTestDrive {

    public static void main(String[] args) {

        List<Songv2> songList = Songv2.getSongsList();
        System.out.println(songList);
        Collections.sort(songList);
        System.out.println(songList);
        songList.sort( new BpmComparator());
        System.out.println(songList);
        songList.sort(new TitleComparator());
        System.out.println(songList);
        songList.sort(new Comparator<Songv2>() {
            @Override
            public int compare(Songv2 o1, Songv2 o2) {
                return o1.getArtist().compareTo(o2.getArtist());
            }
        });
        System.out.println(songList);

    }
}
