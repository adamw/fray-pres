package com.virtuslab.pres;

import org.jetbrains.lincheck.datastructures.IntGen;
import org.jetbrains.lincheck.datastructures.ModelCheckingOptions;
import org.jetbrains.lincheck.datastructures.Operation;
import org.jetbrains.lincheck.datastructures.Param;
import org.jetbrains.lincheck.datastructures.StressOptions;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class S740_Other_Lincheck_features {
    void _1_configuring_strategies() {
        new StressOptions() // Stress testing options:
                .actorsBefore(2) // Number of operations before the parallel part
                .threads(2) // Number of threads in the parallel part
                .actorsPerThread(2) // Number of operations in each thread of the parallel part
                .actorsAfter(1) // Number of operations after the parallel part
                .iterations(100) // Generate 100 random concurrent scenarios
                .invocationsPerIteration(1000);
    }

    void _2_operation_arguments() {
        @Param(name = "key", gen = IntGen.class, conf = "1:2")
        class MultiMapTest {
            private final ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();

            @Operation
            public Integer put(@Param(name = "key") int key, int value) {
                return map.put(key, value);
            }

            @Operation
            public Integer get(@Param(name = "key") int key) {
                return map.get(key);
            }
        }

        new StressOptions().check(MultiMapTest.class);
    }

    void _3_progress_guarantees() {
        class ConcurrentHashMapTest {
            private final ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();

            @Operation
            public Integer put(int key, int value) { return map.put(key, value); }
        }

        new ModelCheckingOptions()
                .actorsBefore(1)
                .actorsPerThread(1)
                .actorsAfter(0)
                .checkObstructionFreedom(true) // fail if any operation can be blocked indefinitely
                .check(ConcurrentHashMapTest.class);
    }

    void _4_sequential_specification() {
        class ConcurrentLinkedQueueTest {
            private final ConcurrentLinkedQueue<Integer> q = new ConcurrentLinkedQueue<>();

            @Operation
            public boolean add(int value) { return q.add(value); }

            @Operation
            public Integer poll() { return q.poll(); }
        }

        // Trivially-correct sequential implementation, used as the linearizability oracle.
        class SequentialQueue {
            private final LinkedList<Integer> q = new LinkedList<>();
            public boolean add(int x) { return q.add(x); }
            public Integer poll() { return q.poll(); }
        }

        new StressOptions()
                .sequentialSpecification(SequentialQueue.class)
                .check(ConcurrentLinkedQueueTest.class);
    }
}
