package org.homi.plugin.ble;
import org.homi.plugin.api.*;
import org.homi.plugin.BLEspec.*;

@PluginID(id = "BLE")
public class BLE extends AbstractPlugin{
	
	private BLEInternal bleInt = BLEInternal.getBLEInternal();

	@Override
	public void setup() {
		
		
		
		CommanderBuilder<BLESpec> cb = new CommanderBuilder<>(BLESpec.class) ;
		
		
		
		Commander<BLESpec> c = cb.onCommandEquals(BLESpec.CONNECT, this::connect).
		onCommandEquals(BLESpec.DISCONNECT, this::disconnect).
		onCommandEquals(BLESpec.PAIR, this::pair).
		onCommandEquals(BLESpec.WRITE, this::write).
		onCommandEquals(BLESpec.READ, this::read).
		build();
		
		addCommander(BLESpec.class, c);
		
		
	}
	
	private boolean connect(Object ...objects) {
		Object o = objects[0] ;
		
			return bleInt.connect((String)o) ;
		
		
	}
	
	private boolean disconnect(Object ...objects) {
		Object o = objects[0] ;
		
		return bleInt.disconnect((String)o) ;
		
		
		
	}
	
	private boolean pair(Object ...objects) {
		Object o = objects[0] ;
		
		return bleInt.pair((String)o) ;
		
		
	}
	
	private boolean write(Object ...objects) {
		String mac = (String)objects[0] ;
		Byte[] config = (Byte[])objects[1];
		String GATTService = (String)(objects[2]);
		String GATTCharacteristic = (String)objects[3];
		
		Connection c = bleInt.connections.get(mac) ;
		try {
			c.write(toPrimitiveByte(config), GATTService, GATTCharacteristic);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
		
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

	@Override
	public void teardown() {
		// TODO Auto-generated method stub
		
	}
	
	static byte[] toPrimitiveByte(Byte[] b) {
		byte[] b2 = new byte[b.length] ;
		
		for(int i = 0; i < b.length; i++) {
			b2[i] = b[i].byteValue();
		}
		
		return b2 ; 
		
	}
	

}
