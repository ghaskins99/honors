package honours.heaps.merge;

import org.jheaps.tree.FibonacciHeap;

public class HonoursFibonacciHeap extends FibonacciHeap<Integer, Void> {

    /**
     * Page 599: <br>
     * To carry out meld (h1 , h2),we combine the root lists of h1 and h2 into a single list, and
     * return as the minimum node of the new heap either the minimum node of h1 or
     * the minimum node of h2, whichever contains the item of the smaller key.
     *
     * <ul><li>Fredman, M. L. & Tarjan, R. E. (1987). Fibonacci heaps and their uses in improved network
     * optimization algorithms. J. ACM, 34(3), 596–615.</li></ul>
     * @param other the other heap to be merged into this one
     */
    public void meld(HonoursFibonacciHeap other) {
        // keep restriction compatibility from parent class
        if (other.other != other){
            throw new IllegalStateException("A heap cannot be used after a meld.");
        }

        // simple if I am empty
        if (size == 0) {
            minRoot = other.minRoot;
        } else if (other.size > 0) {
            // when the other exists, we can do an actual merge as outlined in paper
            /*
                combine root lists into a single list
            i.e.,
                  / <-----------------------> \        / <-----------------------------------> \
                myA <-> myPrev <-> myRoot <-> myB     otherA <-> otherRoot <-> otherNext <-> otherB
            to
                 / <----------------------------------------------------------------------------> \
                myA <-> myPrev <-> [otherNext <-> otherB <-> otherA <-> otherRoot] <-> myRoot <-> myB
            thus
                essentially inserting the Other roots list into the middle of My root list
                since the root lists are circular
            and
                these A and B nodes are extraneous since regardless the lists will be circular,
                myPrev - otherNext - otherRoot - myRoot
                in fact even the siblings are extraneous since in the event of size-1 trees,
                otherRoot - myRoot
                the root's next and prev are itself, so we just do
                me.next = other, other.prev = me, me.prev = other, other.next = me
             */
            Node<Integer, Void> myRoot = minRoot;
            Node<Integer, Void> otherRoot = other.minRoot;

            myRoot.prev.next = otherRoot.next;
            otherRoot.next.prev = myRoot.prev;
            myRoot.prev = otherRoot;
            otherRoot.next = myRoot;

            // return as minimum the smallest of min(h1) and min(h2)
            if (other.minRoot.getKey() < minRoot.getKey()) {
                // new minimum
                minRoot = other.minRoot;
            } // otherwise, my root is already minimal

        } // otherwise, if other is empty there's nothing to do

        // similar to pairing heaps, just some admin work
        // correct size
        size += other.size;
        roots += other.roots;

        // clear and invalidate the other one
        other.size = 0;
        other.minRoot = null;
        other.roots = 0;
        other.other = this;
    }
}
