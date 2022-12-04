package com.example.integradora3apo.control;

import com.example.integradora3apo.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class winnerController {

    @FXML
    private Button backBT;


    @FXML
    void back(ActionEvent event) {
        HelloApplication.showWindow("logInGame.fxml");
        Stage currentStage = (Stage) backBT.getScene().getWindow();
        currentStage.hide();
    }

}


