package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by JacksonEkis on 3/30/15.
 */
public class Ball extends Entity{
    public float xVelocity, xAcceleration = 0.0f;
    public float yVelocity, yAcceleration = 0.0f;
    public float bounceFactor;

    public float speedMod;
    public static float origSpeedMod;

    public boolean isTouching;
    public boolean isCharged;

    public int fileID;

    private Paint paint;

    public Ball (int fileID, int xPosition, int yPosition, float speedMod,
                 int xMax, int yMax, int hitBoxWidth, int hitBoxHeight, float bounceFactor,
                 MainActivity main) {
        super(fileID, xPosition, yPosition, xMax, yMax, hitBoxWidth, hitBoxHeight, main);
        this.speedMod = speedMod;
        origSpeedMod = speedMod;
        paint = new Paint();
        this.bounceFactor = bounceFactor;
    }

    public void updateAcceleration (float xAcceleration, float yAcceleration){
        this.xAcceleration = xAcceleration;
        this.yAcceleration = yAcceleration;
    }

    @Override
    public void update() {
        xVelocity += (xAcceleration * MainActivity.FRAME_TIME);
        yVelocity += (yAcceleration * MainActivity.FRAME_TIME);

        if (isTouching) {
            speedMod = 0.5f;
        } else {
            speedMod = origSpeedMod;
        }

        xVelocity *= speedMod;
        yVelocity *= speedMod;

        //Calculate distance travelled in that time
        float xS = (xVelocity)*MainActivity.FRAME_TIME;
        float yS = (yVelocity)*MainActivity.FRAME_TIME;

        //Add to position negative due to sensor
        //readings being opposite to what we want!
        xPosition += (int) xS;
        yPosition += (int) yS;

        // Creates the bouncing effect if the ball touches a side.
        if (xPosition > xMax) {
            xPosition = xMax;
            xVelocity = -(xVelocity * bounceFactor);
        } else if (xPosition < 0) {
            xPosition = 0;
            xVelocity = -(xVelocity * bounceFactor);
        }
        if (yPosition > yMax) {
            yPosition = yMax;
            yVelocity = -(yVelocity * bounceFactor);
        } else if (yPosition < 0) {
            yPosition = 0;
            yVelocity = -(yVelocity * bounceFactor);
        }

        // MAKE SURE TO ALWAYS UPDATE THE HITBOX AND CENTRAL POINT AFTER MOVING FOR EACH ENTITY!!!
        hitBoxUpdate();
        centralPointUpdate();
        handleCollisions(main.getEntityList());
    }

    public void destroyer() {
        main.entityRemove(this);
    }

    public boolean filterChanger(ColorFilter cFilter) {
        paint.setColorFilter(cFilter);
        return paint.getColorFilter().equals(cFilter);
    }

    public void handleCollisions(ArrayList<Entity> entityArrayList) {
        ArrayList<Entity> collisionArrayList = new ArrayList<>();
        collisionArrayList.addAll(collisionDetect(entityArrayList));

        for (Entity e : collisionArrayList) {
            if (e instanceof Ghost && isCharged) {
                Log.i("BALL", "Ghost collided with");
                e.destroyer();
            }
        }
    }

    public void toggleTouching() {
        isTouching = !isTouching;
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public float getSpeedMod() {
        return speedMod;
    }

    public boolean isCharged() {
        return isCharged;
    }

    public int getFileID() {
        return fileID;
    }

    public boolean isTouching() {
        return isTouching;
    }

    public void setTouching(boolean isTouching) {
        this.isTouching = isTouching;
    }

    public void setCharged(boolean isCharged) {
        this.isCharged = isCharged;
    }

    public Paint getPaint() {
        return paint;
    }
}
