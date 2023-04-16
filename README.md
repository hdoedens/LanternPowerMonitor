# LanternPowerMonitor - Local
This Lantern Power Monitor fork is a Raspberry Pi service and Java Web Service, that allow you to monitor every electrical breaker in your house, regardless of how many panels or breakers you have. It can be managed using a web interface and it only sends the data to a local MQTT broker (for example Mosquitto MQTT in Home Assistant). 
<br><br>
The official website has a lot of technical information:
<br>
[LanternPowerMonitor.com](https://lanternpowermonitor.com)
<br><br>
Here's an imgur album showing what this is and how it works:
<br>
[Lantern Power Monitor - Imgur](https://imgur.com/gallery/SPOJYBR)
<br><br>
The android application won't work with this version. Configuring the hub is done through a web UI.
<br><br>
The iOS application also won't work with this version. Configuring the hub is done through a web UI.
<br>

## Build
To build an executable jar file just clone this repo and run `mvn package`. The jar you need is in the `currentmonitor/lantern-currentmonitor/target` folder.

## Installation
Flash an SD card with the following [image](https://cf.lanternpowermonitor.com/hub_1.1.1.zip) using [Balena Etcher](https://www.balena.io/etcher/).

Power up the Pi and copy the latest release of the LanternPowerMonitor 'local' jar to the Pi. Do not restart the CurrentMonitor service yet, unless you have the correct breaker_config.json on the Pi:

```scp currentmonitor/lantern-currentmonitor/target/lantern-currentmonitor.jar pi@<ip of your rpi>:/opt/currentmonitor```

Restart the currentmonitor service from the rpi commandline `systemctl restart currentmonitor` or use the [lantern-config](https://github.com/hdoedens/lantern-config) UI to restart the service from a webbrowser.
