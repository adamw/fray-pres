package com.virtuslab.pres;

import com.softwaremill.jox.Channel;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pastalab.fray.junit.junit5.FrayTestExtension;
import org.pastalab.fray.junit.junit5.annotations.ConcurrencyTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import static com.softwaremill.jox.Select.select;

@ExtendWith(FrayTestExtension.class)
public class S530_Use_cases_Jox {
    @ConcurrencyTest
    public void joxSelectTest() throws InterruptedException {
        Channel<Integer> ch1 = Channel.newBufferedChannel(4);
        Channel<Integer> ch2 = Channel.newBufferedChannel(4);

        var f1 = Fork.newNoResult(() -> select(ch1.sendClause(10)));
        var f2 = Fork.newNoResult(() -> select(ch2.sendClause(20)));
        var f3 = Fork.newWithResult(() -> select(ch1.receiveClause(), ch2.receiveClause()));

        Fork.startAll(f1, f2, f3);

        int joined = f3.join();
        if (joined == 10) {
            assert (ch2.receive() == 20);
        } else if (joined == 20) {
            assert (ch1.receive() == 10);
        } else {
            assert false;
        }

        f1.join();
        f2.join();
    }

    interface Fork<T> {
        void start();

        void interrupt();

        T join() throws InterruptedException;

        static Fork<Void> newNoResult(Callable<Void> runnable) {
            var thread =
                    new Thread(
                            () -> {
                                try {
                                    runnable.call();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            });
            return new Fork<>() {
                @Override
                public void start() {
                    thread.start();
                }

                @Override
                public void interrupt() {
                    thread.interrupt();
                }

                @Override
                public Void join() throws InterruptedException {
                    thread.join();
                    return null;
                }
            };
        }

        static <T> Fork<T> newWithResult(Callable<T> callable) {
            var result = new AtomicReference<T>();
            var thread =
                    new Thread(
                            () -> {
                                try {
                                    result.set(callable.call());
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            });

            return new Fork<>() {
                @Override
                public void start() {
                    thread.start();
                }

                @Override
                public void interrupt() {
                    thread.interrupt();
                }

                @Override
                public T join() throws InterruptedException {
                    thread.join();
                    return result.get();
                }
            };
        }

        static void startAll(Fork<?>... fork) {
            for (Fork<?> f : fork) {
                f.start();
            }
        }

        @SafeVarargs
        static <T> List<T> joinAll(Fork<T>... fork) throws InterruptedException {
            var result = new ArrayList<T>();
            for (Fork<T> f : fork) {
                result.add(f.join());
            }
            return result;
        }
    }
}
