package honours.heaps.merge.perf;

import honours.heaps.merge.PennantForest;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@org.openjdk.jmh.annotations.State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SamplePerfTest {
    private PennantForest p1;
    private PennantForest p2;

    @Setup(Level.Invocation)
    public void setup() {
        PennantForest.Heap h1 = new PennantForest.Heap();
        PennantForest.Heap h2 = new PennantForest.Heap();
        Random r = new Random();
        int SIZE = 1_000_000;
        for (int i = 0; i < SIZE; i++) {
            h1.insert(r.nextInt(Integer.MAX_VALUE - 1));
            h2.insert(r.nextInt(Integer.MAX_VALUE - 1));
        }
        p1 = PennantForest.constructPF(h1);
        p2 = PennantForest.constructPF(h2);
    }

    @Benchmark
    public PennantForest doMerge() {
        return PennantForest.merge(p1, p2);
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(SamplePerfTest.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
