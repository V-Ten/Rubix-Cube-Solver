package Comparators;

import GeneticAlgorithm.Genome;

import java.util.Comparator;

/**
 * Created by Vince on 4/6/2015.
 */
public class DiversityComparator implements Comparator<Genome> {

    /**
     * Compares its two arguments for order. Returns a negative integer, zero, or a positive integer as the first
     * argument is less than, equal to, or greater than the second.
     *
     * This particular comparator will compare two genomes diversity score.
     *
     * @param one The first genome to be compared.
     * @param two The second genome to be compared.
     * @return A negative integer if one is less than two, 0 if they're equal, or a positive integer if one is larger
     *         than two.
     */
    @Override
    public int compare(Genome one, Genome two) {
        int ans = 0;

        if(one.getDiversity_score() < two.getDiversity_score())
            ans = 1;
        else if(one.getDiversity_score() > two.getDiversity_score())
            ans = -1;

        return ans;
    }

}
