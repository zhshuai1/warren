package com.zebrait.processor;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Log4j2
public abstract class AbstractProcessor {
    protected List<? extends Object> objects;

    public abstract void initialize();

    public void run() {
        initialize();
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        try {
            Map<Object, Integer> failed = new HashMap<>();
            Queue<Pair<Object, Future>> resultFutures = new LinkedList<>();
            for (Object param : objects) {
                Future result = executorService.submit(() -> process(param));
                resultFutures.add(ImmutablePair.of(param, result));
            }
            while (resultFutures.size() > 0) {
                Pair<Object, Future> result = resultFutures.poll();
                Object object = result.getLeft();
                Future future = result.getRight();
                try {
                    future.get();
                } catch (Exception e) {
                    failed.putIfAbsent(object, 1);
                    int count = failed.get(object);
                    failed.put(object, count + 1);
                    if (failed.get(object) <= 5) {
                        log.warn("Error occurred when handling task {} for {} times, will have another try...", object,
                                count, e);
                        resultFutures.offer(ImmutablePair.of(object, executorService.submit(() -> process(object))));
                    } else {
                        log.error("Failed 5 times for the task {}, will no longer try.", object);
                    }

                }
            }
        } finally {
            executorService.shutdown();
        }
    }

    public abstract void process(Object param);
}
