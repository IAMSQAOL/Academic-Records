//241UC2414Q YAP SHERN YU
#include "Obstacles.h"
#include "RaceTrack.h"
#include "Bridges.h"
#include "Bonus.h"
#include "player.h"
#include <iostream>
using namespace std;

Obstacles::Obstacles(Track &Track, Bridge bridge, Bonus bonus, int goal)
{
    cout << "Enter the number of OBSTACLES (minimum 5, maximum 10): ";
    cin >> totalobs;
    while (totalobs < 5 || totalobs > 10) {
        cout << "Please enter a number between 5 and 10: ";
        cin >> totalobs;
    }

    for (int x = 0; x < totalobs; x++)
    {
        bool validInput = false;
        while (!validInput)
        {   
            cout << endl;
            cout << "Choose obstacle type for this obstacle:" << endl;
            cout << "1. Move backward 3 steps." << endl;
            cout << "2. Miss next turn." << endl;
            cout << "Choice : ";
            cin >> type;
            btype[x] = type;
            if (type != 1 && type != 2)
            {
                cout << "Invalid obstacles type. Please enter 1 or 2." << endl;
                continue;
            }

            if (type == 1)
            {
                cout << "At which box do you want to place MOVE BACKWARDS 3 STEPS? (between 2 to " << goal << "): ";
            }
            else if (type == 2)
            {
                cout << "At which box do you want to place MISS NEXT TURN? (between 2 to " << goal << "): ";
            }
            cin >> boxNumber[x];

            // Validate that boxNumber[x] is not the same as any bridge.start[j]
            validInput = true;
            for (int j = 0; j < bridge.BridgeNums; j++)
            {
                if (boxNumber[x] == bridge.start[j])
                {
                    cout << "Invalid position. This position already has a bridge start position. Try again." << endl;
                    validInput = false;
                    break;
                }
            }
            for (int z = 0; z < bonus.totalbonus; z++)
            {
                if (boxNumber[x] == bonus.boxNumber[z])
                {
                    cout << "Invalid position. This position already has a bonus. Try again." << endl;
                    validInput = false;
                    break;
                }
            }

            // Additional validation for range
            if (boxNumber[x] < 1 || boxNumber[x] > goal)
            {
                cout << "Invalid box number. Please enter a number between 2 and " << goal << "." << endl;
                validInput = false;
            }
        }
    }

    for (int j = 0; j < totalobs; j++)
    {
        if (boxNumber[j] > 1 && boxNumber[j] < ((goal - 1) / 4) + 2)
        {
            int startPos = (Track.height - 1) - (boxNumber[j] * 3);
            Track.track[startPos][2] = 'x';
            Track.track[startPos][3] = 'x';
            Track.track[startPos][5] = 'x';
            Track.track[startPos][6] = 'x';
            Track.track[startPos][8] = 'x';
        }
        if (boxNumber[j] > ((goal - 1) / 4) + 1 && boxNumber[j] < (((goal - 1) / 4) * 2) + 2)
        {   
            int startPos = ((boxNumber[j] - (((goal - 1) / 4) + 1)) * 9); 
            Track.track[2][2 + startPos] = 'x';
            Track.track[2][3 + startPos] = 'x';
            Track.track[2][5 + startPos] = 'x';
            Track.track[2][6 + startPos] = 'x';
            Track.track[2][8 + startPos] = 'x';
        }
        if (boxNumber[j] > (((goal - 1) / 4) * 2) + 1 && boxNumber[j] < (((goal - 1) / 4) * 3) + 2)
        {     
            int startPos = ((boxNumber[j] - ((((goal - 1) / 4) * 2) + 1)) * 3); 
            Track.track[2 + startPos][Track.width - 8] = 'x';        
            Track.track[2 + startPos][Track.width - 7] = 'x';
            Track.track[2 + startPos][Track.width - 5] = 'x';
            Track.track[2 + startPos][Track.width - 4] = 'x';
            Track.track[2 + startPos][Track.width - 2] = 'x';
        }

        if (boxNumber[j] > (((goal - 1) / 4) * 3) + 1 && boxNumber[j] < goal + 1)
        {
            int pos = (boxNumber[j] - ((((goal - 1) / 4) * 3) + 1)) * 9;
            Track.track[Track.height - 4][Track.width - 8 - pos] = 'x';
            Track.track[Track.height - 4][Track.width - 7 - pos] = 'x';
            Track.track[Track.height - 4][Track.width - 5 - pos] = 'x';
            Track.track[Track.height - 4][Track.width - 4 - pos] = 'x';
            Track.track[Track.height - 4][Track.width - 2 - pos] = 'x';
        }
    }
}
