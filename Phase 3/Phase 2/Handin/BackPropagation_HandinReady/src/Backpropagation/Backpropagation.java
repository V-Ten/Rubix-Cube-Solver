package Backpropagation;

/**
 * Backpropagation.java
 * CPSC371
 * Vincent Tennant
 * 230099844
 *
 * Backpropagation interface. Objects of this type are enabled to use stochastic backpropagation to train.
 */
public interface Backpropagation {
    public double error = 0.0;

    public void updateSynapseWeight(int layer, int neuron_from, int neuron_to, double weight);

    public double[] feedForward(double[] input);

    public double calcActivation(double net);

    public double getNeuronValue(int layer, int neuron, int hidden_layer);

    public double getHiddenSynapseWeight(int hidden_layer, int neuron, int k);

    public int getHiddenSynapseSize(int hidden_layer);
    
}
