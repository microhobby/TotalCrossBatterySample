package br.com.microhobby.utils;

import totalcross.ni.*;

public class TCNIUdev {

    public TCNIUdev ()
    {
        Runtime.getRuntime().loadLibrary("TCNIUdev");
    }

    public String startListener ()
    {
        // make the call
        return (String) TCNI.invokeMethod("TCNIUdev", "udev_listener",
                                          String.class);
    }
}
