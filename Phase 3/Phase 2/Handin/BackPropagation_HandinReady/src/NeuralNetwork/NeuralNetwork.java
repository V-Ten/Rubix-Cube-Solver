package NeuralNetwork;

import Backpropagation.Backpropagation;
import InputOutput.FileWriter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * NeuralNetwork.java
 * CPSC371
 * Vincent Tennant
 * 230099844
 *
 * This is a neural network class used to conceptualize a neural network and its feed forward operations. It uses layer
 * objects to represent each layer in the network.
 *
 * Currently there are two constructors for a neural network, one that has a single hidden layer, and a constructor that
 * allows for multiple hidden layers. At the same time there are two feed forward methods. The feedForwardXOR method is
 * intended to be used with a 2-2-1 topology neural network in order to solve te XoR problem.
 */
public class NeuralNetwork implements Backpropagation {

    private double error = 0.0;

    private Layer input;
    private Layer[] hidden;
    private Layer output;
    private Layer bias;

    private int input_size;
    private int hidden_size;    //only used for single hidden layers
    private int output_size;

    /**
     * Constructor for a Neural Network.
     * This constructor can be used for a 3 layer neural network.
     *
     * @param num_inputs The number of input neurons.
     * @param num_hiddens The number of hidden neurons.
     * @param num_outputs The number of output neurons.
     */
    public NeuralNetwork(int num_inputs, int num_hiddens, int num_outputs) {
        input_size = num_inputs;
        hidden_size = num_hiddens;
        output_size = num_outputs;

        //create layer objects
        input = new Layer(1, input_size);
        hidden = new Layer[1];
        hidden[0] = new Layer(2, hidden_size);
        output = new Layer(3, output_size);
        bias = new Layer(4, 1);

        //create synapses
        input.connectTo(hidden[0]);
        hidden[0].connectTo(output);
        hidden[0].setLayerNumber(0);
        output.setLayerNumber(1);
        bias.connectBiasTo(hidden, output);

        input.initSynapses();
        hidden[0].initSynapses();
        bias.initSynapses();

        bias.setNeuronValue(0, 1);

        error = 0.0;
    }

    /**
     * Constructor for a 3 or more topology neural network. The only difference between this const. and the previous
     * const. is the array of integers for the hidden. The size of the array corresponds to the number of hidden
     * layers requested, while the values inside are the number of neurons for that layer.
     *
     * @param num_inputs Number of input nodes.
     * @param num_hidden The number of hidden layers, containing the number of hidden neurons within each layer.
     * @param num_outputs The number of output nodes.
     */
    public NeuralNetwork(int num_inputs, int[] num_hidden, int num_outputs) {
        input_size = num_inputs;
        output_size = num_outputs;
        hidden_size = -1;   //hidden size set to -1 as there are multiple hidden layers or possible multiple sizes

        //create layer objects
        input = new Layer(1, input_size);

        hidden = new Layer[num_hidden.length];
        for(int j = 0; j < hidden.length; j++) {
            hidden[j] = new Layer(2, num_hidden[j]);
        }

        output = new Layer(3, output_size);
        bias = new Layer(4, 1);

        //create synapses
        //input layer only sees the first initial hidden layer and this only connects to it and not the other hidden layers.
        input.connectTo(hidden[0]);

        for(int j = 0; j < hidden.length; j++) {
            if((j+1) == hidden.length) {
                hidden[j].connectTo(output);
                hidden[j].setLayerNumber(j);
                output.setLayerNumber(j + 1);
            }
            else {
                hidden[j].connectTo(hidden[j + 1]);
                hidden[j].setLayerNumber(j);
            }
        }

        bias.connectBiasTo(hidden, output);

        input.initSynapses();
        for(int j = 0; j < hidden.length; j++)
            hidden[j].initSynapses();
        bias.initSynapses();

        bias.setNeuronValue(0, 1);

        error = 0.0; // error is initialized to zero. Changed by Stochastic Back propagation todo Change when this value is changed
    }

    /**
     * Constructor that creates this neural network by loading from a saved neural network.
     * @param to_load The name of the file to load.
     */
    public NeuralNetwork(String to_load) {
        loadNeuralNetwork(to_load);
    }

    /**
     * NOTE This method is design for the XOR problem!
     *
     * This method is designed to run with a neural network of size 2-2-1 in order to solve the XOR problem.
     *
     * Feeds the input through the algorithm and return the output
     * @param x0 The first input passed.
     * @param x1 The second input passed.
     * @return Returns the result of the work.
     */
    public void feedForwardXOR(double x0, double x1) {
        //got the inputs from the user, in this case we have x1 and x2.
        //set the input nodes equals to x1 and x2, node0 is x0, and node1 is x1
        input.setNeuronValue(0, x0);
        input.setNeuronValue(1, x1);

        //calculate the initial net values for each node, then pass through the activation function.
        calcNetValues(input, hidden[0]);

        //calculate the initial net values for each node, then pass through the activation function.
        calcNetValues(hidden[0], output);
    }

