// 241UC24157 YEE SI SHUN G7 BRIDGES
#include <iostream>
#include <string>
#include "RaceTrack.h"

#ifndef BRIDGES_H
#define BRIDGES_H

using namespace std;

class Bridge {
public:
    int BridgeNums;
    int start[5], end[5];

    Bridge(Track &track, int goal);
    Bridge();
};

#endif // BRIDGES_H
