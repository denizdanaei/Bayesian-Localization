package com.example.k2.d2.k2d2pf;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

import java.util.ArrayList;
import java.util.List;

public class Walls {


    public static List<ShapeDrawable> walls;

    public static List<ShapeDrawable> build_walls(int width,int height){
        walls = new ArrayList<>();

        //outerlayer North
        ShapeDrawable North = new ShapeDrawable(new RectShape());
        North.setBounds(0, 0, width,40 );
        North.getPaint().setColor(Color.rgb(2, 139, 185));
        walls.add(North);

        //outerlayer South
        ShapeDrawable South = new ShapeDrawable(new RectShape());
        South.setBounds(0, height-40, width, height);
        South.getPaint().setColor(Color.rgb(2, 139, 185));
        walls.add(South);

        //outerlayer West
        ShapeDrawable West = new ShapeDrawable(new RectShape());
        West.setBounds(0, 0, 40, height);
        West.getPaint().setColor(Color.rgb(2, 139, 185));
        walls.add(West);

        //outerlayer East
        ShapeDrawable East = new ShapeDrawable(new RectShape());
        East.setBounds(width-40, 0, width, height);
        East.getPaint().setColor(Color.rgb(2, 139, 185));
        walls.add(East);

        //wall 11 to 3: Black area between cell 11 to cell 3
        ShapeDrawable wall_11to3 = new ShapeDrawable(new RectShape());
        wall_11to3.setBounds(3*width/10, 0, 9*width/10, 6*height/14);
        wall_11to3.getPaint().setColor(Color.rgb(2, 139, 185));
        walls.add(wall_11to3);

        //wall 1 to 14: Black area between cell 1 to cell 14
        ShapeDrawable wall_1to14 = new ShapeDrawable(new RectShape());
        wall_1to14.setBounds(width/10, 8*height/14, 9*width/10, height);
        wall_1to14.getPaint().setColor(Color.rgb(2, 139, 185));
        walls.add(wall_1to14);


        //wall 14 square black box
        ShapeDrawable wall_14_square = new ShapeDrawable(new RectShape());
        wall_14_square.setBounds(0, 8*height/14, 3*width/40, 11*height/14);
        wall_14_square.getPaint().setColor(Color.rgb(2, 139, 185));
        walls.add(wall_14_square);


        //wall 13: Wall btw Cell 16,13
        ShapeDrawable wall_13 = new ShapeDrawable(new RectShape());
        wall_13.setBounds(width/10-10, 0, width/10+10, 6*height/14);
        wall_13.getPaint().setColor(Color.rgb(2, 139, 185));
        walls.add(wall_13);


        //wall 11: Wall btw Cell 13,11
        ShapeDrawable wall_11 = new ShapeDrawable(new RectShape());
        wall_11.setBounds(2*width/10-10, 0, 2*width/10+10, 6*height/14);
        wall_11.getPaint().setColor(Color.rgb(2, 139, 185));
        walls.add(wall_11);

        return walls;
    }

}