    /**
     * TODO This method, while capable of taking more that just 2 inputs, only works with one hidden layer, in teh future allow for multiple hidden layers.
     *
     * Feeds the input through the algorithm and return the output
     * @param input_array The array of inputs to be used.
     * @return Returns the result of the work.
     */
    public double[] feedForward(double[] input_array) {
        //set the input nodes
        for(int i = 0; i < input_array.length; i++) {
            input.setNeuronValue(i, input_array[i]);
        }

        //calculate the initial net values for each node in the hidden layer, then pass through the activation function.
        calcNetValues(input, hidden[0]);

        //calculate the initial net values for each node in the output layer, then pass through the activation function.
        calcNetValues(hidden[0], output);

        double[] ret = new double[output_size];

        for(int k = 0; k < output_size; k++) {
            ret[k] = output.getNeuronValue(k);
        }

        return ret;
    }

    /**
     * Calculates the net value of each node in the "to" layer. This method will only work as expected if the from layer
     * is the input layer, or the hidden layer, and the to layer is either the hidden layer or output layer.
     * If layers passed are used differently than Input->hidden or hidden->output then the results will be incorrect
     * and unknown.
     *
     * @param from The layer with known net values for each node, and synapses with weights going to the "to" layer.
     * @param to The layer the with which the net neuron values are being calculated.
     */
    private void calcNetValues(Layer from, Layer to) {
        double net = 0.0;

        for(int j = 0; j < to.getLayerSize(); j++) {        //iterate through the to neurons
            for(int i = 0; i < from.getLayerSize(); i++) {  //iterate through the from neurons
                net = net + (from.getNeuronValue(i) * from.getSynapseWeight(i, j)) + bias.getBiasSynapseWeight(to.getLayerNumber(), j);

            }
            to.setNeuronValue(j, calcActivation(net));
            net = 0.0;
        }
    }

    /**
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
    public double calcActivation(double net) {
        double A = 1.716;
        double B = 0.667;

        return (A * Math.tanh(B * net));
    }

    /**
     * Returns an array of all output values. Note that this method, like the getOutput method will return zero until
     * some input has been fed forward.
     * @return An array of all output values.
     */
    public double[] getAllOutput() {
        double[] ret = new double[output_size];

        for(int k = 0; k < output_size; k++) {
            ret[k] = output.getNeuronValue(k);
        }

        return ret;
    }

    /**
     * Saves the topology of this neural network to a txt file to be loaded again in the future.
     *
     * Warning: If file is saved using the name of an already existing file of the same name, this save file will
     *          overwrite it!
     *
     * @param name The name of the file to be saved.
     */
    public void saveNeuralNetwork(String name) {
        FileWriter file = new FileWriter(name);

        //add number of input neurons to file
        file.writeToFile("i "+input.getLayerSize());
        file.newLine();

        //add the number of hidden layers and neurons in each layer to file
        for(int i = 0; i < hidden.length; i++) {
            file.writeToFile("h " + hidden[i].getLayerSize());
            file.newLine();
        }

        file.writeToFile("o "+output.getLayerSize());
        file.newLine();

        file.writeToFile("i ");
        //add the synapse weights from the input layer to the hidden layer
        for(int i = 0; i < input.getLayerSize(); i++) {
            for(int j = 0; j < input.getSynapseSize(i); j++) {
                file.writeToFile(""+input.getSynapseWeight(i, j)+" ");
            }
        }
        file.newLine();

        file.writeToFile("h ");
        //add synapse weights form the hidden layers to other hidden layers and the output layer
        for(int j = 0; j < hidden.length; j++) {
            for(int k = 0; k < hidden[j].getLayerSize(); k++) {
                for(int l = 0; l < hidden[j].getSynapseSize(k); l++) {
                    file.writeToFile("" + hidden[j].getSynapseWeight(k, l) + " ");
                }
            }
        }
        file.newLine();

        file.writeToFile("b ");
        //add synapse weights from the bias neuron to the hidden and output neurons
        for(int b = 0; b < bias.getSynapseSize(0); b++) {
            file.writeToFile(""+bias.getSynapseWeight(0, b)+" ");
        }

        file.closeWriter();
    }

