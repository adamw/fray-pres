package com.virtuslab.pres;

public class S810_Fray_and_Lincheck {
    void _1_both() {
        /*
        🍀 model checking of concurrent code
        🍀 generating thread interleavings
        🍀 with model checking, one threads runs at a time
        💔 no virtual thread support
         */
    }

    void _2_fray() {
        /*
        ℹ️ shadow locking
        ℹ️ since 2024
        ℹ️ found multiple bugs in OSS projectts
        🍀 pluggable thread schedulers
        🍀 replay scenarios
        🍀 low-level instrumentation
        💔 needs to use instrumented JVM
        💔 assumes data-race free code
        🍀 fast
         */
    }

    void _3_lincheck() {
        /*
        ℹ️ rewriting bytecode
        ℹ️ since 2017
        ℹ️ extensively used when developing Kotlin coroutines
        🍀 model & stress testing OOTB
        🍀 generating test cases given operations,
           💔 but only for non-blocking data structures,
              or Kotlin-coroutine-based ones
        💔 not everything is instrumented
        🍀 failing interleaving rich output (variable names etc.),
           💔 but not always readable
        🍀 also detects data races
        💔️ slower
         */
    }
}
