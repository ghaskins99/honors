package honours.heaps.merge.perf.manual;

import honours.heaps.merge.HonoursBinomialHeap;
import org.apache.commons.lang3.NotImplementedException;
import org.jheaps.AddressableHeap;

import java.util.IntSummaryStatistics;
import java.util.LongSummaryStatistics;
import java.util.Random;

public class ManualBinomialPerfTest extends AbstractManualPerfTest {
    @Override
    protected AddressableHeap<Integer, Void> createHeap() {
        throw new NotImplementedException();
    }

    @Override
    protected AddressableHeap<Integer, Void> merge(AddressableHeap<Integer, Void> h1, AddressableHeap<Integer, Void> h2) {
        throw new NotImplementedException();
    }

    @Override
    protected String getType() {
        return "BinomialHeap";
    }

    @Override
    protected void testImpl(SizePair size, LongSummaryStatistics stats, IntSummaryStatistics blackHole) {
        HonoursBinomialHeap h1 = new HonoursBinomialHeap();
        HonoursBinomialHeap h2 = new HonoursBinomialHeap();
        Random r = new Random();

        for (int i = 0; i < size.s1(); i++) {
            h1.insert(r.nextInt(Integer.MAX_VALUE - 1));
            if (size.s2() > 0) {
                if (i < size.s2())
                    h2.insert(r.nextInt(Integer.MAX_VALUE - 1));
            } else
                h2.insert(r.nextInt(Integer.MAX_VALUE - 1));
        }

        long preMerge = System.nanoTime();
        h1.meld(h2);
        stats.accept(System.nanoTime() - preMerge);

        blackHole.accept(h1.minKey());
    }

    @Override
    protected void part2TestImpl(SizePair size, LongSummaryStatistics stats, IntSummaryStatistics blackHole) {
        HonoursBinomialHeap h1 = new HonoursBinomialHeap();
        HonoursBinomialHeap h2 = new HonoursBinomialHeap();
        HonoursBinomialHeap h3 = new HonoursBinomialHeap();
        HonoursBinomialHeap h4 = new HonoursBinomialHeap();
        HonoursBinomialHeap h5 = new HonoursBinomialHeap();
        HonoursBinomialHeap lorge = new HonoursBinomialHeap();
//        HonoursBinomialHeap lorge2 = new HonoursBinomialHeap();

        Random r = new Random();

        for (int i = 0; i < size.s1() * 5; i++) {
            if (i < size.s1()) {
                h1.insert(r.nextInt(Integer.MAX_VALUE - 1));
                h2.insert(r.nextInt(Integer.MAX_VALUE - 1));
                h3.insert(r.nextInt(Integer.MAX_VALUE - 1));
                h4.insert(r.nextInt(Integer.MAX_VALUE - 1));
                h5.insert(r.nextInt(Integer.MAX_VALUE - 1));
            }
            lorge.insert(r.nextInt(Integer.MAX_VALUE - 1));
//            lorge2.insert(r.nextInt(Integer.MAX_VALUE - 1));
        }

        h1.meld(h2);
        h1.meld(h3);
        h1.meld(h4);
        h1.meld(h5);
        long pm = System.nanoTime();
        lorge.meld(h1);
//        lorge.meld(lorge2);
        stats.accept(System.nanoTime() - pm);

        blackHole.accept(lorge.minKey());
    }

    @Override
    protected void cascadeImpl(SizePair size, LongSummaryStatistics stats, IntSummaryStatistics blackHole) {
//        HonoursBinomialHeap h1 = new HonoursBinomialHeap();
//        HonoursBinomialHeap h2 = new HonoursBinomialHeap();
//        HonoursBinomialHeap h3 = new HonoursBinomialHeap();
//        HonoursBinomialHeap h4 = new HonoursBinomialHeap();
//        HonoursBinomialHeap h5 = new HonoursBinomialHeap();
        HonoursBinomialHeap lorge = new HonoursBinomialHeap();

        Random r = new Random();

        for (int i = 0; i < size.s2(); i++) {
//            if (i < size.s1()) {
//                h5.insert(r.nextInt(Integer.MAX_VALUE - 1));
//                h4.insert(r.nextInt(Integer.MAX_VALUE - 1));
//            }
//            if (i < size.s1() * 2)
//                h3.insert(r.nextInt(Integer.MAX_VALUE - 1));
//            if (i < size.s1() * 4)
//                h2.insert(r.nextInt(Integer.MAX_VALUE - 1));
//            if (i < size.s1() * 8)
//                h1.insert(r.nextInt(Integer.MAX_VALUE - 1));

            lorge.insert(r.nextInt(Integer.MAX_VALUE - 1));
        }

//        h4.meld(h5);
//        h3.meld(h4);
//        h2.meld(h3);
//        h1.meld(h2);

        for (int i = 0; i < 10; i++) {
            int v = r.nextInt(Integer.MAX_VALUE - 1);
            long pi = System.nanoTime();
//            h1.insert(v);
            lorge.insert(v);
            stats.accept(System.nanoTime() - pi);
        }

//        blackHole.accept(h1.minKey());
        blackHole.accept(lorge.minKey());
    }
}
