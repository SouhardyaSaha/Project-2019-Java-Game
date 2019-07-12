package com.project.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.project.game.CrisisGame;
import com.project.game.Screens.PlayScreen;

public class Box extends InteractiveTileObject {
    public Box(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFiler(CrisisGame.BOX_BIT);
    }
}
