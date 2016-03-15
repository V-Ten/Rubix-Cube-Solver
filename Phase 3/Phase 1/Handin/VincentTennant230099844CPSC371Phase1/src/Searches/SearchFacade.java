package Searches;

import RubixCube.Rubix;
import Searches.Searchable.SearchNode;

/**
 * main.java
 * CPSC371
 * Vincent Tennnant
 * 230099844
 * 
 * Search facade where all of the searches are called from.
 */
public class SearchFacade {

    private static Rubix goal;

    public static SearchNode search(int cube_size, int search_to_perform, Rubix cube) {
        goal = new Rubix(cube_size);

        switch(search_to_perform) {
            //A* search
            case 0: return AStarSearch.ASS(cube, goal);

            //Breadth First Search is the default search
            default: return BreadthFirstSearch.BFS(cube, goal);
        }
    }
}
