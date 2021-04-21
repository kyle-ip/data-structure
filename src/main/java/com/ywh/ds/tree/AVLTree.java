package com.ywh.ds.tree;

/**
 * AVL 树
 *
 * @author ywh
 * @since 4/13/2021
 */
class AVLTree<T extends Comparable<? super T>> {

    public AVLTreeNode<T> root;

    public int count;
    
    public int countSingleRotations;

    public int countDoubleRotations;

    public AVLTree() {
        root = null;
        count = 0;
        countSingleRotations = 0;
        countDoubleRotations = 0;
    }

    /**
     * 获取树高
     *
     * @param t
     * @return
     */
    public int height(AVLTreeNode<T> t) {
        return t == null ? -1 : t.height;
    }

    /**
     * 插入节点
     *
     * @param x
     * @return
     */
    public boolean insert(T x) {
        try {
            root = insert(x, root);
            count++;
            return true;
        } catch (Exception e) { // TODO: catch a DuplicateValueException instead!
            return false;
        }
    }

    /**
     * 插入节点
     *
     * @param x
     * @param t
     * @return
     * @throws Exception
     */
    protected AVLTreeNode<T> insert(T x, AVLTreeNode<T> t) throws Exception {
        if (t == null) {
            t = new AVLTreeNode<>(x);
        } else if (x.compareTo(t.element) < 0) {
            t.left = insert(x, t.left);

            if (height(t.left) - height(t.right) == 2) {
                if (x.compareTo(t.left.element) < 0) {
                    t = rotateWithLeftChild(t);
                    countSingleRotations++;
                } else {
                    t = doubleWithLeftChild(t);
                    countDoubleRotations++;
                }
            }
        } else if (x.compareTo(t.element) > 0) {
            t.right = insert(x, t.right);

            if (height(t.right) - height(t.left) == 2) {
                if (x.compareTo(t.right.element) > 0) {
                    t = rotateWithRightChild(t);
                    countSingleRotations++;
                } else {
                    t = doubleWithRightChild(t);
                    countDoubleRotations++;
                }
            }
        } else {
            throw new Exception("Attempting to insert duplicate value");
        }

        t.height = Math.max(height(t.left), height(t.right)) + 1;
        return t;
    }

    /**
     *
     * @param k2
     * @return
     */
    protected AVLTreeNode<T> rotateWithLeftChild(AVLTreeNode<T> k2) {
        AVLTreeNode<T> k1 = k2.left;

        k2.left = k1.right;
        k1.right = k2;

        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(height(k1.left), k2.height) + 1;

        return (k1);
    }

