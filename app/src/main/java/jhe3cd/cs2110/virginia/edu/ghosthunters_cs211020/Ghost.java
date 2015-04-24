package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.*;
import android.util.Log;

/**
 * Created by JacksonEkis on 3/30/15.
 */
public class Ghost extends Entity{

    private static final String DESTROYER_ID = "ghost";

    protected float xVelocity;
    protected float yVelocity;
    protected float xOrigAcceleration = 0.0f;
    protected float yOrigAcceleration = 0.0f;
    protected float xDynAcceleration = 0.0f;
    protected float yDynAcceleration = 0.0f;
    boolean isVisible;
    protected Point target;
    private Item booty;
    protected int health;

    private boolean ballTouching;
    private boolean ballCharged;
    private boolean frozen;
    protected float bounceFactor = 0.0f;

    private Ghost collidingGhost = null;

    public Ghost(int xPosition, int yPosition, int fileID, Point target, int health,
                 int hitBoxWidth, int hitBoxHeight, int xMax, int yMax, float xAcceleration,
                 float yAcceleration, float bounceFactor, MainActivity main) {
        super(fileID, xPosition, yPosition, xMax, yMax, hitBoxWidth, hitBoxHeight, main);
        this.target = target;
        getRandomItem();
        this.health = health;
        this.xVelocity = 0;
        this.yVelocity = 0;
        this.xOrigAcceleration = xAcceleration;
        this.yOrigAcceleration = yAcceleration;
        this.bounceFactor = bounceFactor;
        this.isVisible = false;
        this.ballTouching = false;
        this.ballCharged = false;
        this.frozen = false;
        this.main = main;
    }

    public void update()
    {
        if(!frozen)
        {
            targetUpdate();
            ballTouching = main.getBall().isTouching();
            ballCharged = main.getBall().isCharged();
            handleCollisions();
            if(ballTouching && !(ballCharged || frozen)) {
                if (collidingGhost != null) {
                    xDynAcceleration = -((float) (collidingGhost.centralPoint.x - centralPoint.x) / (float) xMax) * (3 * xOrigAcceleration);
                    yDynAcceleration = -((float) (collidingGhost.centralPoint.y - centralPoint.y) / (float) yMax) * (3 * yOrigAcceleration);
                } else {
                    xDynAcceleration = ((float) (target.x - centralPoint.x) / (float) xMax) * xOrigAcceleration;
                    yDynAcceleration = ((float) (target.y - centralPoint.y) / (float) yMax) * yOrigAcceleration;
                }
                this.xVelocity += (xDynAcceleration * MainActivity.FRAME_TIME);
                this.yVelocity += (yDynAcceleration * MainActivity.FRAME_TIME);
            } else if (MainActivity.getDifficulty() == 2) {
                xDynAcceleration = 0.0f;
                yDynAcceleration = 0.0f;
            } else {
                xDynAcceleration = 0.0f;
                yDynAcceleration = 0.0f;
                xVelocity = xVelocity * 0.90f;
                yVelocity = yVelocity * 0.95f;
            }




            //Calc distance travelled in that time
            float xS = (xVelocity/2)*MainActivity.FRAME_TIME;
            float yS = (yVelocity/2)*MainActivity.FRAME_TIME;
            xPosition += xS;
            yPosition += yS;

            if (xPosition + hitBox.width() > xMax) {
                xPosition = xMax - hitBox.width();
                xVelocity = -(xVelocity * bounceFactor);
            } else if (xPosition < 0) {
                xPosition = 0;
                xVelocity = -(xVelocity * bounceFactor);
            }
            if (yPosition + hitBox.height() > yMax) {
                yPosition = yMax - hitBox.height();
                yVelocity = -(yVelocity * bounceFactor);
            } else if (yPosition < 0) {
                yPosition = 0;
                yVelocity = -(yVelocity * bounceFactor);
            }

            hitBoxUpdate();
            centralPointUpdate();
        }

    }

    public void targetUpdate() {
        target.set(main.getBall().getxPosition(), main.getBall().getyPosition());
    }

    public void destroyer(String destroyer) {
        if (booty != null && destroyer.equals("friendlyGhost")) {
            //main.createNewEntity(booty);
        }
        main.entityRemove(this);
    }

    public void handleCollisions()
    {
        ArrayList<Entity> collisionArrayList = new ArrayList<>();
        collisionArrayList.addAll(collisionDetect(main.getEntityList()));
        if (collisionArrayList.size() == 0) {
            collidingGhost = null;
        }
        for (Entity e : collisionArrayList)
        {
            if (e instanceof Ghost)
            {
                collidingGhost = (Ghost) e;
            }
        }
    }

    public void randomlyGenerate()
    {
        xPosition = (int) (Math.random() * xMax);
        yPosition = (int) (Math.random() * yMax);
    }

    private void getRandomItem() {
        Random rand1 = new Random();
        Random rand2 = new Random();

        if(rand1.nextFloat() < .01) booty = new Item(main.SHIELD_ID, 30, R.drawable.shield, rand2.nextInt(xMax), rand2.nextInt(yMax), xMax, yMax, 80, 80, main);
        else if(rand1.nextFloat() < .02 && rand1.nextFloat() >= 0.01) booty = new Item(main.EXTRAHEALTH_ID, 30, R.drawable.extra_health, rand2.nextInt(xMax), rand2.nextInt(yMax), xMax, yMax, 80, 80, main);
        else if(rand1.nextFloat() < .03 && rand1.nextFloat() >= 0.02) booty = new Item(main.TIMEFREEZER_ID, 15, R.drawable.time_freezer, rand2.nextInt(xMax), rand2.nextInt(yMax), xMax, yMax, 80, 80, main);
        else if(rand1.nextFloat() < .04 && rand1.nextFloat() >= 0.03) booty = new RayGun(30, R.drawable.ray_gun, rand2.nextInt(xMax), rand2.nextInt(yMax), xMax, yMax, 80, 80, main);
        else booty = null;
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

    public void setFrozen(boolean b) { this.frozen = b; }
}
