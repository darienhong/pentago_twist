package student_player.mcts;

import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class to represent a Node in the Monte Carlo Search Tree
 */
public class MonteCarloNode {
    private MonteCarloState state;
    private MonteCarloNode parent;
    private ArrayList<MonteCarloNode> children;

    MonteCarloNode(MonteCarloState monteCarloState){
        this.state = monteCarloState;
        this.children = new ArrayList<>();
    }

    MonteCarloNode(PentagoBoardState boardState, PentagoMove move){
        this.state = new MonteCarloState(boardState, move);
        this.children = new ArrayList<>();
    }

    MonteCarloNode(MonteCarloState monteCarloState, MonteCarloNode parent, ArrayList<MonteCarloNode> children){
        this.state = monteCarloState;
        this.parent = parent;
        this.children = children;
    }

    MonteCarloNode(MonteCarloNode aNode){
        this.state = new MonteCarloState(aNode.getState());
        this.children = new ArrayList<>();
        if (aNode.getParent() != null) {
            this.parent = aNode.getParent();
        }
        ArrayList<MonteCarloNode> nodeChildren = aNode.getChildren();
        for (MonteCarloNode child: nodeChildren) {
            this.children.add(new MonteCarloNode(child));
        }
    }

    MonteCarloState getState(){
        return state;
    }

    MonteCarloNode getParent(){
        return parent;
    }

    ArrayList<MonteCarloNode> getChildren(){
        return children;
    }

    void setState(MonteCarloState state){
        this.state = state;
    }

    void setParent(MonteCarloNode parent){
        this.parent = parent;
    }

    void setChildren(ArrayList<MonteCarloNode> children){
        this.children = children;
    }

    /**
     *
     * @return child with largest visit count
     */
    MonteCarloNode getMaxScoreChild(){
        return Collections.max(this.children, Comparator.comparing(c -> c.getState().getVisitCount()));
    }

    MonteCarloNode getRandomChild(){
        int noChildren = this.children.size();
        int randomIdx = (int) (Math.random()*noChildren);
        return this.children.get(randomIdx);
    }

}
