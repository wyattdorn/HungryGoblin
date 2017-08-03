package com.example.android.hungrygoblin;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by Wyatt on 7/26/2017.
 */

public class ObstacleView extends SpawnableItem {

    public ObstacleView(Context context) {
        super(context);
        this.mX = 0;
        this.mY = 0;
        this.setImageResource(R.drawable.fleurdelis64);
        this.layoutParams = new LinearLayout.LayoutParams(100, 100);
        this.setLayoutParams(layoutParams);
    }
}
