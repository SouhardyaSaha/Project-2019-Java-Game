package com.project.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.project.game.CrisisGame;
import com.project.game.Screens.PlayScreen;

public abstract class InteractiveTileObject {

    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;

    protected Fixture fixture;

    public InteractiveTileObject(PlayScreen screen, Rectangle bounds) {
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;
        
        BodyDef bdef = new BodyDef();
        FixtureDef fdef =  new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2)/ CrisisGame.PPM, (bounds.getY() + bounds.getHeight() / 2)/ CrisisGame.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / CrisisGame.PPM,  bounds.getHeight() / 2 / CrisisGame.PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = CrisisGame.OBJECT_BIT;
        body.createFixture(fdef);
        fixture = body.createFixture(fdef);
    }

    public void setCategoryFilter(short filerBit){
        Filter filter = new Filter();
        filter.categoryBits = filerBit;
        fixture.setFilterData(filter);
    }
}
