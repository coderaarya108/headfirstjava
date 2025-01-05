package com.oreilly.headfirstjava.excercies.chap10;

public class Singleton {

    private static Singleton sharedObjectOfSingleton = new Singleton();
    private String str ;

    private Singleton(){

    }

    public static Singleton getSingletonObj(){
        return sharedObjectOfSingleton;
    }

    public void setStr(String s){
        str = s;
    }

    public String getStr() {
        return str;
    }
}
