#include <iostream>
#include <limits>
#include "RaceTrack.h"
#include "player.h"
#include "Bridges.h"
#include "Bonus.h"
#include "Obstacles.h"
#include "Register.h"

using namespace std;

int main()
{
    cout << "Assigment 2 program 2 by G7 from TT3L:" << endl;
    cout << "241UC24157 YEE SI SHUN"<< endl;
    cout << "241UC24158 GOH CHUN YONG"<< endl;
    cout << "241UC2414Q YAP SHERN YU"<< endl;
    cout << "241UC2415U YAP CHUN HOONG"<< endl;
    
    int warrior = 1, mage = 1, archer = 1;
    int size, roundss, rounds, goal;
    char ok;

    cout << "Size of track (eg: input 5 will get 5x5 racetracks // between 5-12): ";
    cin >> size;
    while (size < 5 || size > 12) 
    {
        cout << "Invalid size, please choose between 5 to 12: ";
        cin >> size;
    }
    cout << "How many rounds do you want?: ";
    cin >> roundss;
    rounds = roundss - 1;
    goal = (size * 4) + 1;

    // Ignore line to input player name
    cin.ignore();

    // Player Registration
    Game game;
    game.inputPlayerNames();
    game.printPlayerNames();
    game.startCharacterSelection();

    // Print track
    Track test(size);
    Player sample(rounds, goal);
    
    Bridge bridge(test, goal);
    Bonus bonus(test, bridge, goal);
    Obstacles obs(test, bridge, bonus, goal);

    // Initial positions
    sample.updateTrack(test, warrior, 'W');
    sample.updateTrack(test, mage, 'M');
    sample.updateTrack(test, archer, 'A');
    
    test.displayTrack();

    // Sample steps for debugging
    cout << "Type y to continue: ";
    cin >> ok;
    while ((ok == 'y' && !sample.game_over) || ok == 'u'|| (ok != 'y' && ok != 'u'))
    {
        if (ok == 'u') {
            sample.applyBonus(test);
        } else {
            sample.WarriorStep(test, bridge, obs, bonus, game);
            sample.MageStep(test, bridge, obs, bonus, game);
            sample.ArcherStep(test, bridge, obs, bonus, game);
        }
        test.displayTrack();

        if (!sample.game_over)
        {
            cout << "Type y to continue (u if bonus available): ";
            cin >> ok;
        }
    }
    return 0;
}
