#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "iteracion03.h"

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


void LineasDelEncabezado(int caracter1, int caracter2, int caracter3, int caracter4, int lineas, int longCarpetas, int longCorreos) {
    int i = 1;
    printf("%c",caracter1);
    for (i = 1; i <= longCarpetas; i++) { //Es -2 ya que en esa distancia el primer char y el ultimo no cuentan
        printf("%c",lineas);
    }
    printf("%c",caracter2);

    for (i = 1; i <= longCorreos; i++) { //Aqui no se resta ninguno porque ya hemos quitado uno arriba y otro abajo
        printf("%c",lineas);
    }
    printf("%c",caracter3);

    for (i = 1; i <= 5-2; i++) {
        printf("%c",lineas);
    }
    printf("%c",caracter4);
}

void ColumnasEncabezado(int columnas, int alt, int longCarpetas, int longCorreos, Cadena nombreUsuario) {
    int i, j = 1;
    for (j = 1; j < alt-2; j++) {
        printf("%c", columnas);
        for (i = 1; i <= longCarpetas + longCorreos + 5 ; i++) {
            printf(" ");
        }
        printf("%c", columnas);
        printf("\n");
    }
}

void EscribirNombreUsuario(int columnas, int alt, int longCarpetas, int longCorreos,Cadena nombreUsuario)
{
    int i = 1,longituEncabezado = 0,paridad = 0;//j = 0;

    printf("%c",columnas);
    longituEncabezado = longCarpetas + longCorreos + 5;
    for(i = 0;i < (longituEncabezado - nombreUsuario.longitud) / 2;i++)
    {
        printf(" ");
    }
    for(i = 0;i < nombreUsuario.longitud;i++)
    {
        printf("%c",nombreUsuario.contenido[i]);
    }

    if(((longituEncabezado - nombreUsuario.longitud) % 2) == 1)
    {
        paridad++;
    }
    for(i = 0;i < (longituEncabezado - nombreUsuario.longitud) / 2 + paridad;i++)
    {
        printf(" ");
    }

    printf("%c\n",columnas);
}
void DibujarLineasCarpetas(int caracter1, int caracter2, int longCarp, int lineas) { //Completa las líneas de las carpetas
    int i = 1;
    printf("%c", caracter1); //Esquina inferior izquierda
    for (i = 1; i <= longCarp; i++) {
        printf("%c", lineas);
    }
    printf("%c", caracter2); //Esquina inferior derecha
    printf("\n");
}

void DibujarCarpetas(int columnas, int numCarp, int longCarp, int caracter1, int caracter2, int lineas, Cadena *carpetasDinamicas, int tamMax, int it, int numCorr, int carpQMePiden) {
//Dibuja las carpetas y sus lineas menos la ultima// dibuja una semi casilla mas por error
    int i = 0, j = 0, h = 0, contadorCarpetas = 0;


    for (j = it; j < numCarp; j++) { //it = numCorreos

        printf("%c", columnas);

        if(carpQMePiden == carpetasDinamicas[j].carpeta)
        {
            printf("*");
        }
        else
        {
            printf(" ");
        }

        for(h = 0; h < carpetasDinamicas[j].longitud; h++) {
            printf("%c",carpetasDinamicas[j].contenido[h]);
        }

        if(carpetasDinamicas[j].longitud < longCarp) //Que todas las columnas tengan la misma longitud
        {
            for(i = carpetasDinamicas[j].longitud; i < longCarp - 1; i++) {
                printf(" ");
            }
        }
        printf("%c", columnas);
        printf("\n");
        contadorCarpetas++;

        if(contadorCarpetas < numCarp - numCorr) {
            DibujarLineasCarpetas(caracter1, caracter2, longCarp, lineas); //Dibuja conectores entre las columnas
        }
    }
}

void DibujarLineasCorreos(int caracter1, int caracter2, int caracter3, int longCarp, int longCorr, int lineas) {
    int i = 0;
    for (i = 0; i <= longCarp; i++) {
        printf(" ");
    }
    printf("%c", caracter1); //Esquina inferior izquierda
    for (i = 0; i < longCorr; i++) {
        printf("%c", lineas);
    }
    printf("%c", caracter2); //Conector bajo
    for (i = 1; i <= 3; i++) {
        printf("%c", lineas);
    }
    printf("%c", caracter3); //Esquina inferior derecha
    printf("\n");
}

void DibujarCorreos(int numCorr, int longCarp, int columnas, int longCorr, int caracter1, int caracter2, int caracter3, int lineas,  Correo * correo, int it, int numCarp, char * leido, int carpQMePiden, Cadena * carpetasDinamicas) { //Dibuja los correos menos la ultima linea
    //caracter1 = conector izquierdo, caracter2 = cruz, caracter3 = conector derecho;
    int i = 0, j = 0, iterador = -1;
    int  h = 0, m = 0, contadorCorreos = 0, contadorEspacios = 0;//l = 0;k = 0;
   // k = numCorr + numCarp;

    contadorEspacios = 0;
    for(j = 0;j < numCorr + numCarp;j++)
    {
        while(correo[m].carpeta != carpQMePiden)
        {
            m++;
        }

       if(iterador >= numCarp - 1)
       {
            for(i = 0;i <= longCarp;i++)
            {
                printf(" ");
                contadorEspacios++;
            }
            printf("%c", columnas);

            for(h = 0; h < correo[m].titulo.longitud; h++)
            {
                printf("%c",correo[m].titulo.contenido[h]);
            }
            if(correo[m].titulo.longitud < longCorr) //Que todas las columnas tengan la misma longitud
            {
                for(i = correo[m].titulo.longitud; i < longCorr; i++)
                {
                    printf(" ");
                }
            }
            printf("%c ",columnas);
            printf("%c",leido[m]);
            printf(" %c",columnas);
            printf("\n");
            contadorCorreos++;

            if(contadorCorreos < numCorr)
            {
                DibujarLineasCorreos(caracter1, caracter2, caracter3, longCarp, longCorr, lineas);
            }
        }
        iterador++;
        m++;
    }
}

