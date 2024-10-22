import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JProgressBar;

public class Fichero 
{
	String ruta;
	File miFichero;
	float tamanioPorcentaje;
	float tamanioValorAbsoluto;
	JProgressBar barraDeProgreso;
	Random miNumeroRandom;
	
	Fichero(String r, float tamP, float tamVAbs)
	{
		ruta = r;
		miFichero = new File(ruta);
		tamanioPorcentaje = tamP;
		tamanioValorAbsoluto = tamVAbs;
		miNumeroRandom = new Random();
		barraDeProgreso = new JProgressBar(0,100);
		barraDeProgreso.setValue((int)tamanioPorcentaje);
		barraDeProgreso.setForeground(new Color(miNumeroRandom.nextInt(255), miNumeroRandom.nextInt(255), miNumeroRandom.nextInt(255)));
		barraDeProgreso.setBackground(Color.WHITE);//Queda mejor asi
	}
	
	public String toString()
	{
		return ruta+" "+tamanioPorcentaje+" % "+tamanioValorAbsoluto+" Bytes";
		
	}
	
}