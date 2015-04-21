package jhe3cd.cs2110.virginia.edu.ghosthunters_cs211020;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class GameScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
    }

    public void play(View view){
        if(view.getId()== R.id.button10){
            Intent in = new Intent(this, SplashActivity.class);
            this.startActivity(in);
            this.finish();
        }

    }

}
