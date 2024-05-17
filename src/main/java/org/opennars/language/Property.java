package org.opennars.language;

/**
 * A Statement about a Property relation, which is used only in Narsese for I/O,
 * and translated into Inheritance for internal use.
 *
 * @author Patrick Hammer
 */
public abstract class Property /* would extend "Statement" if it were its own type */ {

    /**
     * Try to make a new compound from two components. Called by the inference
     * rules.
     * <p>
     * A --] B becomes A --&gt; [B]
     * 
     * @param subject   The first component
     * @param predicate The second component
     * @return A compound generated or null
     */
    final public static Inheritance make(final Term subject, final Term predicate) {
        return Inheritance.make(subject, new SetInt(predicate));
    }
}
