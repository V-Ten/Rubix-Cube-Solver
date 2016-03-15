package GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Genome.java
 * CPSC371
 * Vincent Tennant
 * 230099844
 *
 * The main container for a genome for the Genetic algorithm. The information required for a genome to hold is
 * contained here.
 * This class could easily be expanded upon to include multiple different type of information for the genetic
 * algorithm to use.
 */
public class Genome {

    double[] synapses;

    private double raw_fitness;     //Raw fitness score for this genome.
    private int fitness_rank;       //The rank of this genome based on its raw fitness.
    private double prob_selection;  //The probability of selection based on either fitness rank, or combined rank.
    private double diversity_score; //The diversity score of this genome.
    private int diversity_rank;     //The diversity rank of this genome.
    private int combined_rank;      //Combined rank for this genome.

    public Genome(double[] encoding) {

        synapses = encoding;

        raw_fitness = 0;
        fitness_rank = 0;
        prob_selection = 0;
        diversity_score = 0;
        diversity_rank = 0;
        combined_rank = 0;
    }

    //Setters
    public void setSynapses(double[] new_synapses) { synapses = new_synapses; }

    public void setRaw_fitness(double fitness) { raw_fitness = fitness; }
    public void setFitness_rank(int rank) { fitness_rank = rank; }
    public void setProb_selection(double probability) { prob_selection = probability; }
    public void setDiversity_score(double score) { diversity_score = score; }
    public void setDiversity_rank(int rank) { diversity_rank = rank; }
    public void setCombined_rank(int rank) { combined_rank = rank; }

    //Getters
    public double[] getSynapses() { return synapses; }

    public double getRaw_fitness() { return raw_fitness; }
    public int getFitness_rank() { return fitness_rank; }
    public double getProb_selection() { return prob_selection; }
    public double getDiversity_score() { return diversity_score; }
    public int getDiversity_rank() { return diversity_rank; }
    public int getCombined_rank() { return combined_rank; }
}
