package com.rdiachenko.jlv.ui.views;

import java.io.IOException;

import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.log4j.socketappender.Server;
import com.rdiachenko.jlv.ui.preferences.PreferenceManager;

public class JlvViewController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final JlvView view;

	private Server server;

	public JlvViewController(JlvView view) {
		this.view = view;
	}

	public void startServer() {
		try {
			server = new Server(PreferenceManager.getServerPortNumber());
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					try {
						logger.debug("Starting server from Jlv view ...");
						server.start();
					} catch (IOException e) {
						logger.error("", e);
					} finally {
						stopServer();
					}
				}
			});
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	public void stopServer() {
		try {
			logger.debug("Stopping server from Jlv view ...");
			server.stop();
		} catch (IOException e) {
			logger.error("", e);
		}
	}
}
