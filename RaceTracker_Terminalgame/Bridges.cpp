//241UC24157 YEE SI SHUN

#include "Bridges.h"
#include "RaceTrack.h"


Bridge::Bridge(Track &Track, int goal) {
    int invalidrange = (((goal - 1)/4)*3)+2;
        cout << endl;
        cout << "How many bridge would you like to add (3 to 5): ";
        cin >> BridgeNums;
        while (BridgeNums < 3 || BridgeNums > 5){                 //update :)
            cout << "Please enter a number between 3 and 5: ";
            cin >> BridgeNums;
        }
        cout << "Note: Bridge will not be allow to build between range of box " << invalidrange << " to " << goal <<"(goal)" << endl;
        for (int j = 0; j < BridgeNums; j++) {
            bool validInput = false;
            while (!validInput) {
            validInput = true;
        
            cout << "Start position for Bridge " << j + 1 << " ? (2 to "<< invalidrange-1<< "): ";
            cin >> start[j];

            if (start[j] < 2 || start[j] > invalidrange-1) {
                cout << "Start position must be between 2 and "<<invalidrange-1<<". Try again." << endl;     
                validInput = false;
                continue;
            }

            for (int k = 0; k < j; k++) {
                if (start[j] == start[k] || start[j] == end[k]) {
                    cout << "This position already exist a bridge.Try again." << endl;
                    validInput = false;
                    break;
                }
            }

            if (!validInput) continue;

            cout << "End position for Bridge " << j + 1 << " ? (2 to "<<invalidrange-1<<"): "; //remark
            cin >> end[j];

            if (end[j] == start[j]) {
                cout << "End position cannot be the same as start position. Try again." << endl;
                validInput = false;
                continue;
            }

            if (end[j] < 2 || end[j] > invalidrange-1) {
                cout << "End position must be between 2 to " <<invalidrange-1<< " and different from start position. Try again." << endl;
                validInput = false;
                continue;
            }

            for (int k = 0; k < j; k++) {
                if (end[j] == start[k] || end[j] == end[k]) {
                    cout << "This postion already exist a bridge.Try again." << endl;
                    validInput = false;
                    break;
                    }
                }
            }
        }


        for(int j = 0 ; j < BridgeNums ; j ++){
            //LEFT ROLE
            if(start[j] > 1 && start[j] < ((goal - 1) / 4) + 2){               //7
            int startPos = (Track.height-2)-(start[j]*3);
            //int startPos2 = (Track.height-1)-(start[j]*3);
            Track.track[startPos][1] = 'B';
            Track.track[startPos][2] = 'r';
            Track.track[startPos][3] = 'i';
            Track.track[startPos][4] = 'd';
            Track.track[startPos][5] = 'g';
            Track.track[startPos][6] = 'e';
            Track.track[startPos][7] = ' ';
            Track.track[startPos][8] = '0'+(j+1);

            }

            //Top Role
            if(start[j] > ((goal - 1) / 4) + 1 && start[j] < (((goal - 1) / 4) * 2) + 2){    //6 , 12
            int startPos = ((start[j]-(((goal-1)/4)+1))*9); 
            Track.track[1][1+startPos] = 'B';
            Track.track[1][2+startPos] = 'r';
            Track.track[1][3+startPos] = 'i';
            Track.track[1][4+startPos] = 'd';
            Track.track[1][5+startPos] = 'g';
            Track.track[1][6+startPos] = 'e';
            Track.track[1][7+startPos] = ' ';
            Track.track[1][8+startPos] = '0'+(j+1);
            }

            if(start[j] > (((goal - 1) / 4) * 2) + 1 && start[j] < (((goal - 1) / 4) * 3) + 2){     //11,17
                int startPos = ((start[j]-((((goal-1)/4)*2)+1))*3);          //12-11
                Track.track[1+startPos][Track.width-9] = 'B';
                Track.track[1+startPos][Track.width-8] = 'r';
                Track.track[1+startPos][Track.width-7] = 'i';
                Track.track[1+startPos][Track.width-6] = 'd';
                Track.track[1+startPos][Track.width-5] = 'g';
                Track.track[1+startPos][Track.width-4] = 'e';
                Track.track[1+startPos][Track.width-3] = ' ';
                Track.track[1+startPos][Track.width-2] = '0'+(j+1);

            }
        }

        //print end position
           for(int j = 0 ; j < BridgeNums ; j ++){ 
                if(end[j] > 1 && end[j] < ((goal - 1) / 4) + 2){                 
                int endPos = (Track.height-2)-(end[j]*3);      
                Track.track[endPos][1] = 'E';
                Track.track[endPos][2] = 'n';
                Track.track[endPos][3] = 'd';
                Track.track[endPos][4] = ' ';
                Track.track[endPos][5] = 'o';
                Track.track[endPos][6] = 'f';
                Track.track[endPos][7] = ' ';
                Track.track[endPos][8] = '0'+(j+1);
            }

                if(end[j] > ((goal - 1) / 4)+1 && end[j] < ((((goal - 1) / 4)*2) + 2)){   //6,12
                    int endPos = ((end[j]-(((goal-1)/4)+1))*9); 
                    Track.track[1][1+endPos] = 'E';     
                    Track.track[1][2+endPos] = 'n';
                    Track.track[1][3+endPos] = 'd';
                    Track.track[1][4+endPos] = ' ';
                    Track.track[1][5+endPos] = 'o';
                    Track.track[1][6+endPos] = 'f';
                    Track.track[1][7+endPos] = ' ';
                    Track.track[1][8+endPos] = '0'+(j+1);
                }

                if(end[j] > ((((goal - 1) / 4) * 2)+1) && end[j] < (((goal - 1) / 4) * 3) + 2){   //11,17
                int endPos = ((end[j]-((((goal - 1) / 4) * 2)+1))*3);    // (12-11)*3          
                Track.track[1+endPos][Track.width-9] = 'E';
                Track.track[1+endPos][Track.width-8] = 'n';
                Track.track[1+endPos][Track.width-7] = 'd';
                Track.track[1+endPos][Track.width-6] = ' ';
                Track.track[1+endPos][Track.width-5] = 'o';
                Track.track[1+endPos][Track.width-4] = 'f';
                Track.track[1+endPos][Track.width-3] = ' ';
                Track.track[1+endPos][Track.width-2] = '0'+(j+1);
            }

        }

        //print bridge design
        for(int j = 0 ; j < BridgeNums ; j ++){ 
            if((start[j] > 1 && start[j] < ((goal - 1) / 4) + 2) && (end[j] > ((goal - 1) / 4)+1 && end[j] < ((((goal - 1) / 4)*2) + 2)) ){
                int linep = 4+((end[j]-(((goal - 1) / 4)+1))*9);
                int linet = (((goal - 1) / 4) + 2) - start[j]; 
                int dline = 2+(linet*3);
                for(int x=4;x<dline;x++){
                    Track.track[x][linep] = '|';
                }
                for(int i=10;i<linep;i++){
                    Track.track[dline][i] = '-';
                    Track.track[dline][linep] = '+';
                }

            }

            if((start[j] > 1 && start[j] < ((goal - 1) / 4) + 2) && (end[j] > 1 && end[j] < ((goal - 1) / 4) + 2)){
                if(start[j] < end[j]){
                    int startP =  (Track.height-1)-(start[j]*3);
                    int endP = (Track.height-1)-(end[j]*3);
                    for(int x=endP;x < startP;x++){
                        Track.track[x][14] = '|';
                    }
                    for(int i=10;i<14;i++){
                        Track.track[endP][i] = '-';
                        Track.track[endP][14] = '+';
                        Track.track[startP][i] = '-';
                        Track.track[startP][14] = '+';
                    }
                }
                if(start[j] > end[j]){
                    int startP =  (Track.height-1)-(start[j]*3);
                    int endP = (Track.height-1)-(end[j]*3);
                    for(int x=startP;x < endP;x++){
                        Track.track[x][14] = '|';
                    }
                    for(int i=10;i<14;i++){
                        Track.track[endP][i] = '-';
                        Track.track[endP][14] = '+';
                        Track.track[startP][i] = '-';
                        Track.track[startP][14] = '+';
                    }
                }
            }

            if((start[j] > 1 && start[j] < ((goal - 1) / 4) + 2) && (end[j] > ((((goal - 1) / 4) * 2)+1) && end[j] < (((goal - 1) / 4) * 3) + 2)){
                int endPos = ((end[j]-((((goal-1)/4)*2)+1))*3); //height
                int endP = Track.width - 11;
                for(int x=endP;x > Track.width-15;x--){
                    Track.track[2+endPos][Track.width-15] = '+';
                    Track.track[2+endPos][x] = '-';
                }
                int pos = (((goal-1)/4)+1);
                int pose = ((((goal-1)/4)+1) - start[j]) * 2;
                int forl = start[j] + pos + pose;
                int startl = ((forl-((((goal-1)/4)*2)+1))*3);
                    int startPos = (Track.height-1)-(start[j]*3);
                if(end[j] < forl){
                    for(int i=endPos+3;i<startl+2;i++){
                        Track.track[i][Track.width-15] = '|';
                    }
                    for(int z=10;z<Track.width-15;z++){
                        Track.track[startPos][z] = '-';
                        Track.track[startPos][Track.width-15] = '+';
                    }
                }
                if(end[j>forl]){
                    for(int i=startl+2;i<endPos+2;i++){
                        Track.track[i][Track.width-15] = '|';
                    }
                    for(int z=10;z<Track.width-15;z++){
                        Track.track[startPos][z] = '-';
                        Track.track[startPos][Track.width-15] = '+';
                    }
                }
                if(end[j]==forl){
                    for(int z=10;z<Track.width-15;z++){
                        Track.track[startPos][z] = '-';
                        Track.track[startPos][Track.width-15] = '+';
                    }
                    for(int x=endP;x > Track.width-15;x--){
                    Track.track[2+endPos][x] = '-';
                }
                }
            }

            if((start[j] > ((goal - 1) / 4) + 1 && start[j] < (((goal - 1) / 4) * 2) + 2) && (end[j] > ((goal - 1) / 4)+1 && end[j] < ((((goal - 1) / 4)*2) + 2))){
                int startPos = 4+((start[j]-(((goal-1)/4)+1))*9); 
                int endPos = 4+((end[j]-(((goal-1)/4)+1))*9); 
                if(start[j] < end[j]){
                    for(int x=startPos;x <endPos;x++){
                        Track.track[4][startPos] = '|';
                        Track.track[5][startPos] = '+';
                        Track.track[4][endPos] = '|';
                        Track.track[5][endPos] = '+';
                        Track.track[5][x] = '-';
                    }
                }
                if(start[j] > end[j]){
                   for(int x=endPos;x <startPos;x++){
                        Track.track[4][startPos] = '|';
                        Track.track[5][startPos] = '+';
                        Track.track[4][endPos] = '|';
                        Track.track[5][endPos] = '+';
                        Track.track[5][x] = '-';
                    }
            }
        }

            if((start[j] > ((goal - 1) / 4) + 1 && start[j] < (((goal - 1) / 4) * 2) + 2) && (end[j] > 1 && end[j] < ((goal - 1) / 4) + 2)){
                int linep = 4+((start[j]-(((goal - 1) / 4)+1))*9);
                int linet = (((goal - 1) / 4) + 2) - end[j]; 
                int dline = 2+(linet*3);
                for(int x=4;x<dline;x++){
                    Track.track[x][linep] = '|';
                }
                for(int i=10;i<linep;i++){
                    Track.track[dline][i] = '-';
                    Track.track[dline][linep] = '+';
                }
            }
            
            if((end[j] > ((((goal - 1) / 4) * 2)+1) && end[j] < (((goal - 1) / 4) * 3) + 2) && (start[j] > ((goal - 1) / 4) + 1 && start[j] < (((goal - 1) / 4) * 2) + 2)){
                int startPos = ((start[j]-(((goal-1)/4)+1))*9); 
                int endPos = ((end[j]-((((goal - 1) / 4) * 2)+1))*3);
                for(int x=4;x < endPos+2; x++){
                    Track.track[x][startPos+4] = '|';
                    Track.track[endPos+2][startPos+4] = '+';
                }
                for(int i=startPos+5;i<Track.width-10;i++){
                    Track.track[endPos+2][i] = '-';
                }
            }

            if((start[j] > (((goal - 1) / 4) * 2) + 1 && start[j] < (((goal - 1) / 4) * 3) + 2) && (end[j] > ((((goal - 1) / 4) * 2)+1) && end[j] < (((goal - 1) / 4) * 3) + 2)){
                int startPos = 2+((start[j]-((((goal-1)/4)*2)+1))*3);   
                int endPos = 2+((end[j]-((((goal - 1) / 4) * 2)+1))*3);
                int lineP = Track.width-13;
                int lineE = Track.width-10;
                if(start[j] < end[j]){
                    for(int x=startPos ;x < endPos;x++){
                        Track.track[x][lineP] = '|';
                    }
                    for(int i=lineP;i<lineE;i++){
                        Track.track[endPos][i] = '-';
                        Track.track[endPos][lineP] = '+';
                        Track.track[startPos][i] = '-';
                        Track.track[startPos][lineP] = '+';
                    }
                }
                if(start[j] > end[j]){
                    for(int x=endPos;x < startPos;x++){
                        Track.track[x][lineP] = '|';
                    }
                    for(int i=lineP;i<lineE;i++){
                        Track.track[endPos][i] = '-';
                        Track.track[endPos][lineP] = '+';
                        Track.track[startPos][i] = '-';
                        Track.track[startPos][lineP] = '+';
                    }
                }
            }

            if((start[j] > (((goal - 1) / 4) * 2) + 1 && start[j] < (((goal - 1) / 4) * 3) + 2) && (end[j] > ((goal - 1) / 4)+1 && end[j] < ((((goal - 1) / 4)*2) + 2))){
                int startPos = ((end[j]-(((goal-1)/4)+1))*9); 
                int endPos = ((start[j]-((((goal - 1) / 4) * 2)+1))*3);
                for(int x=4;x < endPos+2; x++){
                    Track.track[x][startPos+4] = '|';
                    Track.track[endPos+2][startPos+4] = '+';
                }
                for(int i=startPos+5;i<Track.width-10;i++){
                    Track.track[endPos+2][i] = '-';
                }
            }

            if((start[j] > (((goal - 1) / 4) * 2) + 1 && start[j] < (((goal - 1) / 4) * 3) + 2) && (end[j] > 1 && end[j] < ((goal - 1) / 4) + 2)){
                int endPos = ((start[j]-((((goal-1)/4)*2)+1))*3); //height
                int endP = Track.width - 11;
                for(int x=endP;x > 15;x--){
                    Track.track[2+endPos][15] = '+';
                    Track.track[2+endPos][x] = '-';
                }
                int pos = (((goal-1)/4)+1);
                int pose = ((((goal-1)/4)+1) - end[j]) * 2;
                int forl = end[j] + pos + pose;
                int startl = ((forl-((((goal-1)/4)*2)+1))*3);
                int startPos = (Track.height-1)-(end[j]*3);
                if(start[j] < forl){
                    for(int i=endPos+3;i<startl+2;i++){
                        Track.track[i][15] = '|';
                    }
                    for(int z=10;z<15;z++){
                        Track.track[startPos][z] = '-';
                        Track.track[startPos][15] = '+';
                    }
                }
                if(start[j] > forl){
                    for(int i=startl+2;i<endPos+2;i++){
                        Track.track[i][15] = '|';
                    }
                    for(int z=10;z<15;z++){
                        Track.track[startPos][z] = '-';
                        Track.track[startPos][15] = '+';
                    }
                }
                if(start[j]==forl){
                    for(int z=10;z<15;z++){
                        Track.track[startPos][z] = '-';
                        Track.track[startPos][15] = '+';
                    }
                    for(int x=endP;x > 15;x--){
                    Track.track[2+endPos][x] = '-';
                }
                }
            }
    }
};

Bridge::Bridge() {}
