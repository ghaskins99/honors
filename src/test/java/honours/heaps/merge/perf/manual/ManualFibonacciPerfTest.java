package honours.heaps.merge.perf.manual;

import honours.heaps.merge.HonoursFibonacciHeap;
import honours.heaps.merge.perf.manual.AbstractManualPerfTest;
import org.jheaps.AddressableHeap;

public class ManualFibonacciPerfTest extends AbstractManualPerfTest {
    @Override
    protected AddressableHeap<Integer, Void> createHeap() {
        return new HonoursFibonacciHeap();
    }

    @Override
    protected AddressableHeap<Integer, Void> merge(AddressableHeap<Integer, Void> h1, AddressableHeap<Integer, Void> h2) {
        ((HonoursFibonacciHeap) h1).meld((HonoursFibonacciHeap) h2);
        return h1;
    }

    @Override
    protected String getType() {
        return "FibonacciHeap";
    }
}
