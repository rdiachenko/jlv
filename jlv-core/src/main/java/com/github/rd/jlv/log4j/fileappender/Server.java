package com.github.rd.jlv.log4j.fileappender;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server extends Thread {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private final BlockingQueue<FileConf> logFiles = new LinkedBlockingQueue<>();

	public void addFileToProcess(File file, Pattern pattern) {
		if (file == null || !file.exists()) {
			throw new IllegalArgumentException("File " + file.getAbsolutePath() + " doesn't exist");
		}

		if (pattern == null) {
			throw new IllegalArgumentException("Pattern shouldn't be a null");
		}
		logFiles.add(new FileConf(file, pattern));
	}

	@Override
	public void run() {
		logger.debug("Server was started");

		while (Thread.currentThread().isInterrupted()) {
			try {
				logger.debug("Waiting for a new file entrance");
				FileConf fileConf = logFiles.take();
				logger.debug("File has been accepted for the processing: " + fileConf.getFile().getAbsolutePath());
				executor.execute(new TextLogHandler(fileConf.getFile(), fileConf.getPattern()));
			} catch (RejectedExecutionException e) {
				if (!executor.isShutdown()) {
					logger.warn("Files submission rejected", e);
				}
			} catch (InterruptedException e) {
				logger.warn("Failed to accept a new file: server was stopped.");
			}
		}
	}

	public void shutdown() {
		try {
			Thread.currentThread().interrupt();
			logFiles.clear();
			logger.debug("Server was stopped");
		} finally {
			try {
				executor.shutdown();

				if (!executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.NANOSECONDS)) {
					executor.shutdownNow();
				}
			} catch (InterruptedException e) {
				logger.error("InterruptedException occurred while shutting down server executor.", e);
			}
		}
	}

	private static final class FileConf {

		private final File file;

		private final Pattern pattern;

		public FileConf(File file, Pattern pattern) {
			this.file = file;
			this.pattern = pattern;
		}

		public File getFile() {
			return file;
		}

		public Pattern getPattern() {
			return pattern;
		}
	}
}
