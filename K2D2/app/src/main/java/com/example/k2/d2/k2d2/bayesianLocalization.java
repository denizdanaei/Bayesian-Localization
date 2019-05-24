package com.example.k2.d2.k2d2;


import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.k2.d2.k2d2.tab_training.columnsize;
import static com.example.k2.d2.k2d2.tab_training.pmf_data;
import static com.example.k2.d2.k2d2.tab_training.posterior;
import static com.example.k2.d2.k2d2.tab_training.prior;
import static com.example.k2.d2.k2d2.tab_training.rowsize;

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
                pmf_table[(item.getCellNumber())][item.getRSSi()] += (float) 10.0; // TODO : check if + 10 is too much or if it is sufficient.
            }
        }

        for(int i = 0 ; i< pmf_table.length; i++){
            int row_sum = 0;
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
        int k=0;
        Float[] pulled_data  = new Float[3] ; // variable used to pull out our required data off of the offline table.
        for (ScanResult scanResult : scanResults) {
            Float[][] pmf_table = pmf_data.get(scanResult.BSSID);
            for(int i = 0; i<pulled_data.length; i++){
                pulled_data[i] = pmf_table[i][WifiManager.calculateSignalLevel(scanResult.level,30)];
            }
            int prior_sum = 0;
            for(int ii = 0; ii < pulled_data.length; ii++){
                prior[ii] =  prior[ii] * pulled_data[ii];
                prior_sum += prior[ii];
            }
            for(int j = 0; j < pulled_data.length; j++){ // normalizing each row.
                prior[j] =  prior[j] / prior_sum;
            }
            posterior = prior;
        }
        for(k = 0; k < pulled_data.length; k++){
            float next=0;
            if (posterior[k] > next) {
                next=posterior[k];
            }
        }
        return k;
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
