package ro.xzya.bird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ro.xzya.bird.handler.Content;
import ro.xzya.bird.handler.GameStateManager;
import ro.xzya.bird.handler.MyInput;
import ro.xzya.bird.handler.MyInputProcessor;

public class Game extends ApplicationAdapter {

    public static boolean isMobile = false;

    public static final String TITLE = "Flappy Bird";
//    public static final int V_WIDTH = 180;
//    public static final int V_HEIGHT = 320;
    public static final int V_WIDTH = 144;
    public static final int V_HEIGHT = 256;
    public static final int SCALE = 2;

	private SpriteBatch sb;
    private OrthographicCamera cam;
    private OrthographicCamera hudcam;

    private GameStateManager gsm;

    public static Content res;

    public Game(String platform){
        if (platform.equals("mobile")) {
            isMobile = true;
        }
    }

	@Override
	public void create () {

        Gdx.input.setInputProcessor(new MyInputProcessor());

		sb = new SpriteBatch();

        res = new Content();
        loadContents();

        cam = new OrthographicCamera();
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
        cam.update();

        hudcam = new OrthographicCamera();
        hudcam.setToOrtho(false, V_WIDTH, V_HEIGHT);
        hudcam.update();

        gsm = new GameStateManager(this);

	}

	@Override
	public void render () {
//		Gdx.gl.glClearColor(0, 0, 0, 1);

        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render();

        MyInput.update();

	}

    private void loadContents() {
        res.loadTexture("images/background.png", "background");
        res.loadTexture("images/player.png", "player");
        res.loadTexture("images/ground.png", "ground");
    }

    public SpriteBatch getSb() {
        return sb;
    }

    public OrthographicCamera getCam() {
        return cam;
    }

    public OrthographicCamera getHudcam() {
        return hudcam;
    }
}
