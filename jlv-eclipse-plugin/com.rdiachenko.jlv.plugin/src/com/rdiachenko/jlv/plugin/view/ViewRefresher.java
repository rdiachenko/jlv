package com.rdiachenko.jlv.plugin.view;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.rdiachenko.jlv.plugin.JlvConstants;
import com.rdiachenko.jlv.plugin.Operation;
import com.rdiachenko.jlv.plugin.PreferenceStoreUtils;

public class ViewRefresher {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final int EXECUTOR_TIMEOUT_MS = 5000;

    private final Operation callback;
    
    private ExecutorService executor;
    
    private volatile boolean running;

    public ViewRefresher(Operation callback) {
        Preconditions.checkNotNull(callback, "View refresher callback is null");
        this.callback = callback;
    }

    public void start() {
        if (running) {
            throw new IllegalStateException("Refresher is already in progress and can't be started");
        }
        logger.info("Starting log list view refresher");
        running = true;
        executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            while (running) {
                Display.getDefault().asyncExec(() -> callback.perform());
                try {
                    long refreshingTimeMs = PreferenceStoreUtils.getInt(JlvConstants.LOGLIST_REFRESH_TIME_MS_PREF_KEY);
                    Thread.sleep(refreshingTimeMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.error("Timer thread was interrupted", e);
                }
            }
        });
        logger.info("Log list view refresher started");
    }

    public void stop() {
        logger.info("Stopping log list view refresher");
        running = false;
        shutdownExecutor(executor);
        logger.info("Log list view refresher stopped");
    }

    private void shutdownExecutor(ExecutorService executor) {
        if (executor != null && !executor.isShutdown()) {
            logger.info("Shutting down executor {}", executor);
            try {
                executor.shutdown();

                if (!executor.awaitTermination(EXECUTOR_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                    executor.shutdownNow();
                }
                logger.info("Executor shut down: {}", executor.isTerminated());
            } catch (Exception e) {
                logger.error("Failed to shutdown executor {}", executor, e);
            }
        }
    }
}
