import Backpropagation.StochasticBackPropagation;
import NeuralNetwork.NeuralNetwork;
import UnitTests.UnitTests;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Main.java
 * CPSC371
 * Vincent Tennant
 * 230099844
 *
 * Main
 */
public class Main {

    public static void main(String [] args) {
        //print intro
        intro();
        System.out.println("---");
        System.out.println("What would you like to do next?\n1. Unit Tests\n2. Do XOR Problem\n3. Do A Rubix Cube " +
                "Example\n4. Exit");
        System.out.println();

        //get input
        int input = getInput();

        while(true) {
            switch (input) {
                case 1:
                    UnitTests();
                    break;
                case 2:
                    XOR();
                    break;
                case 3:
                    rubixEg();
                    break;
                case 4:
                    System.exit(1);
                default:
                    System.out.println("That's not an option.");
            }

            System.out.println("What would you like to do next?\n1. Unit Tests\n2. Do XOR Problem\n3. Do A Rubix Cube " +
                    "Example\n4. Exit");
            input = getInput();
        }
        //show unit tests

        //show XOR problem

        //Show Example of rubix problem
            //include loading and saving of a rubix cube file
    }

    public static void rubixEg() {
        Random rand = new Random();

        System.out.println("---");
        System.out.println("This section will create a neural network, and train it for for the rubix cube training\n" +
                "data. training_data.txt is said training data. The topology to be used is 72-10-4. That is\n" +
                "72 input neurons, 10 hidden neurons, and 4 output neurons. The number of input neurons is large to\n" +
                "to accommodate for not only the cubes state, but the next move that should be taken to achieve\n" +
                "the shortest path to a solved cube.\n");
        NeuralNetwork net = new NeuralNetwork(72, 10, 4);//double the hidden layers than input layers
        System.out.println("Training...");

        net = (NeuralNetwork) StochasticBackPropagation.SBP(72, 15, 4, "training_data.txt", 50000, 0.1, 0.05);

        System.out.println("Trained.");

        ArrayList<ArrayList<Double>> tuples = StochasticBackPropagation.generateTuples("training_data.txt");

        int tuple = rand.nextInt(79);
        double[] input = UnitTests.getInputTuple(tuples.get(tuple), 72);
        double[] expected_output = UnitTests.getExpectedOutput(tuples.get(tuple), 72);

        tuple = rand.nextInt(79);
        double[] input2 = UnitTests.getInputTuple(tuples.get(tuple), 72);
        double[] expected_output2 = UnitTests.getExpectedOutput(tuples.get(tuple), 72);

        tuple = rand.nextInt(79);
        double[] input3 = UnitTests.getInputTuple(tuples.get(tuple), 72);
        double[] expected_output3 = UnitTests.getExpectedOutput(tuples.get(tuple), 72);

        tuple = rand.nextInt(79);
        double[] input4 = UnitTests.getInputTuple(tuples.get(tuple), 72);
        double[] expected_output4 = UnitTests.getExpectedOutput(tuples.get(tuple), 72);

        double[] actual_output = net.feedForward(input);
        double[] actual_output2 = net.feedForward(input2);
        double[] actual_output3 = net.feedForward(input3);
        double[] actual_output4 = net.feedForward(input4);

        System.out.print("Expected Output: ");
        for(int i = 0; i < expected_output.length; i++)
            System.out.print(Math.round(expected_output[i])+" ");

        System.out.println();

        System.out.print("Actual Output: ");
        for(int a = 0; a < actual_output.length; a++)
            System.out.print(Math.round(actual_output[a])+" ");

        System.out.println();

        System.out.print("Expected Output2: ");
        for(int i = 0; i < expected_output.length; i++)
            System.out.print(Math.round(expected_output2[i])+" ");

        System.out.println();

        System.out.print("Actual Output2: ");
        for(int a = 0; a < actual_output.length; a++)
            System.out.print(Math.round(actual_output2[a])+" ");

        System.out.println();


        System.out.print("Expected Output3: ");
        for(int i = 0; i < expected_output.length; i++)
            System.out.print(Math.round(expected_output3[i])+" ");

        System.out.println();

        System.out.print("Actual Output3: ");
        for(int a = 0; a < actual_output.length; a++)
            System.out.print(Math.round(actual_output3[a])+" ");

        System.out.println();

        System.out.print("Expected Output4: ");
        for(int i = 0; i < expected_output.length; i++)
            System.out.print(Math.round(expected_output4[i])+" ");

        System.out.println();

        System.out.print("Actual Output4: ");
        for(int a = 0; a < actual_output.length; a++)
            System.out.print(Math.round(actual_output4[a])+" ");

        System.out.println("\n---\n");
    }

    /**
     * Performs the XOR problem with a 2-2-1 neural network. Using XOR training data stochastic back propagation will
     * train the neural network to solve the XOR problem.
     */
    public static void XOR() {
        System.out.println("---");
        System.out.println("This section will create, and train a neural network with the topology of 2-2-1. That is\n" +
                "there are 2 input neurons, 2 hidden neurons, and 1 output neuron. The XOR problem will be using the\n" +
                "XOR_training_data.txt file to get the training data. In order to make training simpler, the 0's have\n" +
                "been substituted with -1's.");
        System.out.println("Training...");
        NeuralNetwork xor = (NeuralNetwork)StochasticBackPropagation.SBP(2, 2, 1, "XOR_training_data.txt", 20000, 0.1, 0.05);
        System.out.println("Trained.");

        double[] input = new double[2];
        System.out.println("Input: -1 -1");
        input[0] = -1;
        input[1] = -1;
        double[] output = xor.feedForward(input);
        System.out.println("Expected Output: -1");
        System.out.println("Actual Output: "+Math.round(output[0])); //rounding to make output cleaner.

        System.out.println("Input: -1 1");
        input[0] = -1;
        input[1] = 1;
        output = xor.feedForward(input);
        System.out.println("Expected Output: 1");
        System.out.println("Actual Output: "+Math.round(output[0]));

        System.out.println("Input: 1 -1");
        input[0] = 1;
        input[1] = -1;
        output = xor.feedForward(input);
        System.out.println("Expected Output: 1");
        System.out.println("Actual Output: "+Math.round(output[0]));

        System.out.println("Input: 1 1");
        input[0] = 1;
        input[1] = 1;
        output = xor.feedForward(input);
        System.out.println("Expected Output: -1");
        System.out.println("Actual Output: "+Math.round(output[0]));
        System.out.println("---\n");
    }

    /**
     * Performs unit tests located in the unit tests class.
     */
    public static void UnitTests() {
        System.out.println("---");

        int input = 0;
        boolean running = true;

        while(running) {
            System.out.println("1. Test Layer Class\n2. Test NeuralNetwork Class\n3. Go Back\n");
            input = getInput();
            switch (input) {
                case 1:
                    UnitTests.testLayers();
                    break;
                case 2:
                    UnitTests.testNeuralNetwork();
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("That's not an option.");
            }
        }

        System.out.println("---\n");
    }

    /**
     * Prints an intro :/
     */
    public static void intro() {
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Project Phase One\nCPSC 377\nVincent Tennant\n230099844");
        System.out.println("A simple interface to do unit testsing, perform the XOR problem, and give an example of a\n" +
                "rubix cube neural network. The XOR problem and the rubix cube problems will be performed using neural\n" +
                "networks, and trained by stochastic backpropagation.");
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
}
