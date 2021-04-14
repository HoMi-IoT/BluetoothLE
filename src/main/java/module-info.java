import testapi.*;

module ble {
	requires testapi;
	requires tinyb;
	requires bleSpec;
	provides IPlugin with org.homi.plugin.ble.BLE;
}