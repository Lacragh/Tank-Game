package com.example.integradora3apo.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Enemy {


    private Canvas canvas;
    private GraphicsContext gc;
    public int x,y;

    public Enemy(Canvas canvas, int x, int y){
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        this.x = x;
        this.y = y;
    }

    public void draw(){
        gc.setFill(Color.BLUE);
        gc.fillRect(x-12.5,y-12.5,25,25);
    }

}
