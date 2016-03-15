package RubixCube;

import Enums.Colour;
import Enums.Direction;

import java.util.ArrayList;

/**
 * main.java
 * CPSC371
 * Vincent Tennant
 * 230099844
 *
 * A Rubix cube class. This class creates an objects that conceptualizes a rubix cube. the class has two different
 * constructors. the first is a constructor that creates a new cube in the goal state given the desired size. The
 * second cube creates a deep copy of the cube passed to it.
 */
public class Rubix implements Comparable<Rubix> {

    //the size of the cube
    private int cube_size;
    private double heuristic;

    //the key that will uniquely identify each cube state.
    private String key;

    //could easily be made a 3D array. Do later. Maybe..
    private Colour[][] face0;
    private Colour[][] face1;
    private Colour[][] face2;
    private Colour[][] face3;
    private Colour[][] face4;
    private Colour[][] face5;

    private Direction cw = Direction.CLOCKWISE;
    private Direction ccw = Direction.COUNTERCLOCKWISE;

    //slice information for each axis
    private ArrayList<Slice> X1;
    private ArrayList<Slice> X2;
    private ArrayList<Slice> X3;

    /**
     * Constructor
     * Initializes the cube into the goal state. This is also the default state
     * @param size cube_size of the rubix cube
     */
    public Rubix(int size) {
        cube_size = size;

        X1 = new ArrayList<Slice>();
        X2 = new ArrayList<Slice>();
        X3 = new ArrayList<Slice>();

        //init the cubes faces
        face0 = new Colour[cube_size][cube_size];
        face1 = new Colour[cube_size][cube_size];
        face2 = new Colour[cube_size][cube_size];
        face3 = new Colour[cube_size][cube_size];
        face4 = new Colour[cube_size][cube_size];
        face5 = new Colour[cube_size][cube_size];

        //init face colours
        for(int i = 0; i < cube_size; i++) {
            for(int j = 0; j < cube_size; j++) {
                face0[i][j] = Colour.W;
                face1[i][j] = Colour.B;
                face2[i][j] = Colour.R;
                face3[i][j] = Colour.O;
                face4[i][j] = Colour.G;
                face5[i][j] = Colour.Y;
            }
        }

        //create the slices
        for(int i = 0; i < cube_size; i++) {
            X1.add(new Slice(1, i, cube_size));
            X2.add(new Slice(2, i, cube_size));
            X3.add(new Slice(3, i, cube_size));
        }

        updateKey();
        heuristic = calcHeuristic();
    }

    /**
     * Constructor
     * Create a copy of the passed cube to this cube. In this case a deep copy is required otherwise java will
     * refernece the old cube nad not copy it.
     * @param cube The cube to base this cube off of.
     */
    public Rubix(Rubix cube) {
        this.cube_size = cube.getCubeSize();

        face0 = new Colour[cube_size][cube_size];
        face1 = new Colour[cube_size][cube_size];
        face2 = new Colour[cube_size][cube_size];
        face3 = new Colour[cube_size][cube_size];
        face4 = new Colour[cube_size][cube_size];
        face5 = new Colour[cube_size][cube_size];

        for(int i = 0; i < cube_size; i++) {
            for(int j = 0; j < cube_size; j++) {
                this.face0[i][j] = cube.getFaceValue(0, i, j);
                this.face1[i][j] = cube.getFaceValue(1, i, j);
                this.face2[i][j] = cube.getFaceValue(2, i, j);
                this.face3[i][j] = cube.getFaceValue(3, i, j);
                this.face4[i][j] = cube.getFaceValue(4, i, j);
                this.face5[i][j] = cube.getFaceValue(5, i, j);
            }
        }

        //get the slice objects
        this.X1 = cube.getSlices(1);
        this.X2 = cube.getSlices(2);
        this.X3 = cube.getSlices(3);

        //create the unique key
        updateKey();
        heuristic = calcHeuristic();
    }

