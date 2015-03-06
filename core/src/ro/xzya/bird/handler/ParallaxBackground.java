package ro.xzya.bird.handler;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Xzya on 6/3/2015.
 */
public class ParallaxBackground {

    /**habe ich mir auch mal so zwei Klassen ParallaxLayer und ParallaxBackground geschrieben.
     * the layers of this background
     */
    private ParallaxLayer[] layers;
    /**
     * the camera
     */
    private Camera camera;
    /**
     * sprite batch
     */
    private SpriteBatch batch;

    /**
     * create a background
     * @param pLayers
     * @param pCamera your camera, so you can define whatever you want :P
     * @param pBatch your batch, so we do not have to use more than necessary
     */
    public ParallaxBackground(ParallaxLayer[] pLayers, Camera pCamera,
                              SpriteBatch pBatch) {
        layers = pLayers;
        camera = pCamera;
        batch = pBatch;
    }

    /**
     * render the parallax background
     */
    public void render() {
        batch.setProjectionMatrix(camera.projection);
        batch.begin();
        for (ParallaxLayer layer : layers) {
            batch.draw(layer.region, -camera.viewportWidth / 2
                    - layer.positionX, -camera.viewportHeight / 2
                    - layer.positionY);
        }
        batch.end();
    }

    /**
     * move the parallax background on the x-axis
     * @param pDelta
     */
    public void moveX(float pDelta) {
        for (ParallaxLayer layer : layers) {
            layer.moveX(pDelta);
        }
    }

    /**
     * move the parallax background on the y-axis
     * @param pDelta
     */
    public void moveY(float pDelta) {
        for (ParallaxLayer layer : layers) {
            layer.moveY(pDelta);
        }
    }
}