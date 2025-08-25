package org.opennars.language;

import java.util.LinkedHashMap;
import java.util.Map;

public enum Tense {

    Past(":\\:"),
    Present(":|:"),
    Future(":/:");

    public final String symbol;

    public static final Tense Eternal = null;

    Tense(final String string) {
        this.symbol = string;
    }

    @Override
    public String toString() {
        return symbol;
    }

    protected static final Map<String, Tense> stringToTense = new LinkedHashMap<>(Tense.values().length * 2);

    static {
        for (final Tense t : Tense.values()) {
            stringToTense.put(t.toString(), t);
        }
    }

    public static Tense tense(final String s) {
        return stringToTense.get(s);
    }

}
