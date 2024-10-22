/*
 * Aparcamiento.cpp
 *
 *  Created on: 22 dic. 2019
 *      Author: msabe
 */
#include <stdlib.h>
#include <time.h>
#include <iostream>
#include <string.h>
#include <fstream>

#include "Aparcamiento.h"
#include "Vehiculo.h"
#include "VehiculoOficial.h"
#include "VehiculoResidente.h"
#include "VehiculoNoResidente.h"
#include "ExcepcionFichero.h"
#include "ExcepcionEscribirFichero.h"
#include "ExcepcionOverFlowAparcamiento.h"

using namespace std;

Aparcamiento::Aparcamiento() {
	plazasTotales = 0;
	plazasLibres = plazasTotales;
	estadoAparcamiento = vacio;
	contadorVehiculosEspeciales = 0;
	contadorVehiculosTotales = 0;
	contadorE = 0;
	registroAparcamiento = new Vehiculo*[plazasTotales];
	registroVehiculosEspeciales = new Vehiculo*();
	registroVehiculosTotales = new Vehiculo*();
}

Aparcamiento::Aparcamiento(int plazasT, int plazasL){
	plazasTotales = plazasT;
	plazasLibres = plazasL;
	contadorVehiculosEspeciales = 0;
	contadorVehiculosTotales = 0;
	contadorE = 0;
	if(plazasLibres == 0)
		estadoAparcamiento = lleno;
	else if(plazasLibres < plazasTotales && plazasLibres > 0)
		estadoAparcamiento = conPlazasLibres;
	else if(plazasLibres == plazasTotales)
		estadoAparcamiento = vacio;

	registroAparcamiento = new Vehiculo*[plazasTotales];
	for(int i = 0;i < plazasTotales;i++){
		registroAparcamiento[i] = NULL;
	}
	registroVehiculosEspeciales = new Vehiculo*[1];
	registroVehiculosEspeciales[0] = NULL;
	registroVehiculosTotales = new Vehiculo*[1];
}

Aparcamiento::Aparcamiento(const Aparcamiento &other){
	plazasTotales = other.plazasTotales;
	plazasLibres = other.plazasLibres;
	contadorVehiculosEspeciales = other.contadorVehiculosEspeciales;
	contadorVehiculosTotales = other.contadorVehiculosTotales;
	contadorE = other.contadorE;
	registroAparcamiento = new Vehiculo*[plazasTotales];
	registroVehiculosEspeciales = new Vehiculo*[1];
	registroVehiculosTotales = new Vehiculo*[1];
	estadoAparcamiento = other.estadoAparcamiento;
}

