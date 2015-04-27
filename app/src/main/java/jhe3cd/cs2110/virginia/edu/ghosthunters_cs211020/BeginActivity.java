package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

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

    public void itemsButton(View v) {
        setContentView(R.layout.items);
    }

    public void setDifficulty(View v) {
        String difficulty = (String) v.getTag();
        TextView difficultyIndicator = (TextView)findViewById(R.id.difficultyIndicator);
        TextView difficultyDescriptor = (TextView)findViewById(R.id.difficultyDesc);
        if (difficulty.equals("EASY")) {
            MainActivity.setDifficulty(0);
            difficultyIndicator.setText(R.string.easyName);
            difficultyDescriptor.setText(R.string.easyDesc);
        } else if (difficulty.equals("MEDIUM")) {
            MainActivity.setDifficulty(1);
            difficultyIndicator.setText(R.string.mediumName);
            difficultyDescriptor.setText(R.string.mediumDesc);
        } else if (difficulty.equals("HARD")) {
            MainActivity.setDifficulty(2);
            difficultyIndicator.setText(R.string.hardName);
            difficultyDescriptor.setText(R.string.hardDesc);
        } else {
            throw new IllegalArgumentException("DIFFICULTY MUST BE EITHER EASY, MEDIUM, OR HARD.");
        }
    }

    public void setItem(View v) {
        String item = (String) v.getTag();
        TextView itemIndicator = (TextView)findViewById(R.id.itemName);
        TextView itemDescriptor = (TextView)findViewById(R.id.itemDesc);
        switch(item) {
            case "extraHealth":
                itemIndicator.setText(R.string.extraHealthName);
                itemDescriptor.setText(R.string.extraHealthDesc);
                break;
            case "fear":
                itemIndicator.setText(R.string.fearName);
                itemDescriptor.setText(R.string.fearDesc);
                break;
            case "rayGun":
                itemIndicator.setText(R.string.rayGunName);
                itemDescriptor.setText(R.string.rayGunDesc);
                break;
            case "shield":
                itemIndicator.setText(R.string.shieldName);
                itemDescriptor.setText(R.string.shieldDesc);
                break;
            case "timeFreezer":
                itemIndicator.setText(R.string.timeFreezerName);
                itemDescriptor.setText(R.string.timeFreezerDesc);
                break;
        }
    }

    public void returnToScreen(View v) {
        if(v.getTag() != null && v.getTag().equals("items")) {
            setContentView(R.layout.how_to_play);
        } else {
            setContentView(R.layout.splash_begin_screen);
        }
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
