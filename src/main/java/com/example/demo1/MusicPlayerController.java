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
import javafx.stage.DirectoryChooser;

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

    private float volumeValue = 0.3f;

    /*Hàm khởi tạo giá trị ban đầu.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setInit();
        int index = 0;
        if (songs.size() != 0) {
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
    }

    public void setInit() {
        setImageSong();
        if (songs.size() == 0) {
            Label label = new Label("Vui lòng ấn thêm bài hát");
            label.setFont(new Font("Arial", 30));
            label.setLayoutX(100);
            label.setLayoutY(200);
            list.setContent(label);
            nameSong.setText("");
            singer.setText("");
            timeEnd.setText("00:00");
            timePosition.setText("00:00");
        } else {
            createMusic();
        }
    }

    /*Thời gian trong chương trình được trả về số giây.
    Hàm này trả về chuổi thời gian theo định dạng "Phút : Giây"*/
    public String convertTime(long time) {
        String second = String.valueOf(time % 60);
        if (second.length() == 1) {
            second = "0" + second;
        }
        //convert time to string minute
        String minute = String.valueOf(time / 60);
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        return minute + ":" + second;
    }

    /*Hàm này điều chỉnh thời gian audio theo giá trị hiện tại của thanh trượt thời gian*/
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
                            if (percent.getValue() >= 99.5) {
                                if (stateRandom == true) {
                                    indexSong = (int) (Math.random() * songs.size());
                                }
                                else {
                                    if (indexSong >= songs.size() - 1) {
                                        indexSong = -1;
                                        if (stateLoop == false)
                                        {
                                            stop();
                                        }
                                    }
                                }
                                next(null);
                            }
                            setTimeEnd();
                        }
                    });
                } else {
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }

    /*Hàm đặt lại thời gian hiện tại đang hiển thị về thời gian khởi đầu.*/
    private void setInitTimePositon() {
        timePosition.setText("00:00");
    }

    /*Hàm đặt lại thời gian kết thúc đang hiển thị.*/
    public void setTimeEnd() {
        timeEnd.setText(convertTime(music.getMusicTimeLength()));
    }

    /*Hàm thực hiện thay đổi giá trị volume theo giá trị của thanh trượt volume*/
    public void setVolume() {
        music.setVolume(volumeValue);
    }

    /*Hàm thực chọn thư mục chứa bài hát và lấy thông tin bài hát.*/
    public void addSong() {
        if (statePlay == true) {
            music.stop();
            playButton.setGlyphName("PLAY");
            statePlay = false;
        }
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory == null) {

        } else {
            File folder = new File(selectedDirectory.getAbsolutePath());
            File[] listOfFiles = folder.listFiles();
            try {
                vBox.getChildren().clear();
                songs.clear();
                for (File file : listOfFiles) {
                    if (file.isFile() && (file.getName().endsWith(".wav") || file.getName().endsWith(".mp3"))) {
                        String name = file.getName().substring(0, file.getName().length() - 4);
                        String path = file.getPath();
                        songs.add(new Song(name, "Unknown", path));
                    }
                }
            } catch (Exception e) {
                System.out.println("Không có bài hát nào");
            }
            System.out.println(songs.size());
            initialize(null, null);
        }
    }


    public void setImageSong() {
        File file = new File("src/main/java/img/test.jpg");
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

    /*Khởi tạo bài hát trong album theo vị trí của indexSong.*/
    private void createMusic() {
        music = new Music(songs.get(indexSong).getPath());
        setVolume();
        setTimePosition();
        setTimeEnd();
    }


    public void changeSong() {
        music.stop();
        createMusic();
        music.play();
    }

    /*Thay đổi thời gian phát hiện tại của audio theo giá trị của của thanh trượt thời gian.
    * Thanh trượt thời gian nhận giá trị trong khoảng (0,100).*/
    public void setPercent(MouseEvent mouseEvent) {
        if (songs.size() > 0) {
            if (statePlay == true) {
                music.pause();
                music.setMusicTimePercent(percent.getValue() / 100);
                music.play();
            } else {
                music.setMusicTimePercent(percent.getValue() / 100);
            }
            music.setMusicTimePercent(percent.getValue() / 100);
            timePosition.setText(convertTime(music.getMusicTimePosition()));
        }
    }

    /*Đặt lại thanh trượt thời gian về vị trí bắt đầu.*/
    private void setInitPercent() {
        percent.setValue(0);
    }

    /*Thực hiện xoay hình ảnh "đĩa nhạc".*/
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

    /*Thực hiện phát nhạc.

    * Nếu audio chưa được tạo thì thực hiện tạo và phát.
    * Nhạc khi vừa được tạo thì isPause = true.

    * Nếu audio đang dừng (isPause) thì thực hiện phát.*/
    private void play() {
        playButton.setGlyphName("PAUSE");
        statePlay = true;
        if (music == null) {
            createMusic();
        }
        if (music.isPause()) {
            music.play();
        }
        System.out.println("Playing");
        setRote();
        setTimePosition();
    }

    /*Thực hiện dừng phát nhạc.*/
    private void pause() {
        playButton.setGlyphName("PLAY");
        music.pause();
        statePlay = false;
        System.out.println("Pausing");
    }

    /*Thực hiện dừng phát nhạc khi danh sách nhạc đã đến cuối.*/
    private void stop() {
        music.stop();
        timePosition.setText("00:00");
        playButton.setGlyphName("PLAY");
        statePlay = false;
        System.out.println("End list");
    }

    /*Bắt sự kiện chuột của playButton.

    * Nếu bài hát đang dừng thì phát nhạc và ngược lại.*/
    public void play(MouseEvent mouseEvent) {
        if (songs.size() > 0) {
            if (statePlay == false) {
                play();
            } else {
                pause();
            }
        }
    }

    /*Bắt sự kiện chuột. Thực hiện chuyển sang bài hát tiếp theo.

    * Nếu bài hát hiện tại đang nằm cuối danh sách thì chuyển đến bài hát đầu danh sách,
    * ngược lại thì chuyển đến bài hát tiếp theo.
    *
    * Nếu bài hát trước đang được phát thì sau khi thực hiện chuyển thì phát bài hát mới,
    * ngược lại thì chỉ chuyển.*/
    public void next(MouseEvent mouseEvent) {
        if (songs.size() > 0) {
            if (indexSong < songs.size() - 1) {
                indexSong++;
            } else {
                indexSong = 0;
            }
            if (statePlay) {
                music.stop();
                createMusic();
                music.play();
                changeTheme();
            } else {
                changeSong();
                changeTheme();
                music.stop();
                setInitTimePositon();
                setInitPercent();
                setTimeEnd();
            }
        }
    }

    /*Bắt sự kiện chuột.
    * Cơ chế tương tự next nhưng chuyển về bài hát trước đó,
    * nếu bài hát hiện tại đang ở đầu album thì chuyển đến bài hát ở cuối album.*/
    public void previous(MouseEvent mouseEvent) {
        if (songs.size() > 0) {
            image.setRotate(0);
            if (indexSong == 0) {
                indexSong = songs.size() - 1;
            } else {
                indexSong--;
            }
            System.out.println("Previous");
            if (statePlay) {
                music.stop();
                createMusic();
                music.play();
                changeTheme();
            } else {
                changeSong();
                changeTheme();
                music.stop();
                setInitTimePositon();
                setInitPercent();
                setTimeEnd();
            }
        }
    }

    /*Bắt sự kiện chuột.
    * Đặt lại trạng thái lặp (stateLoop)*/
    public void loop(MouseEvent mouseEvent) {
        if (songs.size() > 0) {
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
    }

    /*Bắt sự kiện chuột.
     * Đặt lại trạng thái ngẩu nhiên (stateRandom)*/
    public void random(MouseEvent mouseEvent) {
        if (songs.size() > 0) {
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
    }

    /*Bắt sự kiện chuột.
    * Thực hiện hiển thị thanh trượt.*/
    public void showVolume(MouseEvent mouseEvent) {
        volume.setVisible(true);
    }

    /*Thực hiện ẩn đi thanh trượt volume.*/
    private void hideVolume() {
        volume.setVisible(false);
    }

    /*Bắt sự kiện chuột thanh trượt volume.
    * Thực hiện điều chỉnh cường độ âm thanh theo giá trị của thanh trượt volume.
    * Thanh trượt volume trả về giá trị trong (0,100).*/
    public void setVolume(MouseEvent mouseEvent) {
        if (songs.size() > 0) {
            volumeValue = (float) (volume.getValue() / 100);
            setVolume();
            hideVolume();
        }
    }

    /*Bắt sự kiện chuột.
    * thực hiện hàm addSong.*/
    public void showPopup(MouseEvent mouseEvent) {
        addSong();
    }

}