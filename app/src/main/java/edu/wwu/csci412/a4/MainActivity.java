package edu.wwu.csci412.a4;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements GameStatus.Callbacks {
    private FragmentTransaction transaction;
    public int test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Paranoid game = Paranoid.getInstance(20,2);
        final GameFragment gameFragment = new GameFragment();
        FragmentManager manager = this.getFragmentManager();
        GameController gc = new GameController();
        GameStatus gs = new GameStatus();

        transaction = manager.beginTransaction();
        transaction.add(R.id.game_view_fragment,gameFragment,"gameFragment");
        transaction.add(R.id.game_controller_fragment, gc, "gameController");
        transaction.add(R.id.game_status_fragment, gs, "gameStatus");

        Button playButon = (Button)findViewById(R.id.play_button);
        playButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "play was pressed", Toast.LENGTH_SHORT).show();
                //gameFragment.startTask();
                game.started = true;
                game.paused = false;
                if(game.justDied){
                    game.justDied = false;
                }
                //game.justDied = !game.justDied;
                setTexView0("Lives left: "+ game.lives);
                TextView status =  findViewById(R.id.game_status_1);
                status.setText("Targets remaining: " + game.countTargetsRemaining()+ " / " + game.getTotalTargets());
            }
        });
        Button pauseButton = (Button) findViewById(R.id.pause_button);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "pause was pressed", Toast.LENGTH_SHORT).show();
                game.paused = true;
                TextView status =  findViewById(R.id.game_status_1);
                status.setText("Paused...");
            }
        });

        Button stopButton = (Button) findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "stop was pressed", Toast.LENGTH_SHORT).show();
                game.init = true;
                game.paused = true;
                game.justDied = true;
                game.lives = 3;
                TextView status =  findViewById(R.id.game_status_1);
                status.setText("Press play!");
                //gameFragment.getView().invalidate();
            }
        });
    }

    @Override
    public void setTexView0(String s) {
        TextView ts = (TextView) findViewById(R.id.game_status_0);
        Paranoid game = Paranoid.getInstance(20,1);
        int lives = game.lives;
        ts.setText(""+lives);
    }

    public void setTextView1(String s){
        TextView ts = (TextView) findViewById(R.id.game_status_1);
        ts.setText(s);
    }

    public void play(){

    }

}
