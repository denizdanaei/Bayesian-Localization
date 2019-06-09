package com.example.example6;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Smart Phone Sensing Example 6. Object movement and interaction on canvas.
 */
public class MainActivity extends Activity implements OnClickListener {


    private Button up, left, right, down;

    private TextView motion_detail;

    private ShapeDrawable user;

    private Canvas canvas;

    private List<ShapeDrawable> walls, FP;

    private  Random random =new Random();
    private int Particles=100, displaced_Particals=0, Replaced_Particles =Particles- displaced_Particals;

    /*******************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


    /*******************set the buttons**********************/
        up = (Button) findViewById(R.id.button1);
        up.setOnClickListener(this);

        left = (Button) findViewById(R.id.button2);
        left.setOnClickListener(this);

        right = (Button) findViewById(R.id.button3);
        right.setOnClickListener(this);

        down = (Button) findViewById(R.id.button4);
        down.setOnClickListener(this);

        motion_detail = (TextView) findViewById(R.id.textView1);

    /*****************get the screen dimensions********************/

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        /* create a user object */
        user = new ShapeDrawable(new OvalShape());
        user.getPaint().setColor(Color.BLUE);
        user.setBounds(width/30, width/20, width/30+40, width/20+40);

    /*********************Drawing The Walls**************************/
        walls = build_walls(width);

    /*********************Drawing The Dots**************************/
        FP = Particle_filters(width,walls);

    /***************************Creating a Canvas************************************/

        ImageView canvasView = (ImageView) findViewById(R.id.canvas);
        Bitmap blankBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(blankBitmap);
        canvasView.setImageBitmap(blankBitmap);

        // draw the objects
        user.draw(canvas);
        for(ShapeDrawable wall : walls)
            wall.draw(canvas);
        for(ShapeDrawable fp: FP)
            fp.draw(canvas);

        motion_detail.setText("FP.size"+ FP.size()+"\nReplaced_Particles"+ Replaced_Particles +"\ndisplaced_Particals"+ displaced_Particals);
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

    @Override
    public void onClick(View v) {
        // This happens when you click any of the four buttons.
        // For each of the buttons, when it is clicked we change:
        // - The text in the center of the buttons
        // - The margins
        // - The text that shows the margin
        Point size = new Point();
        int width = size.x;

//        fp.setBounds(ranX, ranY, ranX + 20, ranY + 20);

        switch (v.getId()) {
            // UP BUTTON
            case R.id.button1: {
//                Toast.makeText(getApplication(), "UP", Toast.LENGTH_SHORT).show();
                Rect r = user.getBounds();
                user.setBounds(r.left,r.top-20,r.right,r.bottom-20);
                for(ShapeDrawable fp: FP)
                {
//                    int ranX = random.nextInt(width);
                    int ranY = random.nextInt(width / 5);
                    Rect R = fp.getBounds();
                    fp.setBounds(R.left,R.top-ranY,R.right,R.bottom-ranY);
                }
                motion_detail.setText("\n\tMove Up" + "\n\tTop Margin = "
                        + user.getBounds().top);
                break;
            }
            // DOWN BUTTON
            case R.id.button4: {
//                Toast.makeText(getApplication(), "DOWN", Toast.LENGTH_SHORT).show();
                Rect r = user.getBounds();
                user.setBounds(r.left,r.top+20,r.right,r.bottom+20);
                for(ShapeDrawable fp: FP)
                {
                    Rect R = fp.getBounds();
                    fp.setBounds(R.left,R.top+20,R.right,R.bottom+20);
                }
                motion_detail.setText("\n\tMove Down" + "\n\tTop Margin = "
                        + user.getBounds().top);
                break;
            }
            // LEFT BUTTON
            case R.id.button2: {
//                Toast.makeText(getApplication(), "LEFT", Toast.LENGTH_SHORT).show();
                Rect r = user.getBounds();
                user.setBounds(r.left-20,r.top,r.right-20,r.bottom);
                for(ShapeDrawable fp: FP)
                {
                    Rect R = fp.getBounds();
                    fp.setBounds(R.left-20,R.top,R.right-20,R.bottom);
                }
                motion_detail.setText("\n\tMove Left" + "\n\tLeft Margin = "
                        + user.getBounds().left);
                break;
            }
            // RIGHT BUTTON
            case R.id.button3: {
//                Toast.makeText(getApplication(), "RIGHT", Toast.LENGTH_SHORT).show();
                Rect r = user.getBounds();
                user.setBounds(r.left+20,r.top,r.right+20,r.bottom);
                for(ShapeDrawable fp: FP)
                {
                    Rect R = fp.getBounds();
                    fp.setBounds(R.left+20,R.top,R.right+20,R.bottom);
                }
                motion_detail.setText("\n\tMove Right" + "\n\tLeft Margin = "
                        + user.getBounds().left);
                break;
            }
        }
        // if there is a collision between the dot and any of the walls
        if(isCollision()) {
            // reset dot to center of canvas
            Display display = getWindowManager().getDefaultDisplay();

            display.getSize(size);
//            int width = size.x;
//            int height = size.y;
            user.setBounds(width/30, width/20, width/30+40, width/20+40);
        }

        if(detect_Collision()) {
            for (ShapeDrawable fp : FP) {
                correct_Collision();
            }
            motion_detail.setText("FP.size"+ FP.size()+"\nReplaced_Particles"+ Replaced_Particles +"\ndisplaced_Particals"+ displaced_Particals);

        }
//            Toast.makeText(getApplication(), " pf collision", Toast.LENGTH_SHORT).show();
        // redrawing of the object
        canvas.drawColor(Color.WHITE);
        user.draw(canvas);
        for(ShapeDrawable wall : walls)
            wall.draw(canvas);
        for(ShapeDrawable fp: FP)
            fp.draw(canvas);
    }

