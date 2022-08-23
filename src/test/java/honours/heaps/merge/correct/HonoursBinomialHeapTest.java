package honours.heaps.merge.correct;

import honours.heaps.merge.HonoursBinomialHeap;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.fail;

/**
 * unfortunately have to do a bit of manual re-typing of tests
 * here since the Binomial heap was not included with JHeaps
 */
public class HonoursBinomialHeapTest {

    private boolean heapViolated(HonoursBinomialHeap h) {
        int l = 0;
        while (h.size() > 0) {
            if (h.minKey() < l)
                return true;
            l = h.delMin();
        }
        return false;
    }

    private HonoursBinomialHeap createHeap() {
        return new HonoursBinomialHeap();
    }

    private HonoursBinomialHeap merge(HonoursBinomialHeap h1, HonoursBinomialHeap h2) {
        h1.meld(h2);
        return h1;
    }

    @Test
    public void fullRandom() {
        Random r = new Random();
        int s1 = 1000;
        for (int s2 = 2; s2 < s1; s2++) {
            HonoursBinomialHeap h1 = createHeap();
            HonoursBinomialHeap h2 = createHeap();
            for (int i = 1; i < s1; i++) {
                h1.insert(r.nextInt(1000));
                if (i < s2) h2.insert(r.nextInt(1000));
            }
            HonoursBinomialHeap res = merge(h1, h2);
            if (heapViolated(res))
                fail();
        }
    }
}
