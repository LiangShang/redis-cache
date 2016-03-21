package me.liang.rediscache;

import redis.clients.jedis.JedisPool;

public class Cache {

    private final int segmentCount = 16;

    Segment[] segments = new Segment[segmentCount];

    public static Builder newBuilder(){return new Builder();}

    public Segment[] getSegments() {
        return segments;
    }



    public static class Builder {

        private JedisPool jedisPool;
        private long maxVolume;

        public Cache build() {
            Cache result = new Cache();
            Segment[] segments = result.getSegments();
            for (int i = 0; i < segments.length; i++) {
                segments[i] = new Segment(maxVolume).jedisPool(jedisPool);
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


    }
}
