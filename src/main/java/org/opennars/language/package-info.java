
/**
 * Term hierarchy in Narsese
 *
 * Open-NARS implements the following formal language, Narsese.
 * 
 * <pre>
 *            &lt;sentence&gt; ::= &lt;judgment&gt;
 *                         | &lt;question&gt;
 *            &lt;judgment&gt; ::= &lt;statement&gt; &lt;truth-value&gt;
 *            &lt;question&gt; ::= &lt;statement&gt;
 *           &lt;statement&gt; ::= &lt;&lt;term&gt; &lt;relation&gt; &lt;term&gt;&gt;
 *                         | &lt;compound-statement&gt;
 *                         | &lt;term&gt;
 *                &lt;term&gt; ::= &lt;word&gt;
 *                         | &lt;variable&gt;
 *                         | &lt;compound-term&gt;
 *                         | &lt;statement&gt;
 *            &lt;relation&gt; ::= --&gt;    // Inheritance
 *                         | &lt;-&gt;    // Similarity
 *                         | {--    // Instance
 *                         | --]    // Property
 *                         | {-]    // InstanceProperty
 *                         | ==&gt;    // Implication
 *                         | &lt;=&gt;    // Equivalence
 *  &lt;compound-statement&gt; ::= (-- &lt;statement&gt;)                 // Negation
 *                         | (|| &lt;statement&gt; &lt;statement&gt;+)    // Disjunction
 *                         | (&amp;&amp; &lt;statement&gt; &lt;statement&gt;+)    // Conjunction
 *       &lt;compound-term&gt; ::= {&lt;term&gt;+}    // SetExt
 *                         | [&lt;term&gt;+]    // SetInt
 *                         | (&amp; &lt;term&gt; &lt;term&gt;+)    // IntersectionExt
 *                         | (| &lt;term&gt; &lt;term&gt;+)    // IntersectionInt
 *                         | (- &lt;term&gt; &lt;term&gt;)     // DifferenceExt
 *                         | (~ &lt;term&gt; &lt;term&gt;)     // DifferenceInt
 *                         | (* &lt;term&gt; &lt;term&gt;+)    // Product
 *                         | (/ &lt;term&gt;+ _ &lt;term&gt;*)    // ImageExt
 *                         | (\ &lt;term&gt;+ _ &lt;term&gt;*)    // ImageInt
 *            &lt;variable&gt; ::= &lt;independent-var&gt;
 *                         | &lt;dependent-var&gt;
 *                         | &lt;query-var&gt;
 *     &lt;independent-var&gt; ::= $[&lt;word&gt;]
 *       &lt;dependent-var&gt; ::= #&lt;word&gt;
 *           &lt;query-var&gt; ::= ?[&lt;word&gt;]
 *                &lt;word&gt; : string in an alphabet
 *         &lt;truth-value&gt; : a pair of real numbers in [0, 1] x (0, 1)
 * </pre>
 *
 * <p>
 * Major methods in the Term classes:
 * </p>
 *
 * <ul>
 * <li>constructors</li>
 * <li>get and set</li>
 * <li>clone, compare, and unify</li>
 * <li>create and access corresponding concept</li>
 * <li>structural operation in compound</li>
 * </ul>
 */
package org.opennars.language;
