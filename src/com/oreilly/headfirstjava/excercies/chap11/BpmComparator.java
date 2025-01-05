package com.oreilly.headfirstjava.excercies.chap11;

import java.util.Comparator;

public class BpmComparator implements Comparator<Songv2> {


    @Override
    public int compare(Songv2 songv2_1 , Songv2 songv2_2) {
        return songv2_1.getBpm() - songv2_2.getBpm();
    }
}
