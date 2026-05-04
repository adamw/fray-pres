package com.virtuslab.pres;

import org.junit.jupiter.api.extension.ExtendWith;
import org.pastalab.fray.junit.junit5.FrayTestExtension;
import org.pastalab.fray.junit.junit5.annotations.ConcurrencyTest;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ExtendWith(FrayTestExtension.class)
public class S510_Use_cases_cache {
    static final class Resource {
        final int id;
        Resource(int id) { this.id = id; }
    }

    static class Cache {
        final ConcurrentHashMap<String, Resource> map = new ConcurrentHashMap<>();
        final AtomicInteger idGenerator = new AtomicInteger();

        Resource get(String key) {
            Resource r = map.get(key);
            if (r == null) {
                r = new Resource(idGenerator.incrementAndGet());
                map.put(key, r);
            }
            return r;
        }
    }

    @ConcurrencyTest
    public void cacheTest() throws InterruptedException {
        var cache = new Cache();
        var refs = new Resource[2];
        Thread t1 = Thread.ofPlatform().start(() -> refs[0] = cache.get("k"));
        Thread t2 = Thread.ofPlatform().start(() -> refs[1] = cache.get("k"));
        t1.join(); t2.join();

        assert refs[0] == refs[1] : "two distinct resources for the same key: "
                + refs[0].id + " vs " + refs[1].id;
    }
}
