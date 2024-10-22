#ifndef ITERACION03_H_INCLUDED
#define ITERACION03_H_INCLUDED

typedef struct {
    char * contenido;
    int longitud;
    int carpeta;
} Cadena;

typedef struct {
    Cadena titulo;
    int leido;
    int carpeta;
    int escrito;
} Correo;

typedef struct {
    int numCorrPorCarpeta;
} VectorCorreosPorCarpeta;

void LineasDelEncabezado(int, int, int, int, int, int, int); //Crear lineas con la medida del encabezado
void ColumnasEncabezado(int, int, int, int, Cadena); //Crea columnas del encabezado
void EscribirNombreUsuario(int, int, int, int, Cadena);
void ConseguirNombre(Cadena * nombreUsuario, FILE *,int);
void ImprimirNombre(Cadena * nombre);
void ConseguirNombreCarpetas(Cadena * carpetasDinamicas, int);
void DibujarCarpetas(int, int, int, int, int, int, Cadena * carpetasDinamicas, int, int, int, int); //Para casos particulares de carpetas y completar tabla
void DibujarLineasCarpetas(int, int, int, int); //Dibuja las líneas que tienen la anchura de las carpetas
void DibujarCorreos(int, int, int, int, int, int, int, int, Correo * correo,int, int, char * leido, int, Cadena * carpetasDinamicas); //Para casos particulares de los correos y completar tabla
void DibujarLineasCorreos(int, int, int, int, int, int); //Dibujar las líneas de la longitud de los correos
void ColumnasDelCuerpo(int, int, int, Cadena * carpetasDinamicas, int, int, Correo * correos, int, char * simboloLeido, int ,int, int, int *);
//Dibuja las columnas del cuerpo, esta separada del cuerpo ya que sino el codigo era excesivo
void Cuerpo(int, int, int, int, int, int, int, int, int, int, Cadena * carpetasDinamicas, int, Correo * correos, int, char * simboloLeido,int);
void RecogerDatosFichero(FILE *f,Cadena nombreDeUsuario);
void PasarDatosAlFichero(Cadena nombreUsuario ,Cadena *carpetasDinamicas,Correo *correos,int,int);


#endif // ITERACION03_H_INCLUDED