    /**
     * Loads a neural network from a txt file. The topology of the constructed neural network along with the synapse
     * weights and neuron weights will be overwritten!
     * @param name The name of the file to load.
     */
    public void loadNeuralNetwork(String name) {
        String input_layer = "";
        String output_layer = "";
        String input_synapses = "";
        String bias_synapses = "";

        ArrayList<String> hidden_layers = new ArrayList<String>();
        ArrayList<String> hidden_synapses = new ArrayList<String>();

        //get all of the information from the file into Strings to be tokenized.
        try {
            BufferedReader reader = new BufferedReader(new FileReader(name));

            input_layer = reader.readLine();
            while(reader.read() == 'h') {
                hidden_layers.add(reader.readLine());
            }
            output_layer = reader.readLine();

            input_synapses = reader.readLine();
            while(reader.read() == 'h') {
                hidden_synapses.add(reader.readLine());
            }
            bias_synapses = reader.readLine();

            reader.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("A file by that name cannot be found.    "+e);
        } catch(IOException eio) {
            System.out.println(eio);
        }

        int in_size = 0;
        int[] hidden_sizes;
        int out_size = 0;

        StringTokenizer tokener;
        String next_token = "";

        //get number of input neurons
        tokener = new StringTokenizer(input_layer);
        while(tokener.hasMoreTokens()){
            next_token = tokener.nextToken();
            if(next_token.equals("i"))
                in_size = Integer.valueOf(tokener.nextToken());
            else
                in_size = Integer.valueOf(next_token);
        }

        //get number of hidden layers and their neurons
        hidden_sizes = new int[hidden_layers.size()];
        for(int i = 0; i < hidden_layers.size(); i++) {
            tokener = new StringTokenizer(hidden_layers.get(i));
            while(tokener.hasMoreTokens()){
                next_token = tokener.nextToken();
                if(next_token.equals("h"))
                    hidden_sizes[i] = Integer.valueOf(tokener.nextToken());
                else
                    hidden_sizes[i] = Integer.valueOf(next_token);
            }
        }

        //get number of output neurons
        tokener = new StringTokenizer(output_layer);
        while(tokener.hasMoreTokens()){
            next_token = tokener.nextToken();
            if(next_token.equals("o"))
                out_size = Integer.valueOf(tokener.nextToken());
            else
                out_size = Integer.valueOf(next_token);
        }

        //create new layers
        setInput(in_size);
        setHidden(hidden_sizes);
        setOutput(out_size);
        setBias();

        //create synapses
        //input layer only sees the first initial hidden layer and this only connects to it and not the other hidden layers.
        input.connectTo(hidden[0]);
        for(int j = 0; j < hidden.length; j++) {
            if((j+1) == hidden.length) {
                hidden[j].connectTo(output);
                hidden[j].setLayerNumber(j);
                output.setLayerNumber(j + 1);
            }
            else {
                hidden[j].connectTo(hidden[j + 1]);
                hidden[j].setLayerNumber(j);
            }
        }
        bias.connectBiasTo(hidden, output);

        //get and set the synapse weights for the input to hidden layer.
        tokener = new StringTokenizer(input_synapses);
        for(int i = 0; i < input.getSynapsesLength(); i++) {
            for(int j = 0; j < input.getInnerSynapsesSize(i); j++) {
                next_token = tokener.nextToken();
                if(next_token.equals("i"))
                    input.setSynapseWeight(i, j, Double.valueOf(tokener.nextToken()));
                else
                    input.setSynapseWeight(i, j, Double.valueOf(next_token));
            }
        }

        //get all of the synapse weights for all of the hidden layers.
        for(int i = 0; i < hidden.length; i++) {
            tokener = new StringTokenizer(hidden_synapses.get(i));
            for(int j = 0; j < hidden[i].getSynapsesLength(); j++) {
                for(int k = 0; k < hidden[i].getInnerSynapsesSize(j); k++) {
                    next_token = tokener.nextToken();
                    if(next_token.equals("h"))
                        hidden[i].setSynapseWeight(j, k, Double.valueOf(tokener.nextToken()));
                    else
                        hidden[i].setSynapseWeight(j, k, Double.valueOf(next_token));
                }
            }
        }

        //get all of the synapse weights for the bias neuron
        tokener = new StringTokenizer(bias_synapses);
        for(int i = 0; i < bias.getSynapsesLength(); i++) {
            for(int b = 0; b < bias.getInnerSynapsesSize(i); b++) {
                next_token = tokener.nextToken();
                if(next_token.equals("b"))
                    bias.setSynapseWeight(i, b, Double.valueOf(tokener.nextToken()));
                else
                    bias.setSynapseWeight(i, b, Double.valueOf(next_token));
            }
        }


        bias.setNeuronValue(0, 1);
    }

