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

public class About implements Screen {
    SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private Table tableBack;
    private TextButton buttonBack;
    private BitmapFont fontWhite;
    CrisisGame game;
    private Texture backGround;

    public About(SpriteBatch batch, BitmapFont fontWhite, Skin skin, TextButton buttonBack, CrisisGame game) {
        this.batch = batch;
        this.fontWhite = fontWhite;
        this.skin = skin;
        this.buttonBack = buttonBack;
        this.game = game;
        backGround = new Texture("About.png");
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        tableBack = new Table(skin);
        tableBack.setBounds(1120,650,Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/4);

        tableBack.add(buttonBack);
        //tableBack.debug();
        stage.addActor(tableBack);
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

        if (buttonBack.isPressed()) {
            ((CrisisGame) Gdx.app.getApplicationListener()).setScreen(new com.mygdx.game.Screens.MenuScreen(game));
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
