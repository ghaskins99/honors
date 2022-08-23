package honours.heaps.merge.perf.manual;

import honours.heaps.merge.HonoursPairingHeap;
import honours.heaps.merge.perf.manual.AbstractManualPerfTest;
import org.jheaps.AddressableHeap;

public class ManualPairingPerfTest extends AbstractManualPerfTest {
    @Override
    protected AddressableHeap<Integer, Void> createHeap() {
        return new HonoursPairingHeap();
    }

    @Override
    protected AddressableHeap<Integer, Void> merge(AddressableHeap<Integer, Void> h1, AddressableHeap<Integer, Void> h2) {
        ((HonoursPairingHeap) h1).meld((HonoursPairingHeap) h2);
        return h1;
    }

    @Override
    protected String getType() {
        return "PairingHeap";
    }
}
