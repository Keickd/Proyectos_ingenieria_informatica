package customPacket;

import jpcap.packet.Packet;

public class CustomPacket {
	
	public boolean direction;
	public Packet packet;
	
	public CustomPacket(Packet packet) {
		this.packet = packet;
		this.direction = true;
	}

}