void Aparcamiento::menu(){
	int respuesta, numElementosVT = 0;
	char matricula[TAM_MATRICULA], recogerMatricula[TAM_MATRICULA];

	do{
		cout<<"Bienvenido al menu del Aparcamiento !!!\n"<<endl;
		cout<<"Introduce un 0 para registrar la entrada de un vehiculo aleatorio."<<endl;
		cout<<"Introduce un 1 para registrar la entrada de un vehiculo manualmente."<<endl;
		cout<<"Introduce un 2 para registrar la salida de un vehiculo."<<endl;
		cout<<"Introduce un 3 para generar el informe de los vehiculos residentes."<<endl;
		cout<<"Introduce un 4 para cambiar de mes."<<endl;
		cout<<"Introduce un 5 para ver los vehiculos aparcados."<<endl;
		cout<<"Introduce un 6 para ver la lista de vehiculos especiales."<<endl;
		cout<<"Introduce un 7 para ver las estancia de un vehiculo."<<endl;
		cout<<"Introduce un 8 para leer desde fichero."<<endl;
		cout<<"Introduce un -1 para salir del menu."<<endl;
		cout<<"\nTu eleccion :";
		cout<<endl;

		cin>>respuesta;
		switch(respuesta){
			case 0:
				generarMatricula(matricula);
				try{
					registrarEntrada(matricula);
				}catch(ExcepcionOverFlowAparcamiento & eofa){
					cout<<eofa.what()<<endl;
				}catch(runtime_error &rt){
					cout<<rt.what();
				}catch(...){
					cout<<"Error generico, aparcamiento lleno"<<endl;
				}
				break;
			case 1:{
				do{
					cout<<"Introduce 4 numeros";
					cout<<" despues 'O' para coche oficial, 'R' para residente y 'N' para no residente"<<endl;
					cout<<"Para finalizar rellena con 2 consonantes comprendidas entre la B y la L"<<endl;
					cout<<"Inserta aqui :";
					cin>>matricula;
					fflush(stdin);
					matricula[TAM_MATRICULA - 1] = '\0';
				}while((comprobarSiElCocheEstaEnElAparcamiento(matricula) == 1) || ((matricula[4] != 'O') && (matricula[4] != 'R') && (matricula[4] != 'N')) || (strlen(matricula) != TAM_MATRICULA - 1));
				comprobarSiElCocheEstaEnElRegistroVE(matricula);//Si el vehiculo ya estaba en el registro de los especiales no añade nada
				try{
					registrarEntrada(matricula);
				}catch(ExcepcionOverFlowAparcamiento & eofa){
					cout<<eofa.what()<<endl;
				}catch(runtime_error &rt){
					cout<<rt.what();
				}catch(...){
					cout<<"Error generico, aparcamiento lleno"<<endl;
				}
				break;
			}case 2:
				cout<<"Introduce la matricula del vehiculo que sale :";//si no esta no borra ninguno
				cin>>recogerMatricula;
				registrarSalida(recogerMatricula);
				break;
			case 3:
				try{
					generarInformeVehiculoResidente();
				}catch(ExcepcionEscribirFichero &eeF){
						cout<<eeF.what();
				}catch(runtime_error &rt){
					cout<<rt.what();
				}catch(...){
					cout<<"Error el fichero no puede ser escrito, fallo generico."<<endl;
				}
				break;
			case 4:
				break;
			case 5:
				verAparcamiento();
				break;
			case 6:
				verVehiculosEspeciales();
				break;
			case 7:{
				int i = 0;
				do{
					cout<<"introduce la matricula del que quieras ver las estancias.";
					cout<<"Introduce 4 numeros";
					cout<<" despues 'O' para coche oficial, 'R' para residente y 'N' para no residente"<<endl;
					cout<<"Para finalizar rellena con 2 consonantes comprendidas entre la B y la L"<<endl;
					cout<<"Inserta aqui :";
					cin>>matricula;
					fflush(stdin);
					matricula[TAM_MATRICULA - 1] = '\0';
				}while((matricula[4] != 'O') && (matricula[4] != 'R') && (matricula[4] != 'N'));
				while(registroVehiculosTotales[i] != NULL){
					if(registroVehiculosTotales[i]->getMatricula() != NULL)
						numElementosVT++;
					i++;
				}
				if(matricula[4] == 'O'){
					for(i = 0;i < numElementosVT;i++){
						if(registroVehiculosTotales[i] != NULL && strcmp(registroVehiculosTotales[i]->getMatricula(), matricula) == 0){
							registroVehiculosTotales[i]->mostrarEstancias();
							i = numElementosVT;
						}
					}
				}else if(matricula[4] == 'R'){
					for(i = 0;i < numElementosVT;i++){
						if(registroVehiculosTotales[i] != NULL && strcmp(registroVehiculosTotales[i]->getMatricula(), matricula) == 0){
							registroVehiculosTotales[i]->mostrarEstancias();
							i = numElementosVT;
						}
					}
				}else if(matricula[4] == 'N'){
					for(i = 0;i < numElementosVT;i++){
						if(registroVehiculosTotales[i] != NULL && strcmp(registroVehiculosTotales[i]->getMatricula(), matricula) == 0){
							registroVehiculosTotales[i]->mostrarEstancias();
							i = numElementosVT;
						}
					}
				}
			}break;
			case 8:
				try{
					leerFicheros();
					leerEstancias();
				}catch(ExcepcionFichero &ef){
					cout<<ef.what();
				}catch(runtime_error &rt){
					cout<<rt.what();
				}catch(...){
					cout<<"Error al leer fichero"<<endl;
				}
				break;
			case -1:
				cout<<"\nFin del programa"<<endl;
				break;
			default:
				cout<<"Introduce una opcion de las anteriores.\n"<<endl;
				break;
		}
	}while(respuesta != -1);
	try{
		rellenarFicheroAparcamiento();
		rellenarFicheroVehiculosEspeciales();
		rellenarFicheroVehiculosTotales();
		rellenarFicheroEstancias();
	}catch(ExcepcionEscribirFichero &eeF){
		cout<<eeF.what();
	}catch(runtime_error &rt){
		cout<<rt.what();
	}catch(...){
		cout<<"Error el fichero no puede ser escrito, fallo generico."<<endl;
	}

}

