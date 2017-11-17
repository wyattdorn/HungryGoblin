package com.example.android.hungrygoblin;

/**
 * Created by Wyatt on 7/21/2017.
 */

import android.content.Context;
import android.widget.LinearLayout;

public class FoodView extends SpawnableItem {

    //construct new food object
    public FoodView(Context context) {
        super(context);
        this.mX = 0;
        this.mY = 0;
        this.setImageResource(R.drawable.apple1);
        this.layoutParams = new LinearLayout.LayoutParams(100, 100);
        this.setLayoutParams(layoutParams);
    }

}