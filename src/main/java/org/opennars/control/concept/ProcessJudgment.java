package org.opennars.control.concept;

import com.google.common.base.Optional;
import org.opennars.control.DerivationContext;
import org.opennars.entity.Concept;
import org.opennars.entity.Sentence;
import org.opennars.entity.Stamp;
import org.opennars.entity.Task;
import org.opennars.storage.Memory;
import org.opennars.storage.InternalExperienceBuffer;
import org.opennars.inference.BudgetFunctions;

import static com.google.common.collect.Iterables.tryFind;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.opennars.inference.LocalRules.revisible;
import static org.opennars.inference.LocalRules.revision;
import static org.opennars.inference.LocalRules.trySolution;
import static org.opennars.inference.LocalRules.calcTaskAchievement;

import org.opennars.inference.TemporalRules;
import org.opennars.io.events.Events;
import org.opennars.language.CompoundTerm;
import org.opennars.language.Conjunction;
import org.opennars.language.Implication;
import org.opennars.language.Interval;
import org.opennars.language.Term;
import org.opennars.operator.Operation;
import org.opennars.operator.Operator;
import org.opennars.operator.mental.Anticipate;
import org.opennars.operator.mental.Believe;
import org.opennars.operator.mental.Evaluate;
import org.opennars.operator.mental.Want;
import org.opennars.operator.mental.Wonder;

public class ProcessJudgment {
    /**
     * To accept a new judgment as belief, and check for revisions and solutions.
     * Revisions will be processed as judgment tasks by themselves.
     * Due to their higher confidence, summarizing more evidence,
     * the will become the top entries in the belief table.
     * Additionally, judgements can themselves be the solution to existing questions
     * and goals, which is also processed here.
     * 
     * @param task    The judgment task to be accepted
     * @param concept The concept of the judment task
     * @param nal     The derivation context
     */
    public static void processJudgment(final Concept concept, final DerivationContext nal, final Task task) {
        InternalExperienceBuffer.handleOperationFeedback(task, nal);
        final Sentence judg = task.sentence;
        ProcessAnticipation.confirmAnticipation(task, concept, nal);
        final Task oldBeliefT = concept.selectCandidate(task, concept.beliefs, nal.time); // only revise with the
                                                                                          // strongest -- how about
                                                                                          // projection?
        Sentence oldBelief = null;
        if (oldBeliefT != null) {
            oldBelief = oldBeliefT.sentence;
            final Stamp newStamp = judg.stamp;
            final Stamp oldStamp = oldBelief.stamp; // when table is full, the latter check is especially important too
            if (newStamp.equals(oldStamp, false, false, true)) {
                concept.memory.removeTask(task, "Duplicated");
                return;
            } else if (revisible(judg, oldBelief, nal.narParameters)) {
                nal.setTheNewStamp(newStamp, oldStamp, nal.time.time());
                final Sentence projectedBelief = oldBelief.projection(nal.time.time(), newStamp.getOccurrenceTime(),
                        concept.memory);
                if (projectedBelief != null) {
                    nal.setCurrentBelief(projectedBelief);
                    revision(judg, projectedBelief, concept, false, nal);
                    task.setAchievement(calcTaskAchievement(task.sentence.truth, projectedBelief.truth));
                }
            }
        }
        if (!task.aboveThreshold()) {
            return;
        }
        final int nnq = concept.questions.size();
        for (int i = 0; i < nnq; i++) {
            trySolution(judg, concept.questions.get(i), nal, true);
        }
        final int nng = concept.desires.size();
        for (int i = 0; i < nng; i++) {
            trySolution(judg, concept.desires.get(i), nal, true);
        }
        concept.addToTable(task, false, concept.beliefs, concept.memory.narParameters.CONCEPT_BELIEFS_MAX,
                Events.ConceptBeliefAdd.class, Events.ConceptBeliefRemove.class);
    }

