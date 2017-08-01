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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameLevel extends Activity {

    private FrameLayout mainView;// = null;

    GoblinView goblin = null;
    ScoreView mScoreView = null;
    List<Obstacle> obstacleList = null;
    ViewGroup obstacleGroup = null;

    Random r = null;

    Obstacle obstacle = null;
    Obstacle testObstacle = null;

    private int screenWidth, screenHeight;
    private int score, negativeNumber;
    private int standardWidth;
    int rand;

    Handler RedrawHandler = new Handler(); //so redraw occurs in main thread
    Timer mTmr = null;
    TimerTask mTsk = null;
    Timer spawnTimer = null;
    TimerTask spawnTask = null;

    private Handler handler = null;//
    private Runnable runnable = null;

    DisplayMetrics displaymetrics = new DisplayMetrics();

    android.graphics.PointF mGoblinPos, mGoblinSpd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //hide title bar
        getWindow().setFlags(0x00000000,
                LayoutParams.FLAG_FULLSCREEN| LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_level);

        mainView = (FrameLayout) findViewById(R.id.main_view);
        //final FrameLayout mainView = (FrameLayout) findViewById(R.id.main_view);

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenWidth = displaymetrics.widthPixels;
        screenHeight = displaymetrics.heightPixels;
        standardWidth = screenWidth/8;

        handler = new Handler();
        handler.postDelayed(runnable, 100);


        obstacleList = new ArrayList<Obstacle>();

        r = new Random();
        int rand  = r.nextInt(8);

        for(int x = 0; x < 8; x++){
            if(x != rand){
                obstacle = new Obstacle(this);
                obstacle.setX(x*standardWidth);
                obstacle.setY(standardWidth);
                obstacle.setSize(standardWidth,standardWidth);
                obstacleList.add(obstacle);
            }
        }

        mGoblinSpd = new android.graphics.PointF();
        mGoblinPos = new android.graphics.PointF();

        score = 0;

        negativeNumber = 1;
        mGoblinSpd.x = 0;
        mGoblinSpd.y = 0;

        goblin = new GoblinView(this, standardWidth, standardWidth);
        goblin.setX(screenWidth/2);
        goblin.setY(screenHeight*3/4);
        mGoblinPos.x = goblin.getX();
        mGoblinPos.y = goblin.getY();

        mScoreView = new ScoreView(this, 8, 8, "Score ");

        mainView.addView(goblin);
        mainView.addView(mScoreView);
        //mainView.addView(testObstacle);

        for(int x = 0; x < obstacleList.size(); x++){
            mainView.addView(obstacleList.get(x));
        }

        //listener for accelerometer, use anonymous class for simplicity
        ((SensorManager)getSystemService(Context.SENSOR_SERVICE)).registerListener(
                new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        //set goblin speed based on phone tilt
                        mGoblinSpd.x = -event.values[0];
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
        spawnTimer.cancel();
        spawnTimer = null;
        super.onPause();
    }


    @Override
    public void onResume() //app moved to foreground (also occurs at app startup)
    {

        //create timer to move goblin to new position
        mTmr = new Timer();
        mTsk = new TimerTask() {
            public void run() {
                //If goblin hits either side of the screen, it stops
                if (mGoblinPos.x >= screenWidth - standardWidth && mGoblinSpd.x >= 0){
                    mGoblinSpd.x = 0;
                }
                if (mGoblinPos.x <= 0 && mGoblinSpd.x <= 0){
                    mGoblinSpd.x = 0;
                }

                //Update goblin position
                mGoblinPos.x += mGoblinSpd.x;
                goblin.setX(mGoblinPos.x);

                //android.util.Log.d("HungryGoblin", "X Position: " + mGoblinPos.x + "X Speed: " + mGoblinSpd.x);

                /******************************************
                 * To be implemented
                 * (Allow for the goblin to be pushed down screen upon collision
                mGoblinPos.y += mGoblinSpd.y;
                goblin.setY(mGoblinPos.y);
                ******************************************/

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(int x = 0; x < obstacleList.size(); x++){
                            if (obstacleList.get(x) != null) {
                                if(obstacleList.get(x).getY()>=screenHeight) {
                                    removeView(obstacleList.get(x));
                                    obstacleList.remove(x);
                                    android.util.Log.d("HungryGoblin", "DELETED");
                                }
                            }
                        }
                        for(int x = 0; x < obstacleList.size(); x++){
                            if (obstacleList.get(x) != null) {
                                obstacleList.get(x).moveDown(5);
                            }
                        }
                    }
                });

                //Redraw Goblin, score, and obstacles. Must run in background thread to prevent thread lock.
                RedrawHandler.post(new Runnable() {
                    public void run() {
                        for(int x = 0; x < obstacleList.size(); x++) {
                            obstacleList.get(x).invalidate();
                        }
                        goblin.invalidate();
                        mScoreView.invalidate();
                    }});

            }}; // TimerTask

        mTmr.schedule(mTsk,10,10); //start timer

        spawnTimer = new Timer();
        spawnTask = new TimerTask() {
            @Override
            public void run() {

                //Force teh following to run on the UI thread
                //Creates new rows of obstacles at 1 second intervals
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rand = r.nextInt(8);

                        for (int x = 0; x < 8; x++) {
                            if (x != rand) {

                                obstacle = new Obstacle(getApplicationContext());
                                obstacle.setX(x * standardWidth);
                                obstacle.setY(standardWidth);
                                obstacle.setSize(standardWidth, standardWidth);
                                obstacleList.add(obstacle);
                                mainView.addView(obstacle);
                            }
                        }
                    }});
                android.util.Log.d("HungryGoblin", "MainView size: " + mainView.getChildCount());
                android.util.Log.d("HungryGoblin", "ObstacleList size: " + obstacleList.size());
            }
        };

        spawnTimer.schedule(spawnTask, 10, 1000);


        super.onResume();

    } // onResume



    public void removeView(View view) {
        ViewGroup vg = (ViewGroup) (view.getParent());
        vg.removeView(view);
    }


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
