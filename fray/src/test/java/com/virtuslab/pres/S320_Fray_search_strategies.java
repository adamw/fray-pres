package com.virtuslab.pres;

public class S320_Fray_search_strategies {
    // Schedules are created dynamically:
    // * the strategy gets a set of enabled threads (which can make progress)

    // Evaluated strategies:
    // * depth-first search
    // * iterative context bounding
    // * iterative delay bounding
    // * probabilistic concurrency testing (PCT)
    // * coverage-guided Maple algorithm
    // * partial order sampling (POS)
    // * selectively uniform random walk (SURW)

    // What works best:
    // * POS: randomize priorities for all threads, pick the one
    //        with the highest priority; if this is the highest-priority
    //        thread, each d step demote it to lowest priority
    // * PCT: randomize priorities for all threads, pick the one with
    //        the highest priority reassign random priorities for threads
    //        that are competing with the scheduled thread
    // * random: pick threads uniformly at random
    // * SURW: pick a thread basing on the number of "interesting events"
    //         remaining per thread
}
