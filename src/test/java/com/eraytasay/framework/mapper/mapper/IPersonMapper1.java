package com.eraytasay.framework.mapper.mapper;

import com.eraytasay.framework.mapper.annotation.MapperMethod;
import com.eraytasay.framework.mapper.dto.Person1DTO;
import com.eraytasay.framework.mapper.entity.Person1;

public interface IPersonMapper1 {
    @MapperMethod
    Person1DTO toPersonDTO(Person1 person);
}
