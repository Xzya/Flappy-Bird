package ro.xzya.bird.handler;

import ro.xzya.bird.Game;
import ro.xzya.bird.gamestates.GameState;
import ro.xzya.bird.gamestates.PlayState;

/**
 * Created by Xzya on 6/3/2015.
 */
public class GameStateManager {

    private Game game;

    private GameState gameState;

    public static final int PLAY = 0;

    public GameStateManager(Game game) {
        this.game = game;

        setState(PLAY);
    }

    public void setState(int state) {
        if (gameState != null) gameState.dispose();
        if (state == PLAY) {
            gameState = new PlayState(this);
        }
    }

    public void update(float dt) {
        gameState.update(dt);
    }

    public void render() {
        gameState.render();
    }

    public Game getGame() {
        return game;
    }
}
