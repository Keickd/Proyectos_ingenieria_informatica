/*
 * Fecha.cpp
 *
 *  Created on: 22 dic. 2019
 *      Author: msabe
 */

#include "Fecha.h"

Fecha::Fecha(int h, int m) {
	hora = h;
	minuto = m;
}

ostream& operator<<(ostream &os,const Fecha &f){
	os<<f.getHora()<<" horas y "<<f.getMinuto()<<" minutos";
	return os;
}

bool Fecha::operator>=(Fecha fecha){
	bool esMayor = false;
	if(hora > fecha.hora)
		esMayor = true;
	else if(hora == fecha.hora)
		if(minuto > fecha.minuto)
			esMayor = true;
	return esMayor;
}

Fecha Fecha::operator=(const Fecha &other){
	hora = other.hora;
	minuto = other.minuto;
	return(*this);
}

int Fecha::operator-(Fecha fechaQueResta){
	int horas = 0, minutos = 0;
	horas = hora - fechaQueResta.hora;
	minutos =minuto - fechaQueResta.minuto;
	while(horas > 0){
		minutos += 60;
		horas--;
	}
	return minutos;
}

Fecha::~Fecha(){}

