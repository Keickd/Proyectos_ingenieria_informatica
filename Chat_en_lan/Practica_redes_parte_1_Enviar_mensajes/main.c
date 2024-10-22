/*
 ============================================================================
 Name        : Practica_01.c
 Author      :
 Version     :
 Copyright   : Your copyright notice
 Description : Hello World in C, Ansi-style
 ============================================================================
 */
//7C-76-35-E5-64-0B
#include <stdio.h>
#include <stdlib.h>
#include <pcap.h>

#define MINIMUM_DATA_TAM 46
#define MAXIMUM_DATA_TAM 1500
#define LONG_MAC_DST 6
#define LONG_MAC_SRC 6
#define LONG_TYPE 2

#define DST_MAC_ADDRESS 0xFF

#define SRC_MAC_ADDRESS_0 0x7C
#define SRC_MAC_ADDRESS_1 0x76
#define SRC_MAC_ADDRESS_2 0x35
#define SRC_MAC_ADDRESS_3 0xE5
#define SRC_MAC_ADDRESS_4 0x64
#define SRC_MAC_ADDRESS_5 0x0B

#define ETHER_TYPE_HEX_VALUE 0x22

struct{
    int tam;
    char * myChain;
}typedef Chain;

int enterAMessage(Chain *c);
int getLongFromPacket(Chain c);
void fillPackets(unsigned char *p, Chain c);
pcap_if_t * listInterfaces(pcap_if_t *alldevs, pcap_if_t *d, char errbuf[PCAP_ERRBUF_SIZE], int*);
pcap_if_t * chooseAnInterface(pcap_if_t *alldevs, pcap_if_t *d, int , int);
void secondMenu(unsigned char *p,char *interfaceChosen, Chain c);
void menu();

int main(){
    printf("Welcome to our send messages program!!!");
    menu();
    printf("\n\nHave a nice day :)");
    return 0;
}

int enterAMessage(Chain *c){
    char chainAux[MAXIMUM_DATA_TAM + MAXIMUM_DATA_TAM];
    int i = 0;
    printf("\n\nPlease, introduce a message with less chars than 1500: ");
    fflush(stdin);
    gets(chainAux);
    chainAux[strlen(chainAux)] = '\0';

    if(strlen(chainAux) < MINIMUM_DATA_TAM)
        c->tam = MINIMUM_DATA_TAM;
    else if(strlen(chainAux) > MAXIMUM_DATA_TAM)
        return -1;
    else
        c->tam = strlen(chainAux) + 1;

    c->myChain = (char*)malloc(sizeof(char)*c->tam);

    for(i = 0;i < c->tam - 1;i++)
        c->myChain[i] = '\0';
    i = 0;
    while((strlen(chainAux) + i) < MINIMUM_DATA_TAM){
        chainAux[strlen(chainAux) + i] = '\0';
        i++;
    }
   /* i = 0;
    while((strlen(chainAux) + i) < MAXIMUM_DATA_TAM){
        chainAux[strlen(chainAux) + i] = '\0';
        i++;
    }*/

    //strcpy(c->myChain, chainAux);

    for(i = 0;i < strlen(chainAux);i++)
        c->myChain[i] = chainAux[i];

    c->myChain[c->tam - 1] = '\0';
}

int getLongFromPacket(Chain c){
    return LONG_MAC_DST + LONG_MAC_SRC + LONG_TYPE + c.tam;
}

void fillPackets(unsigned char *p, Chain c){
    int i;
    printf("\nTAM PACKET: %d", getLongFromPacket(c));

    //destination mac address
    p[0] = DST_MAC_ADDRESS;
    p[1] = DST_MAC_ADDRESS;
    p[2] = DST_MAC_ADDRESS;
    p[3] = DST_MAC_ADDRESS;
    p[4] = DST_MAC_ADDRESS;
    p[5] = DST_MAC_ADDRESS;

    //source mac address
    p[6] = SRC_MAC_ADDRESS_0;
    p[7] = SRC_MAC_ADDRESS_1;
    p[8] = SRC_MAC_ADDRESS_2;
    p[9] = SRC_MAC_ADDRESS_3;
    p[10] = SRC_MAC_ADDRESS_4;
    p[11] = SRC_MAC_ADDRESS_5;

    //Ethertype
    p[12] = ETHER_TYPE_HEX_VALUE;
    p[13] = ETHER_TYPE_HEX_VALUE;

    //saving the message
    int runner = 0; //run the chain
    for(i = (LONG_MAC_DST + LONG_MAC_SRC + LONG_TYPE);i < c.tam + (LONG_MAC_DST + LONG_MAC_SRC + LONG_TYPE);i++){
        p[i] = c.myChain[runner];
        runner++;
    }
}
int sendmess(char* ar, unsigned char *packet, Chain chain)
{
	pcap_t *fp;
	char errbuf[PCAP_ERRBUF_SIZE];

	printf("\nSending message\n");

	/* Open the adapter */
	if ((fp = pcap_open_live(ar,		// name of the device
							 65536,			// portion of the packet to capture. It doesn't matter in this case
							 1,				// promiscuous mode (nonzero means promiscuous)
							 1000,			// read timeout
							 errbuf			// error buffer
							 )) == NULL)
	{
		fprintf(stderr,"\nUnable to open the adapter. %s is not supported by WinPcap\n", ar);
		return 2;
	}

	else
		printf("\nThe adapter %s is open\n", ar);
    fp = pcap_open_live(ar,		// name of the device
							 65536,			// portion of the packet to capture. It doesn't matter in this case
							 1,				// promiscuous mode (nonzero means promiscuous)
							 1000,			// read timeout
							 errbuf			// error buffer
							 );

	/* Send down the packet */
	if (pcap_sendpacket(fp,	// Adapter
		packet,				// buffer with the packet
		getLongFromPacket(chain)					// size
		) != 0){
		fprintf(stderr,"\nError sending the packet: %s\n", pcap_geterr(fp));
		return 3;
	}
	printf("Packet sent");
	pcap_close(fp);
	return 0;
}

