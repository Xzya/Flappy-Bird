package ro.xzya.bird.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import ro.xzya.bird.Game;

/**
 * Created by Xzya on 7/3/2015.
 */
public class Pipe extends B2DSprite {

    private static final Texture tex = Game.res.getTexture("pipe");

    public Pipe(Body body) {
        super(body);

        setAnimation(new TextureRegion[]{new TextureRegion(tex)}, 1/1f);
    }

}
