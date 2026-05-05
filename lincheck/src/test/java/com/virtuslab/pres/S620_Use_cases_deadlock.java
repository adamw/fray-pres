package com.virtuslab.pres;

import java.util.concurrent.locks.ReentrantLock;

import org.jetbrains.lincheck.Lincheck;
import org.junit.jupiter.api.Test;

public class S620_Use_cases_deadlock {
    static class Account {
        final int id;
        final ReentrantLock lock = new ReentrantLock();
        int balance;
        Account(int id, int balance) { this.id = id; this.balance = balance; }
    }

    static void transfer(Account from, Account to, int amount) {
        from.lock.lock();
        try {
            to.lock.lock();
            try {
                from.balance -= amount;
                to.balance += amount;
            } finally { to.lock.unlock(); }
        } finally { from.lock.unlock(); }
    }

    @Test
    public void broken() {
        Lincheck.runConcurrentTest(() -> {
            var a = new Account(1, 100);
            var b = new Account(2, 100);
            Thread t1 = new Thread(() -> transfer(a, b, 10));
            Thread t2 = new Thread(() -> transfer(b, a, 10));
            t1.start(); t2.start();
            try { t1.join(); t2.join(); } catch (InterruptedException e) { throw new RuntimeException(e); }
        });
    }
}
