import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Aditi Mahajan
 *
 */
public class BSTStringSet implements SortedStringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        if (_root == null) {
            _root = new Node(s);
        } else {
            Node low = findLowest(s);
            if (s.compareTo(low.s) > 0) {
                low.right = new Node(s);
            } else if (s.compareTo(low.s) < 0){
                low.left = new Node(s);
            }
        }
    }

    public Node findLowest(String s) {
        if (_root == null) {
            return null;
        }
        Node p = _root;

        while (true) {
            Node next;
            if (s.compareTo(p.s) < 0) {
                next = p.left;
            } else if (s.compareTo(p.s) > 0) {
                next = p.right;
            } else {
                return p;
            }
            if (next == null) {
                return p;
            } else {
                p = next;
            }
        }

    }

    @Override
    public boolean contains(String s) {
        Node node = findLowest(s);
        return node != null && s.equals(node.s);
    }

    @Override
    public List<String> asList() {
        List<String> list = new ArrayList<String>();
        for (String s : this) {
            list.add(s);
        }
        return list;
    }

    public List<String> rangeList(String low, String high) {
        List<String> list = new ArrayList<String>();
        Iterator<String> iter = iterator(low, high);
        while (iter.hasNext()) {
            list.add(iter.next());
        }
        return list;
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    @Override
    public Iterator<String> iterator(String low, String high) {
        return new RangedBSTIterator(low, high);
    }

    private class RangedBSTIterator implements Iterator<String> {

        private Stack<Node> _toDo = new Stack<Node>();
        private String _high;
        private String _low;

        public RangedBSTIterator(String low, String high) {
            _low = low;
            _high = high;
            addTree(_root);
        }


        @Override
        public boolean hasNext() {
            return (!_toDo.isEmpty() && _toDo.peek().s.compareTo(_high) <= 0);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String next() {
           if (!hasNext()) {
               throw new NoSuchElementException();
           }
           Node node = _toDo.pop();
           addTree(node.right);
           return node.s;
        }

        private void addTree(Node node) {
            while (node != null && node.s.compareTo(_low) >= 0) {
                _toDo.push(node);
                node = node.left;
            }

            if (node != null) {
                addTree(node.right);
            }
        }
    }

    /** Root node of the tree. */
    private Node _root;
}
