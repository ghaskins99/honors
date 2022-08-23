package honours.heaps.merge;

import edu.princeton.cs.algs4.BinomialMinPQ;

public class HonoursBinomialHeap extends BinomialMinPQ<Integer> {

    /**
     * Introduction to Algorithms 2nd ed., 19: Binomial Heaps (19.2 Operations on Binomial Heaps)
     * <br><br>
     * "union" from the original binomial naming scheme, called meld to stay in form with the rest
     * @param other the other heap to be merged into this one
     */
    public void meld(HonoursBinomialHeap other) {
        if (other == null) return;

        Node toBeHead = mergeRootLists(head, other.head);
        Node cur = toBeHead, next = cur.sibling, prev = null;
        while (next != null) {
            if (cur.order < next.order || (next.sibling != null && next.sibling.order == cur.order)) {
                // go to the next
                prev = cur;
                cur = next;
            } else if (next.key > cur.key) {
                // if next is larger link s.t. next is above
                cur.sibling = next.sibling;
                link(next, cur);
            } else {
                if (prev == null) {
                    // prev not yet set by regular iteration
                    toBeHead = next;
                } else {
                    prev.sibling = next;
                }
                // link the other way since cur is larger
                link(cur, next);
                cur = next;
            }
            // update next
            next = cur.sibling;
        }

        head = toBeHead;
    }

    private void link(Node larger, Node smaller) {
        larger.sibling = smaller.child;
        smaller.child = larger;
        smaller.order++;
    }

    // name is pretty self-explanatory
    private Node mergeRootLists(Node root, Node otherRoot) {
        if (root == null && otherRoot == null) return new Node();
        if (root == null) return otherRoot;
        else if (otherRoot == null) return root;

        Node me = root; // root list
        Node them = otherRoot; // root list
        Node listHead;
        Node listTail;
        // set up the start of the "new" root linked list
        if (me.order < them.order) {
            listHead = me;
            listTail = me;
            me = me.sibling;
        } else {
            listHead = them;
            listTail = them;
            them = them.sibling;
        }

        // add whichever one is smaller to the list
        while (me != null && them != null) {
            if (me.order < them.order) {
                listTail.sibling = me;
                listTail = me;
                me = me.sibling;
            } else {
                listTail.sibling = them;
                listTail = them;
                them = them.sibling;
            }
        }

        // there may be some remaining in one of them,
        // so tack on whatever is remaining to the end
        if (me == null) {
            listTail.sibling = them;
        } else {
            listTail.sibling = me;
        }
        return listHead;
    }
}
