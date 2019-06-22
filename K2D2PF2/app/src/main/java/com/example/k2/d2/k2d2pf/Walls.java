package com.example.k2.d2.k2d2pf;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

import java.util.ArrayList;
import java.util.List;

import static com.example.k2.d2.k2d2pf.MainActivity.motion_detail;

//import static com.example.k2.d2.k2d2pf.FullscreenActivity.motion_detail;

public class Walls {


    public static List<ShapeDrawable> walls;

    public static List<ShapeDrawable> cells;


    public static List<ShapeDrawable> build_walls(int width,int height){
        walls = new ArrayList<>();

        //outerlayer North
        ShapeDrawable North = new ShapeDrawable(new RectShape());
        North.setBounds(0, 0, width,20 );
        North.getPaint().setColor(Color.rgb(2, 139, 185));
        walls.add(North);

        //outerlayer South
        ShapeDrawable South = new ShapeDrawable(new RectShape());
        South.setBounds(0, height-20, width, height);
        South.getPaint().setColor(Color.rgb(2, 139, 185));
        walls.add(South);

        //outerlayer West
        ShapeDrawable West = new ShapeDrawable(new RectShape());
        West.setBounds(0, 0, 20, height);
        West.getPaint().setColor(Color.rgb(2, 139, 185));
        walls.add(West);

        //outerlayer East
        ShapeDrawable East = new ShapeDrawable(new RectShape());
        East.setBounds(width-20, 0, width, height);
        East.getPaint().setColor(Color.rgb(2, 139, 185));
        walls.add(East);

        //wall 11 to 3: Black area between cell 11 to cell 3
        ShapeDrawable wall_11to3 = new ShapeDrawable(new RectShape());
        wall_11to3.setBounds(3*width/10+10, 0, 9*width/10-10, 6*height/14);
        wall_11to3.getPaint().setColor(Color.rgb(2, 139, 185));
        walls.add(wall_11to3);

        //wall 1 to 14: Black area between cell 1 to cell 14
        ShapeDrawable wall_1to14 = new ShapeDrawable(new RectShape());
        wall_1to14.setBounds(width/10, 8*height/14, 9*width/10-10, height);
        wall_1to14.getPaint().setColor(Color.rgb(2, 139, 185));
        walls.add(wall_1to14);


        //wall 14 square black box
        ShapeDrawable wall_14_square = new ShapeDrawable(new RectShape());
        wall_14_square.setBounds(0, 8*height/14, 3*width/40, 11*height/14);
        wall_14_square.getPaint().setColor(Color.rgb(2, 139, 185));
        walls.add(wall_14_square);


        //wall 13: Wall btw Cell 16,13
        ShapeDrawable wall_13 = new ShapeDrawable(new RectShape());
        wall_13.setBounds(width/10, 0, width/10+10, 6*height/14);
        wall_13.getPaint().setColor(Color.rgb(2, 139, 185));
        walls.add(wall_13);


        //wall 11: Wall btw Cell 13,11
        ShapeDrawable wall_11 = new ShapeDrawable(new RectShape());
        wall_11.setBounds(2*width/10, 0, 2*width/10+10, 6*height/14);
        wall_11.getPaint().setColor(Color.rgb(2, 139, 185));
        walls.add(wall_11);

        return walls;
    }


