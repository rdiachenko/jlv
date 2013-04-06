package com.rdiachenko.jlv.log4j.ranners;

import java.io.IOException;

import com.rdiachenko.jlv.log4j.socketappender.Server;

public final class Log4jServerRunner {

	public static void main(String[] args) throws IOException {
		Server server = new Server(4445);
		try {
			server.start();
		} finally {
			server.stop();
		}
	}

	private Log4jServerRunner() {
		// it is a runner class, no reason to have a public constructor
	}
}
