package br.com.microhobby;

import totalcross.TotalCrossApplication;

public class RunBatteryStateSampleApplication {
    public static void main(String [] args) {
        TotalCrossApplication.run(BatteryStateSample.class, "/r",
                                  "5443444B5AAEEB90306B00E4",
                                  "/scr", "1024x600");
        
        /*TotalCrossApplication.run(BatteryStateSample.class, "/r",
                                  "5443444B5AAEEB90306B00E4",
                                  "/scr", "640x480");*/
    }
}