    public static List<ShapeDrawable> cells(int width,int height){
        cells = new ArrayList<>();

        //Cell 16
        ShapeDrawable cell_16 = new ShapeDrawable(new RectShape());
        cell_16.setBounds(40, 40, width/10-10,6*height/14);
        cell_16.getPaint().setColor(Color.rgb(240, 204, 194));
        cells.add(cell_16);


        //Cell 13
        ShapeDrawable cell_13 = new ShapeDrawable(new RectShape());
        cell_13.setBounds(width/10+10, 40,2*width/10-10,6*height/14);
        cell_13.getPaint().setColor(Color.rgb(240, 204, 194));
        cells.add(cell_13);


        //Cell 11
        ShapeDrawable cell_11 = new ShapeDrawable(new RectShape());
        cell_11.setBounds(2*width/10+10, 40,3*width/10,6*height/14);
        cell_11.getPaint().setColor(Color.rgb(240, 204, 194));
        cells.add(cell_11);

        //Cell 3
        ShapeDrawable cell_3 = new ShapeDrawable(new RectShape());
        cell_3.setBounds( 9*width/10, 40,width-40,6*height/14);
        cell_3.getPaint().setColor(Color.rgb(240, 204, 194));
        cells.add(cell_3);


        //Cell 15
        ShapeDrawable cell_15 = new ShapeDrawable(new RectShape());
        cell_15.setBounds( 40, 6*height/14,width/10-10,8*height/14);
        cell_15.getPaint().setColor(Color.rgb(240, 204, 194));
        cells.add(cell_15);


        //Cell 12
        ShapeDrawable cell_12 = new ShapeDrawable(new RectShape());
        cell_12.setBounds( width/10+10, 6*height/14,2*width/10-10,8*height/14);
        cell_12.getPaint().setColor(Color.rgb(240, 204, 194));
        cells.add(cell_12);

        //Cell 10
        ShapeDrawable cell_10 = new ShapeDrawable(new RectShape());
        cell_10.setBounds( 2*width/10+10, 6*height/14,3*width/10-10,8*height/14);
        cell_10.getPaint().setColor(Color.rgb(240, 204, 194));
        cells.add(cell_10);

        //Cell 9
        ShapeDrawable cell_9 = new ShapeDrawable(new RectShape());
        cell_9.setBounds( 3*width/10+10, 6*height/14,4*width/10-10,8*height/14);
        cell_9.getPaint().setColor(Color.rgb(240, 204, 194));
        cells.add(cell_9);

        //Cell 8
        ShapeDrawable cell_8 = new ShapeDrawable(new RectShape());
        cell_8.setBounds( 4*width/10+10, 6*height/14,5*width/10-10,8*height/14);
        cell_8.getPaint().setColor(Color.rgb(240, 204, 194));
        cells.add(cell_8);


        //Cell 7
        ShapeDrawable cell_7 = new ShapeDrawable(new RectShape());
        cell_7.setBounds( 5*width/10+10, 6*height/14,6*width/10-10,8*height/14);
        cell_7.getPaint().setColor(Color.rgb(240, 204, 194));
        cells.add(cell_7);

        //Cell 6
        ShapeDrawable cell_6 = new ShapeDrawable(new RectShape());
        cell_6.setBounds( 6*width/10+10, 6*height/14,7*width/10-10,8*height/14);
        cell_6.getPaint().setColor(Color.rgb(240, 204, 194));
        cells.add(cell_6);

        //Cell 5
        ShapeDrawable cell_5 = new ShapeDrawable(new RectShape());
        cell_5.setBounds( 7*width/10+10, 6*height/14,8*width/10-10,8*height/14);
        cell_5.getPaint().setColor(Color.rgb(240, 204, 194));
        cells.add(cell_5);

        //Cell 4
        ShapeDrawable cell_4 = new ShapeDrawable(new RectShape());
        cell_4.setBounds( 8*width/10+10, 6*height/14,9*width/10-10,8*height/14);
        cell_4.getPaint().setColor(Color.rgb(240, 204, 194));
        cells.add(cell_4);

        //Cell 2
        ShapeDrawable cell_2 = new ShapeDrawable(new RectShape());
        cell_2.setBounds( 9*width/10+10, 6*height/14,width-40,8*height/14);
        cell_2.getPaint().setColor(Color.rgb(240, 204, 194));
        cells.add(cell_2);


        //Cell 14
        ShapeDrawable cell_14 = new ShapeDrawable(new RectShape());
        cell_14.setBounds( 40, 8*height/14,width/10,height-40);
        cell_14.getPaint().setColor(Color.rgb(240, 204, 194));
        cells.add(cell_14);


        //Cell 1
        ShapeDrawable cell_1 = new ShapeDrawable(new RectShape());
        cell_1.setBounds( 9*width/10, 8*height/14,width-40,height-40);
        cell_1.getPaint().setColor(Color.rgb(240, 204, 194));
        cells.add(cell_1);

        return cells;
    }

