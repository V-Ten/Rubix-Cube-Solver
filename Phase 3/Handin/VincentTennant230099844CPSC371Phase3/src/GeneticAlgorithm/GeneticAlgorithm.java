package GeneticAlgorithm;

import Comparators.CombinedComparator;
import Comparators.DiversityComparator;
import Comparators.FitnessComparator;
import Enums.Rankings;
import NeuralNetwork.NeuralNetwork;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * GeneticAlgorithm.java
 * CPSC371
 * Vincent Tennant
 * 230099844
 *
 * Genetic Algorithm. Creates a population of genomes consisting of neural network synapse weights. The population
 * is then evolved a desired number of times until the optimal genome is found.
 */
public class GeneticAlgorithm {

    private static Comparator fitness_comp = new FitnessComparator();
    private static Comparator diversity_comp = new DiversityComparator();
    private static Comparator combined_comp = new CombinedComparator();

    private static GeneticImpl NN;

    private static int population_size;
    private static int genome_size;
    private static int input_size;

    private static ArrayList<ArrayList<Double>> tuples;

    private static Random rand = new Random();

    /**
     * Genetic algorithm. Evolves a set of genomes to attempt to find the optimal solution to a particular problem.
     * From the initial population the genomes are sorted and ranked based on their initial fitness, or rather how well
     * they perform in the given problem. Once sorted and ranked a set number of the genomes are chosen as the elite
     * genomes. From this set of chosen elite genomes a small(or large) portion of the remaining population to be
     * filled is made up of mutated genomes. Mutated genomes are elite genomes that are copied, and then have one
     * or more of their parameters perturbed slightly. The reminder of the population id filled with crossover
     * genomes. Crossover Genomes are created by mixing the parameters of two chosen elite genomes to create one new
     * genome.
     * This process is repeated until the set number of generations is reached. The most optimal genome in the
     * population after all of the generations is not guaranteed to be an optimal solution ot the given problem.
     *
     * NOTE The number of elite genomes, mutated genomes, and crossover genomes must sum to the size of the population.
     *
     * @param g_size               The size of each genome.
     * @param num_generations      The number of generations to run the genetic algorithm for.
     * @param pop_size             The size of the population for each generation.
     * @param elite_percentage     The number of elite genomes to be chosen. Size is in percentage.
     * @param mutated_percentage   The number of mutated genomes to be created. Size is in percentage.
     * @param crossover_percentage The number of crossover genomes to be created. Size is in percentage.
     */
    public static Genome evolve(int g_size,
                                int num_generations,
                                int pop_size,
                                int elite_percentage,
                                int mutated_percentage,
                                int crossover_percentage,
                                String training_data,
                                int i_size,
                                int hidden_size, //todo accomidate for multiple hidden layers.
                                int output_size) {


        ArrayList<Genome> population;
        ArrayList<Genome> elite = null;
        ArrayList<Genome> mutated = null;
        ArrayList<Genome> crossover = null;

        population_size = pop_size;
        genome_size = g_size;
        input_size = i_size;

        //sets the sizes of each array list based on their percentages.
        //pos 0 == elite size
        //pos 1 == mutated size
        //pos 2 == crossover size
        int[] list_sizes = calcListSizes(elite_percentage, mutated_percentage, crossover_percentage);

        tuples = generateTuples(training_data);

        NN = new NeuralNetwork(input_size, hidden_size, output_size);

        //initialize first population.
        population = createPopulation(NN, population_size, genome_size);

        //begin generation loop
        for (int current_gen = 0; current_gen < num_generations; current_gen++) {

            elite = new ArrayList<Genome>();
            mutated = new ArrayList<Genome>();
            crossover = new ArrayList<Genome>();

            calcRawFitnesses(population, NN, tuples, input_size);                         //calculate the raw fitness of each genome.
            sortAndRank(Rankings.FITNESS, population);
            assignProbabilities(population);                      //assign probabilities of being chosen based on fitness rank.

            //choose first elite genome
            chooseElite(population, elite);

            //choose remaining elite genome
            chooseRemainingElite(list_sizes[0], population, elite);

            //perform mutations with elite population
            mutateElite(mutated, elite, list_sizes[1], elite.size());

            //perform crossover with elite population
            crossoverElite(crossover, elite, list_sizes[2], elite.size());

            //combine elite, mutated, and crossover lists into one new population list
            population = new ArrayList<Genome>();
            population.addAll(elite);
            population.addAll(mutated);
            population.addAll(crossover);
        }
        //end generation loop

        //todo sort based on fitness an choose best performing genome and run through genetic algorithm.
        calcRawFitnesses(population, NN, tuples, input_size);
        sortAndRank(Rankings.FITNESS, population);

        return population.get(0);
    }

