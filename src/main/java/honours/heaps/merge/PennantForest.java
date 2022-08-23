package honours.heaps.merge;

import honours.heaps.util.NumUtil;
import org.jheaps.tree.BinaryTreeAddressableHeap;

import java.util.*;

public class PennantForest {
    public ArrayList<Pennant> pennants;
    public int[] descriptor;
    public long size;

    public PennantForest() {
        pennants = new ArrayList<>();
    }

    public static PennantForest constructPF(Heap h) {
        PennantForest pf = new PennantForest();
        pf.size = h.size;
        int height = NumUtil.floorLog2((int) h.size);
        pf.pennants = new ArrayList<>(height + 1);
        for (int i = 0; i < height + 1; i++) {
            pf.pennants.add(null);
        }

        // initialize the descriptor to 0
        pf.descriptor = new int[height + 1];
        Arrays.fill(pf.descriptor, 0);

        int j = height;
        // for each edge on the path from root to the rightmost leaf on the last level (last node)
        long[] s = {h.size};
        BitSet bits = BitSet.valueOf(s);
        Heap.Node cur = h.root;
        for (int i = bits.length() - 2; i >= 0; i--) {
            Heap.Node child;
            if (bits.get(i)) {
                // go right
                child = cur.o_c.y_s;
                // exclude subtree rooted at child
                cur.o_c.y_s = cur;
            } else {
                // go left
                child = cur.o_c;
                // hang subtree on left instead of right
                if (cur.o_c.y_s != cur)
                    cur.o_c = cur.o_c.y_s;
                else
                    cur.o_c = null;
            }
            // disconnect child
            child.y_s = null;

            int curH = height(cur);
            pf.pennants.set(j, new Pennant(cur, NumUtil.pow2(curH)));
            pf.descriptor[height(cur)]++;
            j--;
            cur = child;
        }
        // collect the remaining leaf
        pf.pennants.set(0, new Pennant(cur, NumUtil.pow2(height(cur))));
        pf.descriptor[height(cur)]++;
        return pf;
    }

