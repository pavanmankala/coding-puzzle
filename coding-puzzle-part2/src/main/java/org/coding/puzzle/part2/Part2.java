package org.coding.puzzle.part2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.coding.puzzle.Result.BooleanResult;
import org.coding.puzzle.processor.string.StringValidator;
import org.coding.puzzle.processor.string.rules.ParenthesesParseRule;

public class Part2 {
    public static void main(String[] args) {
        int totalProcessors = Runtime.getRuntime().availableProcessors();

    }

    private static class Pair<K, V> {
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

    private static class JobQueue implements Runnable {
        private final BlockingQueue<Pair<Integer, String>> queue = new LinkedBlockingQueue<Pair<Integer, String>>();
        private final StringValidator processor = new StringValidator(new ParenthesesParseRule());

        @Override
        public void run() {
            Pair<Integer, String> testString;
            try {
                while ((testString = queue.take()) != null) {
                    BooleanResult result = processor.process(testString.getValue());
                    
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