pcap_if_t * listInterfaces(pcap_if_t *alldevs, pcap_if_t *d, char errbuf[PCAP_ERRBUF_SIZE], int *i){
    *i = 0;
    printf("The program is showing the list of interfaces: \n\n");
    // Retrieve the device list from the local machine
    if (pcap_findalldevs(&alldevs, errbuf) == -1)
    {
        fprintf(stderr,"Error in pcap_findalldevs_ex: %s\n", errbuf);
        return 1;
    }
    // Print the list
    for(d= alldevs; d != NULL; d= d->next)
    {
        printf("%d. %s", ++(*i), d->name);
        if (d->description)
            printf(" (%s)\n", d->description);
        else
            printf(" (No description available)\n");
    }
    if (*i == 0){
        printf("\nNo interfaces found! Make sure WinPcap is installed.\n");
        return 0;
    }
    printf("\n");
    return alldevs;
}

pcap_if_t * chooseAnInterface(pcap_if_t *alldevs, pcap_if_t *d, int interfaceToUse, int i){
    do{
         printf("\nNow, you have to choose an interface: ");
         scanf("%d", &interfaceToUse);
    }while(interfaceToUse <= 0 || interfaceToUse > i);//i is the number of interfaces
    interfaceToUse--;
    printf("\n");

    d = alldevs;
    for(int cont = 0; cont <= interfaceToUse;cont++){
        if(cont == interfaceToUse){
            printf("You chase: \n%s\t%s\n",d->name,d->description);
            return d;
        }
        d=d->next;
    }
}

void secondMenu(unsigned char *p,char *interfaceChosen, Chain c){
    char answer2;
    do{
        fflush(stdin);
        printf("\n\t Enter 's' or 'S' to write and send other message.");
        printf("\n\t Enter 'n' or 'N' to come back.");
        printf("\n\tYour choice: ");
        scanf("%c",&answer2);
        switch(answer2){
            case 's':
                if(enterAMessage(&c) == -1){
                    printf("The message is too long, please enter other message less than %d", MAXIMUM_DATA_TAM);
                    break;
                }
                fillPackets(p, c);
                sendmess(interfaceChosen, p, c);
                printf("\n");
                break;
            case 'S':
                if(enterAMessage(&c) == -1){
                    printf("The message is too long, please enter other message less than %d", MAXIMUM_DATA_TAM);
                    break;
                }
                fillPackets(p, c);
                sendmess(interfaceChosen, p, c);
                printf("\n");
                break;
            case 'n':
                break;
            case 'N':
                break;
            default:
                printf("Enter one of the previous options:\n\n ");
                system("PAUSE");
                system("CLS");
        }
    }while(answer2 != 'n' && answer2 != 'N');
}

void menu(){
    pcap_if_t *alldevs;
    pcap_if_t *d;
    int i = 0, interfaceToUse = 0;
    char errbuf[PCAP_ERRBUF_SIZE], answer1;
    char* ar;
    Chain chain;

    do{
        fflush(stdin);
        printf("\n\n-1- To show the list of interfaces and select which one you want.");
        printf("\n-2- To show parameters from the packet to send.");
        printf("\n-0- To exit.");
        printf("\n\nYour choice: ");
        scanf("%c",&answer1);

        interfaceToUse = 0;
        switch(answer1){
            case '1':{
                alldevs = listInterfaces(alldevs, d, errbuf,&i);
                d = chooseAnInterface(alldevs, d, interfaceToUse, i);
                if(enterAMessage(&chain) == -1){
                    printf("The message is too long, please enter other message less than %d", MAXIMUM_DATA_TAM);
                    break;
                }
                unsigned char packet[getLongFromPacket(chain)];
                printf("Now the program is filling the packet.\n");
                fillPackets(packet, chain);
                ar=d->name;
                sendmess(ar, packet, chain);
                printf("\n");
                secondMenu(packet, ar, chain);
                break;
            }
        case '2':
            printf("\n\n-DST MAC ADDRESS: %x-%x-%x-%x-%x-%x",DST_MAC_ADDRESS, DST_MAC_ADDRESS, DST_MAC_ADDRESS, DST_MAC_ADDRESS, DST_MAC_ADDRESS, DST_MAC_ADDRESS);
            printf("\n-SRC MAC ADDRESS: %x-%x-%x-%x-%x-%x",SRC_MAC_ADDRESS_0, SRC_MAC_ADDRESS_1, SRC_MAC_ADDRESS_2, SRC_MAC_ADDRESS_3, SRC_MAC_ADDRESS_4, SRC_MAC_ADDRESS_5);
            printf("\n-Protocol used: 0x%x%x\n\n",ETHER_TYPE_HEX_VALUE, ETHER_TYPE_HEX_VALUE);
            break;
        case '0':
            break;
        default:
            printf("Enter one of the previous options:\n\n ");
            system("PAUSE");
            system("CLS");
        }
     }while(answer1 != '0');
     pcap_freealldevs(alldevs);
}
