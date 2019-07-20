package com.project.game.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.project.game.CrisisGame;
import com.project.game.Screens.PlayScreen;
import com.project.game.Tools.Bullet;

import java.util.ArrayList;

public class MainPlayer extends Sprite {

    public enum State { Falling, JUMPING, STANDING, RUNNING, Shooting}
    public State currentState;
    public State previousState;

    ///Main Player Bullets
    ArrayList<Bullet> bullets;

    public World world;
    public Body b2body;

    private TextureRegion region;
    private TextureRegion playerStand;
    private TextureRegion playerShooting;
    private Animation playerRun;
    private Animation playerJump;
    private Animation playerFall;
    private Animation playerDieing;
    private float stateTimer;
    public boolean walkingLeft;
    public float posX, posY;
//    public boolean shoot;

    PlayScreen screen;
    private int bulletHitCount;
    private boolean setToDestroy, destroyed;

    public MainPlayer(PlayScreen screen, float posX, float posY){

        this.posX = posX;
        this.posY = posY;
        this.world = screen.getWorld();
        this.screen = screen;

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

        for (int i = 0; i<=12; i++){
            frames.add(new TextureRegion(new Texture("Animations/jump/jump" + i + ".png")));
        }
        playerJump = new Animation(1f/24f, frames);
        frames.clear();

        for (int i = 13; i<=30; i++){
            frames.add(new TextureRegion(new Texture("Animations/jump/jump" + i + ".png")));
        }
        playerFall = new Animation(1f/26f, frames);
        frames.clear();

        for (int i = 1; i<=20; i++){
            frames.add(new TextureRegion(new Texture("Animations/die/robot4-die" + i + ".png")));
        }
        playerDieing = new Animation(1f/20f, frames);
        frames.clear();

        playerShooting = new TextureRegion(new Texture("Animations/shooting/Shooting15.png"));

        frames.clear();

        playerStand = new TextureRegion(new Texture("Animations/shooting/Shooting1.png"));

        defineMainPlayer();
        setBounds(0, 0, 600 / CrisisGame.PPM, 600 / CrisisGame.PPM);
        setRegion(playerStand);

        bulletHitCount = 0;
        setToDestroy = false;
        destroyed = false;

        ///creating bullets
        bullets = new ArrayList<Bullet>();
    }

    public  void update(float dt){

        if(destroyed) stateTimer+=dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            stateTimer = 0;
        }
        else if (!destroyed){
            handleInput(dt);
            if(walkingLeft)
                setPosition(b2body.getPosition().x - getWidth() / 2.2f, b2body.getPosition().y - getHeight() / 1.7f );
            else
                setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight() / 1.7f );

            setRegion(getFrame(dt));

        }
        else if (destroyed){
            region = (TextureRegion) playerDieing.getKeyFrame(stateTimer, false);
            if(!walkingLeft && !region.isFlipX()) region.flip(true,false);
            if(walkingLeft && region.isFlipX()) region.flip(true,false);
            setRegion(region);
        }

        ///for bullets update
        ArrayList<Bullet> bulletToRemove = new ArrayList<Bullet>();
        for(Bullet bullet : bullets){
            bullet.update(dt);
            if(bullet.remove){
                bulletToRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletToRemove);
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
                Region = (TextureRegion) playerFall.getKeyFrame(stateTimer, false);
                break;
            case STANDING:
                Region = playerStand;
                break;
            case Shooting:
                Region = playerShooting;
                break;
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

    public void handleInput(float dt){
        ///bullet
        if(Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)){
            float bulletX = b2body.getPosition().x ;
            float bulletY = b2body.getPosition().y;
            bullets.add(new Bullet(screen, bulletX, bulletY, walkingLeft));

            ///for bullet sound
            CrisisGame.manager.get("Sound Effects/zapsplat_science_fiction_weapon_gun_shoot_003_32196.mp3", Sound.class).play();
        }

        if(b2body.getLinearVelocity().y == 0 &&  Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            b2body.applyLinearImpulse(new Vector2(0, 10f), b2body.getWorldCenter(),true);
//            gameCam.position.y += 100 * dt;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && b2body.getLinearVelocity().x <= 2){
            b2body.applyLinearImpulse(new Vector2(0.5f, 0), b2body.getWorldCenter(),true);
//            gameCam.position.x += 100 * dt;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && b2body.getLinearVelocity().x >= -2){
            b2body.applyLinearImpulse(new Vector2(-0.5f, 0), b2body.getWorldCenter(),true);
//            gameCam.position.x -= 100 * dt;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            b2body.applyLinearImpulse(new Vector2(0, -4f), b2body.getWorldCenter(),true);
//            gameCam.position.y -= 100 * dt;
        }

    }

    public void defineMainPlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(posX / CrisisGame.PPM, posY / CrisisGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.restitution = -3;
        fdef.filter.categoryBits = CrisisGame.PLAYER_BIT;
        fdef.filter.maskBits =    CrisisGame.GROUND_BIT | CrisisGame.OBJECT_BIT
                                | CrisisGame.ENEMY_BIT | CrisisGame.ENEMY_BULLET_BIT | CrisisGame.DOG_BIT;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(70/ CrisisGame.PPM,200/ CrisisGame.PPM);


        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch){
        if(!destroyed || stateTimer < 3)
            super.draw(batch);

        ///bullet rendering
        for (Bullet bullet : bullets){
            bullet.draw(batch);
        }
    }

    public void playerBulletHit() {
        bulletHitCount++;
        if(bulletHitCount > 50) {
            setToDestroy = true;
        }
    }

}
