package com.example.integradora3apo.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AvatarData {

    private String namesPlayer1;
    private String namesPlayer2;


    private int score1 = 0;

    private int score2 = 0;

    private ObservableList<Player> players;

    public ObservableList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ObservableList<Player> players) {
        this.players = players;
    }

    public String getNamesPlayer1() {
        return namesPlayer1;
    }

    public void setNamesPlayer1(String namesPlayer1) {
        this.namesPlayer1 = namesPlayer1;
    }

    public String getNamesPlayer2() {
        return namesPlayer2;
    }

    public void setNamesPlayer2(String namesPlayer2) {
        this.namesPlayer2 = namesPlayer2;
    }

    private static AvatarData instance;

    public static AvatarData getInstance() {
        if (instance == null) {
            instance = new AvatarData();
        }
        return instance;
    }

    private AvatarData() {
        players = FXCollections.observableArrayList();
        namesPlayer1 = " ";
        namesPlayer2 = " ";


    }
}
