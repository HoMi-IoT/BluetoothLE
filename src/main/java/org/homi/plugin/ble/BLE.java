package org.homi.plugin.ble;
import testapi.*;
import org.homi.plugin.spec.*;
public class BLE extends AbstractPlugin{
	
	private BLEInternal bleInt = BLEInternal.getBLEInternal();

	@Override
	public void setup() {
		
		
		
		CommanderBuilder<BLESpec> cb = new CommanderBuilder<>() ;
		
		
		
		Commander<BLESpec> c = cb.onMessageEquals(BLESpec.CONNECT, this::connect).
		onMessageEquals(BLESpec.DISCONNECT, this::disconnect).
		onMessageEquals(BLESpec.PAIR, this::pair).
		onMessageEquals(BLESpec.WRITE, this::write).
		onMessageEquals(BLESpec.READ, this::read).
		build();
		
		addCommander(BLESpec.class, c);
		
		
	}
	
	private Void connect(Object ...objects) {
		Object o = objects[0] ;
		
			bleInt.connect((String)o) ;
		
		return null;
	}
	
	private Void disconnect(Object ...objects) {
		Object o = objects[0] ;
		
			bleInt.disconnect((String)o) ;
		
		return null;
		
	}
	
	private Void pair(Object ...objects) {
		Object o = objects[0] ;
		
			bleInt.pair((String)o) ;
		
		return null;
	}
	
	private Void write(Object ...objects) {
		String mac = (String)objects[0] ;
		byte[] config = (byte[])objects[1];
		String GATTService = (String)(objects[2]);
		String GATTCharacteristic = (String)objects[3];
		
		Connection c = bleInt.connections.get(mac) ;
		try {
			c.write(config, GATTService, GATTCharacteristic);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	private byte[] read(Object ...objects) {
		String mac = (String)objects[0] ;
		//byte[] config = (byte[])objects[1];
		String GATTService = (String)(objects[2]);
		String GATTCharacteristic = (String)objects[3];
		
		Connection c = bleInt.connections.get(mac) ;
		byte[] b = null;
		try {
			b = c.read(GATTService, GATTCharacteristic);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return b;
		
	}
	

}
