package ro.xzya.bird.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import ro.xzya.bird.Game;
import ro.xzya.bird.entities.Pipe;
import ro.xzya.bird.entities.Player;
import ro.xzya.bird.handler.Animation;
import ro.xzya.bird.handler.GameOverGUI;
import ro.xzya.bird.handler.GameStateManager;
import ro.xzya.bird.handler.Jukebox;
import ro.xzya.bird.handler.MenuGUI;
import ro.xzya.bird.handler.MyContactListener;
import ro.xzya.bird.handler.MyInput;

import static ro.xzya.bird.handler.B2DVars.BIT_BORDER;
import static ro.xzya.bird.handler.B2DVars.BIT_PIPE;
import static ro.xzya.bird.handler.B2DVars.BIT_PLAYER;
import static ro.xzya.bird.handler.B2DVars.BIT_POINT;
import static ro.xzya.bird.handler.B2DVars.PPM;

/**
 * Created by Xzya on 6/3/2015.
 */
public class PlayState extends GameState {

    private World world;
    private Box2DDebugRenderer b2dr;

    private ShapeRenderer sr;

    private static MyContactListener cl;

    private Player player;

    private Texture background;
    private Sprite ground;
    private float scrollTimer;
    private float scrollTime;

    private Pipe pipe1;
    private float pipe1X;
    private float pipe1Y;

    private Pipe pipe2;
    private float pipe2X;
    private float pipe2Y;


    public static Animation currentScoreAnim;
    public static Animation currentScoreAnim2;
    public static Animation bestScoreAnim;
    public static Animation bestScoreAnim2;
    public static Animation medals;

    private float menuTimer;
    private float menuTime;

    private Texture game_over;
    private GameOverGUI game_over_gui;

    private Texture get_ready;
    private MenuGUI start_gui;

    private Body border;
    private int best;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        sr = new ShapeRenderer();

        //set up world
//        world = new World(new Vector2(0, -9.81f), true);
        world = new World(new Vector2(0, -6.81f), true);
        cl = new MyContactListener();
        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();

        //set up start menu
        start_gui = new MenuGUI();
        best = 0;

        //create background
        background = Game.res.getTexture("background");
        Texture gr = Game.res.getTexture("ground");
        gr.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        ground = new Sprite(gr, 0, 0, 154, 56);
        scrollTime = 1;
        scrollTimer = 0;


        //set up game_over menu
        game_over = Game.res.getTexture("game_over");
        menuTimer = 0;
        menuTime = 1;

        //create game over gui
        game_over_gui = new GameOverGUI(Game.V_WIDTH / 2 - game_over.getWidth() / 2, Game.V_HEIGHT / 2 - game_over.getHeight() / 2);

        //create best points animation
        createBestPointsAnimation();

