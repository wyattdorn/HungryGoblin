package com.example.android.hungrygoblin;

/**
 * Created by Wyatt on 7/21/2017.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FoodView extends ImageView {

    public float mX;
    public float mY;

    private int HP;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private LinearLayout.LayoutParams layoutParams = null;

    //construct new ball object
    public FoodView(Context context, float x, float y) {
        super(context);
        //color hex is [transparency][red][green][blue]
        mPaint.setColor(0xFFff7777); //not transparent. color is green
        this.mX = x;
        this.mY = y;
        this.setImageResource(R.drawable.apple1);
        layoutParams = new LinearLayout.LayoutParams(100, 100);
        this.setLayoutParams(layoutParams);
    }

    //called by invalidate()
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);
        //mPaint.setColor(0x40000000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(12);
        //canvas.drawCircle(mX, mY, mR, mPaint);
    }
}