package com.github.rd.jlv.props;

/**
 * Event object which describes a property change. The property change is represented by property key, its old value and
 * its new value.
 * 
 * @author <a href="mailto:rd.ryly@gmail.com">Ruslan Diachenko</a>
 */
public class PropertyChangeEvent {

	private PropertyKey property;

	private Object oldValue;

	private Object newValue;

	public PropertyChangeEvent(PropertyKey property, Object oldValue, Object newValue) {
		this.property = property;
		this.oldValue = oldValue;
		this.newValue = newValue;
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
}
