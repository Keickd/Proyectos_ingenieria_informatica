/*
 * Estancia.h
 *
 *  Created on: 22 dic. 2019
 *      Author: msabe
 */

#ifndef ESTANCIA_H_
#define ESTANCIA_H_

#include "Fecha.h"

class Estancia {
	char *matricula;
	Fecha fechaEntrada, fechaSalida;
	int tiempoAparcado;
	float dineroAPagar;
public:
	Estancia();
	Estancia(char *, Fecha, Fecha);
	Estancia(const Estancia &other);
	void iniciarTiempo();
	void pararTiempo();
	void calcularTiempoAparcadoEstancia();
	Estancia operator=(const Estancia &other);
	virtual ~Estancia();

	char* getMatricula() const {
		return matricula;
	}

	float getDineroAPagar() const {
		return dineroAPagar;
	}

	const Fecha& getFechaEntrada() const {
		return fechaEntrada;
	}

	const Fecha& getFechaSalida() const {
		return fechaSalida;
	}

	int getTiempoAparcado() const {
		return tiempoAparcado;
	}


	void setDineroAPagar(float dineroAPagar) {
		this->dineroAPagar = dineroAPagar;
	}

	void setTiempoAparcado(int tiempoAparcado) {
		this->tiempoAparcado = tiempoAparcado;
	}

	void setFechaEntrada(const Fecha &fechaEntrada) {
		this->fechaEntrada = fechaEntrada;
	}

	void setFechaSalida(const Fecha &fechaSalida) {
		this->fechaSalida = fechaSalida;
	}

	void setMatricula(char *matricula) {
		this->matricula = matricula;
	}

};

#endif /* ESTANCIA_H_ */
