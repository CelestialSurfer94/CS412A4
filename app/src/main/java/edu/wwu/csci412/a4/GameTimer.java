package edu.wwu.csci412.a4;


import android.app.Activity;
import android.app.FragmentManager;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.TimerTask;

public class GameTimer extends TimerTask  {
    private Paranoid game;
    private GameView gameView;
    private GameStatus gameStatus;
    private SoundPool pool;
    int testSound;
    int blip2Sound;

    public GameTimer( Paranoid game, GameView view ) {
        gameView = view;
        this.game = game;
        pool = new SoundPool(2,AudioManager.STREAM_MUSIC,0);
        testSound = pool.load(gameView.getContext(),R.raw.pong_blip,1);
        blip2Sound = pool.load(gameView.getContext(),R.raw.pong_blip1,1);

        //game.startDuckFromRightTopHalf( );
    }

    public void run( ) {
        if(game.started) { //current game is running, not yet playing.
            ((MainActivity) gameView.getContext()).setTexView0(""+game.lives);
            if(!game.paused) { //shoot initial bullet
                if (!game.hasFired()) {
                    //fire bullet
                    game.moveBullet();
                    game.setHasFired(true);
                } else if (!game.init) { //everything intialized, playing game
                    int moveType = game.moveBullet();
                    if(moveType == 0){ //hit the paddle
                        pool.play(blip2Sound, 1.0f, 1.0f, 1, 0, 1.0f);

                    } else if(moveType == 1){ //hit a block
                        int totalTargets = game.getTotalTargets();
                        int remainingTargets = game.countTargetsRemaining();
                        pool.play( testSound, 1.0f, 1.0f, 1, 0, 1.0f );

                        String gameStatus = "";
                        if(remainingTargets > 0){
                            gameStatus = remainingTargets + " / " + totalTargets;
                        } else {
                            gameStatus = "You Won!";
                            game.init = true;
                            game.paused = true;
                            game.justDied = true;
                        }
                        ((MainActivity)gameView.getContext()).setTextView1(gameStatus);
                    }else if(moveType == 2){

                    } else if(moveType == 3){ //lost
                        ((MainActivity)gameView.getContext()).setTextView1("You Lost!");

                    }

                }
            }
            //gameView.invalidate();

        }
        gameView.invalidate();
    }
}
