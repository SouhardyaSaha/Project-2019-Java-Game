package com.project.game.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.project.game.Characters.EnemyOne;
import com.project.game.CrisisGame;
import com.project.game.Scenes.Hud;
import com.project.game.Characters.MainPlayer;
import com.project.game.Tools.Box2dWorldCreator;
import com.project.game.Tools.Bullet;
import com.project.game.Tools.worldContactListener;
import com.badlogic.gdx.graphics.GL20;

import java.util.ArrayList;


public class PlayScreen implements Screen, InputProcessor{

    //Main Player Bullet
    ArrayList<Bullet> bullets;

    // backGround
    private Texture backgroundImage;


    ///Game Reference
    private CrisisGame game;

    private TextureAtlas atlas;

    private OrthographicCamera gameCam;
    private Viewport gamePort;

    //HUD
    private Hud hud;

    ///Tiled Map Variables
    private TiledMap map;
    private TmxMapLoader mapLoader;
    private OrthogonalTiledMapRenderer renderer;

    ///Box 2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    Contact contact;

    ///MainPlayer
    public MainPlayer mainPlayer;

    ///Enemy
    private EnemyOne enemyTest;


    public  PlayScreen(CrisisGame game){
        ///for animation
        atlas = new TextureAtlas("Animation/Main player/Walk/Main Player Walk.pack");

        this.game = game;

        ///Camera to follow the player through the game world
        gameCam = new OrthographicCamera();

        ///creating viewport to maintain ratio
        gamePort = new FillViewport(CrisisGame.v_WIDTH / CrisisGame.PPM * 1.6f, CrisisGame.v_HEIGHT /  CrisisGame.PPM * 1.4f, gameCam);

        ///HUD for scores/timers/level info
        hud = new Hud(game.batch);

        backgroundImage = new Texture("BG apocalyptic 3.png");

        //Load map and setup map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Maps/Crisis.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / CrisisGame.PPM);

        //set the gamecam to start of the map
        gameCam.position.set(gamePort.getWorldWidth()  / 1.5f , gamePort.getWorldHeight() / 1.7f , 0);

        //Box2d World setting the gravity of
        world = new World(new Vector2(0,-15), true);

        //allow the debug lines of box2d
        b2dr = new Box2DDebugRenderer();

        new Box2dWorldCreator(this);

        //creating main character
        mainPlayer = new MainPlayer(this);

        ///creating enimies
        enemyTest = new EnemyOne(this, 2000, 700);

        ///creating bullets
        bullets = new ArrayList<Bullet>();

        ///for collision detection
        world.setContactListener(new worldContactListener());


    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt){

        if(Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)){
            float bulletX = mainPlayer.b2body.getPosition().x ;
            float bulletY = mainPlayer.b2body.getPosition().y;
            bullets.add(new Bullet(this, bulletX, bulletY, mainPlayer.walkingLeft));
            System.out.println("Shoot");
        }

        if(mainPlayer.b2body.getLinearVelocity().y == 0 &&  Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            mainPlayer.b2body.applyLinearImpulse(new Vector2(0, 10f), mainPlayer.b2body.getWorldCenter(),true);
//            gameCam.position.y += 100 * dt;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && mainPlayer.b2body.getLinearVelocity().x <= 2){
            mainPlayer.b2body.applyLinearImpulse(new Vector2(0.3f, 0), mainPlayer.b2body.getWorldCenter(),true);
//            gameCam.position.x += 100 * dt;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && mainPlayer.b2body.getLinearVelocity().x >= -2){
            mainPlayer.b2body.applyLinearImpulse(new Vector2(-0.3f, 0), mainPlayer.b2body.getWorldCenter(),true);
//            gameCam.position.x -= 100 * dt;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            mainPlayer.b2body.applyLinearImpulse(new Vector2(0, -4f), mainPlayer.b2body.getWorldCenter(),true);
//            gameCam.position.y -= 100 * dt;
        }

    }

    public void update(float dt){
        ///ajaira
        Gdx.input.setInputProcessor(this);
        //handle user input first
        handleInput(dt);

        world.step(1/60f, 6, 2);

        ///main player update
        mainPlayer.update(dt);
        ///enemy update
        enemyTest.update(dt);

        ///bullets update
        ArrayList<Bullet> bulletToRemove = new ArrayList<Bullet>();
        for(Bullet bullet : bullets){
            bullet.update(dt);
            if(bullet.remove){
                bulletToRemove.add(bullet);
            }
        }
        bullets.removeAll(bulletToRemove);

        ///attach gameCam with players x co ordinate
        gameCam.position.x = mainPlayer.b2body.getPosition().x ;
//        gameCam.position.y = mainPlayer.b2body.getPosition().y;

        //update the gamecam with correct coordinates after changes
        gameCam.update();

        //render what the only gamecam sees
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        ///clear the game screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ///render background
        game.batch.begin();
        game.batch.draw(backgroundImage, 0,0, 1600/2f, 1100/2f);
        game.batch.end();

        //render gameMap
        renderer.render();

        //renderer box2DDebugelines
        b2dr.render(world, gameCam.combined);

        ///
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        mainPlayer.draw(game.batch);
        enemyTest.draw(game.batch);
        game.batch.end();

        ///bullet rendering
        game.batch.begin();
        for (Bullet bullet : bullets){
            bullet.draw(game.batch);
        }
        game.batch.end();

        //to draw what HUD camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }

    @Override
    public void resize(int width, int height) {

        gamePort.update(width , height );
    }

    public TiledMap getMap(){
        return map;
    }
    public  World getWorld(){
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
            return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        System.out.println(screenX +" "+(Gdx.graphics.getHeight() - 1 -screenY));
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
