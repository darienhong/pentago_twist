package student_player;

import boardgame.Move;
import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoPlayer;

public class AlphaBetaPlayer extends PentagoPlayer {

    public AlphaBetaPlayer() {
        super("Alpha Beta Player");
    }

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
