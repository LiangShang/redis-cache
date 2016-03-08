package me.liang.rediscache;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

/**
 * A segment is a fundamental element to support the cache functionality
 *
 *
 */
public class Segment {

    private long currentVolume;

    // the maxVolume defines the max number of elements that this segment can hold
    private final long maxVolume;

    // the evictThreshold defines the threshold that when the currentVolume extends it, eviction of the LRU starts.
    private final long evictThreshold;

    // To implement LRU, we need a queue to record the access( read and write ) recency of an entity
    private BlockingQueue<String> recencyQueue;


    public Segment(long maxVolume) {
        this.maxVolume = maxVolume;
        this.currentVolume = 0;
        this.recencyQueue = new LinkedTransferQueue<>();
        // the init threshold is 75% of the maxVolume
        this.evictThreshold = maxVolume /4 * 3;
    }




}
