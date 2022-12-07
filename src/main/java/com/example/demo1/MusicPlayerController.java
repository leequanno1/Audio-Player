package com.example.demo1;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;


public class MusicPlayerController implements javafx.fxml.Initializable {
    @FXML
    private FontAwesomeIcon playButton;
    @FXML
    private Line unLoop, unRandom;
    private Boolean stateLoop = true, statePlay = false, stateRandom = false, stateVolume = false;
    @FXML
    private Slider volume, percent;
    @FXML
    private ImageView image;
    @FXML
    private Label nameSong, timePosition, singer, timeEnd;
    private ArrayList<Song> songs = new ArrayList<>();
    @FXML
    private ScrollPane list;
    private int indexSong = 0;
    private VBox vBox = new VBox();
    private Music music;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setInit();
        int index = 0;
        for (Song s : songs) {
            try {
                index++;
                AnchorPane pane = FXMLLoader.load(getClass().getResource("item.fxml"));
                //create new label
                Label newLabel = new Label(s.getName());
                newLabel.setLayoutX(56);
                newLabel.setLayoutY(9);
                newLabel.setText(s.getName());
                newLabel.setFont(new Font("Arial", 15));

                //create new number song
                Label newLabelNumberSong = new Label(String.valueOf(index));
                newLabelNumberSong.setLayoutX(24);
                newLabelNumberSong.setLayoutY(9);
                newLabelNumberSong.setFont(new Font("Arial", 15));

                pane.getChildren().set(0, newLabel);
                pane.getChildren().set(1, newLabelNumberSong);

                pane.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                    String itemSelectString = pane.getChildren().get(1).toString();
                    int itemSelect = Integer.parseInt(itemSelectString.substring(itemSelectString.length() - 2, itemSelectString.length() - 1));
                    indexSong = itemSelect - 1;
                    changeTheme();
                    changeSong();
                    music.stop();
                    setTimeEnd();
                    setTimePosition();
                    percent.setValue(0);
                    image.setRotate(0);
                    playButton.setGlyphName("PLAY");
                    statePlay = false;
                });

