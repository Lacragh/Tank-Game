package com.example.integradora3apo.model;

import com.example.integradora3apo.HelloApplication;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class AvatarTwo {

    private Canvas canvas;
    private GraphicsContext gc;
    private Image tank;
    public Vector pos;
    public Vector direction;
    public Rectangle rectangle;

    public int ammo = 5;
    public int live = 5;
    public AvatarTwo(Canvas canvas){
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        String uri = "file:"+ HelloApplication.class.getResource("tanqueJugador2.png").getPath();
        tank = new Image(uri);
        pos = new Vector(100,100);
        direction = new Vector(2,2);
    }

    public void draw(){
        gc.save();
        gc.translate(pos.x, pos.y);
        gc.rotate(90+direction.getAngle());
        gc.drawImage(tank, -25,-25, 50,50);
        rectangle = new Rectangle(pos.x-25,pos.y-25,40,40);
        gc.restore();
    }
    public void setPosition(double x, double y) {
        pos.x = (int) x - 25;
        pos.y = (int) y - 25;
    }

    public void changeAngle(double a){
        double amp = direction.getAmplitude();
        double angle = direction.getAngle();
        angle += a;
        direction.x = amp*Math.cos(Math.toRadians(angle));
        direction.y = amp*Math.sin(Math.toRadians(angle));
    }

    public void moveForward(){
        if (pos.x >= canvas.getWidth() - 25) {
            pos.x = canvas.getWidth() - 25;
        }

        if (pos.x <= 25) {
            pos.x = 25;
        }

        if(pos.y >= canvas.getHeight() - 25){
            pos.y = canvas.getHeight() - 25;
        }

        if(pos.y <= 25){
            pos.y = 25;
        }

        pos.x += direction.x;
        pos.y += direction.y;
    }

    public void moveBackward() {
        if (pos.x >= canvas.getWidth() - 25) {
            pos.x = canvas.getWidth() - 25;
        }

        if (pos.x <= 25) {
            pos.x = 25;
        }

        if(pos.y >= canvas.getHeight() - 25){
            pos.y = canvas.getHeight() - 25;
        }

        if(pos.y <= 25){
            pos.y = 25;
        }

        pos.x -= direction.x;
        pos.y -= direction.y;
    }

}
