package com.github.rd.jlv.fileappender;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rd.jlv.AbstractServer;

/**
 * TODO: add doc
 *
 * @author <a href="mailto:rd.ryly@gmail.com">Ruslan Diachenko</a>
 */
public class FileLogServer extends AbstractServer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final BlockingQueue<Configuration> logFiles = new LinkedBlockingQueue<>();

	public void addFileToProcess(File file, Pattern pattern) {
		if (file == null || !file.exists()) {
			throw new IllegalArgumentException("File " + file.getAbsolutePath() + " doesn't exist");
		}

		if (pattern == null) {
			throw new IllegalArgumentException("Pattern shouldn't be a null");
		}
		logFiles.add(new Configuration(file, pattern));
	}

	@Override
	public void run() {
		logger.debug("Server was started");

		while (Thread.currentThread().isInterrupted()) {
			try {
				logger.debug("Waiting for a new file entrance");
				Configuration config = logFiles.take();
				logger.debug("File has been accepted for the processing: " + config.getFile().getAbsolutePath());
				execute(new TextLogHandler(config.getFile(), config.getPattern(), getEventBus()));
			} catch (InterruptedException e) {
				logger.warn("Failed to accept a new file: server was stopped.");
			}
		}
	}

	@Override
	public void shutdown() {
		try {
			Thread.currentThread().interrupt();
			logFiles.clear();
			logger.debug("Server was stopped");
		} finally {
			super.shutdown();
		}
	}

	private static final class Configuration {

		private final File file;

		private final Pattern pattern;

		public Configuration(File file, Pattern pattern) {
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
