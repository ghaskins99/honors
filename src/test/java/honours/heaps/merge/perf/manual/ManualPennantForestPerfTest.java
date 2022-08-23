package honours.heaps.merge.perf.manual;

import honours.heaps.merge.PennantForest;
import org.jheaps.AddressableHeap;

public class ManualPennantForestPerfTest extends AbstractManualPerfTest {
    @Override
    protected AddressableHeap<Integer, Void> createHeap() {
        return new PennantForest.Heap();
    }

    @Override
    protected AddressableHeap<Integer, Void> merge(AddressableHeap<Integer, Void> h1, AddressableHeap<Integer, Void> h2) {
        Forests f = createForests(h1, h2);
        PennantForest pf = PennantForest.merge(f.p1, f.p2);
        return PennantForest.constructHeap(pf);
    }

    @Override
    protected String getType() {
        return "PennantForest";
    }

    protected record Forests(PennantForest p1, PennantForest p2) {}

    protected Forests createForests(AddressableHeap<Integer, Void> h1, AddressableHeap<Integer, Void> h2) {
        return new Forests(PennantForest.constructPF((PennantForest.Heap) h1), PennantForest.constructPF((PennantForest.Heap) h2));
    }
}
