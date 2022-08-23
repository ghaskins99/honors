package honours.heaps.merge.correct;

import honours.heaps.merge.HonoursBinaryArrayHeap;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.fail;

public class HonoursBinaryArrayHeapTest {

    private boolean heapViolated(HonoursBinaryArrayHeap<Integer> h) {
        int lastMin = 0;
        while (h.size() > 0) {
            if(h.findMin() < lastMin) {
                return true;
            }
            lastMin = h.deleteMin();
        }
        return false;
    }

    @Test
    public void sizeTest() {
        HonoursBinaryArrayHeap<Integer> n = new HonoursBinaryArrayHeap<>();
        for (int i = 0; i < 23; i++) {
            n.insert(i);
        }

        Assert.assertEquals(23, new HonoursBinaryArrayHeap.SubHeap<>(n, 1).size());
        Assert.assertEquals(15, new HonoursBinaryArrayHeap.SubHeap<>(n, 2).size());
        Assert.assertEquals(7, new HonoursBinaryArrayHeap.SubHeap<>(n, 5).size());
        Assert.assertEquals(3, new HonoursBinaryArrayHeap.SubHeap<>(n, 11).size());

        n.clear();
        for (int i = 0; i < 15; i++) {
            n.insert(i);
        }

        Assert.assertEquals(7, new HonoursBinaryArrayHeap.SubHeap<>(n, 3).size());

        n.clear();
        for(int i = 0; i < 16; i++) {
            n.insert(i);
        }

        Assert.assertEquals(4, new HonoursBinaryArrayHeap.SubHeap<>(n, 4).size());
        for(int i = 0; i < 16; i++) {
            n.insert(i);
        }
        Assert.assertEquals(8, new HonoursBinaryArrayHeap.SubHeap<>(n, 4).size());
    }

    @Test
    public void lastIndexLeftTest() {
        HonoursBinaryArrayHeap<Integer> n = new HonoursBinaryArrayHeap<>();
        for (int i = 0; i < 22; i++) {
            n.insert(i);
        }
        Assert.assertEquals(22, new HonoursBinaryArrayHeap.SubHeap<>(n, 2).lastIndexLeft());
        Assert.assertEquals(19, new HonoursBinaryArrayHeap.SubHeap<>(n, 4).lastIndexLeft());
        Assert.assertEquals(22, new HonoursBinaryArrayHeap.SubHeap<>(n, 5).lastIndexLeft());
        Assert.assertEquals(17, new HonoursBinaryArrayHeap.SubHeap<>(n, 8).lastIndexLeft());
        Assert.assertEquals(19, new HonoursBinaryArrayHeap.SubHeap<>(n, 9).lastIndexLeft());
        Assert.assertEquals(22, new HonoursBinaryArrayHeap.SubHeap<>(n, 11).lastIndexLeft());
    }

    @Test
    public void testEqualPerfect() {
        HonoursBinaryArrayHeap<Integer> a = new HonoursBinaryArrayHeap<>();
        HonoursBinaryArrayHeap<Integer> b = new HonoursBinaryArrayHeap<>();

        Integer[] l = {1,5,3,7,9,8,11};
        Integer[] r = {2,10,6,14,18,16,22};

        for (int i = 0; i < l.length; i++) {
            a.insert(l[i]);
            b.insert(r[i]);
        }

        HonoursBinaryArrayHeap.mergeEqualPerfectHeaps(a, 1, b);
        if (heapViolated(a))
            fail();
    }

    @Test
    @Ignore
    public void testSimpleMergeNonRoot() {
        int nSize = 19, kSize = 7, rootedIn = 3, last = 0;
        HonoursBinaryArrayHeap<Integer> n = new HonoursBinaryArrayHeap<>();
        HonoursBinaryArrayHeap<Integer> k = new HonoursBinaryArrayHeap<>();

//        Random ran = new Random();

        for (int i = 0; i < nSize; i++) {
            n.insert(i + 1);
            if (i < kSize) {
                int j = (i + 2) * 10;
                k.insert(j);
                last = j;
            }
        }
        n.debug();
        k.debug();

//        mergeEqualPerfectHeaps(n, 3, k);
        HonoursBinaryArrayHeap.simpleMerge(new HonoursBinaryArrayHeap.SubHeap<>(n, rootedIn), k, last, false, true);
        n.debug();
    }

