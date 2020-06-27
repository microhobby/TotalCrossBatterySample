#include <cstdio>
#include <cstdlib>
#include <UdevListener.h>
#include "string.h"
#include "TCNIUdev.h"

const char * udev_listener ()
{
    UdevListener* udev = new UdevListener();

    udev->setSubSystem("power_supply");
    // this will block the process
    udev->startListening();
    printf("UEVENT has been received from %s!\n", udev->getDeviceName());

    delete udev;

    return udev->getDeviceName();
}
