package final_project_evanliu_v3;

import com.sun.javafx.scene.traversal.Direction;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class Engine {
    
    private Scene scene; // Scene that gets passed to main.
    private GPane gpane; // GPane that allows child nodes to call the Engine through getEngine().
    private ImageView background;
    private ImageView gameOver;
    private ImageView playAgain;
    private ImageView quitGame;
    private ImageView coolKidsOnly; // ?
    private ImageView paused;
    private ImageView basket;
    
    private VBox pausePane;
    
    private Label scoreLabel;
    private int score;
    
    private Timeline mainTime; // Timeline that handles movement and collisions.
    private Timeline alphaSpawnTime; // AlphaEnemyUnit instances are spawned by this Timeline.
    private Timeline alphaAttackTime; // AlphaEnemyUnit instances attack based on this Timeline.
    
    private PlayerUnit player; // Player character
    private ArrayList<ImageView> heartList = new ArrayList(); // ArrayList with hearts for health display
    
    private ArrayList<Unit> allUnitList = new ArrayList(); // ArrayList holding a reference for all Units (Pew projectiles included).
    private ArrayList<Unit> alphaEnemyList = new ArrayList(); // ArrayList for referencing AlphaEnemyUnit instances.
    private ArrayList<Unit> playerPewList = new ArrayList(); // ArrayList for referencing all PlayerPew projectile instances.
    private ArrayList<Unit> alphaPewList = new ArrayList(); // ArrayList for holding all AlphaEnemyPew projectiles.
    
    private ArrayList<Unit> trashList = new ArrayList(); // ArrayList of things to be removed from other lists and the pane for trash collection.
    
    public Engine() {
        
        // Setting this Engine as the one associated with the GPane
        // allows child nodes to call the Engine to tell it to add projeciles
        gpane = new GPane();
        gpane.setEngine(this);
        
        background = new ImageView(new Image(FileNames.BACKGROUND, Values.SCREEN_X, Values.SCREEN_Y, false, false));
        
        gameOver = new ImageView();
        gameOver.setX(Values.SCREEN_X / 2 - Values.GAMEOVER_WIDTH / 2);
        gameOver.setY(Values.SCREEN_Y / 2 - Values.GAMEOVER_HEIGHT / 2);
        
        playAgain = new ImageView();
        playAgain.setX(Values.SCREEN_X / 2 - Values.PLAY_AGAIN_WIDTH / 2);
        playAgain.setY(Values.SCREEN_Y / 2 + Values.GAMEOVER_HEIGHT / 2 + 20);
        
        quitGame = new ImageView();
        quitGame.setX(Values.SCREEN_X / 2 - Values.QUIT_GAME_WIDTH / 2);
        quitGame.setY(Values.SCREEN_Y / 2 + Values.GAMEOVER_HEIGHT / 2 + 20 + Values.PLAY_AGAIN_HEIGHT + 20);
        
        paused = new ImageView(new Image(FileNames.PAUSED, Values.PAUSED_WIDTH, Values.PAUSED_HEIGHT, false, false));
        basket = new ImageView(new Image("basket.png", 4 * Values.coolwide, 4 * Values.coolhite, false, false));
        pausePane = new VBox(20);
        pausePane.getChildren().addAll(paused, basket);
        pausePane.setAlignment(Pos.CENTER);
        
        // Score counter
        score = 0;
        scoreLabel = new Label(String.valueOf(score));
        scoreLabel.setFont(new Font("Arial", 24));
        scoreLabel.setTextFill(Color.AZURE);
        scoreLabel.setAlignment(Pos.TOP_LEFT);
        
        gpane.getChildren().addAll(gameOver, playAgain, quitGame, pausePane, background, scoreLabel);
        
        // PlayerUnit constructed.
        player = new PlayerUnit(
                FileNames.PLAYER_IMAGE,
                Values.PLAYER_HEIGHT,
                Values.PLAYER_WIDTH,
                Values.PLAYER_DX,
                Values.PLAYER_DY);
        
        // Load list with 3 hearts
        for (int i = 0; i < player.getHP(); i++) {
            heartList.add(new ImageView(new Image(FileNames.PLAYER_HP_IMAGE, Values.PLAYER_HEART_WIDTH, Values.PLAYER_HEART_HEIGHT, false, false)));
        }
        
        for (int i = 0; i < heartList.size(); i++) {
            gpane.getChildren().add(heartList.get(i));
            heartList.get(i).setX(i * (Values.PLAYER_HEART_WIDTH * 1.2));
            heartList.get(i).setY(Values.SCREEN_Y - Values.PLAYER_HEART_HEIGHT - 20);
        }
        
        scene = new Scene(gpane, Values.SCREEN_X, Values.SCREEN_Y);
        
        addUnit(player);
        
        // Places player at default position
        player.setX(Values.SCREEN_X / 2 - Values.PLAYER_WIDTH / 2);
        player.setY(3 * Values.SCREEN_Y / 4);
        
        initializeKeyBinds();
        initializeTimelines();
        
        soCool();
    }
    
    public void initializeTimelines() {
        // Main Timeline for managing movement and collisions. Cycled every millisecond.
        mainTime = new Timeline(new KeyFrame(Duration.millis(1), e -> {
            // For all Units, perform act(), containing overriden instructions depending on the unit type.
            // Usually movement.
            for (Unit unit : allUnitList) {
                unit.act();
                
                // If player dies, stop all Timeline loops and display Game Over.
                if (unit instanceof PlayerUnit && ((PlayerUnit)unit).getHP() <= 0) {
                    
                    mainTime.stop();
                    alphaSpawnTime.stop();
                    alphaAttackTime.stop();
                    
                    gameOver.setImage(new Image(FileNames.GAMEOVER, Values.GAMEOVER_WIDTH, Values.GAMEOVER_HEIGHT, false, false));
                    gameOver.toFront();
                    
                    playAgain.setImage(new Image(FileNames.PLAY_AGAIN, Values.PLAY_AGAIN_WIDTH, Values.PLAY_AGAIN_HEIGHT, false, false));
                    playAgain.toFront();
                    
                    quitGame.setImage(new Image(FileNames.QUIT_GAME, Values.QUIT_GAME_WIDTH, Values.QUIT_GAME_HEIGHT, false, false));
                    quitGame.toFront();
                    
                    trash(unit);
                    
                    scene.setOnKeyPressed(eh -> {
                        switch(eh.getCode()) {
                            case F:
                                allUnitList.clear();
                                alphaEnemyList.clear();
                                alphaPewList.clear();
                                playerPewList.clear();
                                
                                gpane.getChildren().removeAll();
                                
                                gpane = new GPane();
                                gpane.setEngine(this);
                                gpane.getChildren().addAll(background, scoreLabel, gameOver);
                                gameOver.toBack();
                                
                                for (int i = 0; i < heartList.size(); i++) {
                                    gpane.getChildren().add(heartList.get(i));
                                    heartList.get(i).setX(i * (Values.PLAYER_HEART_WIDTH * 1.2));
                                    heartList.get(i).setY(Values.SCREEN_Y - Values.PLAYER_HEART_HEIGHT - 20);
                                }
                                
                                scene.setRoot(gpane);
                                score = 0;
                                
                                player = new PlayerUnit(
                                        FileNames.PLAYER_IMAGE,
                                        Values.PLAYER_HEIGHT,
                                        Values.PLAYER_WIDTH,
                                        Values.PLAYER_DX,
                                        Values.PLAYER_DY);
                                
                                addUnit(player);
                                player.setX(Values.SCREEN_X / 2 - Values.PLAYER_WIDTH / 2);
                                player.setY(3 * Values.SCREEN_Y / 4);
                                
                                mainTime.play();
                                alphaSpawnTime.play();
                                alphaAttackTime.play();
                                
                                initializeKeyBinds();
                                
                                break;
                            case ESCAPE: Platform.exit(); break;
                        }
                    });
                    
                    gameOver.setImage(new Image(FileNames.GAMEOVER, Values.GAMEOVER_WIDTH, Values.GAMEOVER_HEIGHT, false, false));
                    gameOver.toFront();
                }
                
                // For all AlphaEnemyUnit instances, if they have no HP left, schedule them for the trash.
                for (Unit alpha : alphaEnemyList) {
                    if (((AlphaEnemyUnit)alpha).getHP() == 0) {
                        trash(alpha);
                    }
                }
            }
            
            // For all PlayerPew projectiles, check if they have collided with any AlphaEnemyUnits.
            for (Unit playerPew : playerPewList) {
                
                for (Unit alpha : alphaEnemyList) {
                    
                    // If intersection is found, inflict damage
                    if (playerPew.intersects(alpha.getX(), alpha.getY(), alpha.getFitWidth(), alpha.getFitHeight())) {
                        ((EnemyUnit)alpha).takeDamage(((PlayerPew)playerPew).getDamage());
                        
                        // If the damaged enemy has no health, increase score.
                        if (((AlphaEnemyUnit)alpha).getHP() == 0) {
                            score++;
                            scoreLabel.setText(String.valueOf(score));
                        }
                        // Always trash the projectile after collision. No piercing abilities here!
                        trash(playerPew);
                    }
                }
                // If PlayerPew projectile goes past two full screen heights, set it for trash.
                if (playerPew.getY() < -1 * Values.SCREEN_Y) {
                    trash(playerPew);
                }
                
                coolEnough(playerPew);
            }
            
            // For all AlphaEnemyPew projectiles, check for collision with PlayerUnit.
            for (Unit alphaPew : alphaPewList) {
                // Intersection
                if (alphaPew.intersects(player.getX(), player.getY(), player.getFitWidth(), player.getFitHeight())){
                    
                    // takeDamage(damage) reduces HP by the damage given.
                    player.takeDamage(((Pew)alphaPew).getDamage());
                    
                    // Remove the heart ImageView corresponding to the ArrayList cell equal to the player's health
                    // Player's HP goes 3 -> 2. Remove heartList.get(2) from the GPane, thus removing the third heart on the screen, leaving 2.
                    gpane.getChildren().remove(heartList.get(player.getHP()));
                    
                    // Trash it when you get hit
                    trash(alphaPew);
                }
                
                // Trash the enemmy pew projectiles when they leave the screen.
                if (alphaPew.getX() < 0 - alphaPew.getFitHeight() || alphaPew.getX() > Values.SCREEN_X || alphaPew.getY() > Values.SCREEN_Y || alphaPew.getY() < 0) {
                    trash(alphaPew);
                }
            }
            
            for (Unit unit : trashList) {
                if (trashList.size() > 0) {
                    removeUnit(unit);
                }
            }
            trashList.clear();
        }));
        
        mainTime.setCycleCount(Timeline.INDEFINITE);
        mainTime.play();
        
        alphaSpawnTime = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
            
            if (alphaEnemyList.size() < 12) {
                addUnit(new AlphaEnemyUnit(
                        FileNames.ALHPA_ENEMY_IMAGE,
                        Values.ALPHA_HEIGHT,
                        Values.ALPHA_WIDTH,
                        (Math.random() >= Values.ALPHA_DIRECTION_CHANCE ? Values.ALPHA_DX : -1 * Values.ALPHA_DX),
                        Values.ALPHA_DY,
                        Values.SCREEN_X * Math.random(),
                        0 - Values.ALPHA_HEIGHT));
            }
        }));
        
        alphaSpawnTime.setCycleCount(Timeline.INDEFINITE);
        alphaSpawnTime.play();
        
        alphaAttackTime = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            for (Unit alpha : alphaEnemyList) {
                if (Math.random() >= Values.ALPHA_FIRE_CHANCE) {
                    ((AlphaEnemyUnit)alpha).shoot();
                }
            }
        }));
        
        alphaAttackTime.setCycleCount(Timeline.INDEFINITE);
        alphaAttackTime.play();
    }
    
    public void initializeKeyBinds() {
        // On press, sets movement boolean in PlayerUnit to true.
        scene.setOnKeyPressed(e -> {
            try {
                switch(e.getCode()) {
                    case W:
                    case UP: player.moveStart(Direction.UP); break;
                    case A:
                    case LEFT: player.moveStart(Direction.LEFT); break;
                    case S:
                    case DOWN: player.moveStart(Direction.DOWN); break;
                    case D:
                    case RIGHT: player.moveStart(Direction.RIGHT); break;
                    // Shooting is a function where the PlayerUnit calls the Engine of its parent GPane
                    // to give instructions to create a PlayerPew projectile.
                    case SPACE: player.shoot(); break;
                    case ESCAPE: 
                        mainTime.pause();
                        alphaSpawnTime.pause();
                        alphaAttackTime.pause();
                        
                        pausePane.toFront();
                        
                        paused.setOnKeyPressed(e2 -> {
                            switch(e2.getCode()) {
                                case SPACE:
                                    pausePane.toBack();
                                    mainTime.play();
                                    alphaSpawnTime.play();
                                    alphaAttackTime.play();
                                    break;
                            }
                        });
                        paused.requestFocus();
                }
            } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("No player");
            }
        });
        
        // On release, boolean reverts to false.
        scene.setOnKeyReleased(e -> {
            try {
                switch(e.getCode()) {
                    case W:
                    case UP: player.moveStop(Direction.UP); break;
                    case A:
                    case LEFT: player.moveStop(Direction.LEFT); break;
                    case S:
                    case DOWN: player.moveStop(Direction.DOWN); break;
                    case D:
                    case RIGHT: player.moveStop(Direction.RIGHT); break;
                }
            } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("No player");
            }
        });
    }
    
    // Adds argument Unit to the allUnitList, the GPane, and the appropriate secondary list.
    public void addUnit(Unit unit) {
        allUnitList.add(unit);
        gpane.getChildren().add(unit);

        if (unit instanceof PlayerPew) {
            playerPewList.add(unit);
        }
        if (unit instanceof AlphaEnemyPew) {
            alphaPewList.add(unit);
        }
        
        if (unit instanceof AlphaEnemyUnit) {
            alphaEnemyList.add(unit);
        }
    }
        
    // Removes unit from any possible list and the GPane.
    // Removing all reference points for the object should allow the Garbage Collector to remove them.
    public void removeUnit(Unit unit) {
        allUnitList.remove(unit);
        gpane.getChildren().remove(unit);
        
        if (unit instanceof AlphaEnemyUnit) {
            alphaEnemyList.remove(unit);
        }
        if (unit instanceof PlayerPew) {
            playerPewList.remove(unit);
        }
        if (unit instanceof AlphaEnemyPew) {
            alphaPewList.remove(unit);
        }
    }
    
    // Adds designated Unit to the list for objects to remove.
    public void trash(Unit unit) {
        trashList.add(unit);
    }
    
    public Scene getScene() {
        return scene;
    }
    
    public PlayerUnit getPlayer() {
        return player;
    }
    
    private void wowMuchBasketAmaze() {
        player.setImage(new Image("basket.png", Values.coolwide, Values.coolhite, true, true));
        player.setFitWidth(Values.coolwide);
        player.setFitHeight(Values.coolhite);
        
    }
    
    private void soCool() {
        coolKidsOnly = new ImageView();
        coolKidsOnly.setX(0);
        coolKidsOnly.setY(0);
        coolKidsOnly.setFitWidth(21);
        coolKidsOnly.setFitHeight(0);
        gpane.getChildren().add(coolKidsOnly);
    }
    
    private void coolEnough(Unit unit) {
        if (unit.intersects(coolKidsOnly.getX(), coolKidsOnly.getY(), coolKidsOnly.getFitWidth(), coolKidsOnly.getFitHeight())) {
            wowMuchBasketAmaze();
        }
    }
}
