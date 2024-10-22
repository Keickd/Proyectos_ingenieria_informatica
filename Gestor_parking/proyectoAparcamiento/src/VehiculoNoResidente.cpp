/*
 * VehiculoNoResidente.cpp
 *
 *  Created on: 22 dic. 2019
 *      Author: msabe
 */

#include "VehiculoNoResidente.h"
#include <ctime>

VehiculoNoResidente::VehiculoNoResidente():Vehiculo(){
	dineroNR = 0;
	tiempoAparcado = 0;
}

VehiculoNoResidente::VehiculoNoResidente(char *matricula):Vehiculo(matricula){
	estancia = new Estancia[1];
	dineroNR = 0;
	tiempoAparcado = 0;
	costePorMinuto = 0.02;
}

VehiculoNoResidente::VehiculoNoResidente(const VehiculoNoResidente &other){
	strcpy(matricula, other.matricula);
	costePorMinuto = other.costePorMinuto;
	dineroNR = other.dineroNR;
	tiempoAparcado = other.tiempoAparcado;
}

void VehiculoNoResidente::guardarEstancia(){
	srand(time(NULL));
	Fecha fechaEntrada(rand() % 24, rand() % 60);
	Fecha fechaSalida(rand() % 24, rand() % 60);
	Estancia miEstancia(matricula, fechaEntrada, fechaSalida);
	miEstancia.calcularTiempoAparcadoEstancia();
	estancia[0] = miEstancia;
}

void VehiculoNoResidente::mostrarEstancias(){
	int horas = 0, minutos = 0;

		cout<<"\nLa matricula :"<<estancia[0].getMatricula()<<" ha finalizado su estancia."<<endl;
		cout<<"Con fecha de entrada :"<<estancia[0].getFechaEntrada()<<endl;
		cout<<"Con fecha de salida :"<<estancia[0].getFechaSalida()<<endl;
		minutos = estancia[0].getTiempoAparcado();
		tiempoAparcado = minutos;
		while(minutos > 60){
			minutos -= 60;
			horas++;
		}
		cout<<"Ha estado aparcado durante "<<horas<<" horas y "<<minutos<<" minutos"<<endl;
		calcularCuotaAPagar();
		cout<<"\n"<<endl;

}

void VehiculoNoResidente::calcularCuotaAPagar(){
	dineroNR = tiempoAparcado * costePorMinuto;
	cout<<"Debe abonar "<<dineroNR<<"€ por su estancia.";
}

void VehiculoNoResidente::pagar(int tiempoAparcadoEstancia){

}

VehiculoNoResidente VehiculoNoResidente::operator=(const VehiculoNoResidente &other){
	strcpy(matricula, other.matricula);
	costePorMinuto = other.costePorMinuto;
	return(*this);
}

VehiculoNoResidente::~VehiculoNoResidente(){}

