package ro.xzya.bird.handler;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by Xzya on 6/3/2015.
 */
public class MyInputProcessor extends InputAdapter {

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            MyInput.setKey(MyInput.BUTTON1, true);
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            MyInput.setKey(MyInput.BUTTON1, false);
        }

        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        MyInput.setKey(MyInput.BUTTON1, true);

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        MyInput.setKey(MyInput.BUTTON1, false);

        return true;
    }
}
