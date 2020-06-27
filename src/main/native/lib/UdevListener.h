#ifndef __HEADER_UDEV_LISTENER__
#define __HEADER_UDEV_LISTENER__

#include <stdio.h>
#include <libudev.h>
#include <unistd.h>

class UdevListener {
	char* subsystem;
	struct udev *udev;
	struct udev_device *dev;
	struct udev_monitor *mon;
public:
	UdevListener ();
	~UdevListener ();
	void setSubSystem (char *);
	struct udev_device *startListening ();
	const char* getDeviceName();
};

#endif
