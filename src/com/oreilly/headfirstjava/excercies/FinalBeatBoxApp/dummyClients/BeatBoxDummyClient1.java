package com.oreilly.headfirstjava.excercies.FinalBeatBoxApp.dummyClients;

import javax.sound.midi.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static javax.sound.midi.ShortMessage.*;

public class BeatBoxDummyClient1 {
    
    //Musical stuff vars
    private Sequencer sequencer;
    private Sequence sequence;
    private Track track;

    String[] instrumentNames = {"Bass Drum" , "Closed Hi-Hat" , "Open Hi-Hat", "Acoustic Snare", "Crash Cymbal"
                                , "Hand Cap" , "High Tom" , "Hi Bongo" , "Maracas" , "Whistle" , "Low Conga",
                                "Cowbell" , "Vibraslap" , "Low-mid Tom" , "High Agogo" , "Open Hi Conga"};

    int[] instruments = {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63};

    //Some GUI vars
    private JList<String> incomingList;
    private JTextField userMessage;
    private ArrayList<JCheckBox> checkBoxList;
    
    private Vector<String> listVector = new Vector<>();
    private HashMap<String , boolean[]> otherSeqsMap = new HashMap<>();
    
    private String userName;
    private int nextNum;
    
    private ObjectOutputStream oos;
    private ObjectInputStream  ois;

    //Some networking vars
    PrintWriter writer;
    BufferedReader reader;

    public static void main(String[] args) {
        String name = "user";
        if(args != null && args.length > 0){
            name = args[0];
        }
        new BeatBoxDummyClient1().go(name);
    }

