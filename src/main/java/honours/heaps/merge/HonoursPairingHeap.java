package honours.heaps.merge;

import org.jheaps.tree.PairingHeap;

public class HonoursPairingHeap extends PairingHeap<Integer, Void> {

    /**
     * From page 114: <br>
     * meld (h1, h2): Return the tree formed by linking trees h1 and h2
     *
     * <ul><li>Fredman, M. L., Sedgewick, R., Sleator, D. D., & Tarjan, R. E. (1986). The pairing heap: A
     * new form of self-adjusting heap. Algorithmica, 1(1–4), 111–129.</li></ul>
     * @param other the tree to be merged into this one
     */
    public void meld(HonoursPairingHeap other) {
        // keep restriction compatibility from parent class
        if (other.other != other){
            throw new IllegalStateException("A heap cannot be used after a meld.");
        }

        // merge by linking as described in paper
        root = link(root, other.root);

        // correct size
        size += other.size;

        // invalidate other
        other.size = 0;
        other.root = null;
        other.other = this;
    }

    /**
     * From page 113: <br>
     * linking, which makes the root of smaller key the parent of the root of larger key, with a tie broken arbitrarily.
     *
     * <ul><li>Fredman, M. L., Sedgewick, R., Sleator, D. D., & Tarjan, R. E. (1986). The pairing heap: A
     * new form of self-adjusting heap. Algorithmica, 1(1–4), 111–129.</li></ul>
     */
    private PairingHeap.Node<Integer, Void> link(Node<Integer, Void> a, Node<Integer, Void> b) {
        // not necessarily in the paper but if either are null the answer is easy
        if (a == null) return b;
        if (b == null) return a;

        Node<Integer, Void> smaller;
        Node<Integer, Void> larger;

        // root of smaller becomes parent of root of larger
        if (a.key < b.key) {
            smaller = a;
            larger = b;
        } else {
            smaller = b;
            larger = a;
        }

        /*
         i.e. from a to b we get
            S         L
         sOC-sYS   lOC-lYS

              S
             /
            L - sOC - sYS
           /
          lOC - lYS
         */

        // set larger sibling to be smaller's (left) child
        larger.y_s = smaller.o_c;
        // set larger OS (= older sibling or parent, in this case parent) to be smaller
        larger.o_s = smaller;

        if (smaller.o_c != null) {
            smaller.o_c.o_s = larger;
        }

        // hang larger below smaller
        smaller.o_c = larger;

        return smaller;
    }
}
