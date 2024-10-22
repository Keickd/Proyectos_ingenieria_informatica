/*
 * Aparcamiento.h
 *
 *  Created on: 22 dic. 2019
 *      Author: msabe
 */

#ifndef APARCAMIENTO_H_
#define APARCAMIENTO_H_
#define TAM_MATRICULA 8

#include <iostream>
#include <string.h>

#include "Vehiculo.h"

using namespace std;

enum estado{vacio, conPlazasLibres, lleno};

enum letrasMatriculaId{O, R, N}; //o =oficial , r=residente, n = no residente
enum letrasMatricula{B, C, D, F, G ,H, J, K, L};

class Aparcamiento {
	int plazasTotales, plazasLibres, contadorVehiculosEspeciales, contadorVehiculosTotales, contadorE;
	Vehiculo **registroAparcamiento, **registroVehiculosEspeciales, **registroVehiculosTotales;
	estado estadoAparcamiento;
public:
	Aparcamiento();
	Aparcamiento(int, int);
	Aparcamiento(const Aparcamiento &other);
	void menu();
	void generarMatricula(char []);
	void aumentarPlazasLibres();
	void reducirPlazasLibres();
	void verAparcamiento();
	void verVehiculosEspeciales();
	void generarInformeVehiculoResidente();
	char deIntAChar(int);
	void leerFicheros();
	void rellenarFicheroAparcamiento();
	void rellenarFicheroVehiculosEspeciales();
	void rellenarFicheroEstancias();
	void rellenarFicheroInformeVehiculosResidentes(Vehiculo **vehiculosR, int);
	void rellenarFicheroVehiculosTotales();
	void leerFicheroVehiculosTotales();
	void leerFicheroAparcamiento();
	void leerEstancias();
	void registrarEntrada(char []);
	int comprobarSiElCocheEstaEnElAparcamiento(char *);
	void comprobarSiElCocheEstaEnElRegistroVE(char *);
	void registrarSalida(char []);
	void siguienteMes();
	Aparcamiento operator=(const Aparcamiento &other);
	virtual ~Aparcamiento();
};

#endif /* APARCAMIENTO_H_ */
