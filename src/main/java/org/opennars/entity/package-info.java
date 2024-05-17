
/**
 * Data entities that are independently stored
 *
 * <h2>Package Specification</h2>
 *
 * <ul>
 * <li>ShortFloats: BudgetValue (priority/durability/quality) and TruthValue
 * (frequency/confidence)</li>
 * <li>Stamp: serial numbers and creation time associated to a TruthValue</li>
 * <li>Sentence: a Term, a TruthValue, and a Stamp. A Sentence can be a
 * Judgment, a Goal, or a Question.</li>
 * <li>Task: a Sentence to be processed.</li>
 * <li>TermLink: built in pair between a compound term and a component
 * term.</li>
 * <li>TaskLink: special TermLink refering to a Task, whose Term equals or
 * contains the current Term.</li>
 * <li>Concept: labeled by a Term, contains a TaskLink bag and a TermLink bag
 * for indirect tasks/beliefs, as well as beliefs/questions/goals directly on
 * the Term.</li>
 * <li>Item: Concept, Task, or TermLink</li>
 * </ul>
 *
 * <p>
 * in NARS, each task is processed in two stages:
 * </p>
 *
 * <ol>
 * <li>Direct processing by matching, in the concept corresponding to the
 * content, in one step. It happens when the task is inserted into memory.</li>
 * <li>Indirect processing by reasoning, in related concepts and unlimited
 * steps. It happens in each inference cycle.</li>
 * </ol>
 */
package org.opennars.entity;
