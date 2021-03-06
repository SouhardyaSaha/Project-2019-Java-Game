package com.project.game.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.project.game.CrisisGame;
import com.project.game.Screens.PlayScreen;
import com.project.game.Tools.EnemyBullet;

import java.util.ArrayList;

public class EnemyBoss extends Enemy {

    public enum State {FALLING, JUMPING, STANDING, RUNNING, SHOOTING}

    public State currentState;
    public State previousState;

    ///Enemy Bullets
    ArrayList<EnemyBullet> bullets;

    public World world;
    public Body b2body;

    private TextureRegion region;
    private TextureRegion playerStand;
    private TextureRegion playerShooting;
    private Animation playerRun;
    private Animation playerJump;
    private Animation playerDieing;
    private float stateTimer;
    public boolean walkingLeft;
//    public boolean shoot;

    PlayScreen screen;
    private int bulletHitCount;
    public float bulletTimeCount;
    public boolean fire;
    private boolean setToDestroy, destroyed;
    private float posX, posY;

    private ParticleEffect particleEffect;

    public EnemyBoss(PlayScreen screen, float posX, float posY) {
        super(screen, posX, posY);
//        super(screen.getAtlas().findRegion("robot4-walk8"));
        this.posX = posX;
        this.posY = posY;
        this.world = screen.getWorld();
        this.screen = screen;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        walkingLeft = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i <= 16; i++) {
            frames.add(new TextureRegion(new Texture("Animations/Enemies/Enemy Boss/walk/robot_9-walk" + i + ".png")));
        }
        playerRun = new Animation(1f / 16f, frames);
        frames.clear();

        for (int i = 0; i <= 12; i++) {
            frames.add(new TextureRegion(new Texture("Animations/Enemies/Enemy Boss/jump/enemy" + i + ".png")));
        }
        playerJump = new Animation(1f / 12f, frames);
        frames.clear();

        for (int i = 1; i <= 10; i++) {
            frames.add(new TextureRegion(new Texture("Animations/Enemies/Enemy Boss/die/robot_9-die" + i + ".png")));
        }
        playerDieing = new Animation(1f / 10f, frames);
        frames.clear();

        playerShooting = new TextureRegion(new Texture("Animations/Enemies/Enemy Boss/attack/robot_9-attack2.png"));

        frames.clear();

        playerStand = new TextureRegion(new Texture("Animations/Enemies/Enemy Boss/attack/robot_9-attack7.png"));

        defineMainPlayer();
        setBounds(0, 0, 1500 / CrisisGame.PPM, 1000 / CrisisGame.PPM / 1.2f);
        setRegion(playerStand);

        bulletHitCount = 0;
        bulletTimeCount = 0;
        setToDestroy = false;
        destroyed = false;
        fire = true;

        ///creating bullets
        bullets = new ArrayList<EnemyBullet>();

        //for particles
        particleEffect = new ParticleEffect();
//        particleEffect.load(Gdx.files.internal(""), Gdx.files.internal(""));
    }

    public void update(float dt) {

        if (destroyed) stateTimer += dt;
        if (setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            stateTimer = 0;
        } else if (!destroyed) {
            setEnemyMomentum();
            if (walkingLeft)
                setPosition(b2body.getPosition().x - getWidth() / 2.2f, b2body.getPosition().y - getHeight() / 1.7f);
            else
                setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight() / 1.7f);

            setRegion(getFrame(dt));

            ///For Bullets
            bulletTimeCount += dt;
            if (bulletTimeCount > 0.5f) {
                fire = true;
                bulletTimeCount = 0;
            } else fire = false;
            if (fire) {
                float bulletX = b2body.getPosition().x;
                float bulletY = b2body.getPosition().y;
                bullets.add(new EnemyBullet(screen, bulletX, bulletY, walkingLeft, 2));
            }

        } else if (destroyed) {
            region = (TextureRegion) playerDieing.getKeyFrame(stateTimer, false);
            if (!walkingLeft && !region.isFlipX()) region.flip(true, false);
            if (walkingLeft && region.isFlipX()) region.flip(true, false);
            setRegion(region);
        }

        ///for bullets update
        ArrayList<EnemyBullet> bulletToRemove = new ArrayList<EnemyBullet>();
        for (EnemyBullet bullet : bullets) {
            bullet.update(dt, fire);
            if (bullet.remove) {
                bulletToRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletToRemove);
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion Region;

        switch (currentState) {
            case JUMPING:
                Region = (TextureRegion) playerJump.getKeyFrame(stateTimer, false);
                break;
            case RUNNING:
                Region = (TextureRegion) playerRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
                Region = playerStand;
                break;
            case SHOOTING:
                Region = playerShooting;
                break;
            default:
                Region = playerStand;
                break;
        }

        if (((b2body.getLinearVelocity().x > 0 || !walkingLeft ) && !Region.isFlipX() ) ) {
            Region.flip(true, false);
            walkingLeft = false;
        } else if ((b2body.getLinearVelocity().x < 0 || walkingLeft ) && Region.isFlipX() ) {
            Region.flip(true, false);
            walkingLeft = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return Region;
    }

    public State getState() {
        if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if (b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else if (Gdx.input.isKeyPressed(Input.Keys.Z))
            return State.SHOOTING;
        else
            return State.STANDING;
    }

    public void defineMainPlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(posX / CrisisGame.PPM, posY / CrisisGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.restitution = -3;
        fdef.filter.categoryBits = CrisisGame.ENEMY_BOSS_BIT;
        fdef.filter.maskBits = CrisisGame.GROUND_BIT | CrisisGame.BULLET_BIT;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(600 / CrisisGame.PPM, 400 / CrisisGame.PPM);


        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch) {
        if (!destroyed || stateTimer < 3)
            super.draw(batch);

        ///bullet rendering
        for (EnemyBullet bullet : bullets) {
            bullet.draw(batch);
        }
    }

    public void setEnemyMomentum() {

        float playerPos = screen.mainPlayer.b2body.getPosition().x;
        float enemyPos = b2body.getPosition().x;

        if (Math.abs(playerPos - enemyPos) > 1400 / CrisisGame.PPM  &&  b2body.getLinearVelocity().y == 0) {
            if (b2body.getPosition().x < screen.mainPlayer.b2body.getPosition().x)
                this.b2body.applyLinearImpulse(new Vector2(6.5f, 12f), new Vector2(0, 0), true);
            else
                this.b2body.applyLinearImpulse(new Vector2(-6.5f, 12f), new Vector2(0, 0), true);
        }
        if (Math.abs(playerPos - enemyPos) > 1200 / CrisisGame.PPM && b2body.getLinearVelocity().y == 0) {
            if (screen.mainPlayer.b2body.getPosition().x > b2body.getPosition().x && b2body.getLinearVelocity().x <= 2) {
                this.b2body.setLinearVelocity(new Vector2(1.7f, 0));
            } else if (screen.mainPlayer.b2body.getPosition().x < b2body.getPosition().x && b2body.getLinearVelocity().x >= -2) {
                this.b2body.setLinearVelocity(new Vector2(-1.7f, 0f));
            }
        }
    }

    public void enemyBulletHit() {
        bulletHitCount++;
        if (bulletHitCount > 30) {
            setToDestroy = true;
        }
    }

}
