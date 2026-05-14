//241UC2415U YAP CHUN HOONG
#ifndef BONUS_H
#define BONUS_H

#include "RaceTrack.h"
#include "player.h"
#include "Bridges.h"
#include <iostream>
using namespace std;

class Bonus
{
public:
    int boxNumber[6];
    int type;
    int totalbonus;
    int btype[6];

    Bonus(Track &Track, Bridge bridge, int goal);
};

#endif