    /**
     * Updates the unique key for this cubes state. This method is called whenever the cbe is created or rotations
     * are performed on the cube.
     */
    public void updateKey() {

        key = "";

        for(int i = 0; i < cube_size; i++) {
            for (int j = 0; j < cube_size; j++) {
                key = key + face0[i][j] + "";
                key = key + face1[i][j] + "";
                key = key + face2[i][j] + "";
                key = key + face3[i][j] + "";
                key = key + face4[i][j] + "";
                key = key + face5[i][j] + "";
            }
        }
    }

    /**
     * Compares two rubix cubes
     * @param cube The other cube to be compared with this rubix cube
     * @return Returns 1 if the cubes are equivalent to one another, 0 if they're different
     */
    @Override
    public int compareTo(Rubix cube) {
        int ans = 0;

        if(this.key.equals(cube.getKey()))
            ans = 1;

        return ans;
    }

    /**
     * Rotates the rubix cube about 1 of 3 axis
     * @param axis The axis about which the cube will be rotated
     * @param dir The direction of rotation, clockwise or counter-clockwise
     * @param slice The slice to be rotated, worth noting that the slices are zero indexed
     */
    public void rotate(int axis, Direction dir, int slice) {

        //rotating about axis X1size
        if(axis == 1) {

            //save colours to be moved
            Colour[][] tmp = new Colour[4][cube_size];
            for(int i = 0; i < cube_size; i++){
                tmp[0][i] = face1[i][slice];    //face1
                tmp[1][i] = face0[i][slice];    //face0
                tmp[2][i] = face4[i][slice];    //face4
                tmp[3][i] = face5[i][slice];    //face5
            }

            if(dir.equals(cw)) {
                for(int i = 0; i < cube_size; i++) {
                    face0[i][slice] = tmp[0][i];
                    face4[i][slice] = tmp[1][i];
                    face5[i][slice] = tmp[2][i];
                    face1[i][slice] = tmp[3][i];
                }
            }

            else {
                for(int i = 0; i < cube_size; i++) {
                    face4[i][slice] = tmp[3][i];
                    face0[i][slice] = tmp[2][i];
                    face1[i][slice] = tmp[1][i];
                    face5[i][slice] = tmp[0][i];
                }
            }

            if(X1.get(slice).is_outer_slice()) {
                if(slice == 0) {
                    face3 = rotate_face(face3, dir);
                }
                else {
                    face2 = rotate_face(face2, dir);
                }
            }
        }

        else if(axis == 2) {

            //save the slice info
            Colour[][] tmp = new Colour[4][cube_size];
            for(int i = 0; i < cube_size; i++) {
                tmp[0][i] = face1[X2.get(slice).get_move(1)][i];    //face1
                tmp[1][i] = face2[i][X2.get(slice).get_move(2)];    //face2
                tmp[2][i] = face3[i][X2.get(slice).get_move(3)];    //face3
                tmp[3][i] = face4[X2.get(slice).get_move(0)][i];    //face4
            }

            if(dir.equals(cw)) {
                //face1 -> face2
                for(int i = 0; i < cube_size; i++) {
                    //face1 -> face2
                    face2[i][X2.get(slice).get_move(2)] = tmp[0][i];
                    //face2 -> face4
                    face4[X2.get(slice).get_move(0)][i] = tmp[1][i];
                    //face4 -> face3
                    face3[i][X2.get(slice).get_move(3)] = tmp[3][i];
                    //face3 -> face1
                    face1[X2.get(slice).get_move(1)][i] = tmp[2][i];
                }
            }

            else {
                for(int i = 0; i < cube_size; i++) {
                    //face1 -> face3
                    face3[i][X2.get(slice).get_move(3)] = tmp[0][i];
                    //face3 -> face4
                    face4[X2.get(slice).get_move(0)][i] = tmp[2][i];
                    //face4 -> face2
                    face2[i][X2.get(slice).get_move(2)] = tmp[3][i];
                    //face2 -> face1
                    face1[X2.get(slice).get_move(1)][i] = tmp[1][i];
                }
            }

            if(X2.get(slice).is_outer_slice()) {
                if(slice == 0) {
                    face5 = rotate_face(face5, dir);
                }
                else {
                    face0 = rotate_face(face0, dir);
                }
            }
        }

        else if(axis == 3) {

            Colour[][] tmp = new Colour[4][cube_size];
            for(int i = 0; i < cube_size; i++) {
                tmp[0][i] = face0[X3.get(slice).get_move(0)][i];                        //face0
                tmp[1][i] = face5[X3.get(slice).get_move(1)][i];                        //face5
                tmp[2][i] = face2[X3.get(slice).get_move(2)][i];                        //face2
                tmp[3][i] = face3[X3.get(slice).get_move(3)][i];                        //face3
            }
            if(dir.equals(cw)) {
                for (int i = 0; i < cube_size; i++) {
                    //face3 -> face0
                    face0[X3.get(slice).get_move(0)][i] = tmp[3][i];
                    //face0 -> face2
                    face2[X3.get(slice).get_move(2)][i] = tmp[0][i];
                    //face2 -> face5
                    face5[X3.get(slice).get_move(1)][i] = tmp[2][i];
                    //face5 -> face3
                    face3[X3.get(slice).get_move(3)][i] = tmp[1][i];
                }
            }

            else {
                for (int i = 0; i < cube_size; i++) {
                    //face2 -> face0
                    face0[X3.get(slice).get_move(0)][i] = tmp[2][i];
                    //face0 -> face3
                    face3[X3.get(slice).get_move(3)][i] = tmp[0][i];
                    //face3 -> face5
                    face5[X3.get(slice).get_move(1)][i] = tmp[3][i];
                    //face5 -> face2
                    face2[X3.get(slice).get_move(2)][i] = tmp[1][i];
                }
            }

            if(X3.get(slice).is_outer_slice()) {
                if(slice == 0) {
                    face4 = rotate_face(face4, dir);
                }
                else {
                    face1 = rotate_face(face1, dir);
                }
            }
        }

        updateKey();
        heuristic = calcHeuristic();
    }

