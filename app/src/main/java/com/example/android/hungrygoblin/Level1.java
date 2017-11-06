package com.example.android.hungrygoblin;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;

import java.util.Timer;
import java.util.TimerTask;

public class Level1 extends GameLevel {

    GoblinView goblin = null;
    FoodView mFoodView = null;
    ScoreView mScoreView = null;

    private int screenWidth, screenHeight;
    private float foodAngle, rotationSpeed;
    private int score, negativeNumber;

    Handler RedrawHandler = new Handler(); //so redraw occurs in main thread
    Timer mTmr = null;
    TimerTask mTsk = null;
    DisplayMetrics displaymetrics = new DisplayMetrics();

    android.graphics.PointF mGoblinPos, mGoblinSpd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //hide title bar
        getWindow().setFlags(0x00000000,
                LayoutParams.FLAG_FULLSCREEN| LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_level);
        //create pointer to main screen
        final FrameLayout mainView = (android.widget.FrameLayout) findViewById(R.id.main_view);

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        mGoblinSpd = new android.graphics.PointF();
        mGoblinPos = new android.graphics.PointF();

        score = 0;
        foodAngle = 0;
        rotationSpeed = 1.0f;
        negativeNumber = 1;
        mGoblinSpd.x = 0;
        mGoblinSpd.y = 0;

        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenWidth = displaymetrics.widthPixels;
        screenHeight = displaymetrics.heightPixels;

        goblin = new GoblinView(this, 400, 900);
        goblin.setX(screenWidth/2);
        goblin.setY(screenHeight/2);
        mGoblinPos.x = goblin.getX();
        mGoblinPos.y = goblin.getY();
        mFoodView = new FoodView(this);
        mFoodView.setX(screenWidth/2);
        mFoodView.setY(screenHeight/2);
        mScoreView = new ScoreView(this, 8, 8, "Apples ");

        mainView.addView(goblin);
        mainView.addView(mFoodView);
        mainView.addView(mScoreView);

        //listener for accelerometer, use anonymous class for simplicity
        ((SensorManager)getSystemService(Context.SENSOR_SERVICE)).registerListener(
                new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        //set ball speed based on phone tilt (ignore Z axis)
                        mGoblinSpd.x = -event.values[0];
                        //mGoblinSpd.y = event.values[1];
                        //timer event will redraw ball
                    }
                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {} //ignore this event
                },
                ((SensorManager)getSystemService(Context.SENSOR_SERVICE))
                        .getSensorList(Sensor.TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    //For state flow see http://developer.android.com/reference/android/app/Activity.html
    @Override
    public void onPause() //app moved to background, stop background threads
    {
        mTmr.cancel(); //kill\release timer (our only background thread)
        mTmr = null;
        mTsk = null;
        super.onPause();
    }


    @Override
    public void onResume() //app moved to foreground (also occurs at app startup)
    {

        spawnTimer = new Timer();
        spawnTask = new TimerTask() {
            @Override
            public void run() {

                //Force the following to run on the UI thread
                //Creates new rows of obstacles at 1 second intervals
                /*
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        rand = r.nextInt(8);

                        for (int x = 0; x < 8; x++) {
                            if (x != rand) {
                                testSpawnableItem = new ObstacleView(getApplicationContext());
                            }
                            else{
                                testSpawnableItem = new FoodView(getApplicationContext());
                            }

                            testSpawnableItem.setX(x * standardWidth);
                            testSpawnableItem.setY(standardWidth);
                            testSpawnableItem.setSize(standardWidth, standardWidth);
                            spawnableItemList.add(testSpawnableItem);
                            mainView.addView(testSpawnableItem);

                        }
                    }});*/
                //android.util.Log.d("HungryGoblin", "MainView size: " + mainView.getChildCount());
                //android.util.Log.d("HungryGoblin", "ObstacleList size: " + spawnableItemList.size());
            }
        };

        spawnTimer.schedule(spawnTask, 10, 1000);


        //create timer to move ball to new position
        mTmr = new Timer();
        mTsk = new TimerTask() {
            public void run() {
                //if debugging with external device,
                //  a cat log viewer will be needed on the device
                android.util.Log.d(
                        "TiltBall","Timer Hit - " + mGoblinPos.x + ":" + mGoblinPos.y);
                //move ball based on current speed
                mGoblinPos.x += mGoblinSpd.x;
                mGoblinPos.y += mGoblinSpd.y;
                //if ball goes off screen, reposition to opposite side of screen
                if (mGoblinPos.x > screenWidth) goblin.setX(0);//
                if (mGoblinPos.y > screenHeight) goblin.setY(0);//mGoblinPos.y=0;
                if (mGoblinPos.x < 0) goblin.setY(screenWidth);//mGoblinPos.x=screenWidth;
                if (mGoblinPos.y < 0) goblin.setY(screenHeight);// mGoblinPos.y=screenHeight;
                //update ball class instance
                goblin.setX(mGoblinPos.x);
                goblin.setY(mGoblinPos.y);

                //mTargetView.mX += 0;// mBallPos.x;
                //mTargetView.mY -= 1.0f;

                //float angleDeg = (targetAngle * 360f + 90) % 360;
                float angleRad = (float) Math.toRadians(foodAngle);

                foodAngle += rotationSpeed;


                // r = radius, cx and cy = center point, a = angle (radians)
                mFoodView.setY(mFoodView.getY() + (float)(Math.cos(angleRad) * 4) * rotationSpeed);
                mFoodView.setX(mFoodView.getX() + negativeNumber * (float)(Math.sin(angleRad) * 4) * rotationSpeed);
                //mFoodView.mX += (Math.cos(angleRad) * 4) * rotationSpeed;// = (float) (mScrWidth/2 + 64 * Math.cos(angleRad));
                //mFoodView.mY += negativeNumber * (Math.sin(angleRad) * 4) * rotationSpeed;
                //mTargetView.mY = (float) (mScrHeight + 64 * Math.sin(angleRad));

                //mTargetView.mY = mBallPos.y;

                if(((mFoodView.mX-goblin.mX) * (mFoodView.mX-goblin.mX)) + ((mFoodView.mX-goblin.mX) * (mFoodView.mX-goblin.mX)) <= 20){
                    score++;
                    mScoreView.setScore(score);
                }

                if(foodAngle % 360 == 0){
                    negativeNumber = negativeNumber * -1;
                }

                //redraw ball. Must run in background thread to prevent thread lock.
                RedrawHandler.post(new Runnable() {
                    public void run() {
                        //mBallView.invalidate();
                        //mTargetView.invalidate();
                        //mScoreView.invalidate();
                    }});

            }}; // TimerTask

        mTmr.schedule(mTsk,10,10); //start timer
        super.onResume();

    } // onResume


    @Override
    public void onDestroy() //main thread stopped
    {
        super.onDestroy();
        System.runFinalizersOnExit(true); //wait for threads to exit before clearing app
        android.os.Process.killProcess(android.os.Process.myPid());  //remove app from memory
    }

    //listener for config change.
    //This is called when user tilts phone enough to trigger landscape view
    //we want our app to stay in portrait view, so bypass event
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        //super.onConfigurationChanged(newConfig);
    }

}
