package br.com.microhobby.Views;

import totalcross.sys.Vm;
import totalcross.ui.Button;
import totalcross.ui.ImageControl;
import totalcross.ui.Label;
import totalcross.ui.MainWindow;
import totalcross.ui.ProgressBar;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.PressListener;
import totalcross.ui.gfx.Color;
import br.com.microhobby.System.Power.PowerSupplyInfo;
import br.com.microhobby.System.Power.PowerState;

public class MainView extends View {

    public static final String BATTERY_STATE_LOCK = "BatteryStateLock";

    Label _status;
    Label _voltage;
    ProgressBar _progressBarBatteryLevel;
    Button _buttonAbout;
    Button _buttonSleep;
    Button _buttonWakeLock;

    ImageControl _imgLowAlert;
    ImageControl _imgUevent;
    ImageControl _imgWakeLock;

    Thread udevThread;
    Thread lowAlertThread;
    boolean udevThreadStop = false;
    boolean loadAlertThreadStop = false;
    boolean wakeLocked = false;

    public MainView(MainWindow mainWindow) {
        super(mainWindow);
    }

    public void init() {
        _status = (Label) View.getControlByID("@+id/status");
        _voltage = (Label) View.getControlByID("@+id/voltage");

        _buttonAbout = (Button) View.getControlByID("@+id/button_about");
        _buttonAbout.setBackColor(Color.getRGB("ABABAB"));

        _buttonSleep = (Button) View.getControlByID("@+id/button_sleep");
        _buttonSleep.setBackColor(Color.getRGB("ABABAB"));

        _buttonWakeLock = (Button) View.getControlByID("@+id/button_wake_lock");
        _buttonWakeLock.setBackColor(Color.getRGB("ABABAB"));

        _imgLowAlert = (ImageControl) View.getControlByID("@+id/img_low_alert");
        _imgLowAlert.setVisible(false);
        _imgWakeLock = (ImageControl) View.getControlByID("@+id/img_wake_lock");
        _imgWakeLock.setVisible(false);
        _imgUevent = (ImageControl) View.getControlByID("@+id/img_uevent_alert");
        _imgUevent.setVisible(false);

        _progressBarBatteryLevel = (ProgressBar)
            View.getControlByID("@+id/progressBarBatteryLevel");
        _progressBarBatteryLevel.setBackColor(Color.getRGB("1C1C1C"));
        _progressBarBatteryLevel.setForeColor(Color.getRGB("03c200"));
        _progressBarBatteryLevel.max = 100;
        _progressBarBatteryLevel.min = 0;

        PowerSupplyInfo ps = new PowerSupplyInfo("BAT1");
        //PowerSupplyInfo ps = new PowerSupplyInfo("battery");

        udevThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!udevThreadStop) {
                    boolean abortThread = udevThreadStop;
                    // monitoring uevents
                    ps.waitEvent();

                    // time to die?
                    if (abortThread)
                        return;
                    
                    // notify the UI
                    ps.updateStatus();
                    UpdateBatteryStatusUI(ps);
                    blinkImage(_imgUevent);
                }
            }
        });

        lowAlertThread = new Thread(new Runnable(){
            @Override
            public void run() {
                while (!loadAlertThreadStop) {
                    blinkImage(_imgLowAlert);
                }
            }
        });

        ps.getCapacity();
        ps.getVoltage();
        UpdateBatteryStatusUI(ps);

        // lets start check the udev
        udevThreadStop = false;
        loadAlertThreadStop = false;
        udevThread.start();

        _buttonAbout.addPressListener(onAbouPressListener);
        _buttonSleep.addPressListener(onSleepPressListener);
        _buttonWakeLock.addPressListener(onWakeLockPressListener);
    }

    private void UpdateBatteryStatusUI(PowerSupplyInfo powerSupplyInfo)
    {
        // color
        if (powerSupplyInfo.Capacity < 15)
            _progressBarBatteryLevel.setForeColor(Color.getRGB("c70000"));
        else if (powerSupplyInfo.Capacity < 55)
            _progressBarBatteryLevel.setForeColor(Color.getRGB("ffd000"));
        else
            _progressBarBatteryLevel.setForeColor(Color.getRGB("03c200"));
        
        if (powerSupplyInfo.Capacity <= 15)
            lowAlertThread.start();
        
        _progressBarBatteryLevel.setValue(powerSupplyInfo.Capacity);

        _status.setText( "Capacity:     " + powerSupplyInfo.Capacity + "%");
        _voltage.setText("Voltage:      " + powerSupplyInfo.VoltageNow);
    }

    private void blinkImage(ImageControl img)
    {
        for (int i = 0; i < 4; i++) {
            img.setVisible(true);
            Vm.sleep(500);
            img.setVisible(false);
            Vm.sleep(500);
        }
    }

    PressListener onAbouPressListener = new PressListener() {
        @Override
        public void controlPressed(ControlEvent e) {
            AboutView aboutView = new AboutView(getMainWindow());
            aboutView.show();
        }
    };

    PressListener onSleepPressListener = new PressListener() {
        @Override
        public void controlPressed(ControlEvent e) {
            PowerState.Suspend();
        }
    };

    PressListener onWakeLockPressListener = new PressListener() {
        @Override
        public void controlPressed(ControlEvent e) {
            if (!wakeLocked) {
                PowerState.WakeLock(BATTERY_STATE_LOCK);
                wakeLocked = true;
                _imgWakeLock.setVisible(wakeLocked);
            } else {
                PowerState.WakeUnlock(BATTERY_STATE_LOCK);
                wakeLocked = false;
                _imgWakeLock.setVisible(wakeLocked);
            }
        }
    };

    public void clean()
    {
        _buttonAbout.removePressListener(onAbouPressListener);
        _buttonSleep.removePressListener(onSleepPressListener);
        _buttonWakeLock.removePressListener(onWakeLockPressListener);
        udevThreadStop = true;
        loadAlertThreadStop = true;
    }
}
