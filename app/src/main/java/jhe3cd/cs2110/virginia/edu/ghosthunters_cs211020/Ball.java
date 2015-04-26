package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.media.MediaPlayer;
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

    private Paint paint;

    private int health;
    private int maxHealth;

    private Item itemStored;

    private int timeFrozen = 0;

    private int itemDurationCounter = 0;

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

        //Calculate distance travelled in that time
        float xS = (xVelocity)*MainActivity.FRAME_TIME;
        float yS = (yVelocity)*MainActivity.FRAME_TIME;

        xPosition += (int) xS;
        yPosition += (int) yS;

        // Creates the bouncing effect if the ball touches a side.
        if (xPosition + hitBox.width() > xMax) {
            xPosition = xMax - hitBox.width();
            xVelocity = -(xVelocity * bounceFactor);
            if ((Math.abs(xVelocity) > 20.0f)) {
                main.playSound();
            }
        } else if (xPosition < 0) {
            xPosition = 0;
            xVelocity = -(xVelocity * bounceFactor);
            if ((Math.abs(xVelocity) > 20.0f)) {
                main.playSound();
            }
        }
        if (yPosition + hitBox.height() > yMax) {
            yPosition = yMax - hitBox.height();
            yVelocity = -(yVelocity * bounceFactor);
            if ((Math.abs(yVelocity) > 20.0f)) {
                main.playSound();
            }
        } else if (yPosition < 0) {
            yPosition = 0;
            yVelocity = -(yVelocity * bounceFactor);
            if ((Math.abs(yVelocity) > 20.0f)) {
                main.playSound();
            }
        }

        // MAKE SURE TO ALWAYS UPDATE THE HITBOX AND CENTRAL POINT AFTER MOVING FOR EACH ENTITY!!!
        hitBoxUpdate();
        centralPointUpdate();
        handleCollisions();

        if(timeFrozen > 0)
            timeFrozen--;
        if(timeFrozen == 0)
            freezeGhosts(false);

        if (itemStored != null) {
            if (itemDurationCounter == 0) {
                setItemStored(null);
            } else {
                itemDurationCounter--;
            }
        }

        if (health == 0) {
            destroyer(DESTROYER_ID);
        }
    }

    public void destroyer(String destroyer) {
        main.endGame();
        main.entityRemove(this);
    }

    public boolean filterChanger(ColorFilter cFilter) {
        paint.setColorFilter(cFilter);
        return paint.getColorFilter() != null;
    }

    public void handleCollisions() {
        ArrayList<Entity> collisionArrayList = new ArrayList<>();
        collisionArrayList.addAll(collisionDetect(main.getEntityList()));

        for (Entity e : collisionArrayList) {
            if (e instanceof Ghost && !(e instanceof FriendlyGhost)) {
                if (isCharged) {
                    e.destroyer(DESTROYER_ID);
                    main.incScore(100);
                } else if (!isCharged && itemStored != null) {
                    if (itemStored.getItemID().equals("shield")) {
                        e.destroyer(DESTROYER_ID);
                        main.incScore(100);
                        deactivateItem();
                    }
                } else if (!isCharged) {
                    if (MainActivity.getDifficulty() == 2) {
                        incHealth(-60);
                        e.destroyer(DESTROYER_ID + "hurt");
                    } else {
                        incHealth(-30);
                        e.destroyer(DESTROYER_ID + "hurt");
                    }
                }
            }

            if (e instanceof Item)
            {
                Item it = (Item) e;

                switch(it.getItemID())
                {
                    case MainActivity.EXTRAHEALTH_ID:
                        this.incHealth(30);
                        break;
                    case MainActivity.TIMEFREEZER_ID:
                        if (getItemStored() != null) {
                            deactivateItem();
                        }
                        this.setItemStored(it);
                        break;
                    case MainActivity.SHIELD_ID:
                        if (!isCharged) {
                            if (getItemStored() != null) {
                                deactivateItem();
                            }
                            filterChanger(main.shieldFilter);
                            this.setItemStored(it);
                        }
                        break;
                    case "RayGun":
                        if (getItemStored() != null) {
                            deactivateItem();
                        }
                        this.setItemStored(it);
                        break;
                    case MainActivity.FEAR_ID:
                        if (getItemStored() != null) {
                            deactivateItem();
                        }
                        this.setItemStored(it);
                    default:
                        break;
                }
                it.destroyer(DESTROYER_ID);
                if (((Item) e).getDuration() > 0) {
                    itemDurationCounter = (int) (((Item) e).getDuration() * 10);
                }
            }
        }


    }

    public void incHealth(int added)
    {
        if ((health + added) < maxHealth) health += added;
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

    public void freezeGhosts(boolean b)
    {
        main.ghostsFrozen = b;
    }

    public void deactivateItem() {
        setItemStored(null);
        itemDurationCounter = 0;
        if (isCharged) {
            filterChanger(main.cFilter);
        } else {
            filterChanger(null);
        }
    }
}
