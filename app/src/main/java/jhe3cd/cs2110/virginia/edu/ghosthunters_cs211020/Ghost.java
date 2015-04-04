package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.graphics.Point;

/**
 * Created by JacksonEkis on 3/30/15.
 */
public class Ghost extends Entity{

    private float xVelocity;
    private float yVelocity;
    private float xAcceleration;
    private float yAcceleration;
    boolean isVisible;
    private Point target;
    private Item booty;
    private int health;

    private boolean ballTouching;
    private boolean ballCharged;

    public Ghost(int xPosition, int yPosition, int fileID, Point target, Item booty, int health,
                 int hitBoxWidth, int hitBoxHeight, int xMax, int yMax, float xAcceleration, float yAcceleration) {
        super(fileID, xPosition, yPosition, xMax, yMax, hitBoxWidth, hitBoxHeight);
        this.target = target;
        this.booty = booty;
        this.health = health;
        this.xVelocity = 0;
        this.yVelocity = 0;
        this.xAcceleration = xAcceleration;
        this.yAcceleration = yAcceleration;
        this.isVisible = false;
        this.ballTouching = false;
        this.ballCharged = false;
    }

    public void updateTarget(int x, int y, boolean isTouching, boolean isCharged) {
        target.set(x, y);
        ballTouching = isTouching;
        ballCharged = isCharged;
        update();
    }

    public void update() {
        if(ballTouching && !ballCharged) {
            if(this.xPosition < target.x) {
                this.xAcceleration = Math.abs(xAcceleration);
            }
            if(this.xPosition > target.x) {
                this.xAcceleration = -(Math.abs(xAcceleration));
            }
            if(this.yPosition < target.y) {
                this.yAcceleration = Math.abs(yAcceleration);
            }
            if(this.yPosition > target.y) {
                this.yAcceleration = -(Math.abs(yAcceleration));
            }
        }
        this.xVelocity += (xAcceleration * MainActivity.FRAME_TIME);
        this.yVelocity += (yAcceleration * MainActivity.FRAME_TIME);

        //Calc distance travelled in that time
        float xS = (xVelocity/2)*MainActivity.FRAME_TIME;
        float yS = (yVelocity/2)*MainActivity.FRAME_TIME;

        //Add to position negative due to sensor
        //readings being opposite to what we want!
        xPosition -= xS;
        yPosition += yS;

        hitBoxUpdate();
    }

    public void destroyer() {

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
