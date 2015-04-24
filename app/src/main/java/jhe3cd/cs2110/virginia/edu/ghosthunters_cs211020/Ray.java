package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by liamj_000 on 4/6/2015.
 */
public class Ray extends Entity {

    private static final String DESTROYER_ID = "ray";
    private float xVelocity;
    private float yVelocity;
    private Point target;

    public Ray(int fileID, int xPosition, int yPosition, int xMax, int yMax,
               int hitBoxWidth, int hitBoxHeight, MainActivity main, Point target) {
        super(fileID, xPosition, yPosition, xMax, yMax, hitBoxWidth, hitBoxHeight, main);
        this.target = target;
        this.xVelocity = (target.x - main.getBall().getCentralPoint().x)/100;
        this.yVelocity = (target.y - main.getBall().getCentralPoint().y)/100;
    }

    public void update() {
        xPosition += xVelocity * main.FRAME_TIME;
        yPosition += yVelocity * main.FRAME_TIME;
        this.hitBoxUpdate();
        this.centralPointUpdate();
        this.handleCollisions();
        if(this.xPosition > xMax) {
            this.destroyer(this.DESTROYER_ID);
        }
        if(this.xPosition < 0) {
            this.destroyer(this.DESTROYER_ID);
        }
        if(this.yPosition > yMax) {
            this.destroyer(this.DESTROYER_ID);
        }
        if(this.yPosition < 0) {
            this.destroyer(this.DESTROYER_ID);
        }
    }

    public void handleCollisions() {
        ArrayList<Entity> collisionArrayList = new ArrayList<>();
        collisionArrayList.addAll(collisionDetect(main.getEntityList()));

        for (Entity e : collisionArrayList) {
            if (e instanceof Ghost) {
                Log.i("Ray", "Ghost collided with");
                e.destroyer(DESTROYER_ID);
            }
        }
    }

   public void destroyer(String destroyer) {
        main.entityRemove(this);
   }

   public void eliminate() {
       for(Entity e : main.getEntityList()) {
           if(e instanceof Ghost) {
               main.getEntityList().remove(e);
           }
       }
       this.destroyer(this.DESTROYER_ID);
   }
}
