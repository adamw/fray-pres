package com.virtuslab.pres;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class S310_How_Fray_works {
    static ThreadLocal<Integer> threadId = new ThreadLocal<>();

    static class Bank {
        AtomicInteger balance = new AtomicInteger(100);

        void withdraw(int amount) {
            Fray.lock_balance[threadId.get()].lock();
            var b1 = balance.get();
            Fray.lock_balance[threadId.get()].unlock();

            if (b1 >= amount) {
                Fray.lock_balance[threadId.get()].lock();
                var b2 = balance.get();
                Fray.lock_balance[threadId.get()].unlock();

                Fray.lock_balance[threadId.get()].lock();
                balance.set(b2 - amount);
                Fray.lock_balance[threadId.get()].unlock();
            }
        }
    }

    static void bankTest () {
        var bank = new S200_Concurrency_test.Bank();
        Thread t1 = Thread.ofPlatform().start(() -> {
            threadId.set(0);
            Fray.lock_thread_t1.lock();
            bank.withdraw(75);
            Fray.lock_thread_t1.unlock();
        });
        Thread t2 = Thread.ofPlatform().start(() -> {
            threadId.set(1);
            Fray.lock_thread_t2.lock();
            bank.withdraw(75);
            Fray.lock_thread_t2.unlock();
        });
    }

    // Shadow locking
    // 1. only one application thread runs at any given time
    // 2. Fray controls the order in which threads interleave
    static class Fray {
        static ReentrantLock lock_thread_t1 = new ReentrantLock();
        static ReentrantLock lock_thread_t2 = new ReentrantLock();
        static ReentrantLock[] lock_balance =  { new ReentrantLock(), new ReentrantLock() };

        void main() {
            // acquire all locks
            lock_thread_t1.lock();
            lock_thread_t2.lock();
            lock_balance[0].lock();
            lock_balance[1].lock();

            // start the code
            Thread.ofPlatform().start(() -> bankTest());

            for (int i=0; i<1000; i++) {
                // Generate thread interleaving
                // When no threads are running:
                // - pick a thread from the interleaving
                // - release the lock it's waiting on
                // - if there's no thread waiting on a releasable lock -> deadlock
                // - re-aquire the lock
            }
        }
    }
}
