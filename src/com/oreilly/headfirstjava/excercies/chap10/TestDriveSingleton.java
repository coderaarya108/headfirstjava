package com.oreilly.headfirstjava.excercies.chap10;

import static java.lang.System.out;

public class TestDriveSingleton {
    public static void main(String[] args) {

        Singleton obj1 = Singleton.getSingletonObj();
        obj1.setStr("hello");
        System.out.println(obj1.getStr());

        Singleton obj2 = Singleton.getSingletonObj();
        obj2.setStr("hi");
        System.out.println(obj2.getStr());

        System.out.println("1: " + obj1.getStr() + "   : 2: " + obj2.getStr());

        System.out.println("static var: " + StaticVariable.var);

        String str1 = String.format("I have %.2f, bugs to fix.", 476578.09876);
        System.out.println(str1);

        out.print("true");
        Math.abs(2);

    }
}
