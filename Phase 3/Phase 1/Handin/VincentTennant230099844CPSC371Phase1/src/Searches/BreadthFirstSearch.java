package Searches;

import RubixCube.Rubix;
import Searches.Searchable.SearchNode;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * main.java
 * CPSC371
 * Vincent Tennnant
 * 230099844
 * 
 * Breadth First Search, when given a rubix cube, and the goal state the nBreadth First Search 
 * will attempt to find the solution to the given cube using the shortest path possible(ie. fewest moves). 
 */
public class BreadthFirstSearch {

    /**
     * Breadth first Searches from the start state to the goal state.
     * @param start starting state
     * @param goal goal state
     * @return returns the goal state
     */
    public static SearchNode BFS(Rubix start, Rubix goal) {
        SearchNode root = new SearchNode(start.getCubeSize(), start);

        //create the open list and the closed lists to be used for beadth first search
        LinkedList<SearchNode> open_list = new LinkedList<SearchNode>();
        HashMap<String, SearchNode> closed_list = new HashMap<String, SearchNode>();

        //append the starting Search Node to the open list
        //need to start somewhere
        open_list.add(root);
        SearchNode current;

        //while the open list is not empty do the following
        while(!open_list.isEmpty()){
            //set the current node to the head of the open_list, remove from the open_list
            current = open_list.remove();

            closed_list.put(current.getKey(), current);

            //compare current cube ot the goal cube
            if(current.getCube().compareTo(goal) == 1)
                return current;

            //child list creation
            LinkedList<SearchNode> child_list = current.generateChildren();

            //for each child in child_list
            for (SearchNode aChild_list : child_list) {
                boolean add_child = true;

                //for each item in the open list; don't add the child if it's already there!
                for (SearchNode anOpen_list : open_list) {
                    if (aChild_list.getCube().compareTo(anOpen_list.getCube()) == 1)
                        add_child = false;
                }

                if (add_child) {
                    //if the child is already in the closed list then do not add the child
                    if(closed_list.containsKey(aChild_list.getKey()))
                        add_child = false;
                }

                if (add_child)
                    open_list.add(aChild_list);
            }
        }

        return null;
    }

}