    /**
     *
     * @param k3
     * @return
     */
    protected AVLTreeNode<T> doubleWithLeftChild(AVLTreeNode<T> k3) {
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    /**
     *
     * @param k1
     * @return
     */
    protected AVLTreeNode<T> rotateWithRightChild(AVLTreeNode<T> k1) {
        AVLTreeNode<T> k2 = k1.right;

        k1.right = k2.left;
        k2.left = k1;

        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
        k2.height = Math.max(height(k2.right), k1.height) + 1;

        return (k2);
    }

    /**
     *
     * @param k1
     * @return
     */
    protected AVLTreeNode<T> doubleWithRightChild(AVLTreeNode<T> k1) {
        k1.right = rotateWithLeftChild(k1.right);
        return rotateWithRightChild(k1);
    }

    /**
     *
     * @return
     */
    public String serializeInfix() {
        StringBuilder str = new StringBuilder();
        serializeInfix(root, str, " ");
        return str.toString();
    }

    /**
     *
     * @param t
     * @param str
     * @param sep
     */
    protected void serializeInfix(AVLTreeNode<T> t, StringBuilder str, String sep) {
        if (t != null) {
            serializeInfix(t.left, str, sep);
            str.append(t.element.toString());
            str.append(sep);
            serializeInfix(t.right, str, sep);
        }
    }

    /**
     *
     * @return
     */
    public String serializePrefix() {
        StringBuilder str = new StringBuilder();
        serializePrefix(root, str, " ");
        return str.toString();
    }

    /**
     *
     * @param t
     * @param str
     * @param sep
     */
    private void serializePrefix(AVLTreeNode<T> t, StringBuilder str, String sep) {
        if (t != null) {
            str.append(t.element.toString());
            str.append(sep);
            serializePrefix(t.left, str, sep);
            serializePrefix(t.right, str, sep);
        }
    }

    /**
     *
     */
    public void makeEmpty() {
        root = null;
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     *
     * @return
     */
    public T findMin() {
        if (isEmpty()) {
            return null;
        }
        return findMin(root).element;
    }

    /**
     *
     * @return
     */
    public T findMax() {
        if (isEmpty()) {
            return null;
        }
        return findMax(root).element;
    }

    /**
     *
     * @param t
     * @return
     */
    private AVLTreeNode<T> findMin(AVLTreeNode<T> t) {
        if (t == null) {
            return null;
        }
        while (t.left != null) {
            t = t.left;
        }
        return t;
    }

    /**
     *
     * @param t
     * @return
     */
    private AVLTreeNode<T> findMax(AVLTreeNode<T> t) {
        if (t == null) {
            return null;
        }

        while (t.right != null) {
            t = t.right;
        }
        return t;
    }

    /**
     *
     * @param x
     */
    public void remove(T x) {
        root = remove(x, root);
    }

    /**
     *
     * @param x
     * @param t
     * @return
     */
    public AVLTreeNode<T> remove(T x, AVLTreeNode<T> t) {
        if (t == null) {
            System.out.println("Sorry but you're mistaken, " + null + " doesn't exist in this tree :)\n");
            return null;
        }
        System.out.println("Remove starts... " + t.element + " and " + x);

        if (x.compareTo(t.element) < 0) {
            t.left = remove(x, t.left);
            int l = t.left != null ? t.left.height : 0;

            if ((t.right != null) && (t.right.height - l >= 2)) {
                int rightHeight = t.right.right != null ? t.right.right.height : 0;
                int leftHeight = t.right.left != null ? t.right.left.height : 0;

                if (rightHeight >= leftHeight) {
                    t = rotateWithLeftChild(t);
                } else {
                    t = doubleWithRightChild(t);
                }
            }
        } else if (x.compareTo(t.element) > 0) {
            t.right = remove(x, t.right);
            int r = t.right != null ? t.right.height : 0;
            if ((t.left != null) && (t.left.height - r >= 2)) {
                int leftHeight = t.left.left != null ? t.left.left.height : 0;
                int rightHeight = t.left.right != null ? t.left.right.height : 0;
                if (leftHeight >= rightHeight) {
                    t = rotateWithRightChild(t);
                } else {
                    t = doubleWithLeftChild(t);
                }
            }
        }

        else if (t.left != null) {
            t.element = findMax(t.left).element;
            remove(t.element, t.left);

            if ((t.right != null) && (t.right.height - t.left.height >= 2)) {
                int rightHeight = t.right.right != null ? t.right.right.height : 0;
                int leftHeight = t.right.left != null ? t.right.left.height : 0;

                if (rightHeight >= leftHeight) {
                    t = rotateWithLeftChild(t);
                } else {
                    t = doubleWithRightChild(t);
                }
            }
        } else {
            t = t.right;
        }

        if (t != null) {
            int leftHeight = t.left != null ? t.left.height : 0;
            int rightHeight = t.right != null ? t.right.height : 0;
            t.height = Math.max(leftHeight, rightHeight) + 1;
        }
        return t;
    }

    /**
     *
     * @param x
     * @return
     */
    public boolean contains(T x) {
        return contains(x, root);
    }

    /**
     *
     * @param x
     * @param t
     * @return
     */
    protected boolean contains(T x, AVLTreeNode<T> t) {
        if (t == null) {
            return false; // The node was not found

        } else if (x.compareTo(t.element) < 0) {
            return contains(x, t.left);
        } else if (x.compareTo(t.element) > 0) {
            return contains(x, t.right);
        }

        return true; // Can only reach here if node was found
    }

    /**
     *
     * @param current
     * @return
     */
    public boolean checkBalanceOfTree(AVLTreeNode<Integer> current) {

        boolean balancedRight = true, balancedLeft = true;
        int leftHeight = 0, rightHeight = 0;

        if (current.right != null) {
            balancedRight = checkBalanceOfTree(current.right);
            rightHeight = getDepth(current.right);
        }

        if (current.left != null) {
            balancedLeft = checkBalanceOfTree(current.left);
            leftHeight = getDepth(current.left);
        }

        return balancedLeft && balancedRight && Math.abs(leftHeight - rightHeight) < 2;
    }

    /**
     *
     * @param n
     * @return
     */
    public int getDepth(AVLTreeNode<Integer> n) {
        int leftHeight = 0, rightHeight = 0;

        if (n.right != null) {
            rightHeight = getDepth(n.right);
        }
        if (n.left != null) {
            leftHeight = getDepth(n.left);
        }

        return Math.max(rightHeight, leftHeight) + 1;
    }

    /**
     *
     * @param current
     * @return
     */
    public boolean checkOrderingOfTree(AVLTreeNode<Integer> current) {
        if (current.left != null) {
            if (current.left.element.compareTo(current.element) > 0) {
                return false;
            } else {
                return checkOrderingOfTree(current.left);
            }
        } else if (current.right != null) {
            if (current.right.element.compareTo(current.element) < 0) {
                return false;
            } else {
                return checkOrderingOfTree(current.right);
            }
        } else {
            return true;
        }

    }
}
