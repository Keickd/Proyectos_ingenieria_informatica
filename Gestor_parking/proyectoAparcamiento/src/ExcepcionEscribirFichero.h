/*
 * ExcepcionEscribirFichero.h
 *
 *  Created on: 14 ene. 2020
 *      Author: msabe
 */

#ifndef EXCEPCIONESCRIBIRFICHERO_H_
#define EXCEPCIONESCRIBIRFICHERO_H_

#include <stdexcept>

class ExcepcionEscribirFichero: public std::runtime_error {
public:
	ExcepcionEscribirFichero():runtime_error("Error, no se pudo escribir en el fichero deseado"){};
};

#endif /* EXCEPCIONESCRIBIRFICHERO_H_ */
