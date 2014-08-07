/*
 * CompoundTerm.java
 *
 * Copyright (C) 2008  Pei Wang
 *
 * This file is part of Open-NARS.
 *
 * Open-NARS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Open-NARS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Open-NARS.  If not, see <http://www.gnu.org/licenses/>.
 */
package nars.language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import nars.entity.TermLink;
import nars.inference.TemporalRules;
import nars.io.Symbols;
import nars.io.Symbols.NativeOperator;
import static nars.io.Symbols.NativeOperator.COMPOUND_TERM_CLOSER;
import static nars.io.Symbols.NativeOperator.COMPOUND_TERM_OPENER;
import static nars.language.CompoundTerm.makeCompoundName;


public abstract class CompoundTerm extends Term {
    
    @Deprecated private static final boolean allowNonDeepCopy = false; //temporary, disables eliminating deep copy. one reason why deepcopy is necessary is to eliminate cyclic clones 
    
    /**
     * list of component terms
     */
    public final Term[] term;
    public final int temporalOrder;
    
    /**
     * syntactic complexity of the compound, the sum of those of its term plus 1
     */
    transient private short complexity;

    transient private String name = null; //cached name string, lazily calculated
    
    /** Whether the term names a concept */
    transient private boolean isConstant;
    
    /** Whether contains a variable */
    transient private boolean hasVar;
    transient private int hash;
    

    /** clone constructor where the individual components are specified. */
    protected CompoundTerm(final Term[] components, int torder, final boolean isConstant, final boolean hasVar, final short complexity, int hash) {        
        super();
        
        assert(validSize(components.length));
        
        this.term = components;
        this.temporalOrder = torder;
        this.complexity = complexity;
        this.hasVar = hasVar;
        this.isConstant = isConstant;
        this.hash = hash;
    }

    /** clone constructor; requires the origin to be of same class */
    protected CompoundTerm(CompoundTerm other) {
        this(other.term, other.temporalOrder, other.isConstant, other.hasVar, other.complexity, other.hash);
        
        assert(getClass() == other.getClass());
    }
    
    /** constructor that specifies only components; cached fields will be computed */
    protected CompoundTerm(final Term[] components, int temporalOrder) {        
        super();
        
        this.term = components;
        this.temporalOrder = temporalOrder;
        changed();
    }
    
    protected CompoundTerm(final Term[] components) {        
        this(components, TemporalRules.ORDER_NONE);
    }

 
    /**
     * @param num number of component terms
     * @return whether the given number of components is valid for this CompoundTerm implementation
     */
    abstract public boolean validSize(int num);

    /**
     * Abstract method to get the operator of the compound
     */
    abstract public NativeOperator operator();


