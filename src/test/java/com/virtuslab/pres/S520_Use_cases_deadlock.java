package com.virtuslab.pres;

import java.util.concurrent.locks.ReentrantLock;

import org.junit.jupiter.api.extension.ExtendWith;
import org.pastalab.fray.junit.junit5.FrayTestExtension;
import org.pastalab.fray.junit.junit5.annotations.ConcurrencyTest;

@ExtendWith(FrayTestExtension.class)
public class S520_Use_cases_deadlock {
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

    @ConcurrencyTest
    public void broken() throws InterruptedException {
        var a = new Account(1, 100);
        var b = new Account(2, 100);
        Thread t1 = Thread.ofPlatform().start(() -> transfer(a, b, 10));
        Thread t2 = Thread.ofPlatform().start(() -> transfer(b, a, 10));
        t1.join(); t2.join();
    }
}