void Aparcamiento::generarMatricula(char matricula[]){
	int i = 0;
	char miChar, letrasMatriculaId[] = {'O', 'R', 'N'}, letrasMatricula [] = {'B', 'C', 'D', 'F', 'G' ,'H', 'J', 'K', 'L'};
	srand(time(0));
	for(i = 0;i < TAM_MATRICULA - 4;i++){
		miChar = deIntAChar(rand() % 10);
		matricula[i] = miChar;//deIntAChar(rand() % 10); //Lleno las 4 primeras casillas de la matricula con numeros
	}
	matricula[i] = letrasMatriculaId[rand() % 3];//En la cuarta posicion pongo el id del vehiculo
	for(i = 5;i < TAM_MATRICULA - 1;i++){
		matricula[i] = letrasMatricula[rand() % 9];//Simplemente relleno el resto de la matricula
	}
	matricula[i] = '\0';
}

int Aparcamiento::comprobarSiElCocheEstaEnElAparcamiento(char m[TAM_MATRICULA]){
	int i = 0, j = 0, matriculaRepetida = 0, verificadorMatricula= 0;
	for(i = 0;i < plazasTotales;i++){
		if(registroAparcamiento[i] != NULL)
			matriculaRepetida = 0;
	}
	for(i = 0;i < plazasTotales;i++){
		for(j = 0;j < TAM_MATRICULA;j++){
			if(registroAparcamiento[i] != NULL){
				if(registroAparcamiento[i]->getMatricula()[j] == m[j])
					verificadorMatricula++;
			}
		}
		if(verificadorMatricula == TAM_MATRICULA){
			matriculaRepetida = 1;
			j = TAM_MATRICULA;
			i = plazasTotales;

		}
		verificadorMatricula = 0;

	}
	if(matriculaRepetida == 1)//si es 1 significa que se repite la matricula
		cout<<"\nEl coche ya se encuentra en el aparcamiento, introduce otro :";
	return matriculaRepetida;
}

void Aparcamiento::comprobarSiElCocheEstaEnElRegistroVE(char* m){
	int i = 0, j = 0, verificadorMatricula= 0;
	for(i = 0;i < contadorVehiculosEspeciales;i++){
		for(j = 0;j < TAM_MATRICULA;j++){
			if(registroVehiculosEspeciales[i] != NULL){
				if(registroVehiculosEspeciales[i]->getMatricula()[j] == m[j])
					verificadorMatricula++;
			}
		}
		if(verificadorMatricula == TAM_MATRICULA){
			registroVehiculosEspeciales[i] = NULL;
			j = TAM_MATRICULA;
			i = plazasTotales;
		}
		verificadorMatricula = 0;
	}
}

char Aparcamiento::deIntAChar(int respuesta) {
	switch (respuesta) {
	case 0:
		return '0';
	case 1:
		return '1';
	case 2:
		return '2';
	case 3:
		return '3';
	case 4:
		return '4';
	case 5:
		return '5';
	case 6:
		return '6';
	case 7:
		return '7';
	case 8:
		return '8';
	case 9:
		return '9';
	}
	return 0;
}

void Aparcamiento::aumentarPlazasLibres(){
	plazasLibres++;
}

void Aparcamiento::reducirPlazasLibres(){
	plazasLibres--;
}

void Aparcamiento::rellenarFicheroAparcamiento(){
	ofstream ficheroEAparcamiento("Aparcamiento.txt");
	if(ficheroEAparcamiento.fail())
		throw ExcepcionEscribirFichero();
	for(int i = 0;i < plazasTotales;i++){
		if(registroAparcamiento[i] != NULL)
			ficheroEAparcamiento<<registroAparcamiento[i]->getMatricula()<<endl;
	}
	ficheroEAparcamiento.close();
}

void Aparcamiento::rellenarFicheroVehiculosEspeciales(){
	ofstream ficheroEVehiculosEspeciales("VehiculosEspeciales.txt");
	if(ficheroEVehiculosEspeciales.fail())
		throw ExcepcionEscribirFichero();
	ficheroEVehiculosEspeciales<<contadorVehiculosEspeciales<<endl;
	for(int i = 0;i < contadorVehiculosEspeciales;i++){
		if(registroVehiculosEspeciales[i] != NULL){
			ficheroEVehiculosEspeciales<<registroVehiculosEspeciales[i]->getMatricula()<<endl;
		}
	}
	ficheroEVehiculosEspeciales.close();
}

