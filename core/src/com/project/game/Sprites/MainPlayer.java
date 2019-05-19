package com.project.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.project.game.CrisisGame;

public class MainPlayer extends Sprite {

    public World world;
    public Body b2body;

    public MainPlayer(World world){
        this.world = world;
        defineMainPlayer();
    }

    public void defineMainPlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(260/ CrisisGame.PPM,809/ CrisisGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(30/ CrisisGame.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }

}
