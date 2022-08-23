package honours.heaps.merge.perf.manual;

import honours.heaps.merge.HonoursLeftistHeap;
import org.jheaps.AddressableHeap;

public class ManualLeftistPerfTest extends AbstractManualPerfTest {
    @Override
    protected AddressableHeap<Integer, Void> createHeap() {
        return new HonoursLeftistHeap();
    }

    @Override
    protected AddressableHeap<Integer, Void> merge(AddressableHeap<Integer, Void> h1, AddressableHeap<Integer, Void> h2) {
        ((HonoursLeftistHeap) h1).meld((HonoursLeftistHeap) h2);
        return h1;
    }

    @Override
    protected String getType() {
        return "LeftistHeap";
    }
}
