package com.virtuslab.pres;

import org.jetbrains.lincheck.datastructures.ModelCheckingOptions;
import org.jetbrains.lincheck.datastructures.Operation;
import org.jetbrains.lincheck.datastructures.StressOptions;
import org.junit.jupiter.api.Test;

public class S700_Data_structures {
    static class Bank {
        private volatile int balance = 100;

        public void deposit(int amount) {
            balance = balance + amount;
        }

        public boolean withdraw(int amount) {
            if (balance >= amount) {
                balance = balance - amount;
                return true;
            }
            return false;
        }

        public int balance() {
            return balance;
        }
    }

    private final Bank bank = new Bank();

    @Operation
    public void deposit(int amount) { bank.deposit(amount); }

    @Operation
    public boolean withdraw(int amount) { return bank.withdraw(amount); }

    @Operation
    public int balance() { return bank.balance(); }

    @Test
    public void modelCheckingTest() {
        new ModelCheckingOptions().check(this.getClass());
        //new StressOptions().check(this.getClass());
    }
}
