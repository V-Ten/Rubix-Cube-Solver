import Enums.Direction;
import RubixCube.Rubix;
import Searches.SearchFacade;
import Searches.Searchable.SearchNode;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

/**
 * main.java
 * CPSC371
 * Vincent Tennant
 * 230099844
 * 
 * This is the main class. The text based gui is ran here. 
 */
public class main {
    public static void main(String[] args) {

        intro();
        System.out.println("What would you like to do?\n\n1. Unit Test\n2. Create, Manipulate, and Solve A Cube\n" +
                "3. exit\n");

        int todo = getInput();

        while(true) {
            switch(todo) {
                case 1: unitTests();        break;
                case 2: userCube();         break;
                case 3: System.exit(1);     break;
                case 99: expirements();     break;
                default: System.out.println("That isn't an option.");
            }

            System.out.println("What would you like to do?\n\n1. Unit Test\n2. Create, Manipulate, and Solve A Cube\n" +
                    "3. exit\n");
            todo = getInput();
        }
    }

    /**
     * Here is where the 4 basic unit tests required will be able to be executed.
     */
    private static void unitTests() {
        boolean running = true;

        //TODO THE UNIT TESTS
        System.out.println("The basic unit tests required can be ran here. Other unit tests can be performed " +
                "separately.");

        int todo;

        while(running) {
            System.out.println("What would you like to do?\n\n1. Show that a cube of size 2 with various move applied " +
                "can be returned to the goal state\n2. Show that a cube of size 3 with various move applied to it " +
                "can be returned to the goal state\n3. Show that a cube of size 2 with three moves applied, A* search" +
                "can solve the cube\n4. Show that a cube of size n with 3 move applied, A* can solve the " +
                "cube NOTE: The larger the cube the longer it may take\n5. Go Back");

            todo = getInput();

            switch(todo) {

                case 1: Rubix cube = new Rubix(2);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 1, slice 0:");
                    cube.rotate(1, Direction.CLOCKWISE, 0);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 1, slice 1:");
                    cube.rotate(1, Direction.CLOCKWISE, 1);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 2, slice 0:");
                    cube.rotate(2, Direction.CLOCKWISE, 0);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 2, slice 1:");
                    cube.rotate(2, Direction.CLOCKWISE, 1);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 3, slice 0:");
                    cube.rotate(3, Direction.CLOCKWISE, 0);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 3, slice 1:");
                    cube.rotate(3, Direction.CLOCKWISE, 1);
                    System.out.println(cube.toString());

                    System.out.println("Now rotating counter-clockwise.\n");

                    System.out.println("Rotating the cube about axis 1, slice 0:");
                    cube.rotate(1, Direction.COUNTERCLOCKWISE, 0);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 1, slice 1:");
                    cube.rotate(1, Direction.COUNTERCLOCKWISE, 1);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 2, slice 0:");
                    cube.rotate(2, Direction.COUNTERCLOCKWISE, 0);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 2, slice 1:");
                    cube.rotate(2, Direction.COUNTERCLOCKWISE, 1);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 3, slice 0:");
                    cube.rotate(3, Direction.COUNTERCLOCKWISE, 0);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 3, slice 1:");
                    cube.rotate(3, Direction.COUNTERCLOCKWISE, 1);
                    System.out.println(cube.toString());

                    System.out.println("Now rotating back to goal state.\n");

                    System.out.println("Rotating the cube about axis 3, slice 1:");
                    cube.rotate(3, Direction.CLOCKWISE, 1);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 3, slice 0:");
                    cube.rotate(3, Direction.CLOCKWISE, 0);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 2, slice 1:");
                    cube.rotate(2, Direction.CLOCKWISE, 1);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 2, slice 0:");
                    cube.rotate(2, Direction.CLOCKWISE, 0);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 1, slice 1:");
                    cube.rotate(1, Direction.CLOCKWISE, 1);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 1, slice 0:");
                    cube.rotate(1, Direction.CLOCKWISE, 0);
                    System.out.println(cube.toString());

                    System.out.println("Rotating the cube about axis 3, slice 1:");
                    cube.rotate(3, Direction.COUNTERCLOCKWISE, 1);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 3, slice 0:");
                    cube.rotate(3, Direction.COUNTERCLOCKWISE, 0);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 2, slice 1:");
                    cube.rotate(2, Direction.COUNTERCLOCKWISE, 1);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 2, slice 0:");
                    cube.rotate(2, Direction.COUNTERCLOCKWISE, 0);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 1, slice 1:");
                    cube.rotate(1, Direction.COUNTERCLOCKWISE, 1);
                    System.out.println(cube.toString());
                    System.out.println("Rotating the cube about axis 1, slice 0:");
                    cube.rotate(1, Direction.COUNTERCLOCKWISE, 0);
                    System.out.println(cube.toString());
                    break;

                case 2: Rubix cube_three = new Rubix(3);
                    System.out.println("All slices will be rotated clockwise. Then rotated counter-clockwise in " +
                            "reverse order.\n");

                    System.out.println(cube_three.toString());
                    System.out.println("Rotating about axis 1, clockwise, slice 0");
                    cube_three.rotate(1, Direction.CLOCKWISE, 0);
                    System.out.println(cube_three.toString());
                    System.out.println("Rotating about axis 1, clockwise, slice 1");
                    cube_three.rotate(1, Direction.CLOCKWISE, 1);
                    System.out.println(cube_three.toString());
                    System.out.println("Rotating about axis 1, clockwise, slice 2");
                    cube_three.rotate(1, Direction.CLOCKWISE, 2);
                    System.out.println(cube_three.toString());
                    System.out.println("Rotating about axis 2, clockwise, slice 0");
                    cube_three.rotate(2, Direction.CLOCKWISE, 0);
                    System.out.println(cube_three.toString());
                    System.out.println("Rotating about axis 2, clockwise, slice 1");
                    cube_three.rotate(2, Direction.CLOCKWISE, 1);
                    System.out.println(cube_three.toString());
                    System.out.println("Rotating about axis 2, clockwise, slice 2");
                    cube_three.rotate(2, Direction.CLOCKWISE, 2);
                    System.out.println(cube_three.toString());
                    System.out.println("Rotating about axis 3, clockwise, slice 0");
                    cube_three.rotate(3, Direction.CLOCKWISE, 0);
                    System.out.println(cube_three.toString());
                    System.out.println("Rotating about axis 3, clockwise, slice 1");
                    cube_three.rotate(3, Direction.CLOCKWISE, 1);
                    System.out.println(cube_three.toString());
                    System.out.println("Rotating about axis 3, clockwise, slice 2");
                    cube_three.rotate(3, Direction.CLOCKWISE, 2);
                    System.out.println(cube_three.toString());

                    System.out.println("Now rotating counter-clockwise in reverse.\n");
                    System.out.println("Rotating about axis 3, clockwise, slice 2");
                    cube_three.rotate(3, Direction.COUNTERCLOCKWISE, 2);
                    System.out.println(cube_three.toString());
                    System.out.println("Rotating about axis 3, clockwise, slice 1");
                    cube_three.rotate(3, Direction.COUNTERCLOCKWISE, 1);
                    System.out.println(cube_three.toString());
                    System.out.println("Rotating about axis 3, clockwise, slice 0");
                    cube_three.rotate(3, Direction.COUNTERCLOCKWISE, 0);
                    System.out.println(cube_three.toString());
                    System.out.println("Rotating about axis 2, clockwise, slice 2");
                    cube_three.rotate(2, Direction.COUNTERCLOCKWISE, 2);
                    System.out.println(cube_three.toString());
                    System.out.println("Rotating about axis 2, clockwise, slice 1");
                    cube_three.rotate(2, Direction.COUNTERCLOCKWISE, 1);
                    System.out.println(cube_three.toString());
                    System.out.println("Rotating about axis 2, clockwise, slice 0");
                    cube_three.rotate(2, Direction.COUNTERCLOCKWISE, 0);
                    System.out.println(cube_three.toString());
                    System.out.println("Rotating about axis 1, clockwise, slice 2");
                    cube_three.rotate(1, Direction.COUNTERCLOCKWISE, 2);
                    System.out.println(cube_three.toString());
                    System.out.println("Rotating about axis 1, clockwise, slice 1");
                    cube_three.rotate(1, Direction.COUNTERCLOCKWISE, 1);
                    System.out.println(cube_three.toString());
                    System.out.println("Rotating about axis 1, clockwise, slice 0");
                    cube_three.rotate(1, Direction.COUNTERCLOCKWISE, 0);
                    System.out.println(cube_three.toString());
                    break;

                case 3: Rubix cube_solve = new Rubix(2);
                    //make some moves
                    cube_solve.rotate(1, Direction.CLOCKWISE, 0);
                    cube_solve.rotate(2, Direction.COUNTERCLOCKWISE, 1);
                    cube_solve.rotate(3, Direction.CLOCKWISE, 1);

                    System.out.println(cube_solve.toString());

                    System.out.println("Solving...");

                    solveCube(cube_solve);
                    break;

                case 4: System.out.println("Cube size?");
                    int size = getInput();
                    Rubix n_cube = new Rubix(size);

                    rotateCube(n_cube);
                    rotateCube(n_cube);
                    rotateCube(n_cube);

                    System.out.println(n_cube.toString());

                    solveCube(n_cube);
                    break;

                case 5: running = false;

            }
        }
    }

