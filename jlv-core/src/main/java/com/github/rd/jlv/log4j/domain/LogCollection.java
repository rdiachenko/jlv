package com.github.rd.jlv.log4j.domain;

import java.util.Arrays;

public class LogCollection {

	private final Log[] logs;

	private final int capacity;

	private int head = -1;

	private int tail = -1;

	public LogCollection(int capacity) {
		if (capacity <= 0) {
			throw new IllegalArgumentException("Collection capacity should be a positive number: " + capacity);
		}
		this.capacity = capacity;
		logs = new Log[capacity];
	}

	public synchronized void add(Log log) {
		if (this.size() == 0) {
			head++;
			tail++;
			logs[head] = log;
		} else {
			tail++;

			if (tail == capacity) {
				tail = 0;
			}
			logs[tail] = log;

			if (head + 1 == capacity) {
				head = 0;
			} else if (head == tail) {
				head++;
			}
		}
	}

	public synchronized Log get(int index) {
		int size = this.size();

		if (size == 0 || index >= size || index < 0 || index >= capacity) {
			throw new IllegalArgumentException("Array index is out of bounds: " + index);
		}
		int position = (head + index) % capacity;
		return logs[position];
	}

	public synchronized int size() {
		if (tail < 0 || head < 0) {
			return 0;
		}
		if (head == tail) {
			return 1;
		} else if (head < tail) {
			return tail - head + 1;
		} else {
			int afterHead = capacity - head;
			int beforeTail = tail + 1;
			return afterHead + beforeTail;
		}
	}

	public synchronized Log[] toArray() {
		if (this.size() == 0) {
			return new Log[] {};
		} else if (head == tail) {
			return new Log[] { logs[head] };
		} else if (head < tail) {
			Log[] array = new Log[this.size()];

			for (int i = 0; i < array.length; i++) {
				array[i] = logs[i + head];
			}
			return array;
		} else {
			Log[] array = new Log[this.size()];
			int index = 0;

			for (int i = head; i < capacity; i++) {
				array[index++] = logs[i];
			}

			for (int i = 0; i <= tail; i++) {
				array[index++] = logs[i];
			}
			return array;
		}
	}

	public synchronized void clear() {
		head = -1;
		tail = -1;

		for (int i = 0; i < capacity; i++) {
			logs[i] = null;
		}
	}

	@Override
	public String toString() {
		return "LogCollection [logs=" + Arrays.toString(logs) + "]";
	}
}
