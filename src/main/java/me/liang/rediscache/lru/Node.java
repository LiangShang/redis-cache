package me.liang.rediscache.lru;

/**
* This is the lru node that saves the pre, next nodes and the key of the cache
*/
public class Node {

    String key;
    Node pre;
    Node next;

    /**
     * Add a node to its next (after it)
     * @param node
     * @return
     */
    public boolean addToNext(Node node) {

        Node nnext = this.next;

        this.next = node;
        node.next = nnext;
        node.pre = this;
        if (nnext != null) {
            nnext.pre = node;
        }
        return true;
    }

    /**
     * Add a node before it
     * @param node
     * @return
     */
    public boolean addToPre(Node node) {

        Node ppre = this.pre;

        this.pre = node;
        node.pre = ppre;
        node.next = this;
        if (ppre != null) {
            ppre.next = node;
        }
        return true;

    }

    /**
     * Remove itself from the train
     * @return
     */
    public boolean removeItself() {
        Node pre = this.pre;
        Node next = this.next;

        if (pre != null) {
            pre.next = next;
        }
        if (next != null) {
            next.pre = pre;
        }

        this.key = null;

        return true;

    }

    /**
     * Set itself invalid
     * @return
     */
    public boolean invalid() {
        this.key = null;
        return true;
    }

    public Node(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public Node setKey(String key) {
        this.key = key;
        return this;
    }

    public Node getNext() {
        return next;
    }

    public Node setNext(Node next) {
        this.next = next;
        return this;
    }

    public Node getPre() {
        return pre;
    }

    public Node setPre(Node pre) {
        this.pre = pre;
        return this;
    }

}