    /**
     * Calculates the list sizes based on the desired population size, and the desired sizes of the elite, mutated,
     * and crossover genome lists.
     *
     * @param elite_size     the percentage of the total population that will be selected elite genomes.
     * @param mutated_size   The percentage of the population that will consist of mutated genomes.
     * @param crossover_size The percentage of the population that will consists of crossover genomes.
     * @return An array of size 3 for the number of elite genomes, number of mutated genomes, and the number of
     * crossover genomes, in that order.
     */
    public static int[] calcListSizes(int elite_size, int mutated_size, int crossover_size) {
        int[] ret = new int[3];

        ret[0] = (int) (population_size * ((double) elite_size / 100));
        ret[1] = (int) (population_size * ((double) mutated_size / 100));
        ret[2] = (int) (population_size * ((double) crossover_size / 100));

        return ret;
    }

    /**
     * Generates an array list of mutated genomes from the already chosen elite genomes. The elite genomes chosen to be
     * mutated are not affected themselves, they are instead copied and then randomly perturbed.
     *
     * @param mute The list where the mutated genomes will be contained.
     * @param elt   The elite genomes list.
     * @param mute_size The number of mutated genomes to produce
     * @param elt_size The number of existing elite genome
     */
    public static void mutateElite(ArrayList<Genome> mute, ArrayList<Genome> elt, int mute_size, int elt_size) {

        double mutation_amount = 0.25; //todo This value can be played with. Experiment.

        for (int pos = 0; pos < mute_size; pos++) {
            int chosen_elite = rand.nextInt(elt_size);

            double[] elites_synapses = elt.get(chosen_elite).getSynapses();
            double[] mutated_synapses = new double[elites_synapses.length];

            for (int i = 0; i < elites_synapses.length; i++) {
                double value = elites_synapses[i];
                //is this specific value modified?
                if (rand.nextDouble() >= 0.5) {
                    //add or subtract from value?
                    if (rand.nextDouble() >= 0.5)
                        value = value + (value * (rand.nextDouble() % mutation_amount));
                    else
                        value = value - (value * (rand.nextDouble() % mutation_amount));
                }

                mutated_synapses[i] = value;
            }

            mute.add(new Genome(mutated_synapses));
        }
    }

