package Backpropagation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import NeuralNetwork.*;

/**
 * StochasticBackpropagation.java
 * CPSC371
 * Vincent Tennant
 * 230099844
 *
 * Stochastic Backpropagation. Allows for the training of Backpropagation type objects.
 */
public class StochasticBackPropagation {

    private static Backpropagation NN = null;

    /**
     * Stochastic Backpropagation.Backpropagation. A form of supervised learning for neural networks(NN). This algorithm will train a
     * NN to solve any given set of training data. The number of input neurons, hidden layers and neurons, a
     * nd output neurons are specified by the caller of this method.
     * The algorithm works by backpropagating itself through the NN every time the NN
     * feeds forward some set of input. The algorithm then calculates the error and changes the synapse weights
     * according to the calculated error.
     * Once complete it will evaluate the total error of the resulting NN and determine if it is sufficient enough
     * to solve the desired problem. If the resulting neural network does not pass the error threshold then the NN is
     * discarded and the algorithm repeated until a suitable NN is trained.
     *
     * NOTE: This SBP does not currently support multiple hidden layers. todo Change the algorithm to accommodate multiple
     *                                                                   todo   hidden layers.
     *
     * @param input_size the size of the input layer, how many neurons it contains.
     * @param hidden_size how many input neurons are in the hidden layer.
     * @param output_size the number of neurons in the output layer.
     * @param training_iterations The number of training iterations that stochastic back propagation will perform.
     * @param error_threshold The error threshold used to determine if a given neural network is trained to a certain degree of accuracy.
     * @param learning_rate The learning rate for stochastic back propagation.
     * @return Return the trained neural network. trained of the supplied training data.
     */
    public static Backpropagation SBP(int input_size,
                                    int hidden_size,
                                    int output_size,
                                    String training_data,
                                    int training_iterations,
                                    double error_threshold,
                                    double learning_rate) {

        int training_epochs = 10;

        //array of doubles that contain the training tuple.
        ArrayList<Double> Ti;
        //the actual output of the neural network.
        double[] actual_output;
        //array to store the error of the output and hidden
        double[] output_errors;
        double[] hidden_errors;
        //Calculated weight updates
        double[] Dwkj = new double[hidden_size * output_size];      //weights for hidden to output synapses
        double[] Dwkbias = new double[output_size];                 //weights for output to bias synapses
        double[] Dwji = new double[hidden_size * input_size];       //weights for hidden to output synapses
        double[] Dwjbias = new double[hidden_size];                 //weights for hidden to bias synapses

        //2d array list of all of the training data. The first e elements, where e=output_size, are considered the
        //expected output. The remaining elements in the list are considered input.
        ArrayList<ArrayList<Double>> tuples = generateTuples(training_data);

        double[] input;
        double[] expected_output;

        boolean training = true;

        //training epoch loop.
        while(training) {
            //initialize a new neural network of the specified sizes.
            NN = genRandomNeuralNetwork(input_size, hidden_size, output_size);
            //create an array to store the output errors for future use.
            output_errors = new double[output_size];
            hidden_errors = new double[hidden_size];

            for(int t = 0; t < training_iterations; t++) {
                //choose a random tuple from the training data.
                Ti = chooseTrainingTuple(tuples);
                //extract the expected output from the tuple
                expected_output = getExpectedOutput(Ti, input_size);
                //get the tuple to be used as input
                input = getInputTuple(Ti, input_size);

                //feed the input through the neural network.
                actual_output = NN.feedForward(input);

                //iterate through each output neuron
                int pos = 0;
                int bias_pos = 0;
                for(int k = 0; k < output_size; k++) {
                    //calculate the error at this node
                    output_errors[k] = (expected_output[k] - actual_output[k]) *
                            (1 - (Math.tanh(NN.getNeuronValue(3, k, 0)) * Math.tanh(NN.getNeuronValue(3, k, 0))));

                    for(int j = 0; j < hidden_size; j++) {
                        Dwkj[pos] = learning_rate * output_errors[k] * NN.calcActivation(NN.getNeuronValue(2, j, 0));
                        pos = pos + 1;
                    }
                    Dwkbias[bias_pos] = learning_rate * Dwkj[k] * 1;
                    bias_pos = bias_pos + 1;
                }

                pos = 0;
                bias_pos = 0;
                //calculate the error at each hidden neuron
                for(int j = 0; j < hidden_size; j++) {
                    hidden_errors[j] = errorAtHidden(output_errors, NN.getNeuronValue(2, j, 0), j, 0);

                    for(int i = 0; i < input_size; i++) {
                        Dwji[pos] = learning_rate * NN.getNeuronValue(1, i, 0) * hidden_errors[j];
                        pos = pos + 1;
                    }

                    Dwjbias[bias_pos] = learning_rate * 1 * hidden_errors[j];
                    bias_pos = bias_pos + 1;
                }

                pos = 0;
                bias_pos = 0;
                //apply all of the weight updates for the hidden to output layer, and hidden to bias.
                for(int k = 0; k < output_size; k++){
                    for(int j = 0; j < hidden_size; j++) {
                        updateSynapseWeight(2, j, k, Dwkj[pos]);
                        pos = pos + 1;
                    }
                    updateSynapseWeight(4, 0, k, Dwkbias[bias_pos]);
                    bias_pos = bias_pos + 1;
                }

                pos = 0;
                bias_pos = 0;
                //update all of the synapse weights for the input to hidden layers, and the hidden to bias layer
                for(int j = 0; j < hidden_size; j++) {
                    for(int i = 0; i < input_size; i++) {
                        updateSynapseWeight(1, i, j, Dwji[pos]);
                        pos = pos + 1;
                    }
                    updateSynapseWeight(4, 0, j, Dwjbias[bias_pos]);
                    bias_pos = bias_pos + 1;
                }
            }
            //calculate the total neural network error across the testing data. Test against each tuple!
            //iterate through all of the training tuples, use their input, and compare output of neural network to that of the training data
            double error = 0.0;
            for(ArrayList<Double> tuple : tuples) {
                //get input
                input = getInputTuple(tuple, input_size);
                //get expected output
                expected_output = getExpectedOutput(tuple, input_size);
                //get actual output
                actual_output = NN.feedForward(input);

                for (int j = 0; j < expected_output.length; j++)
                    error = error +
                            ((expected_output[j] - actual_output[j])*(expected_output[j] - actual_output[j]) / 2);
                error = error / 100;
            }

            if(error <= error_threshold && error >= -error_threshold)
                training = false;
        }
        return NN;
    }

