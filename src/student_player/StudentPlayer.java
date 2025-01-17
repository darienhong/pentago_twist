package student_player;

import boardgame.Move;
import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoPlayer;

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
        boolean TESTING = false; // in debug mode

        // Use the One-Move-Win Heuristic first to see if there is a move that guarantees a win
        OneMoveHeuristic heuristic = new OneMoveHeuristic();
        Move winningMove = heuristic.getNextMove(boardState);

        long start = System.currentTimeMillis();
        if (winningMove != null) {
            if (TESTING) {
                System.out.printf("Time to select move (s): %f%n", (System.currentTimeMillis() - start) / 1000f);
                boardState.printBoard();
            }
            return winningMove;
        }

        // if there is no move that guarantees a win, then use AB Pruning to select next best move
        final int DEPTH = 3;
        AlphaBetaPrune alphaBeta = new AlphaBetaPrune();
        Move myMove = alphaBeta.getNextBestMove(DEPTH, boardState, boardState.getTurnPlayer());
        float timeElapsed = (System.currentTimeMillis() - start) / 1000f;

        if (TESTING) {
            System.out.printf("Time to select move (s): %f%n", timeElapsed);
            System.out.println(myMove.toPrettyString());
            boardState.printBoard();
        }

        return myMove;
    }
}