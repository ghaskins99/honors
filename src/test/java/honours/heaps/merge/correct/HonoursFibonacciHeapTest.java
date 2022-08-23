package honours.heaps.merge.correct;

import honours.heaps.merge.HonoursFibonacciHeap;
import honours.heaps.merge.correct.tree.AbstractTreeAddressableHeapTest;
import org.jheaps.AddressableHeap;

public class HonoursFibonacciHeapTest extends AbstractTreeAddressableHeapTest {

    @Override
    protected AddressableHeap<Integer, Void> createHeap() {
        return new HonoursFibonacciHeap();
    }

    @Override
    protected AddressableHeap<Integer, Void> merge(AddressableHeap<Integer, Void> h1, AddressableHeap<Integer, Void> h2) {
        ((HonoursFibonacciHeap) h1).meld((HonoursFibonacciHeap) h2);
        return h1;
    }
}
