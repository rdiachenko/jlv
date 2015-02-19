package com.github.rd.jlv.server;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * TODO: add doc
 *
 * @author <a href="mailto:rd.ryly@gmail.com">Ruslan Diachenko</a>
 */
public class FileLogServer extends Server {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final BlockingQueue<FilePatternPair> files = new LinkedBlockingQueue<>();

	public void addFileToProcess(File file, Pattern pattern) {
		Preconditions.checkNotNull(file, "File mustn't be null.");
		Preconditions.checkArgument(file.exists(), "File %s doesn't exist.", file);
		Preconditions.checkNotNull(pattern, "Pattern mustn't be null.");
		files.add(new FilePatternPair(file, pattern));
	}

	@Override
	public void stop() {
		try {
			files.clear();
			logger.debug("Files cleared.");
		} finally {
			super.stop();
		}
	}
	
	@Override
	public Runnable getServerProcess() {
		return new Runnable() {
			@Override
			public void run() {
				while (Thread.currentThread().isInterrupted()) {
					try {
						logger.debug("Waiting for a new file entrance.");
						FilePatternPair pair = files.take();
						logger.debug("File has been accepted for the processing: {}", pair.getFile());
				        runHandler(new FileLogHandler(pair.getFile(), pair.getPattern(), eventBus));
					} catch (InterruptedException e) {
						logger.warn("Failed to accept a new file: server was stopped.");
					}
				}
			}
		};
	}
	
	private static final class FilePatternPair {

		private final File file;

		private final Pattern pattern;

		public FilePatternPair(File file, Pattern pattern) {
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