    /**
     * Updates a synapses weight.
     * @param layer The layer the synapse belongs to.
     * @param from The neuron the synapse is coming from.
     * @param to The neuron the synapse is going to.
     * @param weight_update The value to update the synapses weight with.
     */
    private static void updateSynapseWeight(int layer, int from, int to, double weight_update) {
        NN.updateSynapseWeight(layer, from, to, weight_update);
    }

    /**
     * Reads the training data file and turns it into an array of doubles. The arrays are arranged in such a way that
     * the first element in each array is the expected result, every other element is input for the neural network.
     * @param training_data The name of the training data file to be read from.
     */
    public static ArrayList<ArrayList<Double>> generateTuples(String training_data) {
        ArrayList<ArrayList<Double>> ret = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> tuple;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(training_data));
            StringTokenizer tokener;

            String next_line = reader.readLine();

            while(next_line != null) {
                tuple = new ArrayList<Double>();
                tokener = new StringTokenizer(next_line);

                //iterate though the tokenized string.
                while(tokener.hasMoreTokens()) {
                    tuple.add(Double.valueOf(tokener.nextToken()));
                }
                //add the tuple to the list of training tuples.
                ret.add(tuple);

                next_line = reader.readLine();
            }
        }
        catch(FileNotFoundException nf) {
            System.out.println("File "+training_data+" was not found.  "+nf + " ::: @ generateTuples");
        }
        catch(Exception e) {
            System.out.println(e + " ::: @ generateTuples");
        }

        return ret;
    }

    /**
     * Generates a new neural network according to the passed parameters. Currently this method only supports
     * topologies of three layers, one input layer, one hidden layer, and one output layer.
     * @param input_size the number of neurons in the input layer.
     * @param hidden_size the number of neurons in the hidden layer.
     * @param output_size the number of neurons in the output layer.
     * @return Return the newly created neural network.
     */
    public static NeuralNetwork genRandomNeuralNetwork(int input_size, int hidden_size, int output_size) {
        return new NeuralNetwork(input_size, hidden_size, output_size);
    }

    /**
     * Chooses a (pseudo)random tuple form the training data set. This tuple will include the input, and the correct output.
     * @param list The list of tuples available.
     * @return The chooses training tuple to be used.
     */
    public static ArrayList<Double> chooseTrainingTuple(ArrayList<ArrayList<Double>> list) {
        //choose pseudo random tuple to use
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
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
    }

    /**
     * Calculated the error at a hidden neuron.
     * @param error_output the error array of the output layer. Must be calculated first.
     * @param netj The net value of the hidden neuron to calculate the error at.
     * @param neuron The neuron in the layer to get the synapse weight from.
     * @param hidden_layer The hidden layer that the neuron in in. There can be multiple hidden layers.
     * @return the error at the specific hidden neuron.
     */
    public static double errorAtHidden(double[] error_output, double netj, int neuron, int hidden_layer) {
        //calculate f'
        double fprime = 1 - (Math.tanh(netj) * Math.tanh(netj));

        //the error at the hidden neurons is calculated differently than the error at the output neurons.
        double summation = 0.0;
        for(int k = 0; k < NN.getHiddenSynapseSize(hidden_layer); k++) {
            summation = summation + (NN.getHiddenSynapseWeight(hidden_layer, neuron, k) * error_output[k]);
        }

        return fprime * summation;
    }

}
