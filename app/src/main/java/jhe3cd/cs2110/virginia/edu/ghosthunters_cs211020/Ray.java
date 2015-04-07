package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.content.res.Resources;

/**
 * Created by liamj_000 on 4/6/2015.
 */
public class Ray extends Entity {

    private float xVelocity;
    private float yVelocity;
    private float xAcceleration;
    private float yAcceleration;

    public Ray(int fileID, int xPosition, int yPosition, int xMax, int yMax,
               int hitBoxWidth, int hitBoxHeight, MainActivity main, float xVelocity,
               float yVelocity, float xAcceleration, float yAcceleration) {
        super(fileID, xPosition, yPosition, xMax, yMax, hitBoxWidth, hitBoxHeight, main);
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.xAcceleration = xAcceleration;
        this.yAcceleration = yAcceleration;
    }

    public void update() {
        xVelocity += xAcceleration * main.FRAME_TIME;
        yVelocity += yAcceleration * main.FRAME_TIME;
        xPosition += xVelocity;
        yPosition += yVelocity;
        this.hitBoxUpdate();
        this.centralPointUpdate();
    }

   public void destroyer() {

   }

   public void eliminate() {
       for(Entity e : main.getEntityList()) {
           if(e instanceof Ghost) {
               main.getEntityList().remove(e);
           }
       }
   }

}
