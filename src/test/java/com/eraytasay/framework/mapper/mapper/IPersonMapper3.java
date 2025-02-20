package com.eraytasay.framework.mapper.mapper;

import com.eraytasay.framework.mapper.annotation.MapperMethod;
import com.eraytasay.framework.mapper.annotation.Mapping;
import com.eraytasay.framework.mapper.dto.Person2DTO;
import com.eraytasay.framework.mapper.entity.Person2;

public interface IPersonMapper3 {
    @MapperMethod
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "birthDate", ignore = true)
    Person2DTO toPersonDTO(Person2 person);
}
