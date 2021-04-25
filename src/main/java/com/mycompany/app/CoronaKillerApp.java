package com.mycompany.app;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;

import javafx.scene.input.KeyCode;


enum EntityType {
    PLAYER, BULLET, ENEMY, BACKGROUND, WALL
}

public class CoronaKillerApp extends GameApplication {
    /* Setting game settings, such as screen size */
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setFullScreenFromStart(true);
        settings.setScaleAffectedOnResize(false);

        settings.setWidth(1280);
        settings.setHeight(720);
//        settings.setManualResizeEnabled(true);
        settings.setTitle("Corona Killer");
        settings.setVersion("0.1");
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    /**
     * Initing physics manager
     */
    @Override
    protected void initPhysics() {

        /* Collisions SOMETHING -> ENEMY */

        FXGL.onCollision(EntityType.PLAYER, EntityType.ENEMY, (player, enemy) -> {
            System.out.println("Deaddddddddddd");
            Sound deathSound = FXGL.getAssetLoader().loadSound("death.wav");

            FXGL.getAudioPlayer().playSound(deathSound);
            FXGL.showMessage("Perdeu!", () -> {
                FXGL.getGameController().startNewGame();
            });
        });

        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.ENEMY, (bullet, enemy) -> {
            bullet.removeFromWorld();
            enemy.removeFromWorld();
            this.bullet = null;
            System.out.println("On Collision");
        });

        /* Collisions SOMETHING -> WALL */

        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.WALL, (bullet, wall) -> {
            bullet.removeFromWorld();
            wall.removeFromWorld();
            this.bullet = null;
        });

        FXGL.onCollisionBegin(EntityType.PLAYER, EntityType.WALL, (player, wall) -> {
            System.out.println("Start of collision between wall and player");
//            player.getComponent(PlayerComponent.class).stopMovement();
            System.out.println(player.isColliding(wall));
        });
    }

    /**
     * Initing listener for input actions
     */
    @Override
    protected void initInput() {
        FXGL.getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).left();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.A);

        FXGL.getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).right();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Down") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).down();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.S);

        FXGL.getInput().addAction(new UserAction("Up") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).up();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.W);


        FXGL.onKey(KeyCode.SPACE , () -> {
//            if(this.bullet == null)
                this.bullet = this.player.getComponent(PlayerComponent.class).shotProjectile(this.gameFactory);
        });

        FXGL.onKey(KeyCode.L , () -> {
//            if(this.enemy == null)
//                System.out.println("fix");
//                this.enemy = this.gameFactory.newEnemy();
                FXGL.spawn("enemy");
        });
    }

    private final GameFactory gameFactory = new GameFactory();
    private Entity player, bullet, enemy;

    /**
     * Init configurations of the FXGL game
     */
    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(gameFactory);
        FXGL.setLevelFromMap("level1.tmx");

        player = this.gameFactory.newPlayer();

    }


    public static void main(String[] args) {
        launch(args);
    }
}

