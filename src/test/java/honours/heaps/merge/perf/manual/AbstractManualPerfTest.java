package honours.heaps.merge.perf.manual;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.jheaps.AddressableHeap;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Category(PerfTests.class)
public abstract class AbstractManualPerfTest {

    protected record SizePair(int s1, int s2){}
    private enum TestPart {SINGLE, MULTI, CASCADE}
    private final SizePair[] sizes = {new SizePair(10_000, 50_000)};
    private final int iterations = 3;
    private final boolean warmup = true;
    private final TestPart testType = TestPart.MULTI;
    private final double confidence = 0.99;
    private final double iterTime = 10;

    protected abstract AddressableHeap<Integer, Void> createHeap();
    protected abstract AddressableHeap<Integer, Void> merge(AddressableHeap<Integer, Void> h1, AddressableHeap<Integer, Void> h2);
    protected abstract String getType();

    private record IterationResult(double result, double blackHole) {}
    private record TestResult(double avg, double error, double blackHole) {}

    @Test
    public void timingTests() {
        DoubleSummaryStatistics blackHole = new DoubleSummaryStatistics();
        Map<SizePair, TestResult> res = new HashMap<>();
        System.out.println("=========================================================================================");
        for (SizePair s :
                sizes) {
            System.out.printf("%s %d %d%n%n", getType(), s.s1, s.s2);
            if (warmup) {
                blackHole.accept(warmup(s));
            }
            TestResult r = tests(s);
            blackHole.accept(r.blackHole);
            res.put(s, r);
        }

        for (var e :
                res.entrySet()) {
            TestResult v = e.getValue();
            System.out.printf("%-25s %.3f ± %.3f ns/op%n", String.format("%s.%d.%d", getType(), e.getKey().s1, e.getKey().s2), v.avg, v.error);
        }

        System.out.println("black hole: " + blackHole.getAverage());
    }

    private double warmup(SizePair size) {
        DoubleSummaryStatistics blackHole = new DoubleSummaryStatistics();
        for (int i = 0; i < iterations; i++) {
            IterationResult result = performTest(size);
            System.out.printf("# Warmup Iteration %d: %.3f ns/op%n", i + 1, result.result);
            blackHole.accept(result.blackHole);
        }
        return blackHole.getAverage();
    }

    private TestResult tests(SizePair size) {
        SummaryStatistics stats = new SummaryStatistics();
        DoubleSummaryStatistics blackHole = new DoubleSummaryStatistics();
        for (int i = 0; i < iterations; i++) {
            IterationResult result = performTest(size);
            stats.addValue(result.result);
            blackHole.accept(result.blackHole);
            System.out.printf("Iteration %d: %.3f ns/op%n", i + 1, result.result);
        }
        System.out.println();
        return new TestResult(stats.getMean(), displayStats(stats), blackHole.getAverage());
    }

    private double displayStats(SummaryStatistics stats) {
        double confidenceVal = confidenceValue(stats);
        System.out.printf("%.3f ±(%.1f%%) %.3f ns/op [Average]%n", stats.getMean(), confidence * 100, confidenceVal);
        System.out.printf("(min, avg, max) = (%.3f, %.3f, %.3f), stdev = %.3f%n", stats.getMin(), stats.getMean(), stats.getMax(), stats.getStandardDeviation());
        System.out.printf("CI (%.1f%%): [%.3f, %.3f] (normal [t] distribution) %n%n%n", confidence * 100, stats.getMean() - confidenceVal, stats.getMean() + confidenceVal);
        return confidenceVal;
    }

    private double confidenceValue(SummaryStatistics stats) {
        if (stats.getN() <= 2) {
            return Double.NaN;
        }

        TDistribution tDist = new TDistribution(stats.getN() - 1);
        double a = tDist.inverseCumulativeProbability(1 - (1 - confidence) / 2);
        return a * stats.getStandardDeviation() / Math.sqrt(stats.getN());
    }

    private IterationResult performTest(SizePair size) {
        LongSummaryStatistics stats = new LongSummaryStatistics();
        IntSummaryStatistics blackHole = new IntSummaryStatistics();

        long elapsedTime = 0;
        while (TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS) < iterTime) {
            long iterStartTime = System.nanoTime();

            switch (testType) {
                case SINGLE -> testImpl(size, stats, blackHole);
                case MULTI -> part2TestImpl(size, stats, blackHole);
                case CASCADE -> cascadeImpl(size, stats, blackHole);
            }

            elapsedTime += System.nanoTime() - iterStartTime;
        }

