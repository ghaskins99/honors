package honours.heaps.merge;

import honours.heaps.util.ArrayUtil;
import honours.heaps.util.NumUtil;
import org.apache.commons.lang3.NotImplementedException;
import org.jheaps.annotations.LinearTime;
import org.jheaps.array.BinaryArrayHeap;

import java.util.*;

import static honours.heaps.util.NumUtil.findLowestCommonParent;
import static honours.heaps.util.NumUtil.floorLog2;

public class HonoursBinaryArrayHeap<K> extends BinaryArrayHeap<K> {

    record PLR<K>(SubHeap<K> pl, SubHeap<K> pr) {}

    /**
     * Simplest method is to concatenate the two arrays and create a heap from the result
     */
    @LinearTime
    public static <K> HonoursBinaryArrayHeap<K> naiveMerge(HonoursBinaryArrayHeap<K> a, HonoursBinaryArrayHeap<K> b) {
        return (HonoursBinaryArrayHeap<K>) heapify(ArrayUtil.concat(a.array, b.array));
    }

    /**
     * Merging two perfect heaps of equal size, from algorithm 2.1 given in
     * <ul>
     * <li>Sack, J.-R., & Strothotte, T. (1985). An Algorithm for Merging Heaps. Acta Informatica, 22, 171-186.</li>
     * </ul>
     */
    public static <K> void mergeEqualPerfectHeaps(HonoursBinaryArrayHeap<K> heap1, int rootedIn, HonoursBinaryArrayHeap<K> heap2) {
        // invoke simple merge taking last element of heap2 as the new root
        simpleMerge(new SubHeap<>(heap1, rootedIn), heap2, heap2.array[heap2.size]);
    }

    public static <K> void simpleMerge(SubHeap<K> mainHeap, HonoursBinaryArrayHeap<K> heap2, K newRoot) {
        simpleMerge(mainHeap, heap2, newRoot, false, false);
    }

    public static <K> void simpleMerge(SubHeap<K> mainHeap, HonoursBinaryArrayHeap<K> heap2, K newRoot, boolean rootFromMainHeap, boolean moreSpace) {
        HonoursBinaryArrayHeap<K> heap1 = mainHeap.heap;
        int rootedIn = mainHeap.rootedIn;
        int newSize = heap1.size + heap2.size;
        K[] t = heap1.array;
        if (moreSpace) {
            int sz = (int)Math.pow(2, floorLog2(newSize) + 1);
            heap1.ensureCapacity(sz - 1);
            heap1.size = sz;
        } else {
            heap1.ensureCapacity(newSize);
        }
        if (rootFromMainHeap) {
            @SuppressWarnings("unchecked")
            K[] tt = (K[]) new Object[t.length - 1];
            System.arraycopy(t, 0, tt, 0, t.length - 1);
            t = tt;
        }

        // place newRoot root of heap1
        heap1.array[rootedIn] = newRoot;

        int[] children = NumUtil.children(rootedIn);

        // copy t to left child of heap1
        int leftChild = children[0];
        heap1.array[leftChild] = t[rootedIn];

        ArrayList<Integer> heap1Parents = new ArrayList<>();
        ArrayList<Integer> tParents = new ArrayList<>();

        heap1Parents.add(leftChild);
        tParents.add(rootedIn);
        int i = 0;
        boolean breakout = false;
        while (i < tParents.size() && !breakout) {
            int[] heap1c = NumUtil.children(heap1Parents.get(i));
            int[] tC = NumUtil.children(tParents.get(i));

            for (int j = 0; j < 2; j++) {
                if (tC[j] >= t.length) {
                    breakout = true;
                    break;
                }
                heap1.array[heap1c[j]] = t[tC[j]];
                heap1Parents.add(heap1c[j]);
                tParents.add(tC[j]);
            }
            i++;
        }

        // copy heap2 to right child of heap1
        int rightChild = children[1];
        heap1.array[rightChild] = heap2.array[1];

        heap1Parents.clear();
        ArrayList<Integer> heap2Parents = new ArrayList<>();
        heap1Parents.add(rightChild);
        heap2Parents.add(1);

        i = 0;
        while(i < heap2Parents.size()) {
            int[] heap1c = NumUtil.children(heap1Parents.get(i));
            int[] heap2c = NumUtil.children(heap2Parents.get(i));

            if (heap2c[0] > heap2.size) break;

            for(int j = 0; j < 2; j++) {
                if (heap2c[j] >= heap2.size && !rootFromMainHeap) break;
                heap1.array[heap1c[j]] = heap2.array[heap2c[j]];
                heap1Parents.add(heap1c[j]);
                heap2Parents.add(heap2c[j]);
            }
            i++;
        }

        if (moreSpace) {
            heap1.fixdown(rootedIn);
            heap1.size = newSize;
            return;
        }
        heap1.size = newSize;

        // trickle down from root
        heap1.fixdown(rootedIn);
    }

