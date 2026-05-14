#ifndef PLAYER_H
#define PLAYER_H

#include <iostream>
#include <cstdlib>
#include "RaceTrack.h"
#include "Bridges.h"
#include "Bonus.h"
#include "Obstacles.h"
#include "Register.h"

class Game;
class Obstacles;
class Bonus;

class Player
{
public:
    int warrior = 1, mage = 1, archer = 1;
    int Pwarrior = 1, Pmage = 4, Parcher = 7;
    int player;
    int rounds, goal;
    int wturns = 0, mturns = 0, aturns = 0;
    bool game_over = false;

    // For Bonus
    bool warrior_bonus_available = false;
    bool mage_bonus_available = false;
    bool archer_bonus_available = false;
    int warrior_bonus_type = 0;
    int mage_bonus_type = 0;
    int archer_bonus_type = 0;

    // For obstacles
    bool warrior_obs_miss = false;
    bool mage_obs_miss = false;
    bool archer_obs_miss = false;

    Player(int rounds, int goal);

    int randomWarrior();
    int randomMage();
    int randomArcher();
    int randomDoublesteps(std::string character);

    void checkBridge(int &position, Bridge &bridge);
    void checkBonus(int &position, Bonus &bonus, std::string player);
    void applyBonus(Track &test);
    void checkObstacles(int &position, Obstacles &obs, std::string player);

    void WarriorStep(Track &Track, Bridge &bridge, Obstacles &obs, Bonus &bonus, Game &game);
    void MageStep(Track &Track, Bridge &bridge, Obstacles &obs, Bonus &bonus, Game &game);
    void ArcherStep(Track &Track, Bridge &bridge, Obstacles &obs, Bonus &bonus, Game &game);

    void updateTrack(Track &Track, int position, char symbol);
};

#endif // PLAYER_H
