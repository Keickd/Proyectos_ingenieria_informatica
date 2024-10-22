/*
 * ExcepcionOverFlowAparcamiento.h
 *
 *  Created on: 14 ene. 2020
 *      Author: msabe
 */

#ifndef EXCEPCIONOVERFLOWAPARCAMIENTO_H_
#define EXCEPCIONOVERFLOWAPARCAMIENTO_H_

#include <stdexcept>

class ExcepcionOverFlowAparcamiento: public std::runtime_error {
public:
	ExcepcionOverFlowAparcamiento():runtime_error("Error el aparcamiento esta lleno, no se puede insertar."){};
};

#endif /* EXCEPCIONOVERFLOWAPARCAMIENTO_H_ */
