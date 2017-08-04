package com.example.android.hungrygoblin;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by Wyatt on 7/26/2017.
 */

public class BackgroundView extends SpawnableItem {

    public BackgroundView(Context context) {
        super(context);
        this.mX = 0;
        this.mY = 0;
        this.setImageResource(R.drawable.bg);
        this.layoutParams = new LinearLayout.LayoutParams(512, 1024);
        this.setLayoutParams(layoutParams);
    }
}
