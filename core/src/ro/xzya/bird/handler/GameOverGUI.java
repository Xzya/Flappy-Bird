package ro.xzya.bird.handler;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Xzya on 8/3/2015.
 */
public class GameOverGUI {


    private volatile boolean pressedOK;
    private volatile boolean pressedMenu;

    private Rectangle ok;
    private Rectangle menu;

    public GameOverGUI(float width, float height) {
        ok = new Rectangle(width + 6, height, 40 , 14);
        menu = new Rectangle(width + 67, height, 40 , 14);
    }

    public Rectangle getOk() {
        return ok;
    }

    public Rectangle getMenu() {
        return menu;
    }

    public boolean isPressedOK() {
        return pressedOK;
    }

    public boolean isPressedMenu() {
        return pressedMenu;
    }
}