void Aparcamiento::registrarEntrada(char matricula[]){

	int i = 0, salida = 0, elementoRepetido = 0;

	Vehiculo **punteroAuxVT;
	punteroAuxVT = new Vehiculo*[1 + contadorVehiculosTotales];
	for(i = 0;i < contadorVehiculosTotales;i++){
		punteroAuxVT[i] = registroVehiculosTotales[i];
	}
	registroVehiculosTotales = punteroAuxVT;
	if(plazasLibres > 0){
		Vehiculo **punteroAux;
		if(matricula[4] == 'O'){
			VehiculoOficial* miVehiculoO=new VehiculoOficial(matricula);
			for(i = 0;i < plazasTotales;i++){
				if(registroAparcamiento[i] == NULL && salida == 0){
					registroAparcamiento[i] = miVehiculoO;
					salida = 1;
				}
			}
			salida = 0;
			reducirPlazasLibres();

			for(i = 0;i < contadorVehiculosEspeciales;i++){
				if(registroVehiculosEspeciales[i]!= NULL){
					if(strcmp(registroVehiculosEspeciales[i]->getMatricula(), matricula) == 0)
						elementoRepetido = 1;
				}
			}
			if(elementoRepetido == 0){
				punteroAux = new Vehiculo*[1 + contadorVehiculosEspeciales];
				for(i = 0;i < contadorVehiculosEspeciales;i++){
					punteroAux[i] = registroVehiculosEspeciales[i];
				}
				registroVehiculosEspeciales = punteroAux;
				registroVehiculosEspeciales[contadorVehiculosEspeciales] = miVehiculoO;
				contadorVehiculosEspeciales++;
			}
			registroVehiculosTotales[contadorVehiculosTotales] = miVehiculoO;
			contadorVehiculosTotales++;
			elementoRepetido = 0;
		}
		else if(matricula[4] == 'R'){
			VehiculoResidente* miVehiculoR = new VehiculoResidente(matricula, 0, 0);
			for(int j = 0;j < plazasTotales;j++){
				if(registroAparcamiento[j] == NULL && salida == 0){
					registroAparcamiento[j] = miVehiculoR;
					salida = 1;
				}
			}
			salida = 0;
			reducirPlazasLibres();
			for(i = 0;i < contadorVehiculosEspeciales;i++){
				if(registroVehiculosEspeciales[i]!= NULL){
					if(strcmp(registroVehiculosEspeciales[i]->getMatricula(), matricula) == 0)
						elementoRepetido = 1;
				}
			}
			if(elementoRepetido == 0){
				punteroAux = new Vehiculo*[1 + contadorVehiculosEspeciales];
				for(i = 0;i < contadorVehiculosEspeciales;i++){
					punteroAux[i] = registroVehiculosEspeciales[i];
				}
				registroVehiculosEspeciales = punteroAux;
				registroVehiculosEspeciales[contadorVehiculosEspeciales] = miVehiculoR;
				contadorVehiculosEspeciales++;
			}
			registroVehiculosTotales[contadorVehiculosTotales] = miVehiculoR;
			contadorVehiculosTotales++;
			elementoRepetido = 0;
		}
		else if(matricula[4] == 'N'){
			VehiculoNoResidente* miVehiculoNR = new VehiculoNoResidente(matricula);
			registroVehiculosTotales[contadorVehiculosTotales] = miVehiculoNR;
			contadorVehiculosTotales++;
			for(i = 0;i < plazasTotales;i++){
				if(registroAparcamiento[i] == NULL && salida == 0){
					registroAparcamiento[i] = miVehiculoNR;
					salida = 1;
				}
			}
			salida = 0;
			reducirPlazasLibres();
		}
	}else
		throw ExcepcionOverFlowAparcamiento();
}

void Aparcamiento::verAparcamiento(){
	int i = 0;
	if(i <= (plazasTotales - plazasLibres - 1))
		cout<<"Los vehiculos aparcados son :";
	else
		cout<<"El apacarmiento esta vacio\n"<<endl;
	while(i < (plazasTotales)){
		if(registroAparcamiento[i] != NULL)
			cout<<registroAparcamiento[i]->getMatricula()<<" ";
		else
			cout<<" vacio ";
		i++;
	}
	cout<<"\n"<<endl;
}

