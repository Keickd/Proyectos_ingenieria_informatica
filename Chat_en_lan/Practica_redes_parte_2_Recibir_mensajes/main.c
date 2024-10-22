#include "pcap.h"

#define DST_MAC_ADDRESS 0xFF
#define ETHER_TYPE_HEX_VALUE 0x22

void menu();
pcap_if_t * listInterfaces(pcap_if_t *alldevs, pcap_if_t *d, char errbuf[PCAP_ERRBUF_SIZE], int *i);
pcap_if_t * chooseAnInterface(pcap_if_t *alldevs, pcap_if_t *d, int, int, int);
void openAdapterAndScanAndFilterPackets(int res,pcap_if_t * alldevs, pcap_if_t * d,char errbuf[PCAP_ERRBUF_SIZE]);

int main()
{
    printf("Welcome to our receive messages program!!!");
    menu();
    return 0;
}

void menu(){
    pcap_if_t *alldevs;
    pcap_if_t *d;
    int inum, i = 0, interfaceToUse = 0, res;
    pcap_t *adhandle;
    char errbuf[PCAP_ERRBUF_SIZE];
    struct tm *ltime;
    char timestr[16], answer1;


    do{
         fflush(stdin);
        printf("\n\n-1- To show the list of interfaces and select which one you want.");
        printf("\n-0- To exit.");
        printf("\n\nYour choice: ");
        scanf("%c",&answer1);

        interfaceToUse = 0;
        switch(answer1){
            case '1':
                alldevs = listInterfaces(alldevs, d, errbuf, &i);
                d = chooseAnInterface(alldevs, d, interfaceToUse, i, inum);
                openAdapterAndScanAndFilterPackets(res, alldevs, d, errbuf);
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

pcap_if_t * listInterfaces(pcap_if_t *alldevs, pcap_if_t *d, char errbuf[PCAP_ERRBUF_SIZE], int *i){
    *i=0;
      //Retrieve the device list on the local machine
    if (pcap_findalldevs(&alldevs, errbuf) == -1){
        fprintf(stderr,"Error in pcap_findalldevs: %s\n", errbuf);
        return -1;
    }
    printf("\n");
    /* Print the list */
    for(d=alldevs; d; d=d->next){
        printf("%d. %s", ++(*i), d->name);
        if (d->description)
            printf(" (%s)\n", d->description);
        else
            printf(" (No description available)\n");
    }

    if(*i==0){
        printf("\nNo interfaces found! Make sure WinPcap is installed.\n");
        return -1;
    }
    return alldevs;
}

pcap_if_t * chooseAnInterface(pcap_if_t *alldevs, pcap_if_t *d, int interfaceToUse, int i, int inum){
    printf("\nEnter the interface number (1-%d):",i);
    scanf("%d", &inum);

    if(inum < 1 || inum > i)
    {
        printf("\nInterface number out of range.\n");
        /* Free the device list */
        pcap_freealldevs(alldevs);
        return -1;
    }

    /* Jump to the selected adapter */
    for(d=alldevs, i=0; i< inum-1 ;d=d->next, i++);
    return d;
}



void openAdapterAndScanAndFilterPackets(int res,pcap_if_t * alldevs, pcap_if_t * d,char errbuf[PCAP_ERRBUF_SIZE]){
    pcap_t * adhandle;
    struct pcap_pkthdr * header;
    const unsigned char * pkt_data;

    /* Open the device */
    if ( (adhandle= pcap_open_live(d->name,     // name of the device
                              65536,            // portion of the packet to capture.
                                                // 65536 guarantees that the whole packet will be captured on all the link layers
                              1,                // promiscuous mode
                              1000,             // read timeout
                              errbuf            // error buffer
                              ) ) == NULL)
    {
        fprintf(stderr,"\nUnable to open the adapter. %s is not supported by WinPcap\n", d->name);
        /* Free the device list */
        pcap_freealldevs(alldevs);
        return -1;
    }
    printf("\nlistening on %s...\n", d->description);

    while((res = pcap_next_ex( adhandle, &header, &pkt_data)) >= 0){
        if(res == 0)
            /* Timeout elapsed */
            continue;

        /* convert the timestamp to readable format */
        if(pkt_data[0]==DST_MAC_ADDRESS && pkt_data[1]==DST_MAC_ADDRESS &&pkt_data[2]==DST_MAC_ADDRESS &&
           pkt_data[3]==DST_MAC_ADDRESS && pkt_data[4]==DST_MAC_ADDRESS && pkt_data[5]==DST_MAC_ADDRESS &&
            pkt_data[12]==ETHER_TYPE_HEX_VALUE && pkt_data[13]==ETHER_TYPE_HEX_VALUE){
            for(int i=14;i<strlen(pkt_data);i++){
                printf("%c", pkt_data[i]);
                if(i+1==strlen(pkt_data)){
                    printf("\n");
                }
            }
        }
    }

    if(res == -1){
        printf("Error reading the packets: %s\n", pcap_geterr(adhandle));
        return -1;
    }
}
