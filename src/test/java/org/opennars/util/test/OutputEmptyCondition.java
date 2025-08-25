package org.opennars.util.test;

import org.opennars.main.Nar;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author me
 */
public class OutputEmptyCondition extends OutputCondition {
    final List<String> output = new LinkedList();

    public OutputEmptyCondition(final Nar nar) {
        super(nar);
        succeeded = true;
    }

    public String getFalseReason() {
        return "FAIL: output exists but should not: " + output;
    }

    @Override
    public boolean condition(final Class channel, final Object signal) {
        // any OUT or ERR output is a failure
        if ((channel == OUT.class) || (channel == ERR.class)) {
            output.add(channel.getSimpleName() + ": " + signal.toString());
            succeeded = false;
            return false;
        }
        return false;
    }

}
