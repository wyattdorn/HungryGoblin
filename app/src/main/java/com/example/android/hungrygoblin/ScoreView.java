package com.example.android.hungrygoblin;

/**
 * Created by Wyatt on 7/22/2017.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class ScoreView extends View {

    public float mX;
    public float mY;
    private int textSize, mScore;
    private String mText;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //construct new ball object
    public ScoreView(Context context, float x, float y, String t) {
        super(context);
        //color hex is [transparency][red][green][blue]
        mPaint.setColor(0xFFff7777); //not transparent. color is green

        this.textSize = 32;
        this.mScore = 0;
        this.mX = x;
        this.mY = y;
        this.mText = t; //radius
    }

    //called by invalidate()
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(12);
        mPaint.setTextSize(32);
        canvas.drawText(mText + mScore, mX, mY + textSize, mPaint);
    }

    public void setScore(int s){
        this.mScore = s;
    }
}