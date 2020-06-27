package br.com.microhobby.System.Power;

import br.com.microhobby.utils.ExecUtils;

public class PowerState {
    private static boolean POOR_MUTEX = false;
    
    /**
     * Suspend-to-Idle
     * Freeze user space
     * Suspend the timekeeping
     * Put all I/O devices into low-power states 
     */
    public static void Freeze()
    {
        ExecUtils.writeSysFsFile("standby", "/sys/power/state");
    }

    /**
     * Standby
     * Freeze user space
     * Suspend the timekeeping
     * Put all I/O devices into low-power states
     * Put nonboot CPUs offline
     * Put all low-level system function in suspend state
     */
    public static void StandBy()
    {
        ExecUtils.writeSysFsFile("standby", "/sys/power/state");
    }

    /**
     * Wake Lock
     * allows user space to create wakeup source objects
     */
    public static void WakeLock(String wakeLockName)
    {
        ExecUtils.writeSysFsFile(wakeLockName, "/sys/power/wake_lock");
    }

    /**
     * Wake Unlock
     * allows user space to create wakeup source objects
     */
    public static void WakeUnlock(String wakeLockName)
    {
        ExecUtils.writeSysFsFile(wakeLockName, "/sys/power/wake_unlock");
    }

    /**
     * Use wakeup count
     * allows user space to put the system into a sleep state while taking into
     * account the concurrent arrival of wakeup events
     */
    public static void Suspend()
    {
        if (!POOR_MUTEX) {
            POOR_MUTEX = true;
            (new Thread(new Runnable(){
                @Override
                public void run() {
                    boolean canISuspend = false;

                    while (!canISuspend) {
                        // block until all the locks are released
                        String lock_count =
                            ExecUtils.readSysFsFile("/sys/power/wakeup_count");
                        canISuspend = ExecUtils
                            .writeSysFsFile(lock_count, "/sys/power/wakeup_count");
                        
                        if (canISuspend) {
                            PowerState.StandBy();
                            POOR_MUTEX = false;
                        }
                    }
                }
            })).start();
        }
    }
}
