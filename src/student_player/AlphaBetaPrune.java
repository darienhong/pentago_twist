package student_player;

import boardgame.Board;
import boardgame.BoardState;
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

    AlphaBetaPrune(){
        new AlphaBetaPrune(Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.heuristic = new ABHeuristic();
    }

    PentagoMove getNextBestMove(int depth, PentagoBoardState boardState, int currPlayer){
        return prune(depth, boardState, currPlayer, this.initialAlpha, this.initialBeta).getValue();
    }

    private AbstractMap.SimpleImmutableEntry<Integer, PentagoMove> prune(int depth, PentagoBoardState boardState, int currPlayer, int alpha, int beta) {

        ArrayList<PentagoMove> allMoves = boardState.getAllLegalMoves();
        PentagoMove bestMove = allMoves.get(0);
        int bestScore;

        if (depth == 0 || allMoves.isEmpty()) {
            bestScore = this.heuristic.computeHeuristic(boardState, currPlayer, alpha, beta);
            return new AbstractMap.SimpleImmutableEntry(bestScore, bestMove);

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

    private class ABHeuristic {
        private ABHeuristic() {
            super();
        }

        /**
         * This heuristic computes a better score for pieces that can form one ore more horizontal/vertical/diagonal
         * lines. The longer the uninterrupted line is, the higher the score. Alternatively, if there exists a move that
         * guarantees a win, then that state has the highest score.
         *
         * @param currentBoardState The current state of the board and game
         * @param currPlayer        The current player
         * @return Score for the currentBoardState
         */

        private int computeHeuristic(PentagoBoardState currentBoardState, int currPlayer, int alpha, int beta) {

            int score = currPlayer == PentagoBoardState.WHITE ? alpha : beta;
            int horizontalPieceCount = 0;
            int verticalPieceCount = 0;
            int forwardDiagonalPieceCount = 0;
            int reverseDiagonalPieceCount = 0;

            if (currentBoardState.getWinner() == Board.NOBODY) {

                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 5; j++) {

                        //horizontal lines
                        Piece piece = currentBoardState.getPieceAt(i, j);
                        Piece neighbour = currentBoardState.getPieceAt(i, j + 1);

                        if (piece == Piece.WHITE && neighbour == Piece.WHITE) {
                            score += Math.pow(2, horizontalPieceCount);
                            horizontalPieceCount++;
                        } else if (piece == Piece.BLACK && neighbour == Piece.BLACK) {
                            score -= Math.pow(2, horizontalPieceCount);
                            horizontalPieceCount++;
                        } else {
                            horizontalPieceCount = 0;
                        }

                        // Vertical lines
                        piece = currentBoardState.getPieceAt(j, i);
                        neighbour = currentBoardState.getPieceAt(j + 1, i);

                        if (piece == Piece.WHITE && neighbour == Piece.WHITE) {
                            score += Math.pow(2, verticalPieceCount);
                            verticalPieceCount++;
                        } else if (piece == Piece.BLACK && neighbour == Piece.BLACK) {
                            score -= Math.pow(2, verticalPieceCount);
                            verticalPieceCount++;
                        } else {
                            verticalPieceCount = 0;
                        }

                    }
                }


                for (int diagIdx = 0; diagIdx < 5; diagIdx++) {

                    // forward diagonal
                    Piece currPiece = currentBoardState.getPieceAt(diagIdx, diagIdx);
                    Piece neighbour = currentBoardState.getPieceAt(diagIdx + 1, diagIdx + 1);

                    if (currPiece == Piece.WHITE && neighbour == Piece.WHITE) {
                        score += Math.pow(2, forwardDiagonalPieceCount);
                        forwardDiagonalPieceCount++;
                    } else if (currPiece == Piece.BLACK && neighbour == Piece.BLACK) {
                        score -= Math.pow(2, forwardDiagonalPieceCount);
                        forwardDiagonalPieceCount++;
                    } else {
                        forwardDiagonalPieceCount = 0;
                    }

                    // reverse diagonal
                    currPiece = currentBoardState.getPieceAt(diagIdx, 5 - diagIdx);
                    neighbour = currentBoardState.getPieceAt(diagIdx + 1, 4 - diagIdx);

                    if (currPiece == Piece.WHITE && neighbour == Piece.WHITE) {
                        score += Math.pow(2, reverseDiagonalPieceCount);
                        reverseDiagonalPieceCount++;
                    } else if (currPiece == Piece.BLACK && neighbour == Piece.BLACK) {
                        score -= Math.pow(2, reverseDiagonalPieceCount);
                        reverseDiagonalPieceCount++;
                    } else {
                        reverseDiagonalPieceCount = 0;
                    }
                }

            } else {
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
