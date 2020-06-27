package br.com.microhobby.System.Power;

import br.com.microhobby.utils.TCNIUdev;
import br.com.microhobby.utils.ExecUtils;

public class PowerSupplyInfo {
    // uevent data
    public int Capacity;
    public String Status;
    public double VoltageNow;

    // utils
    String batteryPath;
    String batteryID;
    String retDeviceEvent;
    TCNIUdev udev;

    public static final String POWER_SUPPLY_PATH = "/sys/class/power_supply/";

    public PowerSupplyInfo (String batteryID)
    {
        this.batteryID = batteryID;
        this.batteryPath = POWER_SUPPLY_PATH + batteryID;
        udev = new TCNIUdev();
    }

    public void waitEvent ()
    {
        retDeviceEvent = udev.startListener();
    }

    public void updateStatus()
    {
        if (retDeviceEvent.equals(batteryID))
        {
            // event received lets update the status
            getCapacity();
            getVoltage();
        }
    }

    public int getCapacity ()
    {
        String ret = ExecUtils.readSysFsFile(batteryPath + "/capacity");

        if (ret != null) {
            Capacity = Integer.parseInt(ret);

            return Capacity;
        }

        // undefined
        return -1;
    }

    public double getVoltage ()
    {
        String ret = ExecUtils.readSysFsFile(batteryPath + "/voltage_now");

        if (ret != null) {
            long raw = Long.parseLong(ret);
            VoltageNow = raw / 1000000d;

            return VoltageNow;
        }

        // undefined
        return -1;
    }
}
