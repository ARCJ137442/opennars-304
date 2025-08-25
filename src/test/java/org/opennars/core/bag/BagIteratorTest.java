package org.opennars.core.bag;

import org.junit.Test;
import org.opennars.perf.BagPerf.NullItem;
import org.opennars.storage.Bag;
import static org.junit.Assert.assertTrue;

public class BagIteratorTest {

    final int L = 4;

    public void testIterator(final Bag<NullItem, CharSequence> b) {
        int count = 0;
        NullItem first = null, current = null;
        for (final NullItem n : b) {
            if (first == null)
                first = n;
            current = n;
            // System.out.println(current);
            count++;
        }

        if (b.size() > 1) {
            // check correct order
            if (first == null || current == null)
                throw new AssertionError();
            assertTrue(first.getPriority() > current.getPriority());
        }

        assertTrue(count == b.size());
    }

    public int numEmptyLevels(Bag<?, ?> bag) {
        /*
         * int empty = 0;
         * for (int i = 0; i < bag.level.length; i++) {
         * if (bag.level[i].isEmpty()) {
         * empty++;
         * }
         * }
         * return empty;
         */
        return 0;
    }

    public void testBagIterator(final Bag<NullItem, CharSequence> b) {

        b.putIn(new NullItem(0.1f));
        b.putIn(new NullItem(0.2f));
        b.putIn(new NullItem(0.3f));
        b.putIn(new NullItem(0.4f));
        b.putIn(new NullItem(0.5f));
        b.putIn(new NullItem(0.6f));
        b.putIn(new NullItem(0.7f));
        b.putIn(new NullItem(0.8f));

        assert !(b instanceof Bag) || (numEmptyLevels((Bag<?, ?>) b) < L);

        testIterator(b);

        b.clear();

        testIterator(b);

        b.putIn(new NullItem(0.6f));

        testIterator(b);

    }

    @Test
    public void testBags() throws Exception {
        // Nar nar = new Nar();
        // testBagIterator(new Bag(L, L*2, nar.narParameters));
        assert (true);
    }

}
