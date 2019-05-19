package com.project.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.project.game.CrisisGame;
import com.project.game.Scenes.Hud;
import com.project.game.Sprites.MainPlayer;
import com.project.game.Tools.Box2dWorldCreator;


public class PlayScreen implements Screen {

    private CrisisGame game;

    private OrthographicCamera gameCam;
    private Viewport gamePort;

    //HUD
    private Hud hud;

    ///Tiled Map Variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    ///Box 2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    ///MainPlayer
    private MainPlayer mainPlayer;

    public  PlayScreen(CrisisGame game){
        this.game = game;

        ///Camera to follow the player through the game world
        gameCam = new OrthographicCamera();

        ///creating viewport to maintain ratio
        gamePort = new FitViewport(CrisisGame.v_WIDTH / CrisisGame.PPM, CrisisGame.v_HEIGHT /  CrisisGame.PPM , gameCam);

        ///HUD for scores/timers/level info
        hud = new Hud(game.batch);

        //Load map and setup map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Maps/Crisis.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / CrisisGame.PPM);

        //set the gamecam to start of the map
        gameCam.position.set(gamePort.getWorldWidth() / 2 , gamePort.getWorldHeight() / 2 , 0);

        //Box2d World setting the gravity of
        world = new World(new Vector2(0,-15), true);

        //allow the debug lines of box2d
        b2dr = new Box2DDebugRenderer();

        new Box2dWorldCreator(world,map);

        mainPlayer = new MainPlayer(world);


    }

    @Override
    public void show() {

    }

    public void handleInput(float dt){

        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            mainPlayer.b2body.applyLinearImpulse(new Vector2(0, 9f), mainPlayer.b2body.getWorldCenter(),true);
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
            mainPlayer.b2body.applyLinearImpulse(new Vector2(0, 4f), mainPlayer.b2body.getWorldCenter(),true);
//            gameCam.position.y -= 100 * dt;
        }


    }

    public void update(float dt){
        //handle user input first
        handleInput(dt);

        world.step(1/60f, 6, 2);

        gameCam.position.x = mainPlayer.b2body.getPosition().x;

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

        //render gameMap
        renderer.render();

        //renderer box2DDebugelines
        b2dr.render(world, gameCam.combined);

        //to draw what HUD camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }

    @Override
    public void resize(int width, int height) {

        gamePort.update(width , height );
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

    }
}
