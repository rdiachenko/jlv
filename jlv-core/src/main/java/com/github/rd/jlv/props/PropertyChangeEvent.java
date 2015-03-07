package com.github.rd.jlv.props;

/**
 * Event object which describes a property change.
 *
 * @author <a href="mailto:rd.ryly@gmail.com">Ruslan Diachenko</a>
 */
public class PropertyChangeEvent {

	private PropertyKey property;

	private Object oldValue;

	private Object newValue;

	private EventScope scope;

	public PropertyChangeEvent(PropertyKey property, Object oldValue, Object newValue, EventScope scope) {
		this.property = property;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.scope = scope;
	}

	public PropertyKey getProperty() {
		return property;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}

	public EventScope getScope() {
		return scope;
	}
}