    /**
     * Returns a value from the specified row and column from the requested face.
     * @param face_id the face number
     * @param row row number of the colour to be returned
     * @param col column of the colour to be returned
     * @return the colour at the specified coordinates on the specified face
     */
    public Colour getFaceValue(int face_id, int row, int col){
        switch(face_id) {
            case 0: return face0[row][col];
            case 1: return face1[row][col];
            case 2: return face2[row][col];
            case 3: return face3[row][col];
            case 4: return face4[row][col];
            case 5: return face5[row][col];
            default: return null;
        }
    }

    /**
     * Returns an entire face.
     * @param face_id
     * @return
     */
    public Colour[][] getFace(int face_id){
        switch(face_id) {
            case 0: return face0;
            case 1: return face1;
            case 2: return face2;
            case 3: return face3;
            case 4: return face4;
            case 5: return face5;
            default: return null;
        }
    }

    /**
     * Rotates a face either clockwise or counter-clockwise. This is here for when an edge slice is rotated it
     * consequently rotates the edge slice face as well. Even though that face is not(directly) a part of
     * the slice being rotated.
     * @param face
     * @param dir
     * @return
     */
    private Colour[][] rotate_face(Colour[][] face, Direction dir) {
        Colour[][] tmp = new Colour[cube_size][cube_size];

        if(dir.equals(cw)) {
            for (int i = 0; i < cube_size; i++) {
                for (int j = 0; j < cube_size; j++) {
                    tmp[i][j] = face[cube_size - j - 1][i];
                }
            }
            return tmp;
        }

        else {
            for (int i = 0; i < cube_size; i++) {
                for (int j = 0; j < cube_size; j++) {
                    tmp[j][i] = face[i][cube_size - j - 1];
                }
            }
            return tmp;
        }
    }

    /**
     * Returns this cubes size
     * @return This cubes size
     */
    public int getCubeSize() {
        return cube_size;
    }

    public ArrayList<Slice> getSlices(int slices) {
        switch (slices) {
            case 1: return X1;
            case 2: return X2;
            case 3: return X3;
            default: return null;
        }
    }

