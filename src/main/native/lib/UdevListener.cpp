 
#include <UdevListener.h>
#include <stdexcept>

UdevListener::UdevListener () 
{
	this->udev = udev_new();
	if (!this->udev)
		throw std::runtime_error("Cannot create udev context!");
}

UdevListener::~UdevListener () 
{
	udev_unref(udev);
}

void UdevListener::setSubSystem(char *subsystemName) 
{
	this->subsystem = subsystemName;
}

struct udev_device * UdevListener::startListening() 
{
	int fd;
	fd_set fds;
	struct timeval tv;
	int ret;
	
	/* subscribe to subsystem */
	mon = udev_monitor_new_from_netlink(udev, "udev");
	udev_monitor_filter_add_match_subsystem_devtype(mon, 
		subsystem, NULL);
	udev_monitor_enable_receiving(mon);

	/* get file descriptor from monitor */
	fd = udev_monitor_get_fd(mon);

	/* listening until some change */
	while (1) {
		FD_ZERO(&fds);
		FD_SET(fd, &fds);
		tv.tv_sec = 0;
		tv.tv_usec = 0;
		
		ret = select(fd+1, &fds, NULL, NULL, &tv);

		/* check if file descriptor has received data */
		if (ret > 0 && FD_ISSET(fd, &fds)) {
			/* Make the call to receive the device.
				select() ensured that this will not block. */
			dev = udev_monitor_receive_device(mon);
			if (dev) {
				return dev;
			}					
		}

		usleep(250*1000);
		fflush(stdout);
	}
}

const char* UdevListener::getDeviceName () 
{
	return udev_device_get_sysname(dev);
}
