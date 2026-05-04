package com.virtuslab.pres;

import java.util.concurrent.atomic.AtomicInteger;

public class S220_What_is_wrong {
    static class Bank {
        AtomicInteger balance = new AtomicInteger(100);

        void withdraw(int amount) {
            if (balance.get() >= amount) {
                balance.set(balance.get() - amount);
            }
        }
    }

    // Thread t1 = Thread.ofPlatform().start(() -> bank.withdraw(75));
    // Thread t2 = Thread.ofPlatform().start(() -> bank.withdraw(75));

    /*
    +------+----------------------------+----------------------------+-----------------+
    | time | t1                         | t2                         |                 |
    +------+----------------------------+----------------------------+-----------------+
    | 1    | if (balance >= amount)     |                            | true            |
    | 2    |                            | if (balance >= amount)     | still true!     |
    | 3    | balance = balance - amount |                            | balance is 25   |
    | 4    |                            | balance = balance - amount | balance is -50! |
    +------+----------------------------+----------------------------+-----------------+
     */
}
