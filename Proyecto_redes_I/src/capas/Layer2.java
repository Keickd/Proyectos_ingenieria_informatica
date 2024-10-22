package capas;

import java.io.IOException;

import customPacket.CustomPacket;
import jpcap.JpcapSender;
import jpcap.packet.EthernetPacket;
import jpcap.packet.Packet;

public class Layer2 extends Layer{

	JpcapSender sender;
	@Override
	public void configuration() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run(){
		// TODO Auto-generated method stub
		
		
		try {

			//Receive and Send packets (for ten packets)
			while(true){
				
				System.out.println("test capa 2");
				
				if(!packets.isEmpty()) {
					
					lock.acquire();
					Packet Elementoaborrar=packets.remove(0).packet;
					lock.release();
					
					System.out.println("Elemento " + Elementoaborrar);
					if(Elementoaborrar!=null) {
						//System.out.println(Elementoaborrar.toString());
						EthernetPacket ep=(EthernetPacket)Elementoaborrar.datalink;
						
						ep.dst_mac = new byte [] { (byte) 0xff,(byte) 0xff,(byte) 0xff,(byte) 0xff,
								(byte) 0xff,(byte) 0xff
				               };; // XX:XX:XX:XX:XX:XX Address to send this packet to all the devices in my network (Broadcast)
						
						ep.src_mac = new byte [] { (byte) 0xE4,(byte) 0x42,(byte) 0xA6,(byte) 0x25,
								(byte) 0x00,(byte) 0x49
				        };; // XX:XX:XX:XX:XX:XX My address (I’m the gossip)
						Elementoaborrar.datalink = ep; // p is the new packet to send
						
						
						
						//Mandamos hacia abajo
						 sendToBottomLayer(new CustomPacket(Elementoaborrar));
						 System.out.println("He mandado a capa de abajo");
						 //sender.sendPacket(Elementoaborrar);
					}
				}
				
					
				



			}

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
