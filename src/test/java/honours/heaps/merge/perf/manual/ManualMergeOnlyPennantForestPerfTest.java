package honours.heaps.merge.perf.manual;

import honours.heaps.merge.PennantForest;
import org.apache.commons.lang3.NotImplementedException;
import org.jheaps.AddressableHeap;

import java.util.IntSummaryStatistics;
import java.util.LongSummaryStatistics;
import java.util.Random;

public class ManualMergeOnlyPennantForestPerfTest extends ManualPennantForestPerfTest {
    @Override
    protected AddressableHeap<Integer, Void> merge(AddressableHeap<Integer, Void> h1, AddressableHeap<Integer, Void> h2) {
        throw new NotImplementedException();
    }

    @Override
    protected String getType() {
        return "MergePennantForest";
    }

    @Override
    protected void testImpl(SizePair size, LongSummaryStatistics stats, IntSummaryStatistics blackHole) {
        AddressableHeap<Integer, Void> h1 = createHeap();
        AddressableHeap<Integer, Void> h2 = createHeap();
        Random r = new Random();

        for (int i = 0; i < size.s1(); i++) {
            h1.insert(r.nextInt(Integer.MAX_VALUE - 1));
            if (size.s2() > 0) {
                if (i < size.s2())
                    h2.insert(r.nextInt(Integer.MAX_VALUE - 1));
            } else
                h2.insert(r.nextInt(Integer.MAX_VALUE - 1));
        }

        Forests f = createForests(h1, h2);

        long preMerge = System.nanoTime();
        PennantForest pf = PennantForest.merge(f.p1(), f.p2());
        stats.accept(System.nanoTime() - preMerge);

        PennantForest.Heap result = PennantForest.constructHeap(pf);
        blackHole.accept(result.findMin().getKey());
    }

    @Override
    protected void part2TestImpl(SizePair size, LongSummaryStatistics stats, IntSummaryStatistics blackHole) {
        AddressableHeap<Integer, Void> h1 = createHeap();
        AddressableHeap<Integer, Void> h2 = createHeap();
        AddressableHeap<Integer, Void> h3 = createHeap();
        AddressableHeap<Integer, Void> h4 = createHeap();
        AddressableHeap<Integer, Void> h5 = createHeap();
        AddressableHeap<Integer, Void> lorge = createHeap();
//        AddressableHeap<Integer, Void> lorge2 = createHeap();

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

        AddressableHeap<Integer, Void> can = super.merge(super.merge(super.merge(super.merge(h1, h2), h3), h4), h5);
        Forests f = createForests(lorge, can);
//        Forests f = createForests(lorge, lorge2);

        long pm = System.nanoTime();
        PennantForest pf = PennantForest.merge(f.p1(), f.p2());
        stats.accept(System.nanoTime() - pm);

        PennantForest.Heap res = PennantForest.constructHeap(pf);
        blackHole.accept(res.findMin().getKey());
    }

    @Override
    protected void cascadeImpl(SizePair size, LongSummaryStatistics stats, IntSummaryStatistics blackHole) {
//        AddressableHeap<Integer, Void> h1 = createHeap();
//        AddressableHeap<Integer, Void> h2 = createHeap();
//        AddressableHeap<Integer, Void> h3 = createHeap();
//        AddressableHeap<Integer, Void> h4 = createHeap();
//        AddressableHeap<Integer, Void> h5 = createHeap();
        AddressableHeap<Integer, Void> lorge = createHeap();

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

//        AddressableHeap<Integer, Void> res = super.merge(h1, super.merge(h2, super.merge(h3, super.merge(h4, h5))));

        for (int i = 0; i < 10; i++) {
            int v = r.nextInt(Integer.MAX_VALUE - 1);
            long pi = System.nanoTime();
//            res.insert(v);
            lorge.insert(v);
            stats.accept(System.nanoTime() - pi);
        }

//        blackHole.accept(res.findMin().getKey());
        blackHole.accept(lorge.findMin().getKey());
    }
}
