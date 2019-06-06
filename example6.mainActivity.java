package com.example.example6;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Smart Phone Sensing Example 6. Object movement and interaction on canvas.
 */
public class MainActivity extends Activity implements OnClickListener {


    private Button up, left, right, down;

    private TextView textView;

    private ShapeDrawable drawable;

    private Canvas canvas;

    private List<ShapeDrawable> walls, FP;
    /*******************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set the buttons
        up = (Button) findViewById(R.id.button1);
        up.setOnClickListener(this);

        left = (Button) findViewById(R.id.button2);
        left.setOnClickListener(this);

        right = (Button) findViewById(R.id.button3);
        right.setOnClickListener(this);

        down = (Button) findViewById(R.id.button4);
        down.setOnClickListener(this);

        textView = (TextView) findViewById(R.id.textView1);

        // get the screen dimensions
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        /**************************User Location******************************/
        /* create a drawable object */
        drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(Color.BLUE);
        drawable.setBounds(width/30, width/20, width/30+40, width/20+40);

        FP= new ArrayList<>();
        Random random =new Random();

        for (int i=0; i<10; i++) {
            /* create a drawable object */
            int ranX = random.nextInt(width);
            int ranY = random.nextInt(width / 5);
            ShapeDrawable j = new ShapeDrawable(new OvalShape());
            j.getPaint().setColor(Color.RED);
            j.setBounds(ranX, ranY, ranX + 20, ranY + 20);
        }
        /*********************Drawing The Walls**************************/
        walls = build_walls(width);

        /*************************PF dots********************************/

//            if (fpCollision()) {
//                i--;
//            } else {
//                FP.add(j);
//
//            }


        /***************************Creating a Canvas************************************/

        ImageView canvasView = (ImageView) findViewById(R.id.canvas);
        Bitmap blankBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(blankBitmap);
        canvasView.setImageBitmap(blankBitmap);

        // draw the objects
        drawable.draw(canvas);
        for(ShapeDrawable wall : walls)
            wall.draw(canvas);
        for(ShapeDrawable fp: FP)
            fp.draw(canvas);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /***********************************************************/
    /***Dots movement- for now they all move with one vector****/
    /***********************************************************/
    @Override
    public void onClick(View v) {
        // This happens when you click any of the four buttons.
        // For each of the buttons, when it is clicked we change:
        // - The text in the center of the buttons
        // - The margins
        // - The text that shows the margin
        switch (v.getId()) {
            // UP BUTTON
            case R.id.button1: {
                Toast.makeText(getApplication(), "UP", Toast.LENGTH_SHORT).show();
                Rect r = drawable.getBounds();
                drawable.setBounds(r.left,r.top-20,r.right,r.bottom-20);
                for(ShapeDrawable fp: FP)
                {
                    Rect R = fp.getBounds();
                    fp.setBounds(R.left,R.top-20,R.right,R.bottom-20);
                }
                textView.setText("\n\tMove Up" + "\n\tTop Margin = "
                        + drawable.getBounds().top);
                break;
            }
            // DOWN BUTTON
            case R.id.button4: {
                Toast.makeText(getApplication(), "DOWN", Toast.LENGTH_SHORT).show();
                Rect r = drawable.getBounds();
                drawable.setBounds(r.left,r.top+20,r.right,r.bottom+20);
                for(ShapeDrawable fp: FP)
                {
                    Rect R = fp.getBounds();
                    fp.setBounds(R.left,R.top+20,R.right,R.bottom+20);
                }
                textView.setText("\n\tMove Down" + "\n\tTop Margin = "
                        + drawable.getBounds().top);
                break;
            }
            // LEFT BUTTON
            case R.id.button2: {
                Toast.makeText(getApplication(), "LEFT", Toast.LENGTH_SHORT).show();
                Rect r = drawable.getBounds();
                drawable.setBounds(r.left-20,r.top,r.right-20,r.bottom);
                for(ShapeDrawable fp: FP)
                {
                    Rect R = fp.getBounds();
                    fp.setBounds(R.left-20,R.top,R.right-20,R.bottom);
                }
                textView.setText("\n\tMove Down" + "\n\tTop Margin = "
                        + drawable.getBounds().top);
                textView.setText("\n\tMove Left" + "\n\tLeft Margin = "
                        + drawable.getBounds().left);
                break;
            }
            // RIGHT BUTTON
            case R.id.button3: {
                Toast.makeText(getApplication(), "RIGHT", Toast.LENGTH_SHORT).show();
                Rect r = drawable.getBounds();
                drawable.setBounds(r.left+20,r.top,r.right+20,r.bottom);
                for(ShapeDrawable fp: FP)
                {
                    Rect R = fp.getBounds();
                    fp.setBounds(R.left+20,R.top,R.right+20,R.bottom);
                }
                textView.setText("\n\tMove Right" + "\n\tLeft Margin = "
                        + drawable.getBounds().left);
                break;
            }
        }
        // if there is a collision between the dot and any of the walls
        if(isCollision()) {
            // reset dot to center of canvas
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            drawable.setBounds(width/30, width/20, width/30+40, width/20+40);
        }
//        while (fpCollision()) {
//            // reset dot to center of canvas
//            Display display = getWindowManager().getDefaultDisplay();
//            Point size = new Point();
//            display.getSize(size);
//            int width = size.x;
//            int height = size.y;
//            Random random =new Random();
//            for (ShapeDrawable fp: FP)
//            {
//                int ranX= random.nextInt(width);
//                int ranY=random.nextInt(width/5);
//                ShapeDrawable j = new ShapeDrawable(new OvalShape());
//                j.getPaint().setColor(Color.RED);
//                j.setBounds(ranX, ranY,  ranX+20, ranY+20);
//
//
//            }



//        }

        // redrawing of the object
        canvas.drawColor(Color.WHITE);
        drawable.draw(canvas);
        for(ShapeDrawable wall : walls)
            wall.draw(canvas);

        for(ShapeDrawable fp : FP)
            fp.draw(canvas);
    }

