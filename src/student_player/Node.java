package student_player;

import boardgame.BoardState;
import java.util.*;
import pentago_twist.PentagoBoardState;

public class Node {
    BoardState state;
    Node parent;
    List<Node> children;

    public Node(){
        children = new ArrayList<>();
    }

}
