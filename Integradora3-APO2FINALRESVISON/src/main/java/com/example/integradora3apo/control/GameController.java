package com.example.integradora3apo.control;

import com.example.integradora3apo.HelloApplication;
import com.example.integradora3apo.model.*;
import javafx.animation.AnimationTimer;
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
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML private Canvas canvas;
    private GraphicsContext gc;
    private boolean isRunning = true;
    private AnimationTimer gameLoop;

    private boolean player1Dead = false;
    @FXML private ImageView liveImage;
    @FXML private ImageView bulletImage;
    @FXML private ImageView bulletImage2;
    @FXML private Label labelName1;
    @FXML private Label labelName2;
    @FXML private ImageView liveImage2;

    private Avatar enemy;
    private Image fondo;

    private ArrayList<Avatar> avatars;
    private ArrayList<Avatar> avatarTwos;
    private ArrayList<Bullet> bullets1;
    private ArrayList<Bullet> bullets2;
    private ArrayList<Bullet> bulletCPU;
    private ArrayList<Wall> walls;

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
        // Using original logic for generating map to keep the same level design
        for (int i = 350; i >= 100; i += -50) walls.add(new Wall(350, i, canvas));
        for (int i = 350; i < 600; i += 50) walls.add(new Wall(i, 100, canvas));
        for (int i = 0; i < 250; i += 50) walls.add(new Wall(800, i, canvas));
        for (int i = 350; i < 400; i += 50) walls.add(new Wall(800, i, canvas));
        for (int i = 550; i <= canvas.getHeight(); i += 50) walls.add(new Wall(750, i, canvas));
        for (int i = 550; i <= canvas.getHeight(); i += 50) walls.add(new Wall(150, i, canvas));
        for (int i = 950; i <= 1000; i += 50) walls.add(new Wall(i, 150, canvas));
        for (int i = 1000; i <= canvas.getWidth(); i += 50) walls.add(new Wall(i, 500, canvas));
        for (int i = 400; i <= 450; i += 50) walls.add(new Wall(i, 500, canvas));
        for (int i = 450; i <= 450; i += 50) walls.add(new Wall(450, i, canvas));
        for (int i = 200; i > 50; i += -50) walls.add(new Wall(i, 400, canvas));
        for (int i = 0; i < 100; i += 50) walls.add(new Wall(150, i, canvas));
        for (int i = 200; i <= 250; i += 50) walls.add(new Wall(150, i, canvas));
        for (int i = 200; i <= 400; i += 50) walls.add(new Wall(650, i, canvas));
        for (int i = 650; i >= 550; i += -50) walls.add(new Wall(i, 350, canvas));
        for (int i = 700; i < 1000; i += 50) walls.add(new Wall(i, 350, canvas));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gc = canvas.getGraphicsContext2D();
        canvas.setFocusTraversable(true);
        generateMap();
        
        String uri = "file:" + HelloApplication.class.getResource("topDownArenaView.png").getPath();
        fondo = new Image(uri);

        labelName1.setText(AvatarData.getInstance().getNamesPlayer1());
        labelName2.setText(AvatarData.getInstance().getNamesPlayer2());

        avatars = new ArrayList<>();
        avatars.add(new Avatar(canvas, "tanqueJugador1.png", 100, 100));

        avatarTwos = new ArrayList<>();
        avatarTwos.add(new Avatar(canvas, "tanqueJugador2.png", 900, 500));

        enemy = new Avatar(canvas, "tanqueJugador1.png", 1000, 300);
        enemy.ammo = 0;

        bullets1 = new ArrayList<>();
        bullets2 = new ArrayList<>();
        bulletCPU = new ArrayList<>();

        canvas.setOnKeyPressed(this::onKeyPressed);
        canvas.setOnKeyReleased(this::onKeyReleased);
        draw();
    }

    public void draw() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isRunning) {
                    this.stop();
                    return;
                }
                
                gc.drawImage(fondo, 0, 0, canvas.getWidth(), canvas.getHeight());

                if (!avatars.isEmpty()) {
                    avatars.get(0).draw();
                    showLife(avatars.get(0), liveImage);
                } else {
                    setDeadImage(liveImage);
                }

                if (!avatarTwos.isEmpty()) {
                    avatarTwos.get(0).draw();
                    showLife(avatarTwos.get(0), liveImage2);
                } else {
                    setDeadImage(liveImage2);
                }

                for (int i = walls.size() - 1; i >= 0; i--) {
                    if (walls.get(i).damage == 0) {
                        walls.remove(i);
                    } else {
                        walls.get(i).draw();
                    }
                }

                if (enemy != null) {
                    Avatar target = null;
                    if (!avatars.isEmpty()) target = avatars.get(0);
                    else if (!avatarTwos.isEmpty()) target = avatarTwos.get(0);

                    if (target != null) {
                        double compX = target.pos.x - enemy.pos.x;
                        double compY = target.pos.y - enemy.pos.y;
                        double dist = Math.sqrt(compX * compX + compY * compY);
                        if (dist > 0) {
                            enemy.direction.x = compX / dist;
                            enemy.direction.y = compY / dist;
                        }
                    }
                    shootCPU();
                }

                updateBullets(bullets1);
                updateBullets(bullets2);
                
                checkBulletWallCollisions(bullets1);
                checkBulletWallCollisions(bullets2);

                detectDamage();
                
                if (!avatars.isEmpty()) showAmmo(avatars.get(0), bulletImage);
                if (!avatarTwos.isEmpty()) showAmmo(avatarTwos.get(0), bulletImage2);

                if (!avatars.isEmpty()) doKeyboardActions();
                if (!avatarTwos.isEmpty()) doKeyboardActions2();

                if (WinOrLose() != 0) {
                    gameLoop.stop();
                    isRunning = false;
                    HelloApplication.showWindow("gano.fxml");
                    Stage currentStage = (Stage) canvas.getScene().getWindow();
                    currentStage.hide();
                }
            }
        };
        gameLoop.start();
    }
    
    private void setDeadImage(ImageView imgView) {
        String uri = "file:" + HelloApplication.class.getResource("dead.png").getPath();
        imgView.setImage(new Image(uri));
    }

    private void updateBullets(ArrayList<Bullet> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            Bullet b = list.get(i);
            b.draw();
            if (b.pos.x > canvas.getWidth() + 20 || b.pos.y > canvas.getHeight() + 20 ||
                b.pos.x < -20 || b.pos.y < -20) {
                list.remove(i);
            }
        }
    }

    private void checkBulletWallCollisions(ArrayList<Bullet> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            boolean hit = false;
            for (int j = walls.size() - 1; j >= 0; j--) {
                if (list.get(i).circle.intersects(walls.get(j).rectangle.getBoundsInParent())) {
                    walls.get(j).damage--;
                    hit = true;
                    break;
                }
            }
            if (hit) list.remove(i);
        }
    }

    private int WinOrLose() {
        if (avatars.isEmpty() && enemy == null) return 1;
        if (avatarTwos.isEmpty() && enemy == null) return 2;
        if (avatars.isEmpty() && avatarTwos.isEmpty()) return 3;
        return 0;
    }

    private void detectDamage() {
        if (!avatarTwos.isEmpty()) {
            for (int i = bullets1.size() - 1; i >= 0; i--) {
                if (bullets1.get(i).circle.intersects(avatarTwos.get(0).rectangle.getBoundsInParent())) {
                    avatarTwos.get(0).live--;
                    if (avatarTwos.get(0).live <= 0) avatarTwos.remove(0);
                    bullets1.remove(i);
                }
            }
        }

        if (enemy != null) {
            checkEnemyHit(bullets1);
            checkEnemyHit(bullets2);
        }

        if (!avatars.isEmpty()) {
            for (int i = bullets2.size() - 1; i >= 0; i--) {
                if (bullets2.get(i).circle.intersects(avatars.get(0).rectangle.getBoundsInParent())) {
                    avatars.get(0).live--;
                    if (avatars.get(0).live <= 0) {
                        avatars.remove(0);
                        player1Dead = true;
                    }
                    bullets2.remove(i);
                }
            }
        }
    }
    
    private void checkEnemyHit(ArrayList<Bullet> list) {
        if (enemy == null) return;
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).circle.intersects(enemy.rectangle.getBoundsInParent())) {
                enemy.live--;
                if (enemy.live <= 0) enemy = null;
                list.remove(i);
                break;
            }
        }
    }

    private void doKeyboardActions() {
        Avatar player = avatars.get(0);
        boolean stopFlag = false;
        if (Wpressed) {
            for (Wall wall : walls) {
                if (wall.rectangle.intersects(player.pos.x + player.direction.x - 25, player.pos.y + player.direction.y - 25, 50, 50)) {
                    stopFlag = true;
                }
            }
            if (!stopFlag) player.moveForward();
        }
        if (Apressed) player.changeAngle(-6);
        if (Spressed) {
            for (Wall wall : walls) {
                if (wall.rectangle.intersects(player.pos.x - player.direction.x - 30, player.pos.y - player.direction.y - 30, 50, 50)) {
                    stopFlag = true;
                }
            }
            if (!stopFlag) player.moveBackward();
        }
        if (Dpressed) player.changeAngle(6);
    }

    private void doKeyboardActions2() {
        Avatar player = avatarTwos.get(0);
        boolean stopFlag = false;
        if (UPpressed) {
            for (Wall wall : walls) {
                if (wall.rectangle.intersects(player.pos.x + player.direction.x - 25, player.pos.y + player.direction.y - 25, 50, 50)) {
                    stopFlag = true;
                }
            }
            if (!stopFlag) player.moveForward();
        }
        if (LEFTpressed) player.changeAngle(-6);
        if (DOWNpressed) {
            for (Wall wall : walls) {
                if (wall.rectangle.intersects(player.pos.x - player.direction.x - 30, player.pos.y - player.direction.y - 30, 50, 50)) {
                    stopFlag = true;
                }
            }
            if (!stopFlag) player.moveBackward();
        }
        if (RIGHTpressed) player.changeAngle(6);
    }

    private void onKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.W) Wpressed = false;
        if (keyEvent.getCode() == KeyCode.A) Apressed = false;
        if (keyEvent.getCode() == KeyCode.S) Spressed = false;
        if (keyEvent.getCode() == KeyCode.D) Dpressed = false;

        if (keyEvent.getCode() == KeyCode.UP) UPpressed = false;
        if (keyEvent.getCode() == KeyCode.LEFT) LEFTpressed = false;
        if (keyEvent.getCode() == KeyCode.DOWN) DOWNpressed = false;
        if (keyEvent.getCode() == KeyCode.RIGHT) RIGHTpressed = false;
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.W) Wpressed = true;
        if (keyEvent.getCode() == KeyCode.A) Apressed = true;
        if (keyEvent.getCode() == KeyCode.S) Spressed = true;
        if (keyEvent.getCode() == KeyCode.D) Dpressed = true;
        
        if (keyEvent.getCode() == KeyCode.SPACE && !avatars.isEmpty()) {
            Avatar player = avatars.get(0);
            if (player.ammo > 0) {
                bullets1.add(new Bullet(canvas, new Vector(player.pos.x, player.pos.y), new Vector(2 * player.direction.x, 2 * player.direction.y)));
                player.ammo--;
            }
        }
        if (keyEvent.getCode() == KeyCode.R && !avatars.isEmpty()) {
            avatars.get(0).ammo = 5;
            soundRecharge();
        }

        if (keyEvent.getCode() == KeyCode.UP) UPpressed = true;
        if (keyEvent.getCode() == KeyCode.LEFT) LEFTpressed = true;
        if (keyEvent.getCode() == KeyCode.DOWN) DOWNpressed = true;
        if (keyEvent.getCode() == KeyCode.RIGHT) RIGHTpressed = true;
        
        if (keyEvent.getCode() == KeyCode.CONTROL && !avatarTwos.isEmpty()) {
            Avatar player = avatarTwos.get(0);
            if (player.ammo > 0) {
                bullets2.add(new Bullet(canvas, new Vector(player.pos.x, player.pos.y), new Vector(2 * player.direction.x, 2 * player.direction.y)));
                player.ammo--;
            }
        }
        if (keyEvent.getCode() == KeyCode.SHIFT && !avatarTwos.isEmpty()) {
            avatarTwos.get(0).ammo = 5;
            soundRecharge();
        }
    }

    private void showLife(Avatar player, ImageView imgView) {
        String filename = "fullLive.png";
        if (player.live == 4) filename = "fourHearts.png";
        else if (player.live == 3) filename = "threeHearts.png";
        else if (player.live == 2) filename = "twoHearts.png";
        else if (player.live == 1) filename = "oneHeart.png";
        else if (player.live <= 0) filename = "dead.png";
        
        String uri = "file:" + HelloApplication.class.getResource(filename).getPath();
        imgView.setImage(new Image(uri));
    }

    private void showAmmo(Avatar player, ImageView imgView) {
        String filename = "ammo.png";
        if (player.ammo == 4) filename = "fourBullets.png";
        else if (player.ammo == 3) filename = "threeBullets.png";
        else if (player.ammo == 2) filename = "twoBullets.png";
        else if (player.ammo == 1) filename = "oneBullet.png";
        else if (player.ammo <= 0) filename = "reloadIcon.png";
        
        String uri = "file:" + HelloApplication.class.getResource(filename).getPath();
        imgView.setImage(new Image(uri));
    }

    private void soundRecharge() {
        URL resource = HelloApplication.class.getResource("maxAmmo.wav");
        if (resource == null) return;
        File musicPath = new File(resource.getPath());
        if (musicPath.exists()) {
            try {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void shootCPU() {
        if (enemy == null) return;
        enemy.draw();

        boolean stopflag = false;
        for (Wall wall : walls) {
            if (wall.rectangle.intersects(enemy.rectangle.getBoundsInParent())) {
                stopflag = true;
                if (enemy.ammo == 0) {
                    bulletCPU.add(new Bullet(canvas, new Vector(enemy.pos.x, enemy.pos.y), new Vector(5 * enemy.direction.x, 5 * enemy.direction.y)));
                    enemy.ammo = 1;
                }
                break;
            }
        }

        for (int i = bulletCPU.size() - 1; i >= 0; i--) {
            Bullet b = bulletCPU.get(i);
            b.draw();
            boolean hit = false;
            for (Wall wall : walls) {
                if (b.circle.intersects(wall.rectangle.getBoundsInParent())) {
                    wall.damage--;
                    hit = true;
                    break;
                }
            }
            if (hit) {
                bulletCPU.remove(i);
                enemy.ammo = 0;
                continue;
            }
            
            if (b.pos.x > canvas.getWidth() + 20 || b.pos.y > canvas.getHeight() + 20 ||
                b.pos.x < -20 || b.pos.y < -20) {
                bulletCPU.remove(i);
                enemy.ammo = 0;
                continue;
            }

            if (!avatars.isEmpty() && b.circle.intersects(avatars.get(0).rectangle.getBoundsInParent())) {
                avatars.get(0).live--;
                if (avatars.get(0).live <= 0) avatars.remove(0);
                bulletCPU.remove(i);
                enemy.ammo = 0;
                continue;
            }

            if (!avatarTwos.isEmpty() && b.circle.intersects(avatarTwos.get(0).rectangle.getBoundsInParent())) {
                avatarTwos.get(0).live--;
                if (avatarTwos.get(0).live <= 0) avatarTwos.remove(0);
                bulletCPU.remove(i);
                enemy.ammo = 0;
                continue;
            }
        }

        if (!stopflag) {
            enemy.moveForward();
        }
    }
}
