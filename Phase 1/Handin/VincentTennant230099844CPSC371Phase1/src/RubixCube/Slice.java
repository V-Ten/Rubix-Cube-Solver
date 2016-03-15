package RubixCube;

/**
 * Created by Vince on 1/28/2015.
 */
public class Slice {

    private int cube_size;
    private int axis;
    private int slice_num;
    private boolean is_outer;

    //faces in this slice
    private int face0;
    private int face1;
    private int face2;
    private int face3;

    /**
     * Constructor
     * @param axis_num Axis number that this slice is apart of
     * @param s The slice number for this slice
     * @param size The size of the cube
     */
    public Slice(int axis_num, int s, int size) {
        cube_size = size;
        slice_num = s;
        axis = axis_num;

        //axis X1 is simple, the faces columns to move are the same as the slice number
        if(axis == 1) {
            face0 = slice_num; //face0 represents face0 in the cube
            face1 = slice_num; //face1 represents face1 in the cube
            face2 = slice_num; //face2 represents face4 in the cube
            face3 = slice_num; //face3 represents face5 in the cube
        }

        //for axis X2, faces 1 and 3's row and columns are the same as the slice number, respectively
        else if(axis == 2) {
            face0 = (cube_size - slice_num - 1);    //face0 represents face4 in the cube
            face1 = slice_num;                      //face1 represents face1 in the cube
            face2 = (cube_size - slice_num - 1);    //face2 represents face2 in the cube
            face3 = slice_num;                      //face3 represents face3 in the cube
        }

        //faces 3, 0, and 2 all uses the slice number for the row they'll be using.
        //face 5 will be using teh same formula used in axis X2
        else if(axis == 3) {
            face0 = (cube_size - slice_num - 1);;                      //face0 represents face0 in the cube
            face1 = slice_num;                                         //face5 represents face5 in the cube
            face2 = (cube_size - slice_num - 1);;                      //face2 represents face2 in the cube
            face3 = (cube_size - slice_num - 1);;                      //face3 represents face3 in the cube
        }

        if(slice_num == 0 || slice_num == cube_size - 1)
            is_outer = true;
        else
            is_outer = false;
    }

    /**
     * Returns the proper part of the array to move
     * @param face_id The face from this slice to return
     * @return Returns the proper column or row to be used in a move. Returns -1 if a number out of bounds is passed.
     */
    public int get_move(int face_id){
        switch(face_id){
            case 0: return face0;
            case 1: return face1;
            case 2: return face2;
            case 3: return face3;
        }

        return -1;
    }

    /**
     *
     * @return Returns whether or not this slice is an outer slice
     */
    public boolean is_outer_slice() {
        return is_outer;
    }
}
