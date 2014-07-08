package com.github.rd.jlv.log4j.domain;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>
 * Implementation of a circular buffer with a specified capacity. Type T represents the object's type which this buffer
 * will operate with.
 * </p>
 *
 * <p>
 * If a buffer is full while adding a new element then the following algorithm takes place:
 * <ul>
 * <li>Increment the tail</li>
 * <li>Insert a new element at the tail</li>
 * <li>If the tail is equal to the head then increment the head</li>
 * </p>
 * E.g.:
 *
 * <pre>
 * <code>capacity = 3
 * buffer = [1, 2, 3]
 * buffer.add(4)
 * buffer = [2, 3, 4]</code>
 * </pre>
 *
 * @author <a href="mailto:rd.ryly@gmail.com">Ruslan Diachenko</a>
 */
public class CircularBuffer<T> implements Iterable<T> {

	private T[] buffer;

	private int head;

	private int tail;

	@SuppressWarnings("unchecked")
	public CircularBuffer(int capacity) {
		if (capacity <= 0) {
			throw new IllegalArgumentException("Capacity should be a positive number: " + capacity);
		}
		buffer = (T[]) new Object[capacity + 1];
	}

	public synchronized int size() {
		if (head < tail) {
			return tail - head;
		} else if (head > tail) {
			return buffer.length - (head - tail);
		} else {
			return 0;
		}
	}

	public synchronized void add(T item) {
		if (item == null) {
			throw new IllegalArgumentException("item is null");
		}
		buffer[tail++] = item;
		tail %= buffer.length;

		if (tail == head) {
			head = (head + 1) % buffer.length;
		}
	}

	public synchronized T get(int index) {
		if (index < 0 || index >= size()) {
			throw new IllegalArgumentException("Index is out of bounds: " + index);
		}
		return buffer[(head + index) % buffer.length];
	}

	public synchronized void clear() {
		Arrays.fill(buffer, null);
		head = 0;
		tail = 0;
	}

	@SuppressWarnings("unchecked")
	public synchronized T[] toArray() {
		T[] array;

		if (head == tail) {
			array = (T[]) new Object[] {};
		} else {
			array = (T[]) new Object[size()];

			if (head < tail) {
				System.arraycopy(buffer, head, array, 0, tail);
			} else {
				System.arraycopy(buffer, head, array, 0, buffer.length - head);
				System.arraycopy(buffer, 0, array, buffer.length - head, tail);
			}
		}
		return array;
	}

	/* Should be synchronized manually
	 *
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return new CircularBufferIterator();
	}

	@Override
	public String toString() {
		return "CircularBuffer [buffer=" + Arrays.toString(buffer) + ", head=" + head + ", tail=" + tail + "]";
	}

	private class CircularBufferIterator implements Iterator<T> {

		private int index;

		@Override
		public boolean hasNext() {
			return (head + index) % buffer.length != tail;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException("No more elements");
			}
			T item = buffer[(head + index) % buffer.length];
			++index;
			return item;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("'remove' operation is not supported");
		}
	}
}
