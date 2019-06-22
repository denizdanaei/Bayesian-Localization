package com.example.k2.d2.k2d2pf;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static com.example.k2.d2.k2d2pf.FullscreenActivity.motion_detail;



public class PF extends Walls {

    public int x,y, weight, size, color;
    private static int sumx=0,sumy=0;
    ShapeDrawable shapeDrawable;
    private static PF centroid = new PF(0,0,1,Color.GREEN,  new ShapeDrawable(new OvalShape()), 40);
    private static PF mean1= new PF(0,0,1,Color.RED,  new ShapeDrawable(new OvalShape()), 40);
    private static PF mean2= new PF(0,0,1,Color.BLUE,  new ShapeDrawable(new OvalShape()), 40);
    private static PF mean3= new PF(0,0,1,Color.YELLOW,  new ShapeDrawable(new OvalShape()), 40);
    private static List<PF> cluster1, cluster2, cluster3;

    public static  boolean convergence;


    public PF(int x, int y, int weight, int color, ShapeDrawable shapeDrawable, int size ){
        this.weight=weight;
        this.shapeDrawable=shapeDrawable;
        this.color=color;
        this.shapeDrawable.getPaint().setColor(color);
        this.size=size;
        shapeDrawable.setBounds(x,y,x+size,y+size);
    }

    public void setBounds(PF pf){

        pf.shapeDrawable.getPaint().setColor(color);

        pf.shapeDrawable.setBounds(pf.x,pf.y,pf.x+pf.size,pf.y+pf.size);
    }

    public static List<PF> InitPF(int width, int height, List<PF> Particals){

        Random random =new Random();
        for (PF partical: Particals){
            partical.x=random.nextInt(width);
            partical.y=random.nextInt(height);
            partical.setBounds(partical);
        }
        CorrectCollision(width, height, Particals);
        return Particals;
    }

    public static void drawing(Canvas canvas, List<PF> Particals){
        for(PF partical: Particals) {
            partical.shapeDrawable.draw(canvas);
        }

    }

