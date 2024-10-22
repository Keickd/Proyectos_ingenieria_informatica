/*
 * VehiculoOficial.h
 *
 *  Created on: 22 dic. 2019
 *      Author: msabe
 */

#ifndef VEHICULOOFICIAL_H_
#define VEHICULOOFICIAL_H_
#include <iostream>
#include<string.h>
using namespace std;
#include "Vehiculo.h"

class VehiculoOficial: public Vehiculo {
protected:
	//Estancia *estancia;
public:
	VehiculoOficial();
	VehiculoOficial(char * matricula);
	VehiculoOficial(const VehiculoOficial &other);
	void guardarEstancia();
	void mostrarEstancias();
	void calcularCuotaAPagar();
	void resetearEstancias();
	VehiculoOficial operator=(const VehiculoOficial &other);
	virtual ~VehiculoOficial();
};

#endif /* VEHICULOOFICIAL_H_ */
