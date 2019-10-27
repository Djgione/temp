package com.example.wegaminouthere;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    public static float particleVelocity;
    public static final float MAXVELOCITY = 3.4f;
    private boolean running;
    private GameSurface gameSurface;
    private SurfaceHolder surfaceHolder;


    public GameThread(GameSurface gameSurface, SurfaceHolder surfaceHolder) {
        this.gameSurface = gameSurface;
        this.surfaceHolder = surfaceHolder;
    }




    @Override
    public void run() {
        long startTime = System.nanoTime();

        int count = 0;
        long fakeStartTime = startTime;
        while(running) {
            Canvas canvas = null;
            try {
                //get canvas from holder and lock
                canvas = this.surfaceHolder.lockCanvas();

                //synchronized
                synchronized (canvas) {
                    this.gameSurface.update();
                    this.gameSurface.draw(canvas);
                }
            }catch(Exception e){
                    //do nothing
                    ;
            }finally{
                if(canvas!=null){
                    //unlock canvas
                    this.surfaceHolder.unlockCanvasAndPost(canvas);

                }
            }



            long now = System.nanoTime();
            float initialVelocity = -.03f;
            long secondsElapsed = (now-startTime)/1000000000;
            if(particleVelocity<MAXVELOCITY)
                particleVelocity = initialVelocity * (float)(secondsElapsed);

            int forloopControl = (int)secondsElapsed%5;
            if(forloopControl < 3)
            {
                forloopControl = 2;
            }
            count++;

            boolean determinant = secondsElapsed > 5;

            if(determinant) {
                if (count == 60) {
                    for (int i = 0; i < forloopControl; i++) {
                        this.gameSurface.getParticles().add(new Particle(gameSurface, gameSurface.getParticleBitmap(), 2000, (int) (Math.random() * 1000) + 1, particleVelocity));
                    }
                    count = 0;
                }
            }
            else{
                if(count == 100){
                    for(int i = 0; i < forloopControl; i++) {
                        this.gameSurface.getParticles().add(new Particle(gameSurface,gameSurface.getParticleBitmap(),2000,(int) (Math.random() * 1000) + 1, particleVelocity));
                    }
                    count = 0;
                }
            }

            //interval to redraw
            //change nano to millisecond
            long waitTime = (now - fakeStartTime)/10000000;
            if(waitTime<10) {
                waitTime = 10; // millisecond
            }
            System.out.print("Wait Time=" + waitTime);


            try{
                //sleep
                this.sleep(waitTime);
            }catch(InterruptedException e){
                //do nothing
            }
            fakeStartTime = System.nanoTime();
            System.out.print(".");
        }
    }


    public void setRunning(boolean running){
        this.running = running;
    }
}


