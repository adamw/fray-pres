package com.virtuslab.pres;

import java.util.concurrent.atomic.AtomicInteger;

public class S100_Bug_or_not {
    // Is there anything wrong with this code?
    static class Bank {
        AtomicInteger balance = new AtomicInteger(100);

        void withdraw(int amount) {
            if (balance.get() >= amount) {
                balance.set(balance.get() - amount);
            }
        }
    }
}
