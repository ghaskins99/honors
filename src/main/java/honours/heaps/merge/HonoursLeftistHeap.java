package honours.heaps.merge;

import org.jheaps.tree.LeftistHeap;

public class HonoursLeftistHeap extends LeftistHeap<Integer, Void> {

    public void meld(HonoursLeftistHeap other) {
        // keep restriction compatibility from parent class
        if (other.other != other){
            throw new IllegalStateException("A heap cannot be used after a meld.");
        }

        root = merge(root, other.root);

        // correct size
        size += other.size;

        // clear and invalidate the other one
        other.size = 0;
        other.root = null;
        other.other = this;
    }

    private Node<Integer, Void> merge(Node<Integer, Void> n1, Node<Integer, Void> n2) {
        if (n1 == null) return n2;
        if (n2 == null) return n1;

        if (n1.getKey() > n2.getKey()) return merge(n2, n1);

        // if "left" is null we also know there is no right due to the OC and YS structure, so we move
        // the "right child" to the left side, but we would have set the right child to n2 in the case
        // when the right child is null (i.e. n1.right = merge(n1.right, n2), returning n2)
        if (n1.o_c == null) {
            n1.o_c = n2;
            n2.y_s = n1;
        } else {
            if (n1.o_c.y_s == n1) n1.o_c.y_s = null;
            n1.o_c.y_s = merge(n1.o_c.y_s, n2);

            LeftistNode<Integer, Void> n1L = (LeftistNode<Integer, Void>) n1.o_c;
            LeftistNode<Integer, Void> n1R = (LeftistNode<Integer, Void>) n1.o_c.y_s;
            if (n1L.npl < n1R.npl) {
                // move left to right
                n1.o_c = n1R;
                n1R.y_s = n1L;
                n1L.y_s = n1;
                n1R = n1L;
            }
            ((LeftistNode<Integer, Void>)n1).npl = n1R.npl + 1;
        }

        return n1;
    }
}
