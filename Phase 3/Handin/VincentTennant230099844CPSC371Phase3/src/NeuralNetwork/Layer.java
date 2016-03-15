package NeuralNetwork;

import java.util.ArrayList;
import java.util.Random;

/**
 * NeuralNetwork.Layer.java
 * CPSC371
 * Vincent Tennant
 * 230099844
 *
 * This class is a layer class for Neural Networks. Each layer object contains the neurons(ot nodes) and their
 * corresponding values. As well it contains the synapses that each neuron is connected to in the next layer. If the
 * layer is an output layer then there are no synapses that the nodes connect to as it is the end of neural
 * network. Every other layer though will contain an array of the synapses and their weights going to their
 * respective nodes.
 *
 * There are multiple constructors, one for the creation of a simple layer without any connecting layers and one for
 * creating a layer while also connecting it to the corresponding next layer. If the constructor for the layer that
 * does not currently have a layer to connect its synapses to then the connectTo method must be called prior to the
 * layers use if the layer itself is either an input layer, hidden layer or the bias node. An output layer does not
 * have to be connected to any other layers, and should not, as it is the output layer and the end of the line for a
 * neural network.
 */
public class Layer {
    private boolean is_input;
    private boolean is_hidden;
    private boolean is_output;
    private boolean is_bias;

    private int layer_size;

    private double[] neurons;
    private double[][] synapses;

    private ArrayList<Integer> bias_synapses;
    private int layer_number;

    /**
     * Const for the layer class
     * A quirk of this constructor is that the synapse array is not initially created. The setSynapse method must be
     * called in order to initialize and fully create the array.
     *
     * @param type        The type of layer this is. 1 indicates input, 2 indicates a hidden layer, and 3 indicates an output
     *                    layer. A layer type 4 indicates the bias node
     * @param num_neurons The number of neurons in this layer
     */
    public Layer(int type, int num_neurons) {
        layer_size = num_neurons;

        switch (type) {
            case 1:
                is_input = true;
                is_hidden = false;
                is_output = false;
                is_bias = false;
                break;
            case 2:
                is_input = false;
                is_hidden = true;
                is_output = false;
                is_bias = false;
                break;
            case 3:
                is_input = false;
                is_hidden = false;
                is_output = true;
                is_bias = false;
                break;
            case 4:
                is_input = false;
                is_hidden = false;
                is_output = false;
                is_bias = true;
                bias_synapses = new ArrayList<Integer>();
                break;
        }

        neurons = new double[layer_size];
    }

    /**
     * Const for the layer class
     * This methods takes an extra parameter connect_to, which initiates the synapse array as well.
     *
     * @param type        The type of layer this is. 1 indicates input, 2 indicates a hidden layer, and 3 indicates an output
     *                    layer. A layer type 4 indicates the bias node
     * @param num_neurons The number of neurons in this layer.
     * @param connect_to  The NeuralNetwork.Layer to connect to this NeuralNetwork.Layer.
     */
    public Layer(int type, int num_neurons, Layer connect_to) {
        layer_size = num_neurons;

        switch (type) {
            case 1:
                is_input = true;
                is_hidden = false;
                is_output = false;
                is_bias = false;
                break;
            case 2:
                is_input = false;
                is_hidden = true;
                is_output = false;
                is_bias = false;
                break;
            case 3:
                is_input = false;
                is_hidden = false;
                is_output = true;
                is_bias = false;
                break;
            case 4:
                is_input = false;
                is_hidden = false;
                is_output = false;
                is_bias = true;
                break;
        }

        neurons = new double[layer_size];
        this.connectTo(connect_to);
    }

    /**
     * Constructs the array of synapses from this layer to the next layer.
     * This method is not applicable if the layer is an output layer.
     *
     * @param connect_to The layer to connect synapses to
     */
    public void connectTo(Layer connect_to) { synapses = new double[layer_size][connect_to.getLayerSize()]; }

    /**
     * Sets the bias neurons synapses.
     * This method will handle specific accounts where there are multiple hidden layers.
     *
     * @param hidden the hidden layer to connect this neuron to.
     * @param output the output layer to connect this neuron to.
     */
    public void connectBiasTo(Layer hidden[], Layer output) {
        int num_hidden_neurons = 0;

        for(int j = 0; j < hidden.length; j++) {
            //add hidden layer j marker
            bias_synapses.add(num_hidden_neurons);
            num_hidden_neurons = num_hidden_neurons + hidden[j].getLayerSize();
        }
        //add output marker
        bias_synapses.add(num_hidden_neurons);

        synapses = new double[1][num_hidden_neurons + output.getLayerSize()];
    }

