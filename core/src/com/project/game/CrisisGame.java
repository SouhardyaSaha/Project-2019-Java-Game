package com.project.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.MenuScreen;
import com.project.game.Screens.PlayScreen;

public class CrisisGame extends Game {

    public static final int v_WIDTH = 1600;
    public static final int v_HEIGHT = 1100;
	public static final float PPM = 200;

	///variables for identifying object types in box2d
	 public static final short GROUND_BIT = 1;
	 public static final short PLAYER_BIT = 2;
	 public static final short ENEMY_BOSS_BIT = 4;
	 public static final short ENEMY_BULLET_BIT = 8;
	 public static final short DOOR_BIT = 16;
	 public static final short BULLET_BIT = 32;
	 public static final short ENEMY_BIT = 64;
	 public static final short OBJECT_BIT = 128;

	 /// sprite batch for the game
	public SpriteBatch batch;

	public  static AssetManager manager;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();


		manager.load("Sound Effects/bensound-betterdays.mp3", Music.class);
		manager.load("Sound Effects/bensound-epic.mp3", Music.class);
		manager.load("Sound Effects/zapsplat_science_fiction_weapon_gun_shoot_003_32196.mp3", Sound.class);
		manager.finishLoading();

		setScreen(new PlayScreen(this));
//		setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
