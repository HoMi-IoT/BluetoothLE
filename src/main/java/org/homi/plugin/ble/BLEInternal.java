package org.homi.plugin.ble;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.homi.plugin.ble.Connection;

import tinyb.* ;
class BLEInternal {
	ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
	//static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	static boolean discoveryStarted = false;
	static BLEInternal inst = null ;
	
	private BLEInternal() {
		BluetoothManager manager = BluetoothManager.getBluetoothManager();
		try {
			manager.startDiscovery();
			discoveryStarted = true;
		}
		catch(BluetoothException be) {
			be.printStackTrace();
		}
	}
	
	synchronized static BLEInternal getBLEInternal() {
		if (inst == null) {
			return new BLEInternal();
		}
		else {
			return inst ;
		}
	}
	

	
	
	synchronized boolean pair(String macAddr) {
		if(!connections.contains(macAddr)) {
			BluetoothManager manager = BluetoothManager.getBluetoothManager();
		
			if(!discoveryStarted) {
				try {
					manager.startDiscovery();
					discoveryStarted = true;
				}
				catch(BluetoothException be) {
					be.printStackTrace();
					return false;
					
				}
			}
			else {
				try {
					BluetoothDevice d = getDevice((String)macAddr);
					if(d.pair()) {
						Connection c = new Connection(d);
						c.setState(Connection.STATE.PAIRED);
						connections.putIfAbsent((String)macAddr, c);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
					
				}
				
			}
		}
		return true;
	}

	
	
	synchronized boolean connect(String macAddr) {
		if(!connections.contains(macAddr)) {
			BluetoothManager manager = BluetoothManager.getBluetoothManager();
			if(!discoveryStarted) {
				try {
					manager.startDiscovery();
					discoveryStarted = true;
				}
				catch(BluetoothException be) {
					be.printStackTrace();
					return false;
				
				}
			}
			else {
				try {
					BluetoothDevice d = getDevice((String)macAddr);
					if(d.connect()) {
						Connection c = new Connection(d);
						c.setState(Connection.STATE.CONNECTED);
						connections.putIfAbsent((String)macAddr, c);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				
				}
			}
		
		}
			return true;
	}
	
	synchronized boolean disconnect(String macAddr) {
		if(connections.contains(macAddr)) {
			BluetoothManager manager = BluetoothManager.getBluetoothManager();
			if(!discoveryStarted) {
				try {
					manager.startDiscovery();
					discoveryStarted = true;
				}
				catch(BluetoothException be) {
					be.printStackTrace();
					return false;
				
				}
			}
			else {
				try {
					BluetoothDevice d = getDevice((String)macAddr);
					if(d.disconnect()) {
						connections.remove((String)macAddr);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				
				}
			}
		
		}
		return true;
	}
	
	
	private BluetoothDevice getDevice(String address) throws InterruptedException {
		BluetoothManager manager = BluetoothManager.getBluetoothManager();
		BluetoothDevice d = null;
		for (int i = 0; (i < 15); ++i) {
			List<BluetoothDevice> list = manager.getDevices();
			if (list == null)
				return null;

			for (BluetoothDevice device : list) {
				//printDevice(device);
				/*
				 * Here we check if the address matches.
				 */
				if (device.getAddress().equals(address))
					d = device;
			}

			if (d != null) {
				return d;
			}
			//Thread.sleep(4000);
		}
		return null;
	}
	
	static BluetoothGattService getService(BluetoothDevice device, String UUID) throws InterruptedException {
		//System.out.println("Services exposed by device:");
		BluetoothGattService tempService = null;
		List<BluetoothGattService> bluetoothServices = null;
		do {
			bluetoothServices = device.getServices();
			if (bluetoothServices == null)
				return null;

			for (BluetoothGattService service : bluetoothServices) {
				//System.out.println("UUID: " + service.getUUID());
				if (service.getUUID().equals(UUID))
					tempService = service;
			}
			//Thread.sleep(4000);
		} while (bluetoothServices.isEmpty());
		return tempService;
	}

	static BluetoothGattCharacteristic getCharacteristic(BluetoothGattService service, String UUID) {
		List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
		if (characteristics == null)
			return null;

		for (BluetoothGattCharacteristic characteristic : characteristics) {
			if (characteristic.getUUID().equals(UUID))
				return characteristic;
		}
		return null;
	}
	
	
}
