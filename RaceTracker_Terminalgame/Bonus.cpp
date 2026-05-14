//241UC2415U YAP CHUN HOONG

#include "Bonus.h"
#include "RaceTrack.h"
#include "Bridges.h"

Bonus::Bonus(Track &Track, Bridge bridge, int goal) {
    cout << endl;
    cout << "Enter the number of bonuses (minimum 2, maximum 6): ";
    cin >> totalbonus;
    while (totalbonus < 2 || totalbonus > 6) {
        cout << "Please enter a number between 2 and 6: ";
        cin >> totalbonus;
    }

    for (int x = 0; x < totalbonus; x++) {
        bool validInput = false;
        while (!validInput) {
            cout << "Enter bonus type (1 for move 3 step forward, 2 for double steps): ";
            cin >> type;
            btype[x] = type;
            if (type != 1 && type != 2) {
                cout << "Invalid bonus type. Please enter 1 or 2." << endl;
                continue;
            }

            if (type == 1) {
                cout << "At which box do you want to place move 3 steps forward? (between 2 to " << goal - 4 << "): ";
            } else if (type == 2) {
                cout << "At which box do you want to place double step? (between 2 to " << goal - 4 << "): ";
            }
            cin >> boxNumber[x];

            // Validate that boxNumber[x] is not the same as any bridge.start[j]
            validInput = true;
            for (int j = 0; j < bridge.BridgeNums; j++) {
                if (boxNumber[x] == bridge.start[j]) {
                    cout << "Invalid position. This position is already a bridge start position. Try again." << endl;
                    validInput = false;
                    break;
                }
            }

            // Additional validation for range
            if (boxNumber[x] < 1 || boxNumber[x] > goal - 4) {
                cout << "Invalid box number. Please enter a number between 2 and " << goal - 4 << "." << endl;
                validInput = false;
            }
        }
    }

    for (int j = 0; j < totalbonus; j++) {
        if (boxNumber[j] > 1 && boxNumber[j] < ((goal - 1) / 4) + 2) {
            int startPos = (Track.height - 1) - (boxNumber[j] * 3);
            Track.track[startPos][1] = 'o';
            Track.track[startPos][2] = '.';
            Track.track[startPos][3] = '.';
            Track.track[startPos][4] = 'o';
            Track.track[startPos][5] = '.';
            Track.track[startPos][6] = '.';
            Track.track[startPos][7] = 'o';
            Track.track[startPos][8] = '.';
        }
        if (boxNumber[j] > ((goal - 1) / 4) + 1 && boxNumber[j] < (((goal - 1) / 4) * 2) + 2) {
            int startPos = ((boxNumber[j] - (((goal - 1) / 4) + 1)) * 9);
            Track.track[2][1 + startPos] = 'o';
            Track.track[2][2 + startPos] = '.';
            Track.track[2][3 + startPos] = '.';
            Track.track[2][4 + startPos] = 'o';
            Track.track[2][5 + startPos] = '.';
            Track.track[2][6 + startPos] = '.';
            Track.track[2][7 + startPos] = 'o';
            Track.track[2][8 + startPos] = '.';
        }
        if (boxNumber[j] > (((goal - 1) / 4) * 2) + 1 && boxNumber[j] < (((goal - 1) / 4) * 3) + 2) {
            int startPos = ((boxNumber[j] - ((((goal - 1) / 4) * 2) + 1)) * 3);
            Track.track[2 + startPos][Track.width - 9] = 'o';
            Track.track[2 + startPos][Track.width - 8] = '.';
            Track.track[2 + startPos][Track.width - 7] = '.';
            Track.track[2 + startPos][Track.width - 6] = 'o';
            Track.track[2 + startPos][Track.width - 5] = '.';
            Track.track[2 + startPos][Track.width - 4] = '.';
            Track.track[2 + startPos][Track.width - 3] = 'o';
            Track.track[2 + startPos][Track.width - 2] = '.';
        }

        if (boxNumber[j] > (((goal - 1) / 4) * 3) + 1 && boxNumber[j] < goal - 3) {
            int pos = (boxNumber[j] - ((((goal - 1) / 4) * 3) + 1)) * 9;
            Track.track[Track.height - 4][Track.width - 9 - pos] = 'o';
            Track.track[Track.height - 4][Track.width - 8 - pos] = '.';
            Track.track[Track.height - 4][Track.width - 7 - pos] = '.';
            Track.track[Track.height - 4][Track.width - 6 - pos] = 'o';
            Track.track[Track.height - 4][Track.width - 5 - pos] = '.';
            Track.track[Track.height - 4][Track.width - 4 - pos] = '.';
            Track.track[Track.height - 4][Track.width - 3 - pos] = 'o';
            Track.track[Track.height - 4][Track.width - 2 - pos] = '.';
        }
    }
}
