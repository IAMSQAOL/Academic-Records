#include "player.h"
#include "RaceTrack.h"
#include "Bridges.h"
#include "Obstacles.h"
#include "Bonus.h"
#include "Register.h"
#include <iostream>
using namespace std;


Player::Player(int rounds, int goal)
{
    this->rounds = rounds;
    this->goal = goal;
}

int Player::randomWarrior()
{
    srand((unsigned)time(NULL));
    return 3 + (rand() % 3);
}

int Player::randomMage()
{
    srand((unsigned)time(NULL));
    return 2 + (rand() % 5);
}

int Player::randomArcher()
{
    srand((unsigned)time(NULL));
    return 1 + (rand() % 6);
}

int Player::randomDoublesteps(string character)
{
    srand((unsigned)time(NULL));
    int random = 1 + (rand() % 4);
    cout << character << " get " << random << " step, which double is " << random * 2 << "!" << endl;
    return random;
}

void Player::checkBridge(int &position, Bridge &bridge)
{
    for (int i = 0; i < bridge.BridgeNums; ++i)
    {
        if (position == bridge.start[i])
        {
            position = bridge.end[i];
            break;
        }
    }
}

void Player::checkBonus(int &position, Bonus &bonus, string player)
{
    for (int i = 0; i < bonus.totalbonus; ++i)
    {
        if (position == bonus.boxNumber[i])
        {
            if (bonus.btype[i] == 1)
            {
                cout << player << " gets a bonus! : adding 3 steps forward. Type u to use..." << endl;
                if (player == "Warrior")
                {
                    warrior_bonus_available = true;
                    warrior_bonus_type = 1;
                }
                else if (player == "Mage")
                {
                    mage_bonus_available = true;
                    mage_bonus_type = 1;
                }
                else if (player == "Archer")
                {
                    archer_bonus_available = true;
                    archer_bonus_type = 1;
                }
            }
            if (bonus.btype[i] == 2)
            {
                cout << player << " gets a bonus! : Random double steps. Type u to use..." << endl;
                if (player == "Warrior")
                {
                    warrior_bonus_available = true;
                    warrior_bonus_type = 2;
                }
                else if (player == "Mage")
                {
                    mage_bonus_available = true;
                    mage_bonus_type = 2;
                }
                else if (player == "Archer")
                {
                    archer_bonus_available = true;
                    archer_bonus_type = 2;
                }
            }
            break;
        }
    }
}

void Player::applyBonus(Track &test)
{
    if (warrior_bonus_available)
    {
        if (warrior_bonus_type == 1)
        {
            warrior += 3;
        }
        else if (warrior_bonus_type == 2)
        {
            warrior += randomDoublesteps("warrior") * 2; // Double steps
        }
        updateTrack(test, warrior, 'W');
        warrior_bonus_available = false;
    }
    if (mage_bonus_available)
    {
        if (mage_bonus_type == 1)
        {
            mage += 3;
        }
        else if (mage_bonus_type == 2)
        {
            mage += randomDoublesteps("mage") * 2;
        }
        updateTrack(test, mage, 'M');
        mage_bonus_available = false;
    }
    if (archer_bonus_available)
    {
        if (archer_bonus_type == 1)
        {
            archer += 3;
        }
        else if (archer_bonus_type == 2)
        {
            archer += randomDoublesteps("archer") * 2;
        }
        updateTrack(test, archer, 'A');
        archer_bonus_available = false;
    }
}

void Player::checkObstacles(int &position, Obstacles &obs, string player)
{
    for (int i = 0; i < obs.totalobs; ++i)
    {
        if (position == obs.boxNumber[i])
        {
            if (obs.btype[i] == 1)
            {
                cout << player << " will be moving backwards 3 steps" << endl;
                if (player == "Warrior")
                {
                    warrior -= 3;
                }
                else if (player == "Mage")
                {
                    mage -= 3;
                }
                else if (player == "Archer")
                {
                    archer -= 3;
                }
            }

            if (obs.btype[i] == 2)
            {
                cout << player << " will miss next turn." << endl;
                if (player == "Warrior")
                {
                    warrior_obs_miss = true;
                }
                else if (player == "Mage")
                {
                    mage_obs_miss = true;
                }
                else if (player == "Archer")
                {
                    archer_obs_miss = true;
                }
            }
            break;
        }
    }
}

