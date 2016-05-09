package me.liang.rediscache;

import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;

public class Cache {

    private final int segmentCount = 16;
    private final int segmentMask = segmentCount - 1;

    Segment[] segments = new Segment[segmentCount];

    public static Builder newBuilder(){return new Builder();}

    public Segment[] getSegments() {
        return segments;
    }

    public boolean set(String key, String value) {
        return segments[key.hashCode() & segmentMask].set(key, value);
    }

    public String get(String key) {
        return segments[key.hashCode() & segmentMask].get(key);
    }



    public static class Builder {

        private JedisPool jedisPool;
        private long maxVolume = Integer.MAX_VALUE;
        private long expireAfterAccessSeconds = 60;  // default value is 60 seconds

        public Cache build() {
            if (jedisPool == null) {
                throw new IllegalStateException("The jedisPool must not be null");
            }
            if (expireAfterAccessSeconds <= 0) {
                throw new IllegalStateException("The expireAfterAccessSeconds must be larger than 0");
            }
            if (Integer.MAX_VALUE <= expireAfterAccessSeconds) {
                expireAfterAccessSeconds = Integer.MAX_VALUE;
            }
            Cache result = new Cache();
            Segment[] segments = result.getSegments();
            for (int i = 0; i < segments.length; i++) {
                segments[i] = new Segment(maxVolume)
                        .jedisPool(jedisPool)
                        .expireAfterAccessSeconds((int)expireAfterAccessSeconds);
            }
            return result;
        }

        public Builder jedisPool(JedisPool jedisPool) {
            this.jedisPool = jedisPool;
            return this;
        }

        public Builder maxVolume(long maxVolume) {
            this.maxVolume = maxVolume;
            return this;
        }

        public Builder expireAfterAccess(long duration, TimeUnit unit) {
            this.expireAfterAccessSeconds = unit.toSeconds(duration);
            return this;
        }
    }
}
