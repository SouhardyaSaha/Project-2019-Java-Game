package com.project.game.Enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.project.game.Screens.PlayScreen;

public abstract class Enemy extends Sprite {


    public Enemy(PlayScreen screen, float x, float y){

    }

    public abstract void enemyBulletHit();
}
