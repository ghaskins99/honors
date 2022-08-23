package honours.heaps.merge.correct;

import com.jakewharton.picnic.*;
import honours.heaps.merge.PennantForest;
import honours.heaps.merge.correct.tree.AbstractTreeAddressableHeapTest;
import org.jheaps.AddressableHeap;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;

public class PennantForestTest extends AbstractTreeAddressableHeapTest {

    @Override
    protected AddressableHeap<Integer, Void> createHeap() {
        return new PennantForest.Heap();
    }

    @Override
    protected AddressableHeap<Integer, Void> merge(AddressableHeap<Integer, Void> h1, AddressableHeap<Integer, Void> h2) {
        PennantForest p1 = PennantForest.constructPF((PennantForest.Heap)h1), p2 = PennantForest.constructPF((PennantForest.Heap)h2);
        PennantForest pf = PennantForest.merge(p1, p2);
        return PennantForest.constructHeap(pf);
    }

    @Test
    @Ignore
    public void timingTest() {
        timingTest(1_000_000);
    }

    private void timingTest(int size) {
        int iter = 1000;
        long insertSum = 0;
        long mergeSum = 0;
        long createPFSum = 0;
        long createHeapSum = 0;
        for (int i = 0; i < iter; i++) {
            PennantForest.Heap h1 = new PennantForest.Heap();
            PennantForest.Heap h2 = new PennantForest.Heap();
            Random r = new Random();
            long cInsert = System.currentTimeMillis();
            for (int j = 0; j < size; j++) {
                h1.insert(r.nextInt(Integer.MAX_VALUE - 1));
                h2.insert(r.nextInt(Integer.MAX_VALUE - 1));
            }
            insertSum += incrementTimer(cInsert);

            long cCreatePF = System.currentTimeMillis();
            PennantForest p1 = PennantForest.constructPF(h1), p2 = PennantForest.constructPF(h2);
            createPFSum += incrementTimer(cCreatePF);

            long cMerge = System.currentTimeMillis();
            PennantForest pf = PennantForest.merge(p1, p2);
            mergeSum += incrementTimer(cMerge);

            long cCreateHeap = System.currentTimeMillis();
            PennantForest.Heap res = PennantForest.constructHeap(pf);
            createHeapSum += incrementTimer(cCreateHeap);
        }

        double avgInsert = insertSum / 100.0;
        double avgMerge = mergeSum / 100.0;
        double avgCreatePF = createPFSum / 100.0;
        double avgCreateHeap = createHeapSum / 100.0;

        Table t = new Table.Builder()
                .setTableStyle(new TableStyle.Builder()
                        .setBorderStyle(BorderStyle.Hidden)
                        .build())
                .setCellStyle(new CellStyle.Builder()
                        .setAlignment(TextAlignment.MiddleRight)
                        .setPaddingLeft(1)
                        .setPaddingRight(1)
                        .setBorderLeft(true)
                        .setBorderRight(true)
                        .build())
                .setHeader(new TableSection.Builder()
                        .setCellStyle(new CellStyle.Builder()
                                .setBorder(true)
                                .setAlignment(TextAlignment.BottomLeft)
                                .build())
                        .addRow("Operation", "Average Time (ms)")
                        .build())
                .setBody(new TableSection.Builder()
                        .addRow("insert", Double.toString(avgInsert))
                        .addRow("create PF", Double.toString(avgCreatePF))
                        .addRow("merge", Double.toString(avgMerge))
                        .addRow("create heap", Double.toString(avgCreateHeap))
                        .build())
                .setFooter(new TableSection.Builder()
                        .setCellStyle(new CellStyle.Builder()
                                .setBorder(true)
                                .build())
                        .addRow("avg total", Double.toString(avgInsert + avgCreatePF + avgMerge + avgCreateHeap))
                        .build())
                .build();
        System.out.println(t);
    }

    private long incrementTimer(long cur) {
        return System.currentTimeMillis() - cur;
    }
}
