package edu.wwu.csci412.a4;

import android.graphics.Point;
import android.graphics.Rect;

public class Paranoid {
    private static Paranoid instance = null;

    private boolean hasFired;
    private Rect paddleRect;

    private int totalTargets;
    private int numTargetsLeft;

    private Point gameBall;
    private int gameBallRadius;
    private float gameBallSpeed;
    private double ballAngle;

    private int deltaTime;

    public boolean init = true;
    public boolean started = false;
    public boolean paused = true;

    public int xPositive;
    public int yPositive;
    public int maxX;
    public int maxY;
    public int rows;
    public int cols;


    int lives;
    public boolean justDied = true;

    private boolean [][] targetsHit;
    private Rect [][] targetRects;


    public static Paranoid getInstance(int ballR, float speed){
        if(instance == null){
            instance = new Paranoid(ballR, speed);
        }
        return instance;
    }


    private Paranoid(int ballRadius, float speed){
        //gameBall = new Point(paddle.centerX(), paddle.centerY());
        gameBallRadius = ballRadius;
        gameBallSpeed = speed;
        hasFired = false;
        xPositive = 1;
        yPositive = 1;
        ballAngle = 45;
        lives = 3;
    }


    public void setDeltaTime(int dt){
        deltaTime = dt;
    }

    public void setMaxCoordValues(int x, int y){
        maxX = x;
        maxY = y;
    }

    public void setTargetsHit(int rows, int cols){
        targetsHit = new boolean[rows][cols];
        targetRects    = new Rect[rows][cols];
        this.rows = rows;
        this.cols = cols;
        totalTargets = rows * cols;
        numTargetsLeft = totalTargets;
    }

    public void setTarget(Rect r, int row, int col){
        targetRects[row][col] = r;
    }

    public Rect getPaddlRect(){
        return paddleRect;
    }

    public void setPaddle(Rect r){
        paddleRect = r;
    }


    public Point getPaddleCenter(){
        return new Point(paddleRect.centerX(), paddleRect.centerY());
    }

    public void setInitialBallPos(){
        gameBall = new Point(paddleRect.centerX(), paddleRect.centerY() - gameBallRadius -25);
        //canvas.drawCircle(game.getPaddleCenter().x, game.getPaddleCenter().y - game.getGameBallRadius() - 25, game.getGameBallRadius(), p);

    }

    public int getGameBallRadius(){
        return gameBallRadius;
    }

    public void movePaddleRect(int left, int right){
        paddleRect.left = left;
        paddleRect.right = right;
    }

    public boolean hasFired() {
        return hasFired;
    }

    public void setHasFired(boolean b){
        hasFired = b;
    }

    public Point getBallCenter(){
        if(gameBall == null){
            return null;
        }
        return new Point(gameBall.x, gameBall.y);
    }

   // public void start(){

   // }

    //-1 regular movement, no case to handle
    //0 hit paddle
    //1 hit a block, remove block decrement blocks remaining
    //2 died, remove a life, end game if needed.