    /**
     * Check whether the task is an executable hypothesis of the form
     * &lt;(&amp;/,a,op()) =/&gt; b&gt;.
     * 
     * @param task The judgement task be checked
     * @param nal  The derivation context
     * @return Whether task is an executable precondition
     */
    protected static boolean isExecutableHypothesis(Task task, final DerivationContext nal) {
        final Term term = task.getTerm();
        if (!task.sentence.isEternal() ||
                !(term instanceof Implication)) {
            return false;
        }
        final Implication imp = (Implication) term;
        if (imp.getTemporalOrder() != TemporalRules.ORDER_FORWARD) {
            return false;
        }
        // also it has to be enactable, meaning the last entry of the sequence before
        // the interval is an operation:
        final Term subj = imp.getSubject();
        if (!(subj instanceof Conjunction)) {
            return false;
        }
        final Conjunction conj = (Conjunction) subj;
        boolean isInExecutableFormat = !conj.isSpatial &&
                conj.getTemporalOrder() == TemporalRules.ORDER_FORWARD &&
                conj.term.length >= 4 && conj.term.length % 2 == 0 &&
                conj.term[conj.term.length - 1] instanceof Interval &&
                conj.term[conj.term.length - 2] instanceof Operation;
        return isInExecutableFormat;
    }

    /**
     * Add &lt;(&amp;/,a,op()) =/&gt; b&gt; beliefs to preconditions in concept b
     * 
     * @param task The potential implication task
     * @param nal  The derivation context
     */
    protected static void addToTargetConceptsPreconditions(final Task task, final DerivationContext nal) {
        Set<Term> targets = new LinkedHashSet<>();
        // add to all components, unless it doesn't have vars
        if (!((Implication) task.getTerm()).getPredicate().hasVar()) {
            targets.add(((Implication) task.getTerm()).getPredicate());
        } else {
            Map<Term, Integer> ret = ((Implication) task.getTerm()).getPredicate().countTermRecursively(null);
            for (Term r : ret.keySet()) {
                targets.add(r);
            }
        }
        // the concept of the implication task
        Concept origin_concept = nal.memory.concept(task.getTerm());
        if (origin_concept == null) {
            return;
        }
        // get the first eternal. the highest confident one (due to the sorted order):
        Optional<Task> strongest_target = null;
        synchronized (origin_concept) {
            strongest_target = tryFind(origin_concept.beliefs, iTask -> iTask.sentence.isEternal());
        }
        if (!strongest_target.isPresent()) {
            return;
        }
        final Term[] prec = ((Conjunction) ((Implication) strongest_target.get().getTerm()).getSubject()).term;
        for (int i = 0; i < prec.length - 2; i++) {
            if (prec[i] instanceof Operation) { // don't react to precondition with an operation before the last
                return; // for now, these can be decomposed into smaller such statements anyway
            }
        }
        for (Term t : targets) { // the target sub concepts it needs to go to
            final Concept target_concept = nal.memory.concept(t);
            if (target_concept == null) { // target concept does not exist
                continue;
            }
            // we do not add the target, instead the strongest belief in the target concept
            synchronized (target_concept) {
                List<Task> table = strongest_target.get().sentence.term.hasVar()
                        ? target_concept.general_executable_preconditions
                        : target_concept.executable_preconditions;
                // at first we have to remove the last one with same content from table
                int i_delete = -1;
                for (int i = 0; i < table.size(); i++) {
                    if (CompoundTerm.replaceIntervals(table.get(i).getTerm()).equals(
                            CompoundTerm.replaceIntervals(strongest_target.get().getTerm()))) {
                        i_delete = i; // even these with same term but different intervals are removed here
                        break;
                    }
                }
                if (i_delete != -1) {
                    table.remove(i_delete);
                }
                // this way the strongest confident result of this content is put into table but
                // the table ranked according to truth expectation
                target_concept.addToTable(strongest_target.get(), true, table,
                        target_concept.memory.narParameters.CONCEPT_BELIEFS_MAX, Events.EnactableExplainationAdd.class,
                        Events.EnactableExplainationRemove.class);
            }
        }
    }
}
