package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.app.usage.UsageEvents;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.MotionEvent;

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

    public void updateTouch(float xPos, float yPos) {
        Point target = new Point((int) xPos, (int) yPos);
//        Ray ray1 = new Ray(R.drawable.ray, this.getCentralPoint().x, this.getCentralPoint().y, this.xMax, this.yMax,
//               10, 10, main, target);
//        ray1.update();
        // If onTouchEvent then create a new ray, set it's target to the coordinates of the touch, and update it.
        }



    public void activate() {

    }

    public void terminate() {

    }

}
