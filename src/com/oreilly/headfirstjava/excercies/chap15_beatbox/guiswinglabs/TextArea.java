package com.oreilly.headfirstjava.excercies.chap15_beatbox.guiswinglabs;

import javax.swing.*;
import java.awt.*;

public class TextArea {

    public static void main(String[] args) {
        new TextArea().go();
    }

    public void go(){
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        JTextArea textArea = new JTextArea(5, 10);
        textArea.setLineWrap(true);

        JButton button = new JButton("Click it!");
        button.addActionListener(e -> textArea.append("Just Clicked it! \n") );

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        //Add a checkbox
        JCheckBox checkBox = new JCheckBox("Check me up");
        checkBox.addActionListener(e -> {
            if(checkBox.isSelected()){
                textArea.append("Did you just checked me ! Duh! \n");
            } else {
                textArea.setText("");
                textArea.append("Cleared");
            }
        });

        panel.add(scrollPane);

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.getContentPane().add(BorderLayout.SOUTH, button);
        frame.getContentPane().add(BorderLayout.WEST, checkBox);

        frame.setSize(300, 300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
