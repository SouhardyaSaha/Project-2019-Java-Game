package com.project.game.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.project.game.Enemies.EnemyBoss;
import com.project.game.CrisisGame;
import com.project.game.Enemies.Type1;
import com.project.game.Scenes.Hud;
import com.project.game.Characters.MainPlayer;
import com.project.game.Tools.Box2dWorldCreator;
import com.project.game.Tools.worldContactListener;
import com.badlogic.gdx.graphics.GL20;


public class PlayScreen implements Screen, InputProcessor {

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
    private Type1 type1;
    private EnemyBoss boss;

    ///Music
    private Music music;

    public int level;


    public PlayScreen(CrisisGame game) {

        this.game = game;

        ///Camera to follow the player through the game world
        gameCam = new OrthographicCamera();

        ///creating viewport to maintain ratio
        gamePort = new FillViewport(CrisisGame.v_WIDTH / CrisisGame.PPM * 1.8f, CrisisGame.v_HEIGHT / CrisisGame.PPM * 1.4f, gameCam);

        ///HUD for scores/timers/level info
        hud = new Hud(game.batch);


        //Load map and setup map renderer
        mapLoader = new TmxMapLoader();
        level = 1;
        switch (level) {
            case 1:
                backgroundImage = new Texture("BG apocalyptic 3.png");
                music = CrisisGame.manager.get("Sound Effects/bensound-betterdays.mp3", Music.class);
                map = mapLoader.load("Maps/Crisis.tmx");
                break;
            case 2:
                backgroundImage = new Texture("BG alien 4.jpg");
                map = mapLoader.load("Maps/CrisisBoss.tmx");
                music = CrisisGame.manager.get("Sound Effects/bensound-epic.mp3", Music.class);
                break;
            default:
                backgroundImage = new Texture("BG apocalyptic 3.png");
                map = mapLoader.load("Maps/Crisis.tmx");
                break;
        }

        renderer = new OrthogonalTiledMapRenderer(map, 1 / CrisisGame.PPM);

        //set the gamecam to start of the map
        gameCam.position.set(gamePort.getWorldWidth() / 1.5f, gamePort.getWorldHeight() / 1.7f, 0);

        //Box2d World setting the gravity
        world = new World(new Vector2(0, -15), true);

        //allow the debug lines of box2d
        b2dr = new Box2DDebugRenderer();

        new Box2dWorldCreator(this);

        switch (level) {
            case 1:
                //creating main character
                mainPlayer = new MainPlayer(this, 6500, 700);

                ///creating enimies
                type1 = new Type1(this, 2000, 700);
                break;
            case 2:
                //creating main character
                mainPlayer = new MainPlayer(this, 2000, 700);
                boss = new EnemyBoss(this, 4000, 700f);
                ///creating enimies
                break;
        }

        ///for collision detection
        world.setContactListener(new worldContactListener());

        ///music controls
        music.setLooping(true);
        music.play();
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }


    public void update(float dt) {
        ///ajaira
        Gdx.input.setInputProcessor(this);

        world.step(1 / 60f, 6, 2);
        float playerPos;
        switch (level) {
            case 1:
                ///main player update
                mainPlayer.update(dt);
                ///enemy update
                type1.update(dt);
                playerPos = mainPlayer.b2body.getPosition().x;
                if (playerPos > 1300 / CrisisGame.PPM && playerPos < 7100 / CrisisGame.PPM)
                    gameCam.position.x = mainPlayer.b2body.getPosition().x;
                break;
            case 2:
                ///main player update
                mainPlayer.update(dt);
                boss.update(dt);
                playerPos = mainPlayer.b2body.getPosition().x;
                if (playerPos > 1361 / CrisisGame.PPM && playerPos < 6500 / CrisisGame.PPM)
                    gameCam.position.x = mainPlayer.b2body.getPosition().x;
                break;
        }

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
        game.batch.draw(backgroundImage, 0, 0, 1600 / 2f, 1100 / 2f);
        game.batch.end();

        //render gameMap
        renderer.render();

        //renderer box2DDebugelines
        b2dr.render(world, gameCam.combined);

        ///
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        switch (level) {
            case 1:
                mainPlayer.draw(game.batch);
                type1.draw(game.batch);
                break;
            case 2:
                boss.draw(game.batch);
                mainPlayer.draw(game.batch);
                break;
        }
        game.batch.end();

        //to draw what HUD camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }

    @Override
    public void resize(int width, int height) {

        gamePort.update(width, height);
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
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
        System.out.println(screenX + " " + (Gdx.graphics.getHeight() - 1 - screenY));
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