        //create medals
        createMedals();

    }

    @Override
    public void handleInput() {

        if (start_gui.isPressedStart()) {
            //bird jump
            if (!cl.isHit()) {
                if (MyInput.isPressed(MyInput.BUTTON1) ||
                        Gdx.input.justTouched()) {
                    float x = player.getBody().getLinearVelocity().x;
                    player.getBody().setLinearVelocity(x, 0f);
//                    player.getBody().applyForceToCenter(0, 130, true);
                    player.getBody().applyLinearImpulse(new Vector2(0f ,2.38f), new Vector2(0,0) , true);

                    if (Game.sound) {
                        Jukebox.play("wing");
                    }

                }
            }

            if (cl.isDead()) {
                if (Gdx.input.justTouched()) {
                    float x = Gdx.input.getX();
                    float y = Gdx.input.getY();

                    Vector3 coordinates = hudcam.unproject(new Vector3(x, y, 0));

                    if (game_over_gui.getOk().contains(coordinates.x, coordinates.y)) {
                        reset();
                    }
                    if (game_over_gui.getMenu().contains(coordinates.x, coordinates.y)) {
                        start_gui.setPressedStart(false);
                    }
                }
            }
        } else {
            if (Gdx.input.justTouched()) {
                float x = Gdx.input.getX();
                float y = Gdx.input.getY();

                Vector3 coordinates = hudcam.unproject(new Vector3(x, y, 0));

                if (start_gui.getStart().contains(coordinates.x, coordinates.y)) {
                    start_gui.setPressedStart(true);
                    reset();
                }
            }
        }

    }

    @Override
    public void update(float dt) {

        //handle input
        handleInput();

        //update world
        world.step(dt, 6, 2);

        //update background
        scrollTimer += dt;
        if (scrollTimer > scrollTime) {
            scrollTimer = 0f;
        }
        ground.setU(scrollTimer);
        ground.setU2(scrollTimer + 1);

        //update player
        if (start_gui.isPressedStart()) {
            player.update(dt);

            if (player.getPosition().x > (pipe1X + Game.V_WIDTH) / PPM) {
                pipe1X += 300;
                pipe1Y = generateHeight();
                pipe1 = createPipe(pipe1, pipe1X, pipe1Y);
            }
            if (player.getPosition().x > (pipe2X + Game.V_WIDTH) / PPM) {
                pipe2X += 300;
                pipe2Y = generateHeight();
                pipe2 = createPipe(pipe2, pipe2X, pipe2Y);
            }

            //is hit
            if (cl.isHit() && !cl.isDead()) {

                border.setLinearVelocity(0, 0);

                player.getBody().setLinearVelocity(0, player.getBody().getLinearVelocity().y);
                menuTimer += dt;
                if (menuTimer > menuTime) {
                    menuTimer = 0;
                    cl.setDead(true);

                    //set best score and medals
                    int currentPoints = cl.getPoints();
                    if (currentPoints > best) {
                        best = currentPoints;
                        medals.setCurrentFrame(2);
                        bestScoreAnim.setCurrentFrame(currentScoreAnim.getCurrentFrame());
                        bestScoreAnim2.setCurrentFrame(currentScoreAnim2.getCurrentFrame());
                    } else if ((float)currentPoints / best > 0.5) {
                        medals.setCurrentFrame(1);
                    } else {
                        medals.setCurrentFrame(0);
                    }
                }
            }

        }

    }

    @Override
    public void render() {

        //clear
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render background
        sb.setProjectionMatrix(hudcam.combined);
        sb.begin();
        sb.draw(background, 0, 0, background.getWidth(), background.getHeight());
        ground.draw(sb);
        sb.end();

        if (start_gui.isPressedStart()) {
            //set camera to follow player
            cam.position.set(
                    player.getPosition().x * PPM + Game.V_WIDTH / 4,
                    Game.V_HEIGHT / 2,
                    0
            );
            cam.update();

            //render player
            sb.setProjectionMatrix(cam.combined);
            player.render(sb);

            //render pipes
            pipe1.render(sb);
            pipe2.render(sb);

            //render hud
            sb.setProjectionMatrix(hudcam.combined);
            sb.begin();
            if (cl.getPoints() >= 10) {
                sb.draw(currentScoreAnim2.getFrame(), Game.V_WIDTH / 2 - 17, Game.V_HEIGHT - Game.V_HEIGHT / 4, 14, 20);
                sb.draw(currentScoreAnim.getFrame(), Game.V_WIDTH / 2 + 3, Game.V_HEIGHT - Game.V_HEIGHT / 4, 14, 20);
            } else {
                sb.draw(currentScoreAnim.getFrame(), Game.V_WIDTH / 2 - 7, Game.V_HEIGHT - Game.V_HEIGHT / 4, 14, 20);
            }
            sb.end();

            //render start menu
            if (!start_gui.isPressedStart()) {
                sb.begin();
                start_gui.render(sb);
                sb.end();
            }

            //render game over menu
            if (cl.isDead()) {
                sb.begin();
                sb.draw(game_over, Game.V_WIDTH / 2 - game_over.getWidth() / 2, Game.V_HEIGHT / 2 - game_over.getHeight() / 2);

                //draw current score
                if (cl.getPoints() >= 10) {
                    sb.draw(currentScoreAnim2.getFrame(), 100, Game.V_HEIGHT / 2 - 8);
                    sb.draw(currentScoreAnim.getFrame(), 105 + 3, Game.V_HEIGHT / 2 - 8);
                } else {
                    sb.draw(currentScoreAnim.getFrame(), 105, Game.V_HEIGHT / 2 - 8);
                }

                //draw best score
                if (best >= 10) {
                    sb.draw(bestScoreAnim2.getFrame(), 100, Game.V_HEIGHT / 2 - 30);
                    sb.draw(bestScoreAnim.getFrame(), 105 + 3, Game.V_HEIGHT / 2 - 30);
                } else {
                    sb.draw(bestScoreAnim.getFrame(), 105, Game.V_HEIGHT / 2 - 29);
                }

                //draw medal
                if (!(cl.getPoints() == 0)) {
                    sb.draw(medals.getFrame(), 29, Game.V_HEIGHT / 2 - 24);
                }
                sb.end();

            }
        } else {
            sb.setProjectionMatrix(hudcam.combined);
            sb.begin();
            start_gui.render(sb);
            sb.end();

        }

        //draw box2d
        if (debug) {
            b2dr.render(world, cam.combined);
        }

    }

    @Override
    public void dispose() {
        sb.dispose();
        sr.dispose();

        get_ready.dispose();
        start_gui.dispose();
    }

    private void createPlayer() {
        //define vars
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.position.set(80 / PPM, Game.V_HEIGHT / 2 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.linearVelocity.set(.7f, 0);
        Body body = world.createBody(bdef);

        shape.setAsBox(9 / PPM, 6 / PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_PLAYER;
        fdef.filter.maskBits = BIT_PIPE | BIT_POINT | BIT_BORDER;
        body.createFixture(fdef).setUserData("player");
        shape.dispose();

        //remove body from world
        if (player != null) {
            world.destroyBody(player.getBody());
        }

        //create player
        player = null;
        player = new Player(body);

        body.setUserData(player);
    }

    private void createPipe() {
        pipe1X = 200;
//        pipe1X = 100;
        pipe2X = 350;
        pipe1Y = generateHeight();
        pipe2Y = generateHeight();
        pipe1 = createPipe(pipe1, pipe1X, pipe1Y);
        pipe2 = createPipe(pipe2, pipe2X, pipe2Y);
    }

    private Pipe createPipe(Pipe pipe, float pipeX, float pipeY) {

        //remove previous pipe
        if (pipe != null){
            world.destroyBody(pipe.getBody());
        }

        //devine vars
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        //create top pipe
//        bdef.position.set(144 / PPM, 0);
//        bdef.position.set(320 / PPM, Game.V_HEIGHT / 2 / PPM);
        bdef.position.set(pipeX / PPM, pipeY / PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bdef);

//        shape.setAsBox(13 / PPM, 67 / PPM, new Vector2(0, 76 / PPM), 0);
        shape.setAsBox(13 / PPM, 96 / PPM, new Vector2(0, 128 / PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_PIPE;
        fdef.filter.maskBits = BIT_PLAYER;
        body.createFixture(fdef).setUserData("pipe");

        //create sensor
        shape.setAsBox(2 / PPM, 32 / PPM, new Vector2(6 / PPM, -8 / PPM), 0);
        fdef.shape = shape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = BIT_POINT;
        fdef.filter.maskBits = BIT_PLAYER;
        body.createFixture(fdef).setUserData("sensor");

        //create bottom pipe
//        shape.setAsBox(13 / PPM, 60 / PPM, new Vector2(0, 0), 0);
        shape.setAsBox(13 / PPM, 96 / PPM, new Vector2(0, -129 / PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_PIPE;
        fdef.filter.maskBits = BIT_PLAYER;
        body.createFixture(fdef).setUserData("pipe");
        shape.dispose();

        //create pipe
        pipe = null;
        pipe = new Pipe(body);

        body.setUserData(pipe);
        return pipe;
    }

    private Body createBorder(Body border) {

        if (border != null) {
            world.destroyBody(border);
        }

        //devine vars
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.position.set(0, 0);
        bdef.type = BodyDef.BodyType.KinematicBody;
        bdef.linearVelocity.set(0.7f, 0f);
        border = null;
        border = world.createBody(bdef);

//        shape.setAsBox(13 / PPM, 67 / PPM, new Vector2(0, 76 / PPM), 0);
        shape.setAsBox(Game.V_WIDTH / PPM, 1 / PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_BORDER;
        fdef.filter.maskBits = BIT_PLAYER;
        border.createFixture(fdef).setUserData("bottom_border");

        shape.setAsBox(Game.V_WIDTH / PPM, 1 / PPM, new Vector2(0, Game.V_HEIGHT / PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_BORDER;
        fdef.filter.maskBits = BIT_PLAYER;
        border.createFixture(fdef).setUserData("top_border");

        shape.dispose();

        return border;
    }

    private void createPointsAnimation() {
        TextureRegion region = new TextureRegion(Game.res.getTexture("numbers"));
        TextureRegion[] frames = region.split(7, 10)[0];
        currentScoreAnim = new Animation(frames);
        currentScoreAnim2 = new Animation(frames);
    }

    private void createBestPointsAnimation() {
        TextureRegion region = new TextureRegion(Game.res.getTexture("numbers"));
        TextureRegion[] frames = region.split(7, 10)[0];

        bestScoreAnim = new Animation(frames);
        bestScoreAnim2 = new Animation(frames);
    }

    private void createMedals() {
        TextureRegion region = new TextureRegion(Game.res.getTexture("medals"));
        TextureRegion[] frames = region.split(22, 22)[0];

        medals = new Animation(frames);
    }

    private void reset() {
        //reset player state
        cl.setHit(false);
        cl.setDead(false);
        cl.setPoints(0);


        //create points numbers
        createPointsAnimation();

        //create player
        createPlayer();

        //create pipe
        createPipe();

        //create border
        border = createBorder(border);
    }

    private float generateHeight() {
        return MathUtils.random(Game.V_HEIGHT / 5, Game.V_HEIGHT - Game.V_HEIGHT / 5);
    }

    public static MyContactListener getCl() {
        return cl;
    }
}
