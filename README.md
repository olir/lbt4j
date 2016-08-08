lbt4j - Linux Bluetooth Library for Java
========================================

[![Build Status](https://travis-ci.org/olir/lbt4j.png)](https://travis-ci.org/olir/lbt4j/builds)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.serviceflow/lbt4j/badge.png)](https://maven-badges.herokuapp.com/maven-central/de.serviceflow/lbt4j)

# About

lbt4j is a Java API that offers (over a JNI-library) access to [BlueZ](http://www.bluez.org/) over it's DBus Interface Model targeted for Raspberry PI.

It is generated by using [CodegenJ](https://github.com/olir/codegenj), which depends on Gnomes's D-Bus library, documentation and code generator [gdbus-codegen](https://developer.gnome.org/gio/stable/gdbus-codegen.html).

[CodegenJ](https://github.com/olir/codegenj) reads [D-Bus Introspection XML] (https://dbus.freedesktop.org/doc/dbus-specification.html#introspection-format)
 files and generates the API, javadoc and a stuff to build the native library (bundled in native-jar).
 
The xml file is present under src/main/resources. The introspection format is extended by API documenation taken from BlueZ and CodegenJ-specific annotations.

# Alternatives

* [DBus-Java](https://dbus.freedesktop.org/doc/dbus-java/) - generic D-Bus API
* [TinyB](https://github.com/intel-iot-devkit/tinyb) - Great guide and examples, C++ and Java supported.
* [BlueCove](http://bluecove.org/) - a J2SE implementation (J2ME)

Links:
* [JABWT1](http://www.oracle.com/technetwork/articles/javame/index-156193.html) - J2ME article
* [JABWT2](http://www.oracle.com/technetwork/systems/bluetooth2-156149.html) - J2ME article

## Build

### Maven

You can build the jar file and documentation with maven:

> mvn clean package site site:run

Open the project documentation in your web browser on http://localhost:9000 
or open it without site:run under

> target/site/index.html

You can compile the jni library from the target (jni/ and classes/):

> cd target/jni

> make clean all

## Download

Get the API **jar** and **javadoc** here: [MavenCentral](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22lbt4j%22)  
Download the native library **bin** (zip or tar) at [MavenCentral](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22lbt4j-lib%22)  

## HOWTO

This projects builds an Javi API jar to access the D-Bus interfaces of BlueZ.  
It is to be used with the The native library build in
 [lbt4j-lib] (https://github.com/olir/lbt4j-lib).

If you need to setup an Raspberry PI read   [RaspberryPI3_Installation_Walkthrough.md](RaspberryPI3_Installation_Walkthrough.md).

Usage: Invoke your client implementation like this:

> java -cp testclasses:lbt4j-0.3.1.jar -Djava.library.path=jni TestClient

where jni is the folder the native library **libbluezdbus.so** should be placed.

## Example

	import java.io.IOException;
	import java.util.List;
	import java.util.logging.Level;
	import java.util.logging.Logger;
	
	import org.bluez.Adapter1;
	import org.bluez.Device1;
	import org.bluez.GattCharacteristic1;
	import org.bluez.GattService1;
	
	import de.serviceflow.codegenj.ObjectManager;
	
	...
	
	public static void main(String[] args) throws InterruptedException {
		
		ObjectManager m = ObjectManager.getInstance();
		ObjectManager.getLogger().setLevel(Level.FINE); 		
		
		// Show what's on the bus:
		m.dump();
		
		List<Adapter1> adapters = m.getAdapters();
		System.out.println(" ==> # = " + adapters.size());
		
		// Find our bloetooth adapter, and start Discovery ...
		Adapter1 defaultAdapter = null;
		for (Adapter1 a : adapters) {
			System.out.println(" ==> Adapter: " + a.getName());
			try {
				a.startDiscovery();
			} catch (IOException e) {
				System.out.println(" ... ignored.");
			}
			defaultAdapter = a;
		}
		
		// Wait for devices to ne discovered
		Thread.sleep(5000);
		
		for (Adapter1 a : adapters) {
			for (Device1 d : a.getDevices()) {
				if ("CC2650 SensorTag".equals(d.getName())) {
					System.out.println(" ==> Device: " + d.getName());
					try {
						if (!d.getConnected()) {
							d.connect();
							System.out.println(" ... connected.");
						}
						else {
							System.out.println(" ... already connected.");
						}
					} catch (IOException e) {
						System.out.println(" ... ignored: "+e.getMessage());
					}
				}
				else {
					System.out.println(" --> Device " + d.getName()+" skipped.");
				}
			}
		}
		
		// Use the API to traverse through the tree.
		// Use 
		System.out.println("*** Object Tree:");
		for (Adapter1 a : adapters) {
			System.out.println(" ... adapter "+a.getObjectPath()+"  "+a.getName());
			for (Device1 d : a.getDevices()) {
				System.out.println("  .. device "+d.getObjectPath()+"  "+d.getName());
				for (GattService1 s :  d.getServices()) {
					System.out.println("   . service "+s.getObjectPath()+"  "+s.getUUID());
					for (GattCharacteristic1 c :  s.getCharacteristics()) {
						System.out.println("    . char "+c.getObjectPath()+"  "+c.getUUID());
					}
				}
			}
		}
		
		try {
			defaultAdapter.stopDiscovery();
			System.out.println(" ... stopped.");
		} catch (IOException e) {
			System.out.println(" ... ignored.");
		}
		
	}


