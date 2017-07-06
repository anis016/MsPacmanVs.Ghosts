# Ms. Pacman Vs Ghosts AI

![N|Solid](https://vignette2.wikia.nocookie.net/pacman/images/d/db/Pacman_VS_Blinky_VS_Inky.png/revision/latest?cb=20120728100950)

source: [http://pacman.wikia.com](http://pacman.wikia.com/wiki/File:Pacman_VS_Blinky_VS_Inky.png)

Ms. Pacman Vs Ghost AI implementations using Monte Carlo approach.

### Ms. Pacman AI:
Ms. Pacman will decide the best path by taking number of decisions. These are discussed as below
1. The most significant goal is to keep Ms. Pacman alive. Hence, before choosing any kind of path either be in straight line or in a junction it will always try to find if any Ghosts are present within a certain range and if the Ghosts are non edible. If, any such cases are found then, it will call the game function getNextMoveAwayFromTarget() to get away from the Ghosts.
2. From point 1 if the getNextMoveAwayFromTarget() returns MOVE.NEUTRAL then a check is done if a Ghosts are edible. If Ghosts are edible then Ms. Pacman will try to hunt the Ghosts for increasing the score.
3. Ms. Pacman will try to make a decision if it is in any junction points. This decision is made on the basis of Monte Carlo Tree Search algorithm (MCTS). The child selection that is the move from all the present moves are based on the UCB1 selection policy. Before making a decision, some evaluators are implemented which will add some more scoring points.

* Power Pill active evaluator checks if the Ms. Pacman eats a power pill even if the current Power Pill is still active. It will penalize and deduct a certain score.
* Rule based evaluator which adds bonus to a move if Ms. Pacman eats a pill or Ms. Pacman eats a pill on next move or any move that decreases the distance to the nearest pill.
* Distance evaluator which increases the score of the move that brings Ms. Pacman closer to eating a pill.
4. If Ms. Pacman is not in a junction that is if it is in a straight path where it has two moves to chose from, it will try to chose the move which will bring maximum points by utilizing the Power Pill and Pill locations.
 
### Ghosts AI:
Ghosts AI is based upon rules of checking, if Ghosts are edible or Ghosts can chase Ms. Pacman.
1. The most significant checking for the Ghosts are to check if the Ms. Pacman is near the Power Pill or Ghosts are edible. If either of the checking's are satisfied then Ghosts will try to move away from the Ms. Pacman.
2. Normally, if Ghosts are crowding in a place within certain proximity then they will always disperse. Ghosts will mostly guard the power pills and try to make Ms. Pacman avoid eating Power Pills.
3. Chase Ms. Pacman with certain probability if Ghosts are non-edible and if Ms. Pacman is within a defined range from the Ghosts.
4. If none of the above rule applies then, move around randomly.