package Searches.Searchable;

import Enums.Colour;
import Enums.Direction;
import RubixCube.Rubix;

import java.util.LinkedList;

/**
 * main.java
 * CPSC371
 * Vincent Tennant
 * 230099844
 *
 * Search nodes object that are used by BFS and A* search to search a rubix cube state
 * to find/determine the correct steps to solve the cube.
 */
public class SearchNode {

    private SearchNode parent;

    private Rubix cube;
    private Direction dir;
    private int axis;
    private int slice;
    private int cube_size;

    //values used for the heuristic. f=h+g
    //where h is the calculated heuristic and g is the distance traveled so far.
    private double g;
    private double h;
    private double f;

    private Direction cw = Direction.CLOCKWISE;
    private Direction ccw = Direction.COUNTERCLOCKWISE;

    /**
     * Constructor for the root search node.
     * For a rubix cube.
     * @param size Size of the cube
     * @param c The starting cube state
     */
    public SearchNode(int size, Rubix c) {
        cube_size = size;
        cube = c;

        //everything else is null or -1
        parent = null;
        dir = null;
        axis = -1;
        slice = -1;

        //distance travelled so far is 0
        g = 0;
        h = cube.getHeuristic();
        f = g + h;
    }

    /**
     * Constructor for child nodes
     * For a rubix cube.
     * @param size Size of the cube
     * @param c The cube to be copied
     * @param axis_num the axis number to rotate about
     * @param d Direction of the rotation, cw or ccw
     * @param slice_num the slice number to rotate
     * @param p the parent search node
     * @param parent_g the parents g value. As more children get created the distance they will have travelled
     *                 ie. the number of move that have been made to achieve the cubes state, increases
     *                 with each generation.
     */
    public SearchNode(int size, Rubix c, int axis_num, Direction d, int slice_num, SearchNode p, double parent_g) {
        cube = new Rubix(c);
        cube_size = size;
        axis = axis_num;
        dir = d;
        slice = slice_num;

        parent = p;

        //make the move for the cube based on the move the parent made for this search node
        cube.rotate(axis, dir, slice);

        g = parent_g + 1;
        h = cube.getHeuristic();
        f = g + h;
    }

    /**
     * Generates a list of all possible children from the cube state referenced in this search node.
     * @return Returns a list of all possible children
     */
    public LinkedList<SearchNode> generateChildren() {
        LinkedList<SearchNode> children = new LinkedList<SearchNode>();

        //iterate through each axis
        for(int a = 1; a < 4; a++) {
            //iterate through each slice for the axis
            for(int s = 0; s < cube_size; s++) {
                children.add(new SearchNode(this.cube_size, this.cube, a, cw, s, this, this.g));
                children.add(new SearchNode(this.cube_size, this.cube, a, ccw, s, this, this.g));
            }
        }

        return children;
    }

    /**
     * Returns the move that the parent made to achieve the state of the cube in this Search Node
     * The tuple is of the form [AXIS NUMBER, DIRECTION, SLICE]
     * @return a tuple of the move taken
     */
    public LinkedList moveTaken() {
        //returns the move that the parent search node took in order to achieve the state of the rubix cube here
        LinkedList<Integer> tuple = new LinkedList<Integer>();

        //add the axis number: 1, 2, or 3
        tuple.add(axis);

        //add the direction
        //Clockwise == 1
        //counter-clockwise == 0
        //null == -1
        if(dir == null)
            tuple.add(-1);
        else if(dir.equals(Direction.CLOCKWISE))
            tuple.add(1);
        else
            tuple.add(0);

        //add the slice that was rotated
        tuple.add(slice);

        return tuple;
    }

    /**
     * Getters
     */
    public SearchNode getParent() { return parent; }
    public Rubix getCube() { return cube; }
    public int getSlice() { return slice; }
    public Direction getDirection() { return dir; }
    public int getAxis() { return axis; }
    public String getKey() { return cube.getKey(); }

    public double gValue() { return g; }
    public double hValue() { return h; }
    public double fValue() { return g+cube.getHeuristic(); }
}
