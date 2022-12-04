package com.example.integradora3apo.control;

import com.example.integradora3apo.HelloApplication;
import com.example.integradora3apo.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaTray;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameController implements Initializable {


    //Variables globales de la ventana
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;
    private boolean isRunning = true;

    private boolean player1Dead = false;
    @FXML
    private ImageView liveImage;

    @FXML
    private ImageView bulletImage;

    @FXML
    private ImageView bulletImage2;

    @FXML
    private Label labelName1;

    @FXML
    private Label labelName2;
    @FXML
    private ImageView liveImage2;

    //Elementos gráficos
    private Avatar avatar;
    private AvatarTwo avatar2;
    private Avatar enemy;
    private Image fondo;

    private ArrayList<Enemy> enemies;

    private ArrayList<Avatar> avatars;
    private ArrayList<AvatarTwo> avatarTwos;
    private ArrayList<Bullet> bullets1;
    private ArrayList<Bullet> bullets2;
    private ArrayList<Bullet> bulletCPU;
    private ArrayList<Wall> walls;


    //Estados de las teclas
    boolean Wpressed = false;
    boolean Apressed = false;
    boolean Spressed = false;
    boolean Dpressed = false;

    boolean UPpressed = false;
    boolean LEFTpressed = false;
    boolean DOWNpressed = false;
    boolean RIGHTpressed = false;

    public void generateMap() {
        walls = new ArrayList<>();

        for (int i = 350; i >= 100; i += -50) {
            Wall wall1 = new Wall(350, i, canvas);
            walls.add(wall1);
        }

        //horizontal
        for (int i = 350; i < 600; i += 50) {
            Wall wall1 = new Wall(i, 100, canvas);
            walls.add(wall1);
        }

        //Pared vertical fondo arriba
        for (int i = 0; i < 250; i += 50) {
            Wall wall1 = new Wall(800, i, canvas);
            walls.add(wall1);
        }

        //Pared vertical fondo medio
        for (int i = 350; i < 400; i += 50) {
            Wall wall1 = new Wall(800, i, canvas);
            walls.add(wall1);
        }

        //Pared vertical fondo abajo
        for (int i = 550; i <= canvas.getHeight(); i += 50) {
            Wall wall1 = new Wall(750, i, canvas);
            walls.add(wall1);
        }

        //Pared vertical primera abajo
        for (int i = 550; i <= canvas.getHeight(); i += 50) {
            Wall wall1 = new Wall(150, i, canvas);
            walls.add(wall1);
        }

        //Pared horizontal fondo arriba
        for (int i = 950; i <= 1000; i += 50) {
            Wall wall1 = new Wall(i, 150, canvas);
            walls.add(wall1);
        }

        //Pared horizontal fondo abajo
        for (int i = 1000; i <= canvas.getWidth(); i += 50) {
            Wall wall1 = new Wall(i, 500, canvas);
            walls.add(wall1);
        }

        //Pared horizontal medio abajo
        for (int i = 400; i <= 450; i += 50) {
            Wall wall1 = new Wall(i, 500, canvas);
            walls.add(wall1);
        }

        //Pared vertical medio abajo
        for (int i = 450; i <= 450; i += 50) {
            Wall wall1 = new Wall(450, i, canvas);
            walls.add(wall1);
        }

        for (int i = 200; i > 50; i += -50) {
            Wall wall1 = new Wall(i, 400, canvas);
            walls.add(wall1);
        }

        //modifica primera pared vertical
        for (int i = 0; i < 100; i += 50) {
            Wall wall1 = new Wall(150, i, canvas);
            walls.add(wall1);
        }

        //Modifica primera pared vertical parte2
        for (int i = 200; i <= 250; i += 50) {
            Wall wall1 = new Wall(150, i, canvas);
            walls.add(wall1);
        }

        for (int i = 200; i <= 400; i += 50) {
            Wall wall1 = new Wall(650, i, canvas);
            walls.add(wall1);
        }

        for (int i = 650; i >= 550; i += -50) {
            Wall wall1 = new Wall(i, 350, canvas);
            walls.add(wall1);
        }

        for (int i = 700; i < 1000; i += 50) {
            Wall wall1 = new Wall(i, 350, canvas);
            walls.add(wall1);
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = canvas.getGraphicsContext2D();
        canvas.setFocusTraversable(true);
        System.out.println(AvatarData.getInstance().getNamesPlayer1());
        generateMap();
        String uri = "file:" + HelloApplication.class.getResource("topDownArenaView.png").getPath();
        enemy = new Avatar(canvas);
        labelName1.setText(AvatarData.getInstance().getNamesPlayer1());
        labelName2.setText(AvatarData.getInstance().getNamesPlayer2());
        bulletCPU = new ArrayList<>();
        fondo = new Image(uri);
        enemies = new ArrayList<>();
        avatars = new ArrayList<>();
        enemy.ammo = 0;

        avatars.add(new Avatar(canvas));

        avatarTwos = new ArrayList<>();
        avatarTwos.add(new AvatarTwo(canvas));
        bullets1 = new ArrayList<>();
        bullets2 = new ArrayList<>();
        enemy.setPosition(1000,300);



        canvas.setOnKeyPressed(this::onKeyPressed);
        canvas.setOnKeyReleased(this::onKeyReleased);
        draw();

    }

    public void draw() {
        new Thread(
                () -> {
                    while (isRunning) {
                        Platform.runLater(() -> {

                            gc.drawImage(fondo, 0, 0, canvas.getWidth(), canvas.getHeight());

                            if (avatars.size() != 0) {

                                avatars.get(0).draw();
                                showLifePlayer1();
                            } else {
                                String uri = "file:" + HelloApplication.class.getResource("dead.png").getPath();
                                Image live = new Image(uri);
                                liveImage.setImage(live);
                            }

                            if (avatarTwos.size() != 0) {
                                avatarTwos.get(0).draw();
                                showLifePlayer2();
                            } else {
                                String uri = "file:" + HelloApplication.class.getResource("dead.png").getPath();
                                Image live = new Image(uri);
                                liveImage2.setImage(live);
                            }


                            for (int i = 0; i < walls.size(); i++) {
                                if (walls.get(i).damage == 0) {
                                    walls.remove(i);
                                }
                                walls.get(i).draw();
                            }

                            //Pintar enemigos
                            if (enemy != null) {
                                if(avatars.size() != 0){
                                    double componentX = (avatars.get(0).pos.x - enemy.pos.x);
                                    double componentY = (avatars.get(0).pos.y - enemy.pos.y);
                                    double rule = Math.sqrt(Math.pow(componentX, 2) + Math.pow(componentY, 2));
                                    enemy.direction.x = componentX / rule;
                                    enemy.direction.y = componentY / rule;
                                } else if (avatarTwos.size() != 0) {
                                    double componentX = (avatarTwos.get(0).pos.x - enemy.pos.x);
                                    double componentY = (avatarTwos.get(0).pos.y - enemy.pos.y);
                                    double rule = Math.sqrt(Math.pow(componentX, 2) + Math.pow(componentY, 2));
                                    enemy.direction.x = componentX / rule;
                                    enemy.direction.y = componentY / rule;
                                }

                                shootCPU();
                            }


                            //Balas disparos
                            for (int i = 0; i < bullets1.size(); i++) {
                                bullets1.get(i).draw();
                                if (bullets1.get(i).pos.x > canvas.getWidth() + 20 ||
                                        bullets1.get(i).pos.y > canvas.getHeight() + 20 ||
                                        bullets1.get(i).pos.y < -20 ||
                                        bullets1.get(i).pos.x < -20) {
                                    bullets1.remove(i);
                                }

                            }

                            for (int i = 0; i < bullets2.size(); i++) {
                                bullets2.get(i).draw();
                                if (bullets2.get(i).pos.x > canvas.getWidth() + 20 ||
                                        bullets2.get(i).pos.y > canvas.getHeight() + 20 ||
                                        bullets2.get(i).pos.y < -20 ||
                                        bullets2.get(i).pos.x < -20) {
                                    bullets2.remove(i);
                                }

                            }


                            for (int i = 0; i < bullets1.size(); i++) {
                                for (int j = 0; j < walls.size(); j++) {
                                    if (bullets1.get(i).circle.intersects(walls.get(j).rectangle.getBoundsInParent())) {
                                        bullets1.remove(i);
                                        walls.get(j).damage--;
                                    }
                                }
                            }
                            for (int i = 0; i < bullets2.size(); i++) {
                                for (int j = 0; j < walls.size(); j++) {
                                    if (bullets2.get(i).circle.intersects(walls.get(j).rectangle.getBoundsInParent())) {
                                        bullets2.remove(i);
                                        walls.get(j).damage--;
                                    }
                                }
                            }

                            //Colisiones
                            //detectCollission();

                            if(WinOrLose() != 0){
                                HelloApplication.showWindow("gano.fxml");
                                Stage currentStage = (Stage) canvas.getScene().getWindow();
                                currentStage.hide();
                                isRunning =false;
                            }
                            detectDamage();
                            showAmmoPlayer1();
                            showAmmoPlayer2();
                            doKeyboardActions();
                            doKeyboardActions2();
                        });
                        //Sleep
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        ).start();

    }

    private int WinOrLose(){
        int count=0;
        if(avatars.size()==0 && enemy == null){
            count = 1;
            isRunning = false;
            return count;
        }else if(avatarTwos.size()==0 && enemy==null){
            count = 2;
            isRunning = false;
            return count;
        }else if(avatars.size()==0 && avatarTwos.size()==0) {
            count = 3;
            isRunning = false;
            return count;
        }


        return count;
    }

    private void detectDamage() {
        if (avatarTwos.size() != 0) {

            for (int i = 0; i < bullets1.size(); i++) {
                if (bullets1.get(i).circle.intersects(avatarTwos.get(0).rectangle.getBoundsInParent())) {
                    avatarTwos.get(0).live--;
                    if (avatarTwos.get(0).live == 0) {
                        avatarTwos.remove(0);
                    }
                    bullets1.remove(i);
                }
            }


        }

        if (enemy != null){
            for (int i = 0; i < bullets2.size(); i++) {
                if (bullets2.get(i).circle.intersects(enemy.rectangle.getBoundsInParent())) {
                    enemy.live--;
                    if (enemy.live == 0) {
                        enemy = null;

                    }
                    bullets2.remove(i);
                }
            }

            for (int i = 0; i < bullets1.size(); i++) {
                if (bullets1.get(i).circle.intersects(enemy.rectangle.getBoundsInParent())) {
                    enemy.live--;
                    if (enemy.live == 0) {
                        enemy = null;
                    }
                    bullets1.remove(i);
                }
            }
        }


        if (avatars.size() != 0) {
            for (int i = 0; i < bullets2.size(); i++) {
                if (bullets2.get(i).circle.intersects(avatars.get(0).rectangle.getBoundsInParent())) {
                    avatars.get(0).live--;
                    if (avatars.get(0).live == 0) {
                        avatars.remove(0);
                        player1Dead = true;
                    }

                    bullets2.remove(i);
                }
            }
        }

    }

    private void doKeyboardActions() {
        boolean stopFlag = false;
        if (Wpressed) {
            for (int i = 0; i < walls.size(); i++) {

                if (walls.get(i).rectangle.intersects(avatars.get(0).pos.x + avatars.get(0).direction.x - 25, avatars.get(0).pos.y + avatars.get(0).direction.y - 25, 50, 50)) {

                    stopFlag = true;

                }

            }
            if (!stopFlag) {
                avatars.get(0).moveForward();
            }

        }
        if (Apressed) {
            avatars.get(0).changeAngle(-6);
        }
        if (Spressed) {
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).rectangle.intersects(avatars.get(0).pos.x - avatars.get(0).direction.x - 30, avatars.get(0).pos.y - avatars.get(0).direction.y - 30, 50, 50)) {
                    stopFlag = true;
                }

            }
            if (!stopFlag) {
                avatars.get(0).moveBackward();
            }

        }
        if (Dpressed) {
            avatars.get(0).changeAngle(6);
        }
    }

    private void doKeyboardActions2() {
        boolean stopFlag2 = false;
        if (UPpressed) {
            for (int i = 0; i < walls.size(); i++) {

                if (walls.get(i).rectangle.intersects(avatarTwos.get(0).pos.x + avatarTwos.get(0).direction.x - 25, avatarTwos.get(0).pos.y + avatarTwos.get(0).direction.y - 25, 50, 50)) {
                    stopFlag2 = true;
                }

            }
            if (!stopFlag2) {
                avatarTwos.get(0).moveForward();
            }

        }
        if (LEFTpressed) {
            avatarTwos.get(0).changeAngle(-6);
        }
        if (DOWNpressed) {
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).rectangle.intersects(avatarTwos.get(0).pos.x - avatarTwos.get(0).direction.x - 30, avatarTwos.get(0).pos.y - avatarTwos.get(0).direction.y - 30, 50, 50)) {
                    stopFlag2 = true;
                }

            }
            if (!stopFlag2) {
                avatarTwos.get(0).moveBackward();
            }

        }
        if (RIGHTpressed) {
            avatarTwos.get(0).changeAngle(6);
        }
    }

    private void onKeyReleased(KeyEvent keyEvent) {
        //Tank 1
        if (keyEvent.getCode() == KeyCode.W) {
            Wpressed = false;
        }
        if (keyEvent.getCode() == KeyCode.A) {
            Apressed = false;
        }
        if (keyEvent.getCode() == KeyCode.S) {
            Spressed = false;
        }
        if (keyEvent.getCode() == KeyCode.D) {
            Dpressed = false;
        }

        //Tank 2
        if (keyEvent.getCode() == KeyCode.UP) {
            UPpressed = false;
        }
        if (keyEvent.getCode() == KeyCode.LEFT) {
            LEFTpressed = false;
        }
        if (keyEvent.getCode() == KeyCode.DOWN) {
            DOWNpressed = false;
        }
        if (keyEvent.getCode() == KeyCode.RIGHT) {
            RIGHTpressed = false;
        }
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        //Tank 1

        if (keyEvent.getCode() == KeyCode.W) {
            Wpressed = true;
        }
        if (keyEvent.getCode() == KeyCode.A) {
            Apressed = true;
        }
        if (keyEvent.getCode() == KeyCode.S) {
            Spressed = true;
        }
        if (keyEvent.getCode() == KeyCode.D) {
            Dpressed = true;
        }
        if (keyEvent.getCode() == KeyCode.SPACE) {
            Bullet bullet = new Bullet(canvas,
                    new Vector(avatars.get(0).pos.x, avatars.get(0).pos.y),
                    new Vector(2 * avatars.get(0).direction.x, 2 * avatars.get(0).direction.y));
            if (avatars.get(0).ammo != 0) {
                bullets1.add(bullet);
                avatars.get(0).ammo--;
            }

        }
        if (keyEvent.getCode() == KeyCode.R) {
            avatars.get(0).ammo = 5;
            soundRecharge();
        }

        //Tank 2
        System.out.println(keyEvent.getCode());
        if (keyEvent.getCode() == KeyCode.UP) {
            UPpressed = true;
        }
        if (keyEvent.getCode() == KeyCode.LEFT) {
            LEFTpressed = true;
        }
        if (keyEvent.getCode() == KeyCode.DOWN) {
            DOWNpressed = true;
        }
        if (keyEvent.getCode() == KeyCode.RIGHT) {
            RIGHTpressed = true;
        }
        if (keyEvent.getCode() == KeyCode.CONTROL) {
            Bullet bullet = new Bullet(canvas,
                    new Vector(avatarTwos.get(0).pos.x, avatarTwos.get(0).pos.y),
                    new Vector(2 * avatarTwos.get(0).direction.x, 2 * avatarTwos.get(0).direction.y));
            if (avatarTwos.get(0).ammo != 0) {
                bullets2.add(bullet);
                avatarTwos.get(0).ammo--;
            }
        }
        if (keyEvent.getCode() == KeyCode.SHIFT) {
            avatarTwos.get(0).ammo = 5;
            soundRecharge();
        }
    }

    private void showLifePlayer1() {

        if(avatars.size() != 0){
            if (avatars.get(0).live == 5) {
                String uri = "file:" + HelloApplication.class.getResource("fullLive.png").getPath();
                Image live = new Image(uri);
                liveImage.setImage(live);
            }
            if (avatars.get(0).live == 4) {
                String uri = "file:" + HelloApplication.class.getResource("fourHearts.png").getPath();
                Image live = new Image(uri);
                liveImage.setImage(live);
            }
            if (avatars.get(0).live == 3) {
                String uri = "file:" + HelloApplication.class.getResource("threeHearts.png").getPath();
                Image live = new Image(uri);
                liveImage.setImage(live);
            }
            if (avatars.get(0).live == 2) {
                String uri = "file:" + HelloApplication.class.getResource("twoHearts.png").getPath();
                Image live = new Image(uri);
                liveImage.setImage(live);
            }
            if (avatars.get(0).live == 1) {
                String uri = "file:" + HelloApplication.class.getResource("oneHeart.png").getPath();
                Image live = new Image(uri);
                liveImage.setImage(live);
            }
        }

    }

    private void showLifePlayer2() {

        if(avatarTwos.size() != 0){
            if (avatarTwos.get(0).live == 5) {
                String uri = "file:" + HelloApplication.class.getResource("fullLive.png").getPath();
                Image live = new Image(uri);
                liveImage2.setImage(live);
            }
            if (avatarTwos.get(0).live == 4) {
                String uri = "file:" + HelloApplication.class.getResource("fourHearts.png").getPath();
                Image live = new Image(uri);
                liveImage2.setImage(live);
            }
            if (avatarTwos.get(0).live == 3) {
                String uri = "file:" + HelloApplication.class.getResource("threeHearts.png").getPath();
                Image live = new Image(uri);
                liveImage2.setImage(live);
            }
            if (avatarTwos.get(0).live == 2) {
                String uri = "file:" + HelloApplication.class.getResource("twoHearts.png").getPath();
                Image live = new Image(uri);
                liveImage2.setImage(live);
            }
            if (avatarTwos.get(0).live == 1) {
                String uri = "file:" + HelloApplication.class.getResource("oneHeart.png").getPath();
                Image live = new Image(uri);
                liveImage2.setImage(live);
            }
        }

    }

    private void showAmmoPlayer1() {

        if(avatars.size() != 0){
            if (avatars.get(0).ammo == 5) {
                String uri = "file:" + HelloApplication.class.getResource("ammo.png").getPath();
                Image live = new Image(uri);
                bulletImage.setImage(live);
            }
            if (avatars.get(0).ammo == 4) {
                String uri = "file:" + HelloApplication.class.getResource("fourBullets.png").getPath();
                Image live = new Image(uri);
                bulletImage.setImage(live);
            }
            if (avatars.get(0).ammo == 3) {
                String uri = "file:" + HelloApplication.class.getResource("threeBullets.png").getPath();
                Image live = new Image(uri);
                bulletImage.setImage(live);
            }
            if (avatars.get(0).ammo == 2) {
                String uri = "file:" + HelloApplication.class.getResource("twoBullets.png").getPath();
                Image live = new Image(uri);
                bulletImage.setImage(live);
            }
            if (avatars.get(0).ammo == 1) {
                String uri = "file:" + HelloApplication.class.getResource("oneBullet.png").getPath();
                Image live = new Image(uri);
                bulletImage.setImage(live);
            }

            if(avatars.get(0).ammo == 0){
                String uri = "file:" + HelloApplication.class.getResource("reloadIcon.png").getPath();
                Image live = new Image(uri);
                bulletImage.setImage(live);
            }
        }

    }

    private void showAmmoPlayer2() {

        if(avatarTwos.size() != 0){
            if (avatarTwos.get(0).ammo == 5) {
                String uri = "file:" + HelloApplication.class.getResource("ammo.png").getPath();
                Image live = new Image(uri);
                bulletImage2.setImage(live);
            }
            if (avatarTwos.get(0).ammo == 4) {
                String uri = "file:" + HelloApplication.class.getResource("fourBullets.png").getPath();
                Image live = new Image(uri);
                bulletImage2.setImage(live);
            }
            if (avatarTwos.get(0).ammo == 3) {
                String uri = "file:" + HelloApplication.class.getResource("threeBullets.png").getPath();
                Image live = new Image(uri);
                bulletImage2.setImage(live);
            }
            if (avatarTwos.get(0).ammo == 2) {
                String uri = "file:" + HelloApplication.class.getResource("twoBullets.png").getPath();
                Image live = new Image(uri);
                bulletImage2.setImage(live);
            }
            if (avatarTwos.get(0).ammo == 1) {
                String uri = "file:" + HelloApplication.class.getResource("oneBullet.png").getPath();
                Image live = new Image(uri);
                bulletImage2.setImage(live);
            }

            if(avatarTwos.get(0).ammo == 0){
                String uri = "file:" + HelloApplication.class.getResource("reloadIcon.png").getPath();
                Image live = new Image(uri);
                bulletImage2.setImage(live);
            }
        }


    }

    private void soundRecharge(){
        String uri = HelloApplication.class.getResource("maxAmmo.wav").getPath();
        File musicPath = new File(uri);

        if(musicPath.exists()){

            try {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();

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

    private void shootCPU() {

        new Thread(() -> {
            if (enemy != null){
                enemy.draw();
            }

            boolean stopflag = false;
            boolean stopBullets = false;
            for (int i = 0; i < walls.size() ; i++) {
                if (walls.get(i).rectangle.intersects(enemy.rectangle.getBoundsInParent())) {

                    stopflag = true;
                    Bullet bullet = new Bullet(canvas,
                            new Vector(enemy.pos.x, enemy.pos.y),
                            new Vector(5 * enemy.direction.x, 5 * enemy.direction.y));
                    if(enemy.ammo == 0){
                        bulletCPU.add(bullet);
                        enemy.ammo = 1;

                    }

                }
            }

            for (int i = 0; i < bulletCPU.size(); i++) {
                bulletCPU.get(0).draw();
                for (int j = 0; j < walls.size() ; j++) {
                    if(bulletCPU.get(i).circle.intersects(walls.get(j).rectangle.getBoundsInParent())){
                        walls.get(j).damage--;
                        bulletCPU.remove(i);
                        enemy.ammo = 0;
                    }
                }
            }
            for (int i = 0; i < bulletCPU.size(); i++) {
                if (bulletCPU.get(i).pos.x > canvas.getWidth() + 20 ||
                        bulletCPU.get(i).pos.y > canvas.getHeight() + 20 ||
                        bulletCPU.get(i).pos.y < -20 ||
                        bulletCPU.get(i).pos.x < -20) {
                    bulletCPU.remove(i);
                    enemy.ammo = 0;
                }

            }

            for (int i = 0; i < bulletCPU.size(); i++) {
                if(avatars.size() != 0){
                   if(bulletCPU.get(i).circle.intersects(avatars.get(0).rectangle.getBoundsInParent())){
                       bulletCPU.remove(i);
                       enemy.ammo = 0;
                       avatars.get(0).live--;
                       if(avatars.get(0).live == 0){
                           avatars.remove(0);
                       }
                   }
                }
                if(avatarTwos.size() != 0){
                    if(bulletCPU.get(i).circle.intersects(avatarTwos.get(0).rectangle.getBoundsInParent())){
                        bulletCPU.remove(i);
                        enemy.ammo = 0;
                        avatarTwos.get(0).live--;
                        if(avatarTwos.get(0).live == 0){
                            avatarTwos.remove(0);
                        }
                    }
                }


            }



            if (!stopflag) {
                enemy.moveForward();
                stopBullets = false;
            }



            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }).start();

    }
}








