package com.virtuslab.pres;

import java.util.concurrent.locks.ReentrantLock;

public class S110_Bug_or_not {
    // How about this one?
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
}
