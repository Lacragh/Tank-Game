package com.example.integradora3apo.model;

import com.example.integradora3apo.HelloApplication;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall {
    public int x,y;
    private Canvas canvas;
    private Image wall;
    public int damage = 5;
    private GraphicsContext gc;
    public Rectangle rectangle;

    public Wall(int x, int y, Canvas canvas) {
        this.x = x;
        this.y = y;
        gc = canvas.getGraphicsContext2D();
        String uri = "file:"+ HelloApplication.class.getResource("Ladrillo.jpg").getPath();
        wall = new Image(uri);
        this.canvas = canvas;
    }

    public void draw(){
        gc.setFill(Color.GOLD);
        gc.fillRect(x,y,50,50);
        gc.drawImage(wall,x,y,50,50);
        rectangle = new Rectangle(x,y,50,50);

    }
}
