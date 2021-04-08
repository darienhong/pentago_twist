package student_player;

import java.util.*;

public class MonteCarloUCT {

    private static final double scalingConstant = Math.sqrt(2);

    public static double calcUCT(int totalVisit, double nodeWinScore, int nodeVisit){
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }

        return (nodeWinScore / (double) nodeVisit) + scalingConstant*Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    static MonteCarloNode findBestNodeWithUCT(MonteCarloNode node){
        int p
    }


}
