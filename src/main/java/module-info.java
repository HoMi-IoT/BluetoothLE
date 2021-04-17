

module ble {
	requires org.homi.plugin.api;
	requires tinyb;
	requires bleSpec;
	provides org.homi.plugin.api.IPlugin with org.homi.plugin.ble.BLE;
}