package com.project.game.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.project.game.CrisisGame;
import com.project.game.Screens.PlayScreen;
import com.project.game.Tools.EnemyBullet;

import java.util.ArrayList;

public class EnemyOne extends Enemy {

    public enum State { Walking, STANDING, Shooting}
    public State currentState;
    public State previousState;

    ///EnemyBullets
    ArrayList<EnemyBullet> bullets;
    private boolean walkingLeft;

    private float stateTime;
    private TextureRegion region;
    private TextureRegion playerStand;
    private TextureRegion shooting;
    private Animation walkAnimation;
    private Animation dieAnimation;
    private Array<TextureRegion> frames;
    private int bulletHitCount;
    public boolean fire;
    public int bulletTimeCount;

    boolean setToDestroy, destroyed;

    private boolean shoot;


    public EnemyOne(PlayScreen screen, float x, float y) {
        super(screen, x, y);
//        this.x = x;
//        this.y = y;
        frames = new Array<TextureRegion>();
        for(int i=0; i<=32; i++){
            frames.add(new TextureRegion(new Texture("Animations/Enemies/robot11/walk/robot11-walk" + i + ".png")));
        }
        walkAnimation = new Animation(1f/32f, frames);
        frames.clear();

        for(int i=0; i<=20; i++){
            frames.add(new TextureRegion(new Texture("Animations/Enemies/robot11/die/robot11-die" + i + ".png")));
        }
        dieAnimation = new Animation(1f/20f, frames);
        frames.clear();

        playerStand = new TextureRegion(new Texture("Animations/Enemies/robot11/attack/robot11-attack0.png"));

//        idleStand = new Animation(1f, frames);
        frames.clear();

        shooting = new TextureRegion(new Texture("Animations/Enemies/robot11/attack/robot11-attack12.png"));
        frames.clear();

//        stateTime = 0;
        setBounds(10,10 , 350 / CrisisGame.PPM, 410 / CrisisGame.PPM);

        region = playerStand;
        setRegion( region);
        bulletHitCount = 0;
        setToDestroy = false;
        destroyed = false;

        ///creating bullets
        bullets = new ArrayList<EnemyBullet>();
        walkingLeft = true;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTime = 0;
//        shoot = false;
    }

    public void update(float dt){

        ///Only for dying animation
        shoot = false;
        if(destroyed) stateTime += dt;

        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            stateTime = 0;
        }
        else if (!destroyed) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(getFrame(dt));
            setVelocity();


            bulletTimeCount += dt;
            if (bulletTimeCount > 2) {
                fire = true;
                bulletTimeCount = 0;
            } else fire = false;
            if (fire) {
                float bulletX = b2body.getPosition().x;
                float bulletY = b2body.getPosition().y;
                bullets.add(new EnemyBullet(screen, bulletX, bulletY, walkingLeft, 2));
                System.out.println("Enemy Boss Shoot");
            }
        }
        else if(destroyed){
            ///for enemy dies
            region = (TextureRegion) dieAnimation.getKeyFrame(stateTime, false);
            if(!walkingLeft && !region.isFlipX()) region.flip(true,false);
            if(walkingLeft && region.isFlipX()) region.flip(true,false);
            setRegion(region);
        }

        ///for bullets update
        ArrayList<EnemyBullet> bulletToRemove = new ArrayList<EnemyBullet>();
        for(EnemyBullet bullet : bullets){
            bullet.update(dt, fire);
            if(bullet.remove){
                bulletToRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletToRemove);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(super.x / CrisisGame.PPM,  super.y / CrisisGame.PPM );
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.restitution = -3;
        fdef.filter.categoryBits = CrisisGame.ENEMY_BIT;
        fdef.filter.maskBits =    CrisisGame.GROUND_BIT | CrisisGame.BULLET_BIT;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(60/ CrisisGame.PPM,185/ CrisisGame.PPM);

        fdef.shape = shape;
        fdef.restitution = 0;
        b2body.createFixture(fdef).setUserData(this);
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion Region;

        switch (currentState) {
            case Walking:
                Region = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
                break;
            case STANDING:
                Region = playerStand;
                break;
            case Shooting:
                Region = shooting;
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

//        stateTime+=dt;
        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;
        return Region;
    }

    public State getState(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z))
            return State.Shooting;
        else if (b2body.getLinearVelocity().x != 0)
            return State.Walking;
        else
            return State.STANDING;
    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 3)
            super.draw(batch);

        ///bullet rendering
        for (EnemyBullet bullet : bullets){
            bullet.draw(batch);
        }
    }

    @Override
    public void enemyBulletHit() {
        bulletHitCount++;
        if(bulletHitCount > 3) {
            setToDestroy = true;
        }
    }
}
