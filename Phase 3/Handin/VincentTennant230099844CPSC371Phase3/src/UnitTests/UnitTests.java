package UnitTests;

import Enums.Rankings;
import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.GeneticImpl;
import GeneticAlgorithm.Genome;
import NeuralNetwork.NeuralNetwork;

import java.util.ArrayList;

/**
 * UnitTests.java
 * CPSC371
 * Vincent Tennant
 * 230099844
 *
 * Some basic unit tests. For GeneticAlgorithm.java.
 */
public class UnitTests {

    public static void testMutateElite() {
        GeneticImpl NN = new NeuralNetwork(2, 2, 1);

        ArrayList<Genome> elite = GeneticAlgorithm.createPopulation(NN, 5, 2);
        ArrayList<Genome> mutated = new ArrayList<Genome>();

        GeneticAlgorithm.mutateElite(mutated, elite, 5, 5);

        for (Genome e : elite)
            printArray(e.getSynapses());

        System.out.println();

        for (Genome m : mutated)
            printArray(m.getSynapses());
    }

    private static void printArray(double[] array) {
        for (double d : array)
            System.out.print(d + " ");
        System.out.println();
    }

    public static void testChooseElite() {
        ArrayList<ArrayList<Double>> tuples = GeneticAlgorithm.generateTuples("XOR_training_data.csv");

        GeneticImpl NN = new NeuralNetwork(2, 2, 1);

        ArrayList<Genome> pop = GeneticAlgorithm.createPopulation(NN, 5, 9);
        ArrayList<Genome> elite = new ArrayList<Genome>();

        GeneticAlgorithm.calcRawFitnesses(pop, NN, tuples, 2);

        GeneticAlgorithm.sortAndRank(Rankings.FITNESS, pop);

        System.out.println("pop: ");
        for (Genome g : pop)
            System.out.println(g.getRaw_fitness());

        GeneticAlgorithm.chooseElite(pop, elite);

        System.out.println();

        System.out.println("pop: ");
        for (Genome g : pop)
            System.out.println(g.getRaw_fitness());
        System.out.println("elite: ");
        for (Genome g : elite)
            System.out.println(g.getRaw_fitness());
    }

    public static void testSetCombinedRank() {
        GeneticImpl NN = new NeuralNetwork(2, 2, 1);

        ArrayList<Genome> pop = GeneticAlgorithm.createPopulation(NN, 5, 9);
        ArrayList<Genome> elite = GeneticAlgorithm.createPopulation(NN, 2, 9);

        GeneticAlgorithm.sortAndRank(Rankings.FITNESS, pop);

        GeneticAlgorithm.calcDiversityScores(pop, elite);

        GeneticAlgorithm.sortAndRank(Rankings.DIVERSITY, pop);

        GeneticAlgorithm.sortAndRank(Rankings.COMBINED, pop);

        for (Genome g : pop)
            System.out.println((g.getFitness_rank() + g.getDiversity_rank()) + " --- " + g.getCombined_rank());
    }

    public static void testCalcDiversityScores() {
        GeneticImpl NN = new NeuralNetwork(2, 2, 1);

        ArrayList<Genome> pop = GeneticAlgorithm.createPopulation(NN, 5, 9);
        ArrayList<Genome> elite = GeneticAlgorithm.createPopulation(NN, 2, 9);

        GeneticAlgorithm.calcDiversityScores(pop, elite);

        for (Genome g : pop)
            System.out.println(g.getDiversity_score());
    }

    public static void testAssignProbabilities() {
        GeneticImpl NN = new NeuralNetwork(2, 2, 1);

        ArrayList<Genome> list = GeneticAlgorithm.createPopulation(NN, 5, 9);

        double error = 0.0;

        double[] input;
        double[] expected_output;
        double[] actual_output;

        ArrayList<ArrayList<Double>> tuples = GeneticAlgorithm.generateTuples("XOR_training_data.csv");

        int count = 0;
        //For each genome in population
        for (Genome current : list) {
            error = 0.0;

            //create neural network from current genome
            NN.toNetwork(current.getSynapses());
            //calculate the genomes error
            for (ArrayList<Double> tuple : tuples) {
                //get input
                input = GeneticAlgorithm.getInputTuple(tuple, 2);
                //get expected output
                expected_output = GeneticAlgorithm.getExpectedOutput(tuple, 2);
                //get actual output
                actual_output = NN.feedForward(input);

                for (int j = 0; j < expected_output.length; j++) {
                    error = error + ((expected_output[j] - actual_output[j]) * (expected_output[j] - actual_output[j]) / 2);
                }
                //error = error / 100;
            }
            //set genomes error
            current.setRaw_fitness(error);
            count = count + 1;
        }

        GeneticAlgorithm.sortAndRank(Rankings.FITNESS, list);
        GeneticAlgorithm.assignProbabilities(list);

        for (Genome g : list)
            System.out.println(g.getProb_selection());
    }
}
