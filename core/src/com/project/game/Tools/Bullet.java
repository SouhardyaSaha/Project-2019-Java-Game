package com.project.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.project.game.CrisisGame;
import com.project.game.Screens.PlayScreen;

import javax.print.DocFlavor;

public class Bullet extends Sprite{
    private static final int speed = 12;
    private static Texture texture;
    private TextureRegion bulletRegion;
    private boolean setToDestroy;
    private boolean destroyed;
//    private static Sprite sprite;
    private boolean right;

    public Body b2body;
    private World world;
    private PlayScreen screen;

    private float x, y;
    public boolean remove = false;

    public Bullet(PlayScreen screen, float x, float y, boolean fireRight){
        this.x = x + 0.5f;
        this.y = y + 0.25f;
        right = fireRight;

        this.screen = screen;
        this.world = screen.getWorld();

        if(!fireRight) texture = new Texture("fire_blue.png");
        else texture = new Texture("fire_blueflip.png");

        bulletRegion = new TextureRegion(texture);
//            sprite = new Sprite(texture);
//            if(!right) sprite.flip(true, false);
        defineBulletBody();
        setBounds(0, 0, 0.8f, 0.5f);
        setRegion(bulletRegion);

        setToDestroy = false;
        destroyed = false;
    }

    public void update(float dt){

//        if (!right)  x += speed * dt;
//        else x -= speed * dt;

        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new Texture("EmptyBullet.png"));
        }
        else if (!destroyed){

            ///box2d Body for bullet update
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2f);
            setRegion(bulletRegion);

            if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)){
                if(!right)
                    b2body.applyLinearImpulse(new Vector2(10, 0), b2body.getWorldCenter(), true);
                else
                    b2body.applyLinearImpulse(new Vector2(-10, 0), b2body.getWorldCenter(), true);
            }
        }

        if(x > 1600 || x < 0 ){
            remove = true;
        }
    }

    public void render(SpriteBatch batch){
//        sprite.draw(batch);
        batch.draw(texture, x, y, 0.8f, 0.5f);
    }

    protected void defineBulletBody() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x,  y);
//        bdef.position.set(320/CrisisGame.PPM,  700/CrisisGame.PPM );
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.gravityScale = 0;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(50/ CrisisGame.PPM);
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(60/ CrisisGame.PPM,165/ CrisisGame.PPM);


        fdef.shape = shape;
        fdef.restitution = 0;
//        fdef.friction = 0;
        fdef.filter.categoryBits = CrisisGame.BULLET_BIT;
        fdef.filter.maskBits =    CrisisGame.GROUND_BIT
                | CrisisGame.BARREL_BIT | CrisisGame.ENEMY_BIT
                | CrisisGame.BOX_BIT  ;
        b2body.createFixture(fdef).setUserData(this);

//        EdgeShape bulletHitShape = new EdgeShape();
//        Vector2[] vertice = new Vector2[2];
//
//        vertice[0] = new Vector2(getWidth()+2, getHeight()).scl(1 / CrisisGame.PPM);
//        vertice[1] = new Vector2(getWidth()+2, getHeight() - 3).scl(1 / CrisisGame.PPM);
//        bulletHitShape.set(vertice);
//        fdef.shape = bulletHitShape;
//        fdef.filter.categoryBits = CrisisGame.BULLET_BIT;
//        fdef.filter.maskBits =    CrisisGame.GROUND_BIT
//                | CrisisGame.BARREL_BIT | CrisisGame.ENEMY_BIT
//                | CrisisGame.BOX_BIT  ;
//        b2body.createFixture(fdef);

    }
//
    public void hitEnemy(){
        setToDestroy = true;
    }
    public void dispose(){
        texture.dispose();
    }

}