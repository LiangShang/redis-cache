package me.liang.rediscache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.Objects;

public class JedisUtil {

    // TODO NOT FINISHED
    public boolean casSet_NOT_FINISHED(JedisPool jedisPool, String key, String former, String changed) {

        Jedis jedis = jedisPool.getResource();
        jedis.watch(key);
        if (!Objects.equals(former, jedis.get(key))) return false;
        Transaction transaction = jedis.multi();
        transaction.set(key, changed);
        transaction.exec();


        return true;
    }

}
