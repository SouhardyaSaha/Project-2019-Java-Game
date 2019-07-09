package com.project.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.project.game.Sprites.MainPlayer;

public class Bullet {
    public static final int speed = 12;
    private static Texture texture;
    private static Sprite sprite;
    private boolean right;

    float x, y;
    public boolean remove = false;

    public Bullet(float x, float y, boolean fireRight){
        this.x = x;
        this.y = y;
        right = fireRight;


        if(!fireRight) texture = new Texture("fire_blue.png");
        else texture = new Texture("fire_blueflip.png");
//            sprite = new Sprite(texture);
//            if(!right) sprite.flip(true, false);

    }

    public void update(float dt){
        if (!right)  x += speed * dt;
        else x -= speed * dt;
        if(x > 1600 || x < 0 ){
            remove = true;
        }
    }

    public void render(SpriteBatch batch){
//        sprite.draw(batch);
        batch.draw(texture, x, y, 0.8f, 0.5f);
    }
//
//    public void dispose(){
//        texture.dispose();
//    }

}
