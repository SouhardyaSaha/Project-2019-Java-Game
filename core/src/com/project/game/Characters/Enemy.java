package com.project.game.Characters;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.project.game.CrisisGame;
import com.project.game.Screens.PlayScreen;
import sun.reflect.generics.tree.VoidDescriptor;

public abstract class Enemy extends Sprite {

    ///Enemy States

    protected World world;
    protected PlayScreen screen;
    public float x,y;
    public Body b2body;
    public Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        this.x = x;
        this.y = y;
        setPosition(x , y );
        defineEnemy();
        velocity = new Vector2(0f, 0);
    }

    protected abstract void defineEnemy();

    public void reverseVelocity(boolean x, boolean y){
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }
    public abstract void enemyBulletHit();
}
