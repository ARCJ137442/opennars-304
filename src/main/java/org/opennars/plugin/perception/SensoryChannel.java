package org.opennars.plugin.perception;

import org.opennars.entity.Concept;
import org.opennars.entity.Task;
import org.opennars.interfaces.Timable;
import org.opennars.io.Narsese;
import org.opennars.language.Term;
import org.opennars.main.Nar;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opennars.plugin.Plugin;

public abstract class SensoryChannel implements Plugin {
    private Collection<SensoryChannel> reportResultsTo;
    public Nar nar; // for top-down influence of concept budgets
    public final List<Task> results = new ArrayList<>();
    public volatile int height = 0; // 1D channels have height 1
    public volatile int width = 0;
    public volatile int duration = -1;
    private volatile Term label;

    public void resetChannel() {
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double val) {
        this.height = (int) val;
        resetChannel();
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double val) {
        this.width = (int) val;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double val) {
        this.duration = (int) val;
    }

    public SensoryChannel() {
    }

    public SensoryChannel(final Nar nar, final Collection<SensoryChannel> reportResultsTo, final int width,
            final int height, final int duration, Term label) {
        this.reportResultsTo = reportResultsTo;
        this.nar = nar;
        this.width = width;
        this.height = height;
        this.duration = duration;
        this.label = label;
    }

    public SensoryChannel(final Nar nar, final SensoryChannel reportResultsTo, final int width, final int height,
            final int duration, Term label) {
        this(nar, Collections.singletonList(reportResultsTo), width, height, duration, label);
    }

    public void addInput(final String text, final Timable time) {
        try {
            final Task t = new Narsese(nar).parseTask(text);
            this.addInput(t, time);
        } catch (final Narsese.InvalidInputException ex) {
            Logger.getLogger(SensoryChannel.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("Could not parse input", ex);
        }
    }

    public abstract Nar addInput(final Task t, final Timable time);

    public void step_start(final Timable time) {
    } // needs to put results into results and call step_finished when ready

    public void step_finished(final Timable time) {
        for (final SensoryChannel ch : reportResultsTo) {
            for (final Task t : results) {
                ch.addInput(t, time);
            }
        }
        results.clear();
    }

    public double topDownPriority(final Term t) {
        double prioritySum = 0.0f;
        for (final SensoryChannel chan : reportResultsTo) {
            prioritySum += chan.priority(t);
        }
        return prioritySum / (double) reportResultsTo.size();
    }

    public double priority(final Term t) {
        if (this instanceof Nar) { // on highest level it is simply the concept priority
            final Concept c = ((Nar) this).memory.concept(t);
            if (c != null) {
                return c.getPriority();
            }
        }
        return 0.0;
    }

    public String getName() {
        return label.toString();
    }

    public void setName(String val) {
        this.label = new Term(val);
        this.nar.removePlugin(this.nar.new PluginState(this));
        this.nar.addPlugin(this);
    }
}
