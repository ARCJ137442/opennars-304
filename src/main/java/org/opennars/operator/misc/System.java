package org.opennars.operator.misc;

import org.opennars.language.Term;
import org.opennars.operator.FunctionOperator;
import org.opennars.storage.Memory;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Count the number of elements in a set
 */
public class System extends FunctionOperator {

    public System() {
        super("^system");
    }

    @Override
    protected Term function(final Memory memory, final Term[] x) {
        String cmd = "";
        for (int i = 0; i < x.length; ++i) {
            cmd += x[i].name().toString() + " ";
        }
        String s;
        String ret = "";
        String[] cmds = { "bash", "-c", cmd };
        Runtime r;
        Process p;
        try {
            r = Runtime.getRuntime();
            p = r.exec(cmds);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                ret += s;
            // System.out.println("line: " + s);
            p.waitFor();
            // System.out.println ("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {
        }
        return new Term(ret);
    }

    @Override
    protected Term getRange() {
        return Term.get("system_called");
    }

}
