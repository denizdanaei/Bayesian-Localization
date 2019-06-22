package com.example.k2.d2.k2d2pf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.hardware.SensorEventListener;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.k2.d2.k2d2pf.PF.*;


public class FullscreenActivity extends AppCompatActivity implements View.OnClickListener,SensorEventListener {

    private Button up, left, right, down, reset;

    private float aX,aY,aZ =0;

    private boolean steps;

    private int step;

    public String direction;

    public static TextView motion_detail;

    private float azimuth ;

    private SensorManager sensorManager;

    private Sensor accelerometer,mRotationSensor;

    public boolean activityRunning ;

    private Canvas canvas;

    private List<ShapeDrawable> walls, cells;

    public int width=0,height = 0;
    private int stepsize =80;

    int number=2000;
    PF pf;
    public List<PF> Particles =new ArrayList<>();


    /*******************************************************/

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_fullscreen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.canvas);

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

        /*******************set the buttons**********************/

        motion_detail = findViewById(R.id.textView1);
        motion_detail.setMovementMethod(new ScrollingMovementMethod());

        up = findViewById(R.id.button1);
        up.setOnClickListener(this);

        left = findViewById(R.id.button2);
        left.setOnClickListener(this);

        right = findViewById(R.id.button3);
        right.setOnClickListener(this);

        down = findViewById(R.id.button4);
        down.setOnClickListener(this);

        reset = findViewById(R.id.reset);
        reset.setOnClickListener(this);

        /*****************get the screen dimensions********************/

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        height = size.x;
        width= size.y;
        /*****************Draw walls & particles********************/

        ImageView canvasView = findViewById(R.id.canvas);
        Bitmap blankBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(blankBitmap);
        canvasView.setImageBitmap(blankBitmap);

        walls = Walls.build_walls(width,height);
        cells=Walls.cells(width,height);

        /* create Particles */
        for (int i=0; i<number; i++){
            pf= new PF(width/10, height/5,1,Color.BLACK,  new ShapeDrawable(new OvalShape()),10);
            Particles.add(pf);
        }
        /* Initial Placement*/
        Particles =InitPF(width,height, Particles);

        canvas.drawColor(Color.WHITE);
        for(ShapeDrawable wall : walls)
            wall.draw(canvas);
        drawing(canvas, Particles);
        /*****************Initialize Sensors********************/
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // set accelerometer
            accelerometer = sensorManager
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            // register 'this' as a listener that updates values. Each time a sensor value changes,
            // the method 'onSensorChanged()' is called.
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "Hardware compatibility issue", Toast.LENGTH_LONG).show();
        }
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null){
            mRotationSensor = sensorManager
                    .getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            sensorManager.registerListener(this, mRotationSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }else
        {
            Toast.makeText(this, "Hardware compatibility issue", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onClick(View v) {
        motion_detail.setText(null);
        switch (v.getId()) {
            // UP BUTTON
            case R.id.button1: {
                fp_movement(width,height,"up", Particles, height/20);
                break;
            }
            // DOWN BUTTON
            case R.id.button4: {
                fp_movement(width,height,"down", Particles, height/20);
                break;
            }
            // LEFT BUTTON
            case R.id.button2: {

                fp_movement(width,height,"left", Particles, 7*width/400);
                break;
            }
            // RIGHT BUTTON
            case R.id.button3: {
                fp_movement(width,height,"right", Particles, 7*width/400);
                break;
            }
            case R.id.reset: {

                Particles =InitPF(width,height, Particles);
                break;
            }
        }

        List<PF> kmeans=PF.KMean(Particles);
        kmeans.get(0).color=Color.RED; //k
        kmeans.get(1).color=Color.BLUE; //j
        kmeans.get(2).color=Color.YELLOW; //l
        for (PF pf: kmeans){
            pf.size=40;
            pf.setBounds(pf);
        }

        ShapeDrawable cell =new ShapeDrawable(new RectShape());
        cell.getPaint().setColor(Color.rgb(240, 204, 194));
        cell.setBounds(0, 0, 0,0);

        PF centroid=PF.CheckConvergence(Particles);
        if (convergence){cell=Walls.check_cells(centroid, width,height);}

        canvas.drawColor(Color.WHITE);
        for(ShapeDrawable wall : walls)
            wall.draw(canvas);
        cell.draw(canvas);
        drawing(canvas, Particles);
        drawing(canvas, kmeans);
        centroid.shapeDrawable.draw(canvas);

    }
    // onResume() registers the accelerometer for listening the events
    protected void onResume() {
        super.onResume();
        activityRunning = true;
        sensorManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
        }
    }

    // onPause() unregisters the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        activityRunning = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing.
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        boolean check = false;

        if (Sensor.TYPE_ACCELEROMETER == (event.sensor.getType())) {
            motion_detail.setText("0.0");

            // get the the x,y,z values of the accelerometer
            aX = event.values[0];
            aY = event.values[1];
            aZ = event.values[2];

            if (aZ >= 10.5){
                steps = true;
                step++;
            }

        } else if (Sensor.TYPE_STEP_DETECTOR == (event.sensor.getType())) {
            if (activityRunning) {
                check = true;
            }
        } else if (Sensor.TYPE_ROTATION_VECTOR == (event.sensor.getType())) {
            float rotationMatrix[] = new float[16];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
            SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Y, rotationMatrix);
            float rotationValues[] = new float[16];
            SensorManager.getOrientation(rotationMatrix, rotationValues);
            azimuth = (float) Math.toDegrees(rotationValues[0]) + 180;
        }

        // display the current x,y,z accelerometer values
        motion_detail.setText(" steps" + steps + "dir " + azimuth+"\n aX"+aX +"\n aZ"+aZ );

        if (azimuth >= 45 && azimuth <= 150) {
            direction = "North";
        } else if (azimuth <= 330 || azimuth >= 240) {
            direction = "South";
        } else if (azimuth >= 150 && azimuth <= 240) {
            direction = "East";
        } else {
            direction = "West";
        }

        if (steps) {
            steps = false;
            switch (direction) {
                // UP BUTTON
                case "North": {
//                Toast.makeText(getApplication(), "UP", Toast.LENGTH_SHORT).show();
                    fp_movement(width, height, "up", Particles, height / 20);
//                motion_detail.setText(motion_detail.getText() + "\nuser=" + r.left + "," + r.top + "," + r.right + "," + r.bottom);
                    break;
                }
                // DOWN BUTTON
                case "South": {
//                Toast.makeText(getApplication(), "DOWN", Toast.LENGTH_SHORT).show();
                    fp_movement(width, height, "down", Particles, height / 20);
//                motion_detail.setText("\n\tMove Down" + "\n\tTop Margin = "
//                        + user.getBounds().top);
                    break;
                }
                // LEFT BUTTON
                case "West": {
//                Toast.makeText(getApplication(), "LEFT", Toast.LENGTH_SHORT).show();

                    fp_movement(width, height, "left", Particles, 7 * width / 400);
//                motion_detail.setText("\n\tMove Left" + "\n\tLeft Margin = "
//                        + user.getBounds().left);
                    break;
                }
                // RIGHT BUTTON
                case "East": {
//                Toast.makeText(getApplication(), "RIGHT", Toast.LENGTH_SHORT).show();
                    fp_movement(width, height, "right", Particles, 7 * width / 400);
//                motion_detail.setText("\n\tMove Right" + "\n\tLeft Margin = "
//                        + user.getBounds().left);
                    break;
                }
                default: {
                    //do nothing
                    break;
                }
            }

            List<PF> kmeans = PF.KMean(Particles);

            kmeans.get(0).color = Color.RED; //k
            kmeans.get(1).color = Color.BLUE; //j
            kmeans.get(2).color = Color.YELLOW; //l

            for (PF pf : kmeans) {
                pf.size = 40;
                pf.setBounds(pf);
            }


            ShapeDrawable cell = new ShapeDrawable(new RectShape());
            cell.getPaint().setColor(Color.rgb(240, 204, 194));
            cell.setBounds(0, 0, 0, 0);


            PF centroid = PF.CheckConvergence(Particles);
            if (convergence) {
                cell = Walls.check_cells(centroid, width, height);
            }

            canvas.drawColor(Color.WHITE);
            for (ShapeDrawable wall : walls)
                wall.draw(canvas);
            cell.draw(canvas);
            drawing(canvas, Particles);
            drawing(canvas, kmeans);
            centroid.shapeDrawable.draw(canvas);
        }
    }
}
