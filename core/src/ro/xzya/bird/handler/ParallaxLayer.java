package ro.xzya.bird.handler;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Xzya on 6/3/2015.
 */
public class ParallaxLayer {
    /**
     * the Texture sitting on this layer
     */
    public TextureRegion region;

    /**
     * how much shall this layer (in percent) be moved if the whole background is moved
     * 0.5f is half as fast as the speed
     * 2.0f is twice the speed
     */
    float ratioX, ratioY;

    /**
     * current position
     */
    float positionX, positionY;

    /**
     *
     * @param pRegion
     * @param pRatioX
     * @param pRatioY
     */
    public ParallaxLayer(TextureRegion pRegion, float pRatioX, float pRatioY) {
        region = pRegion;
        ratioX = pRatioX;
        ratioY = pRatioY;
    }

    /**
     * move this layer
     * @param pDelta
     */
    protected void moveX(float pDelta) {
        positionX += pDelta * ratioX;
    }

    /**
     * move this layer
     * @param pDelta
     */
    protected void moveY(float pDelta) {
        positionY += pDelta * ratioY;
    }
}