package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.animation.RectEvaluator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.shapes.RectShape;

import java.util.ArrayList;

/**
 * Created by JacksonEkis on 3/30/15.
 */
public abstract class Entity {
    public int xPosition;
    public int yPosition;
    public int xMax, yMax;

    public Point centralPoint;
    public Rect hitBox;

    public int fileID;

    protected MainActivity main;

    Bitmap bmp;

    public Entity(int fileID, int xPosition, int yPosition, int xMax, int yMax,
                  int hitBoxWidth, int hitBoxHeight, MainActivity main) {
        this.fileID = fileID;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.xMax = xMax;
        this.yMax = yMax;
        this.centralPoint = new Point(xPosition + (hitBoxWidth/2), yPosition + (hitBoxHeight/2));
        this.hitBox = new Rect(xPosition, yPosition, xPosition + hitBoxWidth, yPosition + hitBoxHeight);
        this.main = main;
        bmp = decodeSampledBitmapFromResource(main.getResources(), fileID, hitBox.width(), hitBox.height());
    }

    public abstract void update();

    public void hitBoxUpdate() {
        hitBox.offsetTo(xPosition, yPosition);
    }


    public void centralPointUpdate() {
        centralPoint.set((int) hitBox.exactCenterX(), (int) hitBox.exactCenterY());
    }

    public ArrayList<Entity> collisionDetect(ArrayList<Entity> entityArrayList) {
        ArrayList<Entity> tempArrayList = new ArrayList<>();
        tempArrayList.addAll(entityArrayList);
        tempArrayList.remove(this);
        ArrayList<Entity> collisionArrayList = new ArrayList<>();
        for (Entity e : tempArrayList) {
            if (hitBox.intersects(e.hitBox.left, e.hitBox.top, e.hitBox.right, e.hitBox.bottom)) {
                collisionArrayList.add(e);
            }
        }
        return collisionArrayList;
    }

    public boolean intersection(Rect a, Rect b) {
        return (a.left < b.right && b.left < a.right) && (a.top < b.bottom && b.top < a.bottom);
    }


    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bmp, xPosition, yPosition, paint);
    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                  int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public abstract void destroyer(String destroyer);

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int getxMax() {
        return xMax;
    }

    public int getyMax() {
        return yMax;
    }

    public Point getCentralPoint() {
        return centralPoint;
    }

    public Rect getHitBox() {
        return hitBox;
    }

    public int getFileID() {
        return fileID;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public void setxMax(int xMax) {
        this.xMax = xMax;
    }

    public void setyMax(int yMax) {
        this.yMax = yMax;
    }

    public void setHitBox(Rect hitBox) {
        this.hitBox = hitBox;
    }

    public void setHitBox(int left, int top, int right, int bottom) {
        this.hitBox = new Rect(left, top, right, bottom);
    }
}
