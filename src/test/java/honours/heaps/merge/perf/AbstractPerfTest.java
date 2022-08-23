package honours.heaps.merge.perf;

import org.jheaps.AddressableHeap;
import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public abstract class AbstractPerfTest {

    @Param({"10000", "100000"})
    public int size;

    private AddressableHeap<Integer, Void> ha;
    private AddressableHeap<Integer, Void> hb;

    protected abstract AddressableHeap<Integer, Void> createHeap();
    protected abstract AddressableHeap<Integer, Void> merge(AddressableHeap<Integer, Void> h1, AddressableHeap<Integer, Void> h2);

    @Setup(Level.Invocation)
    public void setup() {
        AddressableHeap<Integer, Void> h1 = createHeap();
        AddressableHeap<Integer, Void> h2 = createHeap();
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            h1.insert(r.nextInt(Integer.MAX_VALUE - 1));
            h2.insert(r.nextInt(Integer.MAX_VALUE - 1));
        }
        ha = h1;
        hb = h2;
    }

    @Benchmark
    public AddressableHeap<Integer, Void> doMerge() {
        return merge(ha, hb);
    }
}
