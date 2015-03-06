package ro.xzya.bird.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import ro.xzya.bird.Game;
import ro.xzya.bird.entities.Player;
import ro.xzya.bird.handler.GameStateManager;
import ro.xzya.bird.handler.MyContactListener;
import ro.xzya.bird.handler.MyInput;

import static ro.xzya.bird.handler.B2DVars.BIT_PIPE;
import static ro.xzya.bird.handler.B2DVars.BIT_PLAYER;
import static ro.xzya.bird.handler.B2DVars.PPM;

/**
 * Created by Xzya on 6/3/2015.
 */
public class PlayState extends GameState {

    private World world;
    private Box2DDebugRenderer b2dr;

    private OrthographicCamera b2dCam;

    private MyContactListener cl;

    private Player player;

    private Texture background;
    private Sprite ground;
    private float scrollTimer;
    private float scrollTime;

//    private ParallaxBackground background;

//    private HUD hud;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        //set up world
//        world = new World(new Vector2(0, -9.81f), true);
        world = new World(new Vector2(0, -6.81f), true);
        cl = new MyContactListener();
        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();

        //create player
        createPlayer();

        //create background
//        Texture bg = Game.res.getTexture("background");
//        Texture ground = Game.res.getTexture("ground");
//        ground.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
//        TextureRegion layer1 = new TextureRegion(bg);
//        TextureRegion layer2 = new TextureRegion(ground);
//        ParallaxLayer l1 = new ParallaxLayer(layer1, 0, 0);
//        ParallaxLayer l2 = new ParallaxLayer(layer2, 3f, 0);
//        ParallaxLayer[] layers = {l1, l2};
//        background = new ParallaxBackground(layers, cam, sb);
        background = Game.res.getTexture("background");
        Texture gr = Game.res.getTexture("ground");
        gr.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        ground = new Sprite(gr, 0, 0, 154, 56);
        scrollTime = 1;
        scrollTimer = 0;



        //set up box2d cam
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);
        b2dCam.update();

        //set up hud
//        hud = new HUD(player);


    }

    @Override
    public void handleInput() {

        //bird jump
        if (MyInput.isPressed(MyInput.BUTTON1)) {
//            player.getBody().applyForceToCenter(0, 250, true);
            player.getBody().setLinearVelocity(player.getBody().getLinearVelocity().x, 0);
            player.getBody().applyForceToCenter(0, 160, true);
        }

    }

    @Override
    public void update(float dt) {

        //handle input
        handleInput();

        //is hit
//        if (cl.isHit()) {
//            player.setDead();
//        }

        //update world
        world.step(dt, 6, 2);

        //update player
        player.update(dt);

        //update pipes
        //TODO add pipes

        //update background
//        background.moveX(dt);
        scrollTimer += dt;
        if (scrollTimer > scrollTime) {
            scrollTimer = 0f;
        }
        ground.setU(scrollTimer);
        ground.setU2(scrollTimer+1);

    }

    @Override
    public void render() {

        //clear
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //set camera to follow player
        cam.position.set(
                player.getPosition().x * PPM,
                Game.V_HEIGHT / 2,
                0
        );
        cam.update();

        //render background
//        background.render();
        sb.setProjectionMatrix(hudcam.combined);
        sb.begin();
        sb.draw(background, 0, 0, background.getWidth(), background.getHeight());
        ground.draw(sb);
        sb.end();


        //render player
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);

        //render pipes
        //TODO add render pipes

        //render hud
        //TODO add render hud
//        sb.setProjectionMatrix(hudcam.combined);
//        sb.begin();
//        hud.render(sb);
//        sb.end();

        if (debug) {
            //render box2d world
            b2dr.render(world, b2dCam.combined);
        }

    }

    @Override
    public void dispose() {
        sb.dispose();
    }

    private void createPlayer() {

        //define vars
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.position.set(80/ PPM, 80 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.linearVelocity.set(1f, 0);
        Body body = world.createBody(bdef);

        shape.setAsBox(17/PPM, 12/PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_PLAYER;
        fdef.filter.maskBits = BIT_PIPE;
        body.createFixture(fdef).setUserData("player");

        //create player
        player = new Player(body);

        body.setUserData(player);

    }
}