void Aparcamiento::verVehiculosEspeciales(){
	int i = 0, vacia = 0;
	for(i = 0;i < contadorVehiculosEspeciales;i++){
		if(registroVehiculosEspeciales[i] != NULL)
			vacia = 1;
	}
	if(vacia == 0)
		cout<<"La lista de vehiculos especiales esta vacia";
	else{
		cout<<"Los vehiculos especiales  son :";
		for(i = 0;i < contadorVehiculosEspeciales;i++){
			if(registroVehiculosEspeciales[i] != NULL){
				cout<<registroVehiculosEspeciales[i]->getMatricula()<<" ";
			}
		}
	}
	cout<<"\n"<<endl;
}

void Aparcamiento::registrarSalida(char m[]){
	int i = 0;
	contadorE = 0;
	for(i = 0;i < plazasTotales;i++){
		if(registroAparcamiento[i] != NULL && strcmp(registroAparcamiento[i]->getMatricula(), m) == 0){
			registroAparcamiento[i] = NULL;
			aumentarPlazasLibres();
		}
	}
	for(i = 0;i < contadorVehiculosTotales;i++){
		if(registroVehiculosTotales[i] != NULL && strcmp(registroVehiculosTotales[i]->getMatricula(), m) == 0)
			contadorE++;
	}
	for(i = 0;i < contadorVehiculosTotales;i++){
		if(registroVehiculosTotales[i] != NULL && strcmp(registroVehiculosTotales[i]->getMatricula(), m) == 0){
			registroVehiculosTotales[i]->setContadorEstancias(contadorE);
			registroVehiculosTotales[i]->guardarEstancia();
			i = contadorVehiculosTotales;
		}
	}
	//rellenarFicheroAparcamiento();
}

void Aparcamiento::rellenarFicheroInformeVehiculosResidentes(Vehiculo **registroVehiculosR, int tamanio){
	ofstream ficheroEInformeVR("InformeMensualVehiculosResidentes.txt");
	if(ficheroEInformeVR.fail())
		throw ExcepcionEscribirFichero();
	int i = 0, j = 0, horas = 0, minutos = 0;
	for(i = 0;i < tamanio;i++){
		if(registroVehiculosR[i] != NULL){
			minutos = 0;
			horas = 0;
			ficheroEInformeVR<<"La matricula :"<<registroVehiculosR[i]->getMatricula();
			ficheroEInformeVR<<" ha sido registrada "<<registroVehiculosR[i]->getContadorEstancias()<<" veces estes mes."<<endl;
			for(j = 0;j < registroVehiculosR[i]->getContadorEstancias();j++){
				minutos += registroVehiculosR[i]->getEstancia()[j].getTiempoAparcado();
			}
			registroVehiculosR[i]->setTiempoAparcadoAlMes(minutos);
			while(minutos >= 60){
				minutos -= 60;
				horas++;
			}
			ficheroEInformeVR<<"Ha estado aparcado durante "<<horas<<" horas y "<<minutos<<" minutos"<<endl;
			ficheroEInformeVR<<"Debe abonar a final de mes "<<registroVehiculosR[i]->getDineroAPagarAlMes()<<"€"<<endl;
			ficheroEInformeVR<<endl;
		}
	}
	ficheroEInformeVR.close();
}

