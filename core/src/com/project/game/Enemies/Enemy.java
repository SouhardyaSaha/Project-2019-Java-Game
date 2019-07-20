package com.project.game.Enemies;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.project.game.Screens.PlayScreen;
import com.project.game.Tools.Box2dWorldCreator;

public abstract class Enemy extends Sprite {


    public Enemy(PlayScreen screen, float x, float y){

    }

    public abstract void update(float dt);
    public abstract void enemyBulletHit();
}
