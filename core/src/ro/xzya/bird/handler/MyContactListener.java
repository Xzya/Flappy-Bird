package ro.xzya.bird.handler;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Created by Xzya on 6/3/2015.
 */
public class MyContactListener implements ContactListener {

    private boolean isHit;

    public MyContactListener() {
        super();
    }


    @Override
    public void beginContact(Contact contact) {

        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa.getUserData() != null && fa.getUserData().equals("player")) {
            isHit = true;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("player")) {
            isHit = true;
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
}
