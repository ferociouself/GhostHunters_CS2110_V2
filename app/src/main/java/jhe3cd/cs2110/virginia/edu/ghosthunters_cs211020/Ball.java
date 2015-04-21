package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by JacksonEkis on 3/30/15.
 */
public class Ball extends Entity{
    public static final String DESTROYER_ID = "ball";
    private float xVelocity, xAcceleration = 0.0f;
    private float yVelocity, yAcceleration = 0.0f;
    private float bounceFactor;

    private float speedMod;
    public static float origSpeedMod;

    public boolean isTouching;
    public boolean isCharged;

    private int fileID;

    private Paint paint;

    private int health;
    private int maxHealth;

    private Item itemStored;

    public Ball (int fileID, int xPosition, int yPosition, float speedMod,
                 int xMax, int yMax, int hitBoxWidth, int hitBoxHeight, float bounceFactor,
                 int health, MainActivity main) {
        super(fileID, xPosition, yPosition, xMax, yMax, hitBoxWidth, hitBoxHeight, main);
        this.speedMod = speedMod;
        origSpeedMod = speedMod;
        paint = new Paint();
        this.bounceFactor = bounceFactor;
        this.health = health;
        this.maxHealth = health;
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

        float xS = 0;
        float yS = 0;
        //Calculate distance travelled in that time
        if (MainActivity.getDifficulty() >= 1) {
            xS = (xVelocity)*MainActivity.FRAME_TIME;
            yS = (yVelocity)*MainActivity.FRAME_TIME;
        } else {
            xS = (xVelocity/2) * MainActivity.FRAME_TIME;
            yS = (yVelocity/2) * MainActivity.FRAME_TIME;
        }

        xPosition += (int) xS;
        yPosition += (int) yS;

        // Creates the bouncing effect if the ball touches a side.
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

        // MAKE SURE TO ALWAYS UPDATE THE HITBOX AND CENTRAL POINT AFTER MOVING FOR EACH ENTITY!!!
        hitBoxUpdate();
        centralPointUpdate();
        handleCollisions();
        if (health == 0) {
            this.destroyer(DESTROYER_ID);
        }
    }

    public void destroyer(String destroyer) {
        main.endGame();
        main.entityRemove(this);
    }

    public boolean filterChanger(ColorFilter cFilter) {
        paint.setColorFilter(cFilter);
        return paint.getColorFilter().equals(cFilter);
    }

    public void handleCollisions() {
        ArrayList<Entity> collisionArrayList = new ArrayList<>();
        collisionArrayList.addAll(collisionDetect(main.getEntityList()));

        for (Entity e : collisionArrayList) {
            if (e instanceof Ghost && isCharged) {
                Log.i("BALL", "Ghost collided with");
                e.destroyer(DESTROYER_ID);
                main.incScore(100);
            }
            if (e instanceof Item) {
                Log.i("BALL", "Item collided with");
                ((Item) e).activate();
                if (!(((Item) e).getItemID().equals("extraHealth"))) {
                    itemStored = (Item) e;
                }
            }
        }
    }

    public void incHealth(int added) {
        health += added;
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

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public Item getItemStored() {
        return itemStored;
    }

    public void setItemStored(Item itemStored) {
        this.itemStored = itemStored;
    }
}
