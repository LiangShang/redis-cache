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
    public boolean add(Node node) {

        if (head == null) head = node;
        if (tail == null) tail = node;
        return true;

    }
}
