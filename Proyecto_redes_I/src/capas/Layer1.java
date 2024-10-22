package capas;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import customPacket.CustomPacket;
import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;
import jpcap.packet.Packet;


public class Layer1 extends Layer {
	JpcapCaptor captor;
	JpcapSender sender;
	int numInterfaz;
	NetworkInterface[]devices;
	
	
	
	public void configuration() throws IOException
	{
		//packets = new ArrayList<CustomPacket>();
		/*packets = new ArrayList<CustomPacket>();*/
		
		devices = JpcapCaptor.getDeviceList();

		//for each network interface
		for (int i = 0; i < devices.length; i++) {
		  //print out its name and description
		  System.out.println(i+": "+devices[i].name + "(" + devices[i].description+")");

		  //print out its datalink name and description
		  System.out.println(" datalink: "+devices[i].datalink_name + "(" + devices[i].datalink_description+")");
		  
		  System.out.print("MAC address: ");
		  for(byte b: devices[i].mac_address) System.out.print(Integer.toHexString(b&0xff)+":");
		  
		  for(NetworkInterfaceAddress a : devices[i].addresses) System.out.println("IP address: " + a.address + " " + a.subnet + " " + a.broadcast);
		  
		} //Select Interface
	
		Scanner sc= new Scanner(System.in);
		System.out.println("Choose an Interface:");
		numInterfaz= sc.nextInt();
		
		//Para abrir la interfaz de red para capturar
		captor = JpcapCaptor.openDevice(devices[numInterfaz], 65535, false, 20);
		sender = JpcapSender.openDevice(devices[numInterfaz]);
	

	}

	@Override
	public void run()
	{
		try {

			//Receive and Send packets (for ten packets)
			while(true){

				//capturar los packets y mandar para arriba
				Packet packet = captor.getPacket();
				
				//Al trabajar con el ArrayList de nuestro layer, nuestros metods sendtoupper
				//y sendtobottom nos permiten directamente trabajar con los arraylistde todos
				//los layers y por eso no creamos uno en cada uno
				if(packet!=null) {
					sendToUpperLayer(new CustomPacket(packet));
					System.out.println("He mandado a capa de arriba");


				}
				
				if(!packets.isEmpty()) {
					
					lock.acquire();
					Packet res=packets.remove(0).packet;
					lock.release();
					
					sender.sendPacket(res);
				
					System.out.println("El paquete enviado a la red es:"+res.toString());
			}

			}

		}catch(Exception e) {
			e.printStackTrace();
		}



	} 
	
	

}
