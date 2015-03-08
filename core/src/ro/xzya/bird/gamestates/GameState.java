package ro.xzya.bird.gamestates;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ro.xzya.bird.Game;
import ro.xzya.bird.handler.GameStateManager;

/**
 * Created by Xzya on 6/3/2015.
 */
public abstract class GameState {

    public boolean debug = false;

    protected GameStateManager gsm;
    protected Game game;

    protected SpriteBatch sb;
    protected OrthographicCamera cam;
    protected OrthographicCamera hudcam;

    protected GameState(GameStateManager gsm){
        this.gsm = gsm;

        game = gsm.getGame();
        sb = game.getSb();
        cam = game.getCam();
        hudcam = game.getHudcam();

    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();

}
