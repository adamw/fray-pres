package com.virtuslab.pres;

import org.junit.jupiter.api.extension.ExtendWith;
import org.pastalab.fray.junit.junit5.FrayTestExtension;
import org.pastalab.fray.junit.junit5.annotations.ConcurrencyTest;

@ExtendWith(FrayTestExtension.class)
public class S560_Non_use_cases_data_race {
    static class Bank {
        int balance = 100;

        void withdraw(int amount) {
            if (balance >= amount) {
                balance = balance - amount;
            }
        }
    }

    @ConcurrencyTest
    public void bankTest() throws InterruptedException {
        var bank = new Bank();
        Thread t1 = Thread.ofPlatform().start(() -> bank.withdraw(75));
        Thread t2 = Thread.ofPlatform().start(() -> bank.withdraw(75));
        t1.join(); t2.join();

        assert bank.balance >= 0 : "balance went negative: " + bank.balance;
    }
}
