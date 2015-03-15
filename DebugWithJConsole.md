# About #

This guide explains how you can check what KouChat knows about your network in case of any issues with connecting. That information can be added to bug reports.


# JConsole #

JConsole is a useful tool that comes with the Java SE Development Kit. If you only have the Java SE Runtime Environment then you need to [download](http://java.sun.com/) the JDK first.

In Linux the binary is often added to the path, so just run **jconsole** from a terminal. In Windows you may have to locate the binary yourself:

![http://kouchat.googlecode.com/svn/site/images/jconsole/jconsole_location.png](http://kouchat.googlecode.com/svn/site/images/jconsole/jconsole_location.png)

# Debugging #

Run JConsole like described above, after you have started KouChat. You should see something like this:

![http://kouchat.googlecode.com/svn/site/images/jconsole/jconsole_connect_kouchat.png](http://kouchat.googlecode.com/svn/site/images/jconsole/jconsole_connect_kouchat.png)

Choose **kouchat-0.9.9.jar** and click **Connect**. Navigate here:

![http://kouchat.googlecode.com/svn/site/images/jconsole/jconsole_network_operations.png](http://kouchat.googlecode.com/svn/site/images/jconsole/jconsole_network_operations.png)

Those buttons on the right are the available operations. Now we can check what KouChat knows about your network. On my laptop the current network is cable:

![http://kouchat.googlecode.com/svn/site/images/jconsole/jconsole_current_network.png](http://kouchat.googlecode.com/svn/site/images/jconsole/jconsole_current_network.png)

But KouChat recognizes both cable and wireless as usable networks:

![http://kouchat.googlecode.com/svn/site/images/jconsole/jconsole_usable_networks.png](http://kouchat.googlecode.com/svn/site/images/jconsole/jconsole_usable_networks.png)

The list of all networks would have given me around 30 different virtual network interfaces that are of no interest to KouChat. That's in my Vista. The number will very a lot depending on operating system and installed software.

The information in these boxes can be copied as plain text and saved with the bug reports.