package com.oreilly.headfirstjava.excercies.chap15_beatbox.beatboxapp;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static javax.sound.midi.ShortMessage.*;

public class BeatBox {

    private ArrayList<JCheckBox> checkBoxList;

    private Sequencer sequencer;
    private Sequence sequence;
    private Track track;

    String[] instrumentNames = {"Bass Drum" , "Closed Hi-Hat" , "Open Hi-Hat", "Acoustic Snare", "Crash Cymbal"
                                , "Hand Cap" , "High Tom" , "Hi Bongo" , "Maracas" , "Whistle" , "Low Conga",
                                "Cowbell" , "Vibraslap" , "Low-mid Tom" , "High Agogo" , "Open Hi Conga"};

    int[] instruments = {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63};

    //Some GUI vars
    JTextArea incomingMsgs;
    JTextField sendMsg;

    //Some networking vars
    PrintWriter writer;
    BufferedReader reader;

    public static void main(String[] args) {
        new BeatBox().go();
    }

    public void go(){

        //Networking stuff
        SocketAddress beatBoxServerAddr = new InetSocketAddress("localhost" , 5000);
        try {
            SocketChannel serverChannel =  SocketChannel.open(beatBoxServerAddr);

            writer = new PrintWriter(Channels.newWriter(serverChannel , StandardCharsets.UTF_8));
            reader = new BufferedReader(Channels.newReader(serverChannel, StandardCharsets.UTF_8));

        } catch (Exception e) {
            e.printStackTrace();
        }

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

        JButton serializeIt = new JButton("Serialize it");
        serializeIt.addActionListener(e -> serializeIt());
        buttonBox.add(serializeIt);

        JButton restoreBeats = new JButton("Restore it");
        restoreBeats.addActionListener(e -> restoreBeats());
        buttonBox.add(restoreBeats);

        JButton sendIt = new JButton("Send it");
        sendIt.addActionListener(e -> {
            try {
                sendMsgAndBeatBoxPattern(sendMsg.getText());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Box nameBox = new Box(BoxLayout.Y_AXIS);
        for(String instrumentName : instrumentNames){
            JLabel instrumentLabel = new JLabel(instrumentName);
            instrumentLabel.setBorder(BorderFactory.createEmptyBorder(4,1,4,1));
            nameBox.add(instrumentLabel);
        }

        // add text area and field for chat client module
        addTextComponents(buttonBox);
        buttonBox.add(sendIt);
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

        setUpMidi();

        frame.setBounds(50, 50 , 300 , 300);
        frame.pack();
        frame.setVisible(true);

        //listen to the messages from server
        listenServerMessages();

    }

    private void listenServerMessages() {

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() ->{
            String message ;
            try{
                while((message = reader.readLine()) != null){
                    incomingMsgs.append(message + "\n");
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    private void sendMsgAndBeatBoxPattern(String message) throws IOException {
        writer.println(sendMsg.getText());
        writer.flush();
        sendMsg.setText("");
        sendMsg.requestFocus();
    }

    private void addTextComponents(Box buttonBox) {

        incomingMsgs = new JTextArea(10, 15);
        incomingMsgs.setLineWrap(true);
        incomingMsgs.setWrapStyleWord(true);
        incomingMsgs.setEditable(false);

        JScrollPane scroller = new JScrollPane(incomingMsgs);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        sendMsg = new JTextField(15);

        buttonBox.add(scroller);
        buttonBox.add(sendMsg);
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
}
