package com.rdiachenko.jlv.converter;

import com.rdiachenko.jlv.Log;

public interface Converter<T> {
    
    public Log convert(T le);
}
