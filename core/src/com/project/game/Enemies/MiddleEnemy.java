package com.project.game.Enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.project.game.CrisisGame;
import com.project.game.Screens.PlayScreen;
import com.project.game.Tools.EnemyBullet;

import java.util.ArrayList;

public class MiddleEnemy extends Enemy {

    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    public boolean walkingLeft;
    public float bulletTimeCount;
    public boolean fire;
    ///Enemy Bullets
    ArrayList<EnemyBullet> bullets;
    PlayScreen screen;
    private TextureRegion region;
    private TextureRegion playerStand;
    private Animation playerRun;
    private Animation playerJump;
    private Animation playerShooting;
    private Animation playerDieing;
    private float stateTimer;
    private int bulletHitCount;
    private boolean setToDestroy, destroyed;
    private float posX, posY;
    private ParticleEffect particleEffect;

    public MiddleEnemy(PlayScreen screen, float posX, float posY) {
        super(screen, posX, posY);
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
            frames.add(new TextureRegion(new Texture("Animations/Enemies/robot7/walk/robot_7-walk" + i + ".png")));
        }
        playerRun = new Animation(1f / 17f, frames);
        frames.clear();

        for (int i = 0; i <= 22; i++) {
            frames.add(new TextureRegion(new Texture("Animations/Enemies/robot7/jump/robot_7-jump" + i + ".png")));
        }
        playerJump = new Animation(1f / 23f, frames);
        frames.clear();

        for (int i = 0; i <= 12; i++) {
            frames.add(new TextureRegion(new Texture("Animations/Enemies/robot7/die/robot_7-die" + i + ".png")));
        }
        playerDieing = new Animation(1f / 13f, frames);
        frames.clear();

        for (int i = 0; i <= 10; i++) {
            frames.add(new TextureRegion(new Texture("Animations/Enemies/robot7/attack/robot_7-attack" + i + ".png")));
        }
        playerShooting = new Animation(1f / 11f, frames);
        frames.clear();

        playerStand = new TextureRegion(new Texture("Animations/Enemies/robot7/attack/robot_7-attack0.png"));

        defineEnemy();
        setBounds(10, 10, 600 / CrisisGame.PPM, 700 / CrisisGame.PPM);
        setRegion(playerStand);

        bulletHitCount = 0;
        bulletTimeCount = 0;
        setToDestroy = false;
        destroyed = false;
        fire = true;

        ///creating bullets
        bullets = new ArrayList<EnemyBullet>();


//        b2body.setActive(false);
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
                setPosition(b2body.getPosition().x - getWidth() / 2.2f, b2body.getPosition().y - getHeight() / 1.9f);
            else
                setPosition(b2body.getPosition().x - getWidth() / 2f, b2body.getPosition().y - getHeight() / 1.9f);

            setRegion(getFrame(dt));

            ///For Bullets
            bulletTimeCount += dt;
            if (bulletTimeCount > 0.8f) {
                fire = true;
                bulletTimeCount = 0;
            } else fire = false;
            if (fire) {
                float bulletX = b2body.getPosition().x;
                float bulletY = b2body.getPosition().y;
                bullets.add(new EnemyBullet(screen, bulletX, bulletY, walkingLeft, 1));
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
            case Falling:
            case STANDING:
                Region = playerStand;
                break;
            case Shooting:
                Region = (TextureRegion) playerShooting.getKeyFrame(stateTimer, true);
                break;
            default:
                Region = playerStand;
                break;
        }

        float playerPos = screen.mainPlayer.b2body.getPosition().x;
        float enemyPos = b2body.getPosition().x;

        if (((b2body.getLinearVelocity().x > 0 || !walkingLeft) && !Region.isFlipX())) {
            Region.flip(true, false);
            walkingLeft = false;
        } else if ((b2body.getLinearVelocity().x < 0 || walkingLeft) && Region.isFlipX()) {
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
            return State.Falling;
        else if (b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else if (fire)
            return State.Shooting;
        else
            return State.STANDING;
    }

    public void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(posX / CrisisGame.PPM, posY / CrisisGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.restitution = -3;
//        fdef.density = 3;
        fdef.filter.categoryBits = CrisisGame.ENEMY_BIT;
        fdef.filter.maskBits = CrisisGame.GROUND_BIT | CrisisGame.BULLET_BIT;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(200 / CrisisGame.PPM, 300 / CrisisGame.PPM);


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

        if (Math.abs(playerPos - enemyPos) > 500 / CrisisGame.PPM && b2body.getLinearVelocity().y == 0) {
            if (screen.mainPlayer.b2body.getPosition().x > b2body.getPosition().x && b2body.getLinearVelocity().x <= 2) {
                this.b2body.setLinearVelocity(new Vector2(1.7f, 0));
            } else if (screen.mainPlayer.b2body.getPosition().x < b2body.getPosition().x && b2body.getLinearVelocity().x >= -2) {
                this.b2body.setLinearVelocity(new Vector2(-1.7f, 0f));
            }
        }
    }

    public void enemyBulletHit() {
        bulletHitCount++;
        if (bulletHitCount > 5) {
            setToDestroy = true;
        }
    }

    public enum State {Falling, JUMPING, STANDING, RUNNING, Shooting}

}
