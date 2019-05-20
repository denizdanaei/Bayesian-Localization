package com.example.myapplication;

public class gsonParser {
    public String getBSSI() {
        return BSSI;
    }

    public int getRSSi() {
        return RSSi;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    private String BSSI ;
    private int RSSi;
    private String cellNumber;

    public gsonParser(String BSSI, int RSSI, String cellNumber){
        this.BSSI = BSSI;
        this.RSSi = RSSI;
        this.cellNumber = cellNumber;
    }
}
