package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.graphics.Point;

import java.util.ArrayList;

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

    private boolean isColliding = false;

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
        this.frozen = false;
        this.main = main;
    }

    public void update() {
        targetUpdate();
        ballTouching = main.getBall().isTouching();
        ballCharged = main.getBall().isCharged();
        handleCollisions();
        if(ballTouching && !ballCharged && !frozen) {
            if (isColliding) {
                xDynAcceleration = -((float) (target.x - centralPoint.x) / (float) xMax) * xOrigAcceleration;
                yDynAcceleration = -((float) (target.y - centralPoint.y) / (float) yMax) * yOrigAcceleration;
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

    public void targetUpdate() {
        target.set(main.getBall().getxPosition(), main.getBall().getyPosition());
    }

    public void destroyer(String destroyer) {
        if (destroyer.equals("ball") && Math.random() > .90) {
            main.createNewEntity(new FriendlyGhost(xPosition, yPosition, R.drawable.friendly_ghost,
                    10, hitBox.width(), hitBox.height(), xMax, yMax, xOrigAcceleration,
                    yOrigAcceleration, bounceFactor, 8, main));
        }
        main.reduceNumGhostsActive(1);
        main.entityRemove(this);
    }

    public void handleCollisions()
    {
        ArrayList<Entity> collisionArrayList = new ArrayList<>();
        collisionArrayList.addAll(collisionDetect(main.getEntityList()));
        for (Entity e : collisionArrayList)
        {
            if (e instanceof Ghost)
            {
                isColliding = true;
            }
            if (e instanceof Ball)
            {
                if (!main.getBall().isCharged()) {
                Ball it = (Ball)e;

                if (it.getItemStored() != null) {
                    String id = it.getItemStored().getItemID();


                    switch (id) {
                        case "shield":
                            break;

                        case "extraHealth":
                            main.getBall().incHealth(30);
                            break;

                        case "timeFreezer":
                            frozen = true;
                            break;

                        default:
                            main.getBall().incHealth(-30);
                            destroyer(DESTROYER_ID);
                            break;
                    }
                }
                    if (MainActivity.getDifficulty() == 2) {
                        main.getBall().incHealth(-60);
                        destroyer(DESTROYER_ID);
                        break;
                    } else {
                        main.getBall().incHealth(-30);
                        destroyer(DESTROYER_ID);
                        break;
                    }
                }
            }
        }
        isColliding = collisionArrayList.size() > 1;
    }

//    public void handleGhostToGhostCollision() {
//        Ghost ghost1 =
//        for(Entity e : main.getEntityList()) {
//            if(e instanceof Ghost) {
//                Ghost ghost1 = (Ghost) e;
//            }
//            for(Entity i : main.getEntityList()) {
//                if(i instanceof Ghost) {
//                    Ghost ghost2 = (Ghost) i;
//                    if(ghost1.getHitbox().intersects(zombie2.getHitbox()) == true) {
//                        Rectangle intersection = zombie1.getHitbox().intersection(zombie2.getHitbox());
//                        if(intersection.height > intersection.width) {
//                            if(zombie1.getX() > zombie2.getX()) {
//                                zombie1.setX(zombie1.getX() + intersection.width/4);
//                            }
//                            if(zombie1.getX() < zombie2.getX()) {
//                                zombie1.setX(zombie1.getX() - intersection.width/4);
//                            }
//                        }
//                        if(intersection.width > intersection.height) {
//                            if(zombie1.getY() > zombie2.getY()) {
//                                zombie1.setY(zombie1.getY() + intersection.height/4);
//                            }
//                            if(zombie1.getY() < zombie2.getY()) {
//                                zombie1.setY(zombie1.getY() - intersection.height/4);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    public void randomlyGenerate()
    {
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
