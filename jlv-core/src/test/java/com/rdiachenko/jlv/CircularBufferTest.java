package com.rdiachenko.jlv;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CircularBufferTest {
    
    private static final int BUFFER_SIZE_ZERO = 0;
    private static final int BUFFER_SIZE_ONE = 1;

    private CircularBuffer<Log> buffer;

    @Before
    public void init() {
        buffer = new CircularBuffer<>(BUFFER_SIZE_ONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongCapacity() {
        new CircularBuffer<>(BUFFER_SIZE_ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeIndex() {
        buffer.get(-7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexOutOfBounds() {
        buffer.get(7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrivalOnEmptyBuffer() {
        buffer.get(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNull() {
        buffer.add(null);
    }

    @Test
    public void testBufferOverflow() {
        Log log1 = Log.newBuilder().message("log1").build();
        Log log2 = Log.newBuilder().message("log2").build();
        Log log3 = Log.newBuilder().message("log3").build();
        Log log4 = Log.newBuilder().message("log4").build();
        buffer.add(log1);
        buffer.add(log2);
        buffer.add(log3);
        buffer.add(log4);

        Assert.assertTrue(buffer.size() == 1);
        Assert.assertEquals(log4, buffer.get(0));

        buffer.clear();
        Assert.assertTrue(buffer.size() == 0);
    }

    @Test
    public void testToArray() {
        Log log1 = Log.newBuilder().message("log1").build();
        Log log2 = Log.newBuilder().message("log2").build();
        buffer.add(log1);
        Assert.assertArrayEquals(new Log[] { log1 }, buffer.toArray());
        buffer.clear();
        Assert.assertArrayEquals(new Log[] {}, buffer.toArray());
        buffer.add(log1);
        buffer.add(log2);
        Assert.assertArrayEquals(new Log[] { log2 }, buffer.toArray());
    }

    @Test
    public void testBufferIterator() {
        Log log = Log.newBuilder().message("log1").build();
        buffer.add(log);
        Iterator<Log> iter = buffer.iterator();

        Assert.assertTrue(buffer.size() == 1);
        Assert.assertTrue(iter.hasNext());
        Assert.assertEquals(log, iter.next());
        Assert.assertFalse(iter.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testBufferIteratorNoNext() {
        buffer.iterator().next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBufferIteratorRemove() {
        buffer.iterator().remove();
    }
}
