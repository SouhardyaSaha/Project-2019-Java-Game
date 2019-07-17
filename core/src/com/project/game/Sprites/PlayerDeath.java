package com.project.game.Sprites;

import com.badlogic.gdx.math.Rectangle;
import com.project.game.CrisisGame;
import com.project.game.Screens.PlayScreen;

public class PlayerDeath extends InteractiveTileObject {
    public PlayerDeath(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(CrisisGame.ACID_BIT);
        setCategoryFilter(CrisisGame.OBJECT_BIT);
    }
}
