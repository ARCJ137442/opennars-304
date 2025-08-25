package org.opennars.core.bag;

import org.junit.Test;
import org.opennars.storage.Distributor;

import java.util.ArrayList;
import java.util.List;

/**
 * Report the distribution of the bag
 */
public class DistributorAnalyzer {

    @Test
    public void testDistributorProbabilities() {

        final int levels = 20;
        final Distributor d = new Distributor(levels);
        final int[] count = new int[levels];

        double total = 0;
        for (final int x : d.order) {
            count[x]++;
            total++;
        }

        final List<Double> probability = new ArrayList<>(levels);
        for (int i = 0; i < levels; i++) {
            probability.add(count[i] / total);
        }

        final List<Double> probabilityActiveAdjusted = new ArrayList<>(levels);
        final double activeIncrease = 0.009;
        final double dormantDecrease = ((0.1 * levels) * activeIncrease) / ((1.0 - 0.1) * levels);
        for (int i = 0; i < levels; i++) {
            double p = count[i] / total;
            final double pd = i < ((1.0 - 0.1) * levels) ? -dormantDecrease : activeIncrease;

            p += pd;

            probabilityActiveAdjusted.add(p);
            System.out.println((i / ((double) levels)) + "\t" + p);
        }
        // System.out.println(probabilityActiveAdjusted);

    }

}
