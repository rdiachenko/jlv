package com.github.rd.jlv.server;

import com.google.common.eventbus.EventBus;

public abstract class Server implements LogEventListener {

	protected final EventBus eventBus = new EventBus();
	
	public abstract void start();
	
	public abstract void stop();
	
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