    /**
     * Allows a user to create and manipulate a cube on their own.
     */
    private static void userCube() {
        boolean running = true;

        int size = 0;
        System.out.println("What size of cube would you like to create? ");
        size = getInput();

        Rubix cube = new Rubix(size);

        int todo;

        while(running) {
            System.out.println(cube.toString());

            System.out.println("What would you like to do?\n1. Rotate\n2. Create New Cube\n3. Solve the Current Cube" +
                    "\n4. Go Back");

            todo = getInput();

            switch(todo) {
                case 1: rotateCube(cube);       break;
                case 2: cube = newCube();       break;
                case 3: solveCube(cube);        break;
                case 4: running = false;        break;
            }
        }
    }

    /**
     * Solves the passed cube, if possible
     * @param cube The cube to be solved
     */
    public static void solveCube(Rubix cube) {
        //obtain the solved state
        SearchNode solved = SearchFacade.search(cube.getCubeSize(), 0, cube);

        System.out.println("Solved Cube: \n"+solved.getCube().toString());

        //recurse through the parents to find the moves required to solve the cube
        LinkedList<LinkedList> winning_moves = new LinkedList<LinkedList>();

        //obtain the search nodes parents
        while (!(solved.getParent() == null)) {
            winning_moves.add(solved.moveTaken());
            solved = solved.getParent();
        }

        int t = 1;
        //prints the moves that the user should make to solve the cube
        for(int i = winning_moves.size()-1; i >= 0; i--) {
            System.out.print("Step"+ t++ +": ");
            printTuple(winning_moves.get(i));
        }
    }

