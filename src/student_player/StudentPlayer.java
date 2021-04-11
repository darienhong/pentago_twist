package student_player;

import boardgame.Move;

import pentago_twist.PentagoPlayer;
import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;
import student_player.mcts.MonteCarlo;

/** A player file submitted by a student. */
public class StudentPlayer extends PentagoPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260844079");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    @Override
    public Move chooseMove(PentagoBoardState boardState) {
        final boolean DEBUG = false;
        SimpleHeuristics simpleHeuristics = new SimpleHeuristics();
        Move winningMove = simpleHeuristics.getNextMove(boardState);

        long start = System.currentTimeMillis();
        if (winningMove != null) {
            //noinspection ConstantConditions
            if (DEBUG) {
                System.out.printf("Time for Move (s): %f%n", (System.currentTimeMillis() - start) / 1000f);
                boardState.printBoard();
            }
            return winningMove;
        }

        final int DEPTH = 3;
        AlphaBetaPrune optimizer = new AlphaBetaPrune();
        Move myMove = optimizer.getNextBestMove(DEPTH, boardState, boardState.getTurnPlayer());
        float timeElapsed = (System.currentTimeMillis() - start) / 1000f;

        if (DEBUG) {
            System.out.println(myMove.toPrettyString());
            boardState.printBoard();
            System.out.printf("Time for Move (s): %f%n", timeElapsed);
        }

        return myMove;
    }
}