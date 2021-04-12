package student_player;

import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;
import java.util.*;

/**
 * Simple One-Move-Win heuristic function
 *      This one-move-win Heuristic loops through all the possible moves for the given state and
 *      plays the move that would yield the current player a win. This heuristic is computed before
 *      the search algorithm is run as it would guarantee a win instantly if one exists. If such a move
 *      doesn't exist currently, then the search algorithm is called to find the best move.
 *
 */
public class OneMoveHeuristic {

    OneMoveHeuristic(){
        super();
    }

    PentagoMove getNextMove(PentagoBoardState pentagoBoardState){
        ArrayList<PentagoMove> possibleMoves = pentagoBoardState.getAllLegalMoves();
        for (PentagoMove move: possibleMoves) {
            PentagoBoardState boardStateClone = (PentagoBoardState) pentagoBoardState.clone();
            boardStateClone.processMove(move);
            if (boardStateClone.getWinner() == pentagoBoardState.getTurnPlayer()) {
                return move;
            }
        }
        return null;
    }
}
