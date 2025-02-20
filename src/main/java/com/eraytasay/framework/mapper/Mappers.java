package com.eraytasay.framework.mapper;

public class Mappers {
    public static Mapper getMapper(Class<?> cls)
    {
        return new Mapper(cls);
    }
}