    /**
     * Determines if the drawable dot intersects with any of the walls.
     * @return True if that's true, false otherwise.
     */
    private boolean isCollision() {

        for(ShapeDrawable wall : walls) {
            if(isCollision(wall,drawable))
                return true;
        }

        return false;
    }
    private boolean fpCollision() {

        for(ShapeDrawable wall : walls) {
            for (ShapeDrawable fp : FP)
            {
                if (isCollision(wall, fp))
                    return true;
            }
        }
        return false;
    }

    /**
     * Determines if two shapes intersect.
     * @param first The first shape.
     * @param second The second shape.
     * @return True if they intersect, false otherwise.
     */
    private boolean isCollision(ShapeDrawable first, ShapeDrawable second) {
        Rect firstRect = new Rect(first.getBounds());
        return firstRect.intersect(second.getBounds());
    }


    private List<ShapeDrawable> build_walls(int width){
        walls = new ArrayList<>();
        //outerlayer: Windows wall of EWI
        ShapeDrawable outerlayer = new ShapeDrawable(new RectShape());
        outerlayer.setBounds(5, 5, width,10 );
        walls.add(outerlayer);

        //wall 16: Left wall of cell 16
        ShapeDrawable wall_16 = new ShapeDrawable(new RectShape());
        wall_16.setBounds(5, 5, 10, width/10);
        walls.add(wall_16);

        //wall 13: Left wall of cell 13
        ShapeDrawable wall_13 = new ShapeDrawable(new RectShape());
        wall_13.setBounds(5+width/15, 5, 10+width/15, width/10);
        walls.add(wall_13);

        //wall 11: Left wall of cell 11
        ShapeDrawable wall_11 = new ShapeDrawable(new RectShape());
        wall_11.setBounds(5+2*width/15, 5, 10+2*width/15, width/10);
        walls.add(wall_11);


        //wall 11 to 3: Black area between cell 11 to cell 3
        ShapeDrawable wall_11to3 = new ShapeDrawable(new RectShape());
        wall_11to3.setBounds(5+3*width/15, 5, 10+9*width/15, width/10);
        walls.add(wall_11to3);

        //wall 3: Right wall of cell 3
        ShapeDrawable wall_3 = new ShapeDrawable(new RectShape());
        wall_3.setBounds(5+10*width/15, 5, 10+width, width/10);
        walls.add(wall_3);

        //wall 1: Right wall of cell 1
        ShapeDrawable wall_1 = new ShapeDrawable(new RectShape());
        wall_1.setBounds(5+10*width/15, width/30+width/10, 10+width, 5+width/30+2*width/10);
        walls.add(wall_1);

        //wall 1 to 14: Black area between cell 1 to cell 14
        ShapeDrawable wall_1to14 = new ShapeDrawable(new RectShape());
        wall_1to14.setBounds(5+width/15, width/30+width/10, 10+9*width/15, 5+width/30+2*width/10);
        walls.add(wall_1to14);


        //outerlayer 2: The outer wall of EWI
        ShapeDrawable outerlayer2 = new ShapeDrawable(new RectShape());
        outerlayer2.setBounds(5, width/30+2*width/10, width, 5+width/30+2*width/10);
        walls.add(outerlayer2);

        //wall 14: Left wall of cell 14
        ShapeDrawable wall_14 = new ShapeDrawable(new RectShape());
        wall_14.setBounds(5, width/30+width/10, 10, 5+width/30+2*width/10);
        walls.add(wall_14);

        //wall 14 square black box
        ShapeDrawable wall_14_square = new ShapeDrawable(new RectShape());
        wall_14_square.setBounds(5, width/30+width/10, 5+width/30, 5+width/30+width/10+width/20);
        walls.add(wall_14_square);

        return walls;
    }
}