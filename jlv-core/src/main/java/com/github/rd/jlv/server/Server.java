package com.github.rd.jlv.server;

/**
 * The interface provides methods for running and stopping a server which implements it.
 *
 * @author <a href="mailto:rd.ryly@gmail.com">Ruslan Diachenko</a>
 */
public interface Server extends Runnable {

	void shutdown();
}
