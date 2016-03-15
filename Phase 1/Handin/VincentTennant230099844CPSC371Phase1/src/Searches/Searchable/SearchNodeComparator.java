package Searches.Searchable;

import java.util.Comparator;

/**
 * Created by Vince on 1/28/2015.
 *
 * A simple comaprator created for the tree set in A* search to use.
 */
public class SearchNodeComparator implements Comparator<SearchNode> {

    /**
     * The overridden compare method. Compares two search nodes f values.
     * @param one
     * @param two
     * @return
     */
    @Override
    public int compare(SearchNode one, SearchNode two) {
        if(one.fValue() < two.fValue())
            return -1;
        else if(one.fValue() > two.fValue())
            return 1;
        return -1;
    }

}
