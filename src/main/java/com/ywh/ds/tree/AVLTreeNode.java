package com.ywh.ds.tree;

/**
 * @author ywh
 * @since 4/13/2021
 */
public class AVLTreeNode<T> {

    /**
     * Node data
     */
    public T element;

    /**
     * Left child
     */
    public AVLTreeNode<T> left;

    /**
     * Right child
     */
    public AVLTreeNode<T> right;

    /**
     * Height of node
     */
    public int height;

    /**
     * Constructor; creates a node without any children
     *
     * @param theElement The element contained in this node
     */
    public AVLTreeNode(T theElement) {
        this(theElement, null, null);
    }

    /**
     * Constructor; creates a node with children
     *
     * @param theElement The element contained in this node
     * @param lt         Left child
     * @param rt         Right child
     */
    public AVLTreeNode(T theElement, AVLTreeNode<T> lt, AVLTreeNode<T> rt) {
        element = theElement;
        left = lt;
        right = rt;
    }
}
