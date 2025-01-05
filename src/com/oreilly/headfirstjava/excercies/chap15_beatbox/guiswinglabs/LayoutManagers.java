package com.oreilly.headfirstjava.excercies.chap15_beatbox.guiswinglabs;

import javax.swing.*;
import java.awt.*;

public class LayoutManagers {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        panel.setBackground(Color.DARK_GRAY);

        JButton button1 = new JButton("shock me");
        JButton button2 = new JButton("bliss");
        JButton button3 = new JButton("huh?");

        panel.add(button1);
        panel.add(button2);
        panel.add(button3);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        frame.getContentPane().add(BorderLayout.EAST , panel);
        frame.setSize(300,300);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }



}
