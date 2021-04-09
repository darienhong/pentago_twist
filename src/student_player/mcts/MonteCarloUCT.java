package student_player.mcts;

import java.util.*;

public class MonteCarloUCT {

    private static final double scalingConstant = Math.sqrt(2);

    /**
     *
     * @param totalVisit no times parent was visited
     * @param nodeWinScore value of taking that node
     * @param nodeVisit times node was visited
     * @return UCT value of node
     */
    public static double calcUCT(int totalVisit, double nodeWinScore, int nodeVisit){
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }

        return (nodeWinScore / (double) nodeVisit) + scalingConstant*Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    /**
     *
     * @param node
     * @return Child with best UCT value
     */
    static MonteCarloNode findBestUCTNode(MonteCarloNode node){
        int parentVisit = node.getState().getVisitCount();
        return Collections.max(node.getChildren(),
                Comparator.comparing(c -> calcUCT(parentVisit, c.getState().getWinScore(), c.getState().getVisitCount())));
    }


}
