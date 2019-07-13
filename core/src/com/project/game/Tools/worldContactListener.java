package com.project.game.Tools;

import com.badlogic.gdx.physics.box2d.*;
import com.project.game.Characters.Enemy;
import com.project.game.CrisisGame;

import java.awt.event.ContainerListener;

public class worldContactListener implements ContactListener {

//    public worldContactListener(Contact contact) {
//
//    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cdef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cdef){
            case CrisisGame.ENEMY_BIT | CrisisGame.BULLET_BIT:

                if(fixA.getFilterData().categoryBits == CrisisGame.BULLET_BIT){
                    ((Bullet)fixA.getUserData()).hitEnemy();
                    ((Enemy) fixB.getUserData()).enemyBulletHit();

                }
                else if(fixB.getFilterData().categoryBits == CrisisGame.BULLET_BIT){
                    ( (Bullet)fixB.getUserData()).hitEnemy();
                    ((Enemy) fixA.getUserData()).enemyBulletHit();
                }

        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
