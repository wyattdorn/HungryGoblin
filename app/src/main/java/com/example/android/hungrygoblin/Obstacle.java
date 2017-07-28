package com.example.android.hungrygoblin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Wyatt on 7/26/2017.
 */

public class Obstacle extends ImageView {

    private String obstacleType = null;
    private float mX, mY;
    private int sizeX, sizeY;
    private boolean doesDamage;
    private LinearLayout.LayoutParams layoutParams = null;

    public Obstacle(Context context) {
        super(context);
        this.mX = 0;
        this.mY = 0;
        this.sizeX = 100;
        this.sizeY = 100;
        this.doesDamage = false;
        this.setImageResource(R.drawable.fleurdelis64);
        layoutParams = new LinearLayout.LayoutParams(0, 0);
        this.setLayoutParams(layoutParams);

        if(this.obstacleType=="spikes"){
            doesDamage = true;
        }
        else {
            doesDamage = false;
        }
    }

    public void moveDown(int y){
        this.mY = this.getY();
        //this.setY(100);
        this.setY(this.mY+y);
    }

    public void setSize(int x, int y){
        //this.sizeX = x;
        //this.sizeY = y;
        layoutParams = new LinearLayout.LayoutParams(x, y);
        this.setLayoutParams(layoutParams);
    }

    //called by invalidate()
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
