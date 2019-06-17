package com.example.k2.d2.k2d2pf;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static com.example.k2.d2.k2d2pf.FullscreenActivity.motion_detail;



public class PF extends Walls {

    public int x,y, weight, size;
    private static int sumx=0,sumy=0;
    ShapeDrawable shapeDrawable;


    public PF(int x, int y, int weight, int color, ShapeDrawable shapeDrawable, int size ){
        this.weight=weight;
        this.shapeDrawable=shapeDrawable;
        this.shapeDrawable.getPaint().setColor(color);
        this.size=size;
        shapeDrawable.setBounds(x,y,x+size,y+size);
    }

    public void setBounds(PF pf){

        pf.shapeDrawable.setBounds(pf.x,pf.y,pf.x+pf.size,pf.y+pf.size);
    }

    public static List<PF> InitPF(int width, int height, List<PF> Particals){

        Random random =new Random();
        for (PF partical: Particals){
            partical.x=random.nextInt(width);
            partical.y=random.nextInt(height);
            partical.setBounds(partical);
        }
        CorrectCollision(Particals);
        return Particals;
    }

    public static void drawing(Canvas canvas, List<PF> Particals){
        for(PF partical: Particals) {
            partical.shapeDrawable.draw(canvas);
        }

    }

    private static void CorrectCollision(List<PF> Particals ){
        Random random =new Random();
        List<PF> Correct_PF= new ArrayList<>();
        PF pf;
        int fixed_particals=0;

        for(PF partical: Particals) {
                if (!isCollision(partical)){
                    partical.weight++;
                    fixed_particals++;Correct_PF.add(partical);
                }else{partical.weight=1;}
        }
        for(PF partical: Particals) {
            pf = Correct_PF.get(random.nextInt(fixed_particals));
            partical.x = pf.x;
            partical.y = pf.y;
            partical.setBounds(partical);
        }
    }

    private static  boolean isCollision(PF Partical) {
        for(ShapeDrawable wall : walls) {
            if(isCollision(wall, Partical))
                return true;
        }
        return false;
    }

    private static boolean isCollision(ShapeDrawable first, PF Partical) {
        Rect firstRect = new Rect(first.getBounds());
        return firstRect.intersect(Partical.shapeDrawable.getBounds());
    }

    private static void CheckConvergence(List<PF> Particals){
        for(PF partical: Particals) {
            sumx += partical.x;
            sumy += partical.y;
        }

        sumx/=Particals.size();sumy/=Particals.size();
        motion_detail.setText("sumx="+sumx+"sumy"+sumy);

        int converged=0;
        for (PF partical: Particals){
            if(Math.abs(partical.x-sumx)<700 && Math.abs(partical.y-sumy)<700){converged++;}
        }
        motion_detail.setText(motion_detail.getText()+"\nconverged="+converged);
        if (converged>(Particals.size()*0.8)){
            motion_detail.setText(motion_detail.getText()+"\nconverged!!!");
        }


    }

    private static void KMean( List<PF> Particals){

        int size=Particals.size();
        PF mean1, mean2, pf1, pf2;
        int sumx1=0,sumy1=0, sumx2=0,sumy2=0;

        pf1=Particals.get(0);       mean1=pf1;
        pf2=Particals.get(1);       mean2=pf2;

        List<PF> cluster1, cluster2;
        boolean stopflag=true;
        int k = 0, j = 0;

        while (stopflag){

            sumx1=0;sumy1=0; sumx2=0;sumy2=0;
            cluster1 = new ArrayList<>();
            cluster2 = new ArrayList<>();


            for (int i = 0; i < size; i++) {
//
                if (Math.abs(Particals.get(i).x - mean1.x) <= Math.abs(Particals.get(i).x - mean2.x)
                        && Math.abs(Particals.get(i).y - mean1.y) <= Math.abs(Particals.get(i).y - mean2.y)) {
                    cluster1.add(Particals.get(i));
                    k++;
                } else {
                    cluster2.add(Particals.get(i));
                    j++;
                }
            }


            for (PF partical: cluster1) {
                sumx1+=partical.x;
                sumy1+=partical.y;
            }

            for (PF partical: cluster2) {
                sumx2+=partical.x;
                sumy2+=partical.y;
            }
            pf1.x = mean1.x;    pf1.y=mean1.y;
            pf2.x=mean2.x;      pf2.y=mean2.y;
            mean1.x = Math.round(sumx1 / k);
            mean1.y = Math.round(sumy1 / k);

            mean2.x = Math.round(sumx2 / j);
            mean2.y = Math.round(sumy2 / j);

            stopflag = !(mean1.x == pf1.x && mean1.y == pf1.y && mean2.y == pf2.y && mean2.x == pf2.x);
        }
        motion_detail.setText(motion_detail.getText()+"k="+k+"  ,j="+j);

    }
    public static void fp_movement(int width, int height, String direction, List<PF> Particals, int stepsize) {

        Random random = new Random();

        switch (direction) {
            case "up": {
                motion_detail.setText(null);

                for (PF partical : Particals) {
                    partical.x += (int) random.nextGaussian() * (stepsize/10) * (random.nextBoolean() ? -1 : 1);
                    partical.y -= random.nextInt(stepsize);
                    partical.setBounds(partical);
               }
                break;
            }
            case "down": {
                for (PF partical : Particals) {
                    partical.x += (int) random.nextGaussian() * (stepsize/10) * (random.nextBoolean() ? -1 : 1);
                    partical.y += random.nextInt(stepsize);
                    partical.setBounds(partical);
                }
                break;
            }
            case "right": {
                for (PF partical : Particals) {
                    partical.x += random.nextInt(stepsize);
                    partical.y += (int) random.nextGaussian() * (stepsize/10) * (random.nextBoolean() ? -1 : 1);
                    partical.setBounds(partical);
                }
                break;
            }
            case "left": {
                for (PF partical : Particals) {
                    partical.x -= random.nextInt(stepsize);
                    partical.y += (int) random.nextGaussian() * (stepsize/10) * (random.nextBoolean() ? -1 : 1);
                    partical.setBounds(partical);
              }
                break;
            }
            default:
                break;
        }

        CorrectCollision(Particals);
        CheckConvergence(Particals);
        KMean(Particals);
        }
    }