    public static Heap constructHeap(PennantForest pf) {
        Heap.Node t = pf.pennants.get(0).root;
        int m = NumUtil.floorLog2((int)pf.size);
        for (int i = 1; i <= m; i++) {
            Heap.Node root = pf.pennants.get(i).root;
            Heap.Node child = root.o_c;
            if (height(t) != height(root)) {
                // child of root of pi = left child of pi
                // T = right child of pi
                child.y_s = t;
                t.y_s = root;
            } else {
                // child of root of Pi be right (if exists)
                // T be left child of Pi
                root.o_c = t;
                if (child != null) {
                    root.o_c.y_s = child;
                    root.o_c.y_s.y_s = root;
                } else {
                    root.o_c.y_s = root;
                }
            }
            t = pf.pennants.get(i).root;
        }
        Heap h = new Heap();
        h.size = pf.size;
        h.root = t;
        return h;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public static Pennant mergePennants(Pennant p0, Pennant p1) {
        if (p0.size != p1.size) throw new IllegalArgumentException("must have same size");
        Pennant pi, pj;
        if (p0.root.key < p1.root.key) {
            pi = p0;
            pj = p1;
        } else {
            pi = p1;
            pj = p0;
        }
        if (pi.size == 1) {
            Heap.Node r = pi.root;
            r.o_c = pj.root;
            r.o_c.y_s = r;
            return new Pennant(r, 2);
        }
        Heap.Node r = pi.root; // r = root of pi
        Heap.Node rightChild = pj.root.o_c;
        Heap.Node leftChild = pi.root.o_c;
        Heap.Node childOfR = pj.root;
        leftChild.y_s = rightChild;
        pj.root.o_c = leftChild;
        r.o_c = childOfR;
        childOfR.y_s = r;
        // trickle down on child of R
        Pennant ret = new Pennant(r, p0.size + p1.size);
        ret.trickleDown(r.o_c);
        return ret;
    }

    public static PennantForest merge(PennantForest pPrime, PennantForest pPrimePrime) {
        // if n is not >= k
        if (pPrime.size < pPrimePrime.size) {
            throw new IllegalArgumentException("first must be greater or equal to second");
        }
        // [Initialization]
        Integer[] d = calculateDescriptor(pPrime.size + pPrimePrime.size); // n+k
        int m = d.length;
        Integer[] dPrime = calculateDescriptor(pPrime.size); // n
        Integer[] dPrimePrime = calculateDescriptor(pPrimePrime.size); // k
        // initialize P to empty
        PennantForest P = new PennantForest();
        P.size = pPrime.size + pPrimePrime.size;

        // set descriptor D_ as follows
        int[] dUnderline = new int[m];
        for (int i = 0; i < dPrime.length; i++) {
            if (i < dPrimePrime.length)
                dUnderline[i] = dPrime[i] + dPrimePrime[i];
            else
                dUnderline[i] = dPrime[i];
        }

        // Step [A]
        Pennant largest = pPrimePrime.pennants.get(pPrimePrime.pennants.size() - 1);
        // for every pennant in P' in order of decreasing size
        for (int i = pPrime.pennants.size() - 1; i >=0; i--) {
            Pennant p = pPrime.pennants.get(i);
            // whose size is greater than the size of the largest pennant in P''
            if (p.size > largest.size) {
                if (p.root.key > largest.root.key) {
                    swapRoots(largest, p);
                    largest.trickleDown(largest.root);
                }
                // append p to P
                P.pennants.add(0, p);
                // remove p from P' so that it is not considered 'remaining'
                pPrime.pennants.set(i, null);
            } else {
                // since the forest is already in order of size there are no more
                break;
            }
        }

//        // Step [B]
//        // (1) sort the pennants remaining in p' and p''
//        List<Pennant> allRemaining = pPrime.pennants.stream().filter(Objects::nonNull).collect(Collectors.toList());
//        allRemaining.addAll(pPrimePrime.pennants);
//        // such that decreasing order of size, and if equal size then increasing of root key
//        allRemaining.sort((o1, o2) -> {
//            if (o1.size == o2.size) {
//                return o1.root.key.compareTo(o2.root.key);
//            } else {
//                return Integer.compare(o2.size, o1.size);
//            }
//        });
//        // and append
//        allRemaining.forEach(p -> P.pennants.add(0, p));

        /* Step [B]
        (1) append the pennants remaining in p' and p'' such that they appear in
            decreasing order of size and among pennants of like size they appear in
            non-decreasing order of the keys stored at the roots
         */
        int pPIndex = pPrime.pennants.size() - 1;
        int pPPIndex = pPrimePrime.pennants.size() - 1;
        while (pPIndex >= 0 || pPPIndex >= 0) {
            Pennant toInsert;
            if (pPIndex < 0) {
                toInsert = pPrimePrime.pennants.get(pPPIndex);
                pPPIndex--;
            } else if (pPPIndex < 0) {
                if (pPrime.pennants.get(pPIndex) == null) {
                    pPIndex--;
                    continue;
                }
                toInsert = pPrime.pennants.get(pPIndex);
                pPIndex--;
            } else {
                // both forests have room remaining
                Pennant pP = pPrime.pennants.get(pPIndex);
                Pennant pPP = pPrimePrime.pennants.get(pPPIndex);

                if (pP == null) {
                    pPIndex--;
                    continue;
                }
                // increasing key order when equal
                if (pP.size == pPP.size) {
                    // keys
                    if (pP.root.key < pPP.root.key) {
                        toInsert = pP;
                        pPIndex--;
                    } else {
                        toInsert = pPP;
                        pPPIndex--;
                    }
                } else if (pP.size > pPP.size) {
                    // otherwise, decreasing size
                    toInsert = pP;
                    pPIndex--;
                } else {
                    toInsert = pPP;
                    pPPIndex--;
                }
            }
            P.pennants.add(0, toInsert);
        }

        // (2) establish heap-ordering among keys of roots of pennants in P
        restoreOrder(P);

        // Step [C]
        // sweep through P once more to make it valid
        for (int i = 0; i < m; i++) {
            int desiredSize = NumUtil.pow2(i);
            while(d[i] < dUnderline[i]) {
                // merge the two pennants of size i in P with smallest roots
                // since P is sorted by decreasing size and then increasing keys
                Pennant smallest = null;
                Pennant nextSmallest = null;
                int indx = -1;
                for (int j = P.pennants.size() - 1; j > 1; j--) {
                    if (P.pennants.get(j).size == desiredSize) {
                        smallest = P.pennants.get(j);
                        nextSmallest = P.pennants.get(j-1);
                        if (nextSmallest.size != smallest.size) {
                            throw new RuntimeException(String.format("Could not find a 2nd pennant of size %s", desiredSize));
                        }
                        indx = j;
                        break;
                    }
                }
                if (indx == -1) {
                    throw new RuntimeException(String.format("could not find pennants of size %s", desiredSize));
                }
                Pennant merged = mergePennants(smallest, nextSmallest);
                P.pennants.set(indx, merged);
                P.pennants.remove(indx - 1);
                dUnderline[i]-= 2;
                dUnderline[i + 1]++;
            }
        }

        return P;
    }

    public static void restoreOrder(PennantForest pf) {
        // smallest at start
        int lastIndex = 0;
        for (int i = 1; i < pf.pennants.size(); i++) {
            if (pf.pennants.get(i).root.key > pf.pennants.get(lastIndex).root.key) {
                swapBack(pf, lastIndex, i);
            }
            lastIndex = i;
        }
        for (var p :
                pf.pennants) {
            p.trickleDown(p.root);
        }
        lastIndex = 0;
        for (int i = 1; i < pf.pennants.size(); i++) {
            if (pf.pennants.get(i).root.key > pf.pennants.get(lastIndex).root.key) {
                restoreOrder(pf);
            }
            lastIndex = i;
        }
    }

    public static void swapBack(PennantForest pf, int sIndex, int lIndex) {
        int frontIndex = sIndex;
        int backIndex = lIndex;
        while (frontIndex >=0 && pf.pennants.get(frontIndex).root.key < pf.pennants.get(backIndex).root.key) {
            // swap the roots
            swapRoots(pf.pennants.get(frontIndex), pf.pennants.get(backIndex));
            frontIndex--;
            backIndex--;
        }
    }

    public static void swapRoots(Pennant frontP, Pennant backP) {
        if (frontP.size == 1) {
            Heap.Node frontR = frontP.root;
            if (backP.size == 1) {
                // swap roots
                frontP.root = backP.root;
                backP.root = frontR;
            } else {
                // hook up children
                frontR.o_c = backP.root.o_c;
                frontR.o_c.y_s = frontP.root;
                // swap roots
                frontP.root = backP.root;
                backP.root = frontR;
                // finish hooking up children
                frontP.root.o_c = null;
                frontP.root.y_s = null;
            }
        } else {
            // back size is always larger so no need to check
            Heap.Node frontChild = frontP.root.o_c;
            // hook up children
            frontP.root.o_c = backP.root.o_c;
            frontP.root.o_c.y_s = frontP.root;
            backP.root.o_c = frontChild;
            backP.root.o_c.y_s = backP.root;

            // swap root references
            Heap.Node temp = frontP.root;
            frontP.root = backP.root;
            backP.root = temp;
        }
    }

    private static Pennant maxSizePennant(PennantForest pf) {
        Pennant r = pf.pennants.get(0);
        for (int i = 1; i < pf.pennants.size(); i++) {
            if (pf.pennants.get(i).size > r.size)
                r = pf.pennants.get(i);
        }
        return r;
    }

    public static int height(Heap.Node n) {
        Heap.Node cur = n.o_c;
        int h = 0;
        while (cur != null) {
            h++;
            cur = cur.o_c;
        }
        return h;
    }

    public static Heap.Node getParent(Heap.Node n) {
        if (n.y_s == null) {
            return null;
        }
        Heap.Node c = n.y_s;
        if (c.o_c == n) {
            return c;
        }
        Heap.Node p1 = c.y_s;
        if (p1 != null && p1.o_c == n) {
            return p1;
        }
        return c;
    }

    public static String printBFS(PennantForest pf) {
        StringBuilder sb = new StringBuilder();
        for (var p :
                pf.pennants) {
            sb.append(printBFS(p.root)).append("\n");
        }
        return sb.toString();
    }

    public static String printBFS(Heap.Node r) {
        Queue<Heap.Node> q = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        q.add(r);
        while (!q.isEmpty()) {
            Heap.Node temp = q.poll();
            sb.append(temp.key).append(" ");

            if (temp.o_c != null) {
                q.add(temp.o_c);
                if (temp.o_c.y_s != null && temp.o_c.y_s != temp) {
                    q.add(temp.o_c.y_s);
                }
            }
        }
        return sb.toString();
    }

    public static Integer[] calculateDescriptor(long s) {
        return calculateDescriptor((int)s);
    }

    public static Integer[] calculateDescriptor(int s) {
        long[] i = {s};
        BitSet r = BitSet.valueOf(i);
        int height = NumUtil.floorLog2(s);
        List<Integer> d = new ArrayList<>(height + 1);
        d.add(2 - (r.get(0) ? 1 : 0));
        for (int j = 1; j <= height; j++) {
            d.add((r.get(j-1) ? 1 : 0) + (1 - (r.get(j) ? 1 : 0)));
        }
        return d.toArray(new Integer[0]);
    }

    public static class Heap extends BinaryTreeAddressableHeap<Integer, Void> {}

    public static class Pennant {
        Heap.Node root;
        int size;

        public Pennant(Heap.Node root, int size) {
            this.root = root;
            this.size = size;
        }

        public void trickleDown(Heap.Node n) {
            Heap.Node p = getParent(n);
            while (n.o_c != null) {
                Heap.Node child = n.o_c;
                if (child.y_s != n && child.y_s.key < child.key) {
                    child = child.y_s;
                }
                if (n.key <= child.key) {
                    break;
                }
                swap(child, n, p);
                p = child;
            }
        }

        public void swap(Heap.Node n, Heap.Node root) {
            // assert this.root == root;

            Heap.Node nLeftChild = n.o_c;
            if (root.o_c == n) {
                if (n.y_s == root) {
                    // n is left child and no right sibling
                    n.o_c = root;
                    root.y_s = n;
                } else {
                    // n is left child and has right sibling
                    root.y_s = n.y_s;
                    root.y_s.y_s = n;
                    n.o_c = root;
                }
            } else {
                // n is right child
                root.o_c.y_s = root;
                n.o_c = root.o_c;
                root.y_s = n;
            }
            n.y_s = null;

            // hang children
            root.o_c = nLeftChild;
            if (nLeftChild != null) {
                if (nLeftChild.y_s == n) {
                    nLeftChild.y_s = root;
                } else {
                    nLeftChild.y_s.y_s = root;
                }
            }
            this.root = n;
        }

        public void swap(Heap.Node n, Heap.Node p, Heap.Node pp) {
            if (pp == null) {
                swap(n, p);
                return;
            }

            Heap.Node nLeftChild = n.o_c;
            if (pp.o_c == p) {
                // p left child of pp
                if (p.o_c == n) {
                    if (n.y_s == p) {
                        // n left child of p and no sibling
                        pp.o_c = n;
                        n.y_s = p.y_s;
                        n.o_c = p;
                        p.y_s = n;
                    } else {
                        // n left child or p and sibling
                        n.y_s.y_s = n;
                        Heap.Node tmp = n.y_s;
                        n.y_s = p.y_s;
                        p.y_s = tmp;
                        pp.o_c = n;
                        n.o_c = p;
                    }
                } else {
                    // n right child of p
                    Heap.Node tmp = p.o_c;
                    n.y_s = p.y_s;
                    pp.o_c = n;
                    n.o_c = tmp;
                    tmp.y_s = p;
                    p.y_s = n;
                }
            } else {
                // p right child of pp
                if (p.o_c == n) {
                    if (n.y_s == p) {
                        // n left child of p and no sibling
                        n.y_s = pp;
                        pp.o_c.y_s = n;
                        n.o_c = p;
                        p.y_s = n;
                    } else {
                        // n left child of p and sibling
                        pp.o_c.y_s = n;
                        p.y_s = n.y_s;
                        n.y_s = pp;
                        n.o_c = p;
                        p.y_s.y_s = n;
                    }
                } else {
                    // n right child of p
                    pp.o_c.y_s = n;
                    n.y_s = pp;
                    n.o_c = p.o_c;
                    n.o_c.y_s = p;
                    p.y_s = n;
                }
            }

            // hang children
            p.o_c = nLeftChild;
            if (nLeftChild != null) {
                if (nLeftChild.y_s == n) {
                    nLeftChild.y_s = p;
                } else {
                    nLeftChild.y_s.y_s = p;
                }
            }
        }
    }
}
