/*
 * ExcepcionFichero.h
 *
 *  Created on: 14 ene. 2020
 *      Author: msabe
 */

#ifndef EXCEPCIONFICHERO_H_
#define EXCEPCIONFICHERO_H_

#include <stdexcept>

class ExcepcionFichero: public std::runtime_error {
public:
	ExcepcionFichero():runtime_error("No se pudo cargar el fichero."){};
};

#endif /* EXCEPCIONFICHERO_H_ */
