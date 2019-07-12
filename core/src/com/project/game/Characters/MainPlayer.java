package com.project.game.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.project.game.CrisisGame;
import com.project.game.Screens.PlayScreen;

public class MainPlayer extends Sprite {

    public enum State { Falling, JUMPING, STANDING, RUNNING, Shooting}
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;

    private TextureRegion playerStand;
    private TextureRegion playerShooting;
    private Animation playerRun;
    private Animation playerJump;
    private Animation playerFalling;
    private float stateTimer;
    public boolean walkingLeft;
//    public boolean shoot;

    public MainPlayer(PlayScreen screen){
        super(screen.getAtlas().findRegion("robot4-walk8"));
        this.world = screen.getWorld();

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        walkingLeft = true;
//        shoot = false;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i<=32; i++){
            frames.add(new TextureRegion(new Texture("Animations/Walk/walking" + i + ".png")));
        }
        playerRun = new Animation(1f/32f, frames);
        frames.clear();

        for (int i = 0; i<=30; i++){
            frames.add(new TextureRegion(new Texture("Animations/jump/robot4-jump" + i + ".png")));
        }
        playerJump = new Animation(1f/30f, frames);
        frames.clear();
//
//        for (int i = 1; i<=16; i++){
//            frames.add(new TextureRegion(new Texture("Animations/Shooting/Shooting" + i + ".png")));
//        }
        playerShooting = new TextureRegion(new Texture("Animations/Shooting/Shooting15.png"));

        frames.clear();

        playerStand = new TextureRegion(new Texture("Animations/Shooting/Shooting1.png"));

        defineMainPlayer();
        setBounds(0, 0, 500 / CrisisGame.PPM, 500 / CrisisGame.PPM);
        setRegion(playerStand);
    }

    public  void update(float dt){
        if(walkingLeft)
            setPosition(b2body.getPosition().x - getWidth() / 2.2f, b2body.getPosition().y - getHeight() / 1.7f );
        else
            setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight() / 1.7f );
        setRegion(getFrame(dt));
//        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) shoot = true;
//        if (playerShooting.isAnimationFinished(stateTimer)) shoot = false;
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion Region;

        switch (currentState){
            case JUMPING:
                Region = (TextureRegion) playerJump.getKeyFrame(stateTimer, false);
                break;
            case RUNNING:
                Region = (TextureRegion) playerRun.getKeyFrame(stateTimer, true);
                break;
            case Falling:
            case STANDING:
                Region = playerStand;
                break;
            case Shooting:
                Region = playerShooting;
                break;
//                Region = (TextureRegion) playerShooting.getKeyFrame(stateTimer, false);
//                break;
            default:
                Region = playerStand;
                break;
        }

        if ((b2body.getLinearVelocity().x > 0 || !walkingLeft) && !Region.isFlipX() ){
            Region.flip(true, false);
            walkingLeft = false;
        }
        else if ((b2body.getLinearVelocity().x < 0 || walkingLeft) && Region.isFlipX()){
            Region.flip(true, false);
            walkingLeft = true;
        }

//        if(Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
//            shoot = true;
//        }
//        else shoot = false;

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return Region;
    }

    public State getState(){
        if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if (b2body.getLinearVelocity().y < 0)
            return State.Falling;
        else if (b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
            return State.Shooting;
        else
            return State.STANDING;
    }

    public void defineMainPlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(320/ CrisisGame.PPM,700/ CrisisGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
//        CircleShape shape = new CircleShape();
//        shape.setRadius(130/ CrisisGame.PPM);
        fdef.filter.categoryBits = CrisisGame.PLAYER_BIT;
        fdef.filter.maskBits =    CrisisGame.GROUND_BIT | CrisisGame.ACID_BIT
                                | CrisisGame.BARREL_BIT
                                | CrisisGame.BOX_BIT | CrisisGame.ENEMY_BIT ;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(60/ CrisisGame.PPM,185/ CrisisGame.PPM);


        fdef.shape = shape;
        b2body.createFixture(fdef);
    }

}
