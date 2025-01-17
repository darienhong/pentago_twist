package student_player;

import boardgame.Board;
import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoBoardState.Piece;
import pentago_twist.PentagoMove;

import java.util.AbstractMap;
import java.util.ArrayList;

public class AlphaBetaPrune {
    private int initialAlpha;
    private int initialBeta;
    private ABHeuristic heuristic;
    final private long maxSearchTime = System.currentTimeMillis() + 1950;

    AlphaBetaPrune(int alpha, int beta){
        super();
        this.initialAlpha = alpha;
        this.initialBeta = beta;
        this.heuristic = new ABHeuristic();
    }

    /**
     * Default: initialize alpha and beta with their worst values
     */
    AlphaBetaPrune(){
        new AlphaBetaPrune(Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.heuristic = new ABHeuristic();
    }

    private AbstractMap.SimpleImmutableEntry<Integer, PentagoMove> prune(int depth, PentagoBoardState boardState, int currPlayer, int alpha, int beta) {

        ArrayList<PentagoMove> allMoves = boardState.getAllLegalMoves();
        PentagoMove bestMove = allMoves.get(0);
        int bestScore;

        if (depth == 0 || allMoves.isEmpty()) {
            bestScore = this.heuristic.computeUtility(boardState, currPlayer, alpha, beta);
            return new AbstractMap.SimpleImmutableEntry<>(bestScore, bestMove);

        }

        for (PentagoMove move : allMoves) {
            PentagoBoardState currBoardState = (PentagoBoardState) boardState.clone();
            currBoardState.processMove(move);

            if (currPlayer == PentagoBoardState.WHITE) {
                bestScore = prune(depth - 1, currBoardState, PentagoBoardState.BLACK, alpha, beta).getKey();

                // White player is MAX player
                if (bestScore > alpha) {
                    alpha = bestScore;
                    bestMove = move;
                }
            } else {
                bestScore = prune(depth - 1, currBoardState, PentagoBoardState.WHITE, alpha, beta).getKey();

                // Black player is MIN player
                if (bestScore < beta) {
                    beta = bestScore;
                    bestMove = move;
                }
            }

            if (System.currentTimeMillis() >= this.maxSearchTime || alpha >= beta) {
                return new AbstractMap.SimpleImmutableEntry<>(bestScore, bestMove);
            }
        }

        return new AbstractMap.SimpleImmutableEntry<>((currPlayer == PentagoBoardState.WHITE) ? alpha : beta, bestMove);
    }

    PentagoMove getNextBestMove(int depth, PentagoBoardState boardState, int currPlayer){
        return prune(depth, boardState, currPlayer, this.initialAlpha, this.initialBeta).getValue();
    }

    private class ABHeuristic {
        private ABHeuristic() {
            super();
        }

        /**
         * This ABHeuristic computes scores for the current state of the board. Pieces that can form one ore more
         * horizontal/vertical/diagonal lines have a better score. The longer the uninterrupted line is, the higher the score.
         * If there exists a move that guarantees a win, then that state has the highest score.
         *
         * @param currentBoardState The current state of the board and game
         * @param currPlayer        The current player
         * @return Score for the currentBoardState
         */

        private int computeUtility(PentagoBoardState currentBoardState, int currPlayer, int alpha, int beta) {

            // if current player is WHITE, starting score is alpha, else beta
            int score = currPlayer == PentagoBoardState.WHITE ? alpha : beta;
            int horizontalCount = 0;
            int verticalCount = 0;
            int forwardDiagCount = 0;
            int reverseDiagCount = 0;

            if (currentBoardState.getWinner() == Board.NOBODY) {

                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 5; j++) {

                        //horizontal lines
                        Piece piece = currentBoardState.getPieceAt(i, j);
                        Piece neighbour = currentBoardState.getPieceAt(i, j + 1);

                        if (piece == Piece.WHITE && neighbour == Piece.WHITE) {
                            score += Math.pow(2, horizontalCount);
                            horizontalCount++;
                        } else if (piece == Piece.BLACK && neighbour == Piece.BLACK) {
                            score -= Math.pow(2, horizontalCount);
                            horizontalCount++;
                        } else {
                            horizontalCount = 0;
                        }

                        // Vertical lines
                        piece = currentBoardState.getPieceAt(j, i);
                        neighbour = currentBoardState.getPieceAt(j + 1, i);

                        if (piece == Piece.WHITE && neighbour == Piece.WHITE) {
                            score += Math.pow(2, verticalCount);
                            verticalCount++;
                        } else if (piece == Piece.BLACK && neighbour == Piece.BLACK) {
                            score -= Math.pow(2, verticalCount);
                            verticalCount++;
                        } else {
                            verticalCount = 0;
                        }

                    }
                }


                for (int diagIdx = 0; diagIdx < 5; diagIdx++) {

                    // forward diagonal
                    Piece currPiece = currentBoardState.getPieceAt(diagIdx, diagIdx);
                    Piece neighbour = currentBoardState.getPieceAt(diagIdx + 1, diagIdx + 1);

                    if (currPiece == Piece.WHITE && neighbour == Piece.WHITE) {
                        score += Math.pow(2, forwardDiagCount);
                        forwardDiagCount++;
                    } else if (currPiece == Piece.BLACK && neighbour == Piece.BLACK) {
                        score -= Math.pow(2, forwardDiagCount);
                        forwardDiagCount++;
                    } else {
                        forwardDiagCount = 0;
                    }

                    // reverse diagonal
                    currPiece = currentBoardState.getPieceAt(diagIdx, 5 - diagIdx);
                    neighbour = currentBoardState.getPieceAt(diagIdx + 1, 4 - diagIdx);

                    if (currPiece == Piece.WHITE && neighbour == Piece.WHITE) {
                        score += Math.pow(2, reverseDiagCount);
                        reverseDiagCount++;
                    } else if (currPiece == Piece.BLACK && neighbour == Piece.BLACK) {
                        score -= Math.pow(2, reverseDiagCount);
                        reverseDiagCount++;
                    } else {
                        reverseDiagCount = 0;
                    }
                }

            } else {
                // setting the upper and lower bounds of winning
                if (currentBoardState.getWinner() == PentagoBoardState.WHITE) {
                    score = Integer.MAX_VALUE;
                } else {
                    score = Integer.MIN_VALUE;
                }
            }

            // If black is MIN player then invert score
            if (currPlayer == PentagoBoardState.BLACK) {
                score = -score;
            }

            return score;
        }
    }
}
