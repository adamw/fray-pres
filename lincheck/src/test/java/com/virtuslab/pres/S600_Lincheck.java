package com.virtuslab.pres;

import org.jetbrains.lincheck.Lincheck;
import org.junit.jupiter.api.Test;

public class S600_Lincheck {
    static class Bank {
        int balance = 100;

        void withdraw(int amount) {
            if (balance >= amount) {
                balance = balance - amount;
            }
        }
    }

    @Test
    public void bankTest() {
        Lincheck.runConcurrentTest(() -> {
            var bank = new Bank();
            Thread t1 = new Thread(() -> bank.withdraw(75));
            Thread t2 = new Thread(() -> bank.withdraw(75));
            t1.start(); t2.start();
            try { t1.join(); t2.join(); } catch (InterruptedException e) { throw new RuntimeException(e); }

            assert bank.balance >= 0 : "balance went negative: " + bank.balance;
        });
    }
}