    public int moveBullet(){
        int moveType = -1;
        if(gameBall != null) {
            int xPos = gameBall.x;
            int yPos = gameBall.y;
            if(xPos >= maxX){ //if ball hits the right boundary
                ballAngle = 45;
                xPositive = - 1;
            } else if(xPos <= 0){ //left boundary
                ballAngle = 45;
                xPositive = 1;
            } else if(yPos <=0){ // top boundary
                ballAngle = 45;
                yPositive = -1;
            } else if (yPos >= maxY){ //bottom
                ballAngle = 45;
                lives--;
                setInitialBallPos();
                paused = true;
                justDied = true;
                hasFired = false;
                if(lives == 0){
                    moveType = 3; //lost
                    init = true;
                    lives = 3;
                } else {
                    moveType = 2;
                }
            } else if(paddleHit()){ //
                moveType = 0;
                if(paddleHitLeft()){
                    determineNewBallAngle(paddleRect.left, gameBall.x,0);
                    xPositive = -1;
                } else { //paddle hit right side
                    //Rect rightRec = new Rect(paddleRect.left + paddleRect.left/2,paddleRect.top,paddleRect.right,paddleRect.bottom);
                    determineNewBallAngle(paddleRect.left + paddleRect.right/2, gameBall.x,1);
                    xPositive = 1;
                }
                yPositive = 1;
            } else if(yPos <= maxY /2){ //top half of screen
                for (int i = 0; i < rows; i++){
                    for (int j = 0; j < cols; j++) {
                        if(!targetsHit[i][j]){
                            if(hitTarget(i,j)){
                                moveType = 1;
                                targetsHit[i][j] = true;
                                numTargetsLeft--;
                                int type = determineHitType(i,j);
                                switch (type){
                                    case 0: //up
                                        yPositive = 1;
                                        break;
                                    case 1: //right
                                        xPositive = 1;
                                        break;
                                    case 2: //down
                                        yPositive = -1;
                                        break;
                                    case 3: //left
                                        xPositive = -1;
                                }
                            }
                        }
                    }
                }
            }

            double test = Math.sin(ballAngle);
            gameBall.x += xPositive * gameBallSpeed * Math.cos(ballAngle) * deltaTime;
            gameBall.y -= yPositive * gameBallSpeed * Math.sin(ballAngle) * deltaTime;
        }
        return moveType;
    }



    public boolean hitTarget(int row, int col){
        return targetRects[row][col].contains(gameBall.x, gameBall.y);
    }

    private int determineHitType(int row, int col){
        Rect hitRect = targetRects[row][col] ;
        int type;
        Rect top = new Rect(hitRect.left,hitRect.top,hitRect.right,hitRect.bottom/2);
        Rect bot = new Rect(hitRect.left,hitRect.top/2,hitRect.right,hitRect.bottom);
        Rect left = new Rect(hitRect.left,hitRect.top,hitRect.right/2,hitRect.bottom);

        if(top.contains(gameBall.x,gameBall.y)){
            type = 0;
        } else if(bot.contains(gameBall.x,gameBall.y)){
            type = 2;
        } else if(left.contains(gameBall.x, gameBall.y)){
            type = 3;
        } else {
            type = 1;
        }
        return type;
    }

    public boolean hasTargetBeenHit(int row, int col){
        return targetsHit[row][col];
    }

    public Rect getBallRect(){
        return new Rect();
    }

    public void getBulletAngle(){
    }

    private boolean paddleHit(){
        return paddleRect.contains(gameBall.x, gameBall.y + (gameBallRadius/2));
    }

    private boolean paddleHitLeft(){
        Rect leftRect = new Rect(paddleRect.left,paddleRect.top,paddleRect.right/2, paddleRect.bottom);
        return leftRect.contains(gameBall.x, gameBall.y);
    }

    private void determineNewBallAngle(int hitLeft, int ballLeft, int hitType){
        /*
        int diff = (ballLeft - hitLeft)/100;
        ballAngle = (Math.abs(diff)*2 + Math.PI/4) % Math.PI;
        if(ballAngle > 180){
            System.out.println();
        }
        */
        int diff = (ballLeft - hitLeft)/100;

        //if left hit and diff is large, pi/2
        //if right is hit and diff is large, pi/6, if diff small

        if(hitType == 0){ //left hit, constrain angles from 5pi/6 to pi/2
            ballAngle =  (((5 * Math.PI) / 6) +  diff) %( Math.PI /2);
        } else {
            //double temp  = Math.abs(((Math.PI/6) - diff));
            ballAngle = (Math.abs(diff)*2 + Math.PI/4) % Math.PI;
        }

    }

    public int countTargetsRemaining(){
        return numTargetsLeft;
    }

    public int getTotalTargets(){
        return totalTargets;
    }
}