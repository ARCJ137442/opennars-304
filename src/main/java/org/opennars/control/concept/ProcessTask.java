package org.opennars.control.concept;

import org.opennars.control.DerivationContext;
import org.opennars.entity.*;
import org.opennars.interfaces.Timable;
import org.opennars.io.Symbols;

/**
 * Encapsulates the dispatching task processing
 *
 * @author Patrick Hammer
 *
 */
public class ProcessTask {
    /**
     * Directly process a new task within a concept.Here task can either be a
     * judgement, goal, question or quest.The function is called exactly once on
     * each task.Using
     * local information and finishing in a constant time.
     * Also providing feedback
     * in the budget value of the task:
     * de-priorize already fullfilled questions and goals
     * increase quality of beliefs if they turned out to be useful.
     * After the re-priorization is done, a tasklink is finally constructed.
     * For input events the concept is set observable too.
     *
     * @param concept The concept of the task
     * @param nal     The derivation context
     * @param task    The task to be processed
     * @param time    The time
     * @return whether it was processed
     */
    // called in Memory.localInference only, for both derived and input tasks
    public static boolean processTask(final Concept concept, final DerivationContext nal, final Task task,
            Timable time) {
        synchronized (concept) {
            concept.observable |= task.isInput();
            final char type = task.sentence.punctuation;
            switch (type) {
                case Symbols.JUDGMENT_MARK:
                    ProcessJudgment.processJudgment(concept, nal, task);
                    break;
                case Symbols.GOAL_MARK:
                    ProcessGoal.processGoal(concept, nal, task);
                    break;
                case Symbols.QUESTION_MARK:
                case Symbols.QUEST_MARK:
                    ProcessQuestion.processQuestion(concept, nal, task);
                    break;
                default:
                    return false;
            }
            // reduce priority by achievement:
            task.setPriority((float) (task.getPriority() * task.getAchievement()));
            // now process
            if (task.aboveThreshold()) { // still need to be processed
                TaskLink taskl = concept.linkToTask(task, nal);
                if (task.sentence.isJudgment() && ProcessJudgment.isExecutableHypothesis(task, nal)) { // after
                                                                                                       // linkToTask
                    ProcessJudgment.addToTargetConceptsPreconditions(task, nal); // because now the components are there
                }
                ProcessAnticipation.firePredictions(task, concept, nal, time, taskl);
            }
        }
        return true;
    }
}
