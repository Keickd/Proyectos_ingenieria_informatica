/*
 * Estancia.cpp
 *
 *  Created on: 22 dic. 2019
 *      Author: msabe
 */

#include <iostream>
#include <string.h>
#include "Estancia.h"
using namespace std;

Estancia::Estancia() {
	matricula = new char();
	//matricula = "Vehiculo no autorizado";
	fechaEntrada = Fecha(0, 0);
	fechaSalida = Fecha(0, 0);
	tiempoAparcado = 0;
	dineroAPagar = 0;
}

Estancia::Estancia(char * m, Fecha hE, Fecha hS){
	matricula = new char(strlen(m));
	strcpy(matricula, m);

	fechaEntrada.setHora(hE.getHora());
	fechaEntrada.setMinuto(hE.getMinuto());

	fechaSalida.setHora(hS.getHora());
	fechaSalida.setMinuto(hS.getMinuto());

	tiempoAparcado = 0;
	dineroAPagar = 0;
}

Estancia::Estancia(const Estancia &other){
	matricula = new char(strlen(other.matricula));
	strcpy(matricula, other.matricula);

	fechaEntrada.setHora(other.fechaEntrada.getHora());
	fechaEntrada.setMinuto(other.fechaEntrada.getMinuto());

	fechaSalida.setHora(other.fechaSalida.getHora());
	fechaSalida.setMinuto(other.fechaSalida.getMinuto());

	tiempoAparcado = other.tiempoAparcado;
	dineroAPagar = other.dineroAPagar;
}

void Estancia::iniciarTiempo(){

}

void Estancia::pararTiempo(){

}

void Estancia::calcularTiempoAparcadoEstancia(){
	if(fechaEntrada.operator>=(fechaSalida)){//considereamos que la fecha Entrada es mayor
		Fecha aux(0, 0);
		aux = fechaEntrada;
		fechaEntrada = fechaSalida;
		fechaSalida = aux;
	}
	tiempoAparcado = fechaSalida.operator-(fechaEntrada);//el res en minutos
}

Estancia Estancia::operator =(const Estancia &other){
	if(strlen(matricula) != strlen(other.matricula))
	{
		delete matricula;
		matricula = new char(strlen(other.matricula));
	}
	strcpy(matricula, other.matricula);

	fechaEntrada.setHora(other.fechaEntrada.getHora());
	fechaEntrada.setMinuto(other.fechaEntrada.getMinuto());

	fechaSalida.setHora(other.fechaSalida.getHora());
	fechaSalida.setMinuto(other.fechaSalida.getMinuto());

	tiempoAparcado = other.tiempoAparcado;
	dineroAPagar = other.dineroAPagar;

	return(*this);
}

Estancia::~Estancia() {
	delete matricula;
}

