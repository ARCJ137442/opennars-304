package org.opennars.plugin.perception;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opennars.entity.*;
import org.opennars.inference.BudgetFunctions;
import org.opennars.interfaces.Timable;
import org.opennars.interfaces.pub.Reasoner;
import org.opennars.io.Narsese;
import org.opennars.io.Symbols;
import org.opennars.io.Texts;
import org.opennars.io.events.EventEmitter;
import org.opennars.io.events.Events;
import org.opennars.io.events.Events.CycleEnd;
import org.opennars.io.events.Events.ResetEnd;
import org.opennars.language.*;
import org.opennars.main.Nar;
import org.opennars.operator.Operator;
import org.opennars.io.Narsese;
import org.opennars.io.Parser;
import org.opennars.entity.Task;
import org.opennars.interfaces.Timable;
import org.opennars.io.events.OutputHandler;
import static org.opennars.storage.Memory.isJUnitTest;

/**
 *
 * @author Patrick
 */

public class NarseseChannel extends SensoryChannel {

    Nar nar;

    public NarseseChannel(Nar nar) {
        super(nar);
        this.nar = nar;
    }

    @Override
    public Nar addInput(final Task t, final Timable time) {
        return nar;
    } // this channel can't receive re-routed tasks

    // special put in here since it's about String and not task and can throw a
    // parsing exception
    public void putIn(Nar nar, String text) throws Parser.InvalidInputException {
        final Parser narsese = new Narsese(nar);
        Task t = narsese.parseTask(text);
        if (!t.sentence.isEternal()) {
            t.sentence.stamp.setOccurrenceTime(nar.time());

        }
        t.sentence.stamp.setCreationTime(nar.time(), nar.narParameters.DURATION);
        nar.memory.emit(OutputHandler.IN.class, t);
        if (!nar.memory.checked) {
            nar.memory.checked = true;
            nar.memory.isjUnit = isJUnitTest();
        }
        this.putIn(t);
    }
}
