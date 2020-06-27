package br.com.microhobby.utils;

import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.EventHandler;

public interface IPushButtonListener extends EventHandler {
    /** A PRESSED event was dispatched.
     * @see ControlEvent 
     */
    public void controlPushed(ControlEvent e);
}
