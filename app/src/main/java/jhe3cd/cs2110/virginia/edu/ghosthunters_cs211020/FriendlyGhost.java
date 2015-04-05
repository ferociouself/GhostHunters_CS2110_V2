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
    ArrayList<Entity> entityList;

    public FriendlyGhost(int xPosition, int yPosition, int fileID, Point target, Item booty, int health,
                         int hitBoxWidth, int hitBoxHeight, int xMax, int yMax, float xAcceleration, float yAcceleration, int lifeSpan) {
        super(xPosition, yPosition, fileID, target, booty, health, hitBoxWidth, hitBoxHeight, xMax, yMax, xAcceleration, yAcceleration);
        this.isFriendly = true;
        this.lifeSpan = lifeSpan;
    }

    public void update() {
        ArrayList<Float> distances = this.distanceBetweenGhosts(entityList);
        Collections.sort(distances);
        float smallestDist = distances.get(0);

//        this.setTarget();
        for(Entity e : this.collisionDetect(entityList)) {
            if(e instanceof Ghost) {
                entityList.remove(e);
            }
        }
    }

    public void updateEntityList(ArrayList<Entity> entityList) {
        this.entityList = entityList;
    }

    public ArrayList<Float> distanceBetweenGhosts(ArrayList<Entity> entityList) {
        ArrayList<Float> distances = new ArrayList<Float>();
        Point p1 = this.getCentralPoint();
        ArrayList<Point> centralPoints = new ArrayList<Point>();
        for(Entity e : entityList) {
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

    public void destroyer() {

    }
}
