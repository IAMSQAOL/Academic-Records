#include "RaceTrack.h"
#include <iostream>
using namespace std;

using namespace std;

Track::Track(int size) : size(size) // Initialize size using initializer list
{
    // Calculate width and height
    width = 1 + (2 + size) * 9;
    height = 1 + (size * 3) + 8;

    // Allocate memory for the 2D array
    track = new char*[height];
    for (int i = 0; i < height; ++i)
        track[i] = new char[width];

    // Initialize all to space
    for (int x = 0; x < height; x++)
    {
        for (int i = 0; i < width; i++)
        {
            track[x][i] = ' ';
        }
    }

    // Top track (Start)
    // Horizontal line
    for (int x = 0; x < 4; x += 3)
    {
        for (int i = 9; i < width - 8; i += 9)
        {
            track[x][i] = '+';
            for (int z = 1; z < 9 && (i + z) < width - 9; z += 1)
            {
                track[x][i + z] = '-';
            }
        }
        track[3][width - 1] = '+';
    }

    // Vertical line
    for (int i = 9; i < width - 9; i += 9)
    {
        track[1][i] = '|';
        track[2][i] = '|';
    }

    // LEFT-RIGHT TRACK
    for (int x = 4; x < height - 6; x += 3)
    {
        track[x][0] = '|';
        track[x][9] = '|';
        track[x + 1][0] = '|';
        track[x + 1][9] = '|';
        track[x][width - 10] = '|';
        track[x][width - 1] = '|';
        track[x + 1][width - 10] = '|';
        track[x + 1][width - 1] = '|';
    }

    for (int x = 3; x < height - 5; x += 3)
    {
        for (int i = 0; i < width; i += 9)
        {
            track[x][0] = '+';
            track[x][width - 1] = '+';
            for (int z = 1; z < 9; z += 1)
            {
                track[x][z] = '-';
                track[x][width - 1 - z] = '-';
            }
        }
    }

    for (int x = 6; x < height - 6; x += 3)
    {
        for (int i = 0; i < width; i += 9)
        {
            track[x][0] = '+';
            track[x][9] = '+';
            track[x][width - 10] = '+';
            track[x][width - 1] = '+';
            for (int z = 1; z < 9; z += 1)
            {
                track[x][z] = '-';
                track[x][width - 1 - z] = '-';
            }
        }
    }

    // BOTTOM TRACK
    for (int x = height - 6; x < height; x += 3)
    {
        for (int i = 0; i < width - 8; i += 9)
        {
            track[x][i] = '+';
            for (int z = 1; z < 9 && (i + z) < width - 9; z += 1)
            {
                track[x][i + z] = '-';
            }
        }
    }

    for (int x = height - 5; x < height - 3; x++)
    {
        for (int i = 0; i < width - 9; i += 9)
        {
            track[x][i] = '|';
        }
    }

    // Arrow
    for (int x = 2; x < 3; x++)
    {
        track[x][4] = '|'; 
        track[1][4] = '+';
        for (int i = 5; i < 8; i++)
        {
            track[1][i] = '-';
        }
        track[1][8] = '>';
    }
    for (int x = 2; x < 3; x++)
    {
        track[x][width - 5] = '|';
        track[x + 1][width - 5] = 'V';
        track[1][width - 5] = '+';
        for (int i = width - 9; i < width - 5; i++)
        {
            track[1][i] = '-';
        }
    }
    for (int x = height - 4; x < height - 3; x++)
    {
        track[x - 1][width - 5] = '|';
        track[x][width - 5] = '+';
        for (int i = width - 8; i < width - 5; i++)
        {
            track[height - 4][i] = '-';
        }
        track[x][width - 9] = '<';
    }
}

Track::~Track()
{
    // Deallocate memory
    for (int i = 0; i < height; ++i)
    {
        delete[] track[i];
    }
    delete[] track;
}

void Track::displayTrack()
{
    for (int x = 0; x < height; x++)
    {
        for (int i = 0; i < width; i++)
        {
            cout << track[x][i];
        }
        cout << endl;
    }
}