    /**
     * Determines if the user dot intersects with any of the walls.
     * @return True if that's true, false otherwise.
     */
    private boolean isCollision() {
        for(ShapeDrawable wall : walls) {
            if(isCollision(wall, user))
                return true;
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

    public List<ShapeDrawable> Particle_filters (int width, List<ShapeDrawable> walls)
    {
        FP= new ArrayList<>();

        for (int i=0; i<Particles; i++)
        {
            /** create a drawable object **/
            ShapeDrawable fp = new ShapeDrawable(new OvalShape());
            fp.getPaint().setColor(Color.RED);
            /***place the fp******/
            int ranX = random.nextInt(width);
            int ranY = random.nextInt(width / 5);
            fp.setBounds(ranX, ranY, ranX + 20, ranY + 20);
                FP.add(fp);

        }
        if (detect_Collision()){

            correct_Collision();
        }
        return FP;
    }
    private boolean detect_Collision() {
        boolean collision=false;
        for(ShapeDrawable fp : FP) {
            for (ShapeDrawable wall : walls)
            {
                if (isCollision(wall, fp)){collision=true;}
            }
            if (collision){displaced_Particals++; collision=false;}
            }
        if (displaced_Particals!=0){collision=true;}

//        motion_detail.setText("FP.size"+ FP.size()+"\nReplaced_Particles"+ Replaced_Particles +"\ndisplaced_Particals"+ displaced_Particals);

        return collision;
    }

    private void correct_Collision()
    {
        /**SomeHow I'm loosing the dots**/
        List<ShapeDrawable> crct_FP= new ArrayList<>();
        crct_FP.add(user);
        int fixed_pf=1;
        boolean collision=false;
        for (ShapeDrawable fp : FP )
        {
            for(ShapeDrawable wall : walls)
            {
                if (isCollision(wall, fp))
                {
                    collision=true;
                    /*should be fixed: displaced_Particals in the 2 for loop not a good result*/
                    Rect vector = crct_FP.get(random.nextInt(fixed_pf)).getBounds();
                    fp.setBounds(vector.left, vector.top, vector.left, vector.top);
                }

            }

            fixed_pf++;crct_FP.add(fp);
            if (collision){displaced_Particals--; collision=false;}

        }

    }

}