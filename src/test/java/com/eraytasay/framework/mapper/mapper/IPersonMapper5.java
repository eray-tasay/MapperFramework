package com.eraytasay.framework.mapper.mapper;

import com.eraytasay.framework.mapper.annotation.FieldMapperMethod;
import com.eraytasay.framework.mapper.annotation.MapperMethod;
import com.eraytasay.framework.mapper.annotation.Mapping;
import com.eraytasay.framework.mapper.dto.Person1DTO;
import com.eraytasay.framework.mapper.entity.Person1;

public interface IPersonMapper5 {
    @MapperMethod
    @Mapping(target = "lastName", source = "lastName", fieldMapper = "toUpperCase")
    Person1DTO toPersonDTO(Person1 person);

    @FieldMapperMethod
    static String toUpperCase(String str)
    {
        return str.toUpperCase();
    }
}