    private static void CorrectCollision(int width, int height, List<PF> Particals ){
        Random random =new Random();
        List<PF> Correct_PF= new ArrayList<>();
        PF pf;
        int fixed_particals=0;

        for(PF partical: Particals) {
                if (!isCollision(partical) && isDisplayed(width,height,partical)){
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

    private static  boolean isDisplayed(int width, int height,PF Partical){

        if(Partical.x<width && Partical.x>0 && Partical.y<height && Partical.y>0){return true;}
        return false;
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

    public static PF CheckConvergence(List<PF> Particals){

        for(PF partical: Particals) {
            sumx += partical.x;
            sumy += partical.y;
        }

        sumx/=Particals.size();sumy/=Particals.size();
//        motion_detail.setText(motion_detail.getText()+"\nsum=("+sumx+","+sumy+")");

        int converged=0;
        for (PF partical: Particals){
            if(Math.abs(partical.x-sumx)<300 && Math.abs(partical.y-sumy)<500){converged++;}
        }

        motion_detail.setText(motion_detail.getText()+"\nconverged="+converged+" centroid=("+sumx+","+sumy+")\t");
        centroid.x=sumx; centroid.y=sumy;
        centroid.setBounds(centroid);

        if ((converged>(Particals.size()*0.8))&& aBooleandistance(centroid,mean1,mean2,mean3)){

            motion_detail.setText(motion_detail.getText()+"\nconverged!!!");
            convergence=true;
        } else {convergence=false;}
        return centroid;
    }

    public static void fp_movement(int width, int height, String direction, List<PF> Particals, int stepsize) {

        Random random = new Random();

        switch (direction) {
            case "up": {
                for (PF partical : Particals) {
                    int movement= random.nextInt(stepsize)*2;
                    partical.x += (int) random.nextGaussian() * (movement/3) * (random.nextBoolean() ? -1 : 1);
                    partical.y -= movement;
                    partical.setBounds(partical);
               }
                break;
            }
            case "down": {
                for (PF partical : Particals) {
                    int movement= random.nextInt(stepsize)*2;
                    partical.x += (int) random.nextGaussian() * (movement/3) * (random.nextBoolean() ? -1 : 1);
                    partical.y += movement;
                    partical.setBounds(partical);
                }
                break;
            }
            case "right": {
                for (PF partical : Particals) {
                    int movement= random.nextInt(stepsize)*2;
                    partical.x +=movement;
                    partical.y += (int) random.nextGaussian() * (movement/3) * (random.nextBoolean() ? -1 : 1);
                    partical.setBounds(partical);
                }
                break;
            }
            case "left": {
                for (PF partical : Particals) {
                    int movement= random.nextInt(stepsize)*2;
                    partical.x -= movement;
                    partical.y += (int) random.nextGaussian() * (movement/3) * (random.nextBoolean() ? -1 : 1);
                    partical.setBounds(partical);
              }
                break;
            }
            default:
                break;
        }

        CorrectCollision(width,height,Particals);
//        CheckConvergence(Particles);
//        KMean(Particles);
        }
    public static List<PF> KMean( List<PF> Particals){

        int size=Particals.size();
        PF pf1, pf2,pf3;
        int sumx1=0,sumy1=0, sumx2=0,sumy2=0, sumx3=0,sumy3=0;

        pf1=Particals.get(0);       mean1=pf1;
        pf2=Particals.get(1);       mean2=pf2;
        pf3=Particals.get(2);       mean3=pf3;


        boolean stopflag=true;
        int k = 0, j = 0, l= 0;

        while (stopflag){

            sumx1=0;sumy1=0;    sumx2=0;sumy2=0;   sumx3=0;sumy3=0;
            cluster1 = new ArrayList<>();
            cluster2 = new ArrayList<>();
            cluster3 = new ArrayList<>();

            for (int i = 0; i < size; i++) {

                String distance=distance(Particals.get(i),mean1,mean2,mean3);
                switch (distance){

                    case ("cluster1"):

                        cluster1.add(Particals.get(i));
                        k++;
                        break;

                    case ("cluster2"):
                        cluster2.add(Particals.get(i));
                        j++;
                        break;

                    case ("cluster3"):
                        cluster3.add(Particals.get(i));
                        l++;
                        break;
                    default:
                        break;
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

            for (PF partical: cluster3) {
                sumx3+=partical.x;
                sumy3+=partical.y;
            }

            pf1.x = mean1.x;    pf1.y=mean1.y;
            pf2.x=  mean2.x;    pf2.y=mean2.y;
            pf3.x = mean3.x;    pf3.y=mean3.y;

            if(k != 0) {
                mean1.x = Math.round(sumx1 / k);
                mean1.y = Math.round(sumy1 / k);
            }else{
                mean1.x = 0;
                mean1.y = 0;
            }
            if(j != 0) {
                mean2.x = Math.round(sumx2 / j);
                mean2.y = Math.round(sumy2 / j);
            }else{
                mean2.x =0;
                mean2.y =0;
            }
            if(l !=0){
                mean3.x = Math.round(sumx2 / l);
                mean3.y = Math.round(sumy2 / l);
            }else{
                mean3.x=0;
                mean3.y=0;
            }

            stopflag = !(mean1.x == pf1.x || mean1.y == pf1.y || mean2.y == pf2.y || mean2.x == pf2.x || mean3.y == pf3.y || mean3.x == pf3.x);
        }
        motion_detail.setText(motion_detail.getText()+"\nk="+k+"  ,j="+j + "    ,l="+l);


        mean1.setBounds(mean1);
        mean2.setBounds(mean2);
        mean3.setBounds(mean3);

        List<PF> kmeans=new ArrayList<>();
        kmeans.add(mean1);kmeans.add(mean2); kmeans.add(mean3);
        return kmeans;
    }
    private static int length(PF pf1, PF pf2){
        double x=(double) (pf1.x- pf2.x);
        double y=(double) (pf1.y- pf2.y);

        int length =(int)Math.sqrt((Math.pow(x,2))+(Math.pow(y,2)));
        return length;
    }
    private static String distance(PF pf, PF pf1, PF pf2, PF pf3) {

        int length_pf_pf1=length(pf,pf1);
        int length_pf_pf2=length(pf,pf2);
        int length_pf_pf3=length(pf,pf3);
        int distance= Math.min(length_pf_pf1, Math.min(length_pf_pf2,length_pf_pf3));


        if(distance==length_pf_pf1){return "cluster1";}

        else if(distance==length_pf_pf2){return "cluster2";}

        else{return "cluster3";}

    }
    private static boolean aBooleandistance(PF pf, PF pf1, PF pf2, PF pf3) {

        int length_pf_pf1=length(pf,pf1);
        int length_pf_pf2=length(pf,pf2);
        int length_pf_pf3=length(pf,pf3);
        int distance= Math.min(length_pf_pf1, Math.min(length_pf_pf2,length_pf_pf3));

        int cluster1_size=cluster1.size();

        int cluster2_size=cluster2.size();

        int cluster3_size=cluster3.size();
        int biggest_cluster= Math.max(cluster1_size, Math.max(cluster2_size,cluster3_size));

//        if ((length_pf_pf1<200 && length_pf_pf2<200 && length_pf_pf3<100))
        if((biggest_cluster>900 && distance<300) || (length_pf_pf1<200 && length_pf_pf2<200 && length_pf_pf3<100))
        {return true;}
        else{return false;}

    }


//    private static int[] centroid(PF pf){
//
//    }
}