    /**
     * Generates an array list of crossovered genomes from two different elite genomes chosen at random. Two random
     * elite genomes are chosen, then random elements from each genome are chosen to create a new genome.
     *
     * @param cross The list where the crossover genomes will be contained.
     * @param elt     The elite genomes list.
     * @param cross_size The number of cross over genomes to generate.
     * @param elt_size The number of existing elite genomes.
     */
    private static void crossoverElite(ArrayList<Genome> cross, ArrayList<Genome> elt, int cross_size, int elt_size) {
        //randomly choose two different elite genomes.

        //assign an array of equal size to teh chosen genome with 1s and zeros


        for (int pos = 0; pos < cross_size; pos++) {
            int[] crossover_array = new int[genome_size];
            double[] crossover_synapses = new double[genome_size];
            int second_elite;

            int first_elite = rand.nextInt(elt_size);
            //ensure that the same genomes aren't chosen, no matter how small the chance.
            while (true) {
                second_elite = rand.nextInt(elt_size);
                if (second_elite != first_elite)
                    break;
            }

            double[] first_elite_synapses = elt.get(first_elite).getSynapses();
            double[] second_elite_synapses = elt.get(second_elite).getSynapses();

            //generate the crossover array
            for (int i = 0; i < crossover_array.length; i++) {
                if (rand.nextDouble() >= 0.5)
                    crossover_array[i] = 0;
                else
                    crossover_array[i] = 1;
            }

            //build the crossover_array
            for (int j = 0; j < crossover_array.length; j++) {
                if (crossover_array[j] == 0)
                    crossover_synapses[j] = first_elite_synapses[j];
                else
                    crossover_synapses[j] = second_elite_synapses[j];
            }

            //create genome
            cross.add(new Genome(crossover_synapses));
        }

    }

    /**
     * Chooses an elite genome based on their probability of selection based on either fitness or combined rank. It
     * does not matter which the probability is based on since the probability is based on over sorting/rank of
     * population.
     *
     * @param pop The population array list. The current generation is contained within this array list.
     * @param elt      The elite genomes list.
     */
    public static void chooseElite(ArrayList<Genome> pop, ArrayList<Genome> elt) {
        double chosen = rand.nextDouble();

        //look for chosen genome
        for(int i = 0; i < pop.size(); i++) {
            if(chosen > pop.get(i).getProb_selection()) {
                elt.add(pop.get(i));
                pop.remove(i);
                break;
            }
        }
    }

    /**
     * Chooses the remain elite. Chooses elite_size-1 elite genomes as the first elite genome is chosen purley
     *
     * @param elt_size The number of elite genomes to choose.
     * @param pop The population array list. The current generation is contained within this array list.
     * @param elt      The elite genomes list.
     */
    private static void chooseRemainingElite(int elt_size, ArrayList<Genome> pop, ArrayList<Genome> elt) {

        //choose elite_size-1 number of elite genomes. Do in for loop
        for (int i = 0; i < elt_size - 1; i++) {

            //calculate each remaining genome in populations diversity score.
            calcDiversityScores(pop, elt);

            //sort population based on their diversity score and assign their rank.
            sortAndRank(Rankings.DIVERSITY, pop);

            //calculate each genomes combined rank, then sort based on combined rank
            sortAndRank(Rankings.COMBINED, pop);

            //assign probability of being chosen based on combined rank
            assignProbabilities(pop);

            //choose next genome.
            chooseElite(pop, elt);
        }
        //end for loop
    }

    /**
     * Calculates the diversity score of each remaining genome in population. The vector difference is the summation
     * of the differences between the genomes in populations encoded synapses, and the elites encoded synapses.
     * The vector differences are squared at the end to prevent negative scores from occurring. This allows
     * genomes in population to be easily sorted based on their diversity score.
     *
     * @param pop The population array list. The current generation is contained within this array list.
     * @param elt      The elite genomes list.
     */
    public static void calcDiversityScores(ArrayList<Genome> pop, ArrayList<Genome> elt) {
        //double vector_difference;
        //double[] pop_encoding;
        //double[] elite_encoding;

        //get summation of the vector difference between each genome in population against each elite genome
        for(int genome = 0; genome < pop.size(); genome++) {
            double vector_difference = 0.0;
            double[] pop_encoding = pop.get(genome).getSynapses();
            for(int elite_genome = 0; elite_genome < elt.size(); elite_genome++) {
                double[] elite_encoding = elt.get(elite_genome).getSynapses();

                //calculate vector difference
                for (int j = 0; j < pop_encoding.length; j++) {
                    vector_difference = vector_difference + ((pop_encoding[j] - elite_encoding[j]) * (pop_encoding[j] - elite_encoding[j]));
                }
            }
        }
    }

