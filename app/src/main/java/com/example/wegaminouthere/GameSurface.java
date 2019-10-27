package com.example.wegaminouthere;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

/**
 *
 * MAX X VALUE IS 2000, 0 -> 2000 LEFT TO RIGHT
 * MAX Y VALUE IS 1000, 0 -> 1000 TOP TO BOTTOM
 * check gameobject for get methods
 * getY returns center of the particle
 * if the y value of player is touching any y value of the particle death, do the if statement
 */



public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    public static long startTime;
    private GameThread gameThread;
    private double maxPower;
    private PlayerCharacter player1;
    public static ArrayList<Particle> particles;
    public Bitmap particle;

    public GameSurface(Context context) {
        super(context);

        //make gamesurface focsuable to handle event
        this.setFocusable(true);

        //set callback
        this.getHolder().addCallback(this);
    }
    private boolean collisionChecker(Particle p){
        double playerRadius = 16;
        double particleRadius = 20;
        double radiusSum = playerRadius + particleRadius;
        double cPointX = ( (player1.getX() * particleRadius) + (p.getX() * playerRadius) ) / radiusSum;
        double cPointY = ( (player1.getY() * particleRadius) + (p.getY() * playerRadius) ) / radiusSum;
        //double dx = (p.getX() + particleRadius)- (player1.getX() + playerRadius);
        //double dy = (p.getY() + particleRadius) - (player1.getY() + playerRadius);
        double distance =  Math.sqrt( Math.pow(player1.getX() - p.getX(), 2) + Math.pow(player1.getY() - p.getY(), 2)  );


        if(distance < radiusSum){
            return true;
        }
//        if((p.getX() - p.getWidth()/2)<= (player1.getX() + (player1.getWidth()/2)))
//        {
//
//            if((player1.getY() + (player1.getHeight()/2)) <= (p.getY() + (p.getHeight()/2)) || (p.getY() - (p.getHeight()/2)) <= (player1.getY() - (player1.getHeight()/2))){
//                return true;
//            }
//        }
        //getY returns center of the particle
        //if the y value of player is touching any y value of the particle death, do the if statement

        return false;
    }

    public void update() {
        this.player1.update();
        for(int i = 0; i < particles.size(); i++){
            if(collisionChecker(particles.get(i))){
                Context context = getContext();
                ((MainActivity)context).finish();
            }
            if(particles.get(i).update() < -15)
            {
                particles.remove(i);
                i--;
            }
        }


    }

    public ArrayList<Particle> getParticles(){
        return particles;
    }

    public void draw(Canvas canvas){
        super.draw(canvas);

        this.player1.draw(canvas);
        for(int i = 0; i < particles.size(); i++){
            particles.get(i).draw(canvas);
        }
    }

    //implmement method of surfaceholder.callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap playerBitmap1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.player1);
        this.particle = BitmapFactory.decodeResource(this.getResources(),R.drawable.particles);
        this.player1 = new PlayerCharacter(this,playerBitmap1,0,1000);

        particles = new ArrayList<>();
        this.gameThread = new GameThread(this,holder);
        startTime = System.nanoTime();
        this.gameThread.setRunning(true);
        this.gameThread.start();

    }

    //implements method of surfaceholder.callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


    }

    //implement method of surfaceholder.callback
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        boolean retry = true;
        while(retry) {
            try{
                this.gameThread.setRunning(false);

                //parent threat must wait until end of gamethread
                this.gameThread.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            retry = true;
        }
    }

    public void setMaxPower(double maxPower){
        this.maxPower=maxPower;
    }

    public Bitmap getParticleBitmap(){
        return particle;
    }

    public boolean onInput(double bluetoothalEnergy){

            double y = bluetoothalEnergy * 100;
            y/=maxPower;

            int ans = (int)(y);
            if(ans>99)
                ans=99;
            int movingVectorY = ans-this.player1.getY();


            this.player1.setMovingVector(movingVectorY);
            return true;
    }
}
