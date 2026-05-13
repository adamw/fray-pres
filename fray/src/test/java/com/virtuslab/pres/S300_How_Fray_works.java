package com.virtuslab.pres;

public class S300_How_Fray_works {
    // 1. instrument the JVM (Semaphore, Thread.join(),
    //    Thread.interrupt(), ...)
    //    * thin layer
    //    * optionally enabled - only after bootstrap
    // 2. instrument the application's code
    // 3. run multiple iterations with different interleavings
}