void Aparcamiento::leerFicheros(){
	int i = 0, j = 0, elementoRepetido = 0;
	char m[TAM_MATRICULA];
	ifstream ficheroAparcamiento("Aparcamiento.txt"), ficheroVEspeciales("VehiculosEspeciales.txt");
	ifstream ficheroVTotales("VehiculosTotales.txt");
	if(ficheroAparcamiento.fail() || ficheroVEspeciales.fail() || ficheroVTotales.fail())
		throw ExcepcionFichero();
	Vehiculo **registroAparcamientoAux, **registroVehiculosEspecialesAux, **registroVehiculosTotalesAux;
	registroAparcamientoAux = new Vehiculo*[plazasTotales];
	/*****************VEHICULOS TOTALES******************************/
	/***********LEO CUANTOS HAY EN LO HAGO DE ESE TAM******************/
	ficheroVTotales>>contadorVehiculosTotales;
	ficheroVTotales.getline(m,TAM_MATRICULA);
	registroVehiculosTotalesAux = new Vehiculo*[contadorVehiculosTotales];
	for(i = 0;i < contadorVehiculosTotales;i++){
		ficheroVTotales.getline(m, TAM_MATRICULA+1);
		if(m[4] == 'O'){
			VehiculoOficial *vo = new VehiculoOficial(m);
			registroVehiculosTotalesAux[i] = vo;
		}else if(m[4] == 'R'){
			VehiculoResidente *vr = new VehiculoResidente(m, 0, 0);
			registroVehiculosTotalesAux[i] = vr;
		}else if(m[4] == 'N'){
			VehiculoNoResidente *vnr = new VehiculoNoResidente(m);
			registroVehiculosTotalesAux[i] = vnr;
		}
	}
	cout<<endl;
	/******************Y LO METO AL ARRAY**************************/
	/*****************VEHICULOS ESPECIALES************************/
	ficheroVEspeciales>>contadorVehiculosEspeciales;
	registroVehiculosEspecialesAux = new Vehiculo*[contadorVehiculosEspeciales];
	for(i = 0;i < contadorVehiculosEspeciales;i++){
		registroVehiculosEspecialesAux[i] = NULL;
	}
	ficheroVEspeciales.getline(m, TAM_MATRICULA);
	for(i = 0;i < contadorVehiculosEspeciales;i++){
		ficheroVEspeciales.getline(m, TAM_MATRICULA);
		if(m[0] != '\0'){
			if(m[4] == 'O'){
				VehiculoOficial *vo = new VehiculoOficial(m);
				registroVehiculosEspecialesAux[i] = vo;
			}else if(m[4] == 'R'){
				VehiculoResidente *vr = new VehiculoResidente(m, 0, 0);
				registroVehiculosEspecialesAux[i] = vr;
			}else if(m[4] == 'N'){
				VehiculoNoResidente *vnr = new VehiculoNoResidente(m);
				registroVehiculosEspecialesAux[i] = vnr;
			}
		}
	}
	/***********************VEHICULOS APARCAMIENTO********************/
	for(i = 0;i < plazasTotales;i++){
		registroAparcamientoAux[i] = NULL;
	}
	for(i = 0;i < plazasTotales;i++){
		ficheroAparcamiento.getline(m, TAM_MATRICULA);

		for(j = 0;j < plazasTotales;j++){
			if(registroAparcamientoAux[j] != NULL)
				if(strcmp(registroAparcamientoAux[j]->getMatricula(), m) == 0)
					elementoRepetido = 1;
		}
		if(elementoRepetido == 0)
			if(m[0] != '\0'){
				if(m[4] == 'O'){
					VehiculoOficial *vo = new VehiculoOficial(m);
					registroAparcamientoAux[i] = vo;
				}else if(m[4] == 'R'){
					VehiculoResidente *vr = new VehiculoResidente(m, 0, 0);
					registroAparcamientoAux[i] = vr;
				}else if(m[4] == 'N'){
					VehiculoNoResidente *vnr = new VehiculoNoResidente(m);
					registroAparcamientoAux[i] = vnr;
				}
				reducirPlazasLibres();
			}
		elementoRepetido = 0;
	}
	/********************************************************************/
	registroAparcamiento = registroAparcamientoAux;
	registroVehiculosEspeciales = registroVehiculosEspecialesAux;
	registroVehiculosTotales = registroVehiculosTotalesAux;
	ficheroAparcamiento.close();
	ficheroVEspeciales.close();
	ficheroVTotales.close();
}

