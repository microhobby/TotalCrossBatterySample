package br.com.microhobby;

import br.com.microhobby.System.Power.PowerState;
import br.com.microhobby.Views.MainView;
import br.com.microhobby.utils.GpioPushButton;
import br.com.microhobby.utils.IPushButtonListener;
import totalcross.sys.Settings;
import totalcross.ui.MainWindow;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.gfx.Color;

public class BatteryStateSample extends MainWindow {

    public BatteryStateSample() {
        setUIStyle(Settings.MATERIAL_UI);
    }

    @Override
    public void initUI() {

        MainView mainView = new MainView(this);
        mainView.show();

        // fix the borders
        this.setBackColor(Color.BLACK);

        // hardware resources
        // Colibri iMX6 SODIMM 85
        GpioPushButton powerButton = new GpioPushButton(5, 6);
        
        powerButton.addPushButtonListener(new IPushButtonListener() {
            @Override
            public void controlPushed(ControlEvent e) {
                // lets sleep 😴
                PowerState.StandBy();
            }
        });
    }
}
