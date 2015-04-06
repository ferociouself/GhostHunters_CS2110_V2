package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import java.util.*;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    CustomDrawableView customDrawView = null;
    public int xMax, yMax;
    private SensorManager sensorManager = null;
    public static final float FRAME_TIME = 0.666f;

    private Sensor accelerometer;

    public Point size;

    public int initChargerX = 100;
    public int chargerX = initChargerX + 10;
    public static final int CHARGER_DECAY_RATE = 3;
    // Charger decay rate in ticks per 1 decay.
    public int miniChargerCounter = 0;
    // Keeps track of the ticks.

    public Paint bgPaint;
    public Paint ballPaint;
    public Paint chargerPaint;
    public Paint chargerBGPaint;
    public Paint genericPaint;
    public Paint wordPaint;

    public ColorFilter cFilter;

    public Ball ball;

    public static final int BALL_WIDTH = 40;
    public static final int BALL_HEIGHT = 40;

    public ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<Entity> entitiesRemoved = new ArrayList<>();

    public int numGhostsSpawned = 4;
    public int score;

    public static int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);

        // Finding the boundaries
        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        xMax = (int)(size.x * 0.92);
        yMax = (int)(size.y * 0.73);

        genericPaint = new Paint();
        bgPaint = new Paint();
        chargerPaint = new Paint();
        chargerBGPaint = new Paint();
        ballPaint = new Paint();

        wordPaint = new Paint();

        cFilter = new LightingColorFilter(Color.YELLOW, 1);

        chargerPaint.setARGB(255, 255, 0, 0);
        chargerBGPaint.setARGB(200, 0, 0, 0);
        bgPaint.setARGB(255, 100, 100, 100);
        wordPaint.setARGB(255, 0, 0, 0);
        wordPaint.setTextSize(50);

        createEntities();

        customDrawView = new CustomDrawableView(this);
        setContentView(customDrawView);
    }

    public boolean createEntities(){
        boolean worked = false;
        boolean forWorked = false;

        ball = new Ball(R.drawable.ball, xMax/2, yMax/2, 1, xMax, yMax, BALL_WIDTH, BALL_HEIGHT,
                0.9f, this);
        worked = entityList.add(ball);

        for (int i = 0; i < numGhostsSpawned; i++) {
            Ghost tempGh = new Ghost(0, 0, R.drawable.ghost, ball.getCentralPoint(), null, 1, 32,
                    38, xMax, yMax, 5.0f, 5.0f, 0.9f, this);
            tempGh.randomlyGenerate();
            entityList.add(tempGh);
            forWorked = true;
        }

        return worked && forWorked;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = MotionEventCompat.getActionMasked(event);

        if (action == MotionEvent.ACTION_DOWN) {
            ball.toggleTouching();
        } else if (action == MotionEvent.ACTION_UP) {
            ball.toggleTouching();
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // This method required to implement SensorEventListener
        // Do something if the sensor accuracy changes. (Throw an error?)
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            ball.updateAcceleration(event.values[1], event.values[0]);
        }
    }

    private void update() {
        for (Entity e : entityList) {
            e.update();
        }
        for (Entity e : entitiesRemoved) {
            entityList.remove(e);
        }
        entitiesRemoved.clear();
    }

    public int incScore(int scoreIncrease) {
        this.score += scoreIncrease;
        return this.score;
    }

    public boolean entityRemove(Entity e) {
        return entitiesRemoved.add(e);
    }

    public Ball getBall() {
        return ball;
    }

    public ArrayList<Entity> getEntityList() {
        return entityList;
    }

    public static int getDifficulty(){
        return difficulty;
    }

    public static void setDifficulty(int newDifficulty) {
        difficulty = newDifficulty;
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    /*    @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }*/
    public class CustomDrawableView extends View {
        private Bitmap ballBMP;

        private int chargerBarHeight;
        private int chargerBarWidth;

        public CustomDrawableView(Context context) {
            super(context);
            /*Bitmap ballBMP = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
            final int dstWidth = BALL_WIDTH;
            final int dstHeight = BALL_HEIGHT;
            mainBitmap = Bitmap.createScaledBitmap(ballBMP, dstWidth, dstHeight, true);*/
            /*ballBMP = decodeSampledBitmapFromResource(getResources(), R.drawable.ball,
                    BALL_WIDTH, BALL_HEIGHT);*/

        }

        protected void onDraw(Canvas canvas) {
            ThreadTest newThread = new ThreadTest();
            update();
            newThread.chargerSensor();
            newThread.toucherSensor();
            canvas.drawRect(0, 0, size.x, size.y, bgPaint);
            canvas.drawRect(initChargerX - 10, 40, initChargerX + 410, 110, chargerBGPaint);
            canvas.drawRect(initChargerX, 50, chargerX, 100, chargerPaint);
            // canvas.drawBitmap(ballBMP, ball.getxPosition(), ball.getyPosition(), ballPaint);
            canvas.drawText("Score: " + score, xMax - 150, yMax - 150, wordPaint);
            for (Entity e : entityList) {
                e.draw(canvas, getResources(), genericPaint);
            }
            invalidate();
        }


    }

    public class ThreadTest extends Thread {
        public void toucherSensor() {
            if (ball.isTouching()) {
                if (chargerX < initChargerX + 400 && !ball.isCharged()) {
                    chargerX++;
                } else {
                    ball.setCharged(true);
                }
            }
        }

        public void chargerSensor() {
            if (ball.isCharged()) {
                if (ballPaint.getColorFilter() == null) {
                    ballPaint.setColorFilter(cFilter);
                }
                if (chargerX < initChargerX + 10) {
                    ball.setCharged(false);
                    ballPaint.setColorFilter(null);
                } else {
                    if (miniChargerCounter >= CHARGER_DECAY_RATE) {
                        chargerX--;
                        miniChargerCounter = 0;
                    } else {
                        miniChargerCounter++;
                    }
                }
            }
        }
    }
}
