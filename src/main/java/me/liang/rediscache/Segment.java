package me.liang.rediscache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A segment is a fundamental element to support the cache functionality
 *
 *
 */
public class Segment {

    // FIXME find a way to init this jedisPool
    private JedisPool jedisPool;

    private AtomicLong currentVolume;

    // the maxVolume defines the max number of elements that this segment can hold
    private final long maxVolume;

    // the evictThreshold defines the threshold that when the currentVolume extends it, eviction of the LRU starts.
    private final long evictThreshold;

    // To implement LRU, we need a queue to record the access( read and write ) recency of an entity
    private BlockingQueue<String> recencyQueue;


    public Segment(long maxVolume) {
        this.maxVolume = maxVolume;
        this.currentVolume = new AtomicLong(0);
        this.recencyQueue = new LinkedTransferQueue<>();
        // the init threshold is 75% of the maxVolume
        this.evictThreshold = maxVolume /4 * 3;
    }

    /**
     * set key-value pair to the redis
     * @param key key
     * @param value value
     * @return true if set is successful
     */
    public boolean set(String key, String value) {

        Jedis jedis = jedisPool.getResource();
        jedis.set(key, value);
        long c = currentVolume.incrementAndGet();
        return true;

    }




}