    /**
     * Assigns the probability of being chosen to each genome in population. The chance of being chosen is a non-zero
     * possibility, to ensure that all genomes are included.
     * This method is used for both the probability of selection based on fitness rank, and the probability of
     * selection based on combined rank, since both use the same math.
     * <p/>
     * This method works on the assumption that the population is already sorted in the way the user desires.
     *
     * @param pop The population array list. The current generation is contained within this array list.
     */
    public static void assignProbabilities(ArrayList<Genome> pop) {
        double scalar = 0.667;              //scalar value for the probability equation.
        double prev_probabilities = 0.0;    //summation of all previous probabilities.
        double probability = 0.0;           //hold current probability for the current genome.

        for(int i = 0; i < pop.size(); i++) {
            probability = scalar * (1 - prev_probabilities);
            pop.get(i).setProb_selection(probability);

            prev_probabilities = prev_probabilities + probability;
        }
    }

    /**
     * Calculates the raw fitness of each genome by feeding it through a neural network, and testing it against all
     * possible inputs. Based on its accuracy(or error) it is assigned a fitness value. The lower the fitness the
     * more accurate the genome is for solving the desired problem, and thus is a more desirable genome.
     *
     * @param pop The population array list. The current generation is contained within this array list.
     * @param nn The neural network to used for calculating the raw fitness.
     * @param tuples The list of tuples for the input and expected output. Used to determine the error or the neural
     *               network.
     * @param input_size The size of the input layer in the neural network.
     */
    public static void calcRawFitnesses(ArrayList<Genome> pop, GeneticImpl nn, ArrayList<ArrayList<Double>> tuples, int input_size) {

        double[] input;
        double[] expected_output;
        double[] actual_output;

        for(int current = 0; current < pop.size(); current++) {
            double error = 0.0;

            //create neural network from current genome
            nn.toNetwork(pop.get(current).getSynapses());
            //calculate the genomes error
            for (ArrayList<Double> tuple : tuples) {
                //get input
                input = getInputTuple(tuple, input_size);
                //get expected output
                expected_output = getExpectedOutput(tuple, input_size);
                //get actual output
                actual_output = nn.feedForward(input);

                for (int j = 0; j < expected_output.length; j++)
                    error = error +
                            ((actual_output[j] - expected_output[j]) * (actual_output[j] - expected_output[j]));
            }
            //set genomes error
            pop.get(current).setRaw_fitness(error);
        }
    }

    /**
     * Ranks and then sorts population based on the desired operation. The method sorts then ranks based on fitness
     * score. Sorts then ranks based on diversity score. Or ranks and then sorts based on combined score.
     * Since combined score is a combination of both fitness rank and diversity rank, population is ranked first, and
     * then sorted. As opposed to sorted then ranked.
     *
     * @param operation  The operation to perform, sort and rank based on fitness score. Sort and rank based on
     *                   diversity score. Or rank and sort on combined score.
     * @param pop The population array list. The current generation is contained within this array list.
     */
    public static void sortAndRank(Rankings operation, ArrayList<Genome> pop) {
        if (operation.equals(Rankings.FITNESS)) {
            pop.sort(fitness_comp);

            for (int rank = 0; rank < pop.size(); rank++)
                pop.get(rank).setFitness_rank(rank);
        } else if (operation.equals(Rankings.DIVERSITY)) {
            pop.sort(diversity_comp);

            for (int rank = 0; rank < pop.size(); rank++) {
                pop.get(rank).setDiversity_rank(rank);
            }
        } else if (operation.equals(Rankings.COMBINED)) {
            for (Genome cur : pop)
                cur.setCombined_rank(cur.getFitness_rank() + cur.getDiversity_rank());

            pop.sort(combined_comp);
        }
    }

