package honours.heaps.merge;

import org.jheaps.tree.BinaryTreeAddressableHeap;

import java.util.LinkedList;
import java.util.Queue;

public class Meap extends BinaryTreeAddressableHeap<Integer, Void> {

    record SubHeap(Meap heap, Meap.Node subRoot) {}
    record NewRoot(Meap.Node node, Meap tree) {}

    public static void mergeEqualPerfectHeaps(Meap h1, Meap h2) {
        simpleMerge(new SubHeap(h1, h1.root), h2, new NewRoot(lastNode(h2), h2));
    }

    public static Meap.Node lastNode(Meap h) {
        if (h.size == 1) return h.root;
        Meap.Node p = h.findParentNode(h.size);
        if (p.o_c.y_s == p) return p.o_c;
        else return p.o_c.y_s;
    }

    public static void simpleMerge(SubHeap h1, Meap h2, NewRoot newRoot) {
        if (h1.heap.root == h1.subRoot) {
            Meap.Node t = h1.heap.root;

            detach(newRoot);

            h1.heap.root = newRoot.node;

            h1.heap.root.o_c = t;
            h1.heap.root.o_c.y_s = h2.root;
            h2.root.y_s = h1.heap.root;

            h1.heap.fixdown(h1.heap.root);
        } else {
            detach(newRoot);

            Meap.Node t = h1.subRoot;

            // place newRoot at root of h1
            Meap.Node parent = h1.heap.getParent(h1.subRoot);
            if (parent != null) {
                if (parent.o_c == h1.subRoot) {
                    parent.o_c = newRoot.node;
                    parent.o_c.y_s = h1.subRoot.y_s;
                } else if (parent.o_c.y_s == h1.subRoot) {
                    parent.o_c.y_s = newRoot.node;
                    parent.o_c.y_s.y_s = parent;
                }
            }

            // t to left son of heap1
            newRoot.node.o_c = t;
            newRoot.node.o_c.y_s = h2.root;
            h2.root.y_s = newRoot.node;

            h1.heap.fixdown(newRoot.node);
        }
        h1.heap.size += h2.size;
    }

    public static void detach(NewRoot newRoot) {
        Meap.Node parent = newRoot.tree.getParent(newRoot.node);
        if (parent != null) {
            if (parent.o_c == newRoot.node) {
                parent.o_c = null;
            } else if (parent.o_c.y_s == newRoot.node) {
                parent.o_c.y_s = parent;
            }
            newRoot.node.y_s = null;
        }
    }

    public static String printBFS(Meap.Node r) {
        Queue<Node> q = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        q.add(r);
        while (!q.isEmpty()) {
            Meap.Node temp = q.poll();
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
}
