package Searches;

import RubixCube.Rubix;
import Searches.Searchable.SearchNode;
import Searches.Searchable.SearchNodeComparator;

import java.util.*;

/**
 * main.java
 * CPSC371
 * Vincent Tennant
 * 230099844
 * 
 * A* Search. When given a rubix cube, and the ribix cubes goal state, A* search will
 * attempt to find the solution using the shortest path possible.
 * Interestingly enough 
 */
public class AStarSearch {

    private static SearchNode in_open_list;

    /**
     * The main search method.
     *
     * @param start The start state. The rubix cube passed in the unsolved state.
     * @param goal  The solved rubix cube that will be used to determine when the cube is in a solved state.
     * @return Returns a search node containing the solved cube, its parent state and the move made by the parent
     * to reach the the solved state. The search nodes parent is also a search node. Following the parents until
     * the root node is reached allows the winning moves to become known.
     */
    public static SearchNode ASS(Rubix start, Rubix goal) {
        SearchNode root = new SearchNode(start.getCubeSize(), start);

        //create the open list and the closed list
        //LinkedList<SearchNode> open_list = new LinkedList<SearchNode>();
        Comparator<SearchNode> comp = new SearchNodeComparator();
        TreeSet<SearchNode> open_list = new TreeSet<SearchNode>(comp);

        //closed list is a hash map in order to facilitate for quick seaching of the closed list
        HashMap<String, SearchNode> closed_list = new HashMap<String, SearchNode>();

        open_list.add(root);

        while (open_list.first().getCube().compareTo(goal) == 0) {
            //removes the first element from the open list and sets it to the current node to expand
            SearchNode current = open_list.pollFirst();
            //add current to the closed list right away
            closed_list.put(current.getKey(), current);

            //generate a list of children from the current node
            LinkedList<SearchNode> child_list = current.generateChildren();

            //for each of the children in the child list do the following
            for (SearchNode aChild : child_list) {
                boolean add_child = true;

                //check to see if the child is in the closed list already
                if (closed_list.containsKey(aChild.getKey())) {
                    add_child = false;
                    //check to see if the g value for this child is less than the g value of the child already
                    // in the closed list
                    if (aChild.gValue() < closed_list.get(aChild.getKey()).gValue()) {
                        //if yes then this child is closer to the goal than the child already in the closed list
                        //remove the matching element from the closed list
                        closed_list.remove(aChild.getKey());
                        add_child = true;
                    }
                }
                if (add_child) {

                    if (inOpenList(aChild, open_list)) {
                        if (aChild.gValue() < in_open_list.gValue()) {
                            open_list.remove(in_open_list);
                            open_list.add(aChild);
                        }
                    }
                    else
                        open_list.add(aChild);
                }
            }
        }
        return open_list.pollFirst();
    }

    /**
     * Checks to see if the child node is already in the open_list by iterating 
     * through the entire list, unless a match is found.
     * If it is then return true.
     * @param child The child to look for
     * @param open_list the open list to search in
     * @return true if the child is in the open list, false if it's not.
     */
    private static boolean inOpenList(SearchNode child, TreeSet<SearchNode> open_list) {
        for (SearchNode anOpenList : open_list) {
            if (child.getKey().equals(anOpenList.getKey())) {
                in_open_list = anOpenList;
                return true;
            }
        }
        return false;
    }
}
