Raspberry PI 3 Installation Walkthrough
=======================================
(C)2016 Oliver Rode - cc-by-sa-40. Please notice the references at the end of the document.

# Setup Development Environment
## 1.	Create Pi 3 Boot Configuration
### a.	Create Raspian Jessie Boot microSD (8GB) 
	Steps:
	1.	Download [1] / Install win32diskimager
	2.	Download [2] / Unpack raspbian-jessie 
	3.	Install image on 8GB-SD card with win32diskimager
	4.	Copy this document content in ASCII file on boot partition
	5.	Insert SD in Pi and boot

### b.	Prepare SD for Creating Offline Installation Image
	Steps:
	1.	On Desktop: Preferences->Keyboard Settings -> German
	2.	Open New Terminal
	3.	cd Downloads
	4.	wget http://www.kernel.org/pub/linux/bluetooth/bluez-5.40.tar.xz
	5.	wget https://gist.github.com/pelwell/c8230c48ea24698527cd/archive/3b07a1eb296862da889609a84f8e10b299b7442d.zip
	9.	sudo apt-get update
	10.	sudo apt-get -d --yes install bluetooth
	11.	sudo apt-get -d --yes install bluez-tools
	12.	sudo apt-get -d --yes install build-essential
	13.	sudo apt-get -d --yes install autoconf
	14.	sudo apt-get -d --yes install glib2.0
	15.	sudo apt-get -d --yes install libglib2.0-dev
	16.	sudo apt-get -d --yes install libdbus-1-dev
	17.	sudo apt-get -d --yes install libudev-dev
	18.	sudo apt-get -d --yes install libical-dev
	19.	sudo apt-get -d --yes install libreadline-dev
	20.	sudo apt-get -d --yes install automake
	21.	sudo apt-get -d --yes install build-essential
	22.	sudo apt-get -d --yes install doxygen
	23.	sudo cp /boot/cmdline.txt /boot/cmdline.txt.bootsd
	24.	sudo vi /boot/cmdline.txt.bootusb
	# Replace content by:
	dwc_otg.lpm_enable=0 root=/dev/sda1 rootfstype=ext4 noatime quiet rootwait loglevel=1 zram.num_devices=2
	25.	sudo vi /boot/fstab.usb
	# Replace content by:
	proc            /proc    proc    defaults          0       0
	/dev/mmcblk0p1  /boot    vfat    defaults          0       0
	/dev/sda1       /        ext4    defaults,noatime  0       0

### c.	Create USB-Stick (16+GB) from SD Image [6]
	Steps:
	1.	On Desktop: Open New Terminal
	2.	sudo umount /dev/sda1/
	3.	sudo mkfs.ext4 /dev/sda1
	4.	sudo mount /dev/sda1 /mnt
	5.	cd /
	6.	sudo rsync -xa / /mnt/
	7.	sudo cp /boot/fstab.usb /mnt/etc/fstab	
	8.	sudo cp /boot/cmdline.txt.bootusb /boot/cmdline.txt
	9.	sync
	10.	reboot


## 2.	Install Bluetooth 4.0 LE / bluez 5.40 [7,8,9]
	Steps:
	1.	On Desktop: Open New Terminal
	2.	sudo apt-get --yes install bluetooth
	3.	sudo apt-get --yes install bluez-tools
	4.	sudo apt-get --yes install build-essential
	5.	sudo apt-get --yes install autoconf
	6.	sudo apt-get --yes install glib2.0
	7.	sudo apt-get --yes install libglib2.0-dev
	8.	sudo apt-get --yes install libdbus-1-dev
	9.	sudo apt-get --yes install libudev-dev
	10.	sudo apt-get --yes install libical-dev
	11.	sudo apt-get --yes install libreadline-dev
	12.	tar xf Downloads/bluez-5.40.tar.xz
	13.	sudo mv bluez-5.40 /opt
	14.	cd /opt/bluez-5.40
	15.	unzip ~/Downloads/3b07a1eb296862da889609a84f8e10b299b7442d.zip
	16.	git apply -v c8230c48ea24698527cd-3b07a1eb296862da889609a84f8e10b299b7442d/*
	17.	sudo ./configure --prefix=/usr \
				--mandir=/usr/share/man \
				--sysconfdir=/etc \
				--localstatedir=/var \
				--enable-experimental \
				--enable-maintainer-mode
	18.	sudo make
	19.	sudo make install
	20.	sudo sed -i '/^ExecStart.*bluetoothd\s*$/ s/$/ --experimental/' /lib/systemd/system/bluetooth.service



### Tests:
	1.	hciconfig
	2.	sudo hcitool lescan



# Links:
*	[1] https://sourceforge.net/projects/win32diskimager/
*	[2] https://www.raspberrypi.org/downloads/raspbian/
*	[3] https://www.raspberrypi.org/documentation/installation/installing-images/README.md
*	[4] https://www.raspberrypi.org/documentation/installation/installing-images/windows.md
*	[5] http://www.uni-regensburg.de/EDV/Misc/KeyBoards/
*	[6] https://raspberry.tips/raspberrypi-tutorials/raspberry-pi-sd-karte-durch-usb-stick-ersetzen/
*	[7] https://developer.ibm.com/recipes/tutorials/ti-sensor-tag-and-raspberry-pi/
*	[8] https://github.com/ukBaz/python-bluezero/issues/30
*	[9] https://github.com/ukBaz/python-bluezero/blob/master/docs/install_bluez.rst
*	[10] http://askubuntu.com/questions/610291/how-to-install-cmake-3-2-on-ubuntu-14-04
*	[11] https://software.intel.com/en-us/java-for-bluetooth-le-apps
*	[12] http://iotdk.intel.com/docs/master/tinyb/
*	[13] https://github.com/intel-iot-devkit/tinyb
*	[14] https://www.digitalocean.com/community/tutorials/how-to-install-apache-tomcat-8-on-ubuntu-14-04
*	[15] http://stackoverflow.com/questions/4235171/installing-tomcat-7-on-linux-system-with-native-library?rq=1
*	[16] https://people.freedesktop.org/~david/gio-gdbus-codegen-20110412/gdbus-example-gdbus-codegen.html
*	[17] https://developer.gnome.org/gio/stable/gdbus-codegen.html 
*	[18] https://developer.gnome.org/glib/stable/glib-GVariantType.html
