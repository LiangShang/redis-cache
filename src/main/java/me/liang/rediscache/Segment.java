package me.liang.rediscache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.ConcurrentLinkedQueue;
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
    private ConcurrentLinkedQueue<String> recencyQueue;


    public Segment(long maxVolume) {
        this.maxVolume = maxVolume;
        this.currentVolume = new AtomicLong(0);
        this.recencyQueue = new ConcurrentLinkedQueue<>();
        // the init threshold is 75% of the maxVolume
        this.evictThreshold = maxVolume /4 * 3;
    }

    public Segment jedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        return this;
    }

    /**
     * set key-value pair to the redis
     * @param key key
     * @param value value
     * @return true if set is successful
     */
    public boolean set(String key, String value) {

        try (Jedis jedis = jedisPool.getResource()) {
            long result = jedis.setnx(key, value);
            long c = moveToLast(key);
            if (result == 1 && evictThreshold < c) {
                // too many keys in cache so try to evict the
                evictByLRU(jedis);
            }
            return true;
        }

    }

    public String get(String key) {
        try(Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(key);
            if (value != null) {
                moveToLast(key);
            }
            return value;
        }

    }

    private void evictByLRU(Jedis jedis) {
        String toBeRemoved = recencyQueue.poll();
        jedis.del(toBeRemoved);
        currentVolume.decrementAndGet();
    }

    // TODO change to an atomic action
    // used by set(String key, String value)
    private long moveToLast(String key) {
        long c;
        if (!recencyQueue.remove(key)) {
            c = currentVolume.incrementAndGet();
        } else {
            c = currentVolume.get();
        }
        recencyQueue.offer(key);
        return c;
    }


}
