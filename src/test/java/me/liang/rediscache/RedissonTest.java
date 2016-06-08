package me.liang.rediscache;

import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.RedissonClient;
import org.redisson.core.RAtomicLong;
import org.redisson.core.RMapCache;

public class RedissonTest {

    @Test
    public void testFirstRedissonProgram() {

        RedissonClient client = Redisson.create();
        RAtomicLong atomicLong = client.getAtomicLong("first_long");
        System.out.println(atomicLong.addAndGet(10));
        atomicLong.delete();

        RMapCache rMapCache = client.getMapCache("map_cache");

    }


}
