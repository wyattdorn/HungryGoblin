package com.example.android.hungrygoblin;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Wyatt on 8/2/2017.
 */

public class SpawnableItem extends ImageView {

    protected float mX;
    protected float mY;

    protected LinearLayout.LayoutParams layoutParams = null;

    //construct new spawnable object
    public SpawnableItem(Context context) {
        super(context);
    }

    public void moveDown(int y){
        this.mY = this.getY();
        //this.setY(100);
        this.setY(this.mY+y);
    }

    public void setSize(int x, int y){
        layoutParams = new LinearLayout.LayoutParams(x, y);
        this.setLayoutParams(layoutParams);
    }

    //called by invalidate()
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
