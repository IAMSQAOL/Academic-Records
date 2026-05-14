//241UC2414Q YAP SHERN YU
#ifndef OBSTACLES_H
#define OBSTACLES_H

#include "RaceTrack.h"
#include <iostream>
#include "Bonus.h"

using namespace std;

class Bonus;  // Forward declaration
class Player;

class Obstacles
{
public:
    int boxNumber[10];
    int type;
    int totalobs;
    int btype[10];

    Obstacles(Track &Track, Bridge bridge, Bonus bonus, int goal);
};

#endif // OBSTACLES_H
