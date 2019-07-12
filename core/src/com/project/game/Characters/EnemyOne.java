package com.project.game.Characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.project.game.CrisisGame;
import com.project.game.Screens.PlayScreen;

public class EnemyOne extends Enemy {

    private float stateTime;
//    public float x,y;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;


    public EnemyOne(PlayScreen screen, float x, float y) {
        super(screen, x, y);
//        this.x = x;
//        this.y = y;
        frames = new Array<TextureRegion>();
        for(int i=0; i<=16; i++){
            frames.add(new TextureRegion(new Texture("Animations/Enemies/ZombieOne/walk/zombie1-walk" + i + ".png")));
        }
        walkAnimation = new Animation(1f/16f, frames);
        stateTime = 0;
        setBounds(10,10 , 350 / CrisisGame.PPM, 410 / CrisisGame.PPM);
    }

    public void update(float dt){

        stateTime += dt;
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2.1f );
        setRegion( (TextureRegion) walkAnimation.getKeyFrame(stateTime, true));

    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(super.x /CrisisGame.PPM,  super.y/CrisisGame.PPM );
//        bdef.position.set(320/CrisisGame.PPM,  700/CrisisGame.PPM );
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
//        CircleShape shape = new CircleShape();
//        shape.setRadius(130/ CrisisGame.PPM);
        fdef.filter.categoryBits = CrisisGame.ENEMY_BIT;
        fdef.filter.maskBits =    CrisisGame.GROUND_BIT
                | CrisisGame.BARREL_BIT | CrisisGame.BULLET_BIT
                | CrisisGame.BOX_BIT | CrisisGame.ENEMY_BIT | CrisisGame.PLAYER_BIT ;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(60/ CrisisGame.PPM,165/ CrisisGame.PPM);


        fdef.shape = shape;
        fdef.restitution = 0;
        b2body.createFixture(fdef);

    }

//    @Override
//    protected void hitEnemy() {
//
//    }
}
