#ifndef RACETRACK_H
#define RACETRACK_H

#include <iostream>

class Track
{
public:
    int size;
    int width;
    int height;
    char** track;

    Track(int size); // Constructor declaration
    ~Track(); // Destructor declaration

    void displayTrack();
};

#endif // RACETRACK_H
