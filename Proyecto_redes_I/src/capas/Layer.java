package capas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import customPacket.CustomPacket;

public abstract class Layer extends Thread {
	
	//  L2 // lista de paquetes que voy a tener que gestionar
	// | ^
	// v |
	//  L1
	
	
	//ArrayList<CustomPacket> packets;
	ArrayList<CustomPacket> packets= new ArrayList<CustomPacket>();
	Semaphore lock = new Semaphore(1,true);
	public Layer upperLayer;
	public Layer bottomLayer;
	
	
	public abstract void configuration() throws IOException;
	
	@Override
	public abstract void run();
	
	public void sendToUpperLayer(CustomPacket packetToSend) throws InterruptedException {
		upperLayer.lock.acquire();
		upperLayer.packets.add(packetToSend);
		upperLayer.lock.release();		
	}
	
	public void sendToBottomLayer(CustomPacket packetToSend) throws InterruptedException {
		bottomLayer.lock.acquire();
		bottomLayer.packets.add(packetToSend);
		bottomLayer.lock.release();
		
	}
	
}

	
