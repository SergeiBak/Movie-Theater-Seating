# Movie-Theater-Seating
<img width="414.72" height="233.28" src="https://user-images.githubusercontent.com/77221025/160891504-c9ff8461-e941-4e11-8404-4eb6b8950e10.png">

## Table of Contents
* [Overview](#Overview)
* [Assumptions](#Assumptions)
* [How to Run](#how-to-run)
* [Code](#Code)
* [Technologies](#Technologies)

## Overview
This project is a coding challenge where you are given the challenge of creating a algorithm for assigning seats within a movie theater in order to fulfill 
reservation requests. The goal of the challenge is to maximize both customer satisfaction and customer safety (for public safety, a buffer of 3 seats is required).     

You are given a file with one line of input for each reservation request. The order of the lines reflects the order in which the reservation requests were recieved. 
Each line is comprised of a reservation identifier, followed by a space, and then the number of seats requested. The reservation identifier will have the format: 
R####.     
<img width="414.72" height="233.28" src="https://user-images.githubusercontent.com/77221025/160896693-de2724ca-a427-46da-b85d-569ec15f31b3.png">       

The program should output a file containing the seating assignments for each request. Each row in the file should include the reservation number followed by a space, 
and then a comma-delimitted list of the assigned seats.      

## Assumptions
In coming up with my solution, I made the following assumptions: 
- Theater arrangement defaults to 10 rows with 20 seats per row
- For public safety, each group would like to have a 3 seat buffer from any other groups in same row
- Reservation requests are processed in the order they come in
- Customers would prefer to be seated towards the middle rows of the movie theater

## How to Run
This program was built in Visual Studio Code. In order to run follow the following steps:
- Switch Terminal shell from PowerShell to Command Prompt
- Compile the Java program with the following line: `javac Main.java`   <-- Make sure that the .class files are created for each .java
- Run the Java program with the following line: `java Main input.txt`   <-- Change input.txt to whichever input file you would like to use in the directory
- Alternatively, you can also specify the full path to run: `java Main "D:\GitHub\Interview\Movie-Theater-Seating\input.txt"`

## Code
A brief description of all of the files is as follows:
- The `exactFullInput` file tests the edge case where a reservation is for every seat in the theater (200 seats).
- The `fullInput` file tests the edge case where certain reservations are unable to be allocated due to there not being enough seats left.
- The `input` file tests several reservations with small group sizes.
- The `Main` class contains the main method and is the basis for running the program.
- The `output` file has all of the reservation seating outcomes written to it.
- The `Reservation` class represents the reservation class and is used to keep track of seating for reservation + printing reservation details.
- The `Theater` class contains the seating status of the theater, assigns seats and holds all of the reservations.

## Technologies
- Visual Studio Code
- GitHub
- GitHub Desktop
