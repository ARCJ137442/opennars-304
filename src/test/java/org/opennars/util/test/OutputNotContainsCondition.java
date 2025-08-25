package org.opennars.util.test;

import org.opennars.main.Nar;

/**
 *
 * @author me
 */
public class OutputNotContainsCondition extends OutputContainsCondition {

    public OutputNotContainsCondition(final Nar nar, final String containing) {
        super(nar, containing, -1);
        succeeded = true;
    }

    @Override
    public String getFalseReason() {
        return "incorrect output: " + containing;
    }

    @Override
    public boolean condition(final Class<?> channel, final Object signal) {
        if (!succeeded) {
            return false;
        }
        if (cond(channel, signal)) {
            onFailure(channel, signal);
            succeeded = false;
            return false;
        }
        return true;
    }

    public boolean isInverse() {
        return true;
    }

    protected void onFailure(final Class<?> channel, final Object signal) {
    }

}
