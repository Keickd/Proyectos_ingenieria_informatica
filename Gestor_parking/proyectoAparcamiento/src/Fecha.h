/*
 * Fecha.h
 *
 *  Created on: 22 dic. 2019
 *      Author: msabe
 */

#ifndef FECHA_H_
#define FECHA_H_

#include <iostream>
using namespace std;

class Fecha {
	int hora, minuto;
public:
	Fecha(int h = 0, int m = 0);
	int operator-(Fecha);
	bool operator>=(Fecha);
	friend ostream& operator<<(ostream &os, const Fecha &f);
	Fecha operator=(const Fecha &);
	virtual ~Fecha();

	int getHora() const {
		return hora;
	}

	void setHora(int hora) {
		this->hora = hora;
	}

	int getMinuto() const {
		return minuto;
	}

	void setMinuto(int minuto) {
		this->minuto = minuto;
	}
};

#endif /* FECHA_H_ */
