package com.github.rd.jlv.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;

/**
 * Each log server should extend this abstract class. It describes the common behavior for server of any type.
 *
 * @see ServerType
 * @see LogEventListener
 *
 * @author <a href="mailto:rd.ryly@gmail.com">Ruslan Diachenko</a>
 */
public abstract class Server implements LogEventListener {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ExecutorService bossExecutor = Executors.newSingleThreadExecutor();
	private final ExecutorService workerExecutor = Executors.newFixedThreadPool(Runtime.getRuntime()
			.availableProcessors());

	protected final EventBus eventBus = new EventBus();

	protected abstract Runnable getServerProcess();

	public void start() {
		bossExecutor.execute(getServerProcess());
	}

	public void stop() {
		shutdownExecutor(workerExecutor);
		shutdownExecutor(bossExecutor);
	}

	@Override
	public void addLogEventListener(Object listener) {
		Preconditions.checkNotNull(listener, "Listener object mustn't be null.");
		eventBus.register(listener);
	}

	@Override
	public void removeLogEventListener(Object listener) {
		Preconditions.checkNotNull(listener, "Listener object mustn't be null.");
		eventBus.unregister(listener);
	}

	protected void runHandler(Runnable runnable) {
		Preconditions.checkNotNull(runnable, "Runnable handler mustn't be null.");
		try {
			workerExecutor.execute(runnable);
		} catch (RejectedExecutionException e) {
			if (!workerExecutor.isShutdown()) {
				logger.warn("Connections submission rejected.", e);
			}
		}
	}

	private void shutdownExecutor(ExecutorService executor) {
		try {
			executor.shutdown();

			if (!executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.NANOSECONDS)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			logger.error("Executor shutdown failed.", e);
		}
	}
}
