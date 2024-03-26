/* 
 * The MIT License
 *
 * Copyright 2018 The OpenNARS authors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
/**
 * Term hierarchy in Narsese
 *
 * Open-NARS implements the following formal language, Narsese.
 * 
 * <pre>
 *            <sentence> ::= <judgment>
 *                         | <question>
 *            <judgment> ::= <statement> <truth-value>
 *            <question> ::= <statement>
 *           <statement> ::= <<term> <relation> <term>>
 *                         | <compound-statement>
 *                         | <term>
 *                <term> ::= <word>
 *                         | <variable>
 *                         | <compound-term>
 *                         | <statement>
 *            <relation> ::= -->    // Inheritance
 *                         | <->    // Similarity
 *                         | {--    // Instance
 *                         | --]    // Property
 *                         | {-]    // InstanceProperty
 *                         | ==>    // Implication
 *                         | <=>    // Equivalence
 *  <compound-statement> ::= (-- <statement>)                 // Negation
 *                         | (|| <statement> <statement>+)    // Disjunction
 *                         | (&& <statement> <statement>+)    // Conjunction
 *       <compound-term> ::= {<term>+}    // SetExt
 *                         | [<term>+]    // SetInt
 *                         | (& <term> <term>+)    // IntersectionExt
 *                         | (| <term> <term>+)    // IntersectionInt
 *                         | (- <term> <term>)     // DifferenceExt
 *                         | (~ <term> <term>)     // DifferenceInt
 *                         | (* <term> <term>+)    // Product
 *                         | (/ <term>+ _ <term>*)    // ImageExt
 *                         | (\ <term>+ _ <term>*)    // ImageInt
 *            <variable> ::= <independent-var>
 *                         | <dependent-var>
 *                         | <query-var>
 *     <independent-var> ::= $[<word>]
 *       <dependent-var> ::= #<word>
 *           <query-var> ::= ?[<word>]
 *                <word> : string in an alphabet
 *         <truth-value> : a pair of real numbers in [0, 1] x (0, 1)
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
