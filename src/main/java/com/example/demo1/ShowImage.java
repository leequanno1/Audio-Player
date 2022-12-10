package com.example.demo1;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ShowImage implements Initializable {
    @FXML
    private ImageView image;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("@../../../../java/img/vly.jpg");
        Image img = new Image(file.toURI().toString());
        image.setImage(img);
    }
}