    public static ShapeDrawable check_cells(PF centroid,int width,int height){
        ShapeDrawable cell =new ShapeDrawable(new RectShape());
        cell.getPaint().setColor(Color.rgb(240, 204, 194));

        //cell 16
        if(centroid.x>40 && centroid.y>40 && centroid.x<width/10-10 && centroid.y<6*height/14){
            motion_detail.setText("cell16");
            cell.setBounds(40, 40, width/10-10,6*height/14);
            return cell;
        }
        //cell 13
        else if (centroid.x>width/10+10 && centroid.y>40 && centroid.x<2*width/10-10 && centroid.y<6*height/14){
            motion_detail.setText("cell13");
            cell.setBounds(width/10+10, 40,2*width/10-10,6*height/14);
            return cell;
        }
        //cell 11
        else if (centroid.x>2*width/10+10 && centroid.y>40 && centroid.x<3*width/10 && centroid.y<6*height/14){
            motion_detail.setText("cell11");
            cell.setBounds(2*width/10+10, 40,3*width/10,6*height/14);
            return cell;
        }
        //cell 15
        else if (centroid.x>40 && centroid.y>6*height/14 && centroid.x<width/10-10 && centroid.y<8*height/14){
            motion_detail.setText("cell15");
            cell.setBounds( 40, 6*height/14,width/10-10,8*height/14);
            return cell;
        }
        //cell 12
        else if (centroid.x>width/10+10 && centroid.y>6*height/14 && centroid.x<2*width/10-10 && centroid.y<8*height/14){
            motion_detail.setText("cell12");
            cell.setBounds( width/10+10, 6*height/14,2*width/10-10,8*height/14);
            return cell;
        }
        //cell 10
        else if (centroid.x>2*width/10+10 && centroid.y>6*height/14 && centroid.x<3*width/10-10 && centroid.y<8*height/14){
            motion_detail.setText("cell10");
            cell.setBounds(  2*width/10+10, 6*height/14,3*width/10-10,8*height/14);
            return cell;
        }
        //cell 9
        else if (centroid.x>3*width/10+10 && centroid.y>6*height/14 && centroid.x<4*width/10-10 && centroid.y<8*height/14){
            motion_detail.setText("cell9");
            cell.setBounds( 3*width/10+10, 6*height/14,4*width/10-10,8*height/14);
            return cell;
        }
        //cell 8
        else if (centroid.x>4*width/10+10 && centroid.y>6*height/14 && centroid.x<5*width/10-10 && centroid.y<8*height/14){
            motion_detail.setText("cell8");
            cell.setBounds(  4*width/10+10, 6*height/14,5*width/10-10,8*height/14);
            return cell;
        }
        //cell 7
        else if (centroid.x>5*width/10+10 && centroid.y>6*height/14 && centroid.x<6*width/10-10 && centroid.y<8*height/14){
            motion_detail.setText("cell7");
            cell.setBounds(  5*width/10+10, 6*height/14,6*width/10-10,8*height/14);
            return cell;
        }
        //cell 6
        else if (centroid.x>6*width/10+10 && centroid.y>6*height/14 && centroid.x<7*width/10-10 && centroid.y<8*height/14){
            motion_detail.setText("cell6");
            cell.setBounds( 6*width/10+10, 6*height/14,7*width/10-10,8*height/14);
            return cell;
        }
        //cell 5
        else if (centroid.x>7*width/10+10 && centroid.y>6*height/14 && centroid.x<8*width/10-10 && centroid.y<8*height/14){
            motion_detail.setText("cell5");
            cell.setBounds( 7*width/10+10, 6*height/14,8*width/10-10,8*height/14);
            return cell;
        }
        //cell 4
        else if (centroid.x>8*width/10+10 && centroid.y>6*height/14 && centroid.x<9*width/10-10 && centroid.y<8*height/14){
            motion_detail.setText("cell4");
            cell.setBounds(  8*width/10+10, 6*height/14,9*width/10-10,8*height/14);
            return cell;
        }
        //cell 2
        else if (centroid.x>9*width/10+10 && centroid.y>6*height/14 && centroid.x<width-40 && centroid.y<8*height/14){
            motion_detail.setText("cell2");
            cell.setBounds( 9*width/10+10, 6*height/14,width-40,8*height/14);
            return cell;
        }
        //cell 14
        else if (centroid.x>40 && centroid.y>8*height/14 && centroid.x<width/10 && centroid.y<height-40){
            motion_detail.setText("cell14");
            cell.setBounds( 40, 8*height/14,width/10,height-40);
            return cell;
        }
        //cell 1
        else if (centroid.x>9*width/10+10 && centroid.y>8*height/14 && centroid.x<width-40 && centroid.y<height-40){
            motion_detail.setText("cell2");
            cell.setBounds( 9*width/10, 8*height/14,width-40,height-40);
            return cell;
        }

        cell.setBounds(0, 0, 0,0);
        return cell;
    }
}
