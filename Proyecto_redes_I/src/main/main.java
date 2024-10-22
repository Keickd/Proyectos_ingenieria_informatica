package main;

import java.io.IOException;

import capas.Layer1;
import capas.Layer2;

public class main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//hago un set para enlazar saber donde manda la upper y la bottom
		Layer1 layer1=new Layer1();
		Layer2 layer2=new Layer2();
		layer1.upperLayer=layer2;
		layer2.bottomLayer=layer1;
		
		layer1.configuration();
		
		
		layer1.start();
		layer2.start();
		
		
		
		
	}

}
