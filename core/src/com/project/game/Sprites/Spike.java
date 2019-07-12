package com.project.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.project.game.CrisisGame;
import com.project.game.Screens.PlayScreen;

public class Spike extends InteractiveTileObject {
    public Spike(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);

        fixture.setUserData(this);
        setCategoryFiler(CrisisGame.SPIKE_BIT);
    }
}
