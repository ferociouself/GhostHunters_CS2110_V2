package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.content.Context;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import java.util.*;

/**
 * Jackson Ekis: jhe3cd
 * Liam Dwyer: ljd3za
 * Matthew Thornton: mpt5nm
 *
 */

public class MainActivity extends ActionBarActivity implements SensorEventListener{

    CustomDrawableView customDrawView = null;
    public int xMax, yMax;
    private SensorManager sensorManager = null;
    public static final float FRAME_TIME = 0.666f;

    private Sensor accelerometer;

    public Point size;

    public Ball ball;

    public int initChargerX = 100;
    public int chargerX = initChargerX + 10;
    public static final int CHARGER_DECAY_RATE = 3;
    // Charger decay rate in ticks per 1 decay.
    public int miniChargerCounter = 0;
    // Keeps track of the ticks.

    public int initHealthX;
    public int healthX;

    public Paint bgPaint;
    public Paint ballPaint;
    public Paint chargerPaint;
    public Paint barBGPaint;
    public Paint genericPaint;
    public Paint wordPaint;
    public Paint healthPaint;
    public Paint pausedPaint;
    public Paint pausedWordPaint;

    public ColorFilter cFilter;

    public int doubleTapTimer = 0;
    public boolean doubleTapTriggered = false;

    public static final int BALL_WIDTH = 40;
    public static final int BALL_HEIGHT = 40;

    public ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<Entity> entitiesRemoved = new ArrayList<>();

    public HashMap<String, Item> itemMap = new HashMap<>();

    public int numGhostsSpawned = 4;
    public int numGhostsActive = 4;
    public int score;

    public int timeCounter = 0;

    public static int difficulty = 0;

    public static final String SHIELD_ID = "shield";

    public boolean paused = false;

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
        xMax = (int) (size.x * 0.97);
        yMax = (int)(size.y * 0.88);

        initHealthX = xMax - 400;

        setPaints();

        createInitialEntities();

        score = 0;

        customDrawView = new CustomDrawableView(this);
        setContentView(customDrawView);
    }

    public void setPaints() {
        genericPaint = new Paint();
        bgPaint = new Paint();
        chargerPaint = new Paint();
        barBGPaint = new Paint();
        ballPaint = new Paint();

        wordPaint = new Paint();
        healthPaint = new Paint();

        pausedPaint = new Paint();
        pausedWordPaint = new Paint();

        cFilter = new LightingColorFilter(Color.YELLOW, 1);

        chargerPaint.setARGB(255, 0, 255, 255);
        barBGPaint.setARGB(200, 0, 0, 0);
        bgPaint.setARGB(255, 100, 100, 100);
        wordPaint.setARGB(255, 0, 0, 0);
        wordPaint.setTextSize(50);
        healthPaint.setARGB(255, 255, 0, 0);
        pausedPaint.setARGB(100, 0, 0, 0);
        pausedWordPaint.setARGB(255, 255, 255, 255);
        pausedWordPaint.setTextSize(150.0f);
    }

    public boolean createInitialEntities() {
        boolean forWorked = false;

        ball = new Ball(R.drawable.ball, xMax/2, yMax/2, 1, xMax, yMax, BALL_WIDTH, BALL_HEIGHT,
                0.9f, 300, this);
        healthX = initHealthX + ball.getMaxHealth();

        boolean worked = entityList.add(ball);

        for (int i = 0; i < numGhostsSpawned; i++) {
            Ghost tempGh = new Ghost(0, 0, R.drawable.ghost, ball.getCentralPoint(), null, 1, 32,
                    38, xMax, yMax, 5.0f, 5.0f, 0.9f, this);
            tempGh.randomlyGenerate();
            entityList.add(tempGh);
            forWorked = true;
        }

        return worked && forWorked;
    }

    public void spawnNewGhosts() {
        for (int i = 0; i < numGhostsSpawned; i++) {
            Ghost tempGh= new Ghost(0, 0, R.drawable.ghost, ball.getCentralPoint(), null, 1, 32,
                    38, xMax, yMax, 5.0f, 5.0f, 0.9f, this);
            tempGh.randomlyGenerate();
            createNewEntity(tempGh);
            numGhostsActive++;
        }
    }

    public void createNewEntity(Entity e) {
        entityList.add(e);
    }

    public void reduceNumGhostsActive(int i) {
        numGhostsActive -= i;
    }

    public Item getRandomItem() {
        // TODO Add ability to get random items from a list of items.
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = MotionEventCompat.getActionMasked(event);

        if (doubleTapTriggered && action == MotionEvent.ACTION_DOWN) {
            doubleTapTriggered = false;
            paused = !paused;
            doubleTapTimer = 0;
        } else if (action == MotionEvent.ACTION_DOWN) {
            ball.setTouching(true);
            doubleTapTriggered = true;
            doubleTapTimer = 30;
        } else if (action == MotionEvent.ACTION_UP) {
            ball.setTouching(false);
        }

        if(this.getBall().getItemStored() != null) {
            if (this.getBall().getItemStored().getItemID().equals("RayGun")) {
                RayGun rayGun = (RayGun) this.getBall().getItemStored();
                rayGun.updateTouch(event.getX(), event.getY());
                rayGun.update();
            }
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        paused = false;
        Log.i("PauserResume" , "Paused: " + paused);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
        Log.i("PauserPause" , "Paused: " + paused);
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
        if (doubleTapTimer > 0) {
            doubleTapTimer--;
            if (doubleTapTimer == 0) {
                createNewEntity(new FriendlyGhost(100, 100, R.drawable.friendly_ghost, 50,
                        32, 38, xMax, yMax, 5.0f,
                        5.0f, 0.9f, 10, this));
            }
        }
        if (doubleTapTimer == 0) {
            doubleTapTriggered = false;
        }
        if (!paused) {
            for (Entity e : entityList) {
                e.update();
            }
            for (Entity e : entitiesRemoved) {
                entityList.remove(e);
            }
            entitiesRemoved.clear();
            healthX = initHealthX + ball.getHealth();
            if (timeCounter == 500) {
                spawnNewGhosts();
                timeCounter = 0;
            } else {
                timeCounter++;
            }
        }
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

        /* @Override
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
        } */
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
            if (!paused) {
                ThreadTest newThread = new ThreadTest();
                update();
                newThread.chargerSensor();
                newThread.toucherSensor();
                canvas.drawRect(0, 0, size.x, size.y, bgPaint);
                canvas.drawRect(initChargerX - 10, 40, initChargerX + 410, 110, barBGPaint);
                canvas.drawRect(initChargerX, 50, chargerX, 100, chargerPaint);
                canvas.drawRect(initHealthX - 10, 40, initHealthX + ball.getMaxHealth() + 10, 110, barBGPaint);
                canvas.drawRect(initHealthX, 50, healthX, 100, healthPaint);
                canvas.drawText("Score: " + score, xMax - 250, yMax - 50, wordPaint);
                for (Entity e : entityList) {
                    e.draw(canvas, genericPaint);
                }
            } else {
                update();
                canvas.drawRect(0, 0, size.x, size.y, pausedPaint);
                canvas.drawText("PAUSED", xMax/2, yMax/2, pausedWordPaint);
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
                    if (miniChargerCounter >= 3 - difficulty) {
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