                vBox.getChildren().add(pane);
                vBox.setSpacing(10);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            list.setContent(vBox);
            changeTheme();
        }
    }
    public void setInit(){
        setImageSong();
        addSong();
        music = new WavMusic(songs.get(indexSong).getPath());
        music.setVolume((float) volume.getValue()/100);
        setTimeEnd();
        changePercent();
    }
    public String convertTime(long time) {
        long minute = time / 60000000;
        long second = (time % 60000000) / 1000000;
        return String.format("%02d:%02d", minute, second);
    }

    public void setTimePosition() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (music != null && statePlay == true) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            timePosition.setText(convertTime(music.getMusicTimePosition()));
                            percent.setValue(music.getMusicTimePercent() * 100);
                        }
                    });
                } else {
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }

    public void setTimeEnd() {
        timeEnd.setText(convertTime(music.getMusicTimeLength()));
    }

    public void addSong() {
        Song song = new Song("Vậy là yêu", "Đức Anh", "src/main/java/sound/sound1.wav");
        Song song1 = new Song("Rằng em mãi ở bên", "Bích Phương", "src/main/java/sound/sound2.wav");
        Song song2 = new Song("Chúng ta sau này", "T.R.I", "src/main/java/sound/sound3.wav");
        Song song4 = new Song("Say đắm trong lần đầu", "WINNO x NAMB", "src/main/java/sound/sound4.wav");
        Song song5 = new Song("Rằng em mãi ở bên", "Bích Phương", "src/main/java/sound/sound2.wav");
        Song song6 = new Song("Chúng ta sau này", "T.R.I", "src/main/java/sound/sound3.wav");

        songs.add(song);
        songs.add(song1);
        songs.add(song2);
        songs.add(song4);
        songs.add(song5);
        songs.add(song6);
    }

    public void setImageSong() {
        //set circle image
        File file = new File("src/main/java/img/vly.jpg");
        Image img = new Image(file.toURI().toString());
        Rectangle rectangle = new Rectangle();
        rectangle.setX(0);
        rectangle.setY(0);
        rectangle.setWidth(image.getFitWidth());
        rectangle.setHeight(image.getFitHeight());
        rectangle.setArcHeight(image.getFitHeight());
        rectangle.setArcWidth(image.getFitWidth());
        ImagePattern pattern = new ImagePattern(img);
        rectangle.setFill(pattern);
        image.setClip(rectangle);
        image.setImage(img);

    }

    private void changeTheme() {
        vBox.getChildren().get(indexSong).setStyle("-fx-background-color: #0D4C92; -fx-background-radius: 10px; cursor: default;");
        ((AnchorPane) vBox.getChildren().get(indexSong)).getChildren().get(0).setStyle("-fx-text-fill: white");
        ((AnchorPane) vBox.getChildren().get(indexSong)).getChildren().get(1).setStyle("-fx-text-fill: white");
        nameSong.setText(songs.get(indexSong).getName());
        singer.setText(songs.get(indexSong).getSinger());

        for (var j = 0; j < vBox.getChildren().size(); j++) {
            if (j != indexSong) {
                vBox.getChildren().get(j).setStyle("-fx-background-color: #F0F1F5; -fx-background-radius: 10px; cursor: default;");
                ((AnchorPane) vBox.getChildren().get(j)).getChildren().get(0).setStyle("-fx-text-fill: black");
                ((AnchorPane) vBox.getChildren().get(j)).getChildren().get(1).setStyle("-fx-text-fill: black");
            }
        }
    }

    public void changeSong() {
        music.stop();
        music = new WavMusic(songs.get(indexSong).getPath());
//        music.setVolume((float) volume.getValue() / 100);
        music.play();
        setRote();
    }

    public void changePercent() {
        percent.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            music.pause();
            music.setMusicTimePercent(percent.getValue() / 100);
            music.play();
        });
    }

    public void setRote() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                if (statePlay == true) {
                    image.setRotate(image.getRotate() + 1);
                    if ((image.getRotate()) == 360) {
                        image.setRotate(0);
                    }
                } else {
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 10);
    }

    public void play(MouseEvent mouseEvent) {
        if (statePlay == false) {
            playButton.setGlyphName("PAUSE");
            statePlay = true;
            if (music == null) {
                String source = songs.get(indexSong).getPath();
                music = new WavMusic(source);
            }
            if (music.isPause()) {
                music.play();
            }
            music.setVolume((float) volume.getValue() / 100);
            System.out.println("Playing");
            setRote();
            setTimePosition();
        } else {
            playButton.setGlyphName("PLAY");
            music.pause();
            statePlay = false;
            System.out.println("Pausing");
        }
    }

    public void next(MouseEvent mouseEvent) {
        image.setRotate(0);
        if (indexSong == songs.size() - 1) {
            indexSong = 0;
        } else {
            indexSong++;
        }
        if (statePlay == true) {
            music.stop();
            music = new WavMusic(songs.get(indexSong).getPath());
            music.play();
        } else {
            music.stop();
            music = null;
        }
        changeTheme();
        setTimeEnd();
        System.out.println("Next");
    }

    public void previous(MouseEvent mouseEvent) {
        image.setRotate(0);
        if (indexSong == 0) {
            indexSong = songs.size() - 1;
        } else {
            indexSong--;
        }
        if (statePlay == true) {
            music.stop();
            music = new WavMusic(songs.get(indexSong).getPath());
            music.play();
        } else {
            music.stop();
            music = null;
        }
        changeTheme();
        setTimeEnd();
        System.out.println("Previous");
    }

    public void loop(MouseEvent mouseEvent) {
        if (stateLoop == false) {
            unLoop.setVisible(false);
            stateLoop = true;
            System.out.println("Loop");
        } else {
            unLoop.setVisible(true);
            stateLoop = false;
            System.out.println("Un Loop");
        }
    }

    public void random(MouseEvent mouseEvent) {
        if (stateRandom == false) {
            unRandom.setVisible(false);
            stateRandom = true;
            System.out.println("Random");
        } else {
            unRandom.setVisible(true);
            stateRandom = false;
            System.out.println("Un Random");
        }

    }

    public void showVolume(MouseEvent mouseEvent) {
        volume.setVisible(true);
    }

    private void hideVolume() {
        volume.setVisible(false);
    }

    public void setVolume(MouseEvent mouseEvent) {
        music.setVolume((float) (volume.getValue() / 100));
        hideVolume();
    }

    public void showPopup(MouseEvent mouseEvent) {
        System.out.println("Show popup");
    }

}