    /**
     * creates a new cube for the userCube method
     * @return
     */
    public static Rubix newCube() {
        System.out.println("What size of a cube?");
        int size = getInput();
        return new Rubix(size);
    }

    /**
     * Rotates a cube for the user.
     * @param cube The cube to be rotated
     */
    public static void rotateCube(Rubix cube) {
        int axis;
        int slice;
        String direction;
        Direction dir = Direction.CLOCKWISE;

        System.out.println("What axis to rotate about? 1/2/3");
        axis = getInput();
        System.out.println("What slice to rotate? \nNOTE Slices are zero indexed."
                + "Using a cube of size 2, the slices would be 0 and 1");
        slice = getInput();
        System.out.println("What direction? cw/ccw");
        direction = getStringInput();

        if(direction.equals("cw") || direction.equals("ccw"))
            dir = Direction.CLOCKWISE;
        if(direction.equals("ccw") || direction.equals("CCW"))
            dir = Direction.COUNTERCLOCKWISE;

        cube.rotate(axis, dir, slice);
    }

    /**
     * This method will be used to perform the required expirements, and output their results to a .txt file
     * There will be a total of 80 expiremnets in total.
     * For K = 1..8
     * 10 times create a rubix cube of size 2, apply k random moves on the cube, Use A* search to find the shortest
     * path from the start state to the goal state. Record teh moves taken for later use(ie. a .txt file)
     */
    private static void expirements() {
        //single move on cube
        FileWriter experiments = new FileWriter("TEST.txt");

        Rubix cube;

        for(int i = 1; i < 5; i++) {
            System.out.println("-------------------------------------------------------");
            System.out.println("Doing Experiment "+i);
            System.out.println("-------------------------------------------------------");
            for(int j = 0; j < 10; j++) {
                System.out.println(j);
                //create and move cube
                cube = new Rubix(2);

                for(int k = 0; k < i; k++)
                    randomRotate(cube);

                //print unsolved cube to the file
                experiments.writeToFile(cube.getKey()+"_");

                //solve cube
                SearchNode solved = SearchFacade.search(2, 0, cube);

                //get output
                //recurse through the parents to find the moves required to solve the cube
                LinkedList<LinkedList> winning_moves = new LinkedList<LinkedList>();

                //obtain the search nodes parents
                while (!(solved.getParent() == null)) {
                    winning_moves.add(solved.moveTaken());
                    solved = solved.getParent();
                }

                //print the moves to solve the cube to the file
                for(int l = winning_moves.size()-1; l >= 0; l--)
                    experiments.writeToFile(winning_moves.get(l).toString()+"_");

                experiments.newLine();
                experiments.newLine();
            }
        }

        experiments.closeWriter();

    }

