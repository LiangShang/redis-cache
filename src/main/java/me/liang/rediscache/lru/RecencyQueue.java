package me.liang.rediscache.lru;

import com.google.common.base.Objects;
import org.redisson.RedissonClient;
import org.redisson.core.RBucket;

/**
 * This is the recency queue that presents the LRU recency
 *
 * HEAD <-> 4 <-> 3 <-> 2 <-> 1 <-> TAIL
 */
public class RecencyQueue {

    private static final String prefix = "recency_queue_";
    private static final String headKey = "recency_queue_head";
    private static final String tailKey = "recency_queue_tail";
    private static final String lockPrefix = prefix + "lock_";

    private Node head;
    private Node tail;
    private RedissonClient redissonClient;

    /**
     * INIT the Recency Queue
     * HEAD <-> TAIL
     *
     * @param client
     */
    public RecencyQueue(RedissonClient client) {
        this.redissonClient = client;

        // Init the queue by creating the head node and tail node in redis
        RBucket<Node> head = client.getBucket(headKey);
        RBucket<Node> tail = client.getBucket(tailKey);
        Node headNode = new Node(headKey, null, tailKey);
        Node tailNode = new Node(tailKey, headKey, null);
        head.compareAndSet(null, headNode);
        tail.compareAndSet(null, tailNode);

    }

    /**
     * add means adding a node the the head of the queue
     * <p>
     * The queue before adding is HEAD <-> 4 <-> 3 <-> 2 <-> 1 <-> TAIL
     * add("5")
     * The queue after that is HEAD <-> 5 <-> 4 <-> 3 <-> 2 <-> 1 <-> TAIL
     * <p>
     * TODO This method seems better to use CAS to set the value
     *
     * @param key
     * @return
     */
    private boolean addAfter(String formerKey, String key) {
        formerKey = prefix + formerKey;
        key = prefix + key;
        if (Objects.equal(formerKey, tailKey)) {
            throw new IllegalArgumentException("cannot add node after tail node");
        }

        RBucket<Node> formerBucket = redissonClient.getBucket(formerKey);
        Node former = formerBucket.get();
        if (former == null) {
            throw new IllegalArgumentException("cannot add node after a non-existed node");
        }
        RBucket<Node> addedBucket = redissonClient.getBucket(key);
        Node addedNode = addedBucket.get();
        if (addedNode != null) {
            throw new IllegalArgumentException("cannot add an already-existed node");
        }
        RBucket<Node> nextBucket = redissonClient.getBucket(former.getNext());
        Node next = nextBucket.get();


        addedNode = new Node(key, formerKey, next.getKey());
        Node newFormer = new Node(former).setNext(key);
        Node newNext = new Node(next).setPre(key);

        return formerBucket.compareAndSet(former, newFormer) &&
                addedBucket.compareAndSet(null, addedNode) &&
                nextBucket.compareAndSet(next, newNext);

    }
}
