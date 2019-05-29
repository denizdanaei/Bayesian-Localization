package com.example.k2.d2.k2d2;


import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static com.example.k2.d2.k2d2.Tab_training.columnsize;
import static com.example.k2.d2.k2d2.Tab_training.numberOfLevels;
import static com.example.k2.d2.k2d2.Tab_training.pmf_data;
import static com.example.k2.d2.k2d2.Tab_training.posterior;
import static com.example.k2.d2.k2d2.Tab_training.prior;
import static com.example.k2.d2.k2d2.Tab_training.rowsize;
import static com.example.k2.d2.k2d2.Tab_training.cellnum;

public class bayesianLocalization {

    /*
        Main Function that starts the Bayesian algorithm.
     */
    public static void training_Setup(gsonParser[] items){
        Set<String> bssi = new HashSet<>();
        for(int i = 0;i < items.length ;i++){
            bssi.add(items[i].getBSSI());
        }
        //Initializing all the values of the table to 1
        Float[][] pmf_table = initializeTable();
        for(String bss : bssi){
            pmf_table = updateTables(items, pmf_table, bss);
            pmf_data.put(bss,pmf_table);
            pmf_table = initializeTable();
        }
    }

    public static Float[][] updateTables(gsonParser[] items, Float[][] pmf_table, String bssi){
        for(gsonParser item : items){
            if(item.getBSSI().equals(bssi)){
                pmf_table[(item.getCellNumber())][item.getRSSi()] += (float) 10.0; // TODO : check if + 7 is too much or if it is sufficient.
            }
        }
        /**
         * The Below part is included as part of a Moving average filter. This gives us a smooth transition rather than the peaks that we have in the original data.
         * */
        int windowSize = 3; // The frame length for the taking the average.
        for(int i = 0;i <pmf_table.length;i++){
            Queue<Float> movingAverageFilterQueue = new LinkedList<>();
            for(int init =0 ; init<windowSize-1; init++){
                (movingAverageFilterQueue).add(pmf_table[i][0]);  // initializing the first two values
            }
            for(int j =0; j<pmf_table[i].length;j++){
                if(j+1 >= pmf_table[i].length) break;
                (movingAverageFilterQueue).add(pmf_table[i][j+1]); // adding the new value as a tail
                float row_sum = 0;
                for(int k =0; k<movingAverageFilterQueue.size(); k++){
                    row_sum += ((LinkedList<Float>) movingAverageFilterQueue).get(k); // sum
                }
                pmf_table[i][j] = row_sum/windowSize; // average
                ((LinkedList<Float>) movingAverageFilterQueue).set(windowSize%2,pmf_table[i][j]); // updating the existing queue value
                movingAverageFilterQueue.remove(); // removing the head of the queue so that we can push in the tail value in the next iteration.
            }
        }
        for(int i = 0 ; i< pmf_table.length; i++){
            float row_sum = 0;
            for(int j = 0; j < pmf_table[i].length ; j++){
                row_sum += pmf_table[i][j];
            }
            for(int j = 0; j < pmf_table[i].length ; j++){
                pmf_table[i][j] = pmf_table[i][j]/row_sum;  //normalizing
            }
        }
        return pmf_table;
    }

    // Localizing using the current scan.
    public static int localize(List<ScanResult> scanResults){
        prior = new Float[]{(float)1/cellnum, (float)1/cellnum, (float)1/cellnum, (float)1/cellnum, (float)1/cellnum, (float)1/cellnum,
                (float)1/cellnum, (float)1/cellnum, (float)1/cellnum, (float)1/cellnum};
        posterior = new Float[]{(float)1/cellnum, (float)1/cellnum, (float)1/cellnum, (float)1/cellnum, (float)1/cellnum, (float)1/cellnum,
                (float)1/cellnum, (float)1/cellnum, (float)1/cellnum, (float)1/cellnum};
        Float[] pulled_data  = new Float[cellnum] ; // variable used to pull out our required data off of the offline table.
        Set<String> bssi = new HashSet<>();
        /*
        // This step is added since we need to make sure there are no null pointer exceptions for any new bssi id's that might pop up during demo.
         */
        for (Map.Entry<String, Float[][]> entry : pmf_data.entrySet()) {
            bssi.add(entry.getKey()); // stores the our unique bssi id's so that there are no duplicates.
        }
        for (ScanResult scanResult : scanResults) {
            if (bssi.contains(scanResult.BSSID)) {
                Float[][] pmf_table = pmf_data.get(scanResult.BSSID);
                for (int i = 0; i < pulled_data.length; i++) {
                    pulled_data[i] = pmf_table[i][WifiManager.calculateSignalLevel(scanResult.level, numberOfLevels)];
                }
                float prior_sum = 0;
                for (int ii = 0; ii < pulled_data.length; ii++) {
                    prior[ii] = prior[ii] * pulled_data[ii];
                    prior_sum += prior[ii]; //calculating the row sum for normalization.
                }
                for (int j = 0; j < pulled_data.length; j++) { // normalizing each row.
                    prior[j] = prior[j] / prior_sum;
                }
                posterior = prior;
                for(int i =0; i< posterior.length;i++){
                    if(posterior[i]>0.95){ //since we have reached a very high probability that this is the cell.
                        break;
                    }
                }
            }
        }
        int location = 0; //stores the location that needs to be sent.
        float temp = (float) 0.0; //since comparison is in float we need a temporary float variable.
        for (int k = 0; k < posterior.length; k++) {
            if (posterior[k] > temp) {
                location = k;
                temp = posterior[k]; //updating temp variable to the new greater probability.
            }
        }
        return location;
    }

    // initializing table for each BSSI id.
    public static Float[][] initializeTable(){
        Float[][] pmf_table = new Float[rowsize][columnsize+1] ;
        for(int i = 0;i<pmf_table.length;i++){
            for(int j =0; j<pmf_table[i].length;j++){
                pmf_table[i][j] = (float) 1;
            }
        }
        return pmf_table;
    }
}
