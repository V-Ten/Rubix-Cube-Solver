package GeneticAlgorithm;

/**
 * Created by Vince on 3/30/2015.
 */
public interface GeneticImpl {
    public double[] toGenome();

    public void toNetwork(double[] new_weights);

    public double fitnessTest(double[] synapses);

    public double[] feedForward(double[] input);

    public void reinitialize();
}
