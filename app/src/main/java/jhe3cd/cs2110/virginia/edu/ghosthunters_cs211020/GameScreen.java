package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class GameScreen extends Activity {

    private static int score = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game_over);
        TextView tView = (TextView) findViewById(R.id.scoreDisplay);
        tView.append("" + score);
    }

    public void play(View view){
        if(view.getId()== R.id.button10){
            Intent in = new Intent(this, BeginActivity.class);
            this.startActivity(in);
            this.finish();
        }

    }

    public static void setScore(int newScore) {
        score = newScore;
    }

}
