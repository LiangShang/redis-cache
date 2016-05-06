# redis-cache

=======
## This is a cache that wraps redis using jedis lib

it satisfies that

1. cache update strategy: LRU
2. thread safe


Later it may support and manage distributed redis

it supports that

* define a maximum volume
* cache update strategy: LRU
* cache expires on write or last-read
* thread safe

## We need 2 metrics to evaluate the performance of this cache

* the throughput
* the thread-safe feature


## Usage

#### Init a Cache Instance

    Cache testedCache = Cache.newBuilder()
            .jedisPool(jedisPool)
            .maxVolume(maxValume)
            .build();

#### Use the Cache
    
    testedCache.set(key, value);
    
    testedCache.get(key);
