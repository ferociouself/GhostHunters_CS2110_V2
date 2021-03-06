package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by liamj_000 on 4/4/2015.
 */
public class FriendlyGhost extends Ghost{

    private static final String DESTROYER_ID = "friendlyGhost";

    int lifeSpan;
    long timeActive;
    private Point defaultTarget;

    public FriendlyGhost(int xPosition, int yPosition, int fileID, int health,
                         int hitBoxWidth, int hitBoxHeight, int xMax, int yMax, float xAcceleration,
                         float yAcceleration, float bounceFactor, int lifeSpan, MainActivity main) {
        super(xPosition, yPosition, fileID, null, health, hitBoxWidth, hitBoxHeight, xMax,
                yMax, xAcceleration, yAcceleration, bounceFactor, main);
        this.lifeSpan = lifeSpan;
        this.defaultTarget = new Point(xMax/2, yMax/2);
        setTarget(findNearestGhostCenter());
    }

    public void update() {
        targetUpdate();
        xDynAcceleration = ((float) (target.x - centralPoint.x) / (float) xMax) * xOrigAcceleration;
        yDynAcceleration = ((float) (target.y - centralPoint.y) / (float) yMax) * yOrigAcceleration;
        this.xVelocity += (xDynAcceleration * MainActivity.FRAME_TIME);
        this.yVelocity += (yDynAcceleration * MainActivity.FRAME_TIME);
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
        handleCollisions();
        timeActive++;
        if (timeActive > lifeSpan * 100) {
            destroyer(DESTROYER_ID);
        }
    }

    public void handleCollisions() {
        ArrayList<Entity> collisionArrayList = new ArrayList<>();
        collisionArrayList.addAll(collisionDetect(main.getEntityList()));

        for (Entity e : collisionArrayList) {
            if (e instanceof Ghost && !(e instanceof FriendlyGhost)) {
                e.destroyer(DESTROYER_ID);
                main.incScore(50);
            }
        }
    }

    @Override
    public void targetUpdate() {
        Point newTarget = findNearestGhostCenter();
        if(newTarget != null) {
            setTarget(findNearestGhostCenter());
        }
        else {
            setTarget(this.defaultTarget);
        }
    }

    public static double distanceTwoPoints(Point p1, Point p2) {
        return Math.sqrt(Math.pow((p2.x - p1.x), 2) + Math.pow((p2.y - p1.y), 2));
    }

    public Point findNearestGhostCenter() {
        double minDistance = 1000000.0;
        Point currPoint = null;

        if (main.getEntityList().size() > 1) {
            for (Entity e : main.getEntityList()) {
                if (e instanceof Ghost && !(e instanceof FriendlyGhost)) {
                    double dist = distanceTwoPoints(centralPoint, e.centralPoint);
                    if (dist < minDistance) {
                        minDistance = dist;
                        currPoint = e.centralPoint;
                    }
                }
            }
        }
        return currPoint;
    }

    public void destroyer(String destroyer) {
        main.entityRemove(this);
        this.main.setFriendlyGhostSpawned(false);
    }
}
