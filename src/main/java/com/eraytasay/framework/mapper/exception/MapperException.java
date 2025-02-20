package com.eraytasay.framework.mapper.exception;

public class MapperException extends RuntimeException {
    public MapperException(String msg, Throwable cause)
    {
        super(msg, cause);
    }

    public MapperException(String msg)
    {
        super(msg);
    }

    public MapperException(Throwable cause)
    {
        super(cause);
    }
}
