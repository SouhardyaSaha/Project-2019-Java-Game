package com.project.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.values.RectangleSpawnShapeValue;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.project.game.CrisisGame;
import com.project.game.Screens.PlayScreen;

public class MainPlayer extends Sprite {

    public World world;
    public Body b2body;
    private TextureRegion playerStand;

    public MainPlayer(World world, PlayScreen screen){
        super(screen.getAtlas().findRegion("robot4-walk0"));
        this.world = world;
        defineMainPlayer();
        playerStand = new TextureRegion(getTexture(), 0, 0 , 1000, 1000);
        setBounds(0, 0, 500 / CrisisGame.PPM, 500 / CrisisGame.PPM);
        setRegion(playerStand);
    }

    public  void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 );
    }

    public void defineMainPlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(260/ CrisisGame.PPM,809/ CrisisGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
//        CircleShape shape = new CircleShape();
//        shape.setRadius(130/ CrisisGame.PPM);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(60/ CrisisGame.PPM,165/ CrisisGame.PPM);


        fdef.shape = shape;
        b2body.createFixture(fdef);
    }

}
