package student_player.mcts;

import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;
import java.util.*;

public class MonteCarloState {
    private PentagoBoardState boardState;
    private PentagoMove pentagoMove;
    private int playerNo;
    private int visitCount;
    private double winScore;

    MonteCarloState(MonteCarloState state){
        this.boardState = state.getBoardState();
        this.pentagoMove = state.getPentagoMove();
        this.playerNo = state.getPlayerNo();
        this.visitCount = state.getVisitCount();
        this.winScore = state.getWinScore();
    }

    MonteCarloState(PentagoBoardState boardState, PentagoMove pentagoMove){
        this.boardState = boardState;
        this.pentagoMove = pentagoMove;
    }

    PentagoBoardState getBoardState(){
        return boardState;
    }

    int getPlayerNo(){
        return playerNo;
    }

    int getVisitCount(){
        return visitCount;
    }

    int getOpponent(){
        return this.boardState.getOpponent();
    }

    PentagoMove getPentagoMove(){
        return this.pentagoMove;
    }

    double getWinScore(){
        return winScore;
    }

    void setPlayerNo(int playerNum){
        this.playerNo = playerNum;
    }

    void setBoardState(PentagoBoardState boardState){
        this.boardState = boardState;
    }

    void setVisitCount(int visitCount){
        this.visitCount = visitCount;
    }

    void setWinScore(double winScore){
        this.winScore = winScore;
    }

    ArrayList<MonteCarloState> getPossibleStates(){
        ArrayList<MonteCarloState> possibleStates = new ArrayList<>();
        ArrayList<PentagoMove> allMoves = this.boardState.getAllLegalMoves();
        for (PentagoMove move : allMoves) {
            PentagoBoardState newBoard = (PentagoBoardState) boardState.clone();
            MonteCarloState newState = new MonteCarloState(newBoard, move);
            newState.setPlayerNo(newBoard.getOpponent());
            newState.getBoardState().processMove(move);

            possibleStates.add(newState);
        }

        return possibleStates;
    }


    void addVisit(){
        this.visitCount++;
    }

    void addScore(double newScore){
        if (this.winScore != Integer.MIN_VALUE) {
            this.winScore += newScore;
        }
    }

    void changePlayer(){
        this.playerNo = this.playerNo == PentagoBoardState.WHITE ? PentagoBoardState.BLACK : PentagoBoardState.WHITE;
    }

    void performRandomMove(){
        Random rand = new Random();
        ArrayList<PentagoMove> allMoves = this.boardState.getAllLegalMoves();
        int randomIdx = rand.nextInt(allMoves.size());
        PentagoMove randomMove = allMoves.get(randomIdx);
        this.boardState.processMove(randomMove);
        changePlayer();
    }
}
