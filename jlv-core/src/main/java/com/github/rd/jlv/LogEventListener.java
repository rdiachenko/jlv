package com.github.rd.jlv;

/**
 * The interface provides methods for adding/removing a listener which is interested in the notifications about incoming
 * logs. Log notifications are sent by EventBus to every listener which was registered.
 *
 * @author <a href="mailto:rd.ryly@gmail.com">Ruslan Diachenko</a>
 */
public interface LogEventListener {

	void addLogEventListener(Object listener);

	void removeLogEventListener(Object listener);
}
