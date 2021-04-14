package org.homi.plugin.ble;
import org.homi.plugin.ble.BLEInternal;


import tinyb.* ;
class Connection {
	enum STATE{PAIRED, CONNECTED, DISCONNECTED};
	private STATE state = STATE.DISCONNECTED;
	private BluetoothDevice device = null;
	
	Connection(BluetoothDevice d){
		this.device = d;
	}
	
	void setState(Connection.STATE s){
		this.state = s;
	}
	
	STATE getState() {
		return this.state;
	}

	synchronized Object write(byte[] config, String GATTService, String GATTCharacteristic) throws InterruptedException {
		
		if(!BLEInternal.discoveryStarted) {
			BluetoothManager manager = BluetoothManager.getBluetoothManager();
			try {
				manager.startDiscovery();
				BLEInternal.discoveryStarted = true;
			} catch(BluetoothException be) {
				return -1;
			}
		}
		
		BluetoothGattService tempService = BLEInternal.getService(this.device, (String)GATTService);

		if (tempService == null) {

			return -1;
		}
		//System.out.println("Found service " + tempService.getUUID());

		BluetoothGattCharacteristic gattChar = BLEInternal.getCharacteristic(tempService, (String)GATTCharacteristic);
		
		if (gattChar == null) {
			//System.err.println("Could not find the correct characteristics.");
			
			return -1;
		}
	
		gattChar.writeValue(config) ;
		
		//d.disconnect();
		
		return 1 ;
	}

	synchronized byte[] read(String GATTService, String GATTCharacteristic) throws InterruptedException {
		if(!BLEInternal.discoveryStarted) {
			BluetoothManager manager = BluetoothManager.getBluetoothManager();
			try {
				manager.startDiscovery();
				BLEInternal.discoveryStarted = true;
			} catch(BluetoothException be) {
				
			}
		}
		
		BluetoothGattService tempService = BLEInternal.getService(this.device, (String)GATTService);

		/*
		 * if (tempService == null) {
		 * 
		 * return -1; }
		 */
		//System.out.println("Found service " + tempService.getUUID());

		BluetoothGattCharacteristic gattChar = BLEInternal.getCharacteristic(tempService, (String)GATTCharacteristic);
		
		/*
		 * if (gattChar == null) {
		 * //System.err.println("Could not find the correct characteristics.");
		 * 
		 * return -1; }
		 */
	
		byte[] conf = gattChar.readValue();
		
		//d.disconnect();
		
		return conf ;
	}
}
