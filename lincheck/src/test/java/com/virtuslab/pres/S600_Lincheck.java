package com.virtuslab.pres;

import org.jetbrains.lincheck.Lincheck;
import org.junit.jupiter.api.Test;

public class S600_Lincheck {
    class Counter {
        int c = 0;
        void increment() {
            c++;
        }
    }

    @Test
    public void bankTest() {
        Lincheck.runConcurrentTest(() -> {
            var counter = new Counter();

            var t1 = Thread.ofPlatform().start(() -> counter.increment());
            var t2 = Thread.ofPlatform().start(() -> counter.increment());

            try {
                t1.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                t2.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            assert counter.c == 2;
        });
    }
}
