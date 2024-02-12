# Dungeon Mania

[Checkout UML diagram of my implementation here](https://drive.google.com/file/d/1uJqEFKwZRKXHGMQgYjq-tQ8MB0pi_Rb3/view?usp=sharing)

## Overview

In Dungeon Mania, you control a Player and have to complete various goals within a series of dungeons to complete the game.

The simplest form of such a puzzle is a maze, where the Player must find their way from the starting point to the exit.

https://github.com/PhotKosee/dungeon-mania/assets/114990364/d60e9f0d-192c-4948-a8c7-fa3d798e2db2

## Features

Modifying and implementing new functionality from a given MVP

- Refactored the code to improve the quality of the design from code smell
- Implemented entities: Assassin, Swamp Tile, Sun Stone, Sceptre, Midnight Armour, Light Bulb (off), Light Bulb (on), Wire, Switch Door

This project mainly focuses on applying design patterns to provide a sufficient solution.

- Factory method for creating each of the entities
- Strategy pattern to group up entities with similar behaviors
- Composite pattern to stack up armor and attack damages from all types of equipment
- State pattern to handle each type of potion
- Abstract factory to create goals according to the map specification

![dungeon_show](https://github.com/PhotKosee/dungeon-mania/assets/114990364/3f06c957-9b04-4c09-894c-84b5d2e354f9)