    @Test
    public void testManualNonEqualPerfect() {
        HonoursBinaryArrayHeap<Integer> c = new HonoursBinaryArrayHeap<>();
        HonoursBinaryArrayHeap<Integer> d = new HonoursBinaryArrayHeap<>();

        Integer[] l2 = {1,3,2,5,8,10,6,7,9,22,11,14,18,16,17};
        Integer[] r2 = {20,100,60,140,180,160,220};

        for (int i = 0; i < l2.length; i++) {
            c.insert(l2[i]);
            if (i < r2.length)
                d.insert(r2[i]);
        }

        HonoursBinaryArrayHeap.mergeEqualPerfectHeaps(c, 2, d);
        if (heapViolated(c))
            fail();
    }

    @Test
    public void testNonEqualPerfect() {
        HonoursBinaryArrayHeap<Integer> n = new HonoursBinaryArrayHeap<>();
        HonoursBinaryArrayHeap<Integer> k = new HonoursBinaryArrayHeap<>();

        Random ran = new Random();

        int perfect1 = 7;
        int perfect2 = 15;

        for (int i = 0; i < perfect2; i++) {
            n.insert(ran.nextInt(100));
            if (i < perfect1) {
                k.insert(ran.nextInt(100));
            }
        }

        HonoursBinaryArrayHeap.mergePerfectHeaps(n, k);
        if (heapViolated(n))
            fail();
    }

    @Test
    public void testPerfectNonPerfect() {
        // h(p) == h(k) + 1
        HonoursBinaryArrayHeap<Integer> n = new HonoursBinaryArrayHeap<>();
        HonoursBinaryArrayHeap<Integer> k = new HonoursBinaryArrayHeap<>();

        Random ran = new Random();

        // k should be perfect
        int nSize = 16, kSize = 7;

        for (int i = 0; i < nSize; i++) {
            n.insert(ran.nextInt(100));
            if (i < kSize) {
                k.insert(ran.nextInt(100));
            }
        }

        HonoursBinaryArrayHeap.mergePerfectIntoNonPerfect(n, k);
        if (heapViolated(n))
            fail();
    }

    @Test
    public void testPerfectNonPerfectLarger() {
        // otherwise larger
        HonoursBinaryArrayHeap<Integer> notPerf = new HonoursBinaryArrayHeap<>();
        HonoursBinaryArrayHeap<Integer> perf = new HonoursBinaryArrayHeap<>();

        Random ran = new Random();

        int nSize = 19, kSize = 7;
        for (int i = 0; i < nSize; i++) {
            notPerf.insert(ran.nextInt(100));
            if (i < kSize) {
                perf.insert(ran.nextInt(100));
            }
        }

        HonoursBinaryArrayHeap.mergePerfectIntoNonPerfect(notPerf, perf);
        if (heapViolated(notPerf))
            fail();
    }

    @Test
    public void manyRandom() {
        int[] sizes = new int[] {3, 7, 15, 31, 63, 127};
        Random ran = new Random();

        for (var s :
                sizes) {
            for (int i = s; i < 200; i++) {
                HonoursBinaryArrayHeap<Integer> notP = new HonoursBinaryArrayHeap<>();
                HonoursBinaryArrayHeap<Integer> P = new HonoursBinaryArrayHeap<>();
                for (int j = 0; j < i; j++) {
                    notP.insert(ran.nextInt(1000));
                }
                for (int j = 0; j < s; j++) {
                    P.insert(ran.nextInt(1000));
                }
                try {
                    notP.debug();
                    P.debug();
                    HonoursBinaryArrayHeap.mergePerfectIntoNonPerfect(notP, P);
                    notP.debug();
                    if (heapViolated(notP)) {
                        System.out.println(i);
                        System.out.println(s);
                        notP.debug();
                        fail();
                    }
                } catch (NotImplementedException e) {
                    System.out.println(e.getMessage());
                }

            }
        }
    }

    // perfect into non-perfect as would be used in test
    @Test
    public void large() {
        int s1 = 100_000;
        int s2 = (int) Math.pow(2, 10) - 1;

        HonoursBinaryArrayHeap<Integer> h1 = new HonoursBinaryArrayHeap<>();
        HonoursBinaryArrayHeap<Integer> h2 = new HonoursBinaryArrayHeap<>();
        Random r = new Random();

        for (int i = 0; i < s1; i++) {
            h1.insert(r.nextInt(Integer.MAX_VALUE - 1));
            if (i < s2)
                h2.insert(r.nextInt(Integer.MAX_VALUE - 1));
        }

        HonoursBinaryArrayHeap.mergePerfectIntoNonPerfect(h1, h2);
        if (heapViolated(h1)) {
            fail();
        }
    }
}
