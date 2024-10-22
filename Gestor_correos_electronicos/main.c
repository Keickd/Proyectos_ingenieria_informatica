#include <stdio.h>
#include <stdlib.h>
#include <string.h>

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
/////////////
typedef struct {
    int numCorrPorCarpeta;
} VectorCorreosPorCarpeta;
////////////


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

/*
 *  El cuerpo es la parte de la tabla que si y solo si funciona cuando las carpetas y los correos son > 1, en ese caso dibuja un numero de filas
 *  que se ajusta al termino que sea menor, es decir, si las carpetas < correos entonces dibujara x filas como carpetas haya y viceversa
 */

/*  APUNTE IMPORTANTE
 *  En las funciones que dibujan estructuras como la del cuerpo dibujan la estructura pero les falta la línea de abajo,
 *  y con otra función la relleno.
 */

int main()
{
    int tipoLineas = 45; //Indica cómo pueden ser las líneas, si simples o dobles
    int tamannoMaxCarpetas = 0, tamannoMaxCorreos = 0, iterador = 0, miAux = 0, numCorreosAux = 0, leerDeTeclado = 0,aniadirCarp = 0;
    int esqSuperiorIz, esqSuperiorDe, esqInferiorIZ, esqInferiorDe, conectorArriba, conectorAbajo, conectorIzquierda, conectorDerecha, cruz, columnas = 0;
    int numeroDeCapertas, numeroDeCorreos, longitudDeCarpetas, longitudDeCorreos, altura, i = 0, j = 0,l = 0, numCarpetasAux = 0, carpetaQMePiden = 0;//k = 0;
    char respuesta = 0, decisor = 0;
    char * SimboloLeido; //cadenaAux[200]; //Para saber si le ha gustado el programa

/*  APUNTE IMPORTANTE
 *  Todos los carácteres se declaran como enteros para seguir el orden numerico del código ASCII, con los char entran en números negativos
 */
    FILE * miFichero;
    Cadena nombreUsuario;
    printf("Bienvenido al Gestor de Correos Electronicos\n");
    //printf("\nIntroduce el nombre de usuario: ");


    miFichero = fopen("FicheroPractica.txt","r");/////////////////////////////////////////////////////////
    ConseguirNombre(&nombreUsuario,miFichero,leerDeTeclado);
    /*fgets(cadenaAux,200,miFichero);
    nombreUsuario.longitud = strlen(cadenaAux);
    nombreUsuario.contenido=(char*)malloc(nombreUsuario.longitud*sizeof(char));
    for (i = 0; i < nombreUsuario.longitud-1; i++)
        nombreUsuario.contenido[i] = cadenaAux[i];//RECOGE EL USUARIO*/

    /*do{
        printf("\nIntroduce el numero de carpetas: ");
        scanf("%d", &numeroDeCapertas);
    }while(numeroDeCapertas <= 0);*/
    fscanf(miFichero,"%d\n",&numeroDeCapertas);//RECOGE EL NUMERO DE CARPETAS

    //Cadena carpetas;
    Cadena * carpetasDinamicas;
    Correo * correos;
    VectorCorreosPorCarpeta * vectorCorreosPorCarpeta;
    carpetasDinamicas = (Cadena *)malloc(sizeof(Cadena) * numeroDeCapertas); //Almacena las carpetas
    do
    {
        if(aniadirCarp == 0)
        {
            for(i = 0;i < numeroDeCapertas;i++)
            {
                carpetasDinamicas[i].carpeta = i;
            }
            for(i = 0; i<numeroDeCapertas; i++)
            {
               // printf("La carpeta %d se llama: ", i);
                fflush(stdin);
              //  if(aniadirCarp == 0)
                //{
                ConseguirNombre(&carpetasDinamicas[i],miFichero,leerDeTeclado);//tengo q meterlo por teclado habia puesto.contenido
               // }
            }
        }


        for(i = 0; i<numeroDeCapertas; i++)
        {
            if(tamannoMaxCarpetas < carpetasDinamicas[i].longitud) {
                tamannoMaxCarpetas = carpetasDinamicas[i].longitud;
            }
        }

        fscanf(miFichero,"%d\n",&numeroDeCorreos);//RECOGE EL NUMERO DE CORREOS
        if(aniadirCarp == 0)
        {
            correos = (Correo *)malloc(sizeof(Correo) * numeroDeCorreos);

            vectorCorreosPorCarpeta = malloc(sizeof(VectorCorreosPorCarpeta) * numeroDeCapertas);
        }
        SimboloLeido = malloc(sizeof(char) * numeroDeCorreos);


        for(l = 0;l < numeroDeCapertas;l++)
        {
            vectorCorreosPorCarpeta[l].numCorrPorCarpeta = 0;
        }

        for(i = 0; i<numeroDeCorreos; i++)
        {
            if(aniadirCarp == 0)
            {
                fflush(stdin);
                ConseguirNombre(&correos[i].titulo,miFichero,leerDeTeclado);//antes hasbia puesto contenido
          ////////////////  }

          fscanf(miFichero,"%d\n",&correos[i].carpeta);
            }

            if((i < numeroDeCorreos) && (numeroDeCapertas <= numeroDeCorreos))
            {
                vectorCorreosPorCarpeta[correos[i].carpeta].numCorrPorCarpeta++;
            }
            else if((i < numeroDeCapertas) && (numeroDeCapertas > numeroDeCorreos))
            {
                vectorCorreosPorCarpeta[correos[i].carpeta].numCorrPorCarpeta++;
            }


            if(tamannoMaxCorreos < correos[i].titulo.longitud) {
                tamannoMaxCorreos = correos[i].titulo.longitud;
            }

            do
            {
                fscanf(miFichero,"%d\n",&correos[i].leido);
                if(correos[i].leido == 1) {
                    SimboloLeido[i] = 'X';
                }
                else {
                    SimboloLeido[i] = ' ';
                }
            }while((correos[i].leido != 0) && (correos[i].leido != 1));

        }

        longitudDeCarpetas = tamannoMaxCarpetas + 1;
        longitudDeCorreos = tamannoMaxCorreos;
        do
        {
            if(numeroDeCapertas != 0)
            {
                do
                {
                    printf("\nQue carpeta quieres ver? :");
                    scanf("%d",&carpetaQMePiden);
                } while (carpetaQMePiden >= numeroDeCapertas || carpetaQMePiden < 0);
            }
            else
            {
                do
                {
                    printf("\nQue carpeta quieres ver? :");
                    scanf("%d",&carpetaQMePiden);
                } while (carpetaQMePiden != 0);
            }

            numCorreosAux = numeroDeCorreos;
            miAux = vectorCorreosPorCarpeta[carpetaQMePiden].numCorrPorCarpeta;

            if(numeroDeCapertas < numCorreosAux)
            {
                numeroDeCorreos = miAux;
            }
            else if(numeroDeCapertas >= numCorreosAux)
            {
                numeroDeCorreos = miAux;
            }

            do {
                do {
                    printf("Escibe '-' si quieres que las lineas sean simples o '=' para que sean continuas :");
                    fflush(stdin);
                    scanf("%c", &tipoLineas);
                } while((tipoLineas != '-') && (tipoLineas != '='));

                //Según el tipo de línea seleccionado se establecen los carácteres correspondientes

                if(tipoLineas == '-') {
                    tipoLineas = 196;
                    esqSuperiorIz = 218;
                    esqSuperiorDe = 191;
                    esqInferiorIZ = 192;
                    esqInferiorDe = 217;
                    conectorArriba = 194;
                    conectorAbajo = 193;
                    conectorIzquierda = 195;
                    conectorDerecha = 180;
                    cruz = 197;
                    columnas = 179;
                }
                else if(tipoLineas == '=') {
                    tipoLineas = 205;
                    esqSuperiorIz = 201;
                    esqSuperiorDe = 187;
                    esqInferiorIZ = 200;
                    esqInferiorDe = 188;
                    conectorArriba = 203;
                    conectorAbajo = 202;
                    conectorIzquierda = 204;
                    conectorDerecha = 185;
                    cruz = 206;
                    columnas = 186;
                }
                for (i = 1; i <= 4; i++) {
                    printf("\n"); //Dejar hueco
                }

                //Con los carácteres definidos, creamos el encabezado
                LineasDelEncabezado(esqSuperiorIz, tipoLineas, tipoLineas, esqSuperiorDe, tipoLineas, longitudDeCarpetas, longitudDeCorreos);
                printf("\n");
                altura = 5;
                ColumnasEncabezado(columnas, altura, longitudDeCarpetas, longitudDeCorreos, nombreUsuario);
                EscribirNombreUsuario(columnas, altura, longitudDeCarpetas, longitudDeCorreos, nombreUsuario);
                fclose(miFichero);
                //if(miAux == 0)
                if ((numeroDeCapertas == 0)&&(numeroDeCorreos == 0)) { //Se valoran todos los casos y combinaciones de carpetas y correos
                    LineasDelEncabezado(esqInferiorIZ, tipoLineas, tipoLineas, esqInferiorDe, tipoLineas, longitudDeCarpetas, longitudDeCorreos);
                    //Teniendo la linea de arriba y justo debajo las columnas, para este caso solo falta la linea de abajo
                }
                else if ((numeroDeCapertas>0) && (numeroDeCorreos == 0))
                {
                    LineasDelEncabezado(conectorIzquierda, conectorArriba, tipoLineas, esqInferiorDe, tipoLineas, longitudDeCarpetas, longitudDeCorreos);
                    //Cierra el encabezado
                    printf("\n");
                    DibujarCarpetas(columnas, numeroDeCapertas, longitudDeCarpetas, conectorIzquierda, conectorDerecha, tipoLineas, carpetasDinamicas, tamannoMaxCarpetas, iterador, numeroDeCorreos, carpetaQMePiden);
                    //Dibujo los correos restantes para que acabe la tabla
                    DibujarLineasCarpetas(esqInferiorIZ, esqInferiorDe, longitudDeCarpetas, tipoLineas);
                    //Y finalizo dibujando la linea final con longitud de carpetas
                }
                else if ((numeroDeCapertas>0) && (numeroDeCorreos>0)) { //Habrá como mínimo un correo y una carpeta
                    LineasDelEncabezado(conectorIzquierda, conectorArriba, conectorArriba, conectorDerecha, tipoLineas, longitudDeCarpetas, longitudDeCorreos);
                    printf("\n");
                    //En el cuerpo queremos imprimir el menor numero de elementos comunes
                    Cuerpo(numeroDeCapertas, numeroDeCorreos,conectorIzquierda, cruz, cruz, conectorDerecha, longitudDeCarpetas, longitudDeCorreos, tipoLineas, columnas,carpetasDinamicas,tamannoMaxCarpetas,correos,carpetaQMePiden,SimboloLeido, tamannoMaxCorreos);

                    /*
                     *  Para los 3 casos tanto la finalizacion del encabezado como el cuerpo (que era la parte de la tabla que esta si o si) es comun
                     *  A partir de aqui destacan los 3 casos de que uno se mayor que otro igual o menor
                     */

                    if (numeroDeCapertas > numeroDeCorreos) {
                        LineasDelEncabezado(conectorIzquierda, cruz, conectorAbajo, esqInferiorDe, tipoLineas, longitudDeCarpetas, longitudDeCorreos);
                        printf("\n");
                        numCarpetasAux = numeroDeCapertas;
                        numeroDeCapertas = numeroDeCapertas - numeroDeCorreos;
                        //De esta forma averiguo cuantes carpetas faltan por rellenar para acabar la tabla
                        iterador = numeroDeCorreos;
                        DibujarCarpetas(columnas, numCarpetasAux, longitudDeCarpetas, conectorIzquierda, conectorDerecha, tipoLineas, carpetasDinamicas, tamannoMaxCarpetas, iterador, numeroDeCorreos, carpetaQMePiden);
                        DibujarLineasCarpetas(esqInferiorIZ, esqInferiorDe, longitudDeCarpetas, tipoLineas);
                        numeroDeCapertas = numeroDeCapertas + numeroDeCorreos; //Para evitar crearse valores más pequeños y que falle el programa
                    }

                    else if (numeroDeCapertas < numeroDeCorreos) {
                        LineasDelEncabezado(esqInferiorIZ, cruz, cruz, conectorDerecha, tipoLineas, longitudDeCarpetas, longitudDeCorreos);
                        printf("\n");

                        iterador = numeroDeCapertas;
                        numeroDeCorreos = numeroDeCorreos - numeroDeCapertas; //Hallamos los correos restantes

                        DibujarCorreos(numeroDeCorreos, longitudDeCarpetas, columnas, longitudDeCorreos, conectorIzquierda, cruz, conectorDerecha, tipoLineas, correos, iterador, numeroDeCapertas, SimboloLeido, carpetaQMePiden, carpetasDinamicas);
                        DibujarLineasCorreos(esqInferiorIZ, conectorAbajo, esqInferiorDe, longitudDeCarpetas, longitudDeCorreos, tipoLineas);
                        numeroDeCorreos = numeroDeCorreos + numeroDeCapertas;
                    }
                    else { //En el caso que carpetas == correos, se cierra el cuerpo
                        LineasDelEncabezado(esqInferiorIZ, conectorAbajo, conectorAbajo, esqInferiorDe, tipoLineas, longitudDeCarpetas, longitudDeCorreos);
                    }
                }
                numeroDeCorreos = numCorreosAux;

                iterador = 0;
                printf("\n");

                printf("Ahora puedes cambiar el tipo de lineas para ello presiona 'n' o 'N', si presionas otra tecla accederas a elegir las carpetas :\n");
                fflush(stdin); //Al pulsar el ultimo 'enter' se guarda en el bufer del teclado, para evitarlo esta ese comando
                scanf("%c",&respuesta);
            } while((respuesta == 'n')||(respuesta == 'N'));
            printf("\nQuieres ver otra carpeta? en ese caso introduce 's' o 'S' :");
            scanf("%c",&respuesta);
        }while(respuesta == 's' || respuesta == 'S');

        do
        {
////////////////////////////////////////////////////////////////////////////////////////////
            printf("Si quieres editar las carpetas pulsa 'c'");
            printf("\nSi quieres editar los correos pulsa 'C'");
            printf("\nSi quieres salir pulsa 'x' :");
            scanf("%c",&respuesta);
        }while((respuesta != 'c') && (respuesta != 'C') && (respuesta != 'x'));
        if(respuesta == 'c')
        {
            do
            {
                fflush(stdin);
                printf("\nSi quieres aniadir una carpeta pulsa '+', si quieres eliminar pulsa '-' :");
                scanf("%c",&decisor);
            }while((decisor != '+') && (decisor != '-'));
            if(decisor == '+')//aniadir carpetas
            {
                numeroDeCapertas++;
               carpetasDinamicas = realloc(carpetasDinamicas,numeroDeCapertas * sizeof(Cadena));
               printf("\nIntroduce el nombre de la carpeta nueva :");
               fflush(stdin);
               leerDeTeclado = 1;
               ConseguirNombre(&carpetasDinamicas[numeroDeCapertas - 1], miFichero,leerDeTeclado);//habia puesto contenido
               carpetasDinamicas[numeroDeCapertas - 1].carpeta = numeroDeCapertas - 1;
                leerDeTeclado = 0;
                aniadirCarp = 1;
            }
            else if(decisor == '-')
            {
                numeroDeCapertas--;
                carpetasDinamicas =realloc(carpetasDinamicas,numeroDeCapertas * sizeof(Cadena));
                aniadirCarp = 1;
            }
        }
        else if(respuesta == 'C')
        {
            do
            {
                printf("\nSi quieres aniadir un correo pulsa '+' : ");
                fflush(stdin);
                scanf("%c",&decisor);
            }while(decisor != '+');
            if(decisor == '+')//aniadir correos
            {
                numeroDeCorreos++;
                correos = realloc(correos, sizeof(Correo) * numeroDeCorreos);
               // SimboloLeido = realloc(SimboloLeido, sizeof(char) * numeroDeCorreos-1);
                vectorCorreosPorCarpeta = realloc(vectorCorreosPorCarpeta, sizeof(Correo) * numeroDeCapertas);


                printf("\nIntroduce el nombre del correo nuevo :");
                fflush(stdin);
                leerDeTeclado = 1;
                ConseguirNombre(&correos[numeroDeCorreos - 1].titulo, miFichero,leerDeTeclado);//habia puesto contenido
                printf("\nPertenece a la carpeta : ");
                scanf("%d",&correos[numeroDeCorreos - 1].carpeta);
                printf("\nIntroduce 1 para indicar que esta leido,0 en caso contrario : ");
                scanf("%d",&correos[numeroDeCorreos - 1].leido);
                if(correos[numeroDeCorreos - 1].leido == 1)
                {
                    SimboloLeido[numeroDeCorreos - 1] = 'X';
                }
                else
                {
                    SimboloLeido[numeroDeCorreos - 1] = ' ';
                }
                aniadirCarp = 1;
                leerDeTeclado = 0;
                correos[numeroDeCorreos - 1].escrito = 0;
            }
        }
        free(SimboloLeido);
        decisor = 0;
        respuesta = 0;
        aniadirCarp = 1;
        fflush(stdin);
        printf("si quieres repetir el programa introduce 's' : ");
        scanf("%c",&respuesta);

    }while(respuesta == 's');
     PasarDatosAlFichero(nombreUsuario,carpetasDinamicas,correos,numeroDeCapertas,numeroDeCorreos);

    return 0;
}

//FUNCIONES
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
    miFichero1 = fopen("FicheroPractica1.txt","w");
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
