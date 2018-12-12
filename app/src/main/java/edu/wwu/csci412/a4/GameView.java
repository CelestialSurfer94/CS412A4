package edu.wwu.csci412.a4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {
    Paint p = new Paint();
    final int RECTSIZE = 250;
    public static int DELTA_TIME = 50;
    private Paranoid game;
    Rect paddle;
    int x;
    int y;


    public GameView(Context context) {
        super(context);
        //game = new Paranoid(new Rect(0,0,0,0), 50, 1);

        game = Paranoid.getInstance(25,1);

        invalidate();
    }




    @Override
    protected void onDraw(Canvas canvas) {
        x = canvas.getWidth(); //1080
        y = canvas.getHeight(); //1765
        p.setColor(Color.GREEN);
        //draw paddle

        if(paddle == null){
            paddle = new Rect(x / 3, y - 100, 2 * x / 3, y-75);
            game.setPaddle(paddle);
        }

        //draw ball on paddle

        if(game.init) {
            game.setInitialBallPos();
            game.setDeltaTime(DELTA_TIME);
            game.setMaxCoordValues(x,y);
            game.setTargetsHit(4,6);
        }

        if(game.paused && game.justDied){
            game.setInitialBallPos();
        }


        // int testX = game.getBallCenter().x;
       // int testY = game.getBallCenter().y;
        canvas.drawRect(game.getPaddlRect(),p);
        canvas.drawCircle(game.getBallCenter().x, game.getBallCenter().y, game.getGameBallRadius(),p);

        //draw targets
        int left = x/5;
        int top = 50;
        int width = 100;
        int height = 100;
        for (int row = 0; row < 4; row++){
            for (int col = 0; col < 6; col++){
                if(game.init){
                    Rect curTarg = new Rect(left, top, left + width, top + height);
                    game.setTarget(curTarg, row, col);
                }
                if(!game.hasTargetBeenHit(row,col)){
                    canvas.drawRect(left,top,left+width,top+height,p);
                }
                left = (left + width + 5);
            }
            left = x/5;
            top = top + height + 5;
        }

        if(game.init){
            game.init = false;
        }
    }

    /*
      On touch listener inside the fragment.
         (left, top)
           ++++++++++++++++++++
           +                  +
           +                  +
           +                  +
           ++++++++++++++++++++ (right, bottom)

     */
    public void drawPaddle(MotionEvent e2){
        int xPos = (int) e2.getX();
        //paddle = new Rect(xPos - RECTSIZE, y - 100, (xPos + RECTSIZE),y-50);
        //game.setPaddleRect(paddle);
        game.movePaddleRect(xPos - RECTSIZE, xPos + RECTSIZE);

        invalidate();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public Paranoid getGame(){
        return game;
    }
}
