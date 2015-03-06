package ro.xzya.bird.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import ro.xzya.bird.Game;

/**
 * Created by Xzya on 6/3/2015.
 */
public class Player extends B2DSprite {

    private int points;

    public Player(Body body) {
        super(body);

        Texture tex = Game.res.getTexture("player");
        TextureRegion[] sprites = TextureRegion.split(tex, 17, 12)[0];
        setAnimation(sprites, 1/12f);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (body.getPosition().y < 0f){
            float x = body.getPosition().x;
            body.setTransform(x, 1.5f, 0);
            float velocityx = body.getLinearVelocity().x;
            body.setLinearVelocity(velocityx, 0);
        }
    }

    public int getPoints() {
        return points;
    }

    public void addPoint() {
        points++;
    }
}