    /**
     * Returns the cubes unique string key used for hte hasmap in the Searches
     * @return the String key
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns a string representation of this cube
     * @return the cube represented as a string
     */
    @Override
    public String toString() {
        String ans = "";

        //first face
        for(int i = 0; i < cube_size; i++){
            //for the empty space before first block
            for(int k = 0; k < cube_size; k++){
                ans = ans+"  ";
            }
            //for face1
            for(int k = 0; k < cube_size; k++){
                ans = ans+face1[i][k]+" ";
            }
            ans = ans+"\n";
        }
        //for faces 3, 0, 2 in that order
        for(int i = 0; i < cube_size; i++) {
            for(int k = 0; k < cube_size; k++){
                ans = ans+face3[i][k]+" ";
            }
            for(int k = 0; k < cube_size; k++) {
                ans = ans+face0[i][k]+" ";
            }
            for(int k = 0; k < cube_size; k++) {
                ans = ans+face2[i][k]+" ";
            }
            ans = ans+"\n";
        }
        //for face4
        for(int i = 0; i < cube_size; i++){
            //for the empty space before first block
            for(int k = 0; k < cube_size; k++){
                ans = ans+"  ";
            }
            //for face4
            for(int k = 0; k < cube_size; k++){
                ans = ans+face4[i][k]+" ";
            }
            ans = ans+"\n";
        }
        //for face5
        for(int i = 0; i < cube_size; i++){
            //for the empty space before first block
            for(int k = 0; k < cube_size; k++){
                ans = ans+"  ";
            }
            //for face5
            for(int k = 0; k < cube_size; k++){
                ans = ans+face5[i][k]+" ";
            }
            ans = ans+"\n";
        }

        return ans;
    }

    public double calcHeuristic() {
        //calculate the heuristic for how close the cube is to the goal state
        //for each square on the cube, estimate what colour it is, and how many moves it would take to get that
        //coloured square back to its home face

        //white is face0
        //blue is face 1
        //orange os face2
        //red is face3
        //green is face4
        //yellow is face 5

        double h = 0.0;

        for(int i = 0; i < cube_size; i++){
            for(int j = 0; j < cube_size; j++) {
                //face0, the white face
                if(face0[i][j].equals(Colour.B)
                        || face0[i][j].equals(Colour.O)
                        || face0[i][j].equals(Colour.G)
                        || face0[i][j].equals(Colour.R))
                    h++;
                else if(face0[i][j].equals(Colour.Y))
                    h += 2;

                //face1 the blue face
                if(face1[i][j].equals(Colour.O)
                        || face1[i][j].equals(Colour.W)
                        || face1[i][j].equals(Colour.Y)
                        || face1[i][j].equals(Colour.R))
                    h++;
                else if(face1[i][j].equals(Colour.G))
                    h += 2;

                //face2, the red face
                if(face2[i][j].equals(Colour.B)
                        || face2[i][j].equals(Colour.W)
                        || face2[i][j].equals(Colour.Y)
                        || face2[i][j].equals(Colour.G))
                    h++;
                else if(face2[i][j].equals(Colour.O))
                    h += 2;

                //face3, the orange face
                if(face3[i][j].equals(Colour.W)
                        || face3[i][j].equals(Colour.B)
                        || face3[i][j].equals(Colour.Y)
                        || face3[i][j].equals(Colour.G))
                    h++;
                else if(face3[i][j].equals(Colour.R))
                    h += 2;

                //face4, the green face
                if(face4[i][j].equals(Colour.O)
                        || face4[i][j].equals(Colour.R)
                        || face4[i][j].equals(Colour.Y)
                        || face4[i][j].equals(Colour.W))
                    h++;
                else if(face4[i][j].equals(Colour.B))
                    h += 2;

                //face5, the yellow face
                if(face5[i][j].equals(Colour.R)
                        || face5[i][j].equals(Colour.G)
                        || face5[i][j].equals(Colour.O)
                        || face5[i][j].equals(Colour.B))
                    h++;
                else if(face5[i][j].equals(Colour.W))
                    h += 2;
            }
        }
        return h/cube_size;
    }


    public double getHeuristic() {
        return heuristic;
    }
}
