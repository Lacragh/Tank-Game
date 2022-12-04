package com.example.integradora3apo.control;

import com.example.integradora3apo.HelloApplication;
import com.example.integradora3apo.model.AvatarData;
import com.example.integradora3apo.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;


public class HelloController implements Initializable {

    @FXML
    private Button startGame;

    private int entry = 0;
    @FXML
    private Button openTable;
    private Clip clip;

    @FXML
    public TextField textUsuario1;

    @FXML
    public TextField textUsuario2;

    @FXML
    void startGame(ActionEvent event) {


        if(textUsuario1.getText().equals("") || textUsuario2.getText().equals("")){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please fill up the username spaces");
            alert.showAndWait();

        }else{

            AvatarData.getInstance().setNamesPlayer1(textUsuario1.getText());
            AvatarData.getInstance().setNamesPlayer2(textUsuario2.getText());
            Player player1 = new Player(0,textUsuario1.getText());
            AvatarData.getInstance().getPlayers().add(player1);
            entry++;
            HelloApplication.showWindow("canvasView.fxml");
            Stage currentStage = (Stage) startGame.getScene().getWindow();
            currentStage.hide();
            clip.close();
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String uri = HelloApplication.class.getResource("pou.wav").getPath();
        System.out.println(uri);
        File musicPath = new File(uri);

        if(musicPath.exists()){

            try {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath); clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(3);

            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        }else {
            System.out.println("No existe");
        }

    }

    @FXML
    void openTable(ActionEvent event) {

        if(entry == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Aun no hay puntajes añadidos!");
            alert.showAndWait();
        }else{

            HelloApplication.showWindow("Table.fxml");
            Stage currentStage = (Stage) startGame.getScene().getWindow();
            currentStage.hide();

        }

    }

}


