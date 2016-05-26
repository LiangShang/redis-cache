package me.liang.rediscache.lru;

import java.util.concurrent.ConcurrentHashMap;

/**
 * This is the recency queue that presents the LRU recency
 */
public class RecencyQueue {

    private Node head;
    private Node tail;

    private ConcurrentHashMap<String, Node> keyNodeMapping = new ConcurrentHashMap<>();


    /**
     * add means adding a node the the head of the queue
     *
     * TODO This method seems better to use CAS to set the value
     * @param node
     * @return
     */
    private boolean add(Node node) {

        if (head == null) head = node;
        if (tail == null) tail = node;

        if (tail != node) {
            tail.next = node;
            tail = node;
        }

        return true;

    }

    public boolean add(String key) {
        return add(new Node(key));
    }

    private boolean remove(Node node) {
        if (node == null) return false;
        // todo implement this function
        if (head == node) {
            head = node.next;
        }
        if (tail == node) {
            tail = node.pre;
        }
        node.removeItself();
        node.invalid();
        return true;
    }

    public boolean remove(String key) {
        return remove(keyNodeMapping.get(key));
    }
}
