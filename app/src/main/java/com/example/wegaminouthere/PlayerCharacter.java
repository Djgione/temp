package com.example.wegaminouthere;
import android.graphics.Bitmap;
import android.graphics.Canvas;


public class PlayerCharacter extends GameObject {

    private static final int ROW_LEFT_TO_RIGHT = 2;

    //row index of image being used
    private int rowUsing = ROW_LEFT_TO_RIGHT;

    private int colUsing;

    private final Bitmap[] leftToRights;



    //velo of char(pixel / millisecond)
    public static final float maxVELOCITY = 0.7f;
    public static float velocity = 0.0f;

    private int movingVectorY = 10;
    private long lastDrawNanoTime = -1;

    private GameSurface gameSurface;

    public PlayerCharacter(GameSurface gameSurface, Bitmap image, int x, int y){
        super(image,4,3,x,y);

        this.gameSurface = gameSurface;

        this.leftToRights = new Bitmap[colCount];

        for(int col = 0; col < this.colCount; col++){
            this.leftToRights[col] = this.createSubImageAt(ROW_LEFT_TO_RIGHT,col);

        }
    }

    public Bitmap getCurrentMoveBitmap(){
        return this.leftToRights[this.colUsing];
    }

    public void update(){
        this.colUsing++;
        if(colUsing>=this.colCount)
            this.colUsing=0;

        long now = System.nanoTime();

        if(lastDrawNanoTime==-1)
            lastDrawNanoTime=now;

        int deltaTime = (int)((now - lastDrawNanoTime)/1000000);

        if(velocity > maxVELOCITY)
            velocity = maxVELOCITY;
        float distance = velocity *deltaTime;

        double movingVectorLength = Math.sqrt(movingVectorY * movingVectorY);

        this.y = y + (int)(distance*movingVectorY/movingVectorLength);



        if(this.y<0) {
            this.y = 0;
            //this.movingVectorY = -this.movingVectorY;
        }
        else if(this.y>this.gameSurface.getHeight()-height){
            this.y = this.gameSurface.getHeight()-height;
            //this.movingVectorY = -this.movingVectorY;
        }

        this.rowUsing = ROW_LEFT_TO_RIGHT;

    }

    public void draw(Canvas canvas){
        Bitmap bitmap = this.getCurrentMoveBitmap();
        canvas.drawBitmap(bitmap,x,y,null);
        //last draw time
        this.lastDrawNanoTime = System.nanoTime();
    }

    public void setMovingVector(int movingVectorY){
        this.movingVectorY = movingVectorY;
    }

}








