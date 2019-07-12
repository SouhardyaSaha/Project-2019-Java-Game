package com.project.game.Characters;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.project.game.CrisisGame;
import com.project.game.Screens.PlayScreen;
import sun.reflect.generics.tree.VoidDescriptor;

public abstract class Enemy extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public float x,y;
    public Body b2body;

    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        this.x = x;
        this.y = y;
        setPosition(x , y );
        defineEnemy();
    }

    protected abstract void defineEnemy();
//    public abstract void hitEnemy();
}
