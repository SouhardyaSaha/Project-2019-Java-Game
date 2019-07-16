package com.project.game.Characters;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.project.game.CrisisGame;
import com.project.game.Screens.PlayScreen;

public abstract class Enemy extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public float x,y;
    public Body b2body;
    public boolean shoot;

    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        this.x = x;
        this.y = y;
        setPosition(x , y);
        defineEnemy();
        shoot = false;
    }

    protected abstract void defineEnemy();

    public void setVelocity(){
        float playerPos = screen.mainPlayer.b2body.getPosition().x;
        float enemyPos = b2body.getPosition().x;
        if(Math.abs(playerPos - enemyPos) > 300/ CrisisGame.PPM) {
            if (screen.mainPlayer.b2body.getPosition().x > b2body.getPosition().x)
                this.b2body.setLinearVelocity(new Vector2(0.8f, 0));

            else if (screen.mainPlayer.b2body.getPosition().x < b2body.getPosition().x)
                this.b2body.setLinearVelocity(new Vector2(-0.8f, 0f));
        }
    }
    public abstract void enemyBulletHit();
}
