package student_player;

import boardgame.Move;
import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoPlayer;
import student_player.mcts.MonteCarlo;

/**
 * A PentagoPlayer that uses the MonteCarlo Seach Tree algorithm to select the next move
 */
public class MonteCarloPlayer extends PentagoPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public MonteCarloPlayer() {
        super("MonteCarloPlayer");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(PentagoBoardState boardState) {

        final boolean TESTING = false; // debug mode, print statements

        long start = System.currentTimeMillis();

        MonteCarlo monteCarlo = new MonteCarlo();
        Move myMove = monteCarlo.findNextMove(boardState);

        if (TESTING) {
            System.out.printf("Time for selecting move (s): %f%n", (System.currentTimeMillis() - start) / 1000f);
            boardState.printBoard();
        }
        // Return your move to be processed by the server.
        return myMove;
    }
}
