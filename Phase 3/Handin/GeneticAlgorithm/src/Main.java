import GeneticAlgorithm.GeneticAlgorithm;
import GeneticAlgorithm.Genome;
import InputOutput.FileWriter;
import NeuralNetwork.NeuralNetwork;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
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
    public static void main (String [] args) {
        //main menu
        intro();
        System.out.println("---");
        System.out.println("What would you like to do?\n1. Train a Neural Network\n2. Run an Experiment\n" +
                "3. Exit");

        int input = getInput();

        while(true) {
            switch(input) {
                case 1:
                    trainNetwork();
                    break;
                case 2:
                    expirements();
                    break;
                case 3:
                    System.exit(1);
                default:
                    System.out.println("That's not an option.");
            }

            System.out.println("What would you like to do next?\n1. Train a Neural Network\n2. Run an Experiment\n" +
                    "3. Unit Tests\n4. Exit");
            input = getInput();
        }
    }

    /**
     * Provides options to the user for training or loading a neural network. The neural network(s) are trianed
     * using a genetic algorithm of user defined parameters.
     */
    public static void trainNetwork() {
        int input = 0;
        boolean running = true;

        while(running) {
            System.out.println("---\n1. New Network\n2. Load Existing Network\n3. Back");

            input = getInput();

            switch(input) {
                case 1:
                    NeuralNetwork NN = null;
                    trainNetwork(NN, false);
                    break;
                case 2:
                    loadNetwork();
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("That is not an option.");
            }
        }
    }

    /**
     * Provides the user with options to load a neural network. Once loaded experiments can be ran on the network
     * to test the output of it. the user is also given to option of being able to retrain the loaded network
     * using a genetic algorithm, if they so desire.
     */
    private static void loadNetwork() {

        NeuralNetwork NN = new NeuralNetwork(1, 1, 1);

        String name = "";
        boolean looking = true;
        boolean running = true;

        //Ask the user for the name of the neural network to load.
        //if the file doesn't exist(or cannot be found) then the user can try again, or go back.
        while(looking) {
            System.out.print("What is the networks name? ");
            name = getStringInput();
            //does the file exists?
            if(new File(name).exists()) {
                System.out.println("Loading Network...");
                NN.loadNeuralNetwork(name);
                looking = false;
            }
            else {
                System.out.println("That files doens't exist.\nWhat would you like to do now?\n1. Try Again?\n" +
                        "2. Go Back");
                int input = getInput();
                if(input == 2) {
                    looking = false;
                    running = false;
                }
            }
        }

        //one the user has specified a network to be loaded, and once loading had completed. The user will be able to
        //run tests on the network as well as re-trian it.
        while(running) {
            System.out.println("What would you like to do?\n1. Test the network.\n2. Re-train Network - " +
                    "Performance MAY be worse than original network\n3. Save Network.\n4. Go Back.");
            int input = getInput();

            switch(input) {
                case 1:
                    testNetwork(NN, NN.getInput_size());
                    break;
                case 2:
                    trainNetwork(NN, true);
                    break;
                case 3:
                    System.out.print("Please remember to include a file extension for your convenience!\n" +
                            "File Name: ");
                    String file_name = getStringInput();

                    NN.saveNeuralNetwork(file_name);
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    System.out.println("That's not an option.");
            }
        }
    }

    /**
     * Provides the user with the options needed to train, or retrain a neural network. If the user is training a new
     * network the user is asked to define the topology. Otherwise the network used is the same as the loaded network.
     * The user is then asked to provide the parameters for the genetic algorithm. Once all of the information
     * has be gathered the user is able to run expirements before and after the training of the network to compare
     * the networks output.
     *
     * @param NN the neural network to be trained.
     * @param retrain Whether or not the network is a new network(false), or if it is a loaded network being
     *                retrained(true).
     */
    private static void trainNetwork(NeuralNetwork NN, boolean retrain) {
        int input = 0;

        int input_size;             //size of neural network input layer.
        int num_hidden_layers = 1;
        int[] hidden_layers = null;        //the number of hidden layers and their corresponding size.
        int output_size;            //size of the neural networks output layer.
        int genome_size;            //the size of the genomes to be used with the genetic algorithm.
        int num_generations;        //the number of generations for the neural network.
        int population_size;        //the size of the population for each generation.
        int elite_percentage = 0;       //the percentage of the population that will be chosen elite genomes.
        int mutated_percentage = 0;     //the percentage of the population that will be NEW mutated elite genomes.
        int crossover_percentage = 0;   //the percentage of the population that will be crossover genomes.
        String training_data = "";  //training data.

        //get required information

        //ask for file
        boolean looking = true;
        while(looking) {
            System.out.println("What is the name of the training data? Please ensure that it is in the same directory\n" +
                    "as this .jar.");
            training_data = getStringInput();
            //check to ensure that the file exists
            if(new File(training_data).exists())
                looking = false;
            else
                System.out.println("Sorry, that file cannot be found.");
        }

        if(retrain) {
            System.out.println("Network topology is the same as the pre-loaded network.");

            hidden_layers = new int[NN.getNumberHiddenLayers()];

            input_size = NN.getInput_size();
            for(int j = 0; j < hidden_layers.length; j++)
                hidden_layers[j] = NN.getHiddenSize(j);
            output_size = NN.getOutput_size();
        }
        else {

            //get network topology and genetic algorithm information
            System.out.print("\nNeural Network Topology\nInput Size: ");
            input_size = getInput();

            hidden_layers = new int[num_hidden_layers];
            System.out.print("Hidden Layer Size: ");
            hidden_layers[0] = getInput();

            //todo This section is for multiple hidden layers.
            //todo The genetic algorithm needs to be modified to accomidate for multiple hidden layers.
            //----------------------------------------------------------
            /*System.out.print("Number of hidden layers: ");
            num_hidden_layers = getInput();
            hidden_layers = new int[num_hidden_layers];
            //get the size for each of the hidden layers.
            for(int layer = 0; layer < hidden_layers.length; layer++) {
                System.out.print(layer+"'s Size: ");
                hidden_layers[layer] = getInput();
            }*/
            //----------------------------------------------------------

            System.out.print("Output Size: ");
            output_size = getInput();

            NN = new NeuralNetwork(input_size, hidden_layers, output_size);
        }

        System.out.print("\nGenetic Algorithm Information\nNumber of Generations: ");
        num_generations = getInput();

        System.out.print("Population Size: ");
        population_size = getInput();

        boolean asking = true;
        System.out.println("For Percentages please use integers. :)");
        while(asking) {
            System.out.print("Elite Genome Percentage: ");
            elite_percentage = getInput();

            System.out.print("Mutated Genome Percentage: ");
            mutated_percentage = getInput();

            System.out.print("Crossover Genome Percentage: ");
            crossover_percentage = getInput();

            //deal with an odd percentage of elite genomes
            if((elite_percentage) % 2 != 0) {
                if((mutated_percentage & 2) != 0) {
                    elite_percentage = elite_percentage - 1;
                    mutated_percentage = mutated_percentage + 1;
                }
                else if((crossover_percentage % 2) != 0) {
                    elite_percentage = elite_percentage + 1;
                    crossover_percentage = crossover_percentage - 1;
                }
            }
            else if((mutated_percentage % 2) != 0) {
                mutated_percentage = mutated_percentage + 1;
                crossover_percentage = crossover_percentage - 1;
            }

            if((elite_percentage + mutated_percentage + crossover_percentage) == 100)
                asking = false;
            else
                System.out.println("Please ensure that the sum of the three percentages is equal to 100.");
        }

        genome_size = calcGenomeSize(input_size, hidden_layers, output_size);

        System.out.println("---\n"+genome_size+"\n---");

        //test before training
        boolean testing = true;
        double[] nn_input;
        double[] nn_output;

        //test prior to the training of the neural network.
        while(testing) {
            System.out.println("What would you like to do?\n1. Provide input to Untrained Network\n" +
                    "2. Continue Training");
            input = getInput();
            switch(input) {
                case 1:
                    testNetwork(NN, input_size);
                    break;
                case 2:
                    testing = false;
                    break;
                default:
                    System.out.println("Not an option.");
            }
        }

        //train

        System.out.println("Evolving...");
        Genome evolved = GeneticAlgorithm.evolve(genome_size,
                num_generations,
                population_size,
                elite_percentage,
                mutated_percentage,
                crossover_percentage,
                training_data,
                input_size,
                hidden_layers[0],
                output_size);

        NN.toNetwork(evolved.getSynapses());

        //test after training with option to save
        testing = true;
        while(testing) {
            System.out.println("What would you like to do?\n1. Provide Input to Trained Network\n" +
                    "2. Save This Trained Neural Network\n3. Finish");
            input = getInput();
            switch(input) {
                case 1:
                    testNetwork(NN, input_size);
                    break;
                case 2:
                    System.out.print("Please remember to include a file extension for your convenience!\n" +
                            "File Name: ");
                    String file_name = getStringInput();

                    NN.saveNeuralNetwork(file_name);
                    break;
                case 3:
                    testing = false;
                    break;
                default:
                    System.out.println("Not an option.");
            }
        }
    }

    /**
     * Allows the user to test the given neural network. The user will input a set of values for the input. Those
     * values are then fed through the neural network and the output is printed on the screen.
     *
     * @param NN The neural network to feed the input through.
     * @param input_size The number of neurons in the input layer.
     */
    public static void testNetwork(NeuralNetwork NN, int input_size) {
        double[] nn_output;

        nn_output = NN.feedForward(getNeuralNetworkInput(input_size));
        for(int k = 0; k < nn_output.length; k++)
            System.out.print(nn_output[k]+" ");
        System.out.println();

    }

    /**
     * Calculates the size of the genomes to be used in the neural network based on the networks topology.
     *
     * @param input_size the size of the input layer. Number of input neurons.
     * @param hiddens The hidden layer array. Contains the number of hidden layers, and the number of neurons in each
     *                of said hidden layers.
     * @param output_size The size of the output layer. the number of output neurons.
     * @return The correct size of genome required.
     */
    public static int calcGenomeSize(int input_size, int[] hiddens, int output_size) {
        int ret = 0;

        //number of synapses between input layer and first hidden layer.
        ret = ret + (input_size * hiddens[0]);

        //number of synapses between each hidden layer.
        for(int i = 0; i < hiddens.length-1; i++)
            ret = ret + (hiddens[i] * hiddens[i + 1]);

        //number of synapses between last hidden layer and output layer.
        ret = ret + (hiddens[hiddens.length-1] * output_size);

        //number of synapses between each hidden layer and output later with the bias neuron.
        for(int i = 0; i < hiddens.length; i++)
            ret = ret + hiddens[i];
        ret = ret + output_size;

        return ret;
    }

    /**
     * Asks the user for the desires input to feed through a neural network.
     *
     * @param input_size the number of input neurons for the neural network.
     * @return An array of input values.
     */
    public static double[] getNeuralNetworkInput(int input_size) {
        double[] ret = new double[input_size];

        for(int i = 0; i < ret.length; i++) {
            System.out.print("Input "+i+": ");
            ret[i] = getDoubleInput();
        }

        return ret;
    }

    /**
     * Allows the user to perform a set of experiments for the XOR problem. Determines the top five parameter
     * combinations, then passes these genomes
     */
    public static void expirements() {
        //determine 5 best performing combinations of genomes base on:
            //1. Number of generations
            //2. Number of genomes
            //3. % are elite genomes
            //4. % are mutated genomes
            //5. % are crossover genomes
        int input_size = 2;
        int[] hiddens = {2};
        int output_size = 1;

        ArrayList<Genome> top_five_genomes = new ArrayList<Genome>();
        double[][] top_five = new double[5][6];
        int[] params = new int[5];

        int add_to_top_five = 0;

        int max_genome_size = 500;
        int max_generation_size = 500;

        for(int gen_num = 50; gen_num <= max_generation_size; gen_num = gen_num + 50) {
            System.out.println(gen_num);
            params[0] = gen_num;

            for (int pop_size = 50; pop_size <= max_genome_size; pop_size = pop_size + 10) {
                System.out.println("    "+pop_size);
                params[1] = pop_size;

                for (int elite_percentage = 10; elite_percentage <= 100; elite_percentage = elite_percentage + 10) {
                    System.out.println("        "+elite_percentage);
                    int mutated_percentage = 100 - elite_percentage;
                    params[2] = elite_percentage;

                    for (int crossover_percentage = 0; crossover_percentage < (100 - elite_percentage); crossover_percentage = crossover_percentage + 10) {
                        System.out.println("            Mutation: "+mutated_percentage);
                        System.out.println("            Crossover: "+crossover_percentage);
                        params[3] = mutated_percentage;
                        params[4] = crossover_percentage;

                        //run genetic algorithm, choose best performing(lowest fitness) genome
                        Genome evolved = GeneticAlgorithm.evolve(calcGenomeSize(input_size, hiddens, output_size),
                                gen_num,
                                pop_size,
                                elite_percentage,
                                mutated_percentage,
                                crossover_percentage,
                                "XOR_training_data.csv",
                                2, 2, 1);

                        if(add_to_top_five < top_five.length) {
                            top_five[add_to_top_five][0] = evolved.getRaw_fitness();
                            top_five[add_to_top_five][1] = gen_num;
                            top_five[add_to_top_five][2] = pop_size;
                            top_five[add_to_top_five][3] = elite_percentage;
                            top_five[add_to_top_five][4] = mutated_percentage;
                            top_five[add_to_top_five][5] = crossover_percentage;
                            add_to_top_five = add_to_top_five + 1;
                        }
                        else {
                            //decide whether or not to add parameter combo to top_five
                            for (int i = 0; i < top_five.length; i++) {
                                if (evolved.getRaw_fitness() < top_five[i][0]) {
                                    top_five = addToTopFive(evolved, top_five, params);
                                    break;
                                }
                            }
                        }

                        //decrement mutated_percentage
                        mutated_percentage = mutated_percentage - 10;
                    }
                }
            }
        }
        System.out.println("---");

        for(int i = 0; i < top_five.length; i++) {
            for(int j = 0; j < top_five[i].length; j++)
                System.out.print(top_five[i][j]+" ");
            System.out.println();
        }

        //generate graphs
        generateTopFiveGraphs(top_five);
    }

    /**
     * Adds the parameters of a genome to an array of the top five current performing genome parameter combinations.
     *
     * @param evolved The genome whos parameters are to be copied
     * @param top_five The top five array to add the new genome too.
     * @param params The parameter array for the genome.
     * @return The top five array.
     */
    public static double[][] addToTopFive(Genome evolved, double[][] top_five, int[] params) {
        double[][] tmp = new double[top_five.length][top_five[0].length];
        //do deep copy
        for(int i = 0; i < tmp.length; i++)
            for(int j = 0; j < tmp[0].length; j++)
                tmp[i][j] = top_five[i][j];

        int pos = 0;
        //find position to insert
        for(int i = 0; i < top_five.length; i++) {
            if(evolved.getRaw_fitness() < top_five[i][0]) {
                pos = i;
                break;
            }
        }

        //insert into the top five array
        for(int i = pos; i < top_five.length; i++) {
            for(int j = 0; j < top_five[i].length; j++) {
                if(i == pos) {
                    if (j == 0)
                        top_five[i][j] = evolved.getRaw_fitness();
                    else
                        top_five[i][j] = params[j-1];
                }
                else
                    top_five[i][j] = tmp[i-1][j];
            }
        }


        return top_five;
    }

    /**
     * Generate the .csv file to be used to create the graphs required for the top five performing genomes.
     *
     * @param top_five
     */
    private static void generateTopFiveGraphs(double[][] top_five) {
        DateFormat dateFormat = new SimpleDateFormat("yyyMMdd.HH.mm.ss");
        Date date = new Date();

        int[] hiddens = {2};

        NeuralNetwork NN = new NeuralNetwork(2, hiddens, 1);
        int g_size = 9;

        int elite_percentage = 0;
        int mutated_percentage = 0;
        int crossover_percentage = 0;
        int num_generations = 0;
        int population_size = 0;
        Genome gene;


        //for each one of the top 5 generate a .csv file for each of the three graphs
        for(int current = 0; current < top_five.length; current++) {
            //----------------------------------------------------------------------------------------------------------
            //graph one - dynamic number of generations. Do 50->500 in increments of 10
            FileWriter writer = new FileWriter(current + "GenerationsDynamic-" + dateFormat.format(date) + ".csv");

            population_size = (int) top_five[current][2];
            elite_percentage = (int) top_five[current][3];
            mutated_percentage = (int) top_five[current][4];
            crossover_percentage = (int) top_five[current][5];

            writer.writeToFile("x,y");
            writer.newLine();

            for (num_generations = 50; num_generations <= 500; num_generations = num_generations + 10) {
                gene = GeneticAlgorithm.evolve(g_size, num_generations, population_size, elite_percentage,
                        mutated_percentage, crossover_percentage, "XOR_training_data.csv", 2, 2, 1);
                writer.writeToFile(num_generations + "," + gene.getRaw_fitness());
                writer.newLine();
            }
            writer.closeWriter();
            //----------------------------------------------------------------------------------------------------------

            //----------------------------------------------------------------------------------------------------------
            //graph two - Dynamic number of genomes. Do 50->500 in increments of 10
            writer = new FileWriter(current + "PopulationDynamic-" + dateFormat.format(date) + ".csv");

            num_generations = (int) top_five[current][1];
            elite_percentage = (int) top_five[current][3];
            mutated_percentage = (int) top_five[current][4];
            crossover_percentage = (int) top_five[current][5];

            writer.writeToFile("x,y");
            writer.newLine();

            for(population_size = 50; population_size <= 500; population_size = population_size + 10) {
                gene = GeneticAlgorithm.evolve(g_size, num_generations, population_size, elite_percentage,
                        mutated_percentage, crossover_percentage, "XOR_training_data.csv", 2, 2, 1);
                writer.writeToFile(population_size + "," + gene.getRaw_fitness());
                writer.newLine();
            }
            writer.closeWriter();
            //----------------------------------------------------------------------------------------------------------

            //----------------------------------------------------------------------------------------------------------
            //graph three - dynamic elite percentage. Do 10->100 percent. Increments of 5.
            writer = new FileWriter(current + "ElitePercentageDynamic-" + dateFormat.format(date) + ".csv");

            num_generations = (int) top_five[current][1];
            population_size = (int) top_five[current][2];
            mutated_percentage = (int) top_five[current][4];
            crossover_percentage = (int) top_five[current][5];

            writer.writeToFile("x,y");
            writer.newLine();

            for(elite_percentage = 10; elite_percentage <= 100; elite_percentage = elite_percentage + 10) {
                gene = GeneticAlgorithm.evolve(g_size, num_generations, population_size, elite_percentage,
                        mutated_percentage, crossover_percentage, "XOR_training_data.csv", 2, 2, 1);
                writer.writeToFile(elite_percentage + "," + gene.getRaw_fitness());
                writer.newLine();
            }
            writer.closeWriter();
            //----------------------------------------------------------------------------------------------------------

        }
        System.out.println("DONE! :)");
    }

    public static void unitTests() {

    }

    /**
     * Prints an intro :/
     */
    public static void intro() {
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Project Phase Three\nCPSC 371\nVincent Tennant\n230099844");
        System.out.println("In phase three for CPSC371 we will be exploring the application of Genetic Algorithms\n" +
                "to train Neural Networks.");
    }

    /**
     * Gets input from teh user in the form of a string.
     *
     * @return The input of the user as a String
     */
    public static String getStringInput() {
        Scanner in = new Scanner(System.in);
        return in.next();
    }

    /**
     * Waits for the user to input a double.
     *
     * @return Retruns the double the user input for use
     */
    public static double getDoubleInput() {
        Scanner in;

        boolean looking = true;
        double ret = 0.0;

        while(looking) {
            try {
                in = new Scanner(System.in);
                ret = in.nextDouble();
                looking = false;
            }
            catch(InputMismatchException e) {
                System.out.println("Not a double!");
            }
        }

        return ret;
    }

    /**
     * Waits for the user to input an integer
     *
     * @return Retruns the integer the user input for use
     */
    public static int getInput() {
        Scanner in;

        boolean looking = true;
        int ret = 0;

        while(looking) {
            try {
                in = new Scanner(System.in);
                ret = in.nextInt();
                looking = false;
            }
            catch (InputMismatchException e) {
                System.out.println("Not an Integer!");
            }
        }

        return ret;
    }
}
