package me.liang.rediscache.threadsafe;


import me.liang.rediscache.Cache;
import me.liang.rediscache.Segment;
import org.junit.Test;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This class is used to test the thread-safe feature of this lib
 *
 * The basic idea is that we set values to the cache in the simulated multi-threaded environment.
 * And then check whether the result is the same as in the single-threaded environment.
 *
 */
public class ThreadSafeTest {

    JedisPool jedisPool = new JedisPool();  // The default is localhost redis
    long maxValume = 10L;

    Cache testedCache = Cache.newBuilder()
            .jedisPool(jedisPool)
            .maxVolume(maxValume)
            .expireAfterAccess(1, TimeUnit.SECONDS)
            .build();

    @Test
    public void testThreadSafe() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i+=50) {
            List<String> keys = new ArrayList<>();
            for (int j = 0 ; j < 50; j++) {
                keys.add("test" + String.valueOf(i + j));
            }
            System.out.println("create task with keys " + keys);
            executor.submit(new WriteToCacheTask(keys, "value"));
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            Segment[] segments = testedCache.getSegments();
            for (int i = 0; i< segments.length; i++ ) {
                Segment segment = segments[i];
                System.out.println(i +": "+segment.showCurrentStatus());
            }
            System.out.println("*******************");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Final status");
        Segment[] segments = testedCache.getSegments();
        for (Segment segment : segments) {
            System.out.println(segment.showCurrentStatus());
        }

    }

    class WriteToCacheTask implements Callable<Void> {
        List<String> keys;
        String value;

        public WriteToCacheTask(List<String> keys, String value) {
            this.keys = keys;
            this.value = value;
        }

        @Override
        public Void call() throws Exception {
            for (String key : keys) {
                testedCache.set(key, value);
            }
            return null;
        }
    }


}
