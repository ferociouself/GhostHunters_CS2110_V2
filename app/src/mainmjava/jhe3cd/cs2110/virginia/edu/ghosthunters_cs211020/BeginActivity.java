package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by JacksonEkis on 4/6/15.
 */
public class BeginActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_begin_screen);
    }

    public void playButton(View v) {
        IntentLauncher launcher = new IntentLauncher();
        launcher.start();
    }

    public void changeDifficultyButton(View v) {
        setContentView(R.layout.difficulty);
    }

    public void howToPlayButton(View v) {
        setContentView(R.layout.how_to_play);
    }

    public void setDifficulty(View v) {
        String difficulty = (String) v.getTag();
        if (difficulty.equals("EASY")) {
            MainActivity.setDifficulty(0);
        } else if (difficulty.equals("MEDIUM")) {
            MainActivity.setDifficulty(1);
        } else if (difficulty.equals("HARD")) {
            MainActivity.setDifficulty(2);
        } else {
            throw new IllegalArgumentException("DIFFICULTY MUST BE EITHER EASY, MEDIUM, OR HARD.");
        }
    }

    public void returnToScreen(View v) {
        setContentView(R.layout.splash_begin_screen);
    }

    private class IntentLauncher extends Thread {
        @Override
        public void run() {
            Intent intent = new Intent(BeginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
