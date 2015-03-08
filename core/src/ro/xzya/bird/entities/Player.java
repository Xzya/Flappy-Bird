package ro.xzya.bird.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

import ro.xzya.bird.Game;

/**
 * Created by Xzya on 6/3/2015.
 */
public class Player extends B2DSprite {

    public Player(Body body) {
        super(body);

        Texture tex = Game.res.getTexture("player");
        TextureRegion[] sprites = TextureRegion.split(tex, 18, 12)[0];
        setAnimation(sprites, 1/12f);
    }

}
