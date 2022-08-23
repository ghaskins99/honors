package honours.heaps.merge.correct.tree;

import org.jheaps.AddressableHeap;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.fail;

public abstract class AbstractTreeAddressableHeapTest {

    protected abstract AddressableHeap<Integer, Void> createHeap();
    protected abstract AddressableHeap<Integer, Void> merge(AddressableHeap<Integer, Void> h1, AddressableHeap<Integer, Void> h2);

    protected boolean heapViolated(AddressableHeap<Integer, Void> h) {
        int lastMin = 0;
        while (h.size() > 0) {
            if(h.findMin().getKey() < lastMin)
                return true;
            lastMin = h.deleteMin().getKey();
        }
        return false;
    }

    @Test
    public void randomTest() {
        AddressableHeap<Integer, Void> h1 = createHeap();
        AddressableHeap<Integer, Void> h2 = createHeap();
        Random r = new Random();
        int s1 = 12, s2 = 10;
        for (int i = 0; i < s1; i++) {
            h1.insert(r.nextInt(1000));
        }
        for (int i = s1; i < s1 + s2; i++) {
            h2.insert(r.nextInt(1000));
        }

        AddressableHeap<Integer, Void> res = merge(h1, h2);
        if (heapViolated(res))
            fail();
    }

    @Test
    public void fullRandomTest() {
        Random r = new Random();
        int size1 = 1000;
        for (int size2 = 2; size2 < size1; size2++) {
            //create heaps
            AddressableHeap<Integer, Void> h1 = createHeap();
            AddressableHeap<Integer, Void> h2 = createHeap();
            for (int i = 1; i < size1; i++) {
                h1.insert(r.nextInt(1000));
                if (i < size2) {
                    h2.insert(r.nextInt(1000));
                }
            }
            AddressableHeap<Integer, Void> res = merge(h1, h2);
            if (heapViolated(res))
                fail();
        }
    }
}
