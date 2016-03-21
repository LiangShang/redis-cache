package me.liang.rediscache.threadsafe;


import me.liang.rediscache.Cache;
import redis.clients.jedis.JedisPool;

/**
 * This class is used to test the thread-safe feature of this lib
 *
 * The basic idea is that we set values to the cache in the simulated multi-threaded environment.
 * And then check whether the result is the same as in the single-threaded environment.
 *
 */
public class ThreadSafeTest {

    JedisPool jedisPool = new JedisPool();  // The default is localhost redis
    long maxValume = 3000L;

    Cache testedCachec  = Cache.newBuilder()
            .jedisPool(jedisPool)
            .maxVolume(maxValume)
            .build();


}
