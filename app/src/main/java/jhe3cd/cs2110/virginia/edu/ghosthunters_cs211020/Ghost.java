package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by JacksonEkis on 3/30/15.
 */
public class Ghost extends Entity{

    private float xVelocity;
    private float yVelocity;
    private float xOrigAcceleration = 0.0f;
    private float yOrigAcceleration = 0.0f;
    private float xDynAcceleration = 0.0f;
    private float yDynAcceleration = 0.0f;
    boolean isVisible;
    private Point target;
    private Item booty;
    private int health;

    private boolean ballTouching;
    private boolean ballCharged;

    private float bounceFactor = 0.0f;

    public Ghost(int xPosition, int yPosition, int fileID, Point target, Item booty, int health,
                 int hitBoxWidth, int hitBoxHeight, int xMax, int yMax, float xAcceleration,
                 float yAcceleration, float bounceFactor, MainActivity main) {
        super(fileID, xPosition, yPosition, xMax, yMax, hitBoxWidth, hitBoxHeight, main);
        this.target = target;
        this.booty = booty;
        this.health = health;
        this.xVelocity = 0;
        this.yVelocity = 0;
        this.xOrigAcceleration = xAcceleration;
        this.yOrigAcceleration = yAcceleration;
        this.bounceFactor = bounceFactor;
        this.isVisible = false;
        this.ballTouching = false;
        this.ballCharged = false;
        this.main = main;
    }

    public void update() {
        target.set(main.getBall().getxPosition(), main.getBall().getyPosition());
        ballTouching = main.getBall().isTouching();
        ballCharged = main.getBall().isCharged();
        if(ballTouching && !ballCharged) {
            xDynAcceleration = ((float)(target.x - xPosition) / (float)xMax) * xOrigAcceleration;
            yDynAcceleration = ((float)(target.y - yPosition) / (float)yMax) * xOrigAcceleration;
            this.xVelocity += (xDynAcceleration * MainActivity.FRAME_TIME);
            this.yVelocity += (yDynAcceleration * MainActivity.FRAME_TIME);
        } else {
            xDynAcceleration = 0.0f;
            yDynAcceleration = 0.0f;
            xVelocity = xVelocity * 0.1f;
            yVelocity = yVelocity * 0.1f;
        }

        //Calc distance travelled in that time
        float xS = (xVelocity/2)*MainActivity.FRAME_TIME;
        float yS = (yVelocity/2)*MainActivity.FRAME_TIME;
        xPosition += xS;
        yPosition += yS;

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

        hitBoxUpdate();
        centralPointUpdate();
    }

    public void destroyer() {
        main.entityRemove(this);
    }

    public void randomlyGenerate() {
        xPosition = (int) (Math.random() * xMax);
        yPosition = (int) (Math.random() * yMax);
    }

    public float getxVelocity() {
        return xVelocity;
    }

    public float getyVelocity() {
        return yVelocity;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public Item getBooty() {
        return booty;
    }

    public int getHealth() {
        return health;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void setBooty(Item booty) {
        this.booty = booty;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setTarget(Point target) {
        this.target = target;
    }
}
