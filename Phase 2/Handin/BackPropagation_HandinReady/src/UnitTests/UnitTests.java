package UnitTests;

import Backpropagation.StochasticBackPropagation;
import InputOutput.FileWriter;
import NeuralNetwork.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * UnitTests.java
 * CPSC371
 * Vincent Tennant
 * 230099844
 *
 * Basic unit tests for CPSC371 Phase 2. The unit tests included are, in no particular order:
 *      StochasticBackpropagation
 *      Layer
 *      NeuralNetwork
 *      Combination of the above
 *
 * These unit tests can be performed in Main.
 */
public class UnitTests {

    /**
     * The class tests the formatting to be used for the heat maps for finding the optimal learning rates, training
     * iterations, and hidden layer sizes. It simply creates a .csv file in teh same format that is going to be used
     * for the heat maps. The values in the .csv file are random.
     */
    public static void testCSVFormatting() {
        Random rand = new Random();
        FileWriter test = new FileWriter("testCSVFormatting.csv");

        String hidden = " ,5,10,15,20,25,30,35,40,45,50,55,60,65,70,75,80,85,90,95";
        double learning_rate = 0.05;

        test.writeToFile(hidden);
        test.newLine();
        int bobby = 0;

        for(double lr = learning_rate; lr < 1.0; lr = lr + 0.1) {
            System.out.println(lr);
            test.writeToFile(lr+",");
            for(int h = 5; h < 100; h = h + 5) {
                if(h+5 == 100)
                    test.writeToFile(bobby++ + "");
                else
                    test.writeToFile(bobby++ + ",");
            }
            test.newLine();
            bobby = 0;
        }

        test.closeWriter();
    }

    /**
     * Tests the creation, training, and results of a neural network using training_data.txt. The values used for the
     * hidden layer size, training iterations, error threshold, and learning rate were chosen before the optimal
     * values were found using the heat maps.
     */
    public static void testRubixNetwork() {
        Random rand = new Random();

        System.out.println("---");
        NeuralNetwork net = new NeuralNetwork(72, 10, 4);//double the hidden layers than input layers
        net.saveNeuralNetwork("pretrained.txt");
        System.out.println("Training...");

        net = (NeuralNetwork) StochasticBackPropagation.SBP(72, 15, 4, "training_data.txt", 50000, 1.0, 0.05);

        System.out.println("Trained.");

        net.saveNeuralNetwork("trained.txt");

        ArrayList<ArrayList<Double>> tuples = StochasticBackPropagation.generateTuples("training_data.txt");

        int tuple = rand.nextInt(79);
        double[] input = getInputTuple(tuples.get(tuple), 72);
        double[] expected_output = getExpectedOutput(tuples.get(tuple), 72);

        tuple = rand.nextInt(79);
        double[] input2 = getInputTuple(tuples.get(tuple), 72);
        double[] expected_output2 = getExpectedOutput(tuples.get(tuple), 72);

        tuple = rand.nextInt(79);
        double[] input3 = getInputTuple(tuples.get(tuple), 72);
        double[] expected_output3 = getExpectedOutput(tuples.get(tuple), 72);

        tuple = rand.nextInt(79);
        double[] input4 = getInputTuple(tuples.get(tuple), 72);
        double[] expected_output4 = getExpectedOutput(tuples.get(tuple), 72);

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

        System.out.println();


    }

    /**
     * Tests StochasticBackpropagation.SBP by creating a neural network, training is using SBP given a set of training
     * data. Currently the neural network size is small, and works well for the XOR problem.
     * @param file
     */
    public static void testSBP(String file) {
        System.out.println("---");
        NeuralNetwork trained = new NeuralNetwork(2, 2, 1);
        double[] input = new double[2];
        input[0] = 1;
        input[1] = 0;
        double[] output = trained.feedForward(input);
        System.out.println(output[0]);
        System.out.println("Training...");
        trained = (NeuralNetwork)StochasticBackPropagation.SBP(2, 2, 1, file, 100000, 0.03, 0.09);

        System.out.println("Trained.");
        output = trained.feedForward(input);

        System.out.println("Printing output");
        System.out.println("Expected Output: 1");
        System.out.println("Actual Output: "+output[0]);
        System.out.println("Actual Output: "+Math.round(output[0]));

        trained.saveNeuralNetwork("trained_test_NN.txt");
    }

