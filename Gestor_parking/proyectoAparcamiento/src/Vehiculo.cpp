/*
 * Vehiculo.cpp
 *
 *  Created on: 22 dic. 2019
 *      Author: msabe
 */

#include "Vehiculo.h"

Vehiculo::Vehiculo() {
	strcpy(matricula, "Vehiculo no autorizado");
	costePorMinuto = 0.0;
	contadorEstancias = 0;
	dineroAPagarAlMes = 0;
	estancia = 0;
	tiempoAparcadoAlMes = 0;
}

Vehiculo::Vehiculo(char id[]){

	int i = 0;
	for(i = 0;i < tam_MATRICULA - 1;i++){
		matricula[i] = id[i];
	}
	matricula[i] = '\0';
	costePorMinuto = 0.0; //en cada clase de vehiculo ya lo incializare a su correspondiente coste
	contadorEstancias = 0;
	dineroAPagarAlMes = 0;
	tiempoAparcadoAlMes = 0;
	estancia = 0;
}


/*Vehiculo::Vehiculo(const Vehiculo &other){
	strcpy(matricula, other.matricula);
	costePorMinuto = other.costePorMinuto;
}*/

void Vehiculo::entrar(){

}

void Vehiculo::salir(){

}

/*Vehiculo Vehiculo::operator =(const Vehiculo &other){
	strcpy(matricula, other.matricula);
	costePorMinuto = other.costePorMinuto;
	return(*this);
}*/

Vehiculo::~Vehiculo() {}

