package com.example.integradora3apo.model;

import com.example.integradora3apo.HelloApplication;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Bullet {

    private Canvas canvas;
    private GraphicsContext gc;
    public Vector pos;
    public Vector direction;

    public Circle circle;

    public Image bullet;

    public Bullet(Canvas canvas, Vector pos, Vector dir){
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.pos = pos;
        this.direction = dir;
        String uri = "file:"+ HelloApplication.class.getResource("kame.png").getPath();
        bullet = new Image(uri);
    }
    public void draw(){

        gc.drawImage(bullet, pos.x-5, pos.y-5, 40,40);
        circle = new Circle(pos.x-5,pos.y-5,10);
        pos.x += direction.x;
        pos.y += direction.y;


    }

}
