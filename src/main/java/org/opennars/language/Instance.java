package org.opennars.language;

/**
 * A Statement about an Instance relation, which is used only in Narsese for
 * I/O,
 * and translated into Inheritance for internal use.
 *
 * @author Patrick Hammer
 */
public abstract class Instance /* extends Statement */ {

    /**
     * Try to make a new compound from two components. Called by the inference
     * rules.
     * <p>
     * A {-- B becomes {A} --> B
     * 
     * @param subject   The first component
     * @param predicate The second component
     * @return A compound generated or null
     */
    public static Inheritance make(final Term subject, final Term predicate) {
        return Inheritance.make(new SetExt(subject), predicate);
    }
}
