package org.opennars.interfaces;

import org.opennars.entity.Task;

/**
 * Implementation can consume tasks
 *
 * R is result type
 *
 * @author Robert Wünsche
 */
public interface TaskConsumer<R> {
    /**
     * consumes a task
     * 
     * @param task task to be consumed
     * @param time used to retrieve the time
     * @return something which consumed the task or which was assigned the task
     */
    R addInput(final Task task, final Timable time);
}
