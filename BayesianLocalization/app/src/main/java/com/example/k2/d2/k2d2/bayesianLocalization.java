package com.example.k2.d2.k2d2;


import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
                pmf_table[(item.getCellNumber())][item.getRSSi()] += (float) 7.0; // TODO : check if + 7 is too much or if it is sufficient.
            }
        }

        for(int i = 0 ; i< pmf_table.length; i++){
            float row_sum = 0;
            for(int j = 0; j < pmf_table[i].length ; j++){
                row_sum += pmf_table[i][j];
            }
            for(int j = 0; j < pmf_table[i].length ; j++){
                pmf_table[i][j] = pmf_table[i][j]/row_sum;
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
//                    if(prior[j]> 0.94) break; // since we have reached a very high probability that this is the cell. TODO: Check if this condition is needed.
                }
                posterior = prior;
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
                pmf_table[i][j] = (float) 0.1;
            }
        }
        return pmf_table;
    }
}
