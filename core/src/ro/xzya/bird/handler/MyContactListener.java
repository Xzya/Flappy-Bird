package ro.xzya.bird.handler;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import ro.xzya.bird.Game;
import ro.xzya.bird.gamestates.PlayState;

/**
 * Created by Xzya on 6/3/2015.
 */
public class MyContactListener implements ContactListener {

    private volatile boolean isHit;
    private volatile boolean isDead;
    private int points;

    public MyContactListener() {
        super();
    }


    @Override
    public void beginContact(Contact contact) {

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa.getUserData() != null && (fa.getUserData().equals("pipe") || fa.getUserData().equals("bottom_border"))) {
            if (Game.sound && !isHit) {
                Jukebox.play("hit");
            }
            if (!isHit && !isDead) {
                isHit = true;
            }
        }

        if (fb.getUserData() != null && (fb.getUserData().equals("pipe") || fb.getUserData().equals("bottom_border"))) {
            if (Game.sound && !isHit) {
                Jukebox.play("hit");
            }
            if (!isHit && !isDead) {
                isHit = true;
            }
        }

        if (fa.getUserData() != null && fa.getUserData().equals("sensor")) {
            if (!isHit) {
                points++;
                if (points % 10 > 0) {
                    PlayState.currentScoreAnim.incrementCurrentFrame();
                } else {
                    PlayState.currentScoreAnim2.incrementCurrentFrame();
                    PlayState.currentScoreAnim.incrementCurrentFrame();
                }
                if (Game.sound) {
                    Jukebox.play("point");
                }
            }
        }

        if (fb.getUserData() != null && fb.getUserData().equals("sensor")) {
            if (!isHit) {
                points++;
                if (points % 10 > 0) {
                    PlayState.currentScoreAnim.incrementCurrentFrame();
                } else {
                    PlayState.currentScoreAnim2.incrementCurrentFrame();
                    PlayState.currentScoreAnim.incrementCurrentFrame();
                }
                if (Game.sound) {
                    Jukebox.play("point");
                }
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

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean isHit) {
        this.isHit = isHit;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }
}
