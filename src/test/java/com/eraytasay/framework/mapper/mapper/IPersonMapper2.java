package com.eraytasay.framework.mapper.mapper;

import com.eraytasay.framework.mapper.annotation.MapperMethod;
import com.eraytasay.framework.mapper.annotation.Mapping;
import com.eraytasay.framework.mapper.dto.Person2DTO;
import com.eraytasay.framework.mapper.entity.Person2;

public interface IPersonMapper2 {
    @MapperMethod
    @Mapping(target = "lastName", source = "familyName")
    @Mapping(target = "birthDate", source = "birth")
    Person2DTO toPersonDTO(Person2 person);
}
