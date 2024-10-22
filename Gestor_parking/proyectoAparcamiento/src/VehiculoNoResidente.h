/*
 * VehiculoNoResidente.h
 *
 *  Created on: 22 dic. 2019
 *      Author: msabe
 */

#ifndef VehiculoNoResidente_H_
#define VehiculoNoResidente_H_

#include "Vehiculo.h"

class VehiculoNoResidente: public Vehiculo {
	int dineroNR, tiempoAparcado;
public:
	VehiculoNoResidente();
	VehiculoNoResidente(char *);
	VehiculoNoResidente(const VehiculoNoResidente &other);
	void guardarEstancia();
	void mostrarEstancias();
	void calcularCuotaAPagar();
	void pagar(int);
	VehiculoNoResidente operator=(const VehiculoNoResidente &other);
	virtual ~VehiculoNoResidente();
};

#endif /* VehiculoNoResidente_H_ */
