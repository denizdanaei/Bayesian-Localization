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
import android.text.method.ScrollingMovementMethod;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Smart Phone Sensing Example 6. Object movement and interaction on canvas.
 */
public class MainActivity extends Activity implements OnClickListener {


    private Button up, left, right, down, reset;

    private TextView motion_detail;

    private ShapeDrawable user;

    private Canvas canvas;

    private List<ShapeDrawable> walls, FP;

    private  Random random =new Random();
    private int Particles=10, displaced_Particals=0, Replaced_Particles =Particles- displaced_Particals;
    public int width=0,height = 0;
    private int User_speed =40, Partical_movement=5;

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

        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(this);

        motion_detail = (TextView) findViewById(R.id.textView1);
        motion_detail.setMovementMethod(new ScrollingMovementMethod());

    /*****************get the screen dimensions********************/

//        final ImageView canvass = (ImageView)findViewById(R.id.canvas);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        height = size.y;
        width= size.x;
//        width = canvass.getDrawable().getIntrinsicWidth();;

        ImageView canvasView = (ImageView) findViewById(R.id.canvas);

        Bitmap blankBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(blankBitmap);
        canvasView.setImageBitmap(blankBitmap);
        width=canvasView.getDrawable().getIntrinsicWidth();
        height=canvas.getHeight();

        /* create a user object */
        user = new ShapeDrawable(new OvalShape());
        user.getPaint().setColor(Color.BLUE);
        user.setBounds(width/30, width/20, width/30+30, width/20+30);

    /*********************Drawing The Walls**************************/
        walls = build_walls();

    /*********************Drawing The Dots**************************/
        FP = Particle_filters(walls);

    /***************************Creating a Canvas************************************/



        // draw the objects
        user.draw(canvas);
        for(ShapeDrawable wall : walls)
            wall.draw(canvas);
        for(ShapeDrawable fp: FP)
            fp.draw(canvas);

//        motion_detail.setText("FP.size"+ FP.size()+"\nReplaced_Particles"+ Replaced_Particles +"\ndisplaced_Particals"+ displaced_Particals);
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

//        fp.setBounds(ranX, ranY, ranX + 20, ranY + 20);

        switch (v.getId()) {
            // UP BUTTON
            case R.id.button1: {
//                Toast.makeText(getApplication(), "UP", Toast.LENGTH_SHORT).show();
                Rect r = user.getBounds();
                user.setBounds(r.left,r.top- User_speed,r.right,r.bottom- User_speed);
                fp_movement("up");
                motion_detail.setText(motion_detail.getText()+"\nuser=" + r.left +","+ r.top +","+ r.right +","+ r.bottom);

                break;
            }
            // DOWN BUTTON
            case R.id.button4: {
//                Toast.makeText(getApplication(), "DOWN", Toast.LENGTH_SHORT).show();
                Rect r = user.getBounds();
                user.setBounds(r.left,r.top+ User_speed,r.right,r.bottom+ User_speed);
                fp_movement("down");
//                motion_detail.setText("\n\tMove Down" + "\n\tTop Margin = "
//                        + user.getBounds().top);
                break;
            }
            // LEFT BUTTON
            case R.id.button2: {
//                Toast.makeText(getApplication(), "LEFT", Toast.LENGTH_SHORT).show();
                Rect r = user.getBounds();
                user.setBounds(r.left- User_speed,r.top,r.right- User_speed,r.bottom);
                fp_movement("left");
//                motion_detail.setText("\n\tMove Left" + "\n\tLeft Margin = "
//                        + user.getBounds().left);
                break;
            }
            // RIGHT BUTTON
            case R.id.button3: {
//                Toast.makeText(getApplication(), "RIGHT", Toast.LENGTH_SHORT).show();
                Rect r = user.getBounds();
                user.setBounds(r.left+ User_speed,r.top,r.right+ User_speed,r.bottom);
                fp_movement("right");
//                motion_detail.setText("\n\tMove Right" + "\n\tLeft Margin = "
//                        + user.getBounds().left);
                break;
            }
            case R.id.reset: {
                Particle_filters(walls);
                break;
            }
        }
        // if there is a collision between the dot and any of the walls
        if(isCollision()) {
            // reset dot to center of canvas
            user.setBounds(width/30, width/20, width/30+30, width/20+30);
        }