    /**
     * Uses a pseaudo random number generator to make random moves on a given rubix cube. Due to the nature of
     * java's pseudo random number generator some patterns may occur unexpectedly.
     * @param cube The cube with which the rotations will be performed on.
     */
    public static void randomRotate(Rubix cube) {
        Random rand = new Random();

        int num_slices = cube.getCubeSize();

        Direction direction;

        //axis is a pseudo random number between 1 and 3
        int axis = rand.nextInt(3) + 1;
        //slice is a pseaudo random number between 0 and the number of slices-1
        int slice = rand.nextInt(num_slices);
        //direction is either 0 or 1
        int dir = rand.nextInt(2);

        //decide direction or rotation
        if(dir == 1)
            direction = Direction.CLOCKWISE;
        else
            direction = Direction.COUNTERCLOCKWISE;

        //rotate the cube
        cube.rotate(axis, direction, slice);
    }

    /**
     * Gets input from teh user in the form of a string.
     * @return The input of the user as a String
     */
    public static String getStringInput() {
       Scanner in = new Scanner(System.in);
        return in.next();
    }

    /**
     * Waits for the user to input an integer
     * This method(sadly and wrongly) assumes the user won;t make any mistakes with the gui.
     * @return Retruns the integer the user input for use
     */
    public static int getInput() {
        Scanner in = new Scanner(System.in);

        return in.nextInt();
    }

    /**
     * Performs same action as printTuple, only instead of printing it, the tuple is returned as a string.
     * @param tuple The tuple to print
     * @return The tuple in string form.
     */
    public static String getTuple(LinkedList<Integer> tuple) {
        String dir = "";
        if(tuple.get(1) == 1)
            dir = "Clockwise";
        else if(tuple.get(1) == 0)
            dir = "Counter-Clockwise";
        else
            dir = "no dir :O";

        return "[ Axis "+tuple.get(0)+" , "+dir+" , Slice "+tuple.get(2)+" ]";
    }

    /**
     * Prints move taken tuples in an easily readable manor.
     * Tuple position 0 is the Axis rotated about.
     * Position 1 is the direction.
     * Position 2 is the slice rotated.
     * @param tuple The tuple to print
     */
    public static void printTuple(LinkedList<Integer> tuple) {
        String dir = "";
        if(tuple.get(1) == 1)
            dir = "Clockwise";
        else if(tuple.get(1) == 0)
            dir = "Counter-Clockwise";
        else
            dir = "no dir :O";

        System.out.println("[ Axis "+tuple.get(0)+" , "+dir+" , Slice "+tuple.get(2)+" ]");
    }

    /**
     * Prints an intro :/
     */
    public static void intro() {
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Project Phase One\nCPSC 377\nVincent Tennant\n230099844");
        System.out.println("This program will use either Breadth first search or A* search in order to find\n" +
                "the best solution for a rubix cube in a given state. \nTo use this text based gui, navigate by" +
                "either choosing the number corresponding to your desired option, or follow the directions.");
    }
}
