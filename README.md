## COSC 2658 - Data Structures & Algorithms

<hr/>

#### Assignment #: 4

#### Team G13:

````
- Anh Nguyen
- Bao Pham
- Phuong Nguyen
- Nguyen Nguyen
````

#### Description:

````
“Guess My Number” is a classic two-player game. that your team will implement on your computer. A competition will be held in class between each team. The winner will be the program that wins the most number of games in 2 one-hour tournaments.

Rules.

Your program generates a four-digit number that the other program needs to guess  - e.g. 3467; 0234; 9911; 1110, 5555)

On each move, each program generates a four-digit number for the other program to respond to in the following manner:
      (1) Strike -- a correct digit in the guess and in the right place.
      (2) Hit -- a correct digit in the guess but out of order.
      (3) Miss -- No digit of the guess is anywhere in the number.

Here is an example: Let's say the leader picked 7375. Here are the guesses and the responses:

  Guess  	  Response  
  0124  	          Miss  
  3567  	          Three Hits
  5376  	          Two Strikes and One Hit 
  7375  	          Four Strikes - Game over

The program has to guess the other program's number and vice-versa. The winner is the program that has a lower number of guesses.
````

#### Run:
- Compile the program
````
javac *.java
````
- Normal mode
````
java GuessRunner
````
- Test mode
````
java GuessRunner -t <num_of_tests>
````