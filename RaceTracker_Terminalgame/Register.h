// 241UC24158
// Goh Chun Yong
// Register Header
// This file is using for function Player Registration and Character Selection

#ifndef REGISTER_H
#define REGISTER_H

#include <iostream>
#include <string>
#include <vector>
#include <cstdlib>
#include <ctime>
#include <algorithm>
#include <unordered_map>
#include <set>
#include "player.h"

using namespace std;


class Game {
public:
    Game();

    void inputPlayerNames();
    void printPlayerNames();
    void printCharacterDetails();
    void startCharacterSelection();
    unordered_map<string, char> getPlayerCharacterSymbols() const;
    void announcement(const string& character) const;

private:
    vector<string> players;
    vector<string> characters;
    set<string> validCharacters;
    unordered_map<string, string> playerCharacterMap;
    unordered_map<string, string> characterDetails;

    string selectRandomPlayer();
    void removePlayer(const string& player);
    void chooseCharacter(const string& player);
    void assignLastCharacter(const string& player);
    void printPlayerCharacters();
};

#endif // REGISTER_H
