package com.project.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.project.game.CrisisGame;

public class SelectStage implements Screen {
    SpriteBatch batch;
    private Texture backGround;
    private Stage stage;
    private Skin skin;
    private Table tableStage1, tableStage2;
    private TextButton buttonStage1, buttonStage2;
    private BitmapFont fontWhite;
    CrisisGame game;

    public SelectStage(SpriteBatch batch, BitmapFont fontWhite, Skin skin, TextButton buttonStage1, TextButton buttonStage2, CrisisGame game) {
        this.batch = batch;
        this.fontWhite = fontWhite;
        this.skin = skin;
        this.buttonStage1 = buttonStage1;
        this.buttonStage2 = buttonStage2;
        this.game = game;
        backGround = new Texture("CrisisWarhead.jpg");
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        tableStage1 = new Table(skin);
        tableStage1.setBounds(800,400,Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/4);

        tableStage2 = new Table(skin);
        tableStage2.setBounds(800,200,Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/4);

        com.mygdx.game.Screens.MenuScreen.tableStage2= new Table(skin);
        com.mygdx.game.Screens.MenuScreen.tableStage2.setBounds(1120,650,Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/4);

       // com.mygdx.game.Screens.MenuScreen.tableBack.add(buttonBack);
        tableStage1.add(buttonStage1);
        tableStage2.add(buttonStage2);
        tableStage2.debug();
        tableStage1.debug();
        stage.addActor(tableStage1);
        stage.addActor(tableStage2);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backGround, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(delta);
        stage.draw();

        if (buttonStage1.isPressed()) {
            PlayScreen.level = 1;
            ((CrisisGame) Gdx.app.getApplicationListener()).setScreen(new PlayScreen(game));
        }

        if (buttonStage2.isPressed()) {
            PlayScreen.level = 2;
            ((CrisisGame) Gdx.app.getApplicationListener()).setScreen(new PlayScreen(game));
        }

    }

    @Override
    public void resize(int width, int height) {

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

    }
}
