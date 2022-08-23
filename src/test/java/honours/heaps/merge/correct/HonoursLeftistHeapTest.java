package honours.heaps.merge.correct;

import honours.heaps.merge.HonoursLeftistHeap;
import honours.heaps.merge.correct.tree.AbstractTreeAddressableHeapTest;
import org.jheaps.AddressableHeap;

public class HonoursLeftistHeapTest extends AbstractTreeAddressableHeapTest {
    @Override
    protected AddressableHeap<Integer, Void> createHeap() {
        return new HonoursLeftistHeap();
    }

    @Override
    protected AddressableHeap<Integer, Void> merge(AddressableHeap<Integer, Void> h1, AddressableHeap<Integer, Void> h2) {
        ((HonoursLeftistHeap) h1).meld((HonoursLeftistHeap) h2);
        return h1;
    }
}
