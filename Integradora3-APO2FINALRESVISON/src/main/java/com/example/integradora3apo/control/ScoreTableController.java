package com.example.integradora3apo.control;

import com.example.integradora3apo.model.AvatarData;
import com.example.integradora3apo.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ScoreTableController implements Initializable {

    private AvatarData avatar;
    @FXML
    private TableColumn<Player, Double> columScore;

    @FXML
    private TableColumn<Player, String> columnUser;

    @FXML
    private TableView<Player> scoreTable;

    public void readScore() {

        try {
            File file = new File("Score.txt");
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int readBytes = 0;
            byte[] buffer = new byte[128];
            while ((readBytes = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, readBytes);
            }
            fis.close();
            baos.close();

            String text = baos.toString(StandardCharsets.UTF_8);
            System.out.println(text);
            //JSON


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        columScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        columnUser.setCellValueFactory(new PropertyValueFactory<>("name"));
        scoreTable.setItems(AvatarData.getInstance().getPlayers());


    }


    @FXML
    private Button returnButton;

    @FXML
    void open(ActionEvent event) {

    }
}