    /**
     * Connects the synapses coming from this layer to the next layer. There are two possible cases.
     * The synapses coming form the input layer going to the hidden layer, or the hidden layer going to the output
     * layer.
     */
    public void initSynapses() {
        Random rand = new Random();

        double range_plus = 1/Math.sqrt(layer_size);
        double range_minus = -1/Math.sqrt(layer_size);

        boolean keep_looking;
        double cplus;
        double pos_or_neg;

        for(int i = 0; i < synapses.length; i++) {
            for(int j = 0; j < synapses[i].length; j++) {
                keep_looking = true;
                while(keep_looking) {
                    cplus = rand.nextDouble();
                    if((cplus <= range_plus || cplus >= range_minus) && cplus != 0.0) {
                        pos_or_neg = rand.nextDouble();
                        keep_looking = false;
                        if (pos_or_neg <= 0.5)
                            synapses[i][j] = -cplus;
                        else
                            synapses[i][j] = cplus;
                    }
                }
            }
        }
    }

    /**
     * Connects the synapses coming from this layer to the next layer. There are two possible cases.
     * The synapses coming form the input layer going to the hidden layer, or the hidden layer going to the output
     * layer.
     * This first, simple version that initializes the synapses simple gives them a value between 1 and -1,
     * and hopes on not generating zero. Since this is just a simple "lets get it working" method of giving synapses
     * their initial weights that isn't a serious issue.
     *
     * TODO This the best method of initializing the synapses, use method suggested in class! It's above this method.
     */
    public void initSynapsesSimple() {
        Random rand = new Random();

        double cplus;
        double cminus;
        double pos_or_neg;

        for (int i = 0; i < synapses.length; i++) {
            for (int j = 0; j < synapses[i].length; j++) {
                cplus = rand.nextDouble();
                cminus = -rand.nextDouble();
                pos_or_neg = rand.nextDouble();

                if (pos_or_neg <= 0.5)
                    synapses[i][j] = cminus;
                else
                    synapses[i][j] = cplus;
            }
        }
    }

    public void setSynapseWeight(int node, int node_to, double val) {
        synapses[node][node_to] = val;
    }

    public int getSynapsesLength() { return synapses.length; }

    public int getInnerSynapsesSize(int pos) { return synapses[pos].length; }

    /**
     * Returns this layers layer size.
     *
     * @return layer_size
     */
    public int getLayerSize() {
        return layer_size;
    }

    /**
     * Sets a specific neurons value to equal val
     *
     * @param neuron The neuron whos value will be changed
     * @param val    The value the neuron will be changed to
     */
    public void setNeuronValue(int neuron, double val) {
        neurons[neuron] = val;
    }

    /**
     * Sets this layers layer number. Used by the bias number to determine this layers position in its synapse array.
     * Input neurons and the bias neuron are not assigned layer numbers as they aren't required.
     * @param n The layer number for this layer.
     */
    public void setLayerNumber(int n) { layer_number = n; }

    /**
     * Returns this layers layer number
     * @return layer_number
     */
    public int getLayerNumber() { return layer_number; }

    /**
     * Gets and returns a weight for a synapse going from node "node".
     * @param node The node that the synapse is coming from.
     * @param node_to The node the synapse is going to.
     * @return The double value of the weight.
     */
    public double getSynapseWeight(int node, int node_to) {
        return synapses[node][node_to];
    }

    /**
     * Returns the number of outgoing synapses from the requested neuron.
     * @param node The neuron to look at.
     * @return The number of synapses coming from said neuron.
     */
    public int getSynapseSize(int node) { return synapses[node].length; }

    /**
     * This method is used to return the synapse weight for a synapse going from a bias neuron to a neuron in a hidden
     * layer or the output layer. Input layer neurons are not accounted for as the bias neuron does not have synapses
     * connecting to the input nodes.
     *
     * NOTE: The (first)hidden layer is considered layer 0, as opposed to layer 1. The input layer is not counted
     * so the (first) hidden layer is the first layer that the bias layer connects to.
     *
     * @param layer The layer number of the layer containing the neuron connecting to the bias neuron.
     * @param node_to The specific neuron that the bias neuron is connected to within said layer.
     * @return The synapse weight of the synapse between the bias neuron and node_to.
     */
    public double getBiasSynapseWeight(int layer, int node_to) {

        int node_at = 0;

        if(layer + 1 == bias_synapses.size()) {
            for(int j = bias_synapses.get(layer); j < synapses[0].length; j++) {
                if(node_at == node_to)
                    return synapses[0][j];
                else
                    node_at++;
            }
        }
        else {
            for(int j = bias_synapses.get(layer); j < bias_synapses.get(layer + 1); j++) {
                if(node_at == node_to)
                    return synapses[0][j];
                else
                    node_at++;
            }
        }
        return -1.0;
    }

    /**
     * Returns a neurons value from this layer.
     * @param node The neuron who's value to be returned.
     * @return The neurons value.
     */
    public double getNeuronValue(int node) { return neurons[node]; }

    /**
     * A series of boolean test that return true or false based on the type of layer this layer is.
     * @return true or false based on this layers layer type.
     */
    public boolean isInput() {
        if(is_input)
            return true;
        else
            return false;
    }
    public boolean isHidden() {
        if(is_hidden)
            return true;
        else
            return false;
    }
    public boolean isOutput() {
        if(is_output)
            return true;
        else
            return false;
    }
    public boolean isBias() {
        if(is_bias)
            return true;
        else
            return false;
    }
}
