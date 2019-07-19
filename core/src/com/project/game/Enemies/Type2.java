package com.project.game.Enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.project.game.CrisisGame;
import com.project.game.Screens.PlayScreen;

public class Type2 extends Enemy {

    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    public boolean walkingLeft;
    public float bulletTimeCount;
    public boolean fire;
    PlayScreen screen;
    private TextureRegion region;
    private TextureRegion playerStand;
    private Animation playerAttack;
    private Animation playerRun;
    private Animation playerJump;
    private Animation playerDieing;
    private float stateTimer;
    private int bulletHitCount;
    private boolean setToDestroy, destroyed;
    private float posX, posY;

    public Type2(PlayScreen screen, float posX, float posY) {
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
        for (int i = 0; i <= 32; i++) {
            frames.add(new TextureRegion(new Texture("Animations/Enemies/robot11/walk/robot11-walk" + i + ".png")));
        }
        playerRun = new Animation(1f / 32f, frames);
        frames.clear();

        for (int i = 0; i <= 12; i++) {
            frames.add(new TextureRegion(new Texture("Animations/Enemies/Enemy Boss/jump/robot_9-jump" + i + ".png")));
        }
        playerJump = new Animation(1f / 30f, frames);
        frames.clear();

        for (int i = 1; i <= 20; i++) {
            frames.add(new TextureRegion(new Texture("Animations/Enemies/robot11/die/robot11-die" + i + ".png")));
        }
        playerDieing = new Animation(1f / 20f, frames);
        frames.clear();

        for (int i = 0; i <= 10; i++) {
            frames.add(new TextureRegion(new Texture("Animations/Enemies/robot10/attack/robot10-attack" + i + ".png")));
        }
        playerAttack = new Animation(1f / 10f, frames);
        frames.clear();

        playerStand = new TextureRegion(new Texture("Animations/Enemies/robot11/attack/robot11-attack0.png"));

        defineEnemy();
        setBounds(10, 10, 350 / CrisisGame.PPM, 410 / CrisisGame.PPM);
        setRegion(playerStand);

        bulletHitCount = 0;
        bulletTimeCount = 0;
        setToDestroy = false;
        destroyed = false;
        fire = true;

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


        } else if (destroyed) {
            region = (TextureRegion) playerDieing.getKeyFrame(stateTimer, false);
            if (!walkingLeft && !region.isFlipX()) region.flip(true, false);
            if (walkingLeft && region.isFlipX()) region.flip(true, false);
            setRegion(region);
        }
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
                Region = playerAttack;
                break;
            default:
                Region = playerStand;
                break;
        }

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
        fdef.filter.categoryBits = CrisisGame.ENEMY_BIT;
        fdef.filter.maskBits = CrisisGame.GROUND_BIT | CrisisGame.BULLET_BIT;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(60 / CrisisGame.PPM, 185 / CrisisGame.PPM);


        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch) {
        if (!destroyed || stateTimer < 3)
            super.draw(batch);
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