void ColumnasDelCuerpo(int columnas, int longCarp, int longCorr, Cadena * carpetasDinamicas, int tamMax, int it, Correo *correos,int carpQmePiden, char *leido, int j, int numCarp, int numCorr, int * m) { //Dibuja las columnas a lo largo de la funcion 'Cuerpo'

   int i = 0;//a = 0;contador = 0;l = 0;
    printf("%c",columnas);

    if(carpQmePiden == carpetasDinamicas[j].carpeta)/////////////////////////////////////////////////////////////////////////////////////
    {
        printf("*");
    }
    else
    {
        printf(" ");
    }
    for(i = 0;i < carpetasDinamicas[it].longitud ;i++)
    {
        printf("%c",carpetasDinamicas[it].contenido[i]);
    }
    if(carpetasDinamicas[it].longitud < tamMax)
    {
        for(i = carpetasDinamicas[it].longitud;i < tamMax;i++)
            {
                printf(" ");
            }
    }
    printf("%c",columnas);

    while(correos[*m].carpeta != carpQmePiden)
    {
        *m+=1;
    }

    for(i = 0;i < correos[*m].titulo.longitud;i++)
    {
        printf("%c",correos[*m].titulo.contenido[i]);
    }
    for(i = correos[*m].titulo.longitud;i < longCorr;i++)
    {
        printf(" ");
    }
    printf("%c ",columnas);
    printf("%c",leido[*m]);
    printf(" %c",columnas);

    correos[*m].escrito = 1;
   *m+=1;
}

void Cuerpo(int numCarp, int numCorr, int caracter1, int caracter2, int caracter3, int caracter4, int longCarp, int longCorr, int lineas, int columnas, Cadena *carpetasDinamicas, int tamMax, Correo *correos, int carpQmePiden, char * SimboloLeido,int tamMaxCorr)
{ //Dibuja aquella parte comun en la tabla, siendo limitado al menor valor de carpetas o correos
    int k = numCorr, j = 0,i = 0,m = 0;//h = 0;l = 0;
    int iterador = 0;

    for(i = 0;i < numCarp + numCorr;i++)
    {
        correos[i].escrito = 0;
    }

    if (numCarp >= numCorr) {
        do {

            ColumnasDelCuerpo(columnas, longCarp, longCorr, carpetasDinamicas,tamMax, iterador, correos, carpQmePiden, SimboloLeido, j, numCarp, numCorr,&m);
            printf("\n");
            j++;
            iterador++;
            if (k > 1) {
                LineasDelEncabezado(caracter1, caracter2, caracter3, caracter4, lineas, longCarp, longCorr);
                printf("\n");
            }
            k--;
       } while(k > 0);
    }
    else if (numCarp < numCorr) {
         k = numCarp;
        do {
            ColumnasDelCuerpo(columnas, longCarp, longCorr, carpetasDinamicas, tamMax, iterador, correos, carpQmePiden, SimboloLeido, j,numCarp,numCorr,&m);
            j++;
            iterador++;
            printf("\n");
            if (k > 1) {
                LineasDelEncabezado(caracter1, caracter2, caracter3, caracter4, lineas, longCarp, longCorr);
                printf("\n");

            // * Lo que hago con este bucle es para que se dibujen tantas lineas como digan la funcion principal menos 1 y eso lo hago porque
           //  * la ultima linea es diferente en cada paso y la completo con 'LineasEncabezado'
           //  *
            }
            k--;
       } while(k > 0);
    }
}

void ConseguirNombre(Cadena * nombre, FILE *miArchivo, int leerDeTeclado) {
    int i = 0;
    char CadenaAux[100];
    if (miArchivo == NULL || leerDeTeclado == 1)
        gets(CadenaAux);
    else if (miArchivo){
        fscanf(miArchivo,"%[^\n]",CadenaAux);
        fgetc(miArchivo);
    }
    nombre->longitud = strlen(CadenaAux);
    nombre->contenido = (char *)malloc(sizeof(char) * nombre->longitud);

    for(i = 0; i < nombre->longitud; i++) {
        nombre->contenido[i] = CadenaAux[i];
    }
}

void PasarDatosAlFichero(Cadena nombreUsuario,Cadena *carpetasDinamicas,Correo *correos,int numCarp,int numCorr)
{
    FILE * miFichero1;
    int i = 0,j = 0;
    miFichero1 = fopen("FicheroPractica.txt","w");
    for(i = 0;i < nombreUsuario.longitud;i++)
    {
        fprintf(miFichero1,"%c",nombreUsuario.contenido[i]);
    }
    fprintf(miFichero1,"\n%d\n",numCarp);
    for(i = 0;i < numCarp;i++)
    {
        for(j = 0;j < carpetasDinamicas[i].longitud;j++)
        {
            fprintf(miFichero1,"%c",carpetasDinamicas[i].contenido[j]);
        }
        fprintf(miFichero1,"\n");
    }
    fprintf(miFichero1,"%d\n",numCorr);
    for(i = 0;i < numCorr;i++)
    {
        for(j = 0;j < correos[i].titulo.longitud;j++)
        {
            fprintf(miFichero1,"%c",correos[i].titulo.contenido[j]);
        }
        fprintf(miFichero1,"\n%d",correos[i].carpeta);
        fprintf(miFichero1,"\n%d\n",correos[i].leido);
    }
    fclose(miFichero1);
}
