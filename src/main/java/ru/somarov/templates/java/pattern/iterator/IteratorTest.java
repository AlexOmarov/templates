package ru.somarov.templates.java.pattern.iterator;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class IteratorTest {
}


class Node<T> {
    public T value;
    public Node<T> left, right, parent;

    public Node(T value) {
        this.value = value;
    }

    public Node(T value, Node<T> left, Node<T> right) {
        this.value = value;
        this.left = left;
        left.parent = right.parent = this;
        this.right = right;
    }

    public Iterator<Node<T>> preOrder()
    {
        return new PreOrderIterator<>(this);
    }
}


class PreOrderIterator<T> implements Iterator<Node<T>> {

    Node<T> root, current;
    private boolean yieldedStart;

    public PreOrderIterator(Node<T> root) {
        this.root = current = root;
    }

    public boolean hasRightmostParent(Node<T> node) {
        if(node.parent == null) return false;
        else return (node == node.parent.left) || hasRightmostParent(node.parent);
    }


    @Override
    public boolean hasNext() {
        return current.left != null || current.right != null || hasRightmostParent(current);
    }

    @Override
    public Node<T> next() {
        if(!yieldedStart) {
            yieldedStart = true;
            return current;
        }
        if(current.left != null) current = current.left;
        else if (current.right != null) current = current.right;
        else current = findFirstRightElementFromParents(current);

        return current;
    }

    private Node<T> findFirstRightElementFromParents(Node<T> current) {
        if(current.parent == null) return null;
        if(current == current.parent.left && current.parent.right != null) return current.parent.right;
        else return findFirstRightElementFromParents(current.parent);
    }
}


class InOrderIterator<T> implements Iterator<T> {

    private Node<T> current, root;
    private boolean yieldedStart;

    public InOrderIterator(Node<T> root) {
        this.root = current = root;

        while(current.left != null) {
            current = current.left;
        }

    }

    public boolean hasRightmostParent(Node<T> node) {
        if(node.parent == null) return false;
        else return (node == node.parent.left) || hasRightmostParent(node.parent);
    }

    @Override
    public boolean hasNext() {
        return current.left != null || current.right != null || hasRightmostParent(current);
    }

    @Override
    public T next() {
        return null;
        //TODO: implement method
    }
}

class BinaryTree<T> implements Iterable<T> {

    private Node<T> root;

    public BinaryTree(Node<T> root) {
        this.root = root;
    }

    @Override
    public Iterator<T> iterator() {
        return new InOrderIterator<T>(root);
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        for(T item: this) {
            action.accept(item);
        }
    }

    @Override
    public Spliterator<T> spliterator() {
        return null;
    }
}


class Demo {
    public static void main(String[] args) {
        Node<Integer> ints = new Node<>(1,
                new Node<>(2, new Node<>(3, new Node<>(4),new Node<>(5)), new Node<>(6)),
                new Node<>(7, new Node<>(8, new Node<>(9),new Node<>(10)), new Node<>(11))

                );
        PreOrderIterator<Integer> iterator = (PreOrderIterator<Integer>) ints.preOrder();

        while (iterator.hasNext()) {
            System.out.println(iterator.next().value);
        }
    }
}
