package org.opennars.inference;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import org.opennars.main.Parameters;

/**
 * Common functions on real numbers, mostly in [0,1].
 *
 * @author Pei Wang
 * @author Patrick Hammer
 */
public class UtilityFunctions {

    /**
     * A function where the output is conjunctively determined by the inputs
     *
     * @param arr The inputs, each in [0, 1]
     * @return The output that is no larger than each input
     */
    public final static double and(final double... arr) {
        float product = 1;
        for (final double f : arr) {
            product *= f;
        }
        return product;
    }

    /**
     * A function where the output is disjunctively determined by the inputs
     *
     * @param arr The inputs, each in [0, 1]
     * @return The output that is no smaller than each input
     */
    public final static float or(final float... arr) {
        float product = 1;
        for (final float f : arr) {
            product *= (1 - f);
        }
        return 1 - product;
    }

    /**
     * A function where the output is the arithmetic average the inputs
     *
     * @param arr The inputs, each in [0, 1]
     * @return The arithmetic average the inputs
     */
    public final static float aveAri(final float... arr) {
        float sum = 0;
        for (final float f : arr) {
            sum += f;
        }
        return sum / arr.length;
    }

    /**
     * A function where the output is the geometric average the inputs
     *
     * @param arr The inputs, each in [0, 1]
     * @return The geometric average the inputs
     */
    public final static float aveGeo(final float... arr) {
        float product = 1;
        for (final float f : arr) {
            product *= f;
        }

        if (arr.length == 2) {
            return (float) sqrt(arr[0] * arr[1]);
        }
        return (float) pow(product, 1.00 / arr.length);
    }

    /**
     * A function to convert weight to confidence
     *
     * @param w             Weight of evidence, a non-negative real number
     * @param narParameters parameters of the reasoner
     * @return The corresponding confidence, in [0, 1)
     */
    public final static double w2c(final double w, Parameters narParameters) {
        return w / (w + narParameters.HORIZON);
    }

    /**
     * A function to convert confidence to weight
     *
     * @param c             confidence, in [0, 1)
     * @param narParameters parameters of the reasoner
     * @return The corresponding weight of evidence, a non-negative real number
     */
    public final static double c2w(final double c, Parameters narParameters) {
        return narParameters.HORIZON * c / (1 - c);
    }
}
