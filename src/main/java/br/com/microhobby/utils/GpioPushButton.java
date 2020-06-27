package br.com.microhobby.utils;

import java.util.ArrayList;
import java.util.List;

import totalcross.io.device.gpiod.GpiodChip;
import totalcross.io.device.gpiod.GpiodLine;
import totalcross.sys.Vm;
import totalcross.ui.event.ControlEvent;

public class GpioPushButton {

    public static final int HIGH = 1;
    public static final int LOW = 0;

    GpiodChip gpioBank;
    GpiodLine gpiodLine;

    Thread gpioTask;
    boolean stopGpioTask = false;
    List<IPushButtonListener> eventListeners;

    public GpioPushButton(int bank, int line)
    {
        gpioBank = GpiodChip.open(bank);
        gpiodLine = gpioBank.line(line);

        gpiodLine.requestInput("TC GpioButton");

        eventListeners = new ArrayList<IPushButtonListener>();

        // let's create the thread prototype
        gpioTask = new Thread(new Runnable(){
            @Override
            public void run() {
                while (!stopGpioTask) {
                    if (gpiodLine.getValue() == HIGH) {
                        ControlEvent controlEvent = new ControlEvent();
                        for (IPushButtonListener iPushListener : eventListeners) {
                            iPushListener.controlPushed(controlEvent);
                        }
                    }

                    // debounce
                    Vm.sleep(200);
                }
            }
        });
    }

    public void addPushButtonListener(IPushButtonListener eventListener)
    {
        eventListeners.add(eventListener);

        // ok, now we have listeners so lets start the task
        if (eventListeners.size() == 1)
            gpioTask.start();
    }

    public void removePushButtonListener(IPushButtonListener eventListener)
    {
        eventListeners.remove(eventListener);

        // 😢 no more listeners, lets stop the task
        if (eventListeners.size() == 0)
            stopGpioTask = true;
    }
}
