package audioback;

import java.awt.*;
import java.awt.event.*;

import javax.swing.Action;
import javax.swing.JFrame;


public class Main extends JFrame implements Action{
    Button play = new Button("Play");
    Button pause = new Button("Pause");
    Button newB = new Button("New");
    Button timePos = new Button("Time position");
    Button timePercentB = new Button("Time percent");
    Button setTimePercentB = new Button("Set time pc");
    
    TextField sourceField = new TextField("D:/GitHub/Audio-Player/audioback/a.wav");
    TextField timePosField = new TextField();
    TextField timeLenField = new TextField();
    TextField timePercenField = new TextField();


    Music music ;// D:/GitHub/Audio-Player/audioback/a.wav
    // D:/GitHub/Audio-Player/audioback/【Vietsub】Yoru ni Kakeru「夜に駆ける」YOASOBI.mp3

    public Main(){
        setTitle("Music player");
        setSize(500, 500);
        setVisible(true);
        setLayout(null);
        
        sourceField.setSize(500, 50);
        sourceField.setBounds(50, 50, 100, 50);

        play.setSize(100, 50);
        play.setBounds(50, 150, 100, 50);

        pause.setSize(100, 50);
        pause.setBounds(150, 150, 100, 50);

        newB.setSize(100, 50);
        newB.setBounds(250, 150, 100, 50);

        timePosField.setSize(150, 50);
        timePosField.setBounds(50, 250, 150, 50);

        timeLenField.setSize(150, 50);
        timeLenField.setBounds(250, 250, 150, 50);

        timePos.setSize(100, 50);
        timePos.setBounds(350, 150, 100, 50);

        timePercentB.setSize(100, 50);
        timePercentB.setBounds(50, 350, 100, 50);

        timePercenField.setSize(150, 50);
        timePercenField.setBounds(175, 350, 150, 50);

        setTimePercentB.setSize(100, 50);
        setTimePercentB.setBounds(350, 350, 100, 50);

        play.addActionListener(this);
        pause.addActionListener(this);
        newB.addActionListener(this);
        timePos.addActionListener(this);
        timePercentB.addActionListener(this);
        setTimePercentB.addActionListener(this);

        add(sourceField);
        add(play);
        add(pause);
        add(newB);
        add(timePosField);
        add(timeLenField);
        add(timePos);
        add(timePercentB);
        add(timePercenField);
        add(setTimePercentB);
    }

    public static void main(String[] args) {
        new Main();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource() == play){
            try {
                if(music == null){
                    String source = sourceField.getText();
                    String type = source.substring(source.length()-3, source.length());
                    if(type.equals("mp3")){
                        music = new Mp3Music(source);
                    }else {
                        music = new WavMusic(source);
                    }
                }
                if(music.isPause()){
                    timeLenField.setText("" + music.getMusicTimeLength());
                    music.play();
                    music.setVolume(0.3f);
                }
            } catch (Exception ex) {
                // TODO: handle exception
            }
        }
        if(e.getSource() == pause){
            music.pause();
        }
        if(e.getSource() == newB){
            sourceField.setText("");
            // music.pause();
            // music = null;
            music.stop();
            music = null;
        }
        if(e.getSource() == timePos){
            try {
                timePosField.setText(""+music.getMusicTimePosition());
            } catch (Exception ex) {
                // TODO: handle exception
            } 
        }
        if(e.getSource() == timePercentB){
            timePercenField.setText("" + music.getMusicTimePercent());
        }
        if(e.getSource() == setTimePercentB){
            Double timePercetn = Double.parseDouble(timePercenField.getText());
            music.pause();
            music = null;
            String source = sourceField.getText();
            String type = source.substring(source.length()-3, source.length());
            if(type.equals("mp3")){
                music = new Mp3Music(source);
            }else {
                music = new WavMusic(source);
            }
            music.setMusicTimePercent(timePercetn);
            music.play();
            music.setVolume(0.3f);
        }
    }

    @Override
    public Object getValue(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void putValue(String key, Object value) {
        // TODO Auto-generated method stub
        
    }
}
