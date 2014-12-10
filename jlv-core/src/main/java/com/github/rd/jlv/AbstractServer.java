package com.github.rd.jlv;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

/**
 * The class contains a common logic for all the servers which extend it.
 *
 * @author <a href="mailto:rd.ryly@gmail.com">Ruslan Diachenko</a>
 */
public abstract class AbstractServer implements Runnable, LogEventListener {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private final EventBus eventBus = new EventBus();

	public EventBus getEventBus() {
		return eventBus;
	}

	public void execute(Runnable runnable) {
		if (runnable != null) {
			try {
				executor.execute(runnable);
			} catch (RejectedExecutionException e) {
				if (!executor.isShutdown()) {
					logger.warn("Connections submission rejected", e);
				}
			}
		}
	}

	public void shutdown() {
		try {
			executor.shutdown();

			if (!executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.NANOSECONDS)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			logger.error("InterruptedException occurred while shutting down server executor.", e);
		}
	}

	@Override
	public void addLogEventListener(Object listener) {
		if (listener != null) {
			eventBus.register(listener);
		}
	}

	@Override
	public void removeLogEventListener(Object listener) {
		if (listener != null) {
			eventBus.unregister(listener);
		}
	}
}