    public static <K> void walkDown(HonoursBinaryArrayHeap<K> nHeap, int from, int to, SubHeap<K> kHeap) {
        walkDown(nHeap, from, to, kHeap, null);
    }

    public static <K> void walkDown(HonoursBinaryArrayHeap<K> nHeap, int from, int to, SubHeap<K> kHeap, Stack<Integer> path) {
        if (path == null) {
            path = new Stack<>();
            int current = to;
            while (current != from) {
                path.push(current);
                current = NumUtil.parent(current);
            }
        }
        if (lt(kHeap.heap.array[kHeap.rootedIn], nHeap.array[from])) {
            // exchange
            K temp = nHeap.array[from];
            nHeap.array[from] = kHeap.heap.array[kHeap.rootedIn];
            kHeap.heap.array[kHeap.rootedIn] = temp;
            // trickle down from root
            kHeap.heap.fixdown(kHeap.rootedIn);
        }
        if (from != to) {
            // next node on path from -> to
            // ie from root to root of subHeap p
            int next = path.pop();
            walkDown(nHeap, next, to, kHeap, path);
        }
    }

    public static <K> void mergePerfectHeaps(HonoursBinaryArrayHeap<K> nHeap, HonoursBinaryArrayHeap<K> kHeap) {
        // common parent
        int nLevels = floorLog2(nHeap.size);
        int kLevels = floorLog2(kHeap.size);
        int p = (int) Math.pow(2, nLevels - kLevels);
        walkDown(nHeap, 1, p, new SubHeap<>(kHeap, 1));
        mergeEqualPerfectHeaps(nHeap, p, kHeap);
    }

    private static <K> PLR<K> find2Nodes(HonoursBinaryArrayHeap<K> nHeap, int p, int h) {
        if(floorLog2(new SubHeap<>(nHeap, p).size()) <= h+1) {
            return new PLR<>(new SubHeap<>(nHeap, p), new SubHeap<>(nHeap, p));
        }
        int[] children = NumUtil.children(p);
        int left = children[0], right = children[1]; // left, right children of p

        while(floorLog2(new SubHeap<>(nHeap, left).size()) > h+1) {
            left = NumUtil.children(left)[1]; // right son of left
            right = NumUtil.children(right)[0]; // left son of right
        }

        return new PLR<>(new SubHeap<>(nHeap, left), new SubHeap<>(nHeap, right));
    }

    private static <K> void doubleWalkDown(HonoursBinaryArrayHeap<K> nHeap, int from1, SubHeap<K> to1, int from2, SubHeap<K> to2, SubHeap<K> kHeap) {
        walkDown(nHeap, from1, to1.rootedIn, kHeap);
        walkDown(nHeap, from1, to1.rootedIn, to2);
        walkDown(nHeap, from2, to2.rootedIn, kHeap);
        walkDown(nHeap, from2, to2.rootedIn, to1);
    }

    private static <K> void exchangeHeaps(SubHeap<K> pL, SubHeap<K> pR) {
        Queue<Integer> lParents = new ArrayDeque<>();
        Queue<Integer> rParents = new ArrayDeque<>();

        lParents.add(pL.rootedIn);
        rParents.add(pR.rootedIn);

        while (!lParents.isEmpty() && !rParents.isEmpty()) {
            int[] lCs = NumUtil.children(lParents.remove());
            int[] rCs = NumUtil.children(rParents.remove());
            for (int i = 0; i < 2; i++) {
                int lc = lCs[i], rc = rCs[i];
                if (lc >= pL.heap.array.length || rc >= pR.heap.array.length) break;
                K temp = pL.heap.array[lc];
                pL.heap.array[lc] = pR.heap.array[rc];
                pR.heap.array[rc] = temp;
                lParents.add(lc);
                rParents.add(rc);
            }
        }
    }