void Aparcamiento::generarInformeVehiculoResidente(){
	int i = 0, j = 0, k = 0, minutos = 0, horas = 0;
	bool estaDentro = false;
	Vehiculo **registroVehiculosResidentes = new Vehiculo*[contadorVehiculosTotales];
	for(i = 0;i < contadorVehiculosTotales;i++){
		registroVehiculosResidentes[i] = NULL;
	}
	for(i = 0;i < contadorVehiculosTotales;i++){
		if(registroVehiculosTotales[i] != NULL && registroVehiculosTotales[i]->getMatricula()[4] == 'R'){
			for(j = 0;j < contadorVehiculosTotales;j++){
				if(registroVehiculosResidentes[j] != NULL && registroVehiculosTotales[j]->getMatricula()[4] == 'R'){
					minutos = 0;
					if(strcmp(registroVehiculosTotales[i]->getMatricula(), registroVehiculosResidentes[j]->getMatricula()) != 0)
						estaDentro = false;
					else if(strcmp(registroVehiculosTotales[i]->getMatricula(), registroVehiculosResidentes[j]->getMatricula()) == 0){
						estaDentro = true;
						j = contadorVehiculosTotales;
					}
				}
			}
			if(!estaDentro){
				registroVehiculosResidentes[k] = registroVehiculosTotales[i];
				k++;
			}
		}
	}
	for(i = 0;i < k;i++){
		if(registroVehiculosResidentes[i] != NULL){
			cout<<"\nLa matricula :"<<registroVehiculosResidentes[i]->getMatricula();
			cout<<" ha sido registrada "<<registroVehiculosResidentes[i]->getContadorEstancias()<<" veces este mes"<<endl;
			registroVehiculosResidentes[i]->calcularCuotaAPagar();
			horas = 0;
			minutos = 0;
			for(j = 0;j < registroVehiculosResidentes[i]->getContadorEstancias();j++){
				minutos += registroVehiculosResidentes[i]->getEstancia()[j].getTiempoAparcado();
			}
			registroVehiculosResidentes[i]->setTiempoAparcadoAlMes(minutos);
			while(minutos >= 60){
				minutos -= 60;
				horas++;
			}
			cout<<"Ha estado aparcado durante "<<horas<<" horas y "<<minutos<<" minutos"<<endl;
			cout<<"Debe abonar a final de mes "<< registroVehiculosResidentes[i]->getDineroAPagarAlMes()<<"€"<<endl;
			cout<<"\n"<<endl;
		}
	}
	rellenarFicheroInformeVehiculosResidentes(registroVehiculosResidentes, k);
	delete registroVehiculosResidentes;
}

void Aparcamiento::rellenarFicheroEstancias(){
	int i = 0, j = 0, k = 0,minutos = 0,estanciasFichero = 0;
	bool estaDentro = false;
	Vehiculo **registroVehiculosOficiales = new Vehiculo*[contadorVehiculosTotales];
	ofstream ficheroEEstanciasO("EstanciasVehiculosOficiales.txt");
	if(ficheroEEstanciasO.fail())
		throw ExcepcionEscribirFichero();
	for(i = 0;i < contadorVehiculosTotales;i++){
		registroVehiculosOficiales[i] = NULL;
	}
	for(i = 0;i < contadorVehiculosTotales;i++){
		if(registroVehiculosTotales[i] != NULL && registroVehiculosTotales[i]->getMatricula()[4] == 'O'){
			for(j = 0;j < contadorVehiculosTotales;j++){
				if(registroVehiculosOficiales[j] != NULL && registroVehiculosTotales[i]->getMatricula()[4] == 'O'){
					minutos = 0;
					if(strcmp(registroVehiculosTotales[i]->getMatricula(), registroVehiculosOficiales[j]->getMatricula()) != 0)
						estaDentro = false;
					else if(strcmp(registroVehiculosTotales[i]->getMatricula(), registroVehiculosOficiales[j]->getMatricula()) == 0){
						estaDentro = true;
						j = contadorVehiculosTotales;
					}
				}
			}
			if(!estaDentro){
				registroVehiculosOficiales[k] = registroVehiculosTotales[i];
				k++;
			}
		}
	}
	for(i = 0;i < contadorVehiculosTotales;i++){
		if(registroVehiculosOficiales[i] != NULL){
			estanciasFichero++;
		}
	}
	for(i = 0;i < k;i++){
		if(registroVehiculosOficiales[i] != NULL){
			ficheroEEstanciasO<<registroVehiculosOficiales[i]->getMatricula()<<endl;
			ficheroEEstanciasO<<registroVehiculosOficiales[i]->getContadorEstancias()<<endl;
			minutos = 0;
			for(j = 0;j < registroVehiculosOficiales[i]->getContadorEstancias();j++){
				ficheroEEstanciasO<<registroVehiculosOficiales[i]->getEstancia()[j].getFechaEntrada().getHora()<<endl;
				ficheroEEstanciasO<<registroVehiculosOficiales[i]->getEstancia()[j].getFechaEntrada().getMinuto()<<endl;
				ficheroEEstanciasO<<registroVehiculosOficiales[i]->getEstancia()[j].getFechaSalida().getHora()<<endl;
				ficheroEEstanciasO<<registroVehiculosOficiales[i]->getEstancia()[j].getFechaSalida().getMinuto()<<endl;
				minutos = registroVehiculosOficiales[i]->getEstancia()[j].getTiempoAparcado();
				ficheroEEstanciasO<<minutos<<endl;
			}

		}
	}
	delete registroVehiculosOficiales;
	ficheroEEstanciasO.close();
}

