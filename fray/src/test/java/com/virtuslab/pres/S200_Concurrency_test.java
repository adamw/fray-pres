package com.virtuslab.pres;

import org.junit.jupiter.api.extension.ExtendWith;
import org.pastalab.fray.junit.junit5.FrayTestExtension;
import org.pastalab.fray.junit.junit5.annotations.ConcurrencyTest;

import java.util.concurrent.atomic.AtomicInteger;

@ExtendWith(FrayTestExtension.class)
public class S200_Concurrency_test {
    static class Bank {
        AtomicInteger balance = new AtomicInteger(100);

        void withdraw(int amount) {
            if (balance.get() >= amount) {
                balance.set(balance.get() - amount);
            }
        }
    }

    @ConcurrencyTest
    public void bankTest() throws InterruptedException {
        var bank = new Bank();
        Thread t1 = Thread.ofPlatform().start(() -> bank.withdraw(75));
        Thread t2 = Thread.ofPlatform().start(() -> bank.withdraw(75));
        t1.join(); t2.join();

        assert bank.balance.get() >= 0 : "balance went negative: " + bank.balance.get();
    }
}
