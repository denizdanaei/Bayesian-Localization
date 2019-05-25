package com.example.k2.d2.k2d2;

public class gsonParser {
    public String getBSSI() {
        return BSSI;
    }

    public int getRSSi() {
        return RSSi;
    }

    public int getCellNumber() {
        return cellNumber;
    }

    private String BSSI ;
    private int RSSi;
    private int cellNumber;

    public gsonParser(String BSSI, int RSSI, int cellNumber){
        this.BSSI = BSSI;
        this.RSSi = RSSI;
        this.cellNumber = cellNumber;
    }
}
