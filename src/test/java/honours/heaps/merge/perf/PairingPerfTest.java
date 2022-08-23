package honours.heaps.merge.perf;

import honours.heaps.merge.HonoursPairingHeap;
import org.jheaps.AddressableHeap;

public class PairingPerfTest extends AbstractPerfTest {
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