        if(detect_Collision()) {
            for (ShapeDrawable fp : FP) {
                correct_Collision();
            }
//            motion_detail.setText("FP.size"+ FP.size()+"\nReplaced_Particles"+ Replaced_Particles +"\ndisplaced_Particals"+ displaced_Particals);

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






    /****************user & walls**********************************/
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
    /*************************************************************/

    private List<ShapeDrawable> build_walls(){
        walls = new ArrayList<>();
        //outerlayer: Windows wall of EWI
        ShapeDrawable outerlayer = new ShapeDrawable(new RectShape());
        outerlayer.setBounds(0, 0, width,5 );
        walls.add(outerlayer);

        //wall 16: Left wall of cell 16
        ShapeDrawable wall_16 = new ShapeDrawable(new RectShape());
        wall_16.setBounds(0, 0, 5, height);
        walls.add(wall_16);

        //wall 13: Left wall of cell 13
        ShapeDrawable wall_13 = new ShapeDrawable(new RectShape());
        wall_13.setBounds(width/15, 0, 5+width/15, height/3);
        walls.add(wall_13);

        //wall 11: Left wall of cell 11
        ShapeDrawable wall_11 = new ShapeDrawable(new RectShape());
        wall_11.setBounds(2*width/15, 0, 5+2*width/15, height/3);
        walls.add(wall_11);


        //wall 11 to 3: Black area between cell 11 to cell 3
        ShapeDrawable wall_11to3 = new ShapeDrawable(new RectShape());
        wall_11to3.setBounds(3*width/15, 0, 5+9*width/15, height/3);
        walls.add(wall_11to3);


        //wall 1 to 3: Black area between cell 1 to cell 3
        ShapeDrawable wall_1to3 = new ShapeDrawable(new RectShape());
        wall_1to3.setBounds(10*width/15, 0, 5+width, height);
        walls.add(wall_1to3);

        //wall 1 to 14: Black area between cell 1 to cell 14
        ShapeDrawable wall_1to14 = new ShapeDrawable(new RectShape());
        wall_1to14.setBounds(width/15, height/2, 10+9*width/15, height);
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

    public List<ShapeDrawable> Particle_filters (List<ShapeDrawable> walls)
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

    /****************fp & walls**********************************/
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
                    fp.setBounds(vector.left+10, vector.top+10, vector.left+10, vector.top+10);
                }

            }

            fixed_pf++;crct_FP.add(fp);
            if (collision){displaced_Particals--; collision=false;}

        }

    }

    private void fp_movement(String button){

        int ranX=0,ranY=0;

        switch (button) {
            case "up": {
                motion_detail.setText(null);

                for (ShapeDrawable fp : FP) {
                    ranY = Partical_movement+ random.nextInt(15);
                    ranX = Partical_movement+ random.nextInt(15)*(random .nextBoolean() ? -1 : 1);
                    Rect R = fp.getBounds();
                    fp.setBounds(R.left +ranX, R.top -ranY, R.right +ranX, R.bottom -ranY);

                    R = fp.getBounds();
                    motion_detail.setText(motion_detail.getText()+"\n" + R.left +","+ R.top +","+ R.right +","+ R.bottom);

                }
                break;
            }
            case "down": {
                for (ShapeDrawable fp : FP) {
                    ranY = Partical_movement+ random.nextInt(15);
                    ranX = Partical_movement+ random.nextInt(15)*(random .nextBoolean() ? -1 : 1);
                    Rect R = fp.getBounds();
                    fp.setBounds(R.left+ranX, R.top+ranY, R.right+ranX, R.bottom+ranY);

                    R = fp.getBounds();
                    motion_detail.setText(R.left +","+ R.top +","+ R.right +","+ R.bottom +"\n");

                }
                break;
            }
            case "right": {
                for (ShapeDrawable fp : FP) {
                    ranX = Partical_movement+ random.nextInt(15);
                    ranY = Partical_movement+ random.nextInt(15)*(random .nextBoolean() ? -1 : 1);
                    Rect R = fp.getBounds();
                    fp.setBounds(R.left +ranX, R.top +ranY, R.right +ranX, R.bottom +ranY);

                    R = fp.getBounds();
                    motion_detail.setText(R.left +","+ R.top +","+ R.right +","+ R.bottom +"\n");

                }
                break;
            }
            case "left": {
                for (ShapeDrawable fp : FP) {
                    ranX = Partical_movement+ random.nextInt(15);
                    ranY = Partical_movement+ random.nextInt(15)*(random .nextBoolean() ? -1 : 1);
                    Rect R = fp.getBounds();
                    fp.setBounds(R.left -ranX, R.top +ranY, R.right -ranX, R.bottom +ranY);


                    R = fp.getBounds();
                    motion_detail.setText(R.left +","+ R.top +","+ R.right +","+ R.bottom +"\n");
                }
                break;
            }
            default: break;
        }




    }
}