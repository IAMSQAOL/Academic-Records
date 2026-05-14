//241UC24158 GOH CHUN YONG

#include "Register.h"
#include <iostream>
using namespace std;

Game::Game() {
    players.resize(3);
    characters = {"Warrior", "Mage", "Archer"};
    validCharacters = {"Warrior", "Mage", "Archer"};
    characterDetails = {
        {"Warrior", "The Warrior can move 3-6 steps a round."},
        {"Mage", "The Mage can move 2-5 steps a round."},
        {"Archer", "The Archer can move 1-4 steps a round."}
    };
}

void Game::inputPlayerNames() {
    for (int i = 0; i < 3; i++) {
        cout << "Enter player" << i + 1 << " name: ";
        getline(cin, players[i]);
    }
}

void Game::printPlayerNames() {
    cout << endl;
    for (int i = 0; i < players.size(); i++) {
        cout << "Player " << i + 1 << ": " << players[i] << endl;
    }
}

void Game::printCharacterDetails() {
    cout << endl << "Character Details:" << endl;
    for (const auto& detail : characterDetails) {
        cout << detail.second << endl;
    }
    cout << endl;
}

void Game::startCharacterSelection() {
    printCharacterDetails();

    string selectedPlayer = selectRandomPlayer();
    cout << "Selected player to choose character first: " << selectedPlayer << endl;

    // Selected player chooses a character
    chooseCharacter(selectedPlayer);

    // Other players choose characters
    while (players.size() > 1) {
        chooseCharacter(players[0]);
    }

    // Assign the last character to the last player
    assignLastCharacter(players[0]);

    // Print player-character mappings
    printPlayerCharacters();
}

unordered_map<string, char> Game::getPlayerCharacterSymbols() const {
    unordered_map<string, char> characterSymbols;
    for (const auto& pair : playerCharacterMap) {
        if (pair.second == "Warrior") {
            characterSymbols[pair.first] = 'W';
        } else if (pair.second == "Mage") {
            characterSymbols[pair.first] = 'M';
        } else if (pair.second == "Archer") {
            characterSymbols[pair.first] = 'A';
        }
    }
    return characterSymbols;
}

void Game::announcement(const string& character) const {
    for (const auto& pair : playerCharacterMap) {
        if (pair.second == character) {
            cout << pair.first << " who choose the " << character << " wins the game!" << endl;
            return;
        }
    }
    cout << "No player chose the " << character << "." << endl;
}

string Game::selectRandomPlayer() {
    srand(time(0)); // Initialize random seed
    int randomIndex = rand() % players.size(); // Generate random index between 0 and the number of players
    string player = players[randomIndex];
    removePlayer(player);
    return player;
}

void Game::removePlayer(const string& player) {
    players.erase(remove(players.begin(), players.end(), player), players.end());
}

void Game::chooseCharacter(const string& player) {
    if (characters.size() == 1) {
        assignLastCharacter(player);
        return;
    }

    string selectedCharacter;

    while (true) {
        cout << player << ", choose your character from the following options: ";
        for (const string& character : characters) {
            cout << character << " ";
        }
        cout << endl;

        getline(cin, selectedCharacter);

        // Validate the selected character
        if (validCharacters.find(selectedCharacter) != validCharacters.end()) {
            break; // Exit loop if the character is valid
        } else {
            cout << "Invalid character selected. Please choose a valid character." << endl;
        }
    }

    // Map player to selected character
    playerCharacterMap[player] = selectedCharacter;

    // Remove selected character from the list
    characters.erase(remove(characters.begin(), characters.end(), selectedCharacter), characters.end());
    validCharacters.erase(selectedCharacter); // Also remove from valid characters

    removePlayer(player);
}

void Game::assignLastCharacter(const string& player) {
    if (!characters.empty()) {
        string lastCharacter = characters[0];
        playerCharacterMap[player] = lastCharacter;
        cout << player << " has been automatically assigned the character: " << lastCharacter << endl;
        characters.clear();
        validCharacters.clear();
        removePlayer(player);
    }
}

void Game::printPlayerCharacters() {
    cout << "Player-Character Assignments:" << endl;
    for (const auto& pair : playerCharacterMap) {
        cout << pair.first << " choose " << pair.second << endl;
    }
}
