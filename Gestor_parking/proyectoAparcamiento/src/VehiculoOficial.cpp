/*
 * VehiculoOficial.cpp
 *
 *  Created on: 22 dic. 2019
 *      Author: msabe
 */
#include <ctime>
#include "VehiculoOficial.h"

VehiculoOficial::VehiculoOficial():Vehiculo(){
	contadorEstancias = 0;
	estancia = NULL;
	costePorMinuto = 0;
}

VehiculoOficial::VehiculoOficial(char * matricula):Vehiculo(matricula){
	contadorEstancias = 0;
	estancia = new Estancia[1];
	costePorMinuto = 0;
}

void VehiculoOficial::guardarEstancia(){
	srand(time(NULL));
	Fecha fechaEntrada(rand() % 24, rand() % 60);
	Fecha fechaSalida(rand() % 24, rand() % 60);
	Estancia *punteroAuxiliar = new Estancia[contadorEstancias];
	Estancia miEstancia(matricula, fechaEntrada, fechaSalida);
	for(int i = 0;i < contadorEstancias - 1;i++){
		punteroAuxiliar[i] = estancia[i];
	}
	estancia = punteroAuxiliar;
	//miEstancia.setTiempoAparcado(0);
	miEstancia.calcularTiempoAparcadoEstancia();
	estancia[contadorEstancias - 1] = miEstancia;
}

void VehiculoOficial::mostrarEstancias(){
	int horas = 0, minutos = 0;
	for(int i = 0;i < contadorEstancias;i++){
		cout<<"\nLa matricula :"<<estancia[i].getMatricula();
		cout<<" ha sido registrada "<<contadorEstancias<<" veces este mes"<<endl;
		cout<<"Con fecha de entrada :"<<estancia[i].getFechaEntrada()<<endl;
		cout<<"Con fecha de salida :"<<estancia[i].getFechaSalida()<<endl;
		minutos = estancia[i].getTiempoAparcado();
		horas = 0;
		while(minutos >= 60){
			minutos -= 60;
			horas++;
		}
		cout<<"Ha estado aparcado durante "<<horas<<" horas y "<<minutos<<" minutos"<<endl;
		cout<<"Debido a su estancia debe abonar "<<estancia[i].getDineroAPagar()<<"€"<<endl;
		cout<<"\n"<<endl;
	}
}

void VehiculoOficial::calcularCuotaAPagar(){
	int i = 0;
	for(i = 0;i < contadorEstancias;i++){
		estancia[i].setDineroAPagar(estancia[i].getTiempoAparcado() * costePorMinuto);
	}
	cout<<"CALCULAR CUOTA A PAGAR OFICIAL"<<endl;
}

VehiculoOficial::VehiculoOficial(const VehiculoOficial &other){
	strcpy(matricula, other.matricula);
	costePorMinuto = other.costePorMinuto;
	contadorEstancias = other.contadorEstancias;
	tiempoAparcadoAlMes = other.tiempoAparcadoAlMes;
	estancia->setDineroAPagar(other.estancia->getDineroAPagar());
	estancia->setFechaEntrada(other.estancia->getFechaEntrada());
	estancia->setFechaSalida(other.estancia->getFechaSalida());
	estancia->setMatricula(other.estancia->getMatricula());
	estancia->setTiempoAparcado(other.estancia->getTiempoAparcado());
	dineroAPagarAlMes = other.dineroAPagarAlMes;
}

void VehiculoOficial::resetearEstancias(){
	delete estancia;
	estancia = new Estancia[1];
}

VehiculoOficial VehiculoOficial::operator =(const VehiculoOficial &other){
	strcpy(matricula, other.matricula);
	costePorMinuto = other.costePorMinuto;
	contadorEstancias = other.contadorEstancias;
	tiempoAparcadoAlMes = other.tiempoAparcadoAlMes;
	estancia->setDineroAPagar(other.estancia->getDineroAPagar());
	estancia->setFechaEntrada(other.estancia->getFechaEntrada());
	estancia->setFechaSalida(other.estancia->getFechaSalida());
	estancia->setMatricula(other.estancia->getMatricula());
	estancia->setTiempoAparcado(other.estancia->getTiempoAparcado());
	dineroAPagarAlMes = other.dineroAPagarAlMes;
	return(*this);
}

VehiculoOficial::~VehiculoOficial(){
	delete estancia;
}

