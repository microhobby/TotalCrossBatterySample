package br.com.microhobby.System.Runtime;

import br.com.microhobby.utils.ExecUtils;

public class RuntimeInformation {
    public static String VMVersion()
    {
        return "6.0.4 tcni-rebased-custom";
    }

    public static String OSArchitecture()
    {
        return ExecUtils.executeProgram("/usr/bin/arch").trim();        
    }

    public static String OSDescription()
    {
        String ret = ExecUtils.executeProgram("/bin/cat /etc/os-release");
        String[] lines = ret.split("\n");
        String description = lines[1].replaceAll("NAME=", "")
                                     .replaceAll("\"", "");

        return (description + " v" + OSKernelVersion());
    }

    public static String OSKernelVersion()
    {
        return ExecUtils.executeProgram("/bin/uname -r").trim();        
    }

    public static String HWModel()
    {
        if (OSArchitecture().equals("armv7l") ||
                OSArchitecture().equals("armv8")) {
            return ExecUtils
                    .executeProgram("/bin/cat /proc/device-tree/model")
                    .trim();
        } else if (OSArchitecture().equals("x86_64")) {
            return ExecUtils.executeProgram(
                "/bin/cat /sys/devices/virtual/dmi/id/board_name").trim();
        }

        return "Unknow";
    }
}