    /**
     * Returns the inner synapse size of the synapse array of a layer. The synapse array of a layer is 2 dimensional
     * with the first dimension being the from neuron and the second dimension being the to neuron.
     * @param layer the layer thta the neuron is in, input, hidden, or output.
     * @param pos the position in the inner synapse array to extract.
     * @return the second dimension size.
     */
    public int getInnerSynapseSize(int layer, int pos, int hidden_layer) {
        switch(layer) {
            case 1: return input.getInnerSynapsesSize(pos);
            case 2: return hidden[hidden_layer].getInnerSynapsesSize(pos);
            case 3: return output.getInnerSynapsesSize(pos);
            default: return 0;
        }
    }

    /**
     * Returns a neurons net value. Only accounts for a single hidden layer at this moment as that is all I need it for.
     * @param layer The layer the node is contained in. 1<-input, 2<-hidden, 3<-output
     * @param neuron the neurons whos net value to return.
     * @return The neurons net value.
     */
    public double getNeuronValue(int layer, int neuron, int hidden_layer) {
        switch(layer) {
            case 1: return input.getNeuronValue(neuron);
            case 2: return hidden[hidden_layer].getNeuronValue(neuron);
            case 3: return output.getNeuronValue(neuron);
            default: return 0.0;
        }
    }

    /**
     * Returns the number of synapses a given hidden layer has to te next layer.
     * @param layer the hidden layer to check.
     * @return the nubmer of synapses between this layer and the next layer.
     */
    public int getHiddenSynapseSize(int layer) { return hidden[layer].getSynapseSize(layer); }

    /**
     * Returns the synapse weight of a given synapse from a hidden layer to the next layer. The next layer could
     * either be another hidden layer or the output layer.
     * @param hidden_layer The hidden layer containing the synapse.
     * @param neuron The neuron in the hidden layer.
     * @param neuron_to the neuron in the next layer that the synapse is going to.
     * @return The synapse weight between the two neurons.
     */
    public double getHiddenSynapseWeight(int hidden_layer, int neuron, int neuron_to) {
        return hidden[hidden_layer].getSynapseWeight(neuron, neuron_to);
    }

    /**
     * Returns the total number of hidden layers.
     * @return the nubmer of hidden layers.
     */
    public int getNumHiddenLayers() { return hidden.length; }

    /**
     * Returns the number of neurons in a given hidden layer.
     * @param layer The hidden layer to  check.
     * @return The nubmer of Synapes in that hidden layer.
     */
    public int getHiddenSize(int layer) { return hidden[layer].getLayerSize(); }

    /**
     * Updates the synapse weight for a given layer, from one neuron to another neuron in the next layer.
     * This method does not replace the synapse weight, bu instead adds it to the existing synapses weight.
     * The main purpose of this method is to allow stochastic backpropagation algorithm update the synapse weights.
     * @param layer The hidden layer containing the synapse.
     * @param neuron_from The neuron in the layer.
     * @param neuron_to The neuron the synapse goes to.
     * @param weight The weight to update the current weight with.
     */
    public void updateSynapseWeight(int layer, int neuron_from, int neuron_to, double weight) {
        switch(layer) {
            case 1: input.setSynapseWeight(neuron_from, neuron_to, input.getSynapseWeight(neuron_from, neuron_to) + weight);//input.getSynapseWeight(neuron_from, neuron_to) +
                break;
            case 2: hidden[0].setSynapseWeight(neuron_from, neuron_to, hidden[0].getSynapseWeight(neuron_from, neuron_to) + weight);//hidden[0].getSynapseWeight(neuron_from, neuron_to) +
                break;
            case 4: bias.setSynapseWeight(neuron_from, neuron_to, bias.getSynapseWeight(neuron_from, neuron_to) + weight);//bias.getSynapseWeight(neuron_from, neuron_to) +
        }
    }

    /**
     * Creates a new input layer containing the specified number of neurons.
     * @param size The number of neurons for the new input layer.
     */
    private void setInput(int size) {
        input = new Layer(1, size);
    }

    /**
     * Creates a new hidden layer array containing the correct number of hidden layers. The layer sizes are created
     * elsewhere.
     * @param num_hidden The number of hidden layers for the new hidden layer array.
     */
    private void setHidden(int[] num_hidden) {
        hidden = new Layer[num_hidden.length];
        for(int j = 0; j < hidden.length; j++) {
            hidden[j] = new Layer(2, num_hidden[j]);
        }
    }

    /**
     * Creates a new output layer containing the specified number of neurons.
     * @param size The number of output neurons in the new output layer.
     */
    private void setOutput(int size) {
        output = new Layer(3, size);
    }

    /**
     * Recreates the bias node and its synapses.
     */
    private void setBias() {
        bias = new Layer(4, 1);
    }
}
