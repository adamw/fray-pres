package com.virtuslab.pres;

public class S570_What_is_Fray_for {
    // Fray assumes data-race freedom
    // * conflicting concurrent access to a shared non-volatile variable

    // It's for concurrency, not data race testing
    // * other tools exists
    // * otherwise, we would need to enter locks everywhere

    // In other words:
    // * Fray detects concurrency bugs (structural property of the code)
    // * Fray does not detect parallelism bugs (runtime property)
}
