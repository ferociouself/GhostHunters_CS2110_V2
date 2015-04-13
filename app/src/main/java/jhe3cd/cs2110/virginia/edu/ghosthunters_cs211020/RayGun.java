package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.content.res.Resources;

/**
 * Created by liamj_000 on 4/6/2015.
 */
public class RayGun extends Item {

    private Ray ray;
    private boolean isTouching;

    public RayGun(double duration, int fileID, int xPosition, int yPosition, int xMax, int yMax,
                  int hitBoxWidth, int hitBoxHeight, MainActivity main, Ray ray) {
        super(duration, fileID, xPosition, yPosition, xMax, yMax, hitBoxWidth, hitBoxHeight, main);
        this.ray = ray;
    }

    public void update() {
        // If onTouchEvent then create a new ray, set it's target to the coordinates of the touch, and update it.
    }

    public void activate() {

    }

    public void terminate() {

    }

}
