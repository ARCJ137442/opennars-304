package org.opennars.control;

import org.opennars.control.concept.ProcessAnticipation;
import org.opennars.entity.Concept;
import org.opennars.entity.Task;
import org.opennars.entity.TermLink;
import org.opennars.inference.BudgetFunctions;
import org.opennars.inference.RuleTables;
import org.opennars.io.events.Events;
import org.opennars.main.Nar;
import org.opennars.main.Parameters;
import org.opennars.storage.Memory;

/**
 * Concept reasoning context
 *
 * a concept is "fired" or activated by applying the reasoner
 *
 * @author Patrick Hammer
 *
 */
public class InferenceControl {

    public static void selectConceptForInference(final Memory mem, final Parameters narParameters, final Nar nar) {
        final Concept currentConcept;
        synchronized (mem.concepts) { // modify concept bag
            currentConcept = mem.concepts.takeOut();
            if (currentConcept == null) {
                return;
            }
        }

        final DerivationContext nal = new DerivationContext(mem, narParameters, nar);
        boolean putBackConcept = false;
        float forgetCycles = 0.0f;
        synchronized (currentConcept) { // use current concept (current concept is the resource)
            ProcessAnticipation.maintainDisappointedAnticipations(narParameters, currentConcept, nar);
            if (currentConcept.taskLinks.size() == 0) { // remove concepts without tasklinks and without termlinks
                mem.concepts.pickOut(currentConcept.getTerm());
                mem.conceptRemoved(currentConcept);
                return;
            }
            if (currentConcept.termLinks.size() == 0) { // remove concepts without tasklinks and without termlinks
                mem.concepts.pickOut(currentConcept.getTerm());
                mem.conceptRemoved(currentConcept);
                return;
            }
            nal.setCurrentConcept(currentConcept);
            putBackConcept = fireConcept(nal, 1);
            if (putBackConcept) {
                forgetCycles = nal.memory.cycles(nal.memory.narParameters.CONCEPT_FORGET_DURATIONS);
                if (nal.memory.emotion != null) {
                    nal.currentConcept.setQuality(
                            BudgetFunctions.or(nal.currentConcept.getQuality(), nal.memory.emotion.happy()));
                }
            }
        }
        if (putBackConcept) { // put back into bag (bag is the resource)
            synchronized (nal.memory.concepts) {
                nal.memory.concepts.putBack(nal.currentConcept, forgetCycles, nal.memory);
            }
        }
    }

    // /return true if concept must be put back
    public static boolean fireConcept(final DerivationContext nal, final int numTaskLinks) {
        for (int i = 0; i < numTaskLinks; i++) {
            if (nal.currentConcept.taskLinks.size() == 0) {
                return false;
            }
            nal.currentTaskLink = nal.currentConcept.taskLinks.takeOut();
            if (nal.currentTaskLink == null) {
                return false;
            }
            if (nal.currentTaskLink.budget.aboveThreshold()) {
                fireTaskLink(nal, nal.memory.narParameters.TERMLINK_MAX_REASONED);
            }
            nal.currentConcept.taskLinks.putBack(nal.currentTaskLink,
                    nal.memory.cycles(nal.memory.narParameters.TASKLINK_FORGET_DURATIONS), nal.memory);
        }
        return true;
    }

    protected static void fireTaskLink(final DerivationContext nal, int termLinks) {
        final Task task = nal.currentTaskLink.getTarget();
        nal.setCurrentTerm(nal.currentConcept.term);
        nal.setCurrentTaskLink(nal.currentTaskLink);
        nal.setCurrentBeliefLink(null);
        nal.setCurrentTask(task); // one of the two places where this variable is set
        if (nal.memory.emotion != null) {
            nal.memory.emotion.adjustBusy(nal.currentTaskLink.getPriority(), nal.currentTaskLink.getDurability(), nal);
        }
        if (nal.currentTaskLink.type == TermLink.TRANSFORM) {
            nal.setCurrentBelief(null);
            // TermLink tasklink_as_termlink = new TermLink(nal.currentTaskLink.getTerm(),
            // TermLink.TRANSFORM, nal.getCurrentTaskLink().index);
            // if(nal.currentTaskLink.novel(tasklink_as_termlink, nal.memory.time(), true))
            // { //then record yourself, but also here novelty counts
            RuleTables.transformTask(nal.currentTaskLink, nal); // to turn this into structural inference as below?
            // }
        } else {
            while (termLinks > 0) {
                final TermLink termLink = nal.currentConcept.selectTermLink(nal.currentTaskLink, nal.time.time(),
                        nal.narParameters);
                if (termLink == null) {
                    break;
                }
                fireTermlink(termLink, nal);
                nal.currentConcept.returnTermLink(termLink);
                termLinks--;
            }
        }

        nal.memory.emit(Events.ConceptFire.class, nal);
        // memory.logic.TASKLINK_FIRE.commit(currentTaskLink.budget.getPriority());
    }

    public static boolean fireTermlink(final TermLink termLink, final DerivationContext nal) {
        nal.setCurrentBeliefLink(termLink);
        RuleTables.reason(nal.currentTaskLink, termLink, nal);
        nal.memory.emit(Events.TermLinkSelect.class, termLink, nal.currentConcept, nal);
        return true;
    }
}