        return new IterationResult(stats.getAverage(), blackHole.getAverage());
    }

    protected void testImpl(SizePair size, LongSummaryStatistics stats, IntSummaryStatistics blackHole) {
        AddressableHeap<Integer, Void> h1 = createHeap();
        AddressableHeap<Integer, Void> h2 = createHeap();
        Random r = new Random();

        for (int i = 0; i < size.s1; i++) {
            h1.insert(r.nextInt(Integer.MAX_VALUE - 1));
            if (size.s2 > 0) {
                if (i < size.s2)
                    h2.insert(r.nextInt(Integer.MAX_VALUE - 1));
            } else
                h2.insert(r.nextInt(Integer.MAX_VALUE - 1));
        }

        long preMerge = System.nanoTime();
        AddressableHeap<Integer, Void> result = merge(h1, h2);
        stats.accept(System.nanoTime() - preMerge);

        blackHole.accept(result.findMin().getKey());
    }

    // multi merges, swap lorge2 comment with rest to use multi, use lorge2 for baseline
    protected void part2TestImpl(SizePair size, LongSummaryStatistics stats, IntSummaryStatistics blackHole) {
        AddressableHeap<Integer, Void> h1 = createHeap();
        AddressableHeap<Integer, Void> h2 = createHeap();
        AddressableHeap<Integer, Void> h3 = createHeap();
        AddressableHeap<Integer, Void> h4 = createHeap();
        AddressableHeap<Integer, Void> h5 = createHeap();
        AddressableHeap<Integer, Void> lorge = createHeap();
//        AddressableHeap<Integer, Void> lorge2 = createHeap();

        Random r = new Random();

        for (int i = 0; i < size.s1 * 5; i++) {
            if (i < size.s1) {
                h1.insert(r.nextInt(Integer.MAX_VALUE - 1));
                h2.insert(r.nextInt(Integer.MAX_VALUE - 1));
                h3.insert(r.nextInt(Integer.MAX_VALUE - 1));
                h4.insert(r.nextInt(Integer.MAX_VALUE - 1));
                h5.insert(r.nextInt(Integer.MAX_VALUE - 1));
            }
            lorge.insert(r.nextInt(Integer.MAX_VALUE - 1));
//            lorge2.insert(r.nextInt(Integer.MAX_VALUE - 1));
        }

        AddressableHeap<Integer, Void> can = merge(merge(merge(merge(h1, h2), h3), h4), h5);
        long pm = System.nanoTime();
        AddressableHeap<Integer, Void> res = merge(lorge, can);
//        AddressableHeap<Integer, Void> res = merge(lorge, lorge2);
        stats.accept(System.nanoTime() - pm);

        blackHole.accept(res.findMin().getKey());
    }

    protected void cascadeImpl(SizePair size, LongSummaryStatistics stats, IntSummaryStatistics blackHole) {
//        AddressableHeap<Integer, Void> h1 = createHeap();
//        AddressableHeap<Integer, Void> h2 = createHeap();
//        AddressableHeap<Integer, Void> h3 = createHeap();
//        AddressableHeap<Integer, Void> h4 = createHeap();
//        AddressableHeap<Integer, Void> h5 = createHeap();
        AddressableHeap<Integer, Void> lorge = createHeap();

        Random r = new Random();

        for (int i = 0; i < size.s2; i++) {
//            if (i < size.s1) {
//                h5.insert(r.nextInt(Integer.MAX_VALUE - 1));
//                h4.insert(r.nextInt(Integer.MAX_VALUE - 1));
//            }
//            if (i < size.s1 * 2)
//                h3.insert(r.nextInt(Integer.MAX_VALUE - 1));
//            if (i < size.s1 * 4)
//                h2.insert(r.nextInt(Integer.MAX_VALUE - 1));
//            if (i < size.s1 * 8)
//                h1.insert(r.nextInt(Integer.MAX_VALUE - 1));

            lorge.insert(r.nextInt(Integer.MAX_VALUE - 1));
        }

//        AddressableHeap<Integer, Void> res = merge(h1, merge(h2, merge(h3, merge(h4, h5))));

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
