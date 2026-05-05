package com.virtuslab.pres;

import org.jetbrains.lincheck.datastructures.ModelCheckingOptions;
import org.jetbrains.lincheck.datastructures.Operation;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentLinkedDeque;

public class S720_ConcurrentLinkedDeque {
    private final ConcurrentLinkedDeque<Integer> deque = new ConcurrentLinkedDeque<>();

    @Operation
    public void addFirst(int e) { deque.addFirst(e); }

    @Operation
    public void addLast(int e) { deque.addLast(e); }

    @Operation
    public Integer pollFirst() { return deque.pollFirst(); }

    @Operation
    public Integer pollLast() { return deque.pollLast(); }

    @Operation
    public Integer peekFirst() { return deque.peekFirst(); }

    @Operation
    public Integer peekLast() { return deque.peekLast(); }

    @Test
    public void modelCheckingTest() {
        new ModelCheckingOptions().check(this.getClass());
    }
}

/*
[ERROR] com.virtuslab.pres.S720_ConcurrentLinkedDeque.modelCheckingTest -- Time elapsed: 121.1 s <<< FAILURE!
org.jetbrains.lincheck.LincheckAssertionError:

= Invalid execution results =
| ------------------------------------ |
|     Thread 1     |     Thread 2      |
| ------------------------------------ |
| addLast(1): void |                   |
| ------------------------------------ |
| pollFirst(): 1   | addFirst(0): void |
|                  | peekLast(): 1     |
| ------------------------------------ |
 */