    private static <K> void simpleExchangeMerge(HonoursBinaryArrayHeap<K> nHeap, int p, SubHeap<K> pL, SubHeap<K> pR, SubHeap<K> kHeap) {
        doubleWalkDown(nHeap, p, pL, p, pR, kHeap);

        simpleMerge(new SubHeap<>(nHeap, pR.rootedIn), kHeap.heap, pL.heap.array[pL.lastIndexLeft()], true, true);

        // exchange heaps rooted at pl and pr
        exchangeHeaps(pL, pR);
        if (pL.heap.size + 1 < pL.heap.array.length && pL.heap.array[pL.heap.size + 1] != null) {
            pL.heap.array[pL.heap.size + 1] = null;
        }
    }

    public static <K> void mergePerfectIntoNonPerfect(HonoursBinaryArrayHeap<K> nHeap, HonoursBinaryArrayHeap<K> kHeap) {
        boolean heightSame = floorLog2(nHeap.size) == floorLog2(nHeap.size + kHeap.size);
        if (nHeap.size < nHeap.array.length) {
            nHeap.ensureCapacity(nHeap.size);
        }if (kHeap.size < kHeap.array.length) {
            kHeap.ensureCapacity(kHeap.size);
        }
        // now we know that slots in nHeap to be filled are all on same level
        int pRoot = 1;
        if (heightSame) {
            pRoot = findLowestCommonParent(nHeap.size + 1, nHeap.size + kHeap.size);
        }
        SubHeap<K> p = new SubHeap<>(nHeap, pRoot);
        int pHeight = floorLog2(p.size());
        int kHeight = floorLog2(kHeap.size);
        walkDown(nHeap, 1, p.rootedIn, new SubHeap<>(kHeap, 1));
        if (pHeight == kHeight) {
            // simple merge subHeap P with kHeap using last element of kHeap
            simpleMerge(p, kHeap, kHeap.array[kHeap.size]);
        } else if (pHeight == kHeight + 1) {
            // simple merge subHeap P with kHeap using last element of p
            // last element of p should be same as last element of main heap since it's at end
            simpleMerge(p, kHeap, nHeap.array[nHeap.size], true, false);
        } else {
            if (!heightSame) {
                throw new NotImplementedException();
            }
            PLR<K> pLpR = find2Nodes(nHeap, p.rootedIn, kHeight);
            simpleExchangeMerge(nHeap, p.rootedIn, pLpR.pl, pLpR.pr, new SubHeap<>(kHeap, 1));
        }
    }

    @SuppressWarnings("unchecked")
    private static <K> boolean lt(K a, K b) {
        return ((Comparable<? super K>) a).compareTo(b) < 0;
    }

    public void debug() {
        System.out.println(Arrays.toString(array));
    }

    public static class SubHeap<K> {
        public HonoursBinaryArrayHeap<K> heap;
        public int rootedIn;

        public SubHeap(HonoursBinaryArrayHeap<K> heap, int rootedIn) {
            this.heap = heap;
            this.rootedIn = rootedIn;
        }

        public int size() {
            if (rootedIn == 1) return heap.size;
            int[] children = NumUtil.children(rootedIn);
            int leftMost = children[0];
            int rightMost = children[1];
            int size = 1;
            while(rightMost <= heap.size) {
                size += (rightMost - leftMost) + 1;
                leftMost = NumUtil.children(leftMost)[0];
                rightMost = NumUtil.children(rightMost)[1];
            }
            // the rightmost is now larger than heap size, so we could be on sparse last level
            if (leftMost <= heap.size) {
                size += (heap.size - leftMost) + 1;
            }
            return size;
        }

        public int lastIndexLeft() {
            if (rootedIn == 1) return heap.size;
            int[] children = NumUtil.children(rootedIn);
            int leftMost = children[0];
            int rightMost = children[1];
            while(rightMost < heap.size) {
                leftMost = NumUtil.children(leftMost)[0];
                rightMost = NumUtil.children(rightMost)[1];
            }
            if (floorLog2(rightMost) == floorLog2(heap.size)) {
                return heap.size;
            } else {
                // rightmost is on the lower level
                return NumUtil.parent(rightMost);
            }
        }
    }
}
