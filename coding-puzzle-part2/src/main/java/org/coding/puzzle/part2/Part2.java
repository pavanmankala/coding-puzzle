package org.coding.puzzle.part2;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

import org.coding.puzzle.Processor;
import org.coding.puzzle.Result;
import org.coding.puzzle.Result.BooleanResult;
import org.coding.puzzle.part1.Part1;
import org.coding.puzzle.processor.string.StringValidator;

/**
 * The Part2 extends Part1 in a sense that it add multi-threading nature for
 * Part1.
 * 
 * <pre>
 * HOW TO RUN:
 * ===========
 * java -jar &lt;jar_file_name_incl_path&gt; [-n &lt;numberOfLinesToBeRead&gt;] [-f &lt;file&gt;]
 * </pre>
 * 
 * <ul>
 * <li>If both the optional parameters are not given, the the program will run
 * infinitely until it is terminated by Ctrl-C</li>
 * 
 * <li>If both the arguments are given and are valid, only the given number of
 * lines (value of -n parameter) are processed from file</li>
 * 
 * <li>If only -n parameter is specified, then the given number of lines are
 * processed from cmd-line (stdin)</li>
 * 
 * <li>If only -f parameter is specified, all the lines from the file are
 * processed</li>
 * </ul>
 * <p>
 * See also: {@link Part1}
 * 
 * @author p.mankala
 */
public class Part2 extends Part1 {
    /**
     * Job execution queues, these are given jobs in a round-robin fashion
     */
    private final JobQueue<Integer, String, Boolean, BooleanResult, StringValidator>[] execQueues;

    /**
     * The executor service for request processing threads
     */
    private final ExecutorService service;

    /**
     * The result queue in which the results are thrown in the
     */
    private final BlockingQueue<Pair<Pair<Integer, String>, BooleanResult>> resultQueue = new LinkedBlockingQueue<>();

    /**
     * For tracking #requests and #results
     */
    private volatile int noOfResults, noOfRequests;

    /**
     * ResultProcessor runnable
     */
    private final ResultProcessor resultProcessor = new ResultProcessor();

    /**
     * A latch for shutdown hook
     */
    private final CountDownLatch reqShutdownLatch, resultShutdownLatch;

    /**
     * Create Part2 execution object
     * 
     * @param args
     *            cmd line args
     */
    @SuppressWarnings("unchecked")
    public Part2(String[] args, PrintStream out, PrintStream err) {
        super(args, out, err);
        // create execution queues, as many as processors
        execQueues = new JobQueue[Runtime.getRuntime().availableProcessors()];
        reqShutdownLatch = new CountDownLatch(execQueues.length);
        resultShutdownLatch = new CountDownLatch(1);
        service = Executors.newFixedThreadPool(execQueues.length, new ThreadFactory() {
            private int number;

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "string-validity-processor-" + number++);

                t.setDaemon(true);
                return t;
            }
        });

        // Create Result reader thread
        final Thread resultReader = new Thread(resultProcessor, "result-reader");

        resultReader.setDaemon(true);

        // Add a shutdown hook to print all the buffered results
        Runtime.getRuntime().addShutdownHook(new Thread("graceful-shutdown-hook") {
            @Override
            public void run() {
                // stop request queues
                service.shutdownNow();

                // wait for leftover processing in request queues
                try {
                    reqShutdownLatch.await();
                } catch (InterruptedException e) {
                }

                // interrupt the result reader
                resultReader.interrupt();

                // wait for leftover processing in result queue
                try {
                    resultShutdownLatch.await();
                } catch (InterruptedException e) {
                }
            }
        });

        // ----- start all threads -----

        // for 1st queue use the validator from parent class
        execQueues[0] = new JobQueue<>(validator, resultQueue, reqShutdownLatch);
        service.submit(execQueues[0]);

        for (int i = 1; i < execQueues.length; i++) {
            execQueues[i] = new JobQueue<>(new StringValidator(getBeginRule()), resultQueue,
                    reqShutdownLatch);
            service.submit(execQueues[i]);
        }

        resultReader.start();
    }

    @Override
    public void execute() {
        super.execute();

        while (noOfRequests != noOfResults) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                exit(OTHER);
            }
        }
    }

    @Override
    protected void evalLine(int lineNo, String line) {
        Pair<Integer, String> pair = new Pair<Integer, String>(lineNo, line);

        if (execQueues[lineNo % execQueues.length].offer(pair)) {
            noOfRequests++;
        }
    }

    public static void main(String[] args) {
        new Part2(args, System.out, System.err).execute();
        exit(SUCCESS);
    }

    /**
     * A result processing runnable
     * 
     * @author p.mankala
     *
     */
    private class ResultProcessor implements Runnable {
        @Override
        public void run() {
            Pair<Pair<Integer, String>, BooleanResult> reqResultPair = null;
            try {
                // check after *taking*, for thread interrupted condition
                while ((reqResultPair = resultQueue.take()) != null && !Thread.interrupted()) {
                    display(reqResultPair);
                    reqResultPair = null;
                }
            } catch (InterruptedException e) {
            }

            if (reqResultPair != null) {
                display(reqResultPair);
            }

            // process leftovers in the queue
            for (Pair<Pair<Integer, String>, BooleanResult> r : getLeftOvers(resultQueue)) {
                display(r);
            }

            resultShutdownLatch.countDown();
        }

        public void display(Pair<Pair<Integer, String>, BooleanResult> reqResultPair) {
            Pair<Integer, String> request = reqResultPair.getKey();
            BooleanResult result = reqResultPair.getValue();

            out.println(request.getKey() + ":" + (result.resultValue() == true ? "True" : "False"));
            noOfResults++;
        }
    }

    static <T> Collection<T> getLeftOvers(BlockingQueue<T> bq) {
        List<T> leftOvers = new ArrayList<>(bq.size());

        bq.drainTo(leftOvers);

        return leftOvers;
    }
}

class Pair<K, V> {
    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}

class JobQueue<PK, I, RV, R extends Result<RV>, P extends Processor<I, RV, R>> implements Runnable {
    private final BlockingQueue<Pair<PK, I>> requestQ = new LinkedBlockingQueue<Pair<PK, I>>();
    private final BlockingQueue<Pair<Pair<PK, I>, R>> resultQ;
    private final P processor;
    private volatile boolean processingStopped = false;
    private final CountDownLatch shutdownLatch;

    public JobQueue(P processor, BlockingQueue<Pair<Pair<PK, I>, R>> resultQ, CountDownLatch shutdownLatch) {
        this.processor = processor;
        this.resultQ = resultQ;
        this.shutdownLatch = shutdownLatch;
    }

    @Override
    public void run() {
        Pair<PK, I> request = null;

        try {
            // check after *taking*, for thread interrupted condition
            while ((request = requestQ.take()) != null && !Thread.interrupted()) {
                processReq(request);
                request = null;
            }
        } catch (InterruptedException e) {
        } finally {
            // Flag this queue that it does not accept any more requests
            processingStopped = true;
        }

        if (request != null) {
            processReq(request);
        }

        // process leftovers in the queue
        for (Pair<PK, I> r : Part2.getLeftOvers(requestQ)) {
            processReq(r);
        }

        // Countdown the latch
        shutdownLatch.countDown();
    }

    public boolean offer(Pair<PK, I> pair) {
        if (!processingStopped) {
            requestQ.offer(pair);
            return true;
        }

        return false;
    }

    public void processReq(Pair<PK, I> request) {
        // process request
        R result = processor.process(request.getValue());

        // add result to resultQ
        resultQ.offer(new Pair<Pair<PK, I>, R>(request, result));
    }
}