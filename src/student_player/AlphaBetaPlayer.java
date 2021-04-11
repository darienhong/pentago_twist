package student_player;

import boardgame.Move;
import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoPlayer;

public class AlphaBetaPlayer extends PentagoPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public AlphaBetaPlayer() {
        super("AlphaBetaPlayer");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    @Override
    public Move chooseMove(PentagoBoardState boardState) {

        final boolean TESTING = false;
        SimpleHeuristics simpleHeuristics = new SimpleHeuristics();
        Move winningMove = simpleHeuristics.getNextMove(boardState);

        long start = System.currentTimeMillis();
        if (winningMove != null) {

            if (TESTING) {
                System.out.printf("Time for Move (s): %f%n", (System.currentTimeMillis() - start) / 1000f);
                boardState.printBoard();
            }
            return winningMove;
        }

        final int DEPTH = 3;
        AlphaBetaPrune optimizer = new AlphaBetaPrune();
        Move myMove = optimizer.getNextBestMove(DEPTH, boardState, boardState.getTurnPlayer());
        float timeElapsed = (System.currentTimeMillis() - start) / 1000f;

        if (TESTING) {
            System.out.printf("Time for Move (s): %f%n", timeElapsed);
            System.out.println(myMove.toPrettyString());
            boardState.printBoard();
        }

        return myMove;
    }
}
