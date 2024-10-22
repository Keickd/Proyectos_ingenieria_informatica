import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;


public class Interfaz 
{
	JFrame frame;
	JToolBar barraHerramientas;
	JMenuBar menuBar;
	JMenu menuDesplegableSeleccionarTamanios;
	JMenu menuDesplegableOtrasFuncionalidades;
	JMenu menuDesplegableOpcionesDeMostrado;
	JMenu menuDesplegableSeleccionarExtension;
	JMenuItem itemMuestraTamaniosEnPorcentaje;
	JMenuItem itemMuestraTamaniosEnValorAbsoluto;
	JMenuItem itemMuestraElementosSuperioresAUnPorcentaje;
	JMenuItem itemMuestraElementosSuperioresAUnValorAbsoluto;
	JMenuItem itemMuetraSoloCarpetas;
	JMenuItem itemMuestraExtensionExe;
	JButton botonDeDiscos;
	JButton botonAniadirAlFichero;
	JTable tabla;
	JPanel main;
	JLabel etiquetaSeleccionarFichero;
	JLabel etiquetaMensajeIntroductorio;
	JTable miTabla;
	JScrollPane miPanelScroll;
	File ficheroConDatos;
	FileWriter writer;
	BufferedWriter bufferedWriter;
	PrintWriter miPrintWriter;

	
	Interfaz()
	{
		inicializarElementos();
		aniadirElementos();
		BloquearMenus();
		AniadirListeners();
		
		
		frame.setSize(1280, 720);
		frame.setVisible(true);
	}
	
