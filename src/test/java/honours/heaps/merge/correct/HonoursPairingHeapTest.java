package honours.heaps.merge.correct;

import honours.heaps.merge.HonoursPairingHeap;
import honours.heaps.merge.correct.tree.AbstractTreeAddressableHeapTest;
import org.jheaps.AddressableHeap;

public class HonoursPairingHeapTest extends AbstractTreeAddressableHeapTest {
    @Override
    protected AddressableHeap<Integer, Void> createHeap() {
        return new HonoursPairingHeap();
    }

    @Override
    protected AddressableHeap<Integer, Void> merge(AddressableHeap<Integer, Void> h1, AddressableHeap<Integer, Void> h2) {
        ((HonoursPairingHeap) h1).meld((HonoursPairingHeap) h2);
        return h1;
    }
}
