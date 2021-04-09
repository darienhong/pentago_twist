package student_player.mcts;

import java.util.*;
import boardgame.Board;
import boardgame.Move;
import pentago_twist.PentagoBoardState;

public class MonteCarlo {
    private int opponent;
    private static final int WIN_SCORE = 100;
    private static final int MAX_SEARCH_TIME = 1950;

    public MonteCarlo() {
        super();
    }

    public Move findNextMove(PentagoBoardState boardState) {
        long endTime = System.currentTimeMillis() + MAX_SEARCH_TIME;
        MonteCarloNode root = new MonteCarloNode(boardState, null);
        root.getState().setPlayerNo(boardState.getOpponent());
        this.opponent = boardState.getOpponent();

        while (System.currentTimeMillis() < endTime) {

            // Phase 1 - Selection
            MonteCarloNode promisingNode = findPromisingNode(root);

            // Phase 2 - Expansion
            if (promisingNode.getState().getBoardState().getWinner() == Board.NOBODY) {
                expandNode(promisingNode);
            }

            // Phase 3 - Simulation
            MonteCarloNode exploreNode = promisingNode;
            if (promisingNode.getChildren().size() > 0) {
                exploreNode = promisingNode.getRandomChild();
            }

            int result = simulateRandomPlay(exploreNode);

            // Phase 4 - Update
            backPropagation(exploreNode, result);
        }

        MonteCarloNode winner = root.getMaxScoreChild();
        return winner.getState().getPentagoMove();
    }

    private int simulateRandomPlay(MonteCarloNode node){
        MonteCarloNode tempNode = new MonteCarloNode(node);
        MonteCarloState tempState = tempNode.getState();
        int winner = tempState.getBoardState().getWinner();

        while (winner == Board.NOBODY) {
            tempState.changePlayer();
            tempState.performRandomMove();
            winner = tempState.getBoardState().getWinner();
        }

        if (winner == this.opponent) {
            tempNode.getParent().getState().setWinScore(Integer.MIN_VALUE);
        } else {
            tempNode.getParent().getState().setWinScore(Integer.MAX_VALUE);
        }

        return winner;
    }

    private MonteCarloNode findPromisingNode(MonteCarloNode root){
        MonteCarloNode node = root;
        while (node.getChildren().size() != 0) {
            node = MonteCarloUCT.findBestUCTNode(node);
        }
        return node;
    }

    private void expandNode(MonteCarloNode node){
        ArrayList<MonteCarloState> allPossibleStates = node.getState().getPossibleStates();
        for (MonteCarloState state: allPossibleStates) {
            MonteCarloNode tempNode = new MonteCarloNode(state);
            tempNode.setParent(node);
            tempNode.getState().setPlayerNo(node.getState().getOpponent());
            ArrayList<MonteCarloNode> nodeChildren = node.getChildren();
            nodeChildren.add(tempNode);
            node.setChildren(nodeChildren);
        }
    }

    private void backPropagation(MonteCarloNode exploreNode, int playerNo) {
        MonteCarloNode tempNode = exploreNode;
        while (tempNode != null) {
            tempNode.getState().addVisit();
            if (tempNode.getState().getPlayerNo() == playerNo) {
                tempNode.getState().addScore(WIN_SCORE);
            }
            tempNode = tempNode.getParent();
        }
    }



}
