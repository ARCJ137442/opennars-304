package org.opennars.perf;

import java.text.DecimalFormat;

public abstract class Performance {
    public final int repeats;
    final String name;
    private long totalTime;
    private long totalMemory;
    protected final DecimalFormat df = new DecimalFormat("#.###");

    public Performance(final String name, final int repeats, final int warmups) {
        this(name, repeats, warmups, true);
    }

    public Performance(final String name, final int repeats, int warmups, final boolean gc) {
        this.repeats = repeats;
        this.name = name;

        init();

        totalTime = 0;
        totalMemory = 0;

        final int total = repeats + warmups;
        for (int r = 0; r < total; r++) {

            if (gc) {
                System.gc();
            }

            final long usedMemStart = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());

            final long start = System.nanoTime();

            run(warmups != 0);

            if (warmups == 0) {
                totalTime += System.nanoTime() - start;
                totalMemory += (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) - usedMemStart;
            } else
                warmups--;
        }
    }

    public Performance print() {
        System.out.print(": " + df.format(getCycleTimeMS()) + "ms/run, ");
        System.out.print(df.format(totalMemory / repeats / 1024.0) + " kb/run");
        return this;
    }

    public Performance printCSV(final boolean finalComma) {
        System.out.print(name + ", " + df.format(getCycleTimeMS()) + ", ");
        System.out.print(df.format(totalMemory / repeats / 1024.0));
        if (finalComma)
            System.out.print(",");
        return this;
    }

    abstract public void init();

    abstract public void run(boolean warmup);

    public double getCycleTimeMS() {
        return totalTime / repeats / 1000000.0;
    }
}
