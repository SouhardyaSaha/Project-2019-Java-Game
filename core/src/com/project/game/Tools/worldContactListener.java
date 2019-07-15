package com.project.game.Tools;

import com.badlogic.gdx.physics.box2d.*;
import com.project.game.Characters.Enemy;
import com.project.game.Characters.MainPlayer;
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
            ///When player bullet hits enemy
            case CrisisGame.ENEMY_BIT | CrisisGame.BULLET_BIT:

                if(fixA.getFilterData().categoryBits == CrisisGame.BULLET_BIT){
                    ((Bullet)fixA.getUserData()).hitEnemy();
                    ((Enemy) fixB.getUserData()).enemyBulletHit();

                }
                else if(fixB.getFilterData().categoryBits == CrisisGame.BULLET_BIT){
                    ( (Bullet)fixB.getUserData()).hitEnemy();
                    ((Enemy) fixA.getUserData()).enemyBulletHit();
                }
                break;

            ///When bullet hits objects
            case CrisisGame.GROUND_BIT | CrisisGame.BULLET_BIT :

                if(fixA.getFilterData().categoryBits == CrisisGame.BULLET_BIT){
                    ((Bullet)fixA.getUserData()).hitEnemy();
                }
                else if(fixB.getFilterData().categoryBits == CrisisGame.BULLET_BIT){
                    ( (Bullet)fixB.getUserData()).hitEnemy();
                }
                break;

            //when enemy bullet hits player
            case CrisisGame.ENEMY_BULLET_BIT | CrisisGame.PLAYER_BIT:
                if(fixA.getFilterData().categoryBits == CrisisGame.ENEMY_BULLET_BIT){
                    ((EnemyBullet)fixA.getUserData()).hitEnemy();
                    ((MainPlayer) fixB.getUserData()).playerBulletHit();

                }
                else if(fixB.getFilterData().categoryBits == CrisisGame.ENEMY_BULLET_BIT){
                    ((EnemyBullet)fixB.getUserData()).hitEnemy();
                    ((MainPlayer) fixA.getUserData()).playerBulletHit();
                }
                break;
//            case CrisisGame.GROUND_BIT | CrisisGame.ENEMY_BULLET_BIT :
//
//                if(fixA.getFilterData().categoryBits == CrisisGame.ENEMY_BULLET_BIT){
//                    ((EnemyBullet)fixA.getUserData()).hitEnemy();
//                }
//                else if(fixB.getFilterData().categoryBits == CrisisGame.ENEMY_BULLET_BIT){
//                    ( (EnemyBullet)fixB.getUserData()).hitEnemy();
//                }
//                break;
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