    /**
     * Tests the functionality of layer objects. Ensures that the operations performed by the layer object are performed
     * as expected.
     */
    public static void testLayers() {
        //create an input layer
        Layer layer = new Layer(1, 1);
        System.out.println("Is it an input layer?");
        System.out.println("Expected: true");
        System.out.println("Actual: " +layer.isInput());
        System.out.println("---");
        System.out.println("Is it a hidden layer?");
        System.out.println("Expected: false");
        System.out.println("Actual: " +layer.isHidden());
        System.out.println("---");
        System.out.println("Is it an output layer?");
        System.out.println("Expected: false");
        System.out.println("Actual: " +layer.isOutput());
        System.out.println("---");
        System.out.println("Is it a bias layer?");
        System.out.println("Expected: false");
        System.out.println("Actual: " +layer.isBias());
        System.out.println("---");
        System.out.println("What are the nodes set to?");
        System.out.println("Expected: 0.0, the array has not be given input yet.");
        System.out.println("Actual: "+layer.getNeuronValue(0));
        System.out.println("---");
        System.out.println("Connecting to another layer. A hidden layer.");
        Layer layer_two = new Layer(2, 1);
        System.out.println("Is it a hidden layer?");
        System.out.println("Expected: true");
        System.out.println("Actual: " +layer_two.isHidden());
        layer.connectTo(layer_two);
        layer.initSynapses();
        System.out.println("Syanpse weight between layer and layer_two.");
        System.out.println("Expected: Between -1 and 1");
        System.out.println("Actual: "+layer.getSynapseWeight(0, 0));
        System.out.println("---");
    }

    /**
     * Test teh creation and use of a neural network object. The neural network isn't trained by SBP though. This is
     * a method to test the functionalities of the neural network and ensure correct results.
     */
    public static void testNeuralNetwork() {
        NeuralNetwork nn = new NeuralNetwork(2, 2, 1);

        System.out.println("Testing XOR feed forward.");
        nn.feedForwardXOR(1, 0);

        double[] tmp = nn.getAllOutput();

        for(int i = 0; i < tmp.length; i++)
            System.out.print(tmp[i]);
        System.out.println();
        System.out.println("---");

        System.out.println("Testing the calcNetValue and calcActivation methods.");
        testCalcNetValue();
        System.out.println("---");

        System.out.println("Testing general feed forward method.");

        nn = new NeuralNetwork(2, 2, 1);

        double[] input = new double[2];
        input[0] = 0.73622;
        input[1] = -0.2666;

        nn.feedForward(input);
        tmp = nn.getAllOutput();
        for(int i = 0; i < tmp.length; i++)
            System.out.print(tmp[i]);

        System.out.println();
        System.out.println("---");
        System.out.println("Testing Save Neural Network.");
        nn.saveNeuralNetwork("test_save.txt");
        System.out.println("---");
        System.out.println("Testing Load Neural Network");
        NeuralNetwork test = new NeuralNetwork(1, 1, 3);
        test.loadNeuralNetwork("test_save.txt");
        System.out.println("expected results are for test_save.txt and new_save.txt to be the same.");
        test.saveNeuralNetwork("new_save.txt");
    }

    /**
     * A method of the exact form of the private method in neural network by the same name. A few lines of the method
     * are different form the original for testing perposes. Such as the creation of layer objects, and the setting
     * of a specific neurons value.Main
     */
    public static void testCalcNetValue() {
        Layer layer = new Layer(1, 1);
        Layer layer_two = new Layer(2, 1);
        layer.connectTo(layer_two);
        layer.initSynapses();

        layer.setNeuronValue(0, 0.5);

        double net = 0.0;

        for(int j = 0; j < layer_two.getLayerSize(); j++) {
            for(int i = 0; i < layer.getLayerSize(); i++) {
                net = net + (layer.getNeuronValue(i) * layer.getSynapseWeight(i, j));
            }
            layer_two.setNeuronValue(j, calcActivation(net));
            net = 0.0;
        }

        System.out.println("Expected Input Neuron Value: 0.5");
        System.out.println("Actual: "+layer.getNeuronValue(0));
        System.out.println("Hidden Neuron Value: "+layer_two.getNeuronValue(0));
    }

