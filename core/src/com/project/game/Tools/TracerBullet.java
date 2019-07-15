package com.project.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.project.game.CrisisGame;
import com.project.game.Screens.PlayScreen;

public class TracerBullet extends Sprite {
     /*public static final int Speed = 500;
    //public static final float d_x = 40;*/

    private static Texture texture;
    //float x, y;

    private Body b2body;
    private World world;

    //new
    PVector location;
    PVector velocity;
    PVector acceleration;

    float topspeed;


    //new

    public boolean remove = false;

    public TracerBullet(PlayScreen screen, float x, float y, boolean fireRight){

        //new
        world = screen.getWorld();
        location = new PVector(x, y);
        velocity = new PVector(0, 0);
        topspeed = 5;


        //new

//        if(texture == null)
            texture = new Texture("fire_blueflip.png");
        //System.out.println(Gdx.graphics.getHeight());
    }

    public void update (float dtl){

        //new
        float mx = Gdx.input.getX(), my = Gdx.input.getY();
        //System.out.println(mx+" "+my);

        PVector mouse = new PVector(mx, 480-my);
        PVector dir = PVector.sub(mouse, location);
        dir.normalize();
        dir.mult(.5f);
        acceleration = dir;

        velocity.add(acceleration);
        velocity.limit(topspeed);
        location.add(velocity);

        //new
        b2body.setTransform(new Vector2(location.x, location.y),0);

        //x += Speed * d_time;

        if(location.x > Gdx.graphics.getWidth() || location.y > Gdx.graphics.getHeight() || location.y < 0)
            remove = true;

    }

    public void render (SpriteBatch batch)
    {
        batch.begin();
        batch.draw(texture, location.x, location.y);
        batch.end();
    }

    ///Souhardya
    private void defineBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.gravityScale = 0;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bodyDef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(50/ CrisisGame.PPM);

        fdef.shape = shape;
        fdef.restitution = 0;
//        fdef.friction = 0;
        fdef.filter.categoryBits = CrisisGame.BULLET_BIT;
        fdef.filter.maskBits =    CrisisGame.GROUND_BIT | CrisisGame.ENEMY_BIT  ;
        b2body.createFixture(fdef).setUserData(this);
    }

    public  void draw(Batch batch){
//        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }
}
