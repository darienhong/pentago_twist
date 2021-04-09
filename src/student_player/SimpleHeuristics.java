package student_player;

import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;
import java.util.*;

public class SimpleHeuristics {

    SimpleHeuristics(){
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
