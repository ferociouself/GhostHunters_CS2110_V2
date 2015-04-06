package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by liamj_000 on 4/4/2015.
 */
public class FriendlyGhost extends Ghost{

    private boolean isFriendly;
    int lifeSpan;
    long timeActive;

    public FriendlyGhost(int xPosition, int yPosition, int fileID, Point target, Item booty, int health,
                         int hitBoxWidth, int hitBoxHeight, int xMax, int yMax, float xAcceleration,
                         float yAcceleration, float bounceFactor, int lifeSpan, MainActivity main) {
        super(xPosition, yPosition, fileID, target, booty, health, hitBoxWidth, hitBoxHeight, xMax,
                yMax, xAcceleration, yAcceleration, bounceFactor, main);
        this.isFriendly = true;
        this.lifeSpan = lifeSpan;
    }

    public void update() {
        /*ArrayList<Float> distances = this.distanceBetweenGhosts();
        Collections.sort(distances);
        float smallestDist = distances.get(0);*/

        setTarget(findNearestGhostCenter());

//        this.setTarget();
        for(Entity e : this.collisionDetect(main.getEntityList())) {
            if(e instanceof Ghost) {
                e.destroyer();
            }
        }
        timeActive++;
        if (timeActive > lifeSpan * 1000) {
            destroyer();
        }
    }

    public ArrayList<Float> distanceBetweenGhosts() {
        ArrayList<Float> distances = new ArrayList<Float>();
        Point p1 = this.getCentralPoint();
        ArrayList<Point> centralPoints = new ArrayList<Point>();
        for(Entity e : main.getEntityList()) {
            if(e instanceof Ghost) {
                Ghost g1 = (Ghost) e;
                Point p2 = g1.getCentralPoint();
                centralPoints.add(p2);
            }
        }
        for(Point p : centralPoints) {
            double xDist = 0;
            double yDist = 0;
            if(p1.x > p.x) {
                xDist = p1.x - p.x;
            }
            else if(p1.x < p.x) {
                xDist = p.x - p1.x;
            }
            if(p1.y > p.y) {
                yDist = p1.y - p.y;
            }
            else if(p1.y < p.y) {
                yDist = p.y - p1.y;
            }
            float distance = (float) Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
            distances.add(distance);
        }
        return distances;
    }

    public static double distanceTwoPoints(Point p1, Point p2) {
        return Math.sqrt(Math.pow((p2.x - p1.x), 2) + Math.pow((p2.y - p1.y), 2));
    }

    public Point findNearestGhostCenter() {
        double minDistance = 1000000.0;
        Point currPoint = null;

        if (main.getEntityList().size() > 1) {
            for (Entity e : main.getEntityList()) {
                if (e instanceof Ghost) {
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

    public void destroyer() {
        main.entityRemove(this);
    }
}
