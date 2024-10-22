/*
 * Vehiculo.h
 *
 *  Created on: 22 dic. 2019
 *      Author: msabe
 */

#ifndef VEHICULO_H_
#define VEHICULO_H_
#define tam_MATRICULA 8
using namespace std;

#include "Estancia.h"

#include <string.h>
#include <iostream>

class Vehiculo {
protected:
	char matricula[tam_MATRICULA];
	int contadorEstancias;
	float costePorMinuto;
	float dineroAPagarAlMes;
	int tiempoAparcadoAlMes;
	Estancia *estancia;
public:
	Vehiculo();
	Vehiculo(char [tam_MATRICULA]);
	Vehiculo(const Vehiculo &other);
	void entrar();
	void salir();
	virtual void guardarEstancia() = 0;
	virtual void mostrarEstancias() = 0;
	virtual void calcularCuotaAPagar() = 0;
	//Vehiculo operator=(const Vehiculo &other);
	virtual ~Vehiculo();

	char* getMatricula() {
		return matricula;
	}
	void setMatricula(char m[tam_MATRICULA]){
		for(int i = 0;i < tam_MATRICULA;i++){
			matricula[i] = m[i];
		}
	}
	void setContadorEstancias(int contadorEstancias) {
		this->contadorEstancias = contadorEstancias;
	}

	int getContadorEstancias() const {
		return contadorEstancias;
	}

	float getDineroAPagarAlMes() const {
		return dineroAPagarAlMes;
	}

	Estancia*& getEstancia(){
		return estancia;
	}

	int getTiempoAparcadoAlMes() const {
		return tiempoAparcadoAlMes;
	}

	void setTiempoAparcadoAlMes(int tiempoAparcadoAlMes) {
		this->tiempoAparcadoAlMes = tiempoAparcadoAlMes;
	}

	void setEstancia(Estancia *&estancia) {
		this->estancia = estancia;
	}

	float getCostePorMinuto() const {
		return costePorMinuto;
	}

};

#endif /* VEHICULO_H_ */
