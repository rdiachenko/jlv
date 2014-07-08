package com.github.rd.jlv.log4j.fileappender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rd.jlv.log4j.LogUtils;
import com.github.rd.jlv.log4j.domain.Log;
import com.google.common.eventbus.EventBus;

/**
 * TODO: add me
 *
 * @author <a href="mailto:rd.ryly@gmail.com">Ruslan Diachenko</a>
 */
public class TextLogHandler implements Runnable {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final File file;

	private final Pattern pattern;

	private final EventBus eventBus;

	public TextLogHandler(File file, Pattern pattern, EventBus eventBus) {
		this.file = file;
		this.pattern = pattern;
		this.eventBus = eventBus;
	}

	@Override
	public void run() {
		try (InputStreamReader stream = new InputStreamReader(new FileInputStream(file));
				BufferedReader reader = new BufferedReader(stream)) {
			StringBuilder logBuilder = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) {
				if (isMatching(line) && logBuilder.length() > 0) {
					send(logBuilder.toString());
					logBuilder.setLength(0);
				}
				logBuilder.append(line).append("\n");
			}

			if (isMatching(line) && logBuilder.length() > 0) {
				send(logBuilder.toString());
			}
		} catch (FileNotFoundException e) {
			logger.error("Exception occurred while opening file: " + file.getAbsolutePath(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void send(String input) {
		Log log = LogUtils.convert(input, pattern);
		eventBus.post(log);
	}

	private boolean isMatching(String input) {
		return pattern.matcher(input).find();
	}
}
