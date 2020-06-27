package br.com.microhobby.Views;

import totalcross.ui.Button;
import totalcross.ui.Label;
import totalcross.ui.MainWindow;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.PressListener;
import totalcross.ui.gfx.Color;
import br.com.microhobby.System.Runtime.RuntimeInformation;

public class AboutView extends View {
    
    Label _labelOsInfo;
    Label _labelArch;
    Label _labelVm;
    Label _labelModel;
    Button _buttonBack;

    public AboutView(MainWindow mainWindow)
    {
        super(mainWindow);
    }

    public void init()
    {
        _labelOsInfo = (Label) View.getControlByID("@+id/osinfo_label");
        _labelArch = (Label) View.getControlByID("@+id/arch_label");
        _labelVm = (Label) View.getControlByID("@+id/tcvm_label");
        _labelModel = (Label) View.getControlByID("@+id/model_label");
        _buttonBack = (Button) View.getControlByID("@+id/button_back");
        _buttonBack.setBackColor(Color.getRGB("ABABAB"));

        _labelModel.setText(RuntimeInformation.HWModel());
        _labelArch.setText(RuntimeInformation.OSArchitecture());
        _labelOsInfo.setText(RuntimeInformation.OSDescription());
        _labelVm.setText(RuntimeInformation.VMVersion());

        _buttonBack.addPressListener(onButtonBackPressed);
    }

    PressListener onButtonBackPressed = new PressListener(){
        @Override
        public void controlPressed(ControlEvent e) {
            back();
        }
    };

    public void clean()
    {
        _buttonBack.removePressListener(onButtonBackPressed);
    }
}