    public void go(String name){
        userName = name;

        //Networking stuff
        try {
            Socket socket = new Socket("localhost" , 4545);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(new RemoteReader());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setUpMidi();
        buildGui();
    }

    private void buildGui() {
        // GUI stuff
        JFrame frame = new JFrame("Cyber BeatBox");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        BorderLayout borderLayout = new BorderLayout();
        JPanel background = new JPanel(borderLayout);
        background.setBorder(BorderFactory.createEmptyBorder(10, 10 , 10 , 10));

        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        JButton start = new JButton("Start");
        start.addActionListener(e -> buildTrackAndStart());
        buttonBox.add(start);

        JButton stop = new JButton("Stop");
        stop.addActionListener(e -> sequencer.stop());
        buttonBox.add(stop);

        JButton upTempo = new JButton("Tempo Up");
        upTempo.addActionListener(e -> changeTempo(1.03f));
        buttonBox.add(upTempo);

        JButton downTempo = new JButton("Tempo Down");
        downTempo.addActionListener(e -> changeTempo(0.97f));
        buttonBox.add(downTempo);

        JButton resetCheckBoxes = new JButton("Clear All Selections");
        resetCheckBoxes.addActionListener(e -> clearAllSelections());
        buttonBox.add(resetCheckBoxes);

        incomingList = new JList<>();
        incomingList.addListSelectionListener(new MyListSelectionListener());
        incomingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane theList = new JScrollPane(incomingList);
        buttonBox.add(theList);
        incomingList.setListData(listVector);

        userMessage = new JTextField(15);
        buttonBox.add(userMessage);

        JButton sendIt = new JButton("Send it");
        sendIt.addActionListener(e -> sendMsgAndBeatBoxPattern());
        buttonBox.add(sendIt);

        Box nameBox = new Box(BoxLayout.Y_AXIS);
        for(String instrumentName : instrumentNames){
            JLabel instrumentLabel = new JLabel(instrumentName);
            instrumentLabel.setBorder(BorderFactory.createEmptyBorder(4,1,4,1));
            nameBox.add(instrumentLabel);
        }

        background.add(BorderLayout.EAST, buttonBox);
        background.add(BorderLayout.WEST, nameBox);

        frame.getContentPane().add(background);

        GridLayout grid = new GridLayout(16, 16);
        grid.setVgap(1);
        grid.setHgap(2);

        JPanel mainPanel = new JPanel(grid);
        background.add(BorderLayout.CENTER, mainPanel);

        checkBoxList = new ArrayList<>();
        for(int i = 0 ; i < 256 ; i++){
            JCheckBox checkBox = new JCheckBox();
            checkBox.setSelected(false);
            checkBoxList.add(checkBox);
            mainPanel.add(checkBox);
        }

        frame.setBounds(50, 50 , 300 , 300);
        frame.pack();
        frame.setVisible(true);
    }

    private void sendMsgAndBeatBoxPattern(){
        boolean[] checkBoxState = new boolean[256];

        for (int i = 0; i < 256; i++) {
            JCheckBox checkBox = checkBoxList.get(i);
            if(checkBox.isSelected()){
                checkBoxState[i] = true;
            }
        }

        try{
            oos.writeObject(userName + nextNum++ + ": " + userMessage.getText());
            oos.writeObject(checkBoxState);
        } catch (Exception e){
            System.out.println("Sorry, could not send it to the server atm");
            e.printStackTrace();
        }

        userMessage.setText("");
        userMessage.requestFocus();
    }

    private void setUpMidi() {
        try{
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ , 4);
            track = sequence.createTrack();
            sequencer.setTempoInBPM(120);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void changeTempo(float tempoMultiplier) {
        float tempoFactor = sequencer.getTempoFactor();
        sequencer.setTempoFactor(tempoFactor * tempoMultiplier);
    }

    private void buildTrackAndStart() {

        int[] trackList;

        sequence.deleteTrack(track);
        track = sequence.createTrack();

        for (int i = 0; i < 16; i++) {

            trackList = new int[16];

            int key = instruments[i];

            for (int j = 0; j < 16; j++) {
                JCheckBox jc = checkBoxList.get(j + 16 * i);
                if( jc.isSelected()){
                    trackList[j] = key;
                } else {
                    trackList[j] = 0;
                }
            }

            makeTracks(trackList);
            track.add(makeEvent(CONTROL_CHANGE, 1, 127, 0 , 16));
        }

        track.add(makeEvent(PROGRAM_CHANGE, 9, 1, 0, 15));

        try{
            sequencer.setSequence(sequence);
            sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            sequencer.setTempoInBPM(120);
            sequencer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static MidiEvent makeEvent(int cmd, int chnl, int one, int two, int tick) {
        MidiEvent midiEvent = null;
        try{
            ShortMessage message = new ShortMessage();
            message.setMessage(cmd, chnl, one , two);
            midiEvent = new MidiEvent(message, tick);
        } catch (Exception e){
            e.printStackTrace();
        }

        return midiEvent;
    }

    private void makeTracks(int[] trackList) {
        for (int i = 0; i < 16; i++) {
            int key = trackList[i];

            if(key != 0){
                track.add(makeEvent(NOTE_ON , 9 , key , 100 , i));
                track.add(makeEvent(NOTE_OFF , 9 , key , 100 , i+1));

            }
        }
    }


    private void restoreBeats() {
        boolean[] checkBoxStates = null;

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("beatbox.ser"))) {
            checkBoxStates = (boolean[]) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 256; i++) {
            checkBoxList.get(i).setSelected(checkBoxStates[i]);
        }

        sequencer.stop();
        buildTrackAndStart();
    }

    private void serializeIt() {

        boolean[] checkBoxState = new boolean[256];

        for (int i = 0; i < 256; i++) {
            checkBoxState[i] = checkBoxList.get(i).isSelected();
        }

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("beatbox.ser"))) {
            oos.writeObject(checkBoxState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearAllSelections() {
        for (int i = 0; i < 256 ; i++) {
            checkBoxList.get(i).setSelected(false);
        }
    }

    class MyListSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent lse) {

            if( !lse.getValueIsAdjusting()){
                String selected = incomingList.getSelectedValue();

                if( selected != null){
                    // now go to map and change the sequence
                    boolean[] selectedState = otherSeqsMap.get(selected);
                    changeSequence(selectedState);
                    sequencer.stop();
                    buildTrackAndStart();
                }
            }
        }

        private void changeSequence(boolean[] checkBoxState) {
            for (int i = 0; i < 256 ; i++) {
                JCheckBox checkBox = checkBoxList.get(i);
                checkBox.setSelected(checkBoxState[i]);
            }
        }
    }

    public class RemoteReader implements Runnable {
        @Override
        public void run() {
            try{
                Object obj;

                while( ((obj = ois.readObject()) != null)){
                    System.out.println("got an object from server");
                    System.out.println(obj.getClass());

                    String nameToShow = (String) obj;
                    boolean[] checkBoxState = (boolean[]) ois.readObject();
                    otherSeqsMap.put(nameToShow , checkBoxState);

                    listVector.add(nameToShow);
                    incomingList.setListData(listVector);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
