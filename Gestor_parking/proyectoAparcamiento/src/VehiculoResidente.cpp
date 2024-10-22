/*
 * VehiculoResidente.cpp
 *
 *  Created on: 22 dic. 2019
 *      Author: msabe
 */

#include "VehiculoResidente.h"
#include <ctime>

VehiculoResidente::VehiculoResidente():Vehiculo(){
	dineroAPagarAlMes = 0;
	tiempoAparcadoAlMes = 0;
	estancia = 0;
	costePorMinuto = 0.002;
}

VehiculoResidente::VehiculoResidente(char matricula[],float dineroMes, int tAparcadoMes):Vehiculo(matricula){
	dineroAPagarAlMes = dineroMes;
	tiempoAparcadoAlMes = tAparcadoMes;
	estancia = new Estancia[1];
	costePorMinuto = 0.002;
}

void VehiculoResidente::guardarEstancia(){

	srand(time(NULL));
	Fecha fechaEntrada(rand() % 24, rand() % 60);
	Fecha fechaSalida(rand() % 24, rand() % 60);
	Estancia *punteroAuxiliar = new Estancia[contadorEstancias];
	Estancia miEstancia(matricula, fechaEntrada, fechaSalida);
	for(int i = 0;i < contadorEstancias - 1;i++){
		punteroAuxiliar[i] = estancia[i];
	}
	estancia = punteroAuxiliar;
	miEstancia.calcularTiempoAparcadoEstancia();
	estancia[contadorEstancias - 1] = miEstancia;
}

void VehiculoResidente::mostrarEstancias(){
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

		cout<<"\n"<<endl;
	}
	calcularCuotaAPagar();
}

void VehiculoResidente::calcularCuotaAPagar(){
	int i = 0;
	dineroAPagarAlMes = 0;
	tiempoAparcadoAlMes = 0;
	for(i = 0;i < contadorEstancias;i++){
		tiempoAparcadoAlMes += estancia[i].getTiempoAparcado();
	}
	dineroAPagarAlMes = tiempoAparcadoAlMes * costePorMinuto;
	//cout<<"TIEMPO A AL MES "<<tiempoAparcadoAlMes<<" y dinero "<<dineroAPagarAlMes;
}

VehiculoResidente::VehiculoResidente(const VehiculoResidente &other){
	strcpy(matricula, other.matricula);
	tiempoAparcadoAlMes = other.tiempoAparcadoAlMes;
	estancia->setDineroAPagar(other.estancia->getDineroAPagar());
	estancia->setFechaEntrada(other.estancia->getFechaEntrada());
	estancia->setFechaSalida(other.estancia->getFechaSalida());
	estancia->setMatricula(other.estancia->getMatricula());
	estancia->setTiempoAparcado(other.estancia->getTiempoAparcado());
	dineroAPagarAlMes = other.dineroAPagarAlMes;
	costePorMinuto = other.costePorMinuto;
}

void VehiculoResidente::pagarFinMes(){

}

void VehiculoResidente::resetearTiempo(){
	tiempoAparcadoAlMes = 0;
	dineroAPagarAlMes = 0;
}



VehiculoResidente VehiculoResidente::operator =(const VehiculoResidente &other){
	strcpy(matricula, other.matricula);
	dineroAPagarAlMes = other.dineroAPagarAlMes;
	tiempoAparcadoAlMes = other.tiempoAparcadoAlMes;
	estancia->setDineroAPagar(other.estancia->getDineroAPagar());
	estancia->setFechaEntrada(other.estancia->getFechaEntrada());
	estancia->setFechaSalida(other.estancia->getFechaSalida());
	estancia->setMatricula(other.estancia->getMatricula());
	estancia->setTiempoAparcado(other.estancia->getTiempoAparcado());
	costePorMinuto = other.costePorMinuto;
	return(*this);
}

VehiculoResidente::~VehiculoResidente() {}

