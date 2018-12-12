package edu.wwu.csci412.a4;


import android.gesture.Gesture;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {
    GameView gv;
    GestureDetector gd;
    Paranoid game;
    Timer gt;

    public interface mCallbacks{

    }

    public GameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //game = new Paranoid();
        gv = new GameView(getActivity());
        gv.setClickable(true);
        gv.setFocusable(true);
        TouchHandler th = new TouchHandler();
        gd = new GestureDetector(getActivity(),th);
        gd.setOnDoubleTapListener(th);
        gv.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    gv.drawPaddle(event);
                }
                return true;
            }
        });

        return gv;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init(){
        gt = new Timer();
        game =  Paranoid.getInstance(20,1);
        gt.schedule(new GameTimer(game,gv),0,gv.DELTA_TIME);
    }

    public void startTask(){
        //gt.schedule(new GameTimer(gv),0, 4000);
        //game.started = true;

    }



    private class TouchHandler extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            gv.drawPaddle(e2);
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }
}
