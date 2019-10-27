package com.example.wegaminouthere;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Particle extends GameObject {

    private Bitmap image;

    private long lastDrawNanoTime = -1;
    private GameSurface gameSurface;
    private int movingVectorX = 10;
    public static float velocity;

    public Particle(GameSurface gameSurface, Bitmap image, int x, int y, float velocity){
        super(image, 1,1,x,y);

        this.gameSurface = gameSurface;
        this.image = createSubImageAt(0,0);
        this.velocity = velocity;
    }

    public Bitmap getCurrentMoveBitmap(){
        return image;
    }


    public int update(){

        long now = System.nanoTime();

        if(lastDrawNanoTime==-1)
            lastDrawNanoTime = now;

        int deltaTime = (int)((now - lastDrawNanoTime)/1000000);

        float distance = deltaTime* velocity;

        int temp = x;
        this.x = x + (int)(distance*movingVectorX/Math.abs(movingVectorX));
        //velocity *= 1.02;

        return temp;

    }


    public void draw(Canvas canvas){
        Bitmap bitmap = this.getCurrentMoveBitmap();
        canvas.drawBitmap(bitmap,x,y,null);

        this.lastDrawNanoTime = System.nanoTime();
    }



}