    /**
     * Creates the initial population to be used in the GA(Genetic Algorithm) method. The size of the initial
     * population is equal to population_size.
     *
     * @param net The network that generates the genomes to be used.
     * @return An array list containing the genomes for the genetic algorithm.
     */
    public static ArrayList<Genome> createPopulation(GeneticImpl net, int pop_size, int g_size) {
        ArrayList<Genome> ret = new ArrayList<Genome>();
        double[] tmp;

        for (int i = 0; i < pop_size; i++) {
            tmp = new double[g_size];
            //get genome array
            for (int j = 0; j < g_size; j++) {
                if (rand.nextDouble() >= 0.5)
                    tmp[j] = -rand.nextDouble();
                else
                    tmp[j] = rand.nextDouble();
            }

            ret.add(new Genome(tmp));
        }

        /*for(int i = 0; i < population_size; i++) {
            ret.add(new Genome(net.toGenome()));
            net.reinitialize();
        }*/

        return ret;
    }

    /**
     * Creates the initial population to be used in the GA(Genetic Algorithm) method. The size of the initial
     * population is equal to population_size.
     *
     * @return An array list containing the genomes  for the genetic algorithm.
     */
    private static ArrayList<Genome> createPopulation() {
        //create an array list of genomes of size g_size, the genomes themselves need not be connected to any neural
        //network.
        double pos_or_neg;
        double value;
        double[] genome_array;
        ArrayList<Genome> ret = new ArrayList<Genome>();

        for (int i = 0; i < population_size; i++) {
            genome_array = new double[genome_size];
            for (int j = 0; j < genome_size; j++) {
                pos_or_neg = rand.nextDouble();
                value = rand.nextDouble();
                //make value negative if pos or neg is below 0.5.
                if (pos_or_neg < 0.5)
                    value = value - 1;

                genome_array[j] = value;
            }
            ret.add(new Genome(genome_array));
        }

        return ret;
    }

    /**
     * Reads the training data file and turns it into an array of doubles. The arrays are arranged in such a way that
     * the first element in each array is the expected result, every other element is input for the neural network.
     *
     * @param training_data The name of the training data file to be read from.
     */
    public static ArrayList<ArrayList<Double>> generateTuples(String training_data) {
        ArrayList<ArrayList<Double>> ret = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> tuple;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(training_data));
            StringTokenizer tokener;

            String next_line = reader.readLine();

            while (next_line != null) {
                tuple = new ArrayList<Double>();
                tokener = new StringTokenizer(next_line);

                //iterate though the tokenized string.
                while (tokener.hasMoreTokens()) {
                    tuple.add(Double.valueOf(tokener.nextToken()));
                }
                //add the tuple to the list of training tuples.
                ret.add(tuple);

                next_line = reader.readLine();
            }
        } catch (FileNotFoundException nf) {
            System.out.println("File " + training_data + " was not found.  " + nf + " ::: @ generateTuples");
        } catch (Exception e) {
            System.out.println(e + " ::: @ generateTuples");
        }

        return ret;
    }

    /**
     * Create an array of double to be used as input for a neural netowork.
     *
     * @param list       The Array List to extract the values from.
     * @param input_size The size of the input layer.
     * @return The array of double values to be used as input for a neural network.
     */
    public static double[] getInputTuple(ArrayList<Double> list, int input_size) {
        double[] ret = new double[input_size];
        for (int i = 0; i < input_size; i++) {
            ret[i] = list.get(i);
        }
        return ret;
    }

    /**
     * Creates an array of double values containing the expected out put of any given tuple.
     *
     * @param list       The list containing the expected output and the input required to obtain that output.
     * @param input_size The size of the input layer.
     * @return The array of double values containing the expected output.
     */
    public static double[] getExpectedOutput(ArrayList<Double> list, int input_size) {
        int k = 0;
        while (k <= input_size - 1)
            k = k + 1;

        double[] ret = new double[list.size() - input_size];

        for (int pos = 0; k < list.size() && pos < ret.length; pos++) {
            ret[pos] = list.get(k);
            k = k + 1;
        }

        return ret;
    }
}