void Player::WarriorStep(Track &Track, Bridge &bridge, Obstacles &obs, Bonus &bonus, Game &game)
{
    if (game_over)
        return;

    if (warrior_obs_miss)
    {
        warrior_obs_miss = false;
        cout << "Warrior misses this turn." << endl;
        return;
    }

    warrior += randomWarrior();
    checkBridge(warrior, bridge);
    checkBonus(warrior, bonus, "Warrior");
    checkObstacles(warrior, obs, "Warrior");

    if (warrior > goal)
    {
        wturns++;
        warrior -= goal;
    }
    if (wturns > rounds)
    {
        warrior = 1;
        updateTrack(Track, warrior, 'W');
        cout << "Warrior wins!" << endl;
        game.announcement("Warrior");
        game_over = true;
        return;
    }
    updateTrack(Track, warrior, 'W');
}

void Player::MageStep(Track &Track, Bridge &bridge, Obstacles &obs, Bonus &bonus, Game &game)
{
    if (game_over)
        return;

    if (mage_obs_miss)
    {
        mage_obs_miss = false;
        cout << "Mage misses this turn." << endl;
        return;
    }

    mage += randomMage();
    checkBridge(mage, bridge);
    checkBonus(mage, bonus, "Mage");
    checkObstacles(mage, obs, "Mage");

    if (mage > goal)
    {
        mturns++;
        mage -= goal;
    }
    if (mturns > rounds)
    {
        mage = 1;
        updateTrack(Track, mage, 'M');
        cout << "Mage wins!" << endl;
        game.announcement("Mage");
        game_over = true;
        return;
    }
    updateTrack(Track, mage, 'M');
}

void Player::ArcherStep(Track &Track, Bridge &bridge, Obstacles &obs, Bonus &bonus, Game &game)
{
    if (game_over)
        return;

    if (archer_obs_miss)
    {
        archer_obs_miss = false;
        cout << "Archer misses this turn." << endl;
        return;
    }

    archer += randomArcher();
    checkBridge(archer, bridge);
    checkBonus(archer, bonus, "Archer");
    checkObstacles(archer, obs, "Archer");

    if (archer > goal)
    {
        aturns++;
        archer -= goal;
    }
    if (aturns > rounds)
    {
        archer = 1;
        updateTrack(Track, archer, 'A');
        cout << "Archer wins!" << endl;
        game.announcement("Archer");
        game_over = true;
        return;
    }
    updateTrack(Track, archer, 'A');
}

void Player::updateTrack(Track &Track, int position, char symbol)
{
    // Clear previous positions
    for (int i = 0; i < Track.height; i++)
    {
        for (int j = 0; j < Track.width; j++)
        {
            if (Track.track[i][j] == symbol)
            {
                Track.track[i][j] = ' ';
            }
        }
    }

    // Update new position
    if (position == 1)
    {
        int row = Track.height - 4;
        if (symbol == 'W')
        {
            Track.track[row][1] = symbol;
        }
        else if (symbol == 'M')
        {
            Track.track[row][4] = symbol;
        }
        else if (symbol == 'A')
        {
            Track.track[row][7] = symbol;
        }
    }
    else if (position > 1 && position < ((goal - 1) / 4) + 2)
    {
        int player = Track.height - 4 - ((position - 1) * 3);
        Track.track[player][1 + (symbol == 'M' ? 3 : 0) + (symbol == 'A' ? 6 : 0)] = symbol;
    }
    else if (position > ((goal - 1) / 4) + 1 && position < (((goal - 1) / 4) * 2) + 2)
    {
        int player = 9 * (position - ((goal - 1) / 4 + 1));
        Track.track[2][Pwarrior + player + (symbol == 'M' ? 3 : 0) + (symbol == 'A' ? 6 : 0)] = symbol;
    }
    else if (position > (((goal - 1) / 4) * 2) + 1 && position < (((goal - 1) / 4) * 3) + 2)
    {
        int player = 3 * (position - (((goal - 1) / 4) * 2 + 1));
        Track.track[2 + player][Track.width - 9 + (symbol == 'M' ? 3 : 0) + (symbol == 'A' ? 6 : 0)] = symbol;
    }
    else if (position > (((goal - 1) / 4) * 3) + 1 && position < goal + 1)
    {
        int player = 9 * (position - ((((goal - 1) / 4) * 3) + 1));
        Track.track[Track.height - 4][Track.width - 9 - player + (symbol == 'M' ? 3 : 0) + (symbol == 'A' ? 6 : 0)] = symbol;
    }
}
