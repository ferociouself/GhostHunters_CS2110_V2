package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.app.usage.UsageEvents;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by liamj_000 on 4/6/2015.
 */
public class RayGun extends Item {

    public static final String ITEM_ID = "RayGun";
    private Ray ray;
    private boolean isTouching;

    public RayGun(double duration, int fileID, int xPosition, int yPosition, int xMax, int yMax,
                  int hitBoxWidth, int hitBoxHeight, MainActivity main) {
        super(ITEM_ID, duration, fileID, xPosition, yPosition, xMax, yMax, hitBoxWidth, hitBoxHeight, main);
    }

    public void updateTouch(float xPos, float yPos) {
        Log.i("RayGun", "Spawning Ray towards " + xPos + ", " + yPos);
        Point target = new Point((int) xPos, (int) yPos);
        main.createNewEntity(new Ray(R.drawable.ray, main.getBall().getCentralPoint().x, main.getBall().getCentralPoint().y, this.xMax, this.yMax,
                25, 25, main, target));
        }


    public void update() {
//        if(this.ray != null) {
//            ray.update();
//        }
//        if(main.getBall().getItemStored() != null && main.getBall().getItemStored().equals(this)) {
//            this.duration -= main.FRAME_TIME;
//        }
//        this.terminate();
    }

//    @Override
//    public void activate() {
//        main.getBall().setItemStored(this);
//        super.activate();
//    }
//
//    @Override
//    public void terminate() {
//        if((this.duration * 100) <= 0) {
//            main.getBall().setItemStored(null);
//        }
//    }
}
