package me.liang.rediscache;

import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisTest {

    JedisPool connect() {
        return new JedisPool(); // use default jedis config
    }

    @Test
    public void testConnectionsBySimpleFunctions() {

        JedisPool pool = connect();

        try(Jedis jedis  = pool.getResource()) {

            jedis.set("foo", "bar");
            Assert.assertEquals("bar", jedis.get("foo"));
            jedis.del("foo");
            Assert.assertNull(jedis.get("foo"));

        } finally {

            pool.destroy();
        }

    }


}
