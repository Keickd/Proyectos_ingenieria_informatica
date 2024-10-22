/*
 * VehiculoResidente.h
 *
 *  Created on: 22 dic. 2019
 *      Author: msabe
 */

#ifndef VEHICULORESIDENTE_H_
#define VEHICULORESIDENTE_H_

#include "Vehiculo.h"

class VehiculoResidente: public Vehiculo {
	int tiempoAparcadoAlMes;
	//Estancia *estancia;
public:
	VehiculoResidente();
	VehiculoResidente(char [], float, int);
	VehiculoResidente(const VehiculoResidente &other);
	void guardarEstancia();
	void mostrarEstancias();
	void calcularCuotaAPagar();
	void pagarFinMes();
	void resetearTiempo();
	//void generarInforme();

	VehiculoResidente operator=(const VehiculoResidente &other);
	virtual ~VehiculoResidente();

};

#endif /* VEHICULORESIDENTE_H_ */
