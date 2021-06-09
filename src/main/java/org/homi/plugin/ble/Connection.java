package org.homi.plugin.ble;
import org.homi.plugin.api.observer.IObserver;
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

	synchronized boolean write(byte[] config, String GATTService, String GATTCharacteristic) throws InterruptedException {
		
		if(!BLEInternal.discoveryStarted) {
			BluetoothManager manager = BluetoothManager.getBluetoothManager();
			try {
				manager.startDiscovery();
				BLEInternal.discoveryStarted = true;
			} catch(BluetoothException be) {
				return false;
			}
		}
		
		BluetoothGattService tempService = BLEInternal.getService(this.device, (String)GATTService);

		if (tempService == null) {

			return false;
		}
		//System.out.println("Found service " + tempService.getUUID());

		BluetoothGattCharacteristic gattChar = BLEInternal.getCharacteristic(tempService, (String)GATTCharacteristic);
		
		if (gattChar == null) {
			//System.err.println("Could not find the correct characteristics.");
			
			return false;
		}
	try {
		gattChar.writeValue(config) ;
	}
	catch(BluetoothException be) {
		return false;
	}
		
		//d.disconnect();
		
		return true ;
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

	synchronized void listen(String GATTService, String GATTCharacteristic, IObserver io) throws InterruptedException {
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
		gattChar.enableValueNotifications((b)->{
			io.update(b);
		});
		
	}
}
