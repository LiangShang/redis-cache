package me.liang.rediscache;

import com.google.common.base.Objects;
import org.junit.Assert;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.RedissonClient;
import org.redisson.core.RAtomicLong;
import org.redisson.core.RBucket;
import org.redisson.core.RMapCache;

import java.io.Serializable;

public class RedissonTest {

    @Test
    public void testFirstRedissonProgram() {

        RedissonClient client = Redisson.create();
        RAtomicLong atomicLong = client.getAtomicLong("first_long");
        System.out.println(atomicLong.addAndGet(10));
        atomicLong.delete();

        RMapCache rMapCache = client.getMapCache("map_cache");

    }

    @Test
    public void testRedissonObjectSerilizeAndDeser() {

        SimpleObject so = new SimpleObject();
        so.doubleAttr = 1.0d;
        so.intAttr = 2;
        so.stringAttr = "hello";

        System.out.println(so);

        RedissonClient client = Redisson.create();
        RBucket<SimpleObject> rBucket = client.getBucket("simple_object");
        rBucket.compareAndSet(null, so);
        Assert.assertFalse(rBucket.compareAndSet(null, so));


        Assert.assertEquals(so, rBucket.get());


    }

    // INNER CLASS MUST BE DEFINED AS static class for the sake of jackson deserialization
    static class SimpleObject implements Serializable{
        int intAttr;
        double doubleAttr;
        String stringAttr;

        @Override
        public String toString() {
            return com.google.common.base.MoreObjects.toStringHelper(this)
                    .add("doubleAttr", doubleAttr)
                    .add("intAttr", intAttr)
                    .add("stringAttr", stringAttr)
                    .toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SimpleObject)) return false;
            SimpleObject that = (SimpleObject) o;
            return intAttr == that.intAttr &&
                    Double.compare(that.doubleAttr, doubleAttr) == 0 &&
                    Objects.equal(stringAttr, that.stringAttr);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(intAttr, doubleAttr, stringAttr);
        }
    }


}
