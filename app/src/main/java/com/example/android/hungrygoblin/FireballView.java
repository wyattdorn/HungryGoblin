package com.example.android.hungrygoblin;

/**
 * Created by Wyatt on 7/21/2017.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FireballView extends SpawnableItem {

    public float mX;
    public float mY;
    private LinearLayout.LayoutParams layoutParams = null;

    //construct new fireball object
    public FireballView(Context context, int width, int height) {
        super(context);
        //this.mX = x;
        //this.mY = y;
        this.setImageResource(R.drawable.fireball1);
        layoutParams = new LinearLayout.LayoutParams(width, height);
        this.setLayoutParams(layoutParams);
    }

    //called by invalidate()
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}