    /**
     * This method is copied from the NeuralNetwork.NeuralNetwork class
     *
     * Calculated teh final value for a node based on the activation function we've been told to use for our neural
     * network. The function is as follows:
     *
     * Act(netx) = A * tanh(B * netx)
     *
     * Where netx is the initial net value for either a hidden layer or the output layer, A=1.716, and B=0.667
     * A and B was provided to be by Warren as appropriate, experimentally proven values to be used in this situation.
     *
     * @param net The initial net value of the node being passed through the activation function.
     * @return The final value calculated by the activation function.
     */
    private static double calcActivation(double net) {
        double A = 1.716;
        double B = 0.667;

        return (A * Math.tanh(B * net));
    }

    /**
     * A unit test for the genreate tuple. Uses a txt file called test_tuples.txt and creates the list of tuples from
     * this file. The resulting tuples will then be printed out as expected.
     * @param training_data That name of the file to generate the training tuples from.
     */
    public static ArrayList<ArrayList<Double>> testGenerateTuples(String training_data) {
        System.out.println("---");
        System.out.println("Generating the tuples from the test training data.");
        ArrayList<ArrayList<Double>> tuple = StochasticBackPropagation.generateTuples(training_data);

        System.out.println("Printing the tuples.");
        for(int i = 0; i < tuple.size(); i++) {
            for(int j = 0; j < tuple.get(i).size(); j++) {
                System.out.print(tuple.get(i).get(j)+"  ");
            }
            System.out.println();
        }

        return tuple;
    }

    /**
     * Tests all of the tuple operations, including choosing a pseudorandom tuple from the list of tuples. Exracting
     * the expected out put of a tuple. And extracting the input from the tuple.
     * @param tuple The list of tuples to be tested.
     */
    public static void testTuples(ArrayList<ArrayList<Double>> tuple) {
        System.out.println("---");
        System.out.println("Choosing a training tuple to be used.");
        ArrayList<Double> test = StochasticBackPropagation.chooseTrainingTuple(tuple);
        System.out.println("Printing the choosen tuple.");
        for(int i = 0; i < test.size(); i++)
            System.out.print(test.get(i)+"  ");
        System.out.println();
        System.out.println("Extracting the expected output and the input.");
        double[] actual_output = StochasticBackPropagation.getExpectedOutput(test, 2);
        double[] input = StochasticBackPropagation.getInputTuple(test, 2);
        System.out.println("Expected Output: 1.0  5.0");
        System.out.print("Actual Output: ");
        for(int out = 0; out < actual_output.length; out++)
            System.out.print(actual_output[out]+"  ");
        System.out.println();
        System.out.println("Expected Input: 0.0  3.0  4.0");
        System.out.print("Actual Input: ");
        for(int in = 0; in < input.length; in++)
            System.out.print(input[in]+"  ");
        System.out.println();

    }

    /**
     * Create an array of double to be used as input for a neural netowork.
     * @param list The Array List to extract the values from.
     * @param input_size The size of the input layer.
     * @return The array of double values to be used as input for a neural network.
     */
    public static double[] getInputTuple(ArrayList<Double> list, int input_size) {
        double[] ret = new double[input_size];
        for(int i = 0; i < input_size; i++) {
            ret[i] = list.get(i);
        }
        return ret;
    }

    /**
     * Creates an array of double values containing the expected out put of any given tuple.
     * @param list The list containing the expected output and the input required to obtain that output.
     * @param input_size The size of the input layer.
     * @return The array of double values containing the expected output.
     */
    public static double[] getExpectedOutput(ArrayList<Double> list, int input_size) {
        int k = 0;
        while(k <= input_size - 1)
            k = k + 1;

        double[] ret = new double[list.size() - input_size];

        for(int pos = 0; k < list.size() && pos < ret.length; pos++) {
            ret[pos] = list.get(k);
            k = k + 1;
        }

        return ret;
    }}
