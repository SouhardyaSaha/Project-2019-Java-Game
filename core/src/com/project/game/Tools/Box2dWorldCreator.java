package com.project.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.project.game.CrisisGame;
import com.project.game.Enemies.Dog;
import com.project.game.Enemies.MiddleEnemy;
import com.project.game.Enemies.RoboGirl;
import com.project.game.Screens.PlayScreen;
import com.project.game.Sprites.*;
import com.badlogic.gdx.physics.box2d.World;

public class Box2dWorldCreator {

    private Array<RoboGirl> robogirls;
    private Array<Dog> dogs;
    private Array<MiddleEnemy> middleEnemies;
    public Box2dWorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        //creating ground body fixtures
        for (MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Ground(screen, rect);
        }

        //creating door bodies/fixtures
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Door(screen, rect);
        }
        ///creating death after falling down the ground
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new PlayerDeath(screen, rect);
        }

        //creating robogirl body fixtures
        robogirls = new Array<RoboGirl>();
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            robogirls.add(new RoboGirl(screen, rect.getX() / CrisisGame.PPM, rect.getY() / CrisisGame.PPM));
        }

        dogs = new Array<Dog>();
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            dogs.add(new Dog(screen, rect.getX() / CrisisGame.PPM, rect.getY() / CrisisGame.PPM));
        }

        middleEnemies = new Array<MiddleEnemy>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            middleEnemies.add(new MiddleEnemy(screen, rect.getX() / CrisisGame.PPM, rect.getY() / CrisisGame.PPM));
        }

    }

    public Array<RoboGirl> getRobogirls() {
        return robogirls;
    }

    public Array<Dog> getDogs() {
        return dogs;
    }

    public Array<MiddleEnemy> getMiddleEnemies() {
        return middleEnemies;
    }
}