void Aparcamiento::rellenarFicheroVehiculosTotales(){
	ofstream ficheroVT("VehiculosTotales.txt");
	if(ficheroVT.fail())
		throw ExcepcionEscribirFichero();
	ficheroVT<<contadorVehiculosTotales<<endl;
	for(int i = 0;i < contadorVehiculosTotales;i++){
		if(registroVehiculosTotales[i] != NULL)
			ficheroVT<<registroVehiculosTotales[i]->getMatricula()<<endl;
	}
	ficheroVT.close();
}

void Aparcamiento::leerEstancias(){
	int i = 0, j = 0, horasI = 0, minutosI = 0, horasF = 0, minutosF = 0, minutosTotales = 0, contEstancias;
		char m[TAM_MATRICULA];
		ifstream ficheroLEstanciasO("EstanciasVehiculosOficiales.txt");
		if(ficheroLEstanciasO.fail())
			throw ExcepcionFichero();
		for(int k = 0;k < contadorVehiculosTotales;k++){
			ficheroLEstanciasO.getline(m, TAM_MATRICULA);
			ficheroLEstanciasO>>contEstancias;

			for(i = 0;i < contEstancias;i++){
				ficheroLEstanciasO>>horasI;
				ficheroLEstanciasO>>minutosI;
				ficheroLEstanciasO>>horasF;
				ficheroLEstanciasO>>minutosF;
				ficheroLEstanciasO>>minutosTotales;

				if(registroVehiculosTotales[k] != NULL){
					if(strcmp(registroVehiculosTotales[j]->getMatricula(), m) == 0){
						Fecha miFechaE(horasI, minutosI);
						Fecha miFechaS(horasF, minutosF);
						Estancia *punteroAuxiliar = new Estancia[contEstancias];
						for(int i = 0;i < contEstancias - 1;i++){
							punteroAuxiliar[i] = registroVehiculosTotales[j]->getEstancia()[i];
						}
						registroVehiculosTotales[j]->setEstancia(punteroAuxiliar);
						registroVehiculosTotales[j]->setMatricula(m);
						registroVehiculosTotales[j]->setContadorEstancias(contEstancias);
						registroVehiculosTotales[j]->getEstancia()[i].setFechaEntrada(miFechaE);
						registroVehiculosTotales[j]->getEstancia()[i].setFechaSalida(miFechaS);
						registroVehiculosTotales[j]->getEstancia()[i].setTiempoAparcado(minutosTotales);
					}
				}
			}
		}
		ficheroLEstanciasO.close();
}


void Aparcamiento::siguienteMes(){
	int i = 0, j = 0;
	for(i = 0;i < contadorVehiculosTotales;i++){
		if(registroVehiculosTotales[i] != NULL && registroVehiculosTotales[i]->getMatricula()[4] == 'O'){
			for(j = 0;j < registroVehiculosTotales[i]->getContadorEstancias();j++){
				delete &registroVehiculosTotales[i]->getEstancia()[j];
			}
			registroVehiculosTotales[i]->setContadorEstancias(0);
		}
			//delete registroVehiculosTotales[i]->getEstancia();
		if(registroVehiculosTotales[i] != NULL && registroVehiculosTotales[i]->getMatricula()[4] == 'R'){
			registroVehiculosTotales[i]->setTiempoAparcadoAlMes(0);
		}
		Estancia *estancia = new Estancia[1];
		registroVehiculosTotales[i]->setEstancia(estancia);
	}
	cout<<"\nCambiamos de mes."<<endl;
}

Aparcamiento Aparcamiento::operator=(const Aparcamiento &other)
{
	plazasTotales = other.plazasTotales;
	plazasLibres = other.plazasLibres;
	contadorVehiculosEspeciales = other.contadorVehiculosEspeciales;
	contadorVehiculosTotales = other.contadorVehiculosTotales;
	contadorE = other.contadorE;
	registroAparcamiento = new Vehiculo*[plazasTotales];
	registroVehiculosEspeciales = new Vehiculo*[1];
	registroVehiculosTotales = new Vehiculo*[1];
	estadoAparcamiento = other.estadoAparcamiento;
	return(*this);
}

Aparcamiento::~Aparcamiento() {
	delete registroAparcamiento;
	delete registroVehiculosEspeciales;
}
