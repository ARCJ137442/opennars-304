package org.opennars.operator;

import org.opennars.entity.TruthValue;
import org.opennars.language.Conjunction;

/**
 * ImaginationSpace: A group of operations that when executed in a certain
 * sequence
 * add up to a certain declarative "picture". This "picture" might be different
 * than the feedback of
 * the last operation in the sequence: for instance in case of eye movements,
 * each movement
 * feedback corresponds to a sampling result, where each adds its part of
 * information.
 * 
 * @author Patrick
 */
public interface ImaginationSpace {
    //
    TruthValue AbductionOrComparisonTo(final ImaginationSpace obj, boolean comparison);

    // attaches an imagination space to the conjunction that is constructed
    // by starting with the leftmost element of the conjunction
    // and then gradually moving to the right
    ImaginationSpace ConstructSpace(Conjunction program);

    // Has to return a new instance, not changing "this"!
    ImaginationSpace ProgressSpace(Operation op, ImaginationSpace B);

    // Check whether the operation is part of the space:
    boolean IsOperationInSpace(Operation oper);
}