    /**
     * The complexity of the term is the sum of those of the term plus 1
     */
    private short calcComplexity() {
        int c = 1;
        for (final Term t : term) {
            c += t.getComplexity();        
        }
        return (short)c;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    
    
    @Override
    public boolean equals(final Object that) {
        if (that == this) return true;
        
        if (!(that.getClass() == getClass()))
            return false;
        
        final CompoundTerm t = (CompoundTerm)that;
        
        if (hash != t.hash)
            return false;

        /*
        //redundant, this is already checked in the class comparison
        if (operator() != t.operator())
            return false;
        */
        
        //these are redundant, but may provide an early termination
        if (getComplexity() != t.getComplexity())
            return false;
        
        if (size() != t.size())
            return false;
        
        if (temporalOrder!= t.temporalOrder)
            return false;
        
        for (int i = 0; i < term.length; i++) {
            final Term x = term[i];
            final Term y = t.term[i];
            if (!x.equals(y))
                return false;
        }
        
        return true;
    }


    /**
     * Orders among terms: variable < atomic < compound
     *
     * @param that The Term to be compared with the current Term
     * @return The order of the two terms
     */
    @Override
    public int compareTo(final Term that) {
        
        if (this == that)
            return 0;
        
        if (getClass()!=that.getClass())
            return getClass().getSimpleName().compareTo(that.getClass().getSimpleName());
                    
        
        final CompoundTerm thatC = (CompoundTerm) that;

        int opDiff = operator().ordinal() - thatC.operator().ordinal(); //should be faster faster than Enum.compareTo   
        if (opDiff != 0)
            return opDiff;
        
        if (term.length != thatC.term.length) 
            return term.length - thatC.term.length;

        int tempDiff = temporalOrder - thatC.temporalOrder;
        if (tempDiff != 0)
            return tempDiff;

        for (int i = 0; i < term.length; i++) {
            final int diff = term[i].compareTo(thatC.term[i]);
            if (diff != 0)
                return diff;
        }
        
        return 0; //equal
    }



    /**
     * build a component list from terms
     * @return the component list
     */
    protected static Term[] termArray(final Term... t) {
        return t;
    }
    
    protected static List<Term> termList(final Term... t) {
        return Arrays.asList(t);
    }
    
    protected String makeName() {
        return makeCompoundName(operator(), term);
    }
    
    public String toString() {
        if (name!=null) return name;
        name = makeName();
        return name;
    }

    /**
     * default method to make the oldName of a compound term from given fields
     *
     * @param op the term operator
     * @param arg the list of term
     * @return the oldName of the term
     */
    protected static String makeCompoundName(final NativeOperator op, final Term[] arg) {
        final int sizeEstimate = 12 * arg.length;
        
        final StringBuilder nameBuilder = new StringBuilder(sizeEstimate)
            .append(COMPOUND_TERM_OPENER.ch).append(op.toString());
            
        for (final Term t : arg) {
            nameBuilder.append(Symbols.ARGUMENT_SEPARATOR);
            nameBuilder.append(t.toString());
        }
        nameBuilder.append(COMPOUND_TERM_CLOSER.ch);
                
        return nameBuilder.toString();
    }

    /**
     * make the oldName of an ExtensionSet or IntensionSet
     *
     * @param opener the set opener
     * @param closer the set closer
     * @param arg the list of term
     * @return the oldName of the term
     */
    protected static String makeSetName(final char opener, final Term[] arg, final char closer) {
        final int sizeEstimate = 12 * arg.length + 2;
        
        StringBuilder name = new StringBuilder(sizeEstimate)
            .append(opener);

        if (arg.length == 0) { 
            //is empty arg valid?            
            //throw new RuntimeException("Empty arg list for makeSetName");            
        }
        else {
        
            name.append(arg[0].toString());

            for (int i = 1; i < arg.length; i++) {
                name.append(Symbols.ARGUMENT_SEPARATOR);
                name.append(arg[i].toString());
            }
        }
        
        name.append(closer);
        return name.toString();
    }

    /**
     * default method to make the oldName of an image term from given fields
     *
     * @param op the term operator
     * @param arg the list of term
     * @param relationIndex the location of the place holder
     * @return the oldName of the term
     */
    protected static String makeImageName(final NativeOperator op, final Term[] arg, final int relationIndex) {
        final int sizeEstimate = 12 * arg.length + 2;
        
        StringBuilder name = new StringBuilder(sizeEstimate)
            .append(COMPOUND_TERM_OPENER.ch)
            .append(op)
            .append(Symbols.ARGUMENT_SEPARATOR)
            .append(arg[relationIndex].toString());
        
        for (int i = 0; i < arg.length; i++) {
            name.append(Symbols.ARGUMENT_SEPARATOR);
            if (i == relationIndex) {
                name.append(Symbols.IMAGE_PLACE_HOLDER);
            } else {
                name.append(arg[i].toString());
            }
        }
        name.append(COMPOUND_TERM_CLOSER.ch);
        return name.toString();
    }

    /* ----- utilities for other fields ----- */
    /**
     * report the term's syntactic complexity
     *
     * @return the complexity value
     */    
    @Override public short getComplexity() {
        return complexity;
    }

    /**
     * check if the term contains free variable
     *
     * @return if the term is a constant
     */
    @Override
    public boolean isConstant() {
        return isConstant;
    }

    /**
     * Check if the order of the term matters
     * <p>
     * commutative CompoundTerms: Sets, Intersections Commutative Statements:
     * Similarity, Equivalence (except the one with a temporal order)
     * Commutative CompoundStatements: Disjunction, Conjunction (except the one
     * with a temporal order)
     *
     * @return The default value is false
     */
    public boolean isCommutative() {
        return false;
    }

    /* ----- extend Collection methods to component list ----- */
    /**
     * get the number of term
     *
     * @return the size of the component list
     */
    public int size() {
        return term.length;
    }


    /** Gives a set of all contained term, recursively */
    public Set<Term> getContainedTerms() {
        Set<Term> s = new HashSet();
        for (Term t : term) {
            s.add(t);
            if (t instanceof CompoundTerm)
                s.addAll( ((CompoundTerm)t).getContainedTerms() );
        }
        return s;
    }

    @Override
    abstract public CompoundTerm clone();
    
    /**
     * Clone the component list
     *
     * @return The cloned component list
     */
    public Term[] cloneTerms(boolean deep, Term... additional) {
        return cloneTermsAppend(deep, term, additional);
    }
    
    public Term[] cloneTerms(Term... additional) {
        return cloneTerms(true, additional);
    }
    
    public Term[] cloneTermsExcept(boolean requireModification, final Term... toRemove) {
        return cloneTermsExcept(true, requireModification, toRemove);
    }

    /**
     * Cloned array of Terms, except for one or more Terms.
     * @param toRemove
     * @return the cloned array with the missing terms removed, OR null if no terms were actually removed when requireModification=true
     */
    private Term[] cloneTermsExcept(boolean deep, boolean requireModification, final Term... toRemove) {
        //TODO if deep, this wastes created clones that are then removed.  correct this inefficiency?
        
        List<Term> l = cloneTermsList(deep);
        boolean removed = false;
                
        for (final Term t : toRemove) {
            if (l.remove(t))
                removed = true;
        }
        if ((!removed) && (requireModification))
            return null;
                
        return l.toArray(new Term[l.size()]);
    }
    

    public static Term[] cloneTermsAppend(final Term[] original, Term... additional) {
        return cloneTermsAppend(true, original, additional);
    }
    
    /**
     * Deep clone an array list of terms
     *
     * @param original The original component list
     * @return an identical and separate copy of the list
     */
    public static Term[] cloneTermsAppend(boolean deep, final Term[] original, Term... additional) {
        if (original == null) {
            return null;
        }        

        final Term[] arr = new Term[original.length + additional.length];
        
        int i;
        for (i = 0; i < original.length; i++) {            
            final Term t = original[i];      
            
            //experiental optimization
            if (!t.containsVar() && allowNonDeepCopy)
                arr[i] = t;
            else
                arr[i] = ( deep ? t.clone() : t );

            //arr[i] = ( deep ? (Term)t.clone() : t );
        
        }
        for (int j = 0; j < additional.length; j++) {
            final Term t = additional[j];                    
            
            //experiental optimization
            if (!t.containsVar() && allowNonDeepCopy)
                arr[i+j] = t;
            else            
                arr[i+j] = ( deep ? t.clone() : t );
            
            //arr[i+j] = ( deep ? (Term)t.clone() : t );
        }
        return arr;
        
    }
    
    public ArrayList<Term> cloneTermsList() {
        return cloneTermsList(true);
    }

    public ArrayList<Term> cloneTermsList(boolean deep) {
        ArrayList<Term> l = new ArrayList(term.length);
        for (final Term t : term) {
             //experiental optimization
            if (!t.containsVar() && allowNonDeepCopy)
                l.add(t);
            else
                l.add( deep ? t.clone() : t );  
            
            //l.add( deep ? (Term)t.clone() : t );
        }
        return l;        
    }
  
    /** forced deep clone of terms */
    public ArrayList<Term> cloneTermsListDeep() {
        ArrayList<Term> l = new ArrayList(term.length);
        for (final Term t : term)
            l.add(t.clone());
        return l;        
    }

    
    //TODO move this to a utility method
    public static <T> boolean contains(final T[] array, final T v) {
        /*if (v == null) {
            for (final T e : array)
                if (e == null)
                    return true;
        } else {*/
        
        for (final T e : array)
            if (v.equals(e))
                return true;

        return false;
    }

    
    static void shuffle(Term[] list, Random randomNumber) {
        if (list.length < 2)  {
            return;
        }
        
        int n = list.length;
        for (int i = 0; i < n; i++) {
            // between i and n-1
            int r = i + (int) (randomNumber.nextDouble() * (n-i));
            Term tmp = list[i];    // swap
            list[i] = list[r];
            list[r] = tmp;
        }
    }
    
    //TODO move this to a utility method
    public static <T> int indexOf(final T[] array, final T v) {
        /*if (v == null) {
            for (final T e : array)
                if (e == null)
                    return true;
        } else {*/
        
        int i = 0;
        for (final T e : array) {
            if (v.equals(e)) {
                return i;
            }
            i++;
        }

        return -1;
    }
    
    //TODO move this to a utility method
    public static <T> boolean containsAll(final T[] array, final T v) {
        for (final T e : array)
            if (!v.equals(e))
                return false;

        return true;
    }
    

    /**
     * Check whether the compound contains a certain component
     * Also matches variables, ex: (&&,<a --> b>,<b --> c>) also contains <a --> #1>
     * @param t The component to be checked
     * @return Whether the component is in the compound
     */
    @Override
    public boolean containsTerm(final Term t) {        
        return contains(term, t);
    }

    /**
     * Recursively check if a compound contains a term
     *
     * @param target The term to be searched
     * @return Whether the target is in the current term
     */
    @Override
    public boolean containsTermRecursively(final Term target) {
        for (final Term term : term) {
            if (term.containsTermRecursively(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the compound contains all term of another term, or
 that term as a whole
     *
     * @param t The other term
     * @return Whether the term are all in the compound
     */
    public boolean containsAllTermsOf(final Term t) {
        if (getClass() == t.getClass()) { //(t instanceof CompoundTerm) {
            return containsAll(term, ((CompoundTerm) t).term );
        } else {
            return contains(term, t);
        }
    }

    /**
     * Try to add a component into a compound
     *
     * @param t1 The compound
     * @param t2 The component
     * @param memory Reference to the memory
     * @return The new compound
     */
    public static Term addComponents(final CompoundTerm t1, final Term t2) {
        if (t2 == null)
            return t1;
        
        boolean success;
        Term[] terms;
        if (t2 instanceof CompoundTerm) {
            terms = t1.cloneTerms(((CompoundTerm) t2).term);
        } else {
            terms = t1.cloneTerms(t2);
        }
        return Terms.make(t1, terms);
    }



    /**
     * Try to replace a component in a compound at a given index by another one
     *
     * @param compound The compound
     * @param index The location of replacement
     * @param t The new component
     * @param memory Reference to the memory
     * @return The new compound
     */
    public static Term setComponent(final CompoundTerm compound, final int index, final Term t) {

        List<Term> list = compound.cloneTermsListDeep();
        
        //if (compound.getClass() != t.getClass()) {
        if (t instanceof CompoundTerm) {
            list.set(index, t);
        } else {
            list.remove(index);
            final List<Term> list2 = ((CompoundTerm) t).cloneTermsList();
            for (int i = 0; i < list2.size(); i++) {
                list.add(index + i, list2.get(i));
            }
        }
        
        return Terms.make(compound, list);
    }

    /* ----- variable-related utilities ----- */
    /**
     * Whether this compound term contains any variable term
     *
     * @return Whether the name contains a variable
     */
    @Override
    public boolean containsVar() {
        return hasVar;
    }

    /**
     * Rename the variables in the compound, called from Sentence constructors
     */
    @Override
    public void renameVariables() {
        if (containsVar()) {
            //int existingComponents = term.length;
            renameVariables(new HashMap<Variable, Variable>());
        }
        //isConstant = true;        
        changed();
    }

    /**
     * Recursively rename the variables in the compound
     *
     * @param map The substitution established so far
     */
    private void renameVariables(final HashMap<Variable, Variable> map) {
        if (containsVar()) {
            for (int i = 0; i < term.length; i++) {
                final Term term = this.term[i];
                if (term instanceof Variable) {
                    Variable var;                    
                    if (term.toString().length() == 1) { // anonymous variable from input
                        var = new Variable(term.toString().charAt(0) + String.valueOf(map.size() + 1));
                    } else {
                        var = map.get(term);
                        if (var == null) {
                            var = new Variable(term.toString().charAt(0) + String.valueOf(map.size() + 1));
                        }
                    }
                    if (!term.equals(var)) {
                        this.term[i] = var;
                    }
                    map.put((Variable) term, var);
                } else if (term instanceof CompoundTerm) {
                    CompoundTerm ct = (CompoundTerm)term;
                    ct.renameVariables(map);
                    ct.changed();
                }
            }
        }
    }

    /**
     * Recursively apply a substitute to the current CompoundTerm
     *
     * @param subs
     */
    public void applySubstitute(final HashMap<Term, Term> subs) {                
        for (int i = 0; i < term.length; i++) {
            Term t1 = term[i];
            if (subs.containsKey(t1)) {
                Term t2 = subs.get(t1);                            
                while (subs.containsKey(t2)) {
                    t2 = subs.get(t2);
                }
                //prevents infinite recursion
                if (!t2.containsTerm(t1))
                    term[i] = t2.clone();
            } else if (t1 instanceof CompoundTerm) {
                ((CompoundTerm) t1).applySubstitute(subs);
            }            
        }
        if (this.isCommutative()) {         
            Arrays.sort(term);
        }
        changed();
    }

    /* ----- link CompoundTerm and its term ----- */
    /**
     * Build TermLink templates to constant term and subcomponents
     * <p>
     * The compound type determines the link type; the component type determines
     * whether to build the link.
     *
     * @return A list of TermLink templates
     */
    public List<TermLink> prepareComponentLinks() {
        //complexity seems like an upper bound for the resulting number of componentLinks. 
        //so use it as an initial size for the array list
        final List<TermLink> componentLinks = new ArrayList<>( getComplexity() );              
        return Terms.prepareComponentLinks(componentLinks, this);
    }

    protected int calcHash() {
        return Objects.hash(operator(), term, temporalOrder);
    }
    
    private void changed() {
        this.name = null; //invalidate, recomputed on next toString()
        this.hash = calcHash();        
        this.complexity = calcComplexity();
        this.hasVar = Variables.containVar(term);
        this.isConstant = !hasVar;
    }

}
