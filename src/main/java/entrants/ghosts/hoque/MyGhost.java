package entrants.ghosts.hoque;

import examples.StarterPacMan.MyPacMan;

import pacman.Executor;

import pacman.controllers.IndividualGhostController;
import pacman.controllers.MASController;

import pacman.game.Constants;
import pacman.game.Game;

import java.util.EnumMap;
import java.util.Random;

/**
 * Created by anis016 on 23.06.17.
 */

/*
* Implemented Ghost AI based on 5 rules as below:
* 1. Retreat from Ms Pac-Man if Ms Pac-Man is closer to power pill.
* 2. Retreat from Ms Pac-Man if Ghosts are edible.
* 3. Ghost will always disperse if crowded and guard the power pills.
* 4. Chase Ms Pac-Man if Ghosts are non-edible and if Ms Pac-Man is within certain range.
* 5. If none of the above rule applies then, move randomly.
*
* */

class MyGhosts extends MASController {

    public MyGhosts() {
        super(true, new EnumMap<>(Constants.GHOST.class));
        controllers.put(Constants.GHOST.BLINKY, new MyGhost(Constants.GHOST.BLINKY));
        controllers.put(Constants.GHOST.INKY, new MyGhost(Constants.GHOST.INKY));
        controllers.put(Constants.GHOST.PINKY, new MyGhost(Constants.GHOST.PINKY));
        controllers.put(Constants.GHOST.SUE, new MyGhost(Constants.GHOST.SUE));
    }
}

public class MyGhost extends IndividualGhostController {

    EnumMap<Constants.GHOST, Integer> ghostMap = new EnumMap<>(Constants.GHOST.class);
    Random rnd = new Random();

    public MyGhost(Constants.GHOST ghost) {
        super(ghost);
        ghostMap.put(Constants.GHOST.BLINKY, 0);
        ghostMap.put(Constants.GHOST.INKY, 1);
        ghostMap.put(Constants.GHOST.PINKY, 2);
        ghostMap.put(Constants.GHOST.SUE, 3);
    }

    public static void main(String[] args) {
        Executor po = new Executor(true, true, true);
        po.setDaemon(true);
        po.runGame(new MyPacMan(), new MyGhosts(), true, 40);
    }

    public Constants.MOVE getMove(Game game, long timeDue) {

        //if ghost requires an action
        if (game.doesGhostRequireAction(ghost)) {

            int currentIndex = game.getGhostCurrentNodeIndex(ghost);

            Constants.GHOST[] ghosts = Constants.GHOST.values();

            // find the distance from each ghost to other ghosts
            float distance = 0;
            for (int i = 0; i < ghosts.length - 1; i++) {
                for (int j = i + 1; j < ghosts.length; j++) {

                    distance += game.getShortestPathDistance(
                            game.getGhostCurrentNodeIndex(ghosts[i]),
                            game.getGhostCurrentNodeIndex(ghosts[j])
                    );
                }
            }

            final int GHOST_PROXIMITY = 30;
            boolean ghostTooNearEachOther = (distance / 6) < GHOST_PROXIMITY ? true : false;

            if (checkIfPacmanCloseToPowerPill(game) || game.getGhostEdibleTime(ghost) > 0) {
                // Rule1: Retreat from Ms Pac-Man if Ms Pac-Man is closer to power pill
                // Rule2: Retreat from Ms Pac-Man if Ghosts are edible
                // System.out.println("PacMan closer to Power pills!");
                return game.getApproximateNextMoveAwayFromTarget(
                        game.getGhostCurrentNodeIndex(ghost),
                        game.getPacmanCurrentNodeIndex(),
                        game.getGhostLastMoveMade(ghost),
                        Constants.DM.PATH);
            } else if (ghostTooNearEachOther && !closeToMsPacMan(game, currentIndex)) {
                // Rule3: Ghost will always disperse and guard the power pills
                // System.out.println("Ghosts are crowded. Disperse and guard Power pills!");
                int pacManIndex = game.getPacmanCurrentNodeIndex();
                if (game.getGhostEdibleTime(ghost) == 0 && game.getShortestPathDistance(currentIndex, pacManIndex) < 30) {
                    return game.getApproximateNextMoveTowardsTarget(
                            currentIndex,
                            pacManIndex,
                            game.getGhostLastMoveMade(ghost),
                            Constants.DM.PATH);
                } else {
                    // Guard the Power pills
                    return game.getApproximateNextMoveTowardsTarget(
                            currentIndex,
                            game.getPowerPillIndices()[ghostMap.get(ghost)],
                            game.getGhostLastMoveMade(ghost), Constants.DM.PATH);
                }
            }
            else {
                if (game.getPacmanCurrentNodeIndex() != -1 && rnd.nextFloat() < 0.9f) {
                    // Rule4: Chase Ms Pac-Man if Ghosts are non-edible and if Ms Pac-Man is within certain range
                    // System.out.println("Chase Ms Pac-Man!");
                    Constants.MOVE move = game.getApproximateNextMoveTowardsTarget(
                            game.getGhostCurrentNodeIndex(ghost),
                            game.getPacmanCurrentNodeIndex(),
                            game.getGhostLastMoveMade(ghost),
                            Constants.DM.PATH);
                    return move;
                } else {
                    // Rule5: Move randomly
                    // System.out.println("Ghost moves Randomly");
                    Constants.MOVE[] possibleMoves = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghost), game.getGhostLastMoveMade(ghost));
                    return possibleMoves[rnd.nextInt(possibleMoves.length)];
                }
            }
        } else {
            // System.out.println("Action not required");

        }

        return null;
    }

    /**
     * Checks if Close to Ms Pac-Man.
     *
     * @param game     the game
     * @param location the location
     * @return true, if successful
     */
    private boolean closeToMsPacMan(Game game, int location) {
        final int DISTANCE_FROM_PACMAN = 10;
        if (game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), location) < DISTANCE_FROM_PACMAN) {
            return true;
        }

        return false;
    }

    /**
     * Checks if Ms Pac-Man is close to an available power pill
     *
     * @param game game state
     * @return true, if near proximity; false otherwise
     */
    private boolean checkIfPacmanCloseToPowerPill(Game game) {
        int[] activePowerPills = game.getActivePowerPillsIndices();
        final int ACTIVE_POWER_PILL_DISTANCE = 10;
        for(int i = 0; i < activePowerPills.length; i++) {
            Boolean checkPowerPillAvailable = game.isPowerPillStillAvailable(i);
            int pacManNodeIndex = game.getPacmanCurrentNodeIndex();

            if(checkPowerPillAvailable == null || pacManNodeIndex == -1) {
                return false;
            } else if(checkPowerPillAvailable == true && game.getShortestPathDistance(activePowerPills[i], pacManNodeIndex) < ACTIVE_POWER_PILL_DISTANCE) {
                return true;
            }
        }

        return false;
    }
}