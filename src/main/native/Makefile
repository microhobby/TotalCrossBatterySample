CFLAGS = -fPIC -c -Os -Wall -ggdb3 -DDEBUG
LIBS = -ludev
INCLUDES = -Ilib
FILES_LIB = lib/UdevListener.cpp
MAIN = TCNIUdev.cpp
FILES_OBJ = UdevListener.o
MAIN_OBJ = TCNIUdev.o
ARCH ?= x86_64

fuel-gauge:
	ulimit -c unlimited
	$(CXX) $(CFLAGS) $(INCLUDES) $(FILES_LIB) $(MAIN) $(LIBS)
	$(CXX) -shared $(FILES_OBJ) $(MAIN_OBJ) -o libTCNIUdev.so $(LIBS)

install:
ifeq ($(ARCH),x86_64)
	cp libTCNIUdev.so /usr/lib/x86_64-linux-gnu/
else
	cp libTCNIUdev.so /usr/lib/arm-linux-gnueabihf/
endif

clean:
	rm $(FILES_OBJ) $(MAIN_OBJ) libTCNIUdev.so
