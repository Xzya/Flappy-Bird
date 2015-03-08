package ro.xzya.bird.handler;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import ro.xzya.bird.Game;

/**
 * Created by Xzya on 8/3/2015.
 */
public class MenuGUI {
    private Texture titleTex;
    private Texture startTex;

    private volatile boolean pressedStart;

    private Rectangle start;
    private Rectangle title;

    public MenuGUI() {
        titleTex = Game.res.getTexture("title");
        startTex = Game.res.getTexture("start");
        start = new Rectangle(
                Game.V_WIDTH / 2 - startTex.getWidth() / 2,
                Game.V_HEIGHT / 4 - startTex.getHeight() / 2,
                startTex.getWidth(),
                startTex.getHeight()
        );
        title = new Rectangle(
                Game.V_WIDTH / 2 - titleTex.getWidth() / 2,
                Game.V_HEIGHT - Game.V_HEIGHT / 4 - titleTex.getHeight() / 2,
                titleTex.getWidth(),
                titleTex.getHeight()

        );
    }

    public void render(SpriteBatch sb) {
        sb.draw(
                titleTex,
                title.getX(),
                title.getY()
        );
        sb.draw(
                startTex,
                start.getX(),
                start.getY()
        );

    }

    public boolean isPressedStart() {
        return pressedStart;
    }

    public void setPressedStart(boolean pressedStart) {
        this.pressedStart = pressedStart;
    }

    public void dispose() {
        titleTex.dispose();
        startTex.dispose();
    }

    public Rectangle getStart() {
        return start;
    }
}