	void inicializarElementos()
	{	
		frame = new JFrame("Gestor de archivos");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main = new JPanel();
		
		menuBar = new JMenuBar();
		barraHerramientas = new JToolBar();
		botonDeDiscos = new JButton("Selecciona el disco");
		botonAniadirAlFichero = new JButton("Aniadir al fichero");
		
		menuDesplegableSeleccionarTamanios = new JMenu("Seleccionar formato de tamanio");
		menuDesplegableOtrasFuncionalidades = new JMenu("Otras funcionalidades");
		menuDesplegableOpcionesDeMostrado = new JMenu("Opciones de mostrado");
		menuDesplegableSeleccionarExtension = new JMenu("Seleccionar una extension");
		
		itemMuestraTamaniosEnPorcentaje = new JMenuItem("Mostrar tamanio en porcentaje (%)");
		itemMuestraTamaniosEnValorAbsoluto = new JMenuItem("Mostrar tamanio en valor absoluto (abs)");
		itemMuestraElementosSuperioresAUnPorcentaje = new JMenuItem("Mostrar elementos superiores al porcentaje citado por el usuario");
		itemMuestraElementosSuperioresAUnValorAbsoluto = new JMenuItem("Mostrar elementos superiores al valor absoluto citado por el usuario");
		itemMuetraSoloCarpetas = new JMenuItem("Mostrar solo carpetas");
		itemMuestraExtensionExe = new JMenuItem("Mostrar elementos con una determinada extension");
		
		etiquetaSeleccionarFichero = new JLabel("  Selecciona un fichero");
		etiquetaMensajeIntroductorio = new JLabel(" ATENCION : Los Menus estas dishabilitados, para activarlos selecciona un fichero en la barra de herramientas de abajo izquierda");
		
		ficheroConDatos = new File("Fichero_con_datos.txt");
		try {
			writer = new FileWriter(ficheroConDatos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bufferedWriter = new BufferedWriter(writer);
		miPrintWriter = new PrintWriter(bufferedWriter); 
	}
	void aniadirElementos()
	{
		frame.setJMenuBar(menuBar);
		frame.add(barraHerramientas,BorderLayout.SOUTH);
		frame.add(main, BorderLayout.CENTER);
		
		barraHerramientas.add(botonDeDiscos);
		barraHerramientas.add(botonAniadirAlFichero);
		barraHerramientas.add(etiquetaSeleccionarFichero);
		botonDeDiscos.setBackground(Color.green);
		botonDeDiscos.setToolTipText("Pulsa aqui para seleccionar la ruta que desees");
		menuBar.add(menuDesplegableSeleccionarTamanios);
		menuBar.add(menuDesplegableOtrasFuncionalidades);
		menuBar.add(menuDesplegableOpcionesDeMostrado);
		menuBar.add(etiquetaMensajeIntroductorio);
		
		menuDesplegableSeleccionarTamanios.add(itemMuestraTamaniosEnPorcentaje);
		menuDesplegableSeleccionarTamanios.add(itemMuestraTamaniosEnValorAbsoluto);
		menuDesplegableSeleccionarTamanios.setToolTipText("Aqui puedes elegir mostrar las unidades en % o en valor absoluto");
		
		menuDesplegableOtrasFuncionalidades.add(itemMuestraElementosSuperioresAUnPorcentaje);
		menuDesplegableOtrasFuncionalidades.add(itemMuestraElementosSuperioresAUnValorAbsoluto);
		menuDesplegableOtrasFuncionalidades.setToolTipText("El usuario debera introducir un valor para el correcto funcionamiento de la aplicacion");
		
		menuDesplegableOpcionesDeMostrado.add(itemMuetraSoloCarpetas);
		menuDesplegableOpcionesDeMostrado.add(menuDesplegableSeleccionarExtension);
		menuDesplegableOpcionesDeMostrado.setToolTipText("Podras elegir entre mostrar carpetas u elementos de una determinada extension");
	
		menuDesplegableSeleccionarExtension.add(itemMuestraExtensionExe);
	}
	void BloquearMenus()
	{
		menuDesplegableSeleccionarTamanios.setEnabled(false);
		menuDesplegableOtrasFuncionalidades.setEnabled(false);
		menuDesplegableOpcionesDeMostrado.setEnabled(false);
	}
	void DesbloquearMenus()
	{
		menuDesplegableSeleccionarTamanios.setEnabled(true);
		menuDesplegableOtrasFuncionalidades.setEnabled(true);
		menuDesplegableOpcionesDeMostrado.setEnabled(true);
	}
	
	void AniadirListeners() {
		Listeners listeners = new Listeners();
		listeners.AniadirSeleccionDeFicheroListener();
		
		
	}
	
	class Listeners 
	{
		JFileChooser miFileChooser = new JFileChooser("C:\\");	//HABRIA QUE CAMBIAR LA RUTA POR SI ACA PERO NO SE
		File fich;
		File []miVectorFicheros;
		int seleccion;
		char caracterDecisivoAlCrearLosVectoresDeLaTabla;
		String ruta;
		String respuesta;
		int dis =0,dis1 = 0, dis2 = 0,dis3 = 0;
		
		

		public void AniadirSeleccionDeFicheroListener()
		{
			botonDeDiscos.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					miFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					
					seleccion = miFileChooser.showOpenDialog(frame);
					
					if(seleccion == miFileChooser.APPROVE_OPTION)
					{
						
						fich = new File(miFileChooser.getSelectedFile().getPath());
						miVectorFicheros = fich.listFiles();
						ruta = miFileChooser.getSelectedFile().getPath();
						etiquetaMensajeIntroductorio.setVisible(false);
						DesbloquearMenus();
						etiquetaSeleccionarFichero.setText(" "+ruta);
						
						
							itemMuestraTamaniosEnPorcentaje.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
									
										
										GeneradorVectorFicheros miGeneradorVectorFicheros = new GeneradorVectorFicheros();
										caracterDecisivoAlCrearLosVectoresDeLaTabla = '%';
										
									if(dis == 0)
									{
										miGeneradorVectorFicheros.ConstruirTabla();
									}
									dis++;
									dis=0;
									dis1 = 0;
									dis2 = 0;	
									dis3 = 0;
								}
							});
							
							itemMuestraTamaniosEnValorAbsoluto.addActionListener(new ActionListener() 
							{
								
								@Override
								public void actionPerformed(ActionEvent arg0) {
									caracterDecisivoAlCrearLosVectoresDeLaTabla = 'a';
									
									GeneradorVectorFicheros miGeneradorVectorFicheros = new GeneradorVectorFicheros();
									if(dis == 0)
									{
										miGeneradorVectorFicheros.ConstruirTabla();
									}
									dis++;
									dis=0;
									dis1 = 0;
									dis2 = 0;	
									dis3 = 0;
								}
							});
							
							itemMuetraSoloCarpetas.addActionListener(new ActionListener() 
							{
								int i = 0;
								@Override
								public void actionPerformed(ActionEvent arg0) {
									GeneradorVectorFicheros miGeneradorVectorFicheros = new GeneradorVectorFicheros();
									caracterDecisivoAlCrearLosVectoresDeLaTabla = 'c';
								
									
									for(i = 0;i < miGeneradorVectorFicheros.miVectorArrays.size();i++)
									{
										if(miGeneradorVectorFicheros.miVectorArrays.get(i).miFichero.isDirectory())
										{
											miGeneradorVectorFicheros.miArrayListFiltraCarpetas.add(new Fichero(miGeneradorVectorFicheros.miVectorArrays.get(i).ruta,miGeneradorVectorFicheros.miVectorArrays.get(i).tamanioPorcentaje,miGeneradorVectorFicheros.miVectorArrays.get(i).tamanioValorAbsoluto));
										}
									}
							
									if(dis == 0)
									{
										miGeneradorVectorFicheros.ConstruirTabla();
									}
									dis++;
									dis=0;
									
									dis1 = 0;
									dis2 = 0;	
									dis3 = 0;
									
								}
							});
							dis3 = 0;
							itemMuestraExtensionExe.addActionListener(new ActionListener() 
							{
								int i = 0;
								String respuesta;
								@Override
								public void actionPerformed(ActionEvent arg0) {
									caracterDecisivoAlCrearLosVectoresDeLaTabla = 'e';
									
									if(dis3 == 0)
									{
										respuesta = JOptionPane.showInputDialog("Introduce una extension");
									}
									GeneradorVectorFicheros miGeneradorVectorFicheros = new GeneradorVectorFicheros();
									
									for(i = 0;i < miGeneradorVectorFicheros.miVectorArrays.size();i++)
									{	try
										{
											if(miGeneradorVectorFicheros.miVectorArrays.get(i).ruta.endsWith(respuesta))
											{
												miGeneradorVectorFicheros.miArrayListDeExtensiones.add(new Fichero(miGeneradorVectorFicheros.miVectorArrays.get(i).ruta,miGeneradorVectorFicheros.miVectorArrays.get(i).tamanioPorcentaje,miGeneradorVectorFicheros.miVectorArrays.get(i).tamanioValorAbsoluto));
											}
										}catch(Exception e)
										{
												
										}
										
									}
									
									if(dis3 == 0)
									{
										miGeneradorVectorFicheros.ConstruirTabla();
									}
								
									dis1 = 0;
									dis2 = 0;	
									dis3++;
								
								}
								
							});
							
							itemMuestraElementosSuperioresAUnPorcentaje.addActionListener(new ActionListener() {
								int i = 0;
								
								@Override
								public void actionPerformed(ActionEvent e) {
									// TODO Auto-generated method stub
									caracterDecisivoAlCrearLosVectoresDeLaTabla = 'k';
									if(dis1 == 0)
									{
										respuesta = JOptionPane.showInputDialog("Introduce un valor : ");
									
									}
									
									GeneradorVectorFicheros miGeneradorVectorFicheros = new GeneradorVectorFicheros();
									
									for(i = 0;i < miGeneradorVectorFicheros.miVectorArrays.size();i++)
									{
										try
										{
											if(Integer.parseInt(respuesta) < miGeneradorVectorFicheros.miVectorArrays.get(i).tamanioPorcentaje)
											{
												miGeneradorVectorFicheros.miArrayListFiltradaTamP.add(new Fichero(miGeneradorVectorFicheros.miVectorArrays.get(i).ruta,miGeneradorVectorFicheros.miVectorArrays.get(i).tamanioPorcentaje,miGeneradorVectorFicheros.miVectorArrays.get(i).tamanioValorAbsoluto));
											}
										}
										catch(java.lang.NumberFormatException excepcionn)
										{
											JOptionPane.showMessageDialog(null,"No has introducido un numero, el programa se cerrará incorrectamente.");
										
											System.exit(0);
										}
										
									}
									
									if(dis1 == 0)
									{
										miGeneradorVectorFicheros.ConstruirTabla();
									}
									dis1++;
									dis2 = 0;
									dis3 = 0;
								}
							});
							itemMuestraElementosSuperioresAUnValorAbsoluto.addActionListener(new ActionListener() {
								int i = 0;
								
								@Override
								public void actionPerformed(ActionEvent e) {
									// TODO Auto-generated method stub
									
									caracterDecisivoAlCrearLosVectoresDeLaTabla = 'j';
									if(dis2 == 0)
									{
										respuesta = JOptionPane.showInputDialog("Introduce un valor : ");
								
									}
									
																		
									GeneradorVectorFicheros miGeneradorVectorFicheros = new GeneradorVectorFicheros();
									
									for(i = 0;i < miGeneradorVectorFicheros.miVectorArrays.size();i++)
									{	try
										{
											if(Integer.parseInt(respuesta) < miGeneradorVectorFicheros.miVectorArrays.get(i).tamanioValorAbsoluto)
											{
												miGeneradorVectorFicheros.miArrayListFiltradaTamAbs.add(new Fichero(miGeneradorVectorFicheros.miVectorArrays.get(i).ruta,miGeneradorVectorFicheros.miVectorArrays.get(i).tamanioPorcentaje,miGeneradorVectorFicheros.miVectorArrays.get(i).tamanioValorAbsoluto));
											}	
										}
										catch(java.lang.NumberFormatException excepcion) {
											JOptionPane.showMessageDialog(null,"No has introducido un numero, el programa se cerrará incorrectamente.");
											System.exit(0);
										}
										
									}
									
									if(dis2 == 0)
									{
										miGeneradorVectorFicheros.ConstruirTabla();
									}
									dis2++;
									dis1 = 0;
									dis3 = 0;
								}
							});
							dis = 0;
							
							botonAniadirAlFichero.addActionListener(new ActionListener() {
								int i = 0;
								GeneradorVectorFicheros miGeneradorVectorFicheros = new GeneradorVectorFicheros();
								@Override
								public void actionPerformed(ActionEvent e) {
									
									// TODO Auto-generated method stub
									if(caracterDecisivoAlCrearLosVectoresDeLaTabla == '%' || caracterDecisivoAlCrearLosVectoresDeLaTabla == 'a' )
									{
										for(i = 0;i < miGeneradorVectorFicheros.miVectorArrays.size();i++)
										{
											miPrintWriter.append(miGeneradorVectorFicheros.miVectorArrays.get(i).ruta+"\n");
											miPrintWriter.append(miGeneradorVectorFicheros.miVectorArrays.get(i).tamanioPorcentaje+" %\n");
											miPrintWriter.append(+miGeneradorVectorFicheros.miVectorArrays.get(i).tamanioValorAbsoluto+" Bytes\n");
										}
										miPrintWriter.append("\n");
									}else if(caracterDecisivoAlCrearLosVectoresDeLaTabla == 'c')
									{
										for(i = 0;i < miGeneradorVectorFicheros.miArrayListFiltraCarpetas.size();i++)
										{
											miPrintWriter.append(miGeneradorVectorFicheros.miArrayListFiltraCarpetas.get(i).ruta+"\n");
											miPrintWriter.append(miGeneradorVectorFicheros.miArrayListFiltraCarpetas.get(i).tamanioPorcentaje+" %\n");
											miPrintWriter.append(+miGeneradorVectorFicheros.miArrayListFiltraCarpetas.get(i).tamanioValorAbsoluto+" Bytes\n");
										}
										miPrintWriter.append("\n");
									}else if(caracterDecisivoAlCrearLosVectoresDeLaTabla == 'k')
									{
										for(i = 0;i < miGeneradorVectorFicheros.miArrayListFiltradaTamP.size();i++)
										{
											miPrintWriter.append(miGeneradorVectorFicheros.miArrayListFiltradaTamP.get(i).ruta+"\n");
											miPrintWriter.append(miGeneradorVectorFicheros.miArrayListFiltradaTamP.get(i).tamanioPorcentaje+" %\n");
											miPrintWriter.append(+miGeneradorVectorFicheros.miArrayListFiltradaTamP.get(i).tamanioValorAbsoluto+" Bytes\n");
										}
										miPrintWriter.append("\n");
									}else if(caracterDecisivoAlCrearLosVectoresDeLaTabla == 'j')
									{
										for(i = 0;i < miGeneradorVectorFicheros.miArrayListFiltradaTamAbs.size();i++)
										{
											miPrintWriter.append(miGeneradorVectorFicheros.miArrayListFiltradaTamAbs.get(i).ruta+"\n");
											miPrintWriter.append(miGeneradorVectorFicheros.miArrayListFiltradaTamAbs.get(i).tamanioPorcentaje+" %\n");
											miPrintWriter.append(+miGeneradorVectorFicheros.miArrayListFiltradaTamAbs.get(i).tamanioValorAbsoluto+" Bytes\n");
										}
										miPrintWriter.append("\n");
									}else if(caracterDecisivoAlCrearLosVectoresDeLaTabla == 'e')
									{
										for(i = 0;i < miGeneradorVectorFicheros.miArrayListDeExtensiones.size();i++)
										{
											miPrintWriter.append(miGeneradorVectorFicheros.miArrayListDeExtensiones.get(i).ruta+"\n");
											miPrintWriter.append(miGeneradorVectorFicheros.miArrayListDeExtensiones.get(i).tamanioPorcentaje+" %\n");
											miPrintWriter.append(+miGeneradorVectorFicheros.miArrayListDeExtensiones.get(i).tamanioValorAbsoluto+" Bytes\n");
										}
										miPrintWriter.append("\n");
									}
									miPrintWriter.close();
								}
							});
					}
					
					
				}
				
			});
			
		}
		
		
		
		class GeneradorVectorFicheros 
		{
			int i,j;
			float porcentaje,suma;
			
			
			ArrayList <Fichero>miVectorArrays = new ArrayList<Fichero>();
			ArrayList <Fichero>miArrayListFiltraCarpetas = new ArrayList<Fichero>();
			ArrayList<Fichero> miArrayListFiltradaTamP = new ArrayList<Fichero>();
			ArrayList<Fichero> miArrayListFiltradaTamAbs = new ArrayList<Fichero>();
			ArrayList<Fichero> miArrayListDeExtensiones = new ArrayList<Fichero>();
			
			GeneradorVectorFicheros() 
			{
				miVectorArrays.clear();
				
				for(i = 0;i < miVectorFicheros.length;i++)
				{	
					conseguirPorcentajesDeCadaFichero();
					miVectorArrays.add(new Fichero(miVectorFicheros[i].getPath(),porcentaje,miVectorFicheros[i].length()));
					
					porcentaje = 0;
				}	
	
					main.doLayout();		
			}
		
			
			void conseguirPorcentajesDeCadaFichero()
			{
				for(j = 0;j < miVectorFicheros.length;j++)
				{
					suma += miVectorFicheros[j].length();
				}
				porcentaje = Math.round((miVectorFicheros[i].length() * 100) / suma);
				suma = 0;
			}		
		
			class ModeloDeDatos
			{
				public String[] getColumnNames()
				{	if(caracterDecisivoAlCrearLosVectoresDeLaTabla == 'c' || caracterDecisivoAlCrearLosVectoresDeLaTabla == 'e')
					{
						return new String[] {"Ruta"};
					}
					return new String[] {"Ruta", "Tamanio", "Estado"};
				}
				public Object[][] getData() {
					Object[][] datos,datosfc,datosfp,datosfabs,datosfe;
					
					datos = new Object[miVectorArrays.size()][3];
					datosfc = new Object[miArrayListFiltraCarpetas.size()][1];
					datosfp = new Object[miArrayListFiltradaTamP.size()][3];
					datosfabs = new Object[miArrayListFiltradaTamAbs.size()][3];
					datosfe = new Object[miArrayListDeExtensiones.size()][1];
					
				
					if(caracterDecisivoAlCrearLosVectoresDeLaTabla == '%')
					{
						for(i = 0;i < miVectorArrays.size();i++)
						{
							datos[i][0] = miVectorArrays.get(i).ruta;
							datos[i][1] = miVectorArrays.get(i).tamanioPorcentaje+" %";
							datos[i][2] = miVectorArrays.get(i).barraDeProgreso;
						}
					}else if(caracterDecisivoAlCrearLosVectoresDeLaTabla == 'a')
					{
						for(i = 0;i < miVectorArrays.size();i++)
						{
							datos[i][0] = miVectorArrays.get(i).ruta;
							datos[i][1] = miVectorArrays.get(i).tamanioValorAbsoluto+" Bytes";
							datos[i][2] = miVectorArrays.get(i).barraDeProgreso;
						}
					}else if(caracterDecisivoAlCrearLosVectoresDeLaTabla == 'c')
					{
						
						for(i = 0;i < miArrayListFiltraCarpetas.size();i++)
						{
							datosfc[i][0] = miArrayListFiltraCarpetas.get(i).ruta;
						}
						
						return datosfc;
					}else if(caracterDecisivoAlCrearLosVectoresDeLaTabla == 'j')
					{
						for(i = 0;i < miArrayListFiltradaTamAbs.size();i++)
						{
							datosfabs[i][0] = miArrayListFiltradaTamAbs.get(i).ruta;
							datosfabs[i][1] = miArrayListFiltradaTamAbs.get(i).tamanioValorAbsoluto+" Bytes";
							datosfabs[i][2] = miArrayListFiltradaTamAbs.get(i).barraDeProgreso;
						}
						
						return datosfabs;
					}else if(caracterDecisivoAlCrearLosVectoresDeLaTabla == 'k')
					{
						for(i = 0;i < miArrayListFiltradaTamP.size();i++)
						{
							datosfp[i][0] = miArrayListFiltradaTamP.get(i).ruta;
							datosfp[i][1] = miArrayListFiltradaTamP.get(i).tamanioPorcentaje+" %";
							datosfp[i][2] = miArrayListFiltradaTamP.get(i).barraDeProgreso;
							
						}
						
						return datosfp;
					}else if(caracterDecisivoAlCrearLosVectoresDeLaTabla == 'e')
					{
				
						for(i = 0;i < miArrayListDeExtensiones.size();i++)
						{
							datosfe[i][0] = miArrayListDeExtensiones.get(i).ruta;	
						}
						
						return datosfe;
					}
					
					return datos;
					
				}
			
			}
			void ConstruirTabla()
			{
				ModeloDeDatos dataSource = new ModeloDeDatos();
				
				
				if(miTabla != null)
				{
					main.remove(miPanelScroll);
				}
				
					

				miTabla = new JTable(dataSource.getData(),dataSource.getColumnNames()){
					 public boolean isCellEditable(int row, int column) {  
					     return false;               
					 };
				};
				
				if(caracterDecisivoAlCrearLosVectoresDeLaTabla != 'c' && caracterDecisivoAlCrearLosVectoresDeLaTabla != 'e')
				{
					TableColumnModel columnModel = miTabla.getColumnModel();
					columnModel.getColumn(2).setPreferredWidth(250);
				}
				
				miTabla.setRowHeight(40);
				miTabla.getTableHeader().setReorderingAllowed(false);	
				miTabla.setDefaultRenderer(Object.class, new Render());
				
				miPanelScroll = new JScrollPane(miTabla);
				
				main.add(miPanelScroll);

				main.doLayout();
			}
		}
	}
	class Render extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			
			if(value instanceof JProgressBar)
			{
				JProgressBar barraP1 = (JProgressBar)value;
				return barraP1;
			}
			
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}
	
}
