package com.example.k2.d2.k2d2pf;


import java.io.BufferedReader;
        import java.io.FileNotFoundException;
        import java.io.FileReader;
        import java.io.IOException;
        import java.util.*;

import static com.example.k2.d2.k2d2pf.FullscreenActivity.motion_detail;

public class Kmean{



    public static List<PF> KMean( List<PF> Particals){

        int size=Particals.size();
        PF pf1, pf2,pf3, mean1,mean2, mean3;
        int sumx1=0,sumy1=0, sumx2=0,sumy2=0, sumx3=0,sumy3=0;

        pf1=Particals.get(0);       mean1=pf1;
        pf2=Particals.get(1);       mean2=pf2;
        pf3=Particals.get(2);       mean3=pf3;

        List<PF> cluster1, cluster2, cluster3;
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

            mean1.x = Math.round(sumx1 / k);
            mean1.y = Math.round(sumy1 / k);

            mean2.x = Math.round(sumx2 / j);
            mean2.y = Math.round(sumy2 / j);


            mean3.x = Math.round(sumx2 / l);
            mean3.y = Math.round(sumy2 / l);

            stopflag = !(mean1.x == pf1.x || mean1.y == pf1.y || mean2.y == pf2.y || mean2.x == pf2.x || mean3.y == pf3.y || mean3.x == pf3.x);
        }
        motion_detail.setText(motion_detail.getText()+"\nk="+k+"  ,j="+j +"mean1=("+mean1.x+","+mean1.y+")\tmean2=("+mean2.x+","+mean2.y+")");

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

    private void incircle(PF pf1, PF pf2, PF pf3){

        int pf1_2= length(pf1,pf2);
        int pf1_3= length(pf1,pf3);
        int pf2_3= length(pf2,pf3);
        int p=pf1_2+pf1_3+pf2_3;
        int incircle_x= ((pf1.x*pf2_3)+(pf2.x*pf1_3)+(pf3.x*pf1_2))/p;
        int incircle_y= ((pf1.y*pf2_3)+(pf2.y*pf1_3)+(pf3.y*pf1_2))/p;
    